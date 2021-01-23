package com.example.william.my.jet.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.jet.R;
import com.example.william.my.jet.adapter.PagingAdapter;
import com.example.william.my.jet.model.LoginViewModel;
import com.example.william.my.module.bean.ArticlesBean;
import com.example.william.my.module.router.ARouterPath;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableConverter;

/**
 * https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
 */
@Route(path = ARouterPath.JetPack.JetPack_Paging)
public class PagingActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private PagingAdapter mPagingAdapter;

    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jet_activity_paging);

        mRecycleView = findViewById(R.id.paging_recycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mPagingAdapter = new PagingAdapter(new DiffUtil.ItemCallback<ArticlesBean.DataBean.ArticleBean>() {

            @Override
            public boolean areItemsTheSame(@NonNull @NotNull ArticlesBean.DataBean.ArticleBean oldItem, @NonNull @NotNull ArticlesBean.DataBean.ArticleBean newItem) {
                return oldItem.getId() == newItem.getId();

            }

            @Override
            public boolean areContentsTheSame(@NonNull @NotNull ArticlesBean.DataBean.ArticleBean oldItem, @NonNull @NotNull ArticlesBean.DataBean.ArticleBean newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());
            }
        });
        mRecycleView.setAdapter(mPagingAdapter);

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.getFlowable()
                .subscribe(new Subscriber<PagingData<ArticlesBean.DataBean.ArticleBean>>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(PagingData<ArticlesBean.DataBean.ArticleBean> articleBeanPagingData) {
                        mPagingAdapter.submitData(null, articleBeanPagingData);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}