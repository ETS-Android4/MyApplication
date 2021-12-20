# JAVA 基础知识

## String,StringBuffer,StringBuilder区别

1. 三者在执行速度上：StringBuilder > StringBuffer > String (由于String是常量，不可改变，拼接时会重新创建新的对象)。
2. StringBuffer是线程安全的，StringBuilder是线程不安全的。（由于StringBuffer有缓冲区）

## Java 集合框架

* Set：代表无序、不可重复的集合；
* List：代表有序、重复的集合；
* Map：代表具有映射关系的集合

## ArrayList 与 LinkedList

* ArrayList 基于数组实现，查找快：o(1)，增删慢：o(n)
* LinkedList 基于双向链表实现，查找慢：o(n)，增删快：o(1)。 
* 当需要对数据进行对此访问的情况下选用ArrayList，当需要对数据进行多次增加删除修改时采用LinkedList。

## HashMap 与 HashTable

* HashMap 是非 synchronized 的，性能更好，HashMap 可以接受为 null 的 key-value。
* Hashtable 是线程安全的，比 HashMap 要慢，不接受 null 的 key-value。

# JAVA 线程，多线程

## 开启线程的三种方式

## run() 和 start() 方法的区别

## 线程安全的集合

1. HashTable
2. ConcurrentHashMap

## lock 和 synchronized的区别

## Thread wait 和 sleep的区别

* sleep()方法是Thread类里面的，主要的意义就是让当前线程停止执行，让出cpu给其他的线程，但是不会释放对象锁资源以及监控的状态，当指定的时间到了之后又会自动恢复运行状态。
* wait()方法是Object类里面的，主要的意义就是让线程放弃当前的对象的锁，进入等待此对象的等待锁定池，只有针对此对象调动notify方法后本线程才能够进入对象锁定池准备获取对象锁进入运行状态。

# JAVA 网络基础知识

## TCP 三次握手

1. A：你能听到吗？
2. B：我能听到，你能听到吗？
3. A：我能听到，开始吧

## TCP 四次挥手

1. A：我说完了
2. B：我知道了，等一下，我可能还没说完
3. B：我也说完了
4. A：我知道了，结束吧 TCP 和 UDP 区别

## TCP 和 UDP 的区别

* tcp是面向连接的，由于tcp连接需要三次握手，所以能够最低限度的降低风险，保证连接的可靠性。
* udp不是面向连接的，udp建立连接前不需要与对象建立连接，无论是发送还是接收，都没有发送确认信号。所以说udp是不可靠的。

> 由于udp不需要进行确认连接，使得UDP的开销更小，传输速率更高，所以实时行更好。

## POST 和 GET 区别

* Get 参数放在 url 中；
* Post 参数放在 request Body 中 Get 可能不安全，因为参数放在 url 中