# Android Jetpack

## Lifecycle

1. Lifecycle 库通过在 SupportActivity 的 onCreate 中注入 ReportFragment 来感知发生命周期；
2. Lifecycle 抽象类，是 Lifecycle 库的核心类之一，它是对生命周期的抽象，定义了生命周期事件以及状态，通过它我们可以获取当前的生命周期状态，同时它也奠定了观察者模式的基调；（我是党员你看出来了吗:-D）
3. LifecycleOwner ，描述了一个拥有生命周期的组件，可以自己定义，不过通常我们不需要，直接使用 AppCompatActivity 等即可；
4. LifecycleRegistry 是Lifecycle的实现类，它负责接管生命周期事件，同时也负责Observer` 的注册以及通知；
5. ObserverWithState ，是 Observer 的一个封装类，是它最终 通过 ReflectiveGenericLifecycleObserve 调用了我们用注解修饰的方法；
6. LifecycleObserver ，Lifecycle 的观察者，利用它我们可以享受 Lifecycle 带来的能力；
7. ReflectiveGenericLifecycleObserver，它存储了我们在 Observer 里注解的方法，并在生命周期发生改变的时候最终通过反射的方式调用对应的方法。

## LiveData

## ViewModel