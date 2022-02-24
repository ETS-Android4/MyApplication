# Android 动画

## 1. 帧动画(Frame Animation)

利用 xml 实现逐帧动画。使用较少，可以用gif替代。

```xml
<?xml version="1.0" encoding="utf-8"?>
<animation-list xmlns:android="http://schemas.android.com/apk/res/android"
    android:oneshot="true">
    <item android:drawable="@drawable/frame01" android:duration="100"/>
    <item android:drawable="@drawable/frame02" android:duration="100"/>
    <item android:drawable="@drawable/frame03" android:duration="100"/>
    <item android:drawable="@drawable/frame04" android:duration="100"/>
    <item android:drawable="@drawable/frame05" android:duration="100"/>
</animation-list>
```

## 2. 补间动画(Tween Animation)

实现基本的动画效果：平移、旋转、缩放、透明度。没有改变View属性，只是改变视觉效果。动画效果单一。

1. AlphaAnimation（透明度动画）
2. RotateAnimation（旋转动画）
3. ScaleAnimation（缩放动画）
4. TranslateAnimation（位移动画）
5. AnimationSet 复合动画

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="6000">
    <!--透明度动画标签-->
    <alpha
        android:fromAlpha="1.0"
        android:toAlpha="0" />
    <!--旋转动画标签-->
    <rotate
        android:fromDegrees="0"
        android:pivotX="50%"
        android:pivotY="50%"
        android:toDegrees="360" />
    <!--缩放动画标签-->
    <scale
        android:fromXScale="1.0"
        android:fromYScale="1.0"
        android:pivotX="50%"
        android:pivotY="50%"
        android:toXScale="0"
        android:toYScale="0" />
    <!--平移动画标签-->
    <translate
        android:fromXDelta="0%"
        android:fromYDelta="0%"
        android:toXDelta="100%"
        android:toYDelta="100%" />
</set>
```

## 3. 属性动画

* ValueAnimator 类是先改变值，然后 手动赋值 给对象的属性从而实现动画；是 间接 对对象属性进行操作；
* ObjectAnimator 类是先改变值，然后 自动赋值 给对象的属性从而实现动画；是 直接 对对象属性进行操作；

### Interpolator（插值器）

设置 属性值 从初始值过度到结束值 的变化规律。如匀速、加速、减速等。即确定动画效果变化模式

### TypeEvaluator（估值器）

设置 属性值 从初始值到结束值 的变化具体数值。即确定动画效果变化趋势

## 4. 触摸反馈动画

## 5. 揭露动画

## 6. 转场动画（Android 5.0）

1. explode（分解）
2. slide（滑动）
3. fade（淡入）
4. share（共享元素）

## 7. 约束布局关键帧动画

## 8. 视图状态动画

## 9. 矢量图动画
