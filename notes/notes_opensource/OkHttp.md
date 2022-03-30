# OkHttp

> https://github.com/square/okhttp

• OkHttp是网络执行层，做底层网络请求

## OkHttp 简单使用

```
// （1）创建 OkHttpClient 对象
OkHttpClient client = new OkHttpClient();
// （2）创建 Request 对象
Request request = new Request.Builder()
        .url(url)
        .build();
// （3）创建 Call 对象。
Call call = client.newCall(request);
// （4）发送请求并获取服务器返回的数据
call.enqueue(new okhttp3.Callback() {
    @Override
    public void onFailure( Call call,  IOException e) {
    }
    @Override
    public void onResponse( Call call,  okhttp3.Response response) throws IOException {
    }
});
```

## 源码解析 3.14.9

### 1. 创建 OkHttpClient 对象

在 new OkHttpClient() 内部使用 Builder 模式初始化了一些配置信息：支持协议、任务调度器（其内部包含一个线程池，执行异步请求）、连接池(其内部包含一个线程池，维护connection)、连接/读/写超时时长等信息

```
    public Builder() {
      dispatcher = new Dispatcher(); // 任务调度器，默认最大64个请求
      protocols = DEFAULT_PROTOCOLS; // 支持的协议 http2, http1.1
      connectionSpecs = DEFAULT_CONNECTION_SPECS; // 网络连通性协议tsl
      eventListenerFactory = EventListener.factory(EventListener.NONE); // 网络连接状态监听
      proxySelector = ProxySelector.getDefault();
      if (proxySelector == null) {
        proxySelector = new NullProxySelector();
      }
      cookieJar = CookieJar.NO_COOKIES; // cookie
      socketFactory = SocketFactory.getDefault(); // socket
      hostnameVerifier = OkHostnameVerifier.INSTANCE; //域名校验
      certificatePinner = CertificatePinner.DEFAULT; // 证书校验
      proxyAuthenticator = Authenticator.NONE; // 证书
      authenticator = Authenticator.NONE;
      connectionPool = new ConnectionPool(); // 连接池
      dns = Dns.SYSTEM; // DNS解析
      followSslRedirects = true;
      followRedirects = true;
      retryOnConnectionFailure = true;
      callTimeout = 0;
      connectTimeout = 10_000; // 连接超时时间10秒
      readTimeout = 10_000; // 接收超时时间10秒
      writeTimeout = 10_000; // 发送超时时间10秒
      pingInterval = 0;
    }
```

### 任务调度器 Dispatcher.class

Dispatcher任务调度器，它定义了三个双向任务队列，两个异步队列：准备执行的请求队列 readyAsyncCalls、正在运行的请求队列 runningAsyncCalls；一个正在运行的同步请求队列 runningSyncCalls

```
public final class Dispatcher {
  private int maxRequests = 64; // 最大请求数量
  private int maxRequestsPerHost = 5; // 每台主机最大的请求数量
  private @Nullable Runnable idleCallback;

  /** Executes calls. Created lazily. */
  private @Nullable ExecutorService executorService; // 线程池

  /** Ready async calls in the order they'll be run. */
  private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<>();

  /** Running asynchronous calls. Includes canceled calls that haven't finished yet. */
  private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>();

  /** Running synchronous calls. Includes canceled calls that haven't finished yet. */
  private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();

  /** 没有核心线程，线程数量无限制，空闲60秒回收 */
  public synchronized ExecutorService executorService() {
    if (executorService == null) {
      executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
          new SynchronousQueue<>(), Util.threadFactory("OkHttp Dispatcher", false));
    }
    return executorService;
  }
}
```

### 2. 创建 Request 对象

同样使用了 Builder 模式，用来设置一些请求链接（url）、请求方法（method）、请求头（headers）、请求体（body）、标签（tag，可作为取消请求的标记）

```
  public static class Builder {
    @Nullable HttpUrl url;
    String method;
    Headers.Builder headers;
    @Nullable RequestBody body;

    /** A mutable map of tags, or an immutable empty map if we don't have any. */
    Map<Class<?>, Object> tags = Collections.emptyMap();

    public Builder() {
      this.method = "GET";
      this.headers = new Headers.Builder();
    }

    Builder(Request request) {
      this.url = request.url;
      this.method = request.method;
      this.body = request.body;
      this.tags = request.tags.isEmpty()
          ? Collections.emptyMap()
          : new LinkedHashMap<>(request.tags);
      this.headers = request.headers.newBuilder();
    }
  }
```

### 3. 创建 Call 对象

通过 OkHttpClient 和 Request 构造一个 Call 对象。实质上调用的是 RealCall 中的 newRealCall 方法。

```
  // OkHttpClient.class
  @Override public Call newCall(Request request) {
    return RealCall.newRealCall(this, request, false /* for web socket */);
  }
```

通过工厂模式，创建了一个用来执行的请求 RealCall , 初始化了对应的发射器 transmitter

```
  // RealCall.class
  static RealCall newRealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
    // Safely publish the Call instance to the EventListener.
    RealCall call = new RealCall(client, originalRequest, forWebSocket);
    call.transmitter = new Transmitter(client, call);
    return call;
  }
```

RealCall 的构造方法中主要是记录了 client, 原始的 request 对象

```
  // RealCall.class
  private RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
    this.client = client;
    this.originalRequest = originalRequest;
    this.forWebSocket = forWebSocket;
  }
```

### RealCall 的父类 Call.class

Call是一个准备好的可被执行的 Request。内部有获取原始请求，同步异步执行请求，取消请求，克隆请求，和判断请求状态方法。

```
public interface Call extends Cloneable {
  
  // 返回原始的请求对象
  Request request();

  // 同步阻塞的返回响应，这个只代表传输层成功，并不一定是应用层成功（404，500）
  Response execute() throws IOException;

  //  异步的执行请求，将请求放入请求队列等待执行
  void enqueue(Callback responseCallback);

  // 取消一个请求，如果请求已经执行完成是无法被取消的
  void cancel();

  boolean isExecuted();

  boolean isCanceled();

  Timeout timeout();

  /**
   * Create a new, identical call to this one which can be enqueued or executed even if this call
   * has already been.
   */
  Call clone();

  interface Factory {
    Call newCall(Request request);
  }
}
```

### 4. 同步请求

调用 Call.execute() 方法直接返回当前请求的 Response。

1. client.dispatcher() 方法返回了一个 dispatcher 对象
2. 然后接着调用了 dispatcher 对象的 executed 方法，把一个同步任务添加到了调度器内部的数组
3. 在将任务添加到队列之后，就调用了 getResponseWithInterceptorChain() 方法来获取 Response。

```
  @Override public Response execute() throws IOException {
    // 这里使用 同步锁 + executed 标记，所以一个Call只可以执行一次！
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true; // 标记该Call已经执行了
    }
    transmitter.timeoutEnter();
    transmitter.callStart();
    try {
      client.dispatcher().executed(this); // 将该Call加入同步请求队列
      return getResponseWithInterceptorChain(); // 执行真正的请求
    } finally {
      client.dispatcher().finished(this); // 请求完成，从同步请求队列中移除该Call
    }
  }
```

### 5. 异步请求

调用 Call.enqueue() 方法将请求（AsyncCall）添加到请求队列中去，并通过回调（Callback）获取服务器返回的结果。

1. client.dispatcher() 方法返回了一个 dispatcher 对象
2. 然后接着调用了 dispatcher 对象的 enqueue 方法，传入了一个封装了响应回调 callBack 的 AsyncCall 对象

```
  @Override public void enqueue(Callback responseCallback) {
    // 这里使用 同步锁 + executed 标记，所以一个Call只可以执行一次！
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true; // 标记该Call已经执行了
    }
    transmitter.callStart();
    client.dispatcher().enqueue(new AsyncCall(responseCallback));
  }
```

### AsyncCall

AsyncCall 是 Runnable 的一个实现类。

```
  final class AsyncCall extends NamedRunnable {

    @Override protected void execute() {
      boolean signalledCallback = false;
      transmitter.timeoutEnter();
      try {
        // 执行真正的请求
        Response response = getResponseWithInterceptorChain();
        // 避免执行两次
        signalledCallback = true;
        // 成功回调
        responseCallback.onResponse(RealCall.this, response);
      } catch (IOException e) {
        if (signalledCallback) {
          // Do not signal the callback twice!
          Platform.get().log(INFO, "Callback failure for " + toLoggableString(), e);
        } else {
          responseCallback.onFailure(RealCall.this, e);
        }
      } catch (Throwable t) {
        cancel();
        if (!signalledCallback) {
          IOException canceledException = new IOException("canceled due to " + t);
          canceledException.addSuppressed(t);
          // 失败回调
          responseCallback.onFailure(RealCall.this, canceledException);
        }
        throw t;
      } finally {
        // 不管请求成语与否，都进行finished操作
        client.dispatcher().finished(this);
      }
    }
  }
```


### 6 获取 Response getResponseWithInterceptorChain()

真正去同步执行的是通过getResponseWithInterceptorChain()方法，并返回响应结果。

```
  Response getResponseWithInterceptorChain() throws IOException {
    // Build a full stack of interceptors.
    List<Interceptor> interceptors = new ArrayList<>();
    interceptors.addAll(client.interceptors()); // 应用拦截器
    interceptors.add(new RetryAndFollowUpInterceptor(client)); // 请求重试拦截器
    interceptors.add(new BridgeInterceptor(client.cookieJar())); // 桥接拦截器
    interceptors.add(new CacheInterceptor(client.internalCache())); // 缓存拦截器
    interceptors.add(new ConnectInterceptor(client)); // 连接拦截器
    if (!forWebSocket) {
      interceptors.addAll(client.networkInterceptors()); //  网络拦截器
    }
    interceptors.add(new CallServerInterceptor(forWebSocket)); // Server数据交互拦截器

    Interceptor.Chain chain = new RealInterceptorChain(interceptors, transmitter, null, 0,
        originalRequest, this, client.connectTimeoutMillis(),
        client.readTimeoutMillis(), client.writeTimeoutMillis());

    boolean calledNoMoreExchanges = false;
    try {
      Response response = chain.proceed(originalRequest); // 处理请求
      if (transmitter.isCanceled()) {
        closeQuietly(response);
        throw new IOException("Canceled");
      }
      return response;
    } catch (IOException e) {
      calledNoMoreExchanges = true;
      throw transmitter.noMoreExchanges(e);
    } finally {
      if (!calledNoMoreExchanges) {
        transmitter.noMoreExchanges(null);
      }
    }
  }
```