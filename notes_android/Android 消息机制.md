# Android 消息机制

* Handler，Message，looper 和 MessageQueue 构成了安卓的消息机制。
* Handler 创建后可以通过 sendMessage 将消息加入消息队列，然后 looper 不断的将消息从 MessageQueue 中取出来，回调到 Handler 的 handleMessage 方法，从而实现线程的通信。