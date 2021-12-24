# Glide

## Glide生命周期绑定与Request创建

### Glide.with

* 在Activity上创建一个透明的RequestManagerFragment加入到FragmentManager中，通过添加的Fragment感知Activity\Fragment的生命周期。 在RequestManagerFragment中的相应生命周期方法中通过Lifecycle传递给在lifecycle中注册的LifecycleListener

```
  @NonNull
  public static RequestManager with(@NonNull Context context) {
    return getRetriever(context).get(context);
  }

  @NonNull
  public static RequestManager with(@NonNull Activity activity) {
    return getRetriever(activity).get(activity);
  }

  @NonNull
  public static RequestManager with(@NonNull FragmentActivity activity) {
    return getRetriever(activity).get(activity);
  }

  @NonNull
  public static RequestManager with(@NonNull Fragment fragment) {
    return getRetriever(fragment.getContext()).get(fragment);
  }

  @NonNull
  public static RequestManager with(@NonNull View view) {
    return getRetriever(view.getContext()).get(view);
  }
```

### Glide 单例模式实现

```
  @NonNull
  private static RequestManagerRetriever getRetriever(@Nullable Context context) {
    return Glide.get(context).getRequestManagerRetriever();
  }
  
  public static Glide get(@NonNull Context context) {
    if (glide == null) {
      GeneratedAppGlideModule annotationGeneratedModule =getAnnotationGeneratedGlideModules(context.getApplicationContext());
      synchronized (Glide.class) {
        if (glide == null) {
          checkAndInitializeGlide(context, annotationGeneratedModule);
        }
      }
    }
    return glide;
  }
```

### Glide 生命周期绑定：RequestManagerRetriever.get

```
  @NonNull
  public RequestManager get(@NonNull Context context) {
    if (context == null) {
      throw new IllegalArgumentException("You cannot start a load on a null Context");
    } else if (Util.isOnMainThread() && !(context instanceof Application)) {
      if (context instanceof FragmentActivity) {
        return get((FragmentActivity) context);
      } else if (context instanceof Activity) {
        return get((Activity) context);
      } else if (context instanceof ContextWrapper
          // Only unwrap a ContextWrapper if the baseContext has a non-null application context.
          // Context#createPackageContext may return a Context without an Application instance,
          // in which case a ContextWrapper may be used to attach one.
          && ((ContextWrapper) context).getBaseContext().getApplicationContext() != null) {
        return get(((ContextWrapper) context).getBaseContext());
      }
    }

    /*
     * 如果context属于Application或者不属于某个页面，则调用getApplicationManager(context)
     */
    return getApplicationManager(context);
  }
  
  /*
   * 构建RequestManager对象时，传入了ApplicationLifecycle对象
   * 也就是说，当with方法传入的是Application时，Glide加载图片的生命周期和应用的生命周期一致，当退出应用时，图片停止加载。
   */
  @NonNull
  private RequestManager getApplicationManager(@NonNull Context context) {
    // Either an application context or we're on a background thread.
    if (applicationManager == null) {
      synchronized (this) {
        if (applicationManager == null) {
          // Normally pause/resume is taken care of by the fragment we add to the fragment or
          // activity. However, in this case since the manager attached to the application will not
          // receive lifecycle events, we must force the manager to start resumed using
          // ApplicationLifecycle.

          // TODO(b/27524013): Factor out this Glide.get() call.
          Glide glide = Glide.get(context.getApplicationContext());
          applicationManager =
              factory.build(
                  glide,
                  new ApplicationLifecycle(),
                  new EmptyRequestManagerTreeNode(),
                  context.getApplicationContext());
        }
      }
    }

    return applicationManager;
  }
```

* RequestManagerRetriever.get方法最终会add一个SupportRequestManagerFragment到Activity里面；SupportRequestManagerFragment里面有个lifecycle成员，我们可以向它注册监听。

```
  @NonNull
  public RequestManager get(@NonNull FragmentActivity activity) {
    if (Util.isOnBackgroundThread()) {
      return get(activity.getApplicationContext());
    } else {
      ···
      FragmentManager fm = activity.getSupportFragmentManager();
      return supportFragmentGet(activity, fm, /*parentHint=*/ null, isActivityVisible(activity));
    }
  }
  
  @NonNull
  private RequestManager supportFragmentGet(@NonNull Context context, @NonNull FragmentManager fm, @Nullable Fragment parentHint,boolean isParentVisible) {
    SupportRequestManagerFragment current = getSupportRequestManagerFragment(fm, parentHint);
    RequestManager requestManager = current.getRequestManager();
    ···
    return requestManager;
  }
  
  @NonNull
  private SupportRequestManagerFragment getSupportRequestManagerFragment(@NonNull final FragmentManager fm, @Nullable Fragment parentHint) {
    SupportRequestManagerFragment current = (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
    if (current == null) {
      ···
      if (current == null) {
        current = new SupportRequestManagerFragment();
        ···
        fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
        ···
      }
    }
    return current;
  }
```

### RequestManager.load

* 创建了一个RequestBuilder<T>对象

```
  public RequestBuilder<Drawable> load(@Nullable String string) {
    return asDrawable().load(string);
  }
  
  public RequestBuilder<Drawable> asDrawable() {
    return as(Drawable.class);
  }
  
  public <ResourceType> RequestBuilder<ResourceType> as(@NonNull Class<ResourceType> resourceClass) {
    return new RequestBuilder<>(glide, this, resourceClass, context);
  }
```

```
  public RequestBuilder<TranscodeType> load(@Nullable String string) {
    return loadGeneric(string);
  }

  private RequestBuilder<TranscodeType> loadGeneric(@Nullable Object model) {
    ···
    this.model = model;
    ···
  }
```

### RequestManager.into

* build了一个Request出来，然后runRequest执行它

```
  // RequestBuilder
  public ViewTarget<ImageView, TranscodeType> into(@NonNull ImageView view) {
    ···
    return into(
        glideContext.buildImageViewTarget(view, transcodeClass),
        /*targetListener=*/ null,
        requestOptions,
        Executors.mainThreadExecutor());
  }

  private <Y extends Target<TranscodeType>> Y into(@NonNull Y target, @Nullable RequestListener<TranscodeType> targetListener,BaseRequestOptions<?> options, Executor callbackExecutor) {
    ···
    Request request = buildRequest(target, targetListener, options, callbackExecutor);    
    ···
    target.setRequest(request);
    requestManager.track(target, request);
    
    return target;
  }

  // RequestManager
  synchronized void track(@NonNull Target<?> target, @NonNull Request request) {
    targetTracker.track(target);
    requestTracker.runRequest(request);
  }

  // RequestTracker
  public void runRequest(@NonNull Request request) {
    requests.add(request);
    if (!isPaused) {
      request.begin();
    } else {
      request.clear();
      ···
      pendingRequests.add(request);
    }
  }
```

* request的执行：先获取图片的尺寸，然后使用engine.load去启动真正的加载逻辑

```
  public void begin() {
    ···
    if (Util.isValidDimensions(overrideWidth, overrideHeight)) {
      onSizeReady(overrideWidth, overrideHeight);
    } else {
      target.getSize(this);
    }
    ···
  }
  
  public void onSizeReady(int width, int height) {
    ···
    loadStatus =
        engine.load(
            glideContext,
            model,
            requestOptions.getSignature(),
            this.width,
            this.height,
            requestOptions.getResourceClass(),
            transcodeClass,
            priority,
            requestOptions.getDiskCacheStrategy(),
            requestOptions.getTransformations(),
            requestOptions.isTransformationRequired(),
            requestOptions.isScaleOnlyOrNoTransform(),
            requestOptions.getOptions(),
            requestOptions.isMemoryCacheable(),
            requestOptions.getUseUnlimitedSourceGeneratorsPool(),
            requestOptions.getUseAnimationPool(),
            requestOptions.getOnlyRetrieveFromCache(),
            this,
            callbackExecutor);
    ···
  }
```

## 内存缓存

### Engine.load

1. 创建缓存的key，这个key由一系列的参数组成，其中最重要的参数model在我们的例子中就是传进去的url。
2. 使用这个key从内存缓存中查询资源
3. 如果内存缓存中查不到资源就开启线程去加载资源
4. 如果内存缓存中可以查到资源就调用cb.onResourceReady回调

```
  public <R> LoadStatus load(···) {
    long startTime = VERBOSE_IS_LOGGABLE ? LogTime.getLogTime() : 0;

    EngineKey key =keyFactory.buildKey(model, signature, width, height, transformations, resourceClass, transcodeClass, options);

    EngineResource<?> memoryResource;
    synchronized (this) {
      memoryResource = loadFromMemory(key, isMemoryCacheable, startTime);

      if (memoryResource == null) {
        return waitForExistingOrStartNewJob(···);
      }
    }

    cb.onResourceReady(memoryResource, DataSource.MEMORY_CACHE, /* isLoadedFromAlternateCacheKey= */ false);
    return null;
  }
```

### Engine.loadFromMemory

* 如果开启了内存缓存的话会先从ActiveResources中查询，查不到的话再从Cache里面查询

```
  @Nullable
  private EngineResource<?> loadFromMemory(EngineKey key, boolean isMemoryCacheable, long startTime) {
    if (!isMemoryCacheable) {
      return null;
    }

    EngineResource<?> active = loadFromActiveResources(key);
    if (active != null) {
      ···
      return active;
    }

    EngineResource<?> cached = loadFromCache(key);
    if (cached != null) {
      ···
      return cached;
    }

    return null;
  }
```

```
  // Engine
  @Nullable
  private EngineResource<?> loadFromActiveResources(Key key) {
    EngineResource<?> active = activeResources.get(key);
    if (active != null) {
      active.acquire();
    }

    return active;
  }
  
  // ActiveResources
final class ActiveResources {
  ···
  @VisibleForTesting
  final Map<Key, ResourceWeakReference> activeEngineResources = new HashMap<>();
  ···
  @Nullable
  synchronized EngineResource<?> get(Key key) {
    ResourceWeakReference activeRef = activeEngineResources.get(key);
    if (activeRef == null) {
      return null;
    }

    EngineResource<?> active = activeRef.get();
    if (active == null) {
      cleanupActiveReference(activeRef);
    }
    return active;
  }
  ···
  @VisibleForTesting
  static final class ResourceWeakReference extends WeakReference<EngineResource<?>> {
    ···
  }
}
```

### 弱引用缓存的添加

```
  1. 从Cache里面查询到的时候如果能查到，会将查到的资源放入弱引用缓存
  private EngineResource<?> loadFromCache(Key key) {
    EngineResource<?> cached = getEngineResourceFromCache(key);
    if (cached != null) {
      cached.acquire();
      activeResources.activate(key, cached);
    }
    return cached;
  }
  
  2. 子线程加载完资源后会将资源放入弱引用缓存
  public synchronized void onEngineJobComplete(EngineJob<?> engineJob, Key key, EngineResource<?> resource) {
    if (resource != null && resource.isMemoryCacheable()) {
      activeResources.activate(key, resource);
    }
    ···
  }
```

### 弱引用缓存的删除

* EngineResource是通过引用计数来管理的。有引用计数增加那就有引用计数减少。减少的操作在release方法里面

```
  // EngineResource
  synchronized void acquire() {
    ···
    ++acquired;
  }

  void release() {
    boolean release = false;
    synchronized (this) {
      if (acquired <= 0) {
        throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
      }
      if (--acquired == 0) {
        release = true;
      }
    }
    if (release) {
      listener.onResourceReleased(key, this);
    }
  }
```

* 如果引用计数降到了0就会调用listener的onResourceReleased回调回去，在onResourceReleased里面Engine会将资源从弱引用缓存删除然后移到cache里

```
  // Engine
  @Override
  public void onResourceReleased(Key cacheKey, EngineResource<?> resource) {
    activeResources.deactivate(cacheKey);
    if (resource.isMemoryCacheable()) {
      cache.put(cacheKey, resource);
    } else {
      resourceRecycler.recycle(resource, /*forceNextFrame=*/ false);
    }
  }
  
  // ActiveResources
  synchronized void deactivate(Key key) {
    ResourceWeakReference removed = activeEngineResources.remove(key);
    if (removed != null) {
      removed.reset();
    }
  }
```

### LRUCache

* Engine.load会先从ActiveResources中查询，查不到的话再从Cache里面查询，这个Cache其实是一个LruResourceCache

```
public class LruResourceCache extends LruCache<Key, Resource<?>> implements MemoryCache {
  ···
}
```

* 把资源从lru cache里面移出，放到弱引用缓存中

```
  private EngineResource<?> loadFromCache(Key key) {
    EngineResource<?> cached = getEngineResourceFromCache(key);
    if (cached != null) {
      cached.acquire();
      activeResources.activate(key, cached);
    }
    return cached;
  }
  
  private EngineResource<?> getEngineResourceFromCache(Key key) {
    Resource<?> cached = cache.remove(key);

    final EngineResource<?> result;
    if (cached == null) {
      result = null;
    } else if (cached instanceof EngineResource) {
      // Save an object allocation if we've cached an EngineResource (the typical case).
      result = (EngineResource<?>) cached;
    } else {
      result = new EngineResource<>(cached, /*isMemoryCacheable=*/ true, /*isRecyclable=*/ true, key, /*listener=*/ this);
    }
    return result;
  }
```

* 资源的引用计数被清零的时候就会从弱引用缓存中删除，加入lru cache中

```
  // Engine
  @Override
  public void onResourceReleased(Key cacheKey, EngineResource<?> resource) {
    activeResources.deactivate(cacheKey);
    if (resource.isMemoryCacheable()) {
      cache.put(cacheKey, resource);
    } else {
      resourceRecycler.recycle(resource, /*forceNextFrame=*/ false);
    }
  }
  
  // ActiveResources
  synchronized void deactivate(Key key) {
    ResourceWeakReference removed = activeEngineResources.remove(key);
    if (removed != null) {
      removed.reset();
    }
  }
```

## Bitmap复用机制

* Glide的Bitmap复用是通过BitmapPool实现的，它在Glide在初始化的时候创建

```
  // GlideBuilder
  Glide build(@NonNull Context context) {
    ···
    if (bitmapPool == null) {
      int size = memorySizeCalculator.getBitmapPoolSize();
      if (size > 0) {
        bitmapPool = new LruBitmapPool(size);
      } else {
        bitmapPool = new BitmapPoolAdapter();
      }
    }
  ···
  }
```

### BitmapPoolAdapter

* 创建和销毁Bitmap

```
public class BitmapPoolAdapter implements BitmapPool {
  @Override
  public long getMaxSize() {
    return 0;
  }

  @Override
  public void setSizeMultiplier(float sizeMultiplier) {
    // Do nothing.
  }

  @Override
  public void put(Bitmap bitmap) {
    bitmap.recycle();
  }

  @NonNull
  @Override
  public Bitmap get(int width, int height, Bitmap.Config config) {
    return Bitmap.createBitmap(width, height, config);
  }

  @NonNull
  @Override
  public Bitmap getDirty(int width, int height, Bitmap.Config config) {
    return get(width, height, config);
  }

  @Override
  public void clearMemory() {
    // Do nothing.
  }

  @Override
  public void trimMemory(int level) {
    // Do nothing.
  }
}
```

### LruBitmapPool

* 使用BitmapPool.put方法将它丢到复用池

```
public class BitmapResource implements Resource<Bitmap>, Initializable {
  ···
  @Override
  public void recycle() {
    bitmapPool.put(bitmap);
  }
  ···
}
```

* LruBitmapPool将资源放到strategy中

```
  private static LruPoolStrategy getDefaultStrategy() {
    final LruPoolStrategy strategy;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      strategy = new SizeConfigStrategy();
    } else {
      strategy = new AttributeStrategy();
    }
    return strategy;
  }
  
  public synchronized void put(Bitmap bitmap) {
    ···
    strategy.put(bitmap);
    ···
  }
```

## 假如让你自己写个图片加载框架，你会考虑哪些问题？

1. 异步加载：线程池
2. 切换线程：Handler
3. 缓存：LruCache、DiskLruCache
4. 防止OOM：软引用、LruCache、图片压缩、Bitmap像素存储位置
5. 内存泄露：注意ImageView的正确引用，生命周期管理
6. 列表滑动加载的问题：加载错乱、队满任务过多问题