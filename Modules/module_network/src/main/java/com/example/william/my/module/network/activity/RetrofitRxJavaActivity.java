package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.retrofit.converter.RetrofitConverterFactory;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.api.NetworkService;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannerDetailBean;
import com.example.william.my.module.router.ARouterPath;
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

/**
 * Schedulers.io() io线程池为一个无边界线程池
 */
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

    /**
     * GsonConverterFactory Gson 解析 -> BannerBean
     */
    private void getBanner() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NetworkService service = retrofit.create(NetworkService.class);

        Observable<BannerBean> observable = service.getBanner();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull BannerBean banner) {
                        String netSuccess = "getBanner: " + new Gson().toJson(banner);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String netError = "Error: " + e.getMessage();
                        showResponse(netError);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * RetrofitConverterFactory 自定义解析 -> List<BannerDetailBean>
     */
    private void getBannerResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(RetrofitConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NetworkService service = retrofit.create(NetworkService.class);

        Observable<RetrofitResponse<List<BannerDetailBean>>> observable = service.getBannerResponse();

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RetrofitResponse<List<BannerDetailBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RetrofitResponse<List<BannerDetailBean>> banner) {
                        String netSuccess = "getBannerResponse: " + new Gson().toJson(banner);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String netError = "Error: " + e.getMessage();
                        showResponse(netError);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}