# Android 事件分发

* 事件传递的顺序：Activity（Window） -> ViewGroup -> View

事件分发机制伪代码
```
public boolean dispatch(MotionEvent ev) {
    boolean consume = false; // 是否消费事件
    if (onIntercept(ev)) { // 如果拦截事件
        consume = onTouch(ev); // onTouch返回true，表示消费事件，如返回false，未消费事件
    } else { // 未拦截事件
        consume = child.dispatch(ev); // 只要子View支持clickable或longClickable，都会消费事件
    }
    return consume;
}
```