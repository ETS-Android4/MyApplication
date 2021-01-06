package com.example.william.my.open.activity;

import android.util.Log;

import com.example.william.my.module.activity.ResponseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Notification;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
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
 */
public class RxJavaActivity extends ResponseActivity {

    /*
     * io.reactivex.rxjava3.core.Flowable: 0..N flows, supporting Reactive-Streams and backpressure
     * io.reactivex.rxjava3.core.Observable: 0..N flows, no backpressure,
     * io.reactivex.rxjava3.core.Single: a flow of exactly 1 item or an error,
     * io.reactivex.rxjava3.core.Completable: a flow without items but only a completion or error signal,
     * io.reactivex.rxjava3.core.Maybe: a flow with no items, exactly one item or an error.
     */

    /**
     * 创建观察者
     */
    private void createObserver() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    /*
     * 1. 创建
     * 2. 发送事件
     * 3. 延迟创建
     * https://www.jianshu.com/p/e19f8ed863b1
     */
    private void createObservable() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("next");
                emitter.onComplete();
            }
        });
        Observable<String> observable1 = Observable.just("just");//快速创建 & 发送10个以下事件
        Observable<String> observable2 = Observable.empty();//快速创建 & 仅发送onComplete事件
        Observable<String> observable3 = Observable.error(new NullPointerException());//快速创建 & 仅发送onError事件

        Integer[] items = {0, 1, 2, 3, 4};
        Observable<Integer> observable4 = Observable.fromArray(items);//快速创建 & 发送10个以上事件（数组形式）& 传入数组数据
        List<Integer> list = Arrays.asList(items);
        Observable<Integer> observable5 = Observable.fromIterable(list);//快速创建 & 传入List数据

        Observable<Long> observable6 = Observable.timer(2, TimeUnit.SECONDS);//延迟指定时间后，发送1个数值0
        /*
         * 参数1：起始延迟时间
         * 参数2：间隔时间
         */
        Observable<Long> observable7 = Observable.interval(3, 1, TimeUnit.SECONDS);//每隔指定时间就发送事件
        /*
         * 参数1：起始点
         * 参数2：发送数据数量
         * 参数3：起始延迟时间
         * 参数4：间隔时间
         */
        Observable<Long> observable8 = Observable.intervalRange(0, 10, 3, 1, TimeUnit.SECONDS);//每隔指定时间就发送事件，可指定发送的数据范围
        /*
         * 参数1：起始点
         * 参数2：发送数据数量
         */
        Observable<Integer> observable9 = Observable.range(0, 10);//连续发送1个事件序列，可指定发送的数据范围
        /*
         * 参数1：起始点
         * 参数2：发送数据数量
         */
        Observable<Long> observable10 = Observable.rangeLong(0, 10);//连续发送1个事件序列，可指定发送的数据范围
    }

    /*
     * 变换操作符
     * https://www.jianshu.com/p/904c14d253ba
     */
    private void map() {
        //数据类型转换
        Observable.just(1, 2, 3)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Throwable {
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //无序的将被观察者发送的整个事件序列进行变换
        Observable.just(1, 2, 3)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Throwable {
                        return Observable.just(String.valueOf(integer));
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //有序的将被观察者发送的整个事件序列进行变换
        Observable.just(1, 2, 3)
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Throwable {
                        return Observable.just(String.valueOf(integer));
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //缓存被观察者发送的事件
        Observable.just(1, 2, 3)
                .buffer(2, 1)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Throwable {
                        showResponse(String.valueOf(integers));
                    }
                });
    }

    /*
     * 合并操作符
     * 1. 组合多个被观察者
     * 2. 合并多个事件
     * 3. 发送事件前追加发送事件
     * 4. 统计发送事件数量
     * https://www.jianshu.com/p/c2a7c03da16d
     */
    private void concat() {
        //concat（）组合被观察者数量≤4个，而concatArray（）则可＞4个
        Observable.concat(
                Observable.just("1", "2", "3"),
                Observable.just("4", "5"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //merge（）组合被观察者数量≤4个，mergeArray（）则可＞4个，合并后按照时间线并行执行
        Observable.merge(
                Observable.intervalRange(1, 3, 0, 1, TimeUnit.SECONDS),
                Observable.intervalRange(4, 5, 1, 1, TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        showResponse(String.valueOf(aLong));
                    }
                });
        //concatDelayError（） / mergeDelayError（）
        //将onError事件推迟到其他被观察者发送事件后才触发
        Observable.concatArrayDelayError(
                Observable.just("1", "2", "3"),
                Observable.error(new NullPointerException()),
                Observable.just("4", "5")
        ).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                showResponse(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                showResponse(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void zip() {
        //Zip（） = 按个数合并，即1对1合并；CombineLatest（） = 按时间合并，即在同一个时间点上合并
        Observable.zip(
                Observable.just("1", "2", "3"),
                Observable.just(4, 5),
                new BiFunction<String, Integer, String>() {
                    @Override
                    public String apply(String s, Integer integer) throws Throwable {
                        return s + integer;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        Observable.combineLatest(
                Observable.just("1", "2", "3"),
                Observable.intervalRange(4, 5, 1, 1, TimeUnit.SECONDS),
                new BiFunction<String, Long, String>() {
                    @Override
                    public String apply(String s, Long aLong) throws Throwable {
                        return s + aLong;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //combineLatestDelayError，似于concatDelayError（） / mergeDelayError（）
        //把被观察者需要发送的事件聚合成1个事件 & 发送
        Observable.just("1", "2", "3")
                .reduce(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Throwable {
                        return s + s2;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //将被观察者Observable发送的数据事件收集到一个数据结构里
        Observable.just("1", "2", "3")
                .collect(new Supplier<ArrayList<String>>() {
                             @Override
                             public ArrayList<String> get() throws Throwable {
                                 return new ArrayList<>();
                             }
                         },
                        new BiConsumer<ArrayList<String>, String>() {
                            @Override
                            public void accept(ArrayList<String> strings, String s) throws Throwable {
                                strings.add(s);
                            }
                        })
                .subscribe(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings) throws Throwable {
                        showResponse(strings.toString());
                    }
                });
    }

    private void startWith() {
        //追加数据顺序 = 后调用先追加
        Observable.just("4", "5")
                .startWithArray("1", "2", "3")
                .startWith(Observable.just("0"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //统计被观察者发送事件的数量
        Observable.just("1", "2", "3")
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        showResponse(String.valueOf(aLong));
                    }
                });
    }

    /*
     * 功能操作符
     * 1. 延迟操作
     * 2. 在事件的生命周期中操作
     * 3. 错误处理
     * https://www.jianshu.com/p/b0c3669affdb
     */
    private void delay() {
        //使得被观察者延迟一段时间再发送事件
        Observable.just("1", "2", "3")
                .delay(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {

                    }
                });
    }

    private void doSomething() {
        Observable.just("1", "2", "3")
                // 1. 当Observable每发送1次数据事件就会调用1次
                .doOnEach(new Consumer<Notification<String>>() {
                    @Override
                    public void accept(Notification<String> stringNotification) throws Throwable {
                        Log.d(TAG, "doOnEach: " + stringNotification.getValue());
                    }
                })
                // 2. 执行Next事件前调用
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        Log.d(TAG, "doOnNext: " + s);
                    }
                })
                // 3. 执行Next事件后调用
                .doAfterNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        Log.d(TAG, "doAfterNext: " + s);
                    }
                })
                // 4. Observable正常发送事件完毕后调用
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.e(TAG, "doOnComplete: ");
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
                        Log.e(TAG, "doOnSubscribe: ");
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
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "接收到了事件" + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    private void error() {
        //遇到错误时，发送1个特殊事件 & 正常终止
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onError(new NullPointerException());
            }
        })
                .onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Throwable {
                        // 捕捉错误异常
                        Log.e(TAG, "在onErrorReturn处理了错误: " + throwable.toString());
                        return "666";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //遇到错误时，发送1个新的Observable，拦截的错误 = Throwable
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onError(new NullPointerException());
            }
        })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(Throwable throwable) throws Throwable {
                        // 1. 捕捉错误异常
                        Log.e(TAG, "在onErrorReturn处理了错误: " + throwable.toString());

                        // 2. 发生错误事件后，发送一个新的被观察者 & 发送事件序列
                        return Observable.just("11", "22");
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        showResponse(s);
                    }
                });
        //遇到错误时，发送1个新的Observable
        //重试，即当出现错误时，让被观察者（Observable）重新发射数据
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onError(new NullPointerException());
                emitter.onNext("3");
            }
        })
                .retry(3) // 设置重试次数 = 3次
                .retry(new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Throwable {
                        // 捕获异常
                        Log.e(TAG, "retry错误: " + throwable.toString());

                        //返回false = 不重新重新发送数据 & 调用观察者的onError结束
                        //返回true = 重新发送请求（若持续遇到错误，就持续重新发送）
                        return true;
                    }
                })
                .retry(3, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Throwable {
                        // 捕获异常
                        Log.e(TAG, "retry错误: " + throwable.toString());

                        //返回false = 不重新重新发送数据 & 调用观察者的onError结束
                        //返回true = 重新发送请求（若持续遇到错误，就持续重新发送）
                        return true;
                    }
                })
                .retry(new BiPredicate<Integer, Throwable>() {
                    @Override
                    public boolean test(@NonNull Integer integer, @NonNull Throwable throwable) throws Throwable {
                        return false;
                    }
                })
                // 遇到error事件才会回调
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
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "接收到了事件： " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应： " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*
     * 过滤操作符
     * https://www.jianshu.com/p/c3a930a03855
     */

    private void filter() {
        Observable.just(1, 2, 3)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Throwable {
                        return integer > 1;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "获取到的事件元素是：" + integer);
                    }
                });
        Observable.just(1, "2", "3")
                .ofType(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        Log.d(TAG, "获取到的事件元素是： " + s);
                    }
                });
        Observable.just(1, 2, 3)
                .skip(1)
                .skipLast(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "获取到的事件元素是： " + integer);
                    }
                });
        Observable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                .skip(1, TimeUnit.SECONDS) // 跳过第1s发送的数据
                .skipLast(1, TimeUnit.SECONDS) // 跳过最后1s发送的数据
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long along) throws Exception {
                        Log.d(TAG, "获取到的事件元素是： " + along);
                    }
                });
        Observable.just(1, 2, 3, 1, 2, 3)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "不重复的整型事件元素是： " + integer);
                    }
                });
        Observable.just(1, 1, 2, 2, 3)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Throwable {
                        Log.d(TAG, "不连续重复的整型事件元素是： " + integer);
                    }
                });
    }

    /*
     * 条件操作符
     * https://www.jianshu.com/p/954426f90325
     */
    private void conditional() {

    }
}