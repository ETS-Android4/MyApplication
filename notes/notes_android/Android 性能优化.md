# Android 性能优化

## 1. 布局优化

1. 删除布局中无用的控件和层次，其次有选择地使用性能比较低的ViewGroup
2. 使用include(引用布局)、merge(减少层级)、ViewStub(不参与绘制)布局标签
3. 避免过度绘制

## 2. 绘制优化

4. onDraw中不要创建新的局部对象
5. onDraw方法中不要做耗时的任务，也不做过多的循环操作

## 3. 内存优化

6. 尽量避免static的使用
7. 尽量使用ApplicationContext
8. 使用WeakReference弱引用代替强引用
9. 解决各个情况下的内存泄漏，注意平时代码的规范。

## 4. 启动速度优化

10. 闪屏页优化
11. 避免在启动时做密集沉重的初始化（Heavy app initialization）
12. 避免I/O操作、反序列化、网络操作、布局嵌套等。

## 5. 包体优化

13. 代码混淆
14. 资源优化

## 6. ListView/RecyclerView 和 Bitmap优化

15. ListView使用ViewHolder，分页加载
16. 压缩Bitmap

### RecyclerView

1. 尽量减少Item布局嵌套 • 写死ItemView的布局宽高，设置RecyclerView.setHasFixedSize(true)，避免重新计算大小
2. 在onBindViewHolder 或 getView 方法中，减少逻辑判断，减少临时对象创建。
3. 避免整个列表的数据更新，只更新受影响的布局。例如，加载更多时，不使用notifyDataSetChanged，而是使用notifyItemRangeInserted(
   rangeStart,rangeEnd)

## 7. 线程优化

17. 尽量采用线程池，避免在程序中创建大量的线程。

## 8. 其他性能优化

18. 避免过度的创建对象
19. 不要过度使用枚举，枚举占用的内存空间要比整型大
20. 常量使用static final来修饰
21. 使用一些Android特有的数据结构，比如SparseArray和Pair等
22. 适当采用软引用和弱引用
23. 采用内存缓存和磁盘缓存
24. 尽量采用静态内部类，这样可以避免潜在的由于内部类而导致的内存泄漏。
