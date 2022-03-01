# EventBus

> https://github.com/greenrobot/EventBus

## EventBus 简单使用

1. 构建事件

```
public static class MessageEvent {
    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
```

2. 构建事件处理器

```
@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessageEvent(MessageEvent event) {
    mTextView.setText(event.message);
}
```

3. 注册事件

```
EventBus.getDefault().register(this);
```

4. 发送事件

```
EventBus.getDefault().post(new MessageEvent("Activity Content"));
```

### EventBus的三要素

* Event：可以是任意类型的对象。通过事件的发布者将事件进行传递。
* Subscriber：事件订阅者，需要添加一个注解@Subscribe，并且要指定线程模型（默认为POSTING）
* Publisher：事件发布者，以在任意线程任意位置发送事件，使用EventBus的post(Object)方法调用，根据post函数参数的类型，会自动调用订阅相应类型事件的函数

### EventBus的四种ThreadMode

* POSTING（默认）：订阅者将在发布该事件的同一个线程中被调用
* MAIN：订阅者将在主线程中被调用
* BACKGROUND：订阅者将在后台线程中被调用 ASYNC：订阅者将单独开辟一个线程中调用

### EventBus的粘性事件StickyEvent

StickyEvent在内存中保存最新的消息，取消原有消息，执行最新消息，只有在注册后才会执行，如果没有注册，消息会一直保留来内存中

移除黏性事件

```
EventBus.getDefault().removeStickyEvent(MessageEvent.class)
```

移除所有粘性事件

```
EventBus.getDefault().removeAllStickyEvents();
```

## EventBus 线程池

## EventBus 源码解析

### 注册 -> register()

```
    public void register(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        // subscriberMethods 返回的是 subscriber 这个类中所有的订阅方法
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);   --> ①
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                // 分类保存
                subscribe(subscriber, subscriberMethod);
            }
        }
    }
```

① findSubscriberMethods()

```
    List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        // 先从缓存中查找是否存在订阅者的方法
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (subscriberMethods != null) {
            return subscriberMethods;
        }

        if (ignoreGeneratedIndex) {
            // 使用反射方法拿到订阅者中的订阅方法
            subscriberMethods = findUsingReflection(subscriberClass);   --> ②
        } else {
            // 使用apt处理器拿到订阅者中的订阅方法
            subscriberMethods = findUsingInfo(subscriberClass);   --> ⑥
        }
        if (subscriberMethods.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass + " and its super classes have no public methods with the @Subscribe annotation");
        } else {
            // 将查到的方法放到缓存中，以便下次使用
            METHOD_CACHE.put(subscriberClass, subscriberMethods);
            return subscriberMethods;
        }
    }
```

② findUsingReflection()

```
    private List<SubscriberMethod> findUsingReflection(Class<?> subscriberClass) {
        FindState findState = prepareFindState();   --> ③
        findState.initForSubscriber(subscriberClass);
        while (findState.clazz != null) {
            findUsingReflectionInSingleClass(findState);    --> ⑤
            findState.moveToSuperclass();
        }
        return getMethodsAndRelease(findState);   --> ④
    }
```

③ prepareFindState()

```
    /**
     * 准备一个FindState 来装订阅者的方法
     */
    private FindState prepareFindState() {
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                FindState state = FIND_STATE_POOL[i];
                if (state != null) {
                    // 得到一个state并将相应的引用变量置为null
                    FIND_STATE_POOL[i] = null;
                    return state;
                }
            }
        }
        return new FindState();
    }
```

④ getMethodsAndRelease()

```
    /**
     * 将FindState 里面的方法放入subscriberMethods list中，并将FindState 放回去继续使用
     */
    private List<SubscriberMethod> getMethodsAndRelease(FindState findState) {
        List<SubscriberMethod> subscriberMethods = new ArrayList<>(findState.subscriberMethods);
        findState.recycle();
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                if (FIND_STATE_POOL[i] == null) {
                    // 将FindState 里面的方法放入subscriberMethods list中，并将FindState 放回去继续使用
                    FIND_STATE_POOL[i] = findState;
                    break;
                }
            }
        }
        return subscriberMethods;
    }
```

⑤ findUsingReflectionInSingleClass

```
    private void findUsingReflectionInSingleClass(FindState findState) {
        Method[] methods;
        try {
            // 通过反射方法得到订阅者类里面的订阅方法
            // This is faster than getMethods, especially when subscribers are fat classes like Activities
            methods = findState.clazz.getDeclaredMethods();
        } catch (Throwable th) {
            // Workaround for java.lang.NoClassDefFoundError, see https://github.com/greenrobot/EventBus/issues/149
            try {
                methods = findState.clazz.getMethods();
            } catch (LinkageError error) { // super class of NoClassDefFoundError to be a bit more broad...
                String msg = "Could not inspect methods of " + findState.clazz.getName();
                if (ignoreGeneratedIndex) {
                    msg += ". Please consider using EventBus annotation processor to avoid reflection.";
                } else {
                    msg += ". Please make this class visible to EventBus annotation processor to avoid reflection.";
                }
                throw new EventBusException(msg, error);
            }
            findState.skipSuperClasses = true;
        }
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            //  判断方法是不是public
            if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                // 方法参数是不是一个
                if (parameterTypes.length == 1) {
                    Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                    // 是否有注解
                    if (subscribeAnnotation != null) {
                        Class<?> eventType = parameterTypes[0];
                        // 检查方法和参数（事件）类型是否合法
                        if (findState.checkAdd(method, eventType)) {
                            ThreadMode threadMode = subscribeAnnotation.threadMode();
                            // 都满足将方法加入findState中
                            findState.subscriberMethods.add(new SubscriberMethod(method, eventType, threadMode,
                                    subscribeAnnotation.priority(), subscribeAnnotation.sticky()));
                        }
                    }
                } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
                    String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                    throw new EventBusException("@Subscribe method " + methodName +
                            "must have exactly 1 parameter but has " + parameterTypes.length);
                }
            } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
                String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                throw new EventBusException(methodName +
                        " is a illegal @Subscribe method: must be public, non-static, and non-abstract");
            }
        }
    }
```

⑥ findUsingInfo()

```
    private List<SubscriberMethod> findUsingInfo(Class<?> subscriberClass) {
        FindState findState = prepareFindState();
        findState.initForSubscriber(subscriberClass);
        while (findState.clazz != null) {
            findState.subscriberInfo = getSubscriberInfo(findState);
            if (findState.subscriberInfo != null) {
                SubscriberMethod[] array = findState.subscriberInfo.getSubscriberMethods();
                for (SubscriberMethod subscriberMethod : array) {
                    if (findState.checkAdd(subscriberMethod.method, subscriberMethod.eventType)) {
                        findState.subscriberMethods.add(subscriberMethod);
                    }
                }
            } else {
                findUsingReflectionInSingleClass(findState);
            }
            findState.moveToSuperclass();
        }
        return getMethodsAndRelease(findState);
    }
```

getSubscriberInfo()

```
    private SubscriberInfo getSubscriberInfo(FindState findState) {
        if (findState.subscriberInfo != null && findState.subscriberInfo.getSuperSubscriberInfo() != null) {
            SubscriberInfo superclassInfo = findState.subscriberInfo.getSuperSubscriberInfo();
            if (findState.clazz == superclassInfo.getSubscriberClass()) {
                return superclassInfo;
            }
        }
        if (subscriberInfoIndexes != null) {
            for (SubscriberInfoIndex index : subscriberInfoIndexes) {
                SubscriberInfo info = index.getSubscriberInfo(findState.clazz);
                if (info != null) {
                    return info;
                }
            }
        }
        return null;
    }
```

### 订阅 -> subscribe()

```
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        // 订阅函数参数类型 
        Class<?> eventType = subscriberMethod.eventType;
        // 在构造函数中记录下订阅者和订阅方法
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        // subscriptionsByEventType 可以根据参数类型来获取到订阅事件
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptions);
        } else {
            if (subscriptions.contains(newSubscription)) {
                // 已经注册的事件不允许再次注册
                throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event " + eventType);
            }
        }

        int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            // 根据优先级来添加
            if (i == size || subscriberMethod.priority > subscriptions.get(i).subscriberMethod.priority) {
                subscriptions.add(i, newSubscription);
                break;
            }
        }

        // typesBySubscriber 可以根据订阅者来获取到所有的订阅方法参数类型
        List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);

        // 粘性事件
        if (subscriberMethod.sticky) {
            if (eventInheritance) {
                // Existing sticky events of all subclasses of eventType have to be considered.
                // Note: Iterating over all events may be inefficient with lots of sticky events,
                // thus data structure should be changed to allow a more efficient lookup
                // (e.g. an additional map storing sub classes of super classes: Class -> List<Class>).
                Set<Map.Entry<Class<?>, Object>> entries = stickyEvents.entrySet();
                for (Map.Entry<Class<?>, Object> entry : entries) {
                    Class<?> candidateEventType = entry.getKey();
                    if (eventType.isAssignableFrom(candidateEventType)) {
                        Object stickyEvent = entry.getValue();
                        checkPostStickyEventToSubscription(newSubscription, stickyEvent);
                    }
                }
            } else {
                Object stickyEvent = stickyEvents.get(eventType);
                checkPostStickyEventToSubscription(newSubscription, stickyEvent);
            }
        }
    }
```

### 发送

postToSubscription()

```
    /**
     * 通过threadMode来确认使用哪个线程执行订阅者的订阅函数
     */
    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING:
                invokeSubscriber(subscription, event);
                break;
            case MAIN:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case MAIN_ORDERED:
                if (mainThreadPoster != null) {
                    mainThreadPoster.enqueue(subscription, event);
                } else {
                    // temporary: technically not correct as poster not decoupled from subscriber
                    invokeSubscriber(subscription, event);
                }
                break;
            case BACKGROUND:
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case ASYNC:
                asyncPoster.enqueue(subscription, event);
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }
```