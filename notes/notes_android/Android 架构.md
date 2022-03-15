# Android 架构

## MVC

1. 视图层（View）：对应于xml布局文件和java代码动态view部分。
2. 控制层（Controller）：主要负责业务逻辑，在android中由Activity承担，同时因为XML视图功能太弱，所以Activity既要负责视图的显示又要加入控制逻辑，承担的功能过多。
3. 模型层（Model）：主要负责网络请求，数据库处理，I/O的操作，即页面的数据来源。

> MVC架构的主要问题在于Activity承担了View与Controller两层的职责，同时View层与Model层存在耦合。

## MVP

1. View层：对应于Activity与XML,只负责显示UI,只与Presenter层交互，与Model层没有耦合。
2. Presenter层：主要负责处理业务逻辑，通过接口回调View层。
3. Model层：主要负责网络请求，数据库处理等操作，这个没有什么变化。

> MVP的问题在于随着业务逻辑的增加，View的接口会很庞大，MVVM架构通过双向数据绑定可以解决这个问题。

## MVVM

1. View层：自动反映在 ViewModel
2. ViewModel层：采用双向数据绑定（data-binding）
3. Model层：主要负责网络请求，数据库处理等操作，这个没有什么变化。

> MVVM的双向数据绑定主要通过DataBinding实现，一般使用View通过LiveData等观察ViewModel的数据变化并自我更新

## MVI

1. Model：与MVVM中的Model不同的是，MVI的Model主要指UI状态（State）。例如页面加载状态、控件位置等都是一种UI状态。
2. View：与其他MVX中的View一致，可能是一个Activity或者任意UI承载单元。MVI中的View通过订阅Intent的变化实现界面刷新。
3. Intent：此Intent不是Activity的Intent，用户的任何操作都被包装成Intent后发送给Model层进行数据请求。