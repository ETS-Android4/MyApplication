package com.example.william.my.module.util.activity;

import android.util.Base64;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

import java.nio.charset.StandardCharsets;

/**
 * https://www.ssleye.com/
 */
@Route(path = ARouterPath.Util.Util_Encrypt)
public class EncryptUtilsActivity extends BaseResponseActivity {

    private static final String ALGORITHM_AES = "AES/ECB/PKCS5Padding";

    private final String data = "1234567890123456";
    private final byte[] bytesData = data.getBytes(StandardCharsets.UTF_8);

    private final String key = "1234567890123456";
    private final byte[] bytesKey = key.getBytes(StandardCharsets.UTF_8);

    private final String res = "dXzNDNxckOrb7uz2ON0AAAUBh6DN5amHLLqwkatz5VM=";
    private final byte[] bytesRes = Base64.decode(res, Base64.NO_WRAP);


    @Override
    public void initView() {
        super.initView();

        byte[] encrypted = EncryptUtils.encryptAES(bytesData, bytesKey, ALGORITHM_AES, null);
        LogUtils.e(TAG, Base64.encodeToString(encrypted, Base64.NO_WRAP));
        LogUtils.e(TAG, ConvertUtils.bytes2HexString(encrypted));

        byte[] decrypted = EncryptUtils.decryptAES(bytesRes, bytesKey, ALGORITHM_AES, null);
        LogUtils.e(TAG, new String(decrypted, StandardCharsets.UTF_8));
    }
}