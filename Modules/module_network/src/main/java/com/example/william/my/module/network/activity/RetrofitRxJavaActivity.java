package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.network.retrofit.converter.RetrofitConverterFactory;
import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannersBean;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.service.NetworkService;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Route(path = ARouterPath.NetWork.NetWork_RetrofitRxJava)
public class RetrofitRxJavaActivity extends BaseResponseActivity {

    private boolean b;

    @Override
    public void setOnClick() {
        super.setOnClick();
        b = !b;
        if (b) {
            getBanner();
        } else {
            getBannerResponse();
        }
    }

    private void getBanner() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)//baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
                .addConverterFactory(ScalarsConverterFactory.create()) //标准类型转换器，防止上传图文的时候带引号
                .addConverterFactory(GsonConverterFactory.create())//解析工厂类
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 支持RxJava
                .build();

        NetworkService service = retrofit.create(NetworkService.class);

        Observable<BannersBean> observable = service.getBanners();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannersBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BannersBean banners) {
                        String net_success = "getBanner: " + new Gson().toJson(banners);
                        showResponse(net_success);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String net_error = "Error: " + e.getMessage();
                        showResponse(net_error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * RetrofitConverterFactory
     */
    private void getBannerResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)//baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
                .addConverterFactory(ScalarsConverterFactory.create()) //标准类型转换器，防止上传图文的时候带引号
                .addConverterFactory(RetrofitConverterFactory.create())//解析工厂类
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 支持RxJava
                .build();

        NetworkService service = retrofit.create(NetworkService.class);

        Observable<RetrofitResponse<List<BannerBean>>> observable = service.getBannersResponse();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RetrofitResponse<List<BannerBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RetrofitResponse<List<BannerBean>> banner) {
                        String net_success = "getBannerResponse: " + new Gson().toJson(banner);
                        showResponse(net_success);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String net_error = "Error: " + e.getMessage();
                        showResponse(net_error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}