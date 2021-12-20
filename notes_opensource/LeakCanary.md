## LeakCanary原理介绍

1. RefWatcher.watch()创建了一个KeyedWeakReference用于去观察对象。

2. 然后，在后台线程中，它会检测引用是否被清除了，并且是否没有触发GC。

3. 如果引用仍然没有被清除，那么它将会把堆栈信息保存在文件系统中的.hprof文件里。

4. HeapAnalyzerService被开启在一个独立的进程中，并且HeapAnalyzer使用了HAHA开源库解析了指定时刻的堆栈快照文件heap dump。

5. 从heap dump中，HeapAnalyzer根据一个独特的引用key找到了KeyedWeakReference，并且定位了泄露的引用。

6. HeapAnalyzer为了确定是否有泄露，计算了到GC Roots的最短强引用路径，然后建立了导致泄露的链式引用。

7. 这个结果被传回到app进程中的DisplayLeakService，然后一个泄露通知便展现出来了。

## LeakCanary对于内存泄漏的检测非常有效，但也并不是所有的内存泄漏都能检测出来。

* 无法检测出Service中的内存泄漏问题
* 如果最底层的MainActivity一直未走onDestroy生命周期(它在Activity栈的最底层)，无法检测出它的调用栈的内存泄漏。