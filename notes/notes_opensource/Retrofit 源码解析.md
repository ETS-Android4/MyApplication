## 源码解析 2.9.0

### 1. 创建 Builder 对象

```
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .build();
```

通过 Builder 模式创建 Retrofit 对象。初始化了一些配置，例如网络请求工厂、网络请求地址、数据转换器、请求适配器等

```
  /*Retrofit*/
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
NetworkService service = retrofit.create(NetworkService.class);
Call<ResponseBody> call = service.getInfo("");
```

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
            // 如果该方法是 Object 的方法，例如 equals()、toString()，那就直接调用
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            args = args != null ? args : emptyArgs;
            return platform.isDefaultMethod(method) // （3）如果该方法是 Java8 默认方法
                    ? platform.invokeDefaultMethod(method, service, proxy, args) // （4）执行该平台的默认方法
                    : loadServiceMethod(method) // （5）调用 loadServiceMethod() 方法拿到 ServiceMethod 对象
                    .invoke(args); //（6）
        }
    });
}
```

1. （1）Proxy 的 newProxyInstance() 方法就已经创建了网络请求接口的实例
2. （2）调用网络请求接口中的方法（例如例子中 service.getInfo()）的时候才会执行 invoke() 方法
3. （3）如果该方法是 Java8 默认方法，如果该方法是 Java8 默认方法，否则调用 loadServiceMethod() 方法拿到 ServiceMethod 对象

### （3）platform.isDefaultMethod(method)

```
  boolean isDefaultMethod(Method method) {
    return hasJava8Types && method.isDefault();
  }
```

### （5）loadServiceMethod() 获取当前方法的 ServiceMethod

首先会从 ServiceMethod 缓存集合中取出当前方法对应的 ServiceMethod，如果不为空，则直接返回。如果为空，则使用单例模式创建一个新的 ServiceMethod，即调用 parseAnnotations() 方法创建，然后再存到缓存集合中。

```
  /*Retrofit*/
  ServiceMethod<?> loadServiceMethod(Method method) {
    ServiceMethod<?> result = serviceMethodCache.get(method);
    if (result != null) return result;

    synchronized (serviceMethodCache) {
      result = serviceMethodCache.get(method);
      if (result == null) {
        result = ServiceMethod.parseAnnotations(this, method); // （7）创建一个新的 ServiceMethod
        serviceMethodCache.put(method, result);
      }
    }
    return result;
  }
```

### （7）ServiceMethod.parseAnnotations(this, method); 创建 ServiceMethod

调用 RequestFactory 的 parseAnnotations() 方法解析网络请求接口中的注解。

```
  /*ServiceMethod*/
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method); // （8）解析网络请求接口中的注解

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

    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory); // （9）解析网络适配器，数据转换器，以及网络请求器工厂
  }
```

### （8）RequestFactory.parseAnnotations()

解析网络请求接口中的注解

```
  /*RequestFactory*/
  static RequestFactory parseAnnotations(Retrofit retrofit, Method method) {
    return new Builder(retrofit, method).build();
  }
```

### （9）HttpServiceMethod.parseAnnotations()

解析相对应的网络请求适配器（CallAdapter）、数据转换器（Converter）与网络请求器工厂（okhttp3.Call.Factory）

```
  /*HttpServiceMethod*/
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

### （6）ServiceMethod.invoke()

调用了 CallAdapter 的 adapt() 方法来创建了一个 OkHttpCall 对象

```
  /ServiceMethod*/
  abstract @Nullable T invoke(Object[] args);
  
  /*HttpServiceMethod*/
  @Override
  final @Nullable ReturnT invoke(Object[] args) {
    Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
    return adapt(call, args);
  }
```

### 3. 同步请求

```
Response<PostmanGetBean> response = call.execute();
```

```
@Override
public Response<T> execute() throws IOException {
  return delegate.execute();
}
```

delegate 实际为 OkHttpCall

```
  @Override
  public Response<T> execute() throws IOException {
    okhttp3.Call call;

    synchronized (this) {
      if (executed) throw new IllegalStateException("Already executed.");
      executed = true;

      call = getRawCall(); // （10）
    }

    if (canceled) {
      call.cancel();
    }

    return parseResponse(call.execute()); // （11）
  }
```

### （10）getRawCall()

```
  private okhttp3.Call getRawCall() throws IOException {
    okhttp3.Call call = rawCall;
    if (call != null) return call;

    // Re-throw previous failures if this isn't the first attempt.
    if (creationFailure != null) {
      if (creationFailure instanceof IOException) {
        throw (IOException) creationFailure;
      } else if (creationFailure instanceof RuntimeException) {
        throw (RuntimeException) creationFailure;
      } else {
        throw (Error) creationFailure;
      }
    }

    // Create and remember either the success or the failure.
    try {
      return rawCall = createRawCall(); // （12）
    } catch (RuntimeException | Error | IOException e) {
      throwIfFatal(e); // Do not assign a fatal error to creationFailure.
      creationFailure = e;
      throw e;
    }
  }
```

### （12）createRawCall() 创建了一个 okhttp3.Call

通过 requestFactory.create(args) 构建一个 Request 对象，然后再通过 OkHttpClient 的 newCall() 方法去创建 call

```
  private okhttp3.Call createRawCall() throws IOException {
    okhttp3.Call call = callFactory.newCall(requestFactory.create(args)); // （13）
    if (call == null) {
      throw new NullPointerException("Call.Factory returned null.");
    }
    return call;
  }
```

### （13）requestFactory.create(args) 构建一个 Request 对象

```
  /*RequestFactory*/
  okhttp3.Request create(Object[] args) throws IOException {
    @SuppressWarnings("unchecked") // It is an error to invoke a method with the wrong arg types.
    ParameterHandler<Object>[] handlers = (ParameterHandler<Object>[]) parameterHandlers;

    int argumentCount = args.length;
    if (argumentCount != handlers.length) {
      throw new IllegalArgumentException(
          "Argument count ("
              + argumentCount
              + ") doesn't match expected count ("
              + handlers.length
              + ")");
    }

    RequestBuilder requestBuilder =
        new RequestBuilder(
            httpMethod,
            baseUrl,
            relativeUrl,
            headers,
            contentType,
            hasBody,
            isFormEncoded,
            isMultipart);

    if (isKotlinSuspendFunction) {
      // The Continuation is the last parameter and the handlers array contains null at that index.
      argumentCount--;
    }

    List<Object> argumentList = new ArrayList<>(argumentCount);
    for (int p = 0; p < argumentCount; p++) {
      argumentList.add(args[p]);
      handlers[p].apply(requestBuilder, args[p]);
    }

    return requestBuilder.get().tag(Invocation.class, new Invocation(method, argumentList)).build();
  }
```

### （11）parseResponse(call.execute());

```
  /*OkHttpCall*/
  Response<T> parseResponse(okhttp3.Response rawResponse) throws IOException {
    ResponseBody rawBody = rawResponse.body();

    // Remove the body's source (the only stateful object) so we can pass the response along.
    rawResponse =
        rawResponse
            .newBuilder()
            .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
            .build();

    int code = rawResponse.code();
    if (code < 200 || code >= 300) {
      try {
        // Buffer the entire body to avoid future I/O.
        ResponseBody bufferedBody = Utils.buffer(rawBody);
        return Response.error(bufferedBody, rawResponse);
      } finally {
        rawBody.close();
      }
    }

    if (code == 204 || code == 205) {
      rawBody.close();
      return Response.success(null, rawResponse);
    }

    ExceptionCatchingResponseBody catchingBody = new ExceptionCatchingResponseBody(rawBody);
    try {
      T body = responseConverter.convert(catchingBody); // 通过设置的 Converter 将响应的数据解析成相应的实体类返回
      return Response.success(body, rawResponse);
    } catch (RuntimeException e) {
      // If the underlying source threw an exception, propagate that rather than indicating it was
      // a runtime exception.
      catchingBody.throwIfCaught();
      throw e;
    }
  }
```

### 4. 异步请求

```
call.enqueue(new Callback<ResponseBody>() {
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull final Throwable t) {
    }
});
```

```
    /*DefaultCallAdapterFactory*/
    @Override
    public void enqueue(final Callback<T> callback) {
      Objects.requireNonNull(callback, "callback == null");

      // （14）
      delegate.enqueue(
          new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, final Response<T> response) {
              callbackExecutor.execute(
                  () -> {
                    if (delegate.isCanceled()) {
                      // Emulate OkHttp's behavior of throwing/delivering an IOException on
                      // cancellation.
                      callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                    } else {
                      callback.onResponse(ExecutorCallbackCall.this, response);
                    }
                  });
            }

            @Override
            public void onFailure(Call<T> call, final Throwable t) {
              callbackExecutor.execute(() -> callback.onFailure(ExecutorCallbackCall.this, t));
            }
          });
    }
```

### （14）delegate.enqueue(）

delegate 实际为 OkHttpCall

（15） enqueue() 是 OkHttp 中方法，所以 Retrofit 底层还是通过 Okhttp 进行网络请求。

```
  /*OkHttpCall*/
  @Override
  public void enqueue(final Callback<T> callback) {
    Objects.requireNonNull(callback, "callback == null");

    okhttp3.Call call;
    Throwable failure;

    synchronized (this) {
      if (executed) throw new IllegalStateException("Already executed.");
      executed = true;

      call = rawCall;
      failure = creationFailure;
      if (call == null && failure == null) {
        try {
          call = rawCall = createRawCall(); // （12）
        } catch (Throwable t) {
          throwIfFatal(t);
          failure = creationFailure = t;
        }
      }
    }

    if (failure != null) {
      callback.onFailure(this, failure);
      return;
    }

    if (canceled) {
      call.cancel();
    }

    // （15）
    call.enqueue(
        new okhttp3.Callback() {
          @Override
          public void onResponse(okhttp3.Call call, okhttp3.Response rawResponse) {
            Response<T> response;
            try {
              response = parseResponse(rawResponse); // （11）
            } catch (Throwable e) {
              throwIfFatal(e);
              callFailure(e);
              return;
            }

            try {
              // 将 onResponse() 回调到 UI 线程
              callback.onResponse(OkHttpCall.this, response);
            } catch (Throwable t) {
              throwIfFatal(t);
              t.printStackTrace(); // TODO this is not great
            }
          }

          @Override
          public void onFailure(okhttp3.Call call, IOException e) {
            callFailure(e);
          }

          private void callFailure(Throwable e) {
            try {
              // 将 onFailure() 回调到 UI 线程
              callback.onFailure(OkHttpCall.this, e);
            } catch (Throwable t) {
              throwIfFatal(t);
              t.printStackTrace(); // TODO this is not great
            }
          }
        });
  }
```

### 总结

* （1）创建网络请求接口：

这一步主要是配置 HTTP 请求需要的各种参数，例如 HTTP 请求方法、请求头、请求参数、请求地址等。

* （2）创建 Retrofit 的实例：

这一步是通过建造者模式来创建 Retrofit 的实例，创建的过程中主要配置了网络请求器工厂（callFactory）、网络请求的 url 地址（baseUrl）、数据转换器工厂的集合（converterFactories）、网络请求适配器工厂的集合（callAdapterFactories）、回调方法执行器（callbackExecutor）。这里使用建造者模式的好处是不需要知道 Retrofit 内部是怎么创建的，只需要传入对应的配置即可创建非常复杂的对象。

* （3）创建网络请求接口的实例，并调用接口中的方法获取 Call 对象：

这一步是通过动态代理模式来创建网络请求接口的实例，然后再调用网络请求接口中的方法来执行动态代理中的 invoke() 方法。这一步的作用主要是解析网络请求接口中方法上的注解、参数、获取返回类型等，也就是将网络请求接口中的方法解析成 HTTP 请求需要的各种参数。这里使用动态代理的好处是可以将网络请求接口的所有方法的调用都会集中转发到 InvocationHandler 接口的 invoke() 方法中，方便集中进行处理。
   
* （4）进行网络请求：

这一步的同步与异步请求其实底层分别调用的是 OkHttp 的同步与异步请求方法，然后通过我们设置的数据转换器（Converter）将响应的数据解析成相应的实体类返回。同步请求会直接返回，异步请求则会切换到主线程再进行返回