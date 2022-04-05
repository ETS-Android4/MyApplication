# Retrofit

> https://github.com/square/retrofit

* Retrofit是网络调度层，做具体业务请求、线程切换、数据转换

## Retrofit 简单使用

```
// （1）定义API接口
public interface NetworkService {
  @POST("app/user/main.html")
  Call<ResponseBody> getInfo(@Query("userId") String id);
} 
// （2）创建 Retrofit 对象
Retrofit retrofit = new Retrofit.Builder()
.baseUrl(Urls.Url_Base)// baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
.build();
// （3）创建网络请求接口实例
NetworkService service = retrofit.create(NetworkService.class);
// （4）调用网络接口中的方法获取 Call 对象
Call<ResponseBody> call = service.getInfo("");
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

1. （1）Proxy 的 newProxyInstance() 方法就已经创建了网络请求接口的实例
2. （2）调用网络请求接口中的方法（例如例子中 service.getInfo()）的时候才会执行 invoke() 方法
3. （3）如果该方法是 Object 的方法，例如 equals()、toString()，那就直接调用
4. （4）如果该方法是 Java8 默认方法，则执行该平台的默认方法。
5. （5）如果前面2种都不符合，则会调用 loadServiceMethod() 方法拿到 ServiceMethod 对象
6. （6）拿到 ServiceMethod 对象后调用 invoke() 方法

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
                    : loadServiceMethod(method) // (5)
                    .invoke(args); //（6）
        }
    });
}
```

### loadServiceMethod() 获取当前方法的 ServiceMethod

首先会从 ServiceMethod 缓存集合中取出当前方法对应的 ServiceMethod，如果不为空，则直接返回。如果为空，则使用单例模式创建一个新的 ServiceMethod，即调用 parseAnnotations() 方法创建，然后再存到缓存集合中。

```
  ServiceMethod<?> loadServiceMethod(Method method) {
    ServiceMethod<?> result = serviceMethodCache.get(method);
    if (result != null) return result;

    synchronized (serviceMethodCache) {
      result = serviceMethodCache.get(method);
      if (result == null) {
        result = ServiceMethod.parseAnnotations(this, method);
        serviceMethodCache.put(method, result);
      }
    }
    return result;
  }
```

### parseAnnotations 创建 ServiceMethod

调用 RequestFactory 的 parseAnnotations() 方法解析网络请求接口中的注解。

```
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);

    Type returnType = method.getGenericReturnType();
    if (Utils.hasUnresolvableType(returnType)) {
      throw methodError(
          method,
          "Method return type must not include a type variable or wildcard: %s",
          returnType);
    }
    if (returnType == void.class) {
      throw methodError(method, "Service methods cannot return void.");
    }

    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
  }
```

### RequestFactory.parseAnnotations() 解析网络请求接口中的注解

```
  static RequestFactory parseAnnotations(Retrofit retrofit, Method method) {
    return new Builder(retrofit, method).build();
  }
```

### HttpServiceMethod.parseAnnotations()

主要用来来获取相对应的网络请求适配器（CallAdapter）、数据转换器（Converter）与网络请求器工厂（okhttp3.Call.Factory）

```
  static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
      Retrofit retrofit, Method method, RequestFactory requestFactory) {
    ```
    // 根据网络请求接口中的返回类型与注解获取相对应的网络请求适配器（CallAdapter）
    CallAdapter<ResponseT, ReturnT> callAdapter =
        createCallAdapter(retrofit, method, adapterType, annotations);
    // 获取响应类型，对应例子中是 PostmanGetBean
    Type responseType = callAdapter.responseType();
    if (responseType == okhttp3.Response.class) {
      throw methodError(method, "'" + getRawType(responseType).getName() + "' is not a valid response body type. Did you mean ResponseBody?");
    }
    if (responseType == Response.class) {
      throw methodError(method, "Response must include generic type (e.g., Response<String>)");
    }
    // TODO support Unit for Kotlin?
    if (requestFactory.httpMethod.equals("HEAD") && !Void.class.equals(responseType)) {
      throw methodError(method, "HEAD method must use Void as response type.");
    }

    // 根据网络请求接口中的返回类型与注解获取相对应的数据转换器（Converter）
    Converter<ResponseBody, ResponseT> responseConverter =
        createResponseConverter(retrofit, method, responseType);

    // 获取网络请求器工厂（okhttp3.Call.Factory）
    okhttp3.Call.Factory callFactory = retrofit.callFactory;
    if (!isKotlinSuspendFunction) {
      return new CallAdapted<>(requestFactory, callFactory, responseConverter, callAdapter);
    }
    ···
  }
```

### ServiceMethod.invoke()

调用了 CallAdapter 的 adapt() 方法来获取网络请求需要的 Call 对象

```
  // ServiceMethod
  abstract @Nullable T invoke(Object[] args);
  
  // HttpServiceMethod
  @Override
  final @Nullable ReturnT invoke(Object[] args) {
    Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
    return adapt(call, args);
  }
```

### 3. 发起网络请求