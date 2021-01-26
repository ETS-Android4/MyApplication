package com.example.william.my.jet.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.CombinedLoadStates;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.jet.R;
import com.example.william.my.jet.adapter.PagingAdapter;
import com.example.william.my.jet.comparator.ArticleComparator;
import com.example.william.my.jet.model.LoginViewModel;
import com.example.william.my.module.bean.ArticlesBean;
import com.example.william.my.module.router.ARouterPath;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static autodispose2.AutoDispose.autoDisposable;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
 */
@Route(path = ARouterPath.JetPack.JetPack_Paging)
public class PagingActivity extends AppCompatActivity {

    private PagingAdapter mPagingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jet_activity_paging);

        mPagingAdapter = new PagingAdapter(new ArticleComparator());
        RecyclerView mRecycleView = findViewById(R.id.paging_recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mPagingAdapter);

        LoginViewModel mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //mViewModel.getArticleLiveData().observe(this, new Observer<PagingData<ArticlesBean.DataBean.ArticleBean>>() {
        //    @Override
        //    public void onChanged(PagingData<ArticlesBean.DataBean.ArticleBean> pagingData) {
        //        mPagingAdapter.submitData(getLifecycle(), pagingData);
        //    }
        //});

        mViewModel.getArticleFlowable()
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new Subscriber<PagingData<ArticlesBean.DataBean.ArticleBean>>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(PagingData<ArticlesBean.DataBean.ArticleBean> pagingData) {
                        mPagingAdapter.submitData(getLifecycle(), pagingData);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mPagingAdapter.addLoadStateListener(new Function1<CombinedLoadStates, Unit>() {
            @Override
            public Unit invoke(CombinedLoadStates combinedLoadStates) {
                Log.e("TAG", combinedLoadStates.getRefresh().toString());
                return null;
            }
        });
    }
}