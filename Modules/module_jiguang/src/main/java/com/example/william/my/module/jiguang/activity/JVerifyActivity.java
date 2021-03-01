package com.example.william.my.module.jiguang.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.william.my.module.jiguang.R;
import com.example.william.my.module.jiguang.common.Constants;
import com.example.william.my.module.jiguang.common.JVerifyUIConfigUtil;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.VerifyListener;

public class JVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "JVerify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_jverify);

        Button mButtonLogin = findViewById(R.id.btn_login);
        mButtonLogin.setOnClickListener(this);

        Button mButtonLoginDialog = findViewById(R.id.btn_login_dialog);
        mButtonLoginDialog.setOnClickListener(this);

        Button mButtonRegister = findViewById(R.id.btn_register);
        mButtonRegister.setOnClickListener(this);

        initPermission();
    }

    private void initPermission() {
        ActivityCompat.requestPermissions(JVerifyActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 1000);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            JVerificationInterface.setCustomUIWithConfig(
                    JVerifyUIConfigUtil.getConfig(this, JVerifyUIConfigUtil.Direction.portrait),
                    JVerifyUIConfigUtil.getConfig(this, JVerifyUIConfigUtil.Direction.landscape));
            JVerificationInterface.loginAuth(this, new VerifyListener() {
                @Override
                public void onResult(final int code, final String token, String operator) {
                    Log.e(TAG, "onResult: code=" + code + ",token=" + token + ",operator=" + operator);
                    final String errorMsg = "operator=" + operator + ",code=" + code + "\ncontent=" + token;
                    if (code == Constants.CODE_LOGIN_SUCCESS) {
                        Log.e(TAG, "onResult: loginSuccess");
                    } else if (code != Constants.CODE_LOGIN_CANCELED) {
                        Log.e(TAG, "onResult: loginError");
                    }
                }
            });
        } else if (id == R.id.btn_login_dialog) {
            JVerificationInterface.setCustomUIWithConfig(
                    JVerifyUIConfigUtil.getConfig(this, JVerifyUIConfigUtil.Direction.dialogPortrait),
                    JVerifyUIConfigUtil.getConfig(this, JVerifyUIConfigUtil.Direction.dialogLandscape));
            JVerificationInterface.loginAuth(this, new VerifyListener() {
                @Override
                public void onResult(final int code, final String token, String operator) {
                    Log.e(TAG, "onResult: code=" + code + ",token=" + token + ",operator=" + operator);
                    final String errorMsg = "operator=" + operator + ",code=" + code + "\ncontent=" + token;
                    if (code == Constants.CODE_LOGIN_SUCCESS) {
                        Log.e(TAG, "onResult: loginSuccess");
                    } else if (code != Constants.CODE_LOGIN_CANCELED) {
                        Log.e(TAG, "onResult: loginError");
                    }
                }
            });
        } else if (id == R.id.btn_register) {
            //No phone privilege permission
            JVerificationInterface.getToken(this, new VerifyListener() {
                @Override
                public void onResult(int code, final String content, final String operator) {
                    Log.e(TAG, "onResult: code=" + code + ",content=" + content + ",operator=" + operator);
                }
            });
        }
    }


}