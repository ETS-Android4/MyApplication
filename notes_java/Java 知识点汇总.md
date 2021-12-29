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

1. 继承Thread类
2. 实现Runnable接口
3. 实现Callable接口，继承FutureTask类

## run 和 start 方法的区别

run方法是线程默认执行的方法，start方法是启动线程的方法。单独调用run方法是同步执行，通过start方法调用run方法是异步执行。

## 线程安全的集合

1. HashTable
2. ConcurrentHashMap

## synchronized 和 lock 的区别

### synchronized

1. synchronized是java的关键字
2. synchronized无法判断锁的状态
3. synchronized会自动释放锁
4. A线程阻塞，B线程会一直等待
5. 可重入，不可中断，非公平
6. 少量代码块

### lock

1. lock是java类
2. lock可以判断是否获取到锁
3. lock需在finally中手工释放锁
4. 可以尝试获取锁，不需要一直等待
5. 可重入，可中断，可公平
6. 大量代码块

## sleep 和 wait 的区别

### sleep

1. 暂停当前线程，不释放锁
2. Thread方法
3. 任何场景下都可以使用
4. 时间执行完毕才能释放

### wait

1. 暂停当前线程，释放锁
2. Object方法
3. 只能在同步代码块中使用
4. 可以随时唤醒（notify方法）

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