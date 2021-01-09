package com.example.william.my.open.activity;

import android.util.Log;

import com.example.william.my.module.activity.ResponseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Notification;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.BiPredicate;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.functions.Supplier;

/**
 * https://github.com/ReactiveX/RxJava
 * https://github.com/ReactiveX/Rxandroid
 * <p>
 * https://github.com/trello/RxLifecycle
 */
public class RxJavaActivity extends ResponseActivity {

    /*
     * io.reactivex.rxjava3.core.Flowable: 0..N flows, supporting Reactive-Streams and backpressure, 支持反应式流 背压
     * io.reactivex.rxjava3.core.Observable: 0..N flows, no backpressure, 无背压，有多个 onNext 以及一个 通知
     * io.reactivex.rxjava3.core.Single: a flow of exactly 1 item or an error, 只有 一个 onNext 或者 通知
     * io.reactivex.rxjava3.core.Completable: a flow without items but only a completion or error signal, 只有 一个 通知
     * io.reactivex.rxjava3.core.Maybe: a flow with no items, exactly one item or an error. 只有 一个 onNext 以及 一个 通知
     */

    /**
     * 创建观察者
     */
    private Observer<Long> createObserver() {
        return new Observer<Long>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(@NonNull Long aLong) {
                Log.d(TAG, "接收到了事件 ：" + aLong);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "对Error事件作出响应 ：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };
    }

    private SingleObserver<Long> createSingle() {
        return new SingleObserver<Long>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onSuccess(@NonNull Long aLong) {
                Log.d(TAG, "对Success事件作出响应 ：" + aLong);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "对Error事件作出响应 ：" + e.getMessage());
            }
        };
    }

    private SingleObserver<Boolean> createSingleBoolean() {
        return new SingleObserver<Boolean>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onSuccess(@NonNull Boolean aBoolean) {
                Log.d(TAG, "对Success事件作出响应 ：" + aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "对Error事件作出响应 ：" + e.getMessage());
            }
        };
    }

    private MaybeObserver<Long> createMaybe() {
        return new MaybeObserver<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onSuccess(@NonNull Long aLong) {
                Log.d(TAG, "对Success事件作出响应 ：" + aLong);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "对Error事件作出响应 ：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };
    }

    /**
     * 1.0 创建操作符
     * 1. 创建
     * 2. 发送事件
     * 3. 延迟创建
     * https://www.jianshu.com/p/e19f8ed863b1
     */
    private void create() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("next");
                emitter.onComplete();
            }
        });
    }

    /**
     * 1.2. 快速创建 & 发送事件
     */
    private void just() {
        Observable<String> just = Observable.just("just");//快速创建 & 发送10个以下事件
        Observable<String> empty = Observable.empty();//快速创建 & 仅发送onComplete事件
        Observable<String> error = Observable.error(new NullPointerException());//快速创建 & 仅发送onError事件

        Integer[] items = {0, 1, 2, 3, 4};
        Observable<Integer> fromArray = Observable.fromArray(items);//快速创建 & 发送10个以上事件（数组形式）& 传入数组数据
        List<Integer> list = Arrays.asList(items);
        Observable<Integer> fromIterable = Observable.fromIterable(list);//快速创建 & 传入List数据
    }

    /**
     * 1.3. 延迟创建
     */
    private void defer() {
        /*
         * 直到有观察者（Observer ）订阅时，才动态创建被观察者对象
         */
        Observable<String> defer = Observable.defer(new Supplier<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> get() throws Throwable {
                return Observable.just("defer");
            }
        });
        /*
         * 延迟指定时间后，发送1个数值0（Long类型）
         */
        Observable<Long> timer = Observable.timer(2, TimeUnit.SECONDS);
        /*
         * 每隔指定时间 就发送 事件
         * 参数1：起始延迟时间
         * 参数2：间隔时间
         */
        Observable<Long> interval = Observable.interval(3, 1, TimeUnit.SECONDS);//每隔指定时间就发送事件
        /*
         * 每隔指定时间 就发送 事件，可指定发送的数据的数量
         * 参数1：起始点
         * 参数2：发送数据数量
         * 参数3：起始延迟时间
         * 参数4：间隔时间
         */
        Observable<Long> intervalRange = Observable.intervalRange(0, 10, 3, 1, TimeUnit.SECONDS);
        /*
         * 连续发送 1个事件序列，可指定范围
         * 参数1：起始点
         * 参数2：发送数据数量
         */
        Observable<Integer> range = Observable.range(0, 10);
        /*
         * 连续发送1个事件序列，可指定发送的数据范围
         * 参数1：起始点
         * 参数2：发送数据数量
         */
        Observable<Long> observable6 = Observable.rangeLong(0, 10);
    }

    /**
     * 2.0 变换操作符
     * https://www.jianshu.com/p/904c14d253ba
     */
    private void map() {
        /*
         * map()
         * 将被观察者发送的事件转换为任意的类型事件。
         */
        Observable.just(1, 2, 3)
                .map(new Function<Integer, Long>() {
                    @Override
                    public Long apply(Integer integer) throws Throwable {
                        return Long.valueOf(integer);
                    }
                })
                .subscribe(createObserver());
        /*
         * flatMap()
         * 将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送
         */
        Observable.just(1, 2, 3)
                .flatMap(new Function<Integer, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Integer integer) throws Throwable {
                        return Observable.just(Long.valueOf(integer));
                    }
                })
                .subscribe(createObserver());
        /*
         * concatMap()
         * 将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送 (被观察者旧序列生产的顺序)
         */
        Observable.just(1, 2, 3)
                .concatMap(new Function<Integer, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Integer integer) throws Throwable {
                        return Observable.just(Long.valueOf(integer));
                    }
                })
                .subscribe(createObserver());
        /*
         * buffer()
         * 定期从 被观察者需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
         * 参数1：缓存区大小 = 每次从被观察者中获取的事件数量
         * 参数2：步长 = 每次获取新事件的数量
         */
        Observable.just(1L, 2L, 3L, 4L, 5L)
                .buffer(3, 1)
                .subscribe(new Observer<List<Long>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Long> longs) {
                        for (Long l : longs) {
                            Log.d(TAG, "事件 = " + l);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 3.0 合并操作符
     * 1. 组合多个被观察者
     * 2. 合并多个事件
     * 3. 发送事件前追加发送事件
     * 4. 统计发送事件数量
     * https://www.jianshu.com/p/c2a7c03da16d
     */
    private void concat() {
        /*
         * concat() / concatArray()
         * 组合多个被观察者一起发送数据，合并后 按《发送顺序》串行执行。
         * concat()组合被观察者数量≤4个，而concatArray()则可＞4个
         */
        Observable.concat(
                Observable.just(1L, 2L, 3L),
                Observable.just(4L, 5L))
                .subscribe(createObserver());
        /*
         * merge() / mergeArray()
         * 组合多个被观察者一起发送数据，合并后 按《时间线》并行执行
         * merge()组合被观察者数量≤4个，而mergeArray()则可＞4个
         */
        Observable.merge(
                Observable.intervalRange(1, 3, 0, 1, TimeUnit.SECONDS),
                Observable.intervalRange(4, 5, 1, 1, TimeUnit.SECONDS))
                .subscribe(createObserver());
        /*
         * concatDelayError() / mergeDelayError()
         * 将 onError() 事件推迟到其他被观察者发送事件后才触发
         */
        Observable.concatArrayDelayError(
                Observable.just(1L, 2L, 3L),
                Observable.error(new NullPointerException()),
                Observable.just(4L, 5L)
        ).subscribe(createObserver());
    }

    /**
     * 3.2. 合并多个事件
     */
    private void zip() {
        /*
         * zip() / combineLatest()
         * 合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列），并最终发送
         * Zip（） = 按个数合并，即1对1合并，CombineLatest（） = 按时间合并，即在同一个时间点上合并
         */
        Observable.zip(
                Observable.just(1, 2, 3),
                Observable.just(4L, 5L),
                new BiFunction<Integer, Long, Long>() {
                    @Override
                    public Long apply(Integer integer, Long aLong) throws Throwable {
                        Log.d(TAG, "合并的数据是： " + integer + " " + aLong);
                        return integer + aLong;
                    }
                })
                .subscribe(createObserver());

        Observable.combineLatest(
                Observable.just(1, 2, 3),
                Observable.intervalRange(4, 5, 1, 1, TimeUnit.SECONDS),
                new BiFunction<Integer, Long, Long>() {
                    @Override
                    public Long apply(Integer integer, Long aLong) throws Throwable {
                        Log.d(TAG, "合并的数据是： " + integer + " " + aLong);
                        return integer + aLong;
                    }
                })
                .subscribe(createObserver());
        /*
         * combineLatestDelayError，似于concatDelayError（） / mergeDelayError（）
         */
        /*
         * reduce()
         * 把被观察者需要发送的事件聚合成1个事件 & 发送
         */
        Observable.just(1L, 2L, 3L)
                .reduce(new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long aLong, Long aLong2) throws Throwable {
                        return aLong + aLong2;
                    }
                })
                .subscribe(createMaybe());
        /*
         * collect()
         * 将被观察者Observable发送的数据事件收集到一个数据结构里
         */
        Observable.just(1L, 2L, 3L)
                .collect(new Supplier<List<Long>>() {
                             @Override
                             public List<Long> get() throws Throwable {
                                 return new ArrayList<>();
                             }
                         },
                        new BiConsumer<List<Long>, Long>() {
                            @Override
                            public void accept(List<Long> longs, Long aLong) throws Throwable {
                                longs.add(aLong);
                            }
                        })
                .subscribe(new SingleObserver<List<Long>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Long> longs) {
                        for (Long l : longs) {
                            Log.d(TAG, "事件 = " + l);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    /**
     * 3.3. 发送事件前追加发送事件
     */
    private void startWith() {
        /*
         * startWith() / startWithArray()
         * 在一个被观察者发送事件《前》，追加发送一些数据 / 一个新的被观察者
         */
        Observable.just(4L, 5L)
                .startWithArray(1L, 2L, 3L)
                .startWith(Observable.just(0L))
                .subscribe(createObserver());
    }

    /**
     * 3.4. 统计发送事件数量
     */
    private void count() {
        /*
         * count()
         * 统计被观察者发送事件的数量
         */
        Observable.just("1", "2", "3")
                .count()
                .subscribe(createSingle());
    }

    /**
     * 4.0 功能操作符
     * 1. 延迟操作
     * 2. 在事件的生命周期中操作
     * 3. 错误处理
     * https://www.jianshu.com/p/b0c3669affdb
     */
    private void delay() {
        /*
         * delay()
         * 使得被观察者延迟一段时间再发送事件
         * 1. 指定延迟时间
         * 2. 指定延迟时间 & 调度器
         * 3. 指定延迟时间  & 错误延迟
         * 4. 指定延迟时间 & 调度器 & 错误延迟
         */
        Observable.just(1L, 2L, 3L)
                .delay(3, TimeUnit.SECONDS)
                .subscribe(createObserver());
    }

    /**
     * 4.2. 在事件的生命周期中操作
     */
    private void doSomething() {
        Observable.just(1L, 2L, 3L)
                // 1. 当Observable每发送1次数据事件就会调用1次
                .doOnEach(new Consumer<Notification<Long>>() {
                    @Override
                    public void accept(Notification<Long> stringNotification) throws Throwable {
                        Log.d(TAG, "doOnEach: " + stringNotification.getValue());
                    }
                })
                // 2. 执行Next事件前调用
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        Log.d(TAG, "doOnNext: " + aLong);
                    }
                })
                // 3. 执行Next事件后调用
                .doAfterNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        Log.d(TAG, "doAfterNext: " + aLong);
                    }
                })
                // 4. Observable正常发送事件完毕后调用
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d(TAG, "doOnComplete: ");
                    }
                })
                // 5. Observable发送错误事件时调用
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "doOnError: " + throwable.getMessage());
                    }
                })
                // 6. 观察者订阅时调用
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        Log.d(TAG, "doOnSubscribe: ");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d(TAG, "doAfterTerminate: ");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d(TAG, "doFinally: ");
                    }
                })
                .subscribe(createObserver());
    }

    /**
     * 4.3. 错误处理 = > 发送数据
     */
    private void error() {
        /*
         * onErrorReturn()
         * 遇到错误时，发送1个特殊事件 & 正常终止
         * <p>
         * 接收到了事件 ：1
         * 接收到了事件 ：2
         * 在onErrorReturn处理了错误 ：java.lang.Throwable:发生错误了
         * 接收到了事件 ：666
         * 对Complete事件作出响应
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Throwable {
                emitter.onNext(1L);
                emitter.onNext(2L);
                emitter.onError(new Throwable("发生错误了"));
            }
        })
                .onErrorReturn(new Function<Throwable, Long>() {
                    @Override
                    public Long apply(Throwable throwable) throws Throwable {
                        Log.d(TAG, "在onErrorReturn处理了错误 ：" + throwable.toString());
                        return 666L;
                    }
                })
                .subscribe(createObserver());
        /*
         * onErrorResumeNext()
         * 遇到错误时，发送1个新的Observable
         * <p>
         * 接收到了事件 ：1
         * 接收到了事件 ：2
         * 在onErrorReturn处理了错误 ：java.lang.Throwable:发生错误了
         * 接收到了事件 ：666
         * 对Complete事件作出响应
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Throwable {
                emitter.onNext(1L);
                emitter.onNext(2L);
                emitter.onError(new Throwable("发生错误了"));
            }
        })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Long>>() {
                    @Override
                    public ObservableSource<? extends Long> apply(Throwable throwable) throws Throwable {
                        // 1. 捕捉错误异常
                        Log.d(TAG, "在onErrorReturn处理了错误: " + throwable.toString());

                        // 2. 发生错误事件后，发送一个新的被观察者 & 发送事件序列
                        return Observable.just(666L);
                    }
                })
                .subscribe(createObserver());
    }

    /**
     * 4.4. 错误处理 = > 重试
     */
    private void errorRetry() {
        /*
         * 重试，当出现错误时，让被观察者（Observable）重新发射数据
         * 1. 接收到 onError（）时，重新订阅 & 发送事件
         * 2. Throwable 和 Exception都可拦截
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Throwable {
                emitter.onNext(1L);
                emitter.onNext(2L);
                emitter.onError(new NullPointerException());
                emitter.onNext(3L);
            }
        })
                /*
                 * 1. 出现错误时，让被观察者重新发送数据
                 */
                .retry()
                /*
                 * 2. 出现错误时，让被观察者重新发送数据（具备重试次数限制
                 */
                .retry(3)
                /*
                 * 3. 出现错误后，判断是否需要重新发送数据（若需要重新发送& 持续遇到错误，则持续重试）
                 */
                .retry(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Throwable {
                        // 捕获异常
                        Log.d(TAG, "retry错误: " + throwable.toString());

                        //返回false = 不重新重新发送数据 & 调用观察者的onError结束
                        //返回true = 重新发送请求（若持续遇到错误，就持续重新发送）
                        return true;
                    }
                })
                /*
                 * 4. 出现错误后，判断是否需要重新发送数据（具备重试次数限制
                 */
                .retry(new BiPredicate<Integer, Throwable>() {
                    @Override
                    public boolean test(@NonNull Integer integer, @NonNull Throwable throwable) throws Throwable {
                        // 捕获异常
                        Log.d(TAG, "retry错误: " + throwable.toString());

                        // 重试次数
                        Log.e(TAG, "当前重试次数 =  " + integer);

                        //返回false = 不重新重新发送数据 & 调用观察者的onError结束
                        //返回true = 重新发送请求（若持续遇到错误，就持续重新发送）
                        return true;
                    }
                })
                /*
                 * 5. 出现错误后，判断是否需要重新发送数据（若需要重新发送 & 持续遇到错误，则持续重试
                 */
                .retry(3, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Throwable {
                        // 捕获异常
                        Log.d(TAG, "retry错误: " + throwable.toString());

                        //返回false = 不重新重新发送数据 & 调用观察者的onError结束
                        //返回true = 重新发送请求（若持续遇到错误，就持续重新发送）
                        return true;
                    }
                })
                /*
                 * retryWhen()
                 * 遇到错误时，将发生的错误传递给一个新的被观察者（Observable），并决定是否需要重新订阅原始被观察者（Observable）& 发送事件
                 */
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Throwable {
                        // 参数Observable<Throwable>中的泛型 = 上游操作符抛出的异常，可通过该条件来判断异常的类型
                        // 返回Observable<?> = 新的被观察者 Observable（任意类型）
                        // 此处有两种情况：
                        // 1. 若 新的被观察者 Observable发送的事件 = Error事件，那么 原始Observable则不重新发送事件：
                        // 2. 若 新的被观察者 Observable发送的事件 = Next事件 ，那么原始的Observable则重新发送事件：
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Throwable {
                                // 1. 若返回的Observable发送的事件 = Error事件，则原始的Observable不重新发送事件
                                // 该异常错误信息可在观察者中的onError（）中获得
                                return Observable.error(new Throwable("retryWhen终止啦"));

                                // 2. 若返回的Observable发送的事件 = Next事件，则原始的Observable重新发送事件（若持续遇到错误，则持续重试）
                                //return Observable.just(1);
                            }
                        });
                    }
                })
                .subscribe(createObserver());
    }

    /**
     * 5.0 过滤操作符
     * 1. 指定条件
     * 2. 指定事件数量
     * 3. 指定时间
     * 4. 指定事件位置
     * https://www.jianshu.com/p/c3a930a03855
     */
    private void filter() {
        /*
         * filter()
         * 过滤 《特定条件》 的事件
         */
        Observable.just(1L, 2L, 3L)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Throwable {
                        return aLong > 1L;
                    }
                })
                .subscribe(createObserver());
        /*
         * ofType()
         * 过滤 《指定数据类型》的数据
         */
        Observable.just(1, 2L, 3L)
                .ofType(Long.class)
                .subscribe(createObserver());
        /*
         * skip() / skipLast()
         * 跳过某个事件
         */
        Observable.just(1L, 2L, 3L, 4L, 5L)
                .skip(1) // 跳过正序的前1项
                .skipLast(2) // 跳过正序的后2项
                .subscribe(createObserver());
        Observable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                .skip(1, TimeUnit.SECONDS) // 跳过第1s发送的数据
                .skipLast(1, TimeUnit.SECONDS) // 跳过最后1s发送的数据
                .subscribe(createObserver());
        /*
         * distinct() / distinctUntilChanged()
         * 过滤事件序列中重复的事件 / 连续重复的事件
         */
        Observable.just(1L, 2L, 3L, 1L, 2L)
                .distinct()
                .subscribe(createObserver());
        Observable.just(1L, 2L, 3L, 3L, 3L)
                .distinctUntilChanged()
                .subscribe(createObserver());
    }

    /**
     * 5.2. 指定事件数量
     */
    private void task() {
        /*
         * take()
         * 指定最多能接收到的事件数量
         */
        Observable.just(1L, 2L, 3L, 4L, 5L)
                .take(3)
                .subscribe(createObserver());
        /*
         * takeLast()
         * 指定只接收到被观察者发送的最后几个事件
         */
        Observable.just(1L, 2L, 3L, 4L, 5L)
                .takeLast(3)
                .subscribe(createObserver());
    }

    /**
     * 5.3. 指定事件时间
     */
    private void throttle() {
        /*
         * throttleFirst() / throttleLast()
         * 在某段时间内，只发送该段时间内第1次事件 / 最后1次事件
         * throttleWithTimeout()
         * 发送数据事件时，若2次发送事件的间隔＜指定时间，就会丢弃前一次的数据，直到指定时间内都没有新数据发射时才会发送后一次的数据
         */
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Throwable {
                // 隔段事件发送时间
                emitter.onNext(1L);
                Thread.sleep(500);
                emitter.onNext(2L);
                Thread.sleep(500);
                emitter.onNext(3L);
                Thread.sleep(500);
                emitter.onNext(4L);
                Thread.sleep(500);
                emitter.onNext(5L);
                Thread.sleep(500);
            }
        })
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(createObserver());
    }

    /**
     * 5.4. 指定事件位置
     */
    private void element() {
        /*
         * firstElement() / lastElement()
         * 仅选取第1个元素 / 最后一个元素
         */
        Observable.just(1L, 2L, 3L)
                .firstElement()
                .subscribe(createMaybe());
        /*
         * elementAt()
         * 指定接收某个元素（通过 索引值 确定）
         * 1. 获取位置索引
         * 2. 获取位置索引，当获取的位置索引 ＞ 发送事件序列长度时，设置默认参数
         * 3. 获取位置索引，当获取的位置索引 ＞ 发送事件序列长度时，抛出异常
         */
        Observable.just(1L, 2L, 3L)
                .elementAt(1)
                .subscribe(createMaybe());

        Observable.just(1L, 2L, 3L)
                .elementAt(5, 666L)
                .subscribe(createSingle());

        Observable.just(1L, 2L, 3L)
                .elementAtOrError(5)
                .subscribe(createSingle());
    }

    /**
     * 6.0 条件操作符
     * https://www.jianshu.com/p/954426f90325
     */
    private void take() {
        /*
         * all()
         * 判断发送的数据是否都满足
         */
        Observable.just(1, 2, 3)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Throwable {
                        return integer <= 5;
                    }
                })
                .subscribe(createSingleBoolean());
        /*
         * takeWhile() / skipUntil()
         * 判断发送的数据是否满足，若发送的数据满足该条件，则发送 / 跳过 该项数据；否则不发送 / 跳过
         */
        Observable.just(1L, 2L, 3L, 4L, 5L)
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Throwable {
                        return aLong < 3L;
                    }
                })
                .subscribe(createObserver());
        /*
         * takeUntil() / skipUntil()
         * 执行到某个条件时，停止发送事件
         */
        Observable.just(1L, 2L, 3L, 4L, 5L)
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Throwable {
                        return aLong > 3L;
                    }
                })
                .subscribe(createObserver());
        /*
         * 判断条件也可以是Observable
         * 当第3秒后，第2个Observable开始发送数据，第一个Observable停止发送
         */
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(Observable.timer(3, TimeUnit.SECONDS))
                .subscribe(createObserver());
        /*
         * sequenceEqual()
         * 判定两个Observables需要发送的数据是否相同
         */
        Observable.sequenceEqual(
                Observable.just(1, 2, 3),
                Observable.just(1, 2, 3)
        )
                .subscribe(createSingleBoolean());
        /*
         * contains()
         * 判断发送的数据中是否包含指定数据
         */
        Observable.just(1, 2, 3)
                .contains(3)
                .subscribe(createSingleBoolean());
        /*
         * isEmpty()
         * 判断发送的数据是否为空
         */
        Observable.just(1, 2, 3)
                .isEmpty()
                .subscribe(createSingleBoolean());
        /*
         * amb()
         * 当需要发送多个 Observable时，只发送 先发送数据的Observable的数据，而其余 Observable则被丢弃。
         */
        List<ObservableSource<Long>> list = new ArrayList<>();
        list.add(Observable.just(1L, 2L, 3L));// 第1个Observable正常发送数据
        list.add(Observable.just(4L, 5L, 6L).delay(1, TimeUnit.SECONDS));// 第2个Observable延迟1秒发射数据
        Observable
                .amb(list)
                .subscribe(createObserver());
        /*
         * defaultIfEmpty()
         * 在不发送任何有效事件（ Next事件）、仅发送了 Complete 事件的前提下，发送一个默认值
         */
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 不发送任何有效事件，仅发送Complete事件
                e.onComplete();
            }
        })
                .defaultIfEmpty(10)
                .subscribe();
    }
}