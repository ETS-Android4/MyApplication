# Android 架构

## MVC

1. 视图层（View）：对应于xml布局文件和java代码动态view部分。
2. 控制层（Controller）：主要负责业务逻辑，在android中由Activity承担，同时因为XML视图功能太弱，所以Activity既要负责视图的显示又要加入控制逻辑，承担的功能过多。
3. 模型层（Model）：主要负责网络请求，数据库处理，I/O的操作，即页面的数据来源。

### 因此MVC架构在android平台上的主要存在以下问题：

1. Activity同时负责View与Controller层的工作，违背了单一职责原则。
2. Model层与View层存在耦合，存在互相依赖，违背了最小知识原则。


## MVP

1. View层：对应于Activity与XML,只负责显示UI,只与Presenter层交互，与Model层没有耦合。
2. Presenter层：主要负责处理业务逻辑，通过接口回调View层。
3. Model层：主要负责网络请求，数据库处理等操作，这个没有什么变化。

### MVP架构同样有自己的问题：

1. Presenter层通过接口与View通信，实际上持有了View的引用。
2. 但是随着业务逻辑的增加，一个页面可能会非常复杂，这样就会造成View的接口会很庞大。

## MVVM

1. View观察ViewModel的数据变化并自我更新,这其实是单一数据源而不是双向数据绑定，所以其实MVVM的这一大特性我其实并没有用到。
2. View通过调用ViewModel提供的方法来与ViewModel交互。

### MVVM架构主要有以下不足：

1. 为保证对外暴露的LiveData是不可变的，需要添加不少模板代码并且容易遗忘。
2. View层与ViewModel层的交互比较分散零乱，不成体系。

## MVI

1. Model: 与MVVM中的Model不同的是，MVI的Model主要指UI状态（State）。例如页面加载状态、控件位置等都是一种UI状态。
2. View: 与其他MVX中的View一致，可能是一个Activity或者任意UI承载单元。MVI中的View通过订阅Intent的变化实现界面刷新。（注意：这里不是Activity的Intent）
3. Intent: 此Intent不是Activity的Intent，用户的任何操作都被包装成Intent后发送给Model层进行数据请求。


1. Model层承载UI状态，并暴露出ViewState供View订阅，ViewState是个data class,包含所有页面状态。
2. View层通过Action更新ViewState，替代MVVM通过调用ViewModel方法交互的方式。

### 单向数据流

1. 用户操作以Intent的形式通知Model。
2. Model基于Intent更新State。
3. View接收到State变化刷新UI。

1 强调数据单向流动，很容易对状态变化进行跟踪和回溯。
2 使用ViewState对State集中管理，只需要订阅一个 ViewState 便可获取页面的所有状态，相对 MVVM 减少了不少模板代码。
3. ViewModel通过ViewState与Action通信，通过浏览ViewState 和 Aciton 定义就可以理清 ViewModel 的职责，可以直接拿来作为接口文档使用。


### 当然MVI也有一些缺点，比如：

1. 所有的操作最终都会转换成State，所以当复杂页面的State容易膨胀。
2. state是不变的，因此每当state需要更新时都要创建新对象替代老对象，这会带来一定内存开销。