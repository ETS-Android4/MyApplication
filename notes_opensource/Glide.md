# Glide

## 假如让你自己写个图片加载框架，你会考虑哪些问题？

1. 异步加载：线程池
2. 切换线程：Handler，没有争议吧
3. 缓存：LruCache、DiskLruCache
4. 防止OOM：软引用、LruCache、图片压缩、Bitmap像素存储位置
5. 内存泄露：注意ImageView的正确引用，生命周期管理
6. 列表滑动加载的问题：加载错乱、队满任务过多问题

## Glide 生命周期

1. Glide如何绑定Activity、Fragment生命周期。
2. Glide如何监听内存变化、网络变化。
3. Glide如何处理请求的生命周期。

## Glide源码解析

1. Glide.java

```
  public static RequestManager with(@NonNull FragmentActivity activity) {
    RequestManagerRetriever retriever = Glide.get(activity).getRequestManagerRetriever();
    return retriever.get(activity);
  }
  
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
     * 如果Glide.with(context) 传入的context属于Application或者不属于某个页面，那么在其方法里直接调getApplicationManager(context)方法，
     * 在getApplicationManager方法中，就是直接创建了一个RequestManager并返回，在构建RequestManager对象时，传入了ApplicationLifecycle对象，
     * 也就是说，当with方法传入的是Application时，Glide加载图片的生命周期和应用的生命周期一致，当退出应用时，图片停止加载。
     */
    return getApplicationManager(context);
  }
  
  public RequestManagerRetriever getRequestManagerRetriever() {
    return requestManagerRetriever;
  }
```

2. RequestManagerRetriever.java

```
public class RequestManagerRetriever implements Handler.Callback {

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

    return getApplicationManager(context);
  }
  
  /**
   * 子线程创建 RequestManager
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
  
  @NonNull
  public RequestManager get(@NonNull FragmentActivity activity) {
    if (Util.isOnBackgroundThread()) {
      return get(activity.getApplicationContext());
    } else {
      assertNotDestroyed(activity);
      frameWaiter.registerSelf(activity);
      FragmentManager fm = activity.getSupportFragmentManager();
      return supportFragmentGet(activity, fm, /*parentHint=*/ null, isActivityVisible(activity));
    }
  }
  
  @NonNull
  public RequestManager get(@NonNull Fragment fragment) {
    Preconditions.checkNotNull(
        fragment.getContext(),
        "You cannot start a load on a fragment before it is attached or after it is destroyed");
    /**
     * 子线程逻辑，与生命周期无关
     */
    if (Util.isOnBackgroundThread()) {
      return get(fragment.getContext().getApplicationContext());
    } else {
      // In some unusual cases, it's possible to have a Fragment not hosted by an activity. There's
      // not all that much we can do here. Most apps will be started with a standard activity. If
      // we manage not to register the first frame waiter for a while, the consequences are not
      // catastrophic, we'll just use some extra memory.
      if (fragment.getActivity() != null) {
        frameWaiter.registerSelf(fragment.getActivity());
      }
      /**
       * 主线程逻辑，监听 activity / fragment 生命周期
       */
      FragmentManager fm = fragment.getChildFragmentManager();
      return supportFragmentGet(fragment.getContext(), fm, fragment, fragment.isVisible());
    }
  }
  
  /**
   * 主线程创建 RequestManager
   */
  @NonNull
  private RequestManager supportFragmentGet(@NonNull Context context, @NonNull FragmentManager fm, @Nullable Fragment parentHint, boolean isParentVisible) {
    SupportRequestManagerFragment current = getSupportRequestManagerFragment(fm, parentHint);
    RequestManager requestManager = current.getRequestManager();
    if (requestManager == null) {
      // TODO(b/27524013): Factor out this Glide.get() call.
      Glide glide = Glide.get(context);
      /**
       * 1. 新建一个RequestManager，将current的lifecycle传入，通过requestManager可以把lifecycleListener添加到lifecycle
       * 2. 在RequestManagerFragment的onStart onStop onDestroy时回调lifeCycle.onStart/onStop...对应的方法
       * 3. 进而回调ActivityFragmentLifecycle中的Set集合中所有监听对应的onStart/onStop...方法
       * 4. requestManager中持有一个单例的glide对象，
       * 5. glide创建时又会从manifest中读取<meta-data android:name="xxx.xx.GlideModule" android:value="GlideModule">自定义的GlideModule这个自定义GlideModule需要防混淆
       * 6. GlideModule 是一个抽象方法，全局改变 Glide 行为的一个方式，通过全局GlideModule 配置Glide，用GlideBuilder设置选项，用Glide注册ModelLoader等                 
       */
      requestManager = factory.build(glide, current.getGlideLifecycle(), current.getRequestManagerTreeNode(), context);
      // This is a bit of hack, we're going to start the RequestManager, but not the
      // corresponding Lifecycle. It's safe to start the RequestManager, but starting the
      // Lifecycle might trigger memory leaks. See b/154405040
      if (isParentVisible) {
        requestManager.onStart();
      }
      current.setRequestManager(requestManager);
    }
    return requestManager;
  }
  
  @NonNull
  private SupportRequestManagerFragment getSupportRequestManagerFragment(@NonNull final FragmentManager fm, @Nullable Fragment parentHint) {
    /*
     * 根据tag找到已经添加的Fragment
     */
    SupportRequestManagerFragment current = (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
    if (current == null) {
      /*
       * 根据tag找到已经添加的Fragment
       */
      current = pendingSupportRequestManagerFragments.get(fm);
      if (current == null) {
        /*
         * RequestManagerFragment内部有个ActivityFragmentLifecycle，ActivityFragmentLifecycle中有个Set集合，里面存放lifeCycleListener
         */
        current = new SupportRequestManagerFragment();
        current.setParentFragmentHint(parentHint);
        pendingSupportRequestManagerFragments.put(fm, current);
        /*
         * 添加Fragment
         */
        fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
        /*
         * 进入队列去删除FragmentManager
         */
        handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
      }
    }
    return current;
  }
  
  @Override
  public boolean handleMessage(Message message) {
    boolean handled = true;
    Object removed = null;
    Object key = null;
    switch (message.what) {
      case ID_REMOVE_FRAGMENT_MANAGER:
        android.app.FragmentManager fm = (android.app.FragmentManager) message.obj;
        key = fm;
        removed = pendingRequestManagerFragments.remove(fm);
        break;
      case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
        FragmentManager supportFm = (FragmentManager) message.obj;
        key = supportFm;
        /**
         * fragment创建好后，将之前的FragmentManager删除掉，同一个Activity或父Fragment中只会创建一个SupportRequestManagerFragment
         */
        removed = pendingSupportRequestManagerFragments.remove(supportFm);
        break;
      default:
        handled = false;
        break;
    }
    if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
      Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
    }
    return handled;
  }

}
```

3. RequestManager，由Glide.with(xxx)返回

```
public class RequestManager implements ComponentCallbacks2, LifecycleListener, ModelTypes<RequestBuilder<Drawable>> {
  
  RequestManager(
      Glide glide,
      Lifecycle lifecycle,
      RequestManagerTreeNode treeNode,
      RequestTracker requestTracker,
      ConnectivityMonitorFactory factory,
      Context context) {
    this.glide = glide;
    this.lifecycle = lifecycle;
    /* treeNode:
	 * 提供了可以访问当前上下文中所有的RequestManager，也就是说基于Context层次结构建立了RequestManager的层次结构，
	 * 而上下文的层次结构是在Activity / Fragment中嵌套的。
	 * 不过，注意，如果当前上下文是Application Context ，只能访问当前上下文的RequestManager. 总而言之就是更方便的管理所有的RequestManager
	 */
    this.treeNode = treeNode;
    /*
     * 请求管理控制类
     */
    this.requestTracker = requestTracker;
    this.context = context;

    /* 网络状态的监听接口，实现了LifecycleListener ，与Activity/Fragment 生命周期绑定在一起。
     * 具体实现DefaultConnectivityMonitor 以及NullConnectivityMonitor
     * 其中前者是一个网络监听的具体实现，后者是一个空实现，类似于Null Object Pattern ，可以避免空指针异常
     */
    connectivityMonitor = factory.build(context.getApplicationContext(), new RequestManagerConnectivityListener(requestTracker));

    // If we're the application level request manager, we may be created on a background thread.
    // In that case we cannot risk synchronously pausing or resuming requests, so we hack around the
    // issue by delaying adding ourselves as a lifecycle listener by posting to the main thread.
    // This should be entirely safe.
    if (Util.isOnBackgroundThread()) {
      Util.postOnUiThread(addSelfToLifecycle);
    } else {
      lifecycle.addListener(this);
    }
    lifecycle.addListener(connectivityMonitor);

    defaultRequestListeners = new CopyOnWriteArrayList<>(glide.getGlideContext().getDefaultRequestListeners());
    setRequestOptions(glide.getGlideContext().getDefaultRequestOptions());

    glide.registerRequestManager(this);
  }

  /**
   * 根据传入的参数类型作为泛型，创建一个图片请求对象DrawableTypeRequest
   * DrawableTypeRequest中存有 glide、requestTracker、lifecycle等的引用
   * load方法传入的数据不一定是url，传入的数据通过ModelLoader处理后转为真正的图片数据源
   */
  @NonNull
  @CheckResult
  @Override
  public RequestBuilder<Drawable> load(@Nullable String string) {
    return new RequestBuilder<>(glide, this, Drawable.class, context).load(string);
  }
}
```

4. RequestBuilder

```
public class RequestBuilder<TranscodeType> extends BaseRequestOptions<RequestBuilder<TranscodeType>>
    implements Cloneable, ModelTypes<RequestBuilder<TranscodeType>> {

  @NonNull
  @CheckResult
  @SuppressWarnings("unchecked")
  @Override
  public RequestBuilder<TranscodeType> load(@Nullable Object model) {
    return loadGeneric(model);
  }

  @NonNull
  private RequestBuilder<TranscodeType> loadGeneric(@Nullable Object model) {
    if (isAutoCloneEnabled()) {
      return clone().loadGeneric(model);
    }
    this.model = model;
    isModelSet = true;
    return selfOrThrowIfLocked();
  }

  @NonNull
  public ViewTarget<ImageView, TranscodeType> into(@NonNull ImageView view) {

    return into(
        glideContext.buildImageViewTarget(view, transcodeClass),
        /*targetListener=*/ null,
        requestOptions,
        Executors.mainThreadExecutor());
  }
  

  private <Y extends Target<TranscodeType>> Y into(@NonNull Y target, @Nullable RequestListener<TranscodeType> targetListener, BaseRequestOptions<?> options, Executor callbackExecutor) {
    Preconditions.checkNotNull(target);
    if (!isModelSet) {
      throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    Request request = buildRequest(target, targetListener, options, callbackExecutor);

    Request previous = target.getRequest();
    if (request.isEquivalentTo(previous)
        && !isSkipMemoryCacheWithCompletePreviousRequest(options, previous)) {
      // If the request is completed, beginning again will ensure the result is re-delivered,
      // triggering RequestListeners and Targets. If the request is failed, beginning again will
      // restart the request, giving it another chance to complete. If the request is already
      // running, we can let it continue running without interruption.
      if (!Preconditions.checkNotNull(previous).isRunning()) {
        // Use the previous request rather than the new one to allow for optimizations like skipping
        // setting placeholders, tracking and un-tracking Targets, and obtaining View dimensions
        // that are done in the individual Request.
        previous.begin();
      }
      return target;
    }

    requestManager.clear(target);
    /*
     * 给target添加一个新请求 
     */
    target.setRequest(request);
    /*
     * 加载请求
     */
    requestManager.track(target, request);

    return target;
  }
}
```

1）Glide.with(context)创建了一个RequestManager，同时实现加载图片与组件生命周期绑定：在Activity上创建一个透明的RequestManagerFragment加入到FragmentManager中，通过添加的Fragment感知Activty\Fragment的生命周期。因为添加到Activity中的Fragment会跟随Activity的生命周期。在RequestManagerFragment中的相应生命周期方法中通过liftcycle传递给在lifecycle中注册的LifecycleListener

2）RequestManager.load(url) 创建了一个RequestBuilder<T>对象 T可以是Drawable对象或是ResourceType等

3) RequestBuilder.into(view)-->into(glideContext.buildImageViewTarget(view, transcodeClass))返回的是一个DrawableImageViewTarget, Target用来最终展示图片的，buildImageViewTarget-->ImageViewTargetFactory.buildTarget()根据传入class参数不同构建不同的Target对象，这个Class是根据构建Glide时是否调用了asBitmap()方法，如果调用了会构建出BitmapImageViewTarget，否则构建的是GlideDrawableImageViewTarget对象。

-->GenericRequestBuilder.into(Target),该方法进行了构建Request，并用RequestTracker.runRequest()

Request request = buildRequest(target);//构建Request对象，Request是用来发出加载图片的，它调用了buildRequestRecursive()方法以，内部调用了GenericRequest.obtain()方法 target.setRequest(request); lifecycle.addListener(target); requestTracker.runRequest(request);//判断Glide当前是不是处于暂停状态，若不是则调用Request.begin()方法来执行Request，否则将Request添加到待执行队列里，等暂停态解除了后再执行 -->GenericRequest.begin()

1）onSizeReady()--> Engine.load(signature, width, height, dataFetcher, loadProvider, transformation, transcoder,priority, isMemoryCacheable, diskCacheStrategy, this) --> a)先构建EngineKey; b) loadFromCache从缓存中获取EngineResource，如果缓存中获取到cache就调用cb.onResourceReady(cached)； c)如果缓存中不存在调用loadFromActiveResources从active中获取，如果获取到就调用cb.onResourceReady(cached)；d)如果active中也不存在，调用EngineJob.start(EngineRunnable), 从而调用decodeFromSource()/decodeFromCache()-->如果是调用decodeFromSource()-->ImageVideoFetcher.loadData()-->HttpUrlFetcher()调用HttpUrlConnection进行网络请求资源-->得于InputStream()后,调用decodeFromSourceData()-->loadProvider.getSourceDecoder().decode()方法解码-->GifBitmapWrapperResourceDecoder.decode()-->decodeStream()先从流中读取2个字节判断是GIF还是普通图，若是GIF调用decodeGifWrapper()来解码，若是普通静图则调用decodeBitmapWrapper()来解码-->bitmapDecoder.decode()

6、Glide使用什么缓存？

1) 内存缓存：LruResourceCache(memory)+弱引用activeResources

Map<Key, WeakReference<EngineResource<?>>> activeResources正在使用的资源，当acquired变量大于0，说明图片正在使用，放到activeResources弱引用缓存中，经过release()后，acquired=0,说明图片不再使用，会把它放进LruResourceCache中

2）磁盘缓存：DiskLruCache,这里分为Source(原始图片)和Result（转换后的图片）

第一次获取图片，肯定网络取，然后存active\disk中，再把图片显示出来，第二次读取相同的图片，并加载到相同大小的imageview中，会先从memory中取，没有再去active中获取。如果activity执行到onStop时，图片被回收，active中的资源会被保存到memory中，active中的资源被回收。当再次加载图片时，会从memory中取，再放入active中，并将memory中对应的资源回收。

之所以需要activeResources，它是一个随时可能被回收的资源，memory的强引用频繁读写可能造成内存激增频繁GC，而造成内存抖动。资源在使用过程中保存在activeResources中，而activeResources是弱引用，随时被系统回收，不会造成内存过多使用和泄漏。