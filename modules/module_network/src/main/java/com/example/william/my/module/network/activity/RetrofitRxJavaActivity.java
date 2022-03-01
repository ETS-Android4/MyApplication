package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.api.NetworkService;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.bean.data.ArticleBean;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.converter.RetrofitConverterFactory;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
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
            getBannerList();
        }
    }

    /**
     * GsonConverterFactory Gson 解析 -> ArticleBean
     */
    private void getBanner() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.Url_Base)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NetworkService service = retrofit.create(NetworkService.class);

        Single<ArticleBean> observable = service.getArticle(0);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArticleBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ArticleBean articleBean) {
                        String netSuccess = "GsonConverterFactory: " + new Gson().toJson(articleBean);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String netError = "Error: " + e.getMessage();
                        showResponse(netError);
                    }
                });
    }

    /**
     * RetrofitConverterFactory 自定义解析 -> RetrofitResponse<ArticleDataBean>
     */
    private void getBannerList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.Url_Base)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(RetrofitConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NetworkService service = retrofit.create(NetworkService.class);

        Single<RetrofitResponse<ArticleDataBean>> observable = service.getArticleResponse(0);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RetrofitResponse<ArticleDataBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(RetrofitResponse<ArticleDataBean> articleBean) {
                        String netSuccess = "RetrofitConverterFactory: " + new Gson().toJson(articleBean);
                        showResponse(netSuccess);
                    }

                    @Override
                    public void onError(Throwable e) {
                        String netError = "Error: " + e.getMessage();
                        showResponse(netError);
                    }
                });
    }
}