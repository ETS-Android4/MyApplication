# IPC（跨进程通信）

## IPC方式

1. Bundle

四大组件间的进程通讯。要求传递数据能被序列化，实现 Parcelable、Serializable。

2. 文件共享

不适合高并发场景，无法做到进程间及时通讯。

3. AIDL

支持实时通讯，使用稍微复杂。一套可快速实现Binder的工具。

4. Messenger

支持实时通讯，数据通过Message传递，只能支持Bundle。基于 AIDL 实现，服务端串行处理，主要用于传递消息，适用于低并发一对多通信。

5. ContentProvider

基于 Binder 实现，适用于一对多进程间数据共享。Android提供的专门用来进行不同应用间数据共享的方式。

6. Socket

TCP、UDP，适用于网络数据交换。不仅可跨进程，还可以跨设备通信。

## Binder 机制

### 构成：

1. Client。客户端进程。
2. Server。服务端进程。
3. ServiceManager。提供注册、查询和返回代理服务对象的功能。
4. Binder驱动。主要负责建立进程间的Binder连接，进程间的数据交互等等底层操作。

### 流程：

1. 服务端通过Binder驱动在ServiceManager中注册我们的服务。
2. 客户端通过Binder驱动查询在ServiceManager中注册的服务。
3. ServiceManager通过Binder驱动返回服务端的代理对象。
4. 客户端拿到服务端的代理对象后即可进行进程间通信。
