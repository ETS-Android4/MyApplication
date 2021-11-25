# Glide

<!-- ## Glide内存缓存的特点

内存缓存使用弱引用和LruCache结合完成的,弱引用来缓存的是正在使用中的图片。图片封装类Resources内部有个计数器判断是该图片否正在使用。

## Glide内存缓存的流程

读：是先从lruCache取，取不到再从弱引用中取；
存：从网络拉取回来先放在弱引用里，渲染图片，图片对象Resources使用计数加一； 渲染完图片，图片对象Resources使用计数减一，如果计数为0，图片缓存从弱引用中删除，放入lruCache缓存。

## Glide磁盘缓存流程

读：先找处理后（result）的图片，没有的话再找原图,即读取网络图片。
存：先存原图，再存处理后的图。 -->

## 假如让你自己写个图片加载框架，你会考虑哪些问题？

https://www.cnblogs.com/Android-Alvin/p/12672294.html

1. 异步加载：线程池
2. 切换线程：Handler，没有争议吧
3. 缓存：LruCache、DiskLruCache
4. 防止OOM：软引用、LruCache、图片压缩、Bitmap像素存储位置
5. 内存泄露：注意ImageView的正确引用，生命周期管理
6. 列表滑动加载的问题：加载错乱、队满任务过多问题

## Glide 生命周期

1.Glide如何绑定Activity、Fragment生命周期。
2.Glide如何监听内存变化、网络变化。
3.Glide如何处理请求的生命周期。

1. 通过with()多个重载方法来实现对生命周期的绑定工作（Glide.class）
```
  public static RequestManager with(@NonNull Activity activity) {
    return getRetriever(activity).get(activity);
  }
  public static RequestManager with(@NonNull Fragment fragment) {
    return getRetriever(fragment.getContext()).get(fragment);
  }
  public static RequestManager with(@NonNull View view) {
    return getRetriever(view.getContext()).get(view);
  }
```
<!-- 2. 获取RequestManager（Glide.class）
```
  private static RequestManagerRetriever getRetriever(@Nullable Context context) {
    // Context could be null for other reasons (ie the user passes in null), but in practice it will
    // only occur due to errors with the Fragment lifecycle.
    Preconditions.checkNotNull(
        context,
        "You cannot start a load on a not yet attached View or a Fragment where getActivity() "
            + "returns null (which usually occurs when getActivity() is called before the Fragment "
            + "is attached or after the Fragment is destroyed).");
    return Glide.get(context).getRequestManagerRetriever();
  }
```

3. 单例获取全局唯一Glide对象（Glide.class）
```
  public static Glide get(@NonNull Context context) {
    if (glide == null) {
      GeneratedAppGlideModule annotationGeneratedModule =
          getAnnotationGeneratedGlideModules(context.getApplicationContext());
      synchronized (Glide.class) {
        if (glide == null) {
          checkAndInitializeGlide(context, annotationGeneratedModule);
        }
      }
    }
    return glide;
  }
``` -->
4. RequestManagerRetriever是一个单例类，可以理解为一个工厂类，通过get方法接收不同的参数，来创建RequestManager。

* 如果是不在主线程或者是ApplicationContext，就会使用全局的RequestManager，图片的生命周期就会和application保持同步，内存得不到及时回收。
* 反之，如果我们有一张全局图片（比如WindowBackground），我们就可以利用这一特点提高效率，节约内存
```
  // Context
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
  // Activcity
  public RequestManager get(@NonNull FragmentActivity activity) {
    if (Util.isOnBackgroundThread()) {
      return get(activity.getApplicationContext());
    } else {
      assertNotDestroyed(activity);
      frameWaiter.registerSelf(activity);
      // Activity传的参数的是Activity的FragmentManager
      FragmentManager fm = activity.getSupportFragmentManager();
      return supportFragmentGet(activity, fm, /*parentHint=*/ null, isActivityVisible(activity));
    }
  }
  // Fragment
  public RequestManager get(@NonNull Fragment fragment) {
    Preconditions.checkNotNull(
        fragment.getContext(),
        "You cannot start a load on a fragment before it is attached or after it is destroyed");
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
      // Fragment传的参数的是ChildFragmentManager
      FragmentManager fm = fragment.getChildFragmentManager();
      return supportFragmentGet(fragment.getContext(), fm, fragment, fragment.isVisible());
    }
  }
```

5. 创建RequestManager（RequestManagerRetriever.class）
```
  private RequestManager supportFragmentGet(
      @NonNull Context context,
      @NonNull FragmentManager fm,
      @Nullable Fragment parentHint,
      boolean isParentVisible) {
    // 获取RequestManagerFragment，并获取绑定到这个fragment的RequestManager
    SupportRequestManagerFragment current = getSupportRequestManagerFragment(fm, parentHint);
    RequestManager requestManager = current.getRequestManager();
    if (requestManager == null) {
      // TODO(b/27524013): Factor out this Glide.get() call.
      Glide glide = Glide.get(context);
      // 如果获取RequestManagerFragment还没有绑定过RequestManager，那么就创建RequestManager并绑定到RequestManagerFragment
      requestManager =
          factory.build(
              glide, current.getGlideLifecycle(), current.getRequestManagerTreeNode(), context);
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
```

6. 创建RequestManagerFragment（RequestManagerRetriever.class）

* 首先会判断是否已经存在和此fragment关联的SupportRequestManagerFragment，没有的话则去创建，
* 因为Fragment的add是post执行的，所以为了保证不重复实例化Fragment，先把SupportRequestManagerFragment存入pendingSupportRequestManagerFragments集合中
* 收到ID_REMOVE_SUPPORT_FRAGMENT_MANAGER的handler消息后，会立即移除这个Fragment，因为已经能保证findFragmentByTag获取出来不为null
```
  private SupportRequestManagerFragment getSupportRequestManagerFragment(
      @NonNull final FragmentManager fm, @Nullable Fragment parentHint) {
    // 尝试根据id去找到此前创建的RequestManagerFragment
    SupportRequestManagerFragment current =
        (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
    if (current == null) {
      // 如果没有找到，那么从临时存储中寻找
      current = pendingSupportRequestManagerFragments.get(fm);
      if (current == null) {
        // 如果仍然没有找到，那么新建一个RequestManagerFragment，并添加到临时存储中
        // 然后开启事务绑定fragment并使用handler发送消息来将临时存储的fragment移除。
        current = new SupportRequestManagerFragment();
        current.setParentFragmentHint(parentHint);
        pendingSupportRequestManagerFragments.put(fm, current);
        fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
        handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
      }
    }
    return current;
  }
```
7. setParentFragmentHint（SupportRequestManagerFragment.class）

* 子fragment的声明周期未真正执行前，提前获取父fragment的生命周期回调
```
  void setParentFragmentHint(@Nullable Fragment parentFragmentHint) {
    this.parentFragmentHint = parentFragmentHint;
    if (parentFragmentHint == null || parentFragmentHint.getContext() == null) {
      return;
    }
    FragmentManager rootFragmentManager = getRootFragmentManager(parentFragmentHint);
    if (rootFragmentManager == null) {
      return;
    }
    registerFragmentWithRoot(parentFragmentHint.getContext(), rootFragmentManager);
  }
```
