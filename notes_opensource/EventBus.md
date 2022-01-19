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

1. EventBus.getDefault()

```
    public static EventBus getDefault() {
        EventBus instance = defaultInstance;
        if (instance == null) {
            synchronized (EventBus.class) {
                instance = EventBus.defaultInstance;
                if (instance == null) {
                    instance = EventBus.defaultInstance = new EventBus();
                }
            }
        }
        return instance;
    }
```

register()

```
    public void register(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        // subscriberMethods 返回的是 subscriber 这个类中所有的订阅方法
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                // 分类保存
                subscribe(subscriber, subscriberMethod);
            }
        }
    }
```

subscribe()

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

post()

```
    public void post(Object event) {
        // currentPostingThreadState 是 ThreadLocal
        PostingThreadState postingState = currentPostingThreadState.get();
        List<Object> eventQueue = postingState.eventQueue;
        eventQueue.add(event);

        if (!postingState.isPosting) {
            postingState.isMainThread = isMainThread();
            postingState.isPosting = true;
            if (postingState.canceled) {
                throw new EventBusException("Internal error. Abort state was not reset");
            }
            try {
                while (!eventQueue.isEmpty()) {
                    postSingleEvent(eventQueue.remove(0), postingState);
                }
            } finally {
                postingState.isPosting = false;
                postingState.isMainThread = false;
            }
        }
    }
```