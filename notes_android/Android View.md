# View

## View 与 SurfaceView 的区别

• View需要在UI线程对画面进行刷新，而SurfaceView可在子线程进行页面的刷新
• View适用于主动更新的情况，而SurfaceView适用于被动更新
• SurfaceView在底层已实现双缓冲机制，而View没有，因此SurfaceView更适用于需要频繁刷新、刷新时数据处理量很大的页面

## requestLayout、invalidate与postInvalidate的区别

• invalidate和postInvalidate都会使view重绘（即调用onDraw方法），不同的是，invalidate是在UI线程自身中使用，而postInvalidate在非UI线程中使用
• requestLayout：当当前布局的布局属性发生了变化的时候, 此时需要重新调用父view的onMeaure和onLayout, 来给子view重新排版布局