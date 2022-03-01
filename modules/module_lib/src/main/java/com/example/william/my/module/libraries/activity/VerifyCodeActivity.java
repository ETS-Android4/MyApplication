package com.example.william.my.module.libraries.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.verifycode.VerifyCodeView;
import com.example.william.my.module.libraries.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.Lib.Lib_VerifyCode)
public class VerifyCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_activity_verify_code);
        VerifyCodeView codeView = findViewById(R.id.securityCodeView);
        codeView.setEditContent("0731");
    }
}