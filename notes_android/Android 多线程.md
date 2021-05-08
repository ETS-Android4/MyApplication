# Android 多线程

AsyncTask：底层封装了线程池和Handler，便于执行后台任务以及在主线程中进行UI操作。
HandlerThread：一种具有消息循环的线程，其内部可使用Handler。
IntentService：是一种异步、会自动停止的服务，内部采用HandlerThread。

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