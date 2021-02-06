package com.example.william.my.module.open.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.router.ARouterPath;

import java.util.Arrays;

/**
 * 自动翻页的时间
 * app:autoTurningTime="3000"
 * 是否循环播放
 * app:canLoop="true"
 * <p>
 * https://github.com/saiwu-bigkoo/Android-ConvenientBanner
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Banner)
public class BannerActivity extends BaseActivity {

    //顶部广告栏控件，加载本地图片
    private ConvenientBanner<Integer> mConvenientBannerLocal;
    //顶部广告栏控件，加载网络图片
    private ConvenientBanner<String> mConvenientBannerNet;

    //本地图片
    private final Integer[] mImagesLocal = new Integer[]{
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher};

    //网络图片
    private final String[] mImagesNet = new String[]{
            "https://img2.woyaogexing.com/2018/08/14/9dc2bb4e96604f6993e46b05ed17915c!600x600.jpeg",
            "https://img2.woyaogexing.com/2018/08/14/f1472844169f4c059c0add35b10ecda9!600x600.jpeg",
            "https://img2.woyaogexing.com/2018/08/14/c47a1c92bc8449178f966a90285e1f88!600x600.jpeg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_banner);

        mConvenientBannerLocal = findViewById(R.id.localConvenientBanner);
        mConvenientBannerLocal.setPages(new CBViewHolderCreator() {

            @Override
            public Holder<Integer> createHolder(View itemView) {
                return new LocalImageHolderView(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.basics_layout_image;
            }
        }, Arrays.asList(mImagesLocal))
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器，不需要圆点指示器可以不设
                .setPageIndicator(new int[]{R.drawable.open_banner_circle_nor, R.drawable.open_banner_circle_sel})
                //设置指示器的位置（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                //设置指示器是否可见
                .setPointViewVisible(true)
        //监听单击事件
        //.setOnItemClickListener(this)
        //监听翻页事件
        //.setOnPageChangeListener(this)
        ;

        mConvenientBannerNet = findViewById(R.id.netConvenientBanner);
        mConvenientBannerNet.setPages(new CBViewHolderCreator() {

            @Override
            public Holder<String> createHolder(View itemView) {
                return new NetImageHolderView(BannerActivity.this, itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.basics_layout_image;
            }
        }, Arrays.asList(mImagesNet))
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器，不需要圆点指示器可以不设
                .setPageIndicator(new int[]{R.drawable.open_banner_circle_nor, R.drawable.open_banner_circle_sel})
                //设置指示器的位置（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                //设置指示器是否可见
                .setPointViewVisible(true)
        //监听单击事件
        //.setOnItemClickListener(this)
        //监听翻页事件
        //.setOnPageChangeListener(this)
        ;
        mConvenientBannerNet.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBannerLocal.startTurning();
        mConvenientBannerNet.startTurning();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止翻页
        mConvenientBannerLocal.stopTurning();
        mConvenientBannerNet.stopTurning();
    }

    public static class LocalImageHolderView extends Holder<Integer> {

        private ImageView mImageView;

        public LocalImageHolderView(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            mImageView = itemView.findViewById(R.id.basics_imageView);
        }

        @Override
        public void updateUI(Integer data) {
            mImageView.setImageResource(data);
        }

    }

    public static class NetImageHolderView extends Holder<String> {

        private final Context mContext;
        private ImageView mImageView;

        public NetImageHolderView(Context context, View itemView) {
            super(itemView);
            mContext = context;
        }

        @Override
        protected void initView(View itemView) {
            mImageView = itemView.findViewById(R.id.basics_imageView);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @Override
        public void updateUI(String data) {
            Glide.with(mContext).load(data).into(mImageView);
        }

    }
}