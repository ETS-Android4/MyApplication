# Retrofit

> https://github.com/square/retrofit

* Retrofit是网络调度层，做具体业务请求、线程切换、数据转换

## Retrofit 简单使用

1. 创建 Retrofit 实例

```
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(url)//baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//设置RxJava
    .addConverterFactory(GsonConverterFactory.create())//设置数据解析器
    .build();
```

2. 定义API接口，并获取API实例

```
public interface InfoService {
  @POST("app/user/main.html")
  Call<ResponseBody> requestInfo(@Query("userId") String userId);
}  
InfoService service = retrofit.create（InfoService.class）;
```

3. 创建HTTP请求

```
Call<ResponseBody> repos = service.requestInfo("");
```

4. 执行网络请求

```
repos.enqueue(new Callback<ResponseBody>() {
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
    }
    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
    }
});
```

## 源码解析

1. 创建 Retrofit -> new Retrofit.Builder()

```
public final class Retrofit {
  private final Map<Method, ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<>();

  final okhttp3.Call.Factory callFactory; // okhttp网络请求工厂，默认okhtt
  final HttpUrl baseUrl; // 基地址
  final List<Converter.Factory> converterFactories; // 数据转换器
  final List<CallAdapter.Factory> callAdapterFactories; // 请求适配器
  final @Nullable Executor callbackExecutor; // 回调执行器，切换线程（子线程 ->>主线程）
  final boolean validateEagerly; // 是否立即解析接口注解方法，开启则会马上加载所有方法。一般不用。
  ···
  public static final class Builder {
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

      // //添加默认的请求适配器
      List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>(this.callAdapterFactories);
      callAdapterFactories.addAll(platform.defaultCallAdapterFactories(callbackExecutor));

      List<Converter.Factory> converterFactories = new ArrayList<>(1 + this.converterFactories.size() + platform.defaultConverterFactoriesSize());
      
      // 添加默认数据解析器
      converterFactories.add(new BuiltInConverters());
      converterFactories.addAll(this.converterFactories);
      converterFactories.addAll(platform.defaultConverterFactories());
      ···
    }
  }
}
```

Platform.class

```
class Platform {
  ···
  static final class Android extends Platform {
    Android() {
      super(Build.VERSION.SDK_INT >= 24);
    }

    @Override
    public Executor defaultCallbackExecutor() {
      return new MainThreadExecutor();
    }
    ···
    static final class MainThreadExecutor implements Executor {
      private final Handler handler = new Handler(Looper.getMainLooper());

      @Override
      public void execute(Runnable r) {
        handler.post(r);
      }
    }
  }
}
```

2. 网络请求接口的创建 -> Retrofit.create()

Retrofit.create()

```
  public <T> T create(final Class<T> service) {
    validateServiceInterface(service);
    return (T)
        Proxy.newProxyInstance(
            service.getClassLoader(),
            new Class<?>[] {service},
            new InvocationHandler() {
              private final Platform platform = Platform.get();
              private final Object[] emptyArgs = new Object[0];

              @Override
              public @Nullable Object invoke(Object proxy, Method method, @Nullable Object[] args)
                  throws Throwable {
                // If the method is a method from Object then defer to normal invocation.
                if (method.getDeclaringClass() == Object.class) {
                  return method.invoke(this, args);
                }
                args = args != null ? args : emptyArgs;
                return platform.isDefaultMethod(method)
                    ? platform.invokeDefaultMethod(method, service, proxy, args)
                    : loadServiceMethod(method).invoke(args);
              }
            });
  }
```

1. Retrofit.loadServiceMethod()

```
  /**
   * 将Method转为ServiceMethod对象并缓存起来
   * serviceMethodCache 是一个 ConcurrentHashMap实例
   */
  ServiceMethod<?> loadServiceMethod(Method method) {
    ServiceMethod<?> result = serviceMethodCache.get(method);
    if (result != null) return result;

    synchronized (serviceMethodCache) {
      result = serviceMethodCache.get(method);
      if (result == null) {
        // 解析 method
        result = ServiceMethod.parseAnnotations(this, method);
        serviceMethodCache.put(method, result);
      }
    }
    return result;
  }
```

ServiceMethod.class

```
abstract class ServiceMethod<T> {
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);
    ···
    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
  }

  abstract @Nullable T invoke(Object[] args);
}
```

2. RequestFactory.class

```
/**
 * 解析方法的注解参数
 */
final class RequestFactory {
  static RequestFactory parseAnnotations(Retrofit retrofit, Method method) {
    return new Builder(retrofit, method).build();
  }
  ···
  static final class Builder {
    ···
    RequestFactory build() {
      // 注解解析
      for (Annotation annotation : methodAnnotations) {
        parseMethodAnnotation(annotation);
      }
      ···
      int parameterCount = parameterAnnotationsArray.length;
      parameterHandlers = new ParameterHandler<?>[parameterCount];
      // 参数解析
      for (int p = 0, lastParameter = parameterCount - 1; p < parameterCount; p++) {
        parameterHandlers[p] =
            parseParameter(p, parameterTypes[p], parameterAnnotationsArray[p], p == lastParameter);
      }

      if (relativeUrl == null && !gotUrl) {
        throw methodError(method, "Missing either @%s URL or @Url parameter.", httpMethod);
      }
      if (!isFormEncoded && !isMultipart && !hasBody && gotBody) {
        throw methodError(method, "Non-body HTTP method cannot contain @Body.");
      }
      if (isFormEncoded && !gotField) {
        throw methodError(method, "Form-encoded method must contain at least one @Field.");
      }
      if (isMultipart && !gotPart) {
        throw methodError(method, "Multipart method must contain at least one @Part.");
      }

      return new RequestFactory(this);
    }
  }
}
```

3. HttpServiceMethod.parseAnnotations()

```
  static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(Retrofit retrofit, Method method, RequestFactory requestFactory) {
    ···
    Type adapterType;
    // 是否为kotlin的suspend方法
    if (isKotlinSuspendFunction) {
      ···
    } else {
      ···
    }

    // 找到一个合适的CallAdapter
    CallAdapter<ResponseT, ReturnT> callAdapter = createCallAdapter(retrofit, method, adapterType, annotations);
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

    // 找到一个合适的数据转换器Converter
    Converter<ResponseBody, ResponseT> responseConverter = createResponseConverter(retrofit, method, responseType);
    ···
  }
```

4. HttpServiceMethod.createCallAdapter()

```
  private static <ResponseT, ReturnT> CallAdapter<ResponseT, ReturnT> createCallAdapter(
      Retrofit retrofit, Method method, Type returnType, Annotation[] annotations) {
    try {
      //noinspection unchecked
      return (CallAdapter<ResponseT, ReturnT>) retrofit.callAdapter(returnType, annotations);
    } catch (RuntimeException e) { // Wide exception range because factories are user code.
      throw methodError(method, e, "Unable to create call adapter for %s", returnType);
    }
  }
```

Retrofit.callAdapter()

```
  public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
    return nextCallAdapter(null, returnType, annotations);
  }
  
  public CallAdapter<?, ?> nextCallAdapter(@Nullable CallAdapter.Factory skipPast, Type returnType, Annotation[] annotations) {
    Objects.requireNonNull(returnType, "returnType == null");
    Objects.requireNonNull(annotations, "annotations == null");

    int start = callAdapterFactories.indexOf(skipPast) + 1;
    for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
      CallAdapter<?, ?> adapter = callAdapterFactories.get(i).get(returnType, annotations, this);
      if (adapter != null) {
        return adapter;
      }
    }

    StringBuilder builder = new StringBuilder("Could not locate call adapter for ").append(returnType).append(".\n");
    if (skipPast != null) {
      builder.append("  Skipped:");
      for (int i = 0; i < start; i++) {
        builder.append("\n   * ").append(callAdapterFactories.get(i).getClass().getName());
      }
      builder.append('\n');
    }
    builder.append("  Tried:");
    for (int i = start, count = callAdapterFactories.size(); i < count; i++) {
      builder.append("\n   * ").append(callAdapterFactories.get(i).getClass().getName());
    }
    throw new IllegalArgumentException(builder.toString());
  }
```

5. HttpServiceMethod.createResponseConverter()

```
  private static <ResponseT> Converter<ResponseBody, ResponseT> createResponseConverter(
      Retrofit retrofit, Method method, Type responseType) {
    Annotation[] annotations = method.getAnnotations();
    try {
      return retrofit.responseBodyConverter(responseType, annotations);
    } catch (RuntimeException e) { // Wide exception range because factories are user code.
      throw methodError(method, e, "Unable to create converter for %s", responseType);
    }
  }
```

Retrofit.responseBodyConverter()

```
  public <T> Converter<ResponseBody, T> responseBodyConverter(Type type, Annotation[] annotations) {
    return nextResponseBodyConverter(null, type, annotations);
  }

  public <T> Converter<ResponseBody, T> nextResponseBodyConverter(@Nullable Converter.Factory skipPast, Type type, Annotation[] annotations) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(annotations, "annotations == null");

    int start = converterFactories.indexOf(skipPast) + 1;
    for (int i = start, count = converterFactories.size(); i < count; i++) {
      Converter<ResponseBody, ?> converter =
          converterFactories.get(i).responseBodyConverter(type, annotations, this);
      if (converter != null) {
        //noinspection unchecked
        return (Converter<ResponseBody, T>) converter;
      }
    }

    StringBuilder builder = new StringBuilder("Could not locate ResponseBody converter for ").append(type).append(".\n");
    if (skipPast != null) {
      builder.append("  Skipped:");
      for (int i = 0; i < start; i++) {
        builder.append("\n   * ").append(converterFactories.get(i).getClass().getName());
      }
      builder.append('\n');
    }
    builder.append("  Tried:");
    for (int i = start, count = converterFactories.size(); i < count; i++) {
      builder.append("\n   * ").append(converterFactories.get(i).getClass().getName());
    }
    throw new IllegalArgumentException(builder.toString());
  }
```

HttpServiceMethod.invoke()

```
  final @Nullable ReturnT invoke(Object[] args) {
    Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
    return adapt(call, args);
  }
```

CallAdapted.class
```
    protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
      return callAdapter.adapt(call);
    }
```