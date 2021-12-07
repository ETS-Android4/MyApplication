# Android 消息机制

* Handler，Message，looper 和 MessageQueue 构成了安卓的消息机制。
* Handler 创建后可以通过 sendMessage 将消息加入消息队列，然后 looper 不断的将消息从 MessageQueue 中取出来，回调到 Handler 的 handleMessage 方法，从而实现线程的通信。

## Message（消息）

需要被传递的消息，其中包含了消息ID，消息处理对象以及处理的数据等，由MessageQueue统一列队，最终由Handler处理。

## MessageQueue（消息队列）

用来存放Handler发送过来的消息，内部通过单链表的数据结构来维护消息列表，等待Looper的抽取。

## Handler（处理者）

负责Message的发送及处理。通过sendMessage()向消息池发送各种消息事件，通过handleMessage()处理相应的消息事件。

## Looper（消息泵）

通过Looper.loop()不断地从MessageQueue中抽取Message，按分发机制将消息分发给目标处理者。

## sendMessage 与 post 区别

* sendMessage : 在当前线程执行
* post : 开启新的线程执行

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

# Handler、Thread、HandlerThread三者的区别

1. Handler：在android中负责发送和处理消息，通过它可以实现其他支线线程与主线程之间的消息通讯。

2. Thread：Java进程中执行运算的最小单位，亦即执行处理机调度的基本单位。某一进程中一路单独运行的程序。

3. HandlerThread：一个继承自Thread的类HandlerThread，Android中没有对Java中的Thread进行任何封装，而是提供了一个继承自Thread的类HandlerThread类，这个类对Java的Thread做了很多便利的封装。
