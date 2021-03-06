package com.example.william.my.module.opensource.activity;

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
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.opensource.tangram.cell.CustomCell;
import com.example.william.my.module.opensource.tangram.cell.CustomHolderCell;
import com.example.william.my.module.opensource.tangram.holder.CustomViewHolder;
import com.example.william.my.module.opensource.tangram.support.CustomClickSupport;
import com.example.william.my.module.opensource.tangram.support.CustomExposureSupport;
import com.example.william.my.module.opensource.tangram.view.CustomAnnotationView;
import com.example.william.my.module.opensource.tangram.view.CustomCellView;
import com.example.william.my.module.opensource.tangram.view.CustomInterfaceView;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.ResourceUtilsService;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.util.IInnerImageSetter;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity_tangram);
        recyclerView = findViewById(R.id.tangram_rv);

        // ????????? Tangram ??????
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view, String url) {
                //??????????????? Picasso ????????????
                //Picasso.with(context).load(url).into(view);
                Glide.with(TangramActivity.this).load(url).into(view);
            }
        }, ImageView.class);

        // ????????? TangramBuilder
        TangramBuilder.InnerBuilder builder = TangramBuilder.newInnerBuilder(TangramActivity.this);

        // ?????????????????????????????????
        // ????????????????????????????????????View????????????1?????????????????????????????????TestView????????????
        //builder.registerCell("type", TestView.class);
        // ???????????????????????????????????? model????????????View
        //builder.registerCell("type", TestCell.class, TestView.class);
        // ????????????????????????????????????model????????????ViewHolder
        //builder.registerCell("type", TestCell.class, new ViewHolderCreator<>(R.layout.item_holder, TestViewHolder.class, TestView.class));

        builder.registerCell("InterfaceCell", CustomInterfaceView.class);
        builder.registerCell("AnnotationCell", CustomAnnotationView.class);
        builder.registerCell("CustomCell", CustomCell.class, CustomCellView.class);
        builder.registerCell("HolderCell", CustomHolderCell.class,
                new ViewHolderCreator<>(R.layout.open_item_tangram, CustomViewHolder.class, LinearLayout.class));

        // ??????TangramEngine??????
        engine = builder.build();

        // ???????????? support ?????? engine
        //engine.register(InternalErrorSupport.class, new SimpleErrorSupport());
        //engine.register(SimpleClickSupport.class, new CustomClickSupport());

        // ????????????
        engine.addSimpleClickSupport(new CustomClickSupport());
        // ????????????
        engine.addExposureSupport(new CustomExposureSupport());

        // ?????? recyclerView
        engine.bindView(recyclerView);

        // ?????? recyclerView ???????????????
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //??? scroll ??????????????? engine ??? onScroll??????????????????????????????????????????????????????????????????
                engine.onScrolled();
            }
        });

        /*
         * {
         *   "type": "container-oneColumn", ---> ??????????????????
         *   "style": { ---> ????????????
         *     ...
         *   },
         *   "header": { ---> ??????header
         *   },
         *   "items": [ ---> ??????????????????
         *     ...
         *   ],
         *   "footer": { ---> ??????footer
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
        // ????????????????????? engine
        if (engine != null) {
            engine.destroy();
        }
    }
}