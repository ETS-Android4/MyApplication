## 源码解析 

### 1. Subscribe 注解

```
public @interface Subscribe {
    // 指定线程模式，即指定订阅方法在哪个线程执行，线程模式一共有 5 种：POSTING（默认模式）、MAIN、MAIN_ORDERED、BACKGROUND、ASYNC
    ThreadMode threadMode() default ThreadMode.POSTING;

    // 是否是粘性事件
    boolean sticky() default false;

    //指定优先级
    int priority() default 0;
}
```

### 2. 注册

```
EventBus.getDefault().register(this);
```

通过双重校验锁的单例模式来获取 EventBus 的实例

```
    EventBus(EventBusBuilder builder) {
        logger = builder.getLogger();
        
        subscriptionsByEventType = new HashMap<>();
        typesBySubscriber = new HashMap<>();
        stickyEvents = new ConcurrentHashMap<>();
        
        mainThreadSupport = builder.getMainThreadSupport();
        
        mainThreadPoster = mainThreadSupport != null ? mainThreadSupport.createPoster(this) : null; // 实际上是个 HandlerPoster
        backgroundPoster = new BackgroundPoster(this);
        asyncPoster = new AsyncPoster(this);
        
        indexCount = builder.subscriberInfoIndexes != null ? builder.subscriberInfoIndexes.size() : 0;
        
        subscriberMethodFinder = new SubscriberMethodFinder(builder.subscriberInfoIndexes,
                builder.strictMethodVerification, builder.ignoreGeneratedIndex); // 用来获取注册类上所有订阅方法的集合

        logSubscriberExceptions = builder.logSubscriberExceptions;
        logNoSubscriberMessages = builder.logNoSubscriberMessages;
        sendSubscriberExceptionEvent = builder.sendSubscriberExceptionEvent;
        sendNoSubscriberEvent = builder.sendNoSubscriberEvent;
        throwSubscriberException = builder.throwSubscriberException;
        eventInheritance = builder.eventInheritance;

        executorService = builder.executorService;
    }
```

### register()

```
    /*EventBus*/
    public void register(Object subscriber) {
        if (AndroidDependenciesDetector.isAndroidSDKAvailable() && !AndroidDependenciesDetector.areAndroidComponentsAvailable()) {
            // Crash if the user (developer) has not imported the Android compatibility library.
            throw new RuntimeException("It looks like you are using EventBus on Android, " +
                    "make sure to add the \"eventbus\" Android library to your dependencies.");
        }

        Class<?> subscriberClass = subscriber.getClass();
        List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass); // （1）
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) { // （2）
                subscribe(subscriber, subscriberMethod); // （3）
            }
        }
    }
```

### （1）subscriberMethodFinder.findSubscriberMethods(subscriberClass);

获取注册类上所有订阅方法的集合，也就是加了 @Subscribe 注解的方法

```
    /*SubscriberMethodFinder*/
    List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
        // 从 METHOD_CACHE 集合中查找是否已经缓存了该注册类上的订阅方法
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (subscriberMethods != null) {
            return subscriberMethods;
        }
        
        if (ignoreGeneratedIndex) { // 在 EventBus 的有参构造中通过 EventBusBuilder 配置，默认是 false
            subscriberMethods = findUsingReflection(subscriberClass);
        } else {
            // 查找所有订阅方法
            subscriberMethods = findUsingInfo(subscriberClass); // （4）
        }
        // 订阅方法为空，说明注册类中不存在 @Subscribe 注解的方法。
        if (subscriberMethods.isEmpty()) {
            throw new EventBusException("Subscriber " + subscriberClass
                    + " and its super classes have no public methods with the @Subscribe annotation");
        } else {
            METHOD_CACHE.put(subscriberClass, subscriberMethods); // 找到所有订阅方法后缓存到 METHOD_CACHE 集合中
            return subscriberMethods;
        }
    }
```

### （4）findUsingInfo(subscriberClass);

```
    /*SubscriberMethodFinder*/
    private List<SubscriberMethod> findUsingInfo(Class<?> subscriberClass) {
        FindState findState = prepareFindState(); // （5）
        findState.initForSubscriber(subscriberClass);
        while (findState.clazz != null) {
            findState.subscriberInfo = getSubscriberInfo(findState); // （6）
            if (findState.subscriberInfo != null) {
                SubscriberMethod[] array = findState.subscriberInfo.getSubscriberMethods();
                for (SubscriberMethod subscriberMethod : array) {
                    if (findState.checkAdd(subscriberMethod.method, subscriberMethod.eventType)) {
                        findState.subscriberMethods.add(subscriberMethod);
                    }
                }
            } else {
                findUsingReflectionInSingleClass(findState); // （7）
            }
            findState.moveToSuperclass();
        }
        return getMethodsAndRelease(findState); // （8）
    }
```

### （5）prepareFindState()

prepareFindState 方法返回一个 FindState 对象

```
    /*SubscriberMethodFinder*/
    private FindState prepareFindState() {
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                FindState state = FIND_STATE_POOL[i];
                if (state != null) {
                    FIND_STATE_POOL[i] = null;
                    return state;
                }
            }
        }
        return new FindState();
    }
```

### FindState.class

```
    static class FindState {
        final List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        final Map<Class, Object> anyMethodByEventType = new HashMap<>();
        final Map<String, Class> subscriberClassByMethodKey = new HashMap<>();
        final StringBuilder methodKeyBuilder = new StringBuilder(128);

        Class<?> subscriberClass;
        Class<?> clazz;
        boolean skipSuperClasses;
        SubscriberInfo subscriberInfo;

        void initForSubscriber(Class<?> subscriberClass) {
            this.subscriberClass = clazz = subscriberClass;
            skipSuperClasses = false;
            subscriberInfo = null;
        }

        void recycle() { // 查找完成进行回收
            subscriberMethods.clear();
            anyMethodByEventType.clear();
            subscriberClassByMethodKey.clear();
            methodKeyBuilder.setLength(0);
            subscriberClass = null;
            clazz = null;
            skipSuperClasses = false;
            subscriberInfo = null;
        }
        ···
    }
```

### （6）getSubscriberInfo(findState);

```
    /*SubscriberMethodFinder*/
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

### （7）findUsingReflectionInSingleClass(findState);

```
    /*SubscriberMethodFinder*/
    private void findUsingReflectionInSingleClass(FindState findState) {
        Method[] methods;
        try {
            // 反射获取注册类中的所有方法
            // This is faster than getMethods, especially when subscribers are fat classes like Activities
            methods = findState.clazz.getDeclaredMethods();
        } catch (Throwable th) {
            // Workaround for java.lang.NoClassDefFoundError, see https://github.com/greenrobot/EventBus/issues/149
            try {
                // 反射获取注册类中的所有方法
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
        // 循环遍历所有方法
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    // 获取方法上的注解
                    Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                    if (subscribeAnnotation != null) {
                        // 获取方法上的第一个参数，也就是事件类型（即使用示例中 MessageEvent 的类型）
                        Class<?> eventType = parameterTypes[0];
                        if (findState.checkAdd(method, eventType)) {
                            // 获取线程模式
                            ThreadMode threadMode = subscribeAnnotation.threadMode();
                            // 将方法、事件类型、线程模式、优先级、是否是粘性事件封装到 SubscriberMethod 对象，然后添加到 FindState 类中的 subscriberMethods 集合中存起来
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

### （8）getMethodsAndRelease(findState);

```
    private List<SubscriberMethod> getMethodsAndRelease(FindState findState) {
        List<SubscriberMethod> subscriberMethods = new ArrayList<>(findState.subscriberMethods);
        findState.recycle();
        synchronized (FIND_STATE_POOL) {
            for (int i = 0; i < POOL_SIZE; i++) {
                if (FIND_STATE_POOL[i] == null) {
                    FIND_STATE_POOL[i] = findState;
                    break;
                }
            }
        }
        return subscriberMethods;
    }
```

### （2）subscribe(subscriber, subscriberMethod);

```
    /*EventBus*/
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        // 获取事件类型
        Class<?> eventType = subscriberMethod.eventType;
        // 将注册类与订阅方法封装到 Subscription
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        // subscriptionsByEventType 是一个存储 key 为事件类型，value 为 Subscription 集合的 HashMap
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptions);
        } else {
            if (subscriptions.contains(newSubscription)) {
                throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event "
                        + eventType);
            }
        }

        // 根据设置的优先级将 newSubscription 添加到 subscriptions 集合中，也就是将优先级越高的排在集合中越前的位置。
        int size = subscriptions.size();
        for (int i = 0; i <= size; i++) {
            if (i == size || subscriberMethod.priority > subscriptions.get(i).subscriberMethod.priority) {
                subscriptions.add(i, newSubscription);
                break;
            }
        }

        // typesBySubscriber 是一个存储 key 为注册类，value 为事件类型集合的 HashMap。判断某个对象是否注册过，防止重复注册。
        List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
        if (subscribedEvents == null) {
            subscribedEvents = new ArrayList<>();
            typesBySubscriber.put(subscriber, subscribedEvents);
        }
        subscribedEvents.add(eventType);

        // 是否为粘性事件
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

### 3. 反注册

```
    /*EventBus*/
    public synchronized void unregister(Object subscriber) {
        // 根据注册类获取 typesBySubscriber 集合中保存的事件类型集合
        List<Class<?>> subscribedTypes = typesBySubscriber.get(subscriber);
        if (subscribedTypes != null) {
            for (Class<?> eventType : subscribedTypes) {
                unsubscribeByEventType(subscriber, eventType); //（9）
            }
            // 移除 typesBySubscriber 中保存的事件类型集合
            typesBySubscriber.remove(subscriber);
        } else {
            logger.log(Level.WARNING, "Subscriber to unregister was not registered before: " + subscriber.getClass());
        }
    }
```

### （9）unsubscribeByEventType(subscriber, eventType);

```
    /*EventBus*/
    private void unsubscribeByEventType(Object subscriber, Class<?> eventType) {
        // 根据事件类型获取 subscriptionsByEventType 集合中保存的 Subscription 集合
        List<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscription.active = false;
                    // 从 Subscription 集合中移除 Subscription
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }
```

### 4. 发送事件

```
    /*EventBus*/
    public void post(Object event) {
        // currentPostingThreadState 是一个 ThreadLocal 对象，里面保存了 PostingThreadState。
        // 使用 ThreadLocal 的好处是保证 PostingThreadState 是线程私有的，其他线程无法访问，避免出现线程安全问题。
        PostingThreadState postingState = currentPostingThreadState.get(); // （10）
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
                    postSingleEvent(eventQueue.remove(0), postingState); // （11）
                }
            } finally {
                postingState.isPosting = false;
                postingState.isMainThread = false;
            }
        }
    }
```

### （10）PostingThreadState.class

```
    final static class PostingThreadState {
        final List<Object> eventQueue = new ArrayList<>();
        boolean isPosting;
        boolean isMainThread;
        Subscription subscription;
        Object event;
        boolean canceled;
    }
```

### （11）postSingleEvent(eventQueue.remove(0), postingState);

```
    /*EventBus*/
    private void postSingleEvent(Object event, PostingThreadState postingState) throws Error {
        Class<?> eventClass = event.getClass();
        // 是否找到订阅者
        boolean subscriptionFound = false;
        if (eventInheritance) { // 是否需要发送父类与接口中的事件，默认为 true
            List<Class<?>> eventTypes = lookupAllEventTypes(eventClass); // 查找所有事件类型，包括当前事件、父类和接口中的事件
            int countTypes = eventTypes.size();
            for (int h = 0; h < countTypes; h++) {
                Class<?> clazz = eventTypes.get(h);
                // 根据事件类型发送事件，包括父类、接口
                subscriptionFound |= postSingleEventForEventType(event, postingState, clazz); // （12）
            }
        } else {
            // 根据事件类型只发送当前注册类的事件，忽略父类以及接口
            subscriptionFound = postSingleEventForEventType(event, postingState, eventClass);
        }
        if (!subscriptionFound) {
            if (logNoSubscriberMessages) {
                logger.log(Level.FINE, "No subscribers registered for event " + eventClass);
            }
            if (sendNoSubscriberEvent && eventClass != NoSubscriberEvent.class &&
                    eventClass != SubscriberExceptionEvent.class) {
                // 没有找到订阅者，发送一个 NoSubscriberEvent 事件
                post(new NoSubscriberEvent(this, event));
            }
        }
    }
```

### （12）postSingleEventForEventType(event, postingState, clazz);

```
    /*EventBus*/
    private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
        CopyOnWriteArrayList<Subscription> subscriptions;
        synchronized (this) {
            // 从 subscriptionsByEventType 集合中取出之前注册的时候保存的 Subscription 集合
            subscriptions = subscriptionsByEventType.get(eventClass);
        }
        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (Subscription subscription : subscriptions) {
                postingState.event = event;
                postingState.subscription = subscription;
                boolean aborted;
                try {
                    postToSubscription(subscription, event, postingState.isMainThread); // （13）
                    aborted = postingState.canceled;
                } finally {
                    postingState.event = null;
                    postingState.subscription = null;
                    postingState.canceled = false;
                }
                if (aborted) {
                    break;
                }
            }
            return true;
        }
        return false;
    }
```

### （13）postToSubscription(subscription, event, postingState.isMainThread);

通过我们在注解中设置的线程模式来决定在哪个线程执行订阅方法

```
    /*EventBus*/
    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING: // （14）
                invokeSubscriber(subscription, event);
                break;
            case MAIN: // （15）
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    mainThreadPoster.enqueue(subscription, event);
                }
                break;
            case MAIN_ORDERED: // （16）
                if (mainThreadPoster != null) {
                    mainThreadPoster.enqueue(subscription, event);
                } else {
                    // temporary: technically not correct as poster not decoupled from subscriber
                    invokeSubscriber(subscription, event);
                }
                break;
            case BACKGROUND: // （17）
                if (isMainThread) {
                    backgroundPoster.enqueue(subscription, event);
                } else {
                    invokeSubscriber(subscription, event);
                }
                break;
            case ASYNC: // （18）
                asyncPoster.enqueue(subscription, event);
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }
```

### （14）POSTING:invokeSubscriber(subscription, event);

如果线程模式是 POSTING，那么在哪个线程发送事件，就在哪个线程执行订阅方法

```
    void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (InvocationTargetException e) {
            handleSubscriberException(subscription, event, e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }
```

### （15）MAIN:

首先判断是否在主线程，在主线程则直接反射调用；否则调用 mainThreadPoster 的 enqueue 方法

mainThreadPoster 实际上是一个 HandlerPoster

```
public class HandlerPoster extends Handler implements Poster {
    ···
    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event); // 将 subscription 和 event 封装成一个 PendingPost 对象
        synchronized (this) {
            queue.enqueue(pendingPost); // 将 PendingPost 对象加入到队列中
            if (!handlerActive) {
                handlerActive = true;
                if (!sendMessage(obtainMessage())) { // 发送消息
                    throw new EventBusException("Could not send handler message");
                }
            }
        }
    }
    
    @Override
    public void handleMessage(Message msg) {
        boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                // 不断的从队列中取出 PendingPost
                PendingPost pendingPost = queue.poll();
                ···
                // 调用 EventBus 的 invokeSubscriber 方法进行处理
                eventBus.invokeSubscriber(pendingPost); // （19）
                ···
            }
        } finally {
            handlerActive = rescheduled;
        }
    }
}
```

### （19）eventBus.invokeSubscriber(pendingPost);

```
    /*EventBus*/
    void invokeSubscriber(PendingPost pendingPost) {
        Object event = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        if (subscription.active) {
            invokeSubscriber(subscription, event);
        }
    }
    
    void invokeSubscriber(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (InvocationTargetException e) {
            handleSubscriberException(subscription, event, e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }
```

### （16）MAIN_ORDERED:

mainThreadPoster 不会为空，所以与 MAIN 一样是调用了 enqueue 方法

### （17）BACKGROUND:

首先判断是否在主线程，在主线程则调用 backgroundPoster 的 enqueue 方法；否则直接反射调用

```
final class BackgroundPoster implements Runnable, Poster {
    ···
    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event); // 将 subscription 和 event 封装成一个 PendingPost 对象
        synchronized (this) {
            queue.enqueue(pendingPost); // 将 PendingPost 对象加入到队列中
            if (!executorRunning) {
                executorRunning = true;
                eventBus.getExecutorService().execute(this); // 通过线程执行
            }
        }
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    PendingPost pendingPost = queue.poll(1000);
                    if (pendingPost == null) {
                        synchronized (this) {
                            //  不断的从队列中取出 PendingPost
                            // Check again, this time in synchronized
                            pendingPost = queue.poll();
                            if (pendingPost == null) {
                                executorRunning = false;
                                return;
                            }
                        }
                    }
                    // 调用 EventBus 的 invokeSubscriber 方法进行处理
                    eventBus.invokeSubscriber(pendingPost); // 同（19）
                }
            } catch (InterruptedException e) {
                eventBus.getLogger().log(Level.WARNING, Thread.currentThread().getName() + " was interruppted", e);
            }
        } finally {
            executorRunning = false;
        }
    }

}
```

### （18）ASYNC:

没有线程判断，直接调用asyncPoster.enqueue(subscription, event);

```
class AsyncPoster implements Runnable, Poster {

    private final PendingPostQueue queue;
    private final EventBus eventBus;

    AsyncPoster(EventBus eventBus) {
        this.eventBus = eventBus;
        queue = new PendingPostQueue();
    }

    public void enqueue(Subscription subscription, Object event) {
        PendingPost pendingPost = PendingPost.obtainPendingPost(subscription, event); // 将 subscription 和 event 封装成一个 PendingPost 对象
        queue.enqueue(pendingPost); // 将 PendingPost 对象加入到队列中
        eventBus.getExecutorService().execute(this); // 通过线程执行
    }

    @Override
    public void run() {
        PendingPost pendingPost = queue.poll();
        if(pendingPost == null) {
            throw new IllegalStateException("No pending post available");
        }
        // 调用 EventBus 的 invokeSubscriber 方法进行处理
        eventBus.invokeSubscriber(pendingPost); // 同（19）
    }

}
```

### 5. 发送粘性事件

首先将事件保存到 stickyEvents 集合中，然后调用 post 方法发送事件。

```
    /*EventBus*/
    public void postSticky(Object event) {
        synchronized (stickyEvents) {
            stickyEvents.put(event.getClass(), event);
        }
        // Should be posted after it is putted, in case the subscriber wants to remove immediately
        post(event);
    }
```

post() —> postSingleEvent() —> postSingleEventForEventType()

```
    /*EventBus*/
    private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
        ···
        if (subscriptions != null && !subscriptions.isEmpty()) {
                    ···
                    postToSubscription(subscription, event, postingState.isMainThread); // （20）
                    ···
            return true;
        }
        return false;
    }
```

（20）这里 subscriptions 为空，并不会执行 postToSubscription 方法

```
    /*EventBus*/
    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        ···
        // 是否为粘性事件
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
                        checkPostStickyEventToSubscription(newSubscription, stickyEvent); // （21）如果是粘性事件，则从 stickyEvents 集合中取出事件，然后调用 checkPostStickyEventToSubscription 方法
                    }
                }
            } else {
                Object stickyEvent = stickyEvents.get(eventType);
                checkPostStickyEventToSubscription(newSubscription, stickyEvent);
            }
        }
    }
```

### （21）checkPostStickyEventToSubscription(newSubscription, stickyEvent);

```
    private void checkPostStickyEventToSubscription(Subscription newSubscription, Object stickyEvent) {
        if (stickyEvent != null) {
            // If the subscriber is trying to abort the event, it will fail (event is not tracked in posting state)
            // --> Strange corner case, which we don't take care of here.
            postToSubscription(newSubscription, stickyEvent, isMainThread());
        }
    }
```

### 总结

* 注册

通过反射获取注册类上所有的订阅方法，然后将这些订阅方法进行包装保存到 subscriptionsByEventType 集合。这里还用 typesBySubscriber 集合保存了事件类型集合，用来判断某个对象是否注册过。

* 反注册

注册的时候使用 subscriptionsByEventType 集合保存了所有订阅方法信息，使用 typesBySubscriber 集合保存了所有事件类型。那么解注册的时候就是为了移除这两个集合中保存的内容。
  
* 发送普通事件

从 subscriptionsByEventType 集合中取出所有订阅方法，然后根据线程模式判断是否需要切换线程，不需要则直接通过反射调用订阅方法；需要则通过 Handler 或线程池切换到指定线程再执行。
  
* 发送粘性事件

发送粘性事件的的时候，首先会将事件保存到 stickyEvents 集合，等到注册的时候判断如果是粘性事件，则从集合中取出事件进行发送。
