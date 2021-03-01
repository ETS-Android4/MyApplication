package com.example.william.my.module.open.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.open.R;
import com.example.william.my.module.open.tangram.cell.CustomCell;
import com.example.william.my.module.open.tangram.cell.CustomHolderCell;
import com.example.william.my.module.open.tangram.holder.CustomViewHolder;
import com.example.william.my.module.open.tangram.support.CustomClickSupport;
import com.example.william.my.module.open.tangram.support.CustomExposureSupport;
import com.example.william.my.module.open.tangram.view.CustomAnnotationView;
import com.example.william.my.module.open.tangram.view.CustomCellView;
import com.example.william.my.module.open.tangram.view.CustomInterfaceView;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.ResourceUtilsService;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.util.IInnerImageSetter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * https://github.com/alibaba/Tangram-Android
 * http://tangram.pingguohe.net/docs/android/access-tangram
 */
@Route(path = ARouterPath.OpenSource.OpenSource_Tangram)
public class TangramActivity extends BaseActivity {

    TangramEngine engine;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_tangram);
        recyclerView = findViewById(R.id.tangram_rv);

        // 初始化 Tangram 环境
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view, @Nullable String url) {
                //假设你使用 Picasso 加载图片
                //Picasso.with(context).load(url).into(view);
                Glide.with(TangramActivity.this).load(url).into(view);
            }
        }, ImageView.class);

        // 初始化 TangramBuilder
        TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(TangramActivity.this);

        // 注册自定义的卡片和组件
        // 注册绑定组件类型和自定义View，类型为1的组件渲染时会被绑定到TestView的实例上
        //builder.registerCell("type", TestView.class);
        // 注册绑定组件类型、自定义 model、自定义View
        //builder.registerCell("type", TestCell.class, TestView.class);
        // 注册绑定组件类型、自定义model、自定义ViewHolder
        //builder.registerCell("type", TestCell.class, new ViewHolderCreator<>(R.layout.item_holder, TestViewHolder.class, TestView.class));

        builder.registerCell("InterfaceCell", CustomInterfaceView.class);
        builder.registerCell("AnnotationCell", CustomAnnotationView.class);
        builder.registerCell("CustomCell", CustomCell.class, CustomCellView.class);
        builder.registerCell("HolderCell", CustomHolderCell.class,
                new ViewHolderCreator<>(R.layout.open_item_tangram, CustomViewHolder.class, LinearLayout.class));

        // 生成TangramEngine实例
        engine = builder.build();

        // 绑定业务 support 类到 engine
        //engine.register(InternalErrorSupport.class, new SimpleErrorSupport());
        //engine.register(SimpleClickSupport.class, new CustomClickSupport());

        // 处理点击
        engine.addSimpleClickSupport(new CustomClickSupport());
        // 处理曝光
        engine.addExposureSupport(new CustomExposureSupport());

        // 绑定 recyclerView
        engine.bindView(recyclerView);

        // 监听 recyclerView 的滚动事件
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                engine.onScrolled();
            }
        });

        /*
         * {
         *   "type": "container-oneColumn", ---> 描述布局类型
         *   "style": { ---> 描述样式
         *     ...
         *   },
         *   "header": { ---> 描述header
         *   },
         *   "items": [ ---> 描述组件列表
         *     ...
         *   ],
         *   "footer": { ---> 描述footer
         *   }
         * }
         */

        try {
            ResourceUtilsService service = (ResourceUtilsService) ARouter.getInstance().build(ARouterPath.Service.ResourceUtilsService).navigation();
            JSONArray data = new JSONArray(service.getAssets("data.json"));
            engine.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出的时候销毁 engine
        if (engine != null) {
            engine.destroy();
        }
    }
}