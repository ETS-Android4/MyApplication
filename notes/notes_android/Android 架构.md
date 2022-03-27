# Android 架构

## MVC

1. View： Layout XML 文件；
2. Model： 负责管理业务数据逻辑，如网络请求、数据库处理；
3. Controller： Activity 负责处理表现逻辑。

* Activity 里糅合了视图和业务的代码，分离程度不够

## MVP

1. View： Activity 和 Layout XML 文件；
2. Model： 负责管理业务数据逻辑，如网络请求、数据库处理；
3. Presenter： 负责处理表现逻辑。

* 双向依赖： View 和 Presenter 是双向依赖的，一旦 View 层做出改变，相应地 Presenter 也需要做出调整。在业务语境下，View 层变化是大概率事件；
* 内存泄漏风险： Presenter 持有 View 层的引用，当用户关闭了 View 层，但 Model 层仍然在进行耗时操作，就会有内存泄漏风险。虽然有解决办法，但还是存在风险点和复杂度（弱引用 / onDestroy() 回收 Presenter）。
* 协议接口类膨胀： View 层和 Presenter 层的交互需要定义接口方法，当交互非常复杂时，需要定义很多接口方法和回调方法，也不好维护。

## MVVM

1. View： Activity 和 Layout XML 文件，与 MVP 中 View 的概念相同； 
2. Model： 负责管理业务数据逻辑，如网络请求、数据库处理，与 MVP 中 Model 的概念相同； 
3. ViewModel： 存储视图状态，负责处理表现逻辑，并将数据设置给可观察数据容器。

* 多数据流： View 与 ViewModel 的交互分散，缺少唯一修改源，不易于追踪；
* LiveData 膨胀： 复杂的页面需要定义多个 MutableLiveData，并且都需要暴露为不可变的 LiveData。

## MVI

1. View： Activity 和 Layout XML 文件，与 MVVM 中 View 的概念相同；
2. Intent： 定义数据操作，是将数据传到 Model 的唯一来源，相比 MVVM 是新的概念；
3. ViewModel： 存储视图状态，负责处理表现逻辑，并将 ViewState 设置给可观察数据容器；
4. ViewState： 一个数据类，包含页面状态和对应的数据。

* 唯一可信源： 数据只有一个来源（ViewModel），与 MVVM 的思想相同；
* 单数据流： View 和 ViewModel 之间只有一个数据流，只有一个地方可以修改数据，确保数据是安全稳定的。并且 View 只需要订阅一个 ViewState 就可以获取所有状态和数据，相比 MVVM 是新的特性；
* 响应式： ViewState 包含页面当前的状态和数据，View 通过订阅 ViewState 就可以完成页面刷新，相比于 MVVM 是新的特性。

* State 膨胀： 所有视图变化都转换为 ViewState，还需要管理不同状态下对应的数据。实践中应该根据状态之间的关联程度来决定使用单流还是多流；
* 内存开销： ViewState 是不可变类，状态变更时需要创建新的对象，存在一定内存开销；
* 局部刷新： View 根据 ViewState 响应，不易实现局部 Diff 刷新，可以使用 Flow#distinctUntilChanged() 来刷新来减少不必要的刷新。
