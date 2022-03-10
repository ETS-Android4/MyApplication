# 如何避免OOM

## 1.使用更加轻量的数据结构

如使用ArrayMap/SparseArray替代HashMap,HashMap更耗内存，因为它需要额外的实例对象来记录Mapping操作，SparseArray更加高效，因为它避免了Key Value的自动装箱，和装箱后的解箱操作

## 2. 避免枚举的使用

可以用静态常量或者注解@IntDef替代

## 3. Bitmap优化

* a.尺寸压缩：通过InSampleSize设置合适的缩放
* b.颜色质量：设置合适的format，ARGB_6666/RBG_545/ARGB_4444/ALPHA_6，存在很大差异
* c.inBitmap:复用内存块，不需要在重新给这个bitmap申请一块新的内存,避免了一次内存的分配和回收，从而改善了运行效率

> 使用inBitmap，在4.4之前，只能重用相同大小的bitmap的内存区域，而4.4之后你可以重用任何bitmap的内存区域，只要这块内存比将要分配内存的bitmap大就可以。  
> 这里最好的方法就是使用LRUCache来缓存bitmap，后面来了新的bitmap，可以从cache中按照api版本找到最适合重用的bitmap，来重用它的内存区域。

## 4. StringBuilder替代String

在有些时候，代码中会需要使用到大量的字符串拼接的操作，这种时候有必要考虑使用StringBuilder来替代频繁的“+”

## 5. 避免在类似onDraw这样的方法中创建对象

因为它会迅速占用大量内存，引起频繁的GC甚至内存抖动

## 6. 减少内存泄漏也是一种避免OOM的方法
