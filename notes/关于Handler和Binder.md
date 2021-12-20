# 关于Handler和Binder

## 1. 一个线程有几个Handler？

可以创建无数个Handler，但是他们使用的消息队列都是同一个，也就是同一个Looper。

## 2. Handler 内存泄漏原因？为什么其他的内部类没有说过有这个问题？

Message持有Handler的引用，Handler又持有Activity的引用。解决：使用static+弱引用。

## 3. 为何主线程可以new Handler？如果想要在子线程中new Handler要做些什么准备？

主线程已经对Looper进行了prepare()操作，所以可以直接在主线程new Handler。如果想在子线程中new Handler，则需要先手动调用Looper的prepare()方法初始化Looper，再调用Looper的loop()方法。

## 4. 子线程中维护的Looper，消息队列无消息的时候的处理方案是什么?有什么用？

如果不处理的话，会阻塞线程，处理方案是调用Looper的quitSafely()； 这个方法会调用MessageQueue的quit()方法，清空所有的Message，并调用nativeWake()方法唤醒之前被阻塞的nativePollOnce()，使得方法next()方法中的for循环继续执行，接下来发现Message为null后就会结束循环，Looper结束。如此便可以释放内存和线程。

## 5. 既然可以存在多个Handler往MessageQueue中添加数据(发消息时各个Handler可能处于不同线程)，那它内部是如何确保线程安全的？

添加消息的方法enqueueMessage()中有synchronize修饰，取消息的方法next()中也有synchronize修饰。由于上述的加锁操作，所以时间不能保证完全准确。

## 6. 我们使用Message时应该如何创建它？

使用Message的obtain()方法创建，直接new出来容易造成内存抖动。内部维护了一个Message池，其是一个链表结构，当调用obtain()的时候会复用表头的Message。

## 7. Looper死循环为什么不会导致应用卡死？

卡死就是ANR，产生的原因有2个：

1. 在5s内没有响应输入的事件(例如按键，屏幕触摸等)
2. BroadcastReceiver在10s内没有执行完毕。

应用运行到虚拟机之后，首先它要执行的就是启动ActivityThread，在ActivityThread中，它又会启动它的main()函数。所以在程序运行的时候，主线程所有的代码都运行在这个Looper里面。 也就是说应用所有生命周期的函数（包括Activity、Service所有生命周期）都运行在这个Looper里面，而且，它们都是以消息的方式存在的。 所以在没有消息产生的时候，looper会被block(阻塞)，主线程会进入休眠，一旦有输入事件或者Looper添加消息的操作后会唤醒这个Looper，从而对事件进行响应，所以不会导致ANR。 简单来说looper的阻塞表明没有事件输入，而ANR是由于有事件没响应导致，所以looper的死循环并不会导致应用卡死。

## 8. Binder有什么优势？

不需要事先分配控件存取内存，节省空间。并且只进行一次拷贝。

## 9. Binder是如何做到一次拷贝的？

Binder借助了内存映射的方法，在内核空间和接收方用户空间的数据缓存区之间做了一层内存映射，就相当于直接拷贝到了接收方用户空间的数据缓存区，从而减少了一次数据拷贝

## 10. MMAP的原理讲解

mmap是一种内存映射文件的方法，即将一个文件或者其它对象映射到进程的地址空间，实现文件磁盘地址和进程虚拟地址空间中一段虚拟地址的一一对映关系。

1. 进程启动映射过程，并在虚拟地址空间中为映射创建虚拟映射区域。
2. 调用内核空间的系统调用函数mmap（不同于用户空间函数），实现文件物理地址和进程虚拟地址的一一映射关系。
3. 进程发起对这片映射空间的访问，引发缺页异常，实现文件内容到物理内存（主存）的拷贝。

mmap优点总结：

1. 对文件的读取操作跨过了页缓存，减少了数据的拷贝次数，用内存读写取代I/O读写，提高了文件读取效率。
2. 实现了用户空间和内核空间的高效交互方式。
3. 提供进程间共享内存及相互通信的方式。
4. 可用于实现高效的大规模数据传输。

## 11. 为什么Intent不能传递大数据？

因为Intent内部使用了 Binder 通信机制，Binder 事务缓冲区限制了传递数据的大小。 解决方案：通过 EventBus 粘性事件来传递数据。

## 12. 描述AIDL生成的java类细节

## 13. 四大组件底层的通信机制

## 14. Binder机制原理

* Android系统的Binder机制中，是有Client,Service,ServiceManager,Binder驱动程序组成的，其中Client，service，Service Manager运行在用户空间，Binder驱动程序是运行在内核空间的。 
* Binder就是把这4种组件粘合在一块的粘合剂，其中核心的组件就是Binder驱动程序。
* Service Manager提供辅助管理的功能，而Client和Service正是在Binder驱动程序和Service Manager提供的基础设施上实现C/S 之间的通信。
* Binder驱动程序提供设备文件/dev/binder与用户控件进行交互，Client、Service，Service Manager通过open和ioctl文件操作相应的方法与Binder驱动程序进行通信。
* Client和Service之间的进程间通信是通过Binder驱动程序间接实现的。
* Binder Manager是一个守护进程，用来管理Service，并向Client提供查询Service接口的能力。
