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