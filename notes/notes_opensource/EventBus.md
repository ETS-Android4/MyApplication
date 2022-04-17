# EventBus

> https://github.com/greenrobot/EventBus

## EventBus 简单使用

### 普通事件

（1）定义事件

```
public class MessageEvent {

    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
```

（2）准备订阅者

```
@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessageEvent(MessageEvent event) {
    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
}
```

（3）注册与反注册

```
@Override
public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
}

@Override
public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
}
```

（4）发送事件

```
EventBus.getDefault().post(new MessageEvent("发送事件"));
```

### 粘性事件

（2）准备订阅者

```
@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
public void onMessageEvent(MessageEvent event) {
    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
}
```

（4）发送事件

```
EventBus.getDefault().postSticky(new MessageEvent("EventBus"));
```