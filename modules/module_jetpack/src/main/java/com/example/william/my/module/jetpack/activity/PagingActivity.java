package com.example.william.my.module.jetpack.activity;

import static autodispose2.AutoDispose.autoDisposable;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.CombinedLoadStates;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.data.ArticleDetailBean;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.jetpack.R;
import com.example.william.my.module.jetpack.adapter.PagingAdapter;
import com.example.william.my.module.jetpack.comparator.ArticleComparator;
import com.example.william.my.module.jetpack.model.ArticleViewModel;
import com.example.william.my.module.router.ARouterPath;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
 */
@Route(path = ARouterPath.JetPack.JetPack_Paging)
public class PagingActivity extends BaseActivity {

    private PagingAdapter mPagingAdapter;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jet_activity_paging);

        mPagingAdapter = new PagingAdapter(new ArticleComparator());
        RecyclerView mRecycleView = findViewById(R.id.paging_recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mPagingAdapter);

        ArticleViewModel mViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        //initArticleLiveData(mViewModel);

        initArticleFlowable(mViewModel);

        mPagingAdapter.addLoadStateListener(new Function1<CombinedLoadStates, Unit>() {
            @Override
            public Unit invoke(CombinedLoadStates combinedLoadStates) {
                return null;
            }
        });
    }

    /**
     * Paging Coroutines -> LiveData
     */
    private void initArticleLiveData(ArticleViewModel viewModel) {
        viewModel.getArticleLiveData().observe(this, new Observer<PagingData<ArticleDetailBean>>() {
            @Override
            public void onChanged(PagingData<ArticleDetailBean> pagingData) {
                mPagingAdapter.submitData(getLifecycle(), pagingData);
            }
        });
    }

    /**
     * Paging RxJava -> Flowable
     */
    private void initArticleFlowable(ArticleViewModel viewModel) {
        mDisposable.add(viewModel.getArticleFlowable()
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new Consumer<PagingData<ArticleDetailBean>>() {
                    @Override
                    public void accept(PagingData<ArticleDetailBean> pagingData) throws Throwable {
                        mPagingAdapter.submitData(getLifecycle(), pagingData);
                    }
                }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // clear all the subscriptions
        mDisposable.clear();
    }
}