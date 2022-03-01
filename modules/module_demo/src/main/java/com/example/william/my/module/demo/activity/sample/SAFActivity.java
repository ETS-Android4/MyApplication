package com.example.william.my.module.demo.activity.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * 存储访问框架 - Storage Access Framework
 */
@Route(path = ARouterPath.Demo.Demo_SAF)
public class SAFActivity extends BaseResponseActivity {

    @Override
    public void onClick(View v) {
        super.onClick(v);
        startSAF();
    }

    private void startSAF() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            showResponse("image uri is : " + uri);
        }
    }
}