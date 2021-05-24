# Android 事件分发

* 事件传递的顺序：Activity（Window） -> ViewGroup -> View

## 工作流程

1. 点击事件达到顶级 View(一般是一个 ViewGroup)，会调用 ViewGroup 的 dispatchTouchEvent 方法，
2. 如果顶级 ViewGroup 拦截事件即 onInterceptTouchEvent 返回 true，则事件由 ViewGroup 处理，
3. 这时如果 ViewGroup 的 mOnTouchListener 被设置，则 onTouch 会被调用，否则 onTouchEvent 会被调用。也就是说如果都提供的话，onTouch 会屏蔽掉 onTouchEvent。
4. 在 onTouchEvent 中，如果设置了 mOnClickListenser，则 onClick 会被调用。
5. 如果顶级 ViewGroup 不拦截事件，则事件会传递给它所在的点击事件链上的子 View，这时子 View 的 dispatchTouchEvent 会被调用。如此循环。

* ViewGroup 默认不拦截任何事件。ViewGroup 的 onInterceptTouchEvent 方法默认返回 false。
* View没有 onInterceptTouchEvent 方法，一旦有点击事件传递给它，onTouchEvent 方法就会被调用。
* View 在可点击状态下，onTouchEvent 默认会消耗事件，除非它是不可点击的（clickable和longClickable同时为false）。
* ACTION_DOWN 被拦截了，onInterceptTouchEvent 方法执行一次后，就会留下记号（mFirstTouchTarget == null）那么往后的 ACTION_MOVE 和 ACTION_UP 都会拦截。`

## 事件分发机制伪代码

```
// 步骤1：调用dispatchTouchEvent()
public boolean dispatch(MotionEvent ev) {
    boolean consume = false; // 代表 是否会消费事件
    // 步骤2：判断是否拦截事件
    if (onIntercept(ev)) {
        // a. 若拦截，则将该事件交给当前View进行处理
        // 即调用onTouchEvent (）方法去处理点击事件
        consume = onTouch(ev); // onTouch返回true，表示消费事件，如返回false，未消费事件
    } else {
        // b. 若不拦截，则将该事件传递到下层
        // 即 下层元素的dispatchTouchEvent（）就会被调用，重复上述过程
        // 直到点击事件被最终处理为止
        consume = child.dispatch(ev); // 只要子View支持clickable或longClickable，都会消费事件
    }
    // 步骤3：最终返回通知 该事件是否被消费（接收 ; 处理）
    return consume;
}
```

## onInterceptTouchEvent 拦截事件

* ViewGroup特有方法。默认返回 false，不拦截任何事件。
* 如果 onInterceptTouchEvent 返回 true，则事件由 ViewGroup 处理，会执行自己的 onTouchEvent 方法，且此方法不会再次调用。
* 如果 ViewGroup 的 mOnTouchListener 被设置，则 onTouch 会调用，否则会调用 onTouchEvent 方法。在 onTouch 中，如果设置了 mOnClickListener，则 onClick 被调用。
* 如果 onInterceptTouchEvent 返回 false，则事件由子 view 处理，由子 view 的 dispatchTouchEvent 分发事件

## dispatchTouchEvent 分发事件

* 返回值是 boolean 类型，受当前onTouchEvent和下级view的dispatchTouchEvent影响。

## onTouchEvent 消费事件

* dispatchTouchEvent 和 onTouchEvent 一旦返回 true ，事件就被消费掉了，该事件就消失了，不会往下传递也不会向上回溯
* dispatchTouchEvent 和 onTouchEvent 一旦返回 false ，事件就会回溯到父控件的 onTouchEvent，说明自己不处理，由父 view 的 onTouchEvent 接收。

## requestDisallowInterceptTouchEvent 禁止拦截触摸事件

* ViewGroup特有方法。一般在子view中来调用的。
* 当一个子view不希望它的父view来通过onInterceptTouchEvent方法拦截事件的时候，调用该方法即可实现事件的传递和接管

## onTouch、onTouchEvent、onClick执行顺序

* 执行顺序：onTouch —> onTouchEvent —> onClick

因此onTouchListener的onTouch()方法会先触发；如果onTouch()返回false才会接着触发onTouchEvent()，同样的，内置诸如onClick()事件的实现等等都基于onTouchEvent()；如果onTouch()返回true，这些事件将不会被触发。
