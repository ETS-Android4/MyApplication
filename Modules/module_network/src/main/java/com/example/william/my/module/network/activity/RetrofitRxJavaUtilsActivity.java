package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.api.NetworkService;
import com.example.william.my.module.router.ARouterPath;

/**
 * {@link RetrofitUtils}
 */
@Route(path = ARouterPath.NetWork.NetWork_RetrofitRxJavaUtils)
public class RetrofitRxJavaUtilsActivity extends BaseResponseActivity {

    private boolean b;

    private NetworkService service;

    @Override
    public void initView() {
        super.initView();
//        service = RetrofitUtils.buildApi(NetworkService.class);
    }

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
     * GsonConverterFactory Gson 解析 -> BannerBean
     */
    private void getBanner() {
//        Observable<BannerBean> obs = RetrofitUtils.buildObs(service.getBanner());
//
//        obs.subscribe(new RetrofitObserver<BannerBean>() {
//            @Override
//            public void onLoading() {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull BannerBean response) {
//                String net_success = "Success: " + new Gson().toJson(response);
//                showResponse(net_success);
//            }
//
//            @Override
//            public void onFailure(@NonNull ApiException e) {
//                String net_error = "Error: " + e.getMessage();
//                showResponse(net_error);
//            }
//        });
    }

    /**
     * RetrofitConverterFactory 自定义解析 -> List<BannerDetailBean>
     */
    private void getBannerList() {
//        Observable<RetrofitResponse<List<BannerDetailBean>>> responseObs = RetrofitUtils.buildObs(service.getBannerResponse());
//
//        responseObs.subscribe(new RetrofitObserver<RetrofitResponse<List<BannerDetailBean>>>() {
//            @Override
//            public void onLoading() {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull RetrofitResponse<List<BannerDetailBean>> response) {
//                String net_success = "Success: " + new Gson().toJson(response);
//                showResponse(net_success);
//            }
//
//            @Override
//            public void onFailure(@NonNull ApiException e) {
//                String net_error = "Error: " + e.getMessage();
//                showResponse(net_error);
//            }
//        });
    }
}