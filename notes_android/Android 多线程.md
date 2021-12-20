# Android 多线程

## AsyncTask

内部是Handler和两个线程池实现的，Handler用于将线程切换到主线程，两个线程池一个用于任务的排队，一个用于执行任务，当AsyncTask执行execute方法时会封装出一个FutureTask对象，将这个对象加入队列中，如果此时没有正在执行的任务，就执行它，执行完成之后继续执行队列中下一个任务，执行完成通过Handler将事件发送到主线程。AsyncTask必须在主线程初始化，因为内部的Handler是一个静态对象，在AsyncTask类加载的时候他就已经被初始化了。在Android3.0开始，execute方法串行执行任务的，一个一个来，3.0之前是并行执行的。如果要在3.0上执行并行任务，可以调用executeOnExecutor方法。

##  HandlerThread

继承自Thread，start开启线程后，会在其run方法中会通过Looper创建消息队列并开启消息循环，这个消息队列运行在子线程中，所以可以将HandlerThread中的Looper实例传递给一个Handler，从而保证这个Handler的handleMessage方法运行在子线程中，Android中使用HandlerThread的一个场景就是IntentService。

##  IntentService

继承自Service，它的内部封装了HandlerThread和Handler，可以执行耗时任务，同时因为它是一个服务，优先级比普通线程高很多，所以更适合执行一些高优先级的后台任务，HandlerThread底层通过Looper消息队列实现的，所以它是顺序的执行每一个任务。可以通过Intent的方式开启IntentService，IntentService通过handler将每一个intent加入HandlerThread子线程中的消息队列，通过looper按顺序一个个的取出并执行，执行完成后自动结束自己，不需要开发者手动关闭。

## HandlerThread

HandlerThread是一个线程类，它继承自Thread

### 工作原理

内部HandlerThread.run()方法中有Looper，通过Looper.prepare()来创建消息队列，并通过Looper.loop()来开启消息循环。

## IntentService

IntentService是一个继承自Service的抽象类

* 相比于线程：由于是服务，优先级比线程高，更不容易被系统杀死。因此较适合执行一些高优先级的后台任务。
* 相比于普通Service：可自动创建子线程来执行任务，且任务执行完毕后自动退出。

### 工作原理

1. 在IntentService.onCreate()里创建一个Handle对象即HandlerThread，利用其内部的Looper会实例化一个ServiceHandler对象；
2. 任务请求的Intent会被封装到Message并通过ServiceHandler发送给Looper的MessageQueue，最终在HandlerThread中执行；
3. 在ServiceHandler.handleMessage()中会调用IntentService.onHandleIntent()，可在该方法中处理后台任务的逻辑。

## 线程池

```
public ThreadPoolExecutor(int corePoolSize, // 核心线程数
                          int maximumPoolSize, // 最大线程数
                          long keepAliveTime,  // 闲置等待时间
                          TimeUnit unit, // 超时时间
                          BlockingQueue<Runnable> workQueue) { // 线程池中任务队列
}
```

1. 单线程线程池

ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

2. 定长线程池

ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

3. 缓存线程池

ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

4. 定时线程池

ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
