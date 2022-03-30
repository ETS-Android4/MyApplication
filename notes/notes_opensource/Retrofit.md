# Retrofit

> https://github.com/square/retrofit

* Retrofit是网络调度层，做具体业务请求、线程切换、数据转换

## Retrofit 简单使用

```
// （1）定义API接口
public interface NetworkService {
  @POST("app/user/main.html")
  Call<ResponseBody> call(@Query("userId") String id);
} 
// （2）创建 Retrofit 对象
Retrofit retrofit = new Retrofit.Builder()
.baseUrl(Urls.Url_Base)// baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
.build();
// （3）创建网络请求接口实例
NetworkService service = retrofit.create(NetworkService.class);
// （4）调用网络接口中的方法获取 Call 对象
Call<ResponseBody> call = service.call("");
// （5）进行网络请求
call.enqueue(new Callback<ResponseBody>() {
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull final Throwable t) {
    }
});
```

## 源码解析 2.9.0

### 1. 创建 Builder 对象

通过 Builder 模式创建 Retrofit 对象。初始化了一些配置，例如网络请求工厂、网络请求地址、数据转换器、请求适配器等

```
  public static final class Builder {
    private final Platform platform; // 选择平台，Java或者Android
    private @Nullable okhttp3.Call.Factory callFactory; // 网络请求工厂 默认okhttp
    private @Nullable HttpUrl baseUrl; // 网络请求地址
    private final List<Converter.Factory> converterFactories = new ArrayList<>(); // 数据转换器
    private final List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>(); // 请求适配器
    private @Nullable Executor callbackExecutor; // 回调线程池，切换线程
    private boolean validateEagerly; // 是否立即解析接口注解方法（ServiceMethod），开启则会马上加载所有方法。一般不用

    Builder(Platform platform) {
      this.platform = platform;
    }

    public Builder() {
      this(Platform.get()); // 获取当前执行的平台
    }
    ···
    public Retrofit build() {
      if (baseUrl == null) {
        throw new IllegalStateException("Base URL required.");
      }

      okhttp3.Call.Factory callFactory = this.callFactory;
      // 默认OkHttpClient
      if (callFactory == null) {
        callFactory = new OkHttpClient();
      }

      Executor callbackExecutor = this.callbackExecutor;
      // 如果没有设置回调执行器，就创建一个默认的主线程回调执行器
      if (callbackExecutor == null) {
        callbackExecutor = platform.defaultCallbackExecutor();
      }

      // 添加默认请求适配器
      List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>(this.callAdapterFactories);
      callAdapterFactories.addAll(platform.defaultCallAdapterFactories(callbackExecutor));

      // 添加默认数据解析器
      List<Converter.Factory> converterFactories = new ArrayList<>(1 + this.converterFactories.size() + platform.defaultConverterFactoriesSize());

      converterFactories.add(new BuiltInConverters());
      converterFactories.addAll(this.converterFactories);
      converterFactories.addAll(platform.defaultConverterFactories());

      return new Retrofit(
          callFactory,
          baseUrl,
          unmodifiableList(converterFactories),
          unmodifiableList(callAdapterFactories),
          callbackExecutor,
          validateEagerly);
    }
  }
```

### Platform.class

Retrofit 支持 Android 与 Java 平台。

```
class Platform {
  private static final Platform PLATFORM = findPlatform();

  static Platform get() {
    return PLATFORM;
  }

  private static Platform findPlatform() {
    return "Dalvik".equals(System.getProperty("java.vm.name"))
        ? new Android() //
        : new Platform(true);
  }

  static final class Android extends Platform {
    Android() {
      super(Build.VERSION.SDK_INT >= 24);
    }

    @Override
    public Executor defaultCallbackExecutor() {
      return new MainThreadExecutor();
    }

    static final class MainThreadExecutor implements Executor {
      private final Handler handler = new Handler(Looper.getMainLooper());

      @Override
      public void execute(Runnable r) {
        // 使用Hanlder.post回调到主线程
        handler.post(r);
      }
    }
  }
}
```

### 2. 动态代理模式创建接口实例

```
public <T> T create(final Class<T> service) {
    // 验证service必须是一个接口，并且不能继承其他接口，否则抛出异常
    validateServiceInterface(service);
    // （1）Proxy.newProxyInstance()
    return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new InvocationHandler() {
        private final Platform platform = Platform.get();
        private final Object[] emptyArgs = new Object[0];

        // （2）invoke()
        @Override
        public @Nullable Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);// （3）
            }
            args = args != null ? args : emptyArgs;
            return platform.isDefaultMethod(method)
                    ? platform.invokeDefaultMethod(method, service, proxy, args) //（4）
                    : loadServiceMethod(method).invoke(args); //（5）
        }
    });
}
```

### 3. 发起网络请求