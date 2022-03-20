# Android 基础知识

## Activity

### 生命周期

#### onCreate()

* 状态：Activity 正在创建
* 任务：做初始化工作，如setContentView界面资源、初始化数据
* 注意：此方法的传参Bundle为该Activity上次被异常情况销毁时保存的状态信息。

#### onStart()

* 状态：Activity 正在启动，这时Activity 可见但不在前台，无法和用户交互。

#### onResume()

* 状态：Activity 获得焦点，此时Activity 可见且在前台并开始活动。

#### onPause()

* 状态： Activity 正在停止
* 任务：可做 数据存储、停止动画等操作。
* 注意：Activity切换时，旧Activity的onPause会先执行，然后才会启动新的Activity。

#### onStop()

* 状态：Activity 即将停止
* 任务：可做稍微重量级回收工作，如取消网络连接、注销广播接收器等。
* 注意：新Activity是透明主题时，旧Activity都不会走onStop。

#### onDestroy()

* 状态：Activity 即将销毁
* 任务：做回收工作、资源释放。

#### onRestart()

* 状态：Activity 重新启动，Activity由后台切换到前台，由不可见到可见。

### 启动Activity所执行的方法

#### 当AActivity切换BActivity

1. A: onPause()
2. B: onCreate()
3. B: onStart()
4. B: onResume()
5. A: onStop()

#### 当BActivity点击back键

1. B: onPause()
2. A: onRestart()
3. A: onStart()
4. A: onResume()
5. B: onStop()
6. B: onDestroy()

### 当Activity按home键，然后再打开应用

1. A: onPause()
2. A: onStop()
3.
4. A: onRestart()
5. A: onStart()
6. A: onResume()

### 启动模式

#### standard：标准模式、默认模式

* 含义：每次启动一个Activity就会创建一个新的实例。
*

注意：使用ApplicationContext去启动standard模式Activity就会报错。因为standard模式的Activity会默认进入启动它所属的任务栈，但是由于非Activity的Context没有所谓的任务栈。

#### singleTop：栈顶复用模式

* 含义：如果新Activity已经位于任务栈的栈顶，就不会重新创建，并回调onNewIntent(intent)方法。

#### singleTask：栈内复用模式

* 含义：只要该Activity在一个任务栈中存在，都不会重新创建，并回调onNewIntent(intent)
  方法。如果不存在，系统会先寻找是否存在需要的栈，如果不存在该栈，就创建一个任务栈，并把该Activity放进去；如果存在，就会创建到已经存在的栈中。

#### singleInstance：单实例模式

* 含义： 具有此模式的Activity只能单独位于一个任务栈中，且此任务栈中只有唯一一个实例。

## Fragment

### 声明周期

1. onAttach()：当Fragment和Activity建立关联时调用
2. onCreateView()：当Fragment创建视图时调用
3. onActivityCreated()：当与Fragment相关联的Activity完成onCreate()之后调用
4. onDestroyView()：在Fragment中的布局被移除时调用
5. onDetach()：当Fragment和Activity解除关联时调用
