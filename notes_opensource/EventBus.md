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
