# Android 消息机制

## Handler、Thread、HandlerThread三者的区别

1. Handler：在android中负责发送和处理消息，通过它可以实现其他支线线程与主线程之间的消息通讯。

2. Thread：Java进程中执行运算的最小单位，亦即执行处理机调度的基本单位。某一进程中一路单独运行的程序。

3. HandlerThread：一个继承自Thread的类HandlerThread，Android中没有对Java中的Thread进行任何封装，而是提供了一个继承自Thread的类HandlerThread类，这个类对Java的Thread做了很多便利的封装。

## HandlerThread

HandlerThread对象start后可以获得其Looper对象，并且使用这个Looper对象实例Handler，之后Handler就可以运行在其他线程中了。

## Handler

Handler 创建后可以通过 sendMessage 将消息加入消息队列，然后 looper 不断的将消息从 MessageQueue 中取出来，回调到 Handler 的 handleMessage 方法，从而实现线程的通信。

1. Message（消息）

需要被传递的消息，其中包含了消息ID，消息处理对象以及处理的数据等，由MessageQueue统一列队，最终由Handler处理。

2. MessageQueue（消息队列）

用来存放Handler发送过来的消息，内部通过单链表的数据结构来维护消息列表，等待Looper的抽取。

3. Handler（处理者）

负责Message的发送及处理。通过sendMessage()向消息池发送各种消息事件，通过handleMessage()处理相应的消息事件。

4. Looper（消息泵）

一个线程可以产生一个 Looper 对象，由它来管理此线程里的 MessageQueue( 消息队列 ) 和对消息进行循环。通过Looper.loop()不断地从MessageQueue中抽取Message，按分发机制将消息分发给目标处理者。

## Looper的工作原理

* Looper.prepare()                      -   为当前线程创建一个Looper；
* Looper.loop()                         -   开启消息循环，内部会调用MessageQueue中的next()方法，只有调用该方法，消息循环系统才会开始循环；
* Looper.prepareMainLooper()            -   为主线程也就是ActivityThread创建Looper使用；
* Looper.getMainLooper()                -   通过该方法可以在任意地方获取到主线程的Looper；
* Looper.quit(),Looper.quitSafely()     -   退出Looper，自主创建的Looper建议在不使用的时候退出

## sendMessage 与 post 区别

* sendMessage : 在当前线程执行，发送一个消息，这个消息会在Looper中去处理
* post : 开启新的线程执行，将一个Runnable投递到Handler内部的Looper中去处理

```
    public final boolean sendMessage(@NonNull Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    public final boolean post(@NonNull Runnable r) {
       return  sendMessageDelayed(getPostMessage(r), 0);
    }

    private static Message getPostMessage(Runnable r) {
        Message m = Message.obtain();
        m.callback = r;
        return m;
    }

    public void dispatchMessage(@NonNull Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }

    private static void handleCallback(Message message) {
        message.callback.run();
    }
```