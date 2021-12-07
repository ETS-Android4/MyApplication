# Glide

## 假如让你自己写个图片加载框架，你会考虑哪些问题？

https://www.cnblogs.com/Android-Alvin/p/12672294.html

1. 异步加载：线程池
2. 切换线程：Handler，没有争议吧
3. 缓存：LruCache、DiskLruCache
4. 防止OOM：软引用、LruCache、图片压缩、Bitmap像素存储位置
5. 内存泄露：注意ImageView的正确引用，生命周期管理
6. 列表滑动加载的问题：加载错乱、队满任务过多问题

## Glide 生命周期

1.Glide如何绑定Activity、Fragment生命周期。
2.Glide如何监听内存变化、网络变化。
3.Glide如何处理请求的生命周期。