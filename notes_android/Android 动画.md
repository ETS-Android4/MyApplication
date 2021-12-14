# Android 动画

## 1. 帧动画(Frame Animation)

• 利用 xml 实现逐帧动画。使用较少，可以用gif替代。

res/drawable 下新建 xml 文件

```
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

• 实现基本的动画效果：平移、旋转、缩放、透明度。没有改变View属性，只是改变视觉效果。动画效果单一。

1. AlphaAnimation（透明度动画）
2. RotateAnimation（旋转动画）
3. ScaleAnimation（缩放动画）
4. TranslateAnimation（位移动画）
5. AnimationSet 复合动画

```
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

# 6. 转场动画（Android 5.0）

1. explode（分解）
2. slide（滑动）
3. fade（淡入）
4. share（共享元素）

## 7. 约束布局关键帧动画

## 8. 视图状态动画

## 9. 矢量图动画

1. 起始动画 VectorDrawable文件 drawable/vector_drawable_satisfied.xml

```
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24.0"
    android:viewportHeight="24.0">
    <group
        android:name="group"
        android:pivotX="12.0"
        android:pivotY="12.0">
        <path
            android:fillColor="#FF000000"
            android:pathData="M11.99,2 C 6.47,2 2,6.48 2,12 s 4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2z
                              M12,20c-4.42,0 -8,-3.58 -8,-8s3.58,-8 8,-8 8,3.58 8,8 -3.58,8 -8,8z
                              M15.5,9.5m-1.5,0a1.5,1.5 0,1 1,3 0a1.5,1.5 0,1 1,-3 0z
                              M8.5,9.5m-1.5,0a1.5,1.5 0,1 1,3 0a1.5,1.5 0,1 1,-3 0z" />
        <path
            android:name="satisfied"
            android:fillColor="#FF000000"
            android:pathData="M12,16c-1.48,0 -2.75,-0.81 -3.45,-2 L6.88,14c0.8,2.05 2.79,3.5 5.12,3.5s4.32,-1.45 5.12,-3.5h-1.67c-0.7,1.19 -1.97,2 -3.45,2" />
    </group>
</vector>
```

2. 属性动画 anim/anim_path_type.xml

```
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:ordering="sequentially">
    <objectAnimator
        android:duration="1000"
        android:propertyName="pathData"
        android:valueFrom="M12,16c-1.48,0 -2.75,-0.81 -3.45,-2 L6.88,14c0.8,2.05 2.79,3.5 5.12,3.5s4.32,-1.45 5.12,-3.5h-1.67c-0.7,1.19 -1.97,2 -3.45,2"
        android:valueTo="M12,14 c-2.33,0 -4.32,1.45 -5.12,3.5 L 8.55,17.5 c0.69,-1.19 1.97,-2 3.45,-2s2.75,0.81 3.45,2h1.67c-0.8,-2.05 -2.79,-3.5 -5.12,-3.5"
        android:valueType="pathType" />
    <objectAnimator
        android:duration="1000"
        android:propertyName="pathData"
        android:valueFrom="M12,14 c-2.33,0 -4.32,1.45 -5.12,3.5 L 8.55,17.5 c0.69,-1.19 1.97,-2 3.45,-2s2.75,0.81 3.45,2h1.67c-0.8,-2.05 -2.79,-3.5 -5.12,-3.5"
        android:valueTo="M12,16c-1.48,0 -2.75,-0.81 -3.45,-2 L6.88,14c0.8,2.05 2.79,3.5 5.12,3.5s4.32,-1.45 5.12,-3.5h-1.67c-0.7,1.19 -1.97,2 -3.45,2"
        android:valueType="pathType" />
</set>
```

3. AnimatedVector文件 drawable/vector_animated_satisfied.xml

```
<?xml version="1.0" encoding="utf-8"?>
<animated-vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:drawable="@drawable/vector_drawable_satisfied">
    <target
        android:name="group"
        android:animation="@anim/anim_rotation" />
    <target
        android:name="satisfied"
        android:animation="@anim/anim_satisfied" />
</animated-vector>
```

### ordering

ordering 有两个选项：sequentially 和 together 。其中 together 为默认项。

1. sequentially 表示 set 中的动画，按照先后顺序逐步进行（a 完成之后进行 b ）。
2. together 表示set 中的动画，在同一时间同时进行。

### pathData

大写的字母是基于原点的坐标系(偏移量)，即绝对位置；小写字母是基于当前点坐标系(偏移量)，即相对位置。

1. 移动：

* M x,y (m dx, dy) 移动虚拟画笔到对应的点，但是并不绘制。一开始的时候默认是在(0,0)。

2. 直线：

* L x,y (l dx, dy) 从当前点划一条直线到对应的点。
* H x (h dx) 从当前点绘制水平线，相当于l x,0
* Y y (v dy) 从当前点绘制垂直线，相当于l 0,y

3. 闭合

Z(或z) 从结束点绘制一条直线到开始点，闭合路径
