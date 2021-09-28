package com.example.william.my.module.custom.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.core.verifycode.VerifyCodeView;
import com.example.william.my.module.custom.R;
import com.example.william.my.module.router.ARouterPath;

@Route(path = ARouterPath.CustomView.CustomView_SecurityCode)
public class SecurityCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_security_code_view);
        VerifyCodeView codeView = findViewById(R.id.securityCodeView);
        codeView.setEditContent("0731");
    }
}