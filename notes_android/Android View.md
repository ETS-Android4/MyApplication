# View

## View 与 SurfaceView 的区别

* View需要在UI线程对画面进行刷新，而SurfaceView可在子线程进行页面的刷新
* View适用于主动更新的情况，而SurfaceView适用于被动更新
* SurfaceView在底层已实现双缓冲机制，而View没有，因此SurfaceView更适用于需要频繁刷新、刷新时数据处理量很大的页面

## requestLayout、invalidate与postInvalidate的区别

* invalidate和postInvalidate都会使view重绘（即调用onDraw方法），不同的是，invalidate是在UI线程自身中使用，而postInvalidate在非UI线程中使用
* requestLayout：当当前布局的布局属性发生了变化的时候, 此时需要重新调用父view的OnMeasure和onLayout, 来给子view重新排版布局

## View的绘制流程

OnMeasure()——>OnLayout()——>OnDraw()

第一步：OnMeasure()：测量视图大小。从顶层父View到子View递归调用measure方法，measure方法又回调OnMeasure。

第二步：OnLayout()：确定View位置，进行页面布局。从顶层父View向子View的递归调用view.layout方法的过程，即父View根据上一步measure子View所得到的布局大小和布局参数，将子View放在合适的位置上。

第三步：OnDraw()：绘制视图。ViewRoot创建一个Canvas对象，然后调用OnDraw()。

### Draw 的基本流程

```
// 绘制基本上可以分为六个步骤
public void draw(Canvas canvas) {
...
// 步骤一：绘制View的背景
drawBackground(canvas);
...
// 步骤二：如果需要的话，保持canvas的图层，为fading做准备
saveCount = canvas.getSaveCount();
...
canvas.saveLayer(left, top, right, top + length, null, flags);
...
// 步骤三：绘制View的内容
onDraw(canvas);
...
// 步骤四：绘制View的子View
dispatchDraw(canvas);
...
// 步骤五：如果需要的话，绘制View的fading边缘并恢复图层
canvas.drawRect(left, top, right, top + length, p);
...
canvas.restoreToCount(saveCount);
...
// 步骤六：绘制View的装饰(例如滚动条等等)
onDrawForeground(canvas)
}
```