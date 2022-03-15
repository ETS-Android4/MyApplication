package com.example.william.my.bean.repo;

import androidx.lifecycle.LiveData;

import com.example.william.my.bean.api.NetworkService;
import com.example.william.my.bean.data.ArticleDataBean;
import com.example.william.my.core.retrofit.callback.RetrofitResponseCallback;
import com.example.william.my.core.retrofit.exception.ApiException;
import com.example.william.my.core.retrofit.response.RetrofitResponse;
import com.example.william.my.core.retrofit.utils.RetrofitUtils;

import org.jetbrains.annotations.NotNull;

/**
 * 数据仓库
 * LiveData 是一个生命周期感知组件，它并不属于 Repositories 或者 DataSource 层，最好在 View 和 ViewModel 层中使用它。
 * 如果在 Repositories 或者 DataSource 中使用会有几个问题：
 * 1. 它不支持线程切换，其次不支持背压，也就是在一段时间内发送数据的速度 > 接受数据的速度，LiveData 无法正确的处理这些请求
 * 2. 使用 LiveData 的最大问题是所有数据转换都将在主线程上完成
 */
public class ArticleRepository implements ArticleDataSource {

    private final NetworkService service;

    private static ArticleRepository sInstance;

    private ArticleRepository() {
        service = RetrofitUtils.buildApi(NetworkService.class);
    }

    public static ArticleRepository getInstance() {
        if (sInstance == null) {
            synchronized (ArticleRepository.class) {
                if (sInstance == null) {
                    sInstance = new ArticleRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getArticle(int page, LoadArticleCallback callback) {
        RetrofitUtils.buildSingle(
                service.getArticleResponse(page),
                new RetrofitResponseCallback<ArticleDataBean>() {

                    @Override
                    public void onLoading() {
                        super.onLoading();
                        callback.showLoading();
                    }

                    @Override
                    public void onFailure(@NotNull ApiException e) {
                        callback.onFailure(e.getMessage());
                    }

                    @Override
                    public void onResponse(ArticleDataBean response) {
                        callback.onArticleLoaded(response.getDatas());
                    }
                });
    }

    public LiveData<RetrofitResponse<ArticleDataBean>> loadArticle(int page) {
        return RetrofitUtils.buildLiveData(service.getArticleResponse(page));
    }
}
