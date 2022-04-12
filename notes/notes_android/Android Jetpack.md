# Android Jetpack

## Lifecycle

1. Lifecycle 库通过在 SupportActivity 的 onCreate 中注入 ReportFragment 来感知发生命周期；
2. Lifecycle 抽象类，是 Lifecycle 库的核心类之一，它是对生命周期的抽象，定义了生命周期事件以及状态，通过它我们可以获取当前的生命周期状态，同时它也奠定了观察者模式的基调；（我是党员你看出来了吗:-D）
3. LifecycleOwner ，描述了一个拥有生命周期的组件，可以自己定义，不过通常我们不需要，直接使用 AppCompatActivity 等即可；
4. LifecycleRegistry 是Lifecycle的实现类，它负责接管生命周期事件，同时也负责 Observer 的注册以及通知；
5. ObserverWithState ，是 Observer 的一个封装类，是它最终 通过 ReflectiveGenericLifecycleObserve 调用了我们用注解修饰的方法；
6. LifecycleObserver ，Lifecycle 的观察者，利用它我们可以享受 Lifecycle 带来的能力；
7. ReflectiveGenericLifecycleObserver，它存储了我们在 Observer 里注解的方法，并在生命周期发生改变的时候最终通过反射的方式调用对应的方法。

## LiveData

## ViewMode

## Flow

* LiveData 只能在主线程更新数据： 只能在主线程 setValue，即使 postValue 内部也是切换到主线程执行；
* LiveData 数据重放问题： 注册新的订阅者，会重新收到 LiveData 存储的数据，这在有些情况下不符合预期（可以使用自定义的 LiveData 子类 SingleLiveData 或 UnPeekLiveData 解决）；
* LiveData 不防抖： 重复 setValue 相同的值，订阅者会收到多次 onChanged() 回调（可以使用 distinctUntilChanged() 解决，此处不展开）；
* LiveData 不支持背压： 在数据生产速度 > 数据消费速度时，LiveData 无法正常处理。比如在子线程大量 postValue 数据但主线程消费跟不上时，中间就会有一部分数据被忽略。

* Flow 支持协程： Flow 基于协程基础能力，能够以结构化并发的方式生产和消费数据，能够实现线程切换（依靠协程的 Dispatcher）；
* Flow 支持背压： Flow 的子类 SharedFlow 支持配置缓存容量，可以应对数据生产速度 > 数据消费速度的情况；
* Flow 支持数据重放配置： Flow 的子类 SharedFlow 支持配置重放 replay，能够自定义对新订阅者重放数据的配置；
* Flow 相对 RxJava 的学习门槛更低： Flow 的功能更精简，学习性价比相对更高。不过 Flow 是基于协程，在协程会有一些学习成本，但这个应该拆分来看。
* Flow 不是生命周期感知型组件： Flow 不是 Android 生态下的产物，自然 Flow 是不会关心组件生命周期。

* Flow（冷流）： 冷流是不共享的，也没有缓存机制。冷流只有在订阅者 collect 数据时，才按需执行发射数据流的代码。冷流和订阅者是一对一的关系，多个订阅者间的数据流是相互独立的，一旦订阅者停止监听或者生产代码结束，数据流就自动关闭。
* SharedFlow / StateFlow（热流）： 热流是共享的，有缓存机制的。无论是否有订阅者 collect 数据，都可以生产数据并且缓存起来。热流和订阅者是一对多的关系，多个订阅者可以共享同一个数据流。当一个订阅者停止监听时，数据流不会自动关闭（除非使用 WhileSubscribed 策略）。