## Java 集合框架
Set：代表无序、不可重复的集合；
List：代表有序、重复的集合；
Map：代表具有映射关系的集合

## HashMap 与 HashTable
HashMap 是非 synchronized 的，性能更好，HashMap 可以接受为 null 的 key-value。
Hashtable 是线程安全的，比 HashMap 要慢，不接受 null 的 key-value。

## ArrayList 与 LinkedList
ArrayList 基于数组实现，查找快：o(1)，增删慢：o(n)
LinkedList 基于双向链表实现，查找慢：o(n)，增删快：o(1)。
当需要对数据进行对此访问的情况下选用ArrayList，当需要对数据进行多次增加删除修改时采用LinkedList。

## ArrayBlockingQueue 与 LinkedBlockingQueue
ArrayBlockingQueue和ArrayList一样，内部基于数组来存放元素
LinkedBlockingQueue则和LinkedList一样，内部基于链表来存放元素。
LinkedBlockingQueue如果不指定容量，默认为Integer.MAX_VALUE

## synchronized和lock的区别
1.synchronized是java内置关键字，在jvm层面，Lock是java类；
2.synchronized无法判断是否获取锁的状态，Lock可以判断是否获取到锁；
3.synchronized会自动释放锁(a 线程执行完同步代码会释放锁 ；b 线程执行过程中发生异常会释放锁)，Lock需在finally中手工释放锁（unlock()方法释放锁），否则容易造成线程死锁；
4.用synchronized关键字的两个线程1和线程2，如果当前线程1获得锁，线程2线程等待。如果线程1阻塞，线程2则会一直等待下去，而Lock锁就不一定会等待下去，如果尝试获取不到锁，线程可以不用一直等待就结束了；
5.synchronized的锁可重入、不可中断、非公平，而Lock锁可重入、可判断、可公平
6.Lock锁适合大量同步的代码的同步问题，synchronized锁适合代码少量的同步问题。
7.Lock是一个接口，synchronized是一个关键字，synchronized放弃锁只有两种情况：①线程执行完了同步代码块的内容②发生异常；而lock不同，它可以设定超时时间，也就是说他可以在获取锁时便设定超时时间，如果在你设定的时间内它还没有获取到锁，那么它会放弃获取锁然后响应放弃操作

https://www.cnblogs.com/iyyy/p/7993788.html

## TCP
TCP 三次握手

A：你能听到吗？
B：我能听到，你能听到吗？
A：我能听到，开始吧
TCP 四次挥手

A：我说完了
B：我知道了，等一下，我可能还没说完
B：我也说完了
A：我知道了，结束吧
TCP 和 UDP 区别

TCP 面向连接；可靠；有序；面向字节流；速度慢；较重量；全双工；适用于文件传输、浏览器等
UDP 无连接；不可靠；无序；面向报文；速度快；轻量；适用于即时通讯、视频通话等
HTTP 相关
POST 和 GET 区别
Get 参数放在 url 中；Post 参数放在 request Body 中
Get 可能不安全，因为参数放在 url 中
HTTPS
HTTP 是超文本传输协议，明文传输；HTTPS 使用 SSL 协议对 HTTP 传输数据进行了加密
HTTP 默认 80 端口；HTTPS 默认 443 端口
优点：安全
缺点：费时、SSL 证书收费，加密能力还是有限的，但是比 HTTP 强多了