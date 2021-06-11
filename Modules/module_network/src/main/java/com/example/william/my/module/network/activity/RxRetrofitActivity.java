package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * {@link RxRetrofit}
 */
@Route(path = ARouterPath.NetWork.NetWork_RxRetrofit)
public class RxRetrofitActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        getBanner();
    }

    private void getBanner() {
//        RxRetrofit
//                .<List<BannerDetailBean>>Builder()
//                .api(Urls.banner)
//                .get()
//                .build()
//                .createObservable()
//                .subscribe(new RetrofitObserver<RetrofitResponse<List<BannerDetailBean>>>() {
//                    @Override
//                    public void onLoading() {
//
//                    }
//
//                    @Override
//                    public void onResponse(@NonNull RetrofitResponse<List<BannerDetailBean>> response) {
//                        String net_success = "Success: " + new Gson().toJson(response);
//                        showResponse(net_success);
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull ApiException e) {
//                        String net_error = "Error: " + e.getMessage();
//                        showResponse(net_error);
//                    }
//                });
    }
}