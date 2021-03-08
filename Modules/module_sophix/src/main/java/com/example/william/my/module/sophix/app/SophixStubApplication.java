package com.example.william.my.module.sophix.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.multidex.MultiDex;

import com.example.william.my.module.sophix.BuildConfig;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Sophix入口类，专门用于初始化Sophix，不应包含任何业务逻辑。
 * 此类必须继承自SophixApplication，onCreate方法不需要实现。
 * 此类不应与项目中的其他类有任何互相调用的逻辑，必须完全做到隔离。
 * AndroidManifest中设置application为此类，而SophixEntry中设为原先Application类。
 * 注意原先Application里不需要再重复初始化Sophix，并且需要避免混淆原先Application类。
 * 如有其它自定义改造，请咨询官方后妥善处理。
 */
public class SophixStubApplication extends SophixApplication {

    private final String TAG = "SophixStubApplication";

    private static final String appKey = "333389975";

    private static final String appSecret = "a11e251ffdc24882a7006f6cdb758426";

    private static final String rsa = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9wrTx6mdOuDBASbIqRHsyM6noWYGR8XGcadO1xtw0TiGLkXME82oOGAhyl70i5zwIo1nZYZUbnuJJtvKXzhGZde92oaer/KYMzUvNTqekECTFNfbKvBnJOpuw+wfaPmymGPN9gECSRbomZ05kF9BBldqxqNdh9nlYjFakIsABnXjkTvabL1w+Of77HFNyPs8XTnWxbTigTtmTksqw37G9OQRV6tNe8vOLZrHfMaRr7dTCmPj75VnHDqiwqK/vCSDzF1MAvgA3XbEG9b6XhKOqK6QcPfOSBOLBJoLTMUsmwwaEk5Z6tGYcm46tFebvu/RuqICQi7iU+bVwdhipXrDXAgMBAAECggEBAJSkWCjHfHTlQqaZE76YNgh0/7rJof04K85h9zyEsSknqo8xN+/A3Gp92OAqjDUy/IunqVHmZm5kXs1vSUgwWwjioNlEd5r5JPkSNzZzTk3td2AjjVXxUiGWjy5q9RO3olPZZ6H/PJVXmSFL+tPc83zsbYqMv2o1L4h1Jckcj1qzLZMRz+Zdv5W7R8fxTrCBbQMYhj7JF3javCXHLJ2WoRvJrQe0IXf2puePKZQcl55pGVaxntf457EWSRzx9Ja8blYNJqAFkgPrx2vOSmRzirSoYal7zHixwEwLHBxcsjLTeLEwAujD/CEuHJFdcPgAFOaRrIJ8hfqi+LsX5qzOy8ECgYEA6Pf2k3y1tdtEHwioelJGosi7DwU3ZGCb38/hj4rarBMRTAT7pCwnij8PxijYedYeT2NcziNY7T3xLairqLbd0SuxeYLZ96Z/Vv8wS1BLttTs3qTAJD2r2nlHx/UMQU26dssdJ0aVBil/p8SUrAAq+dbAckjC7gNony/Lqhm6EKMCgYEA0IU45V9GWz+uCb1tjTzFuDFYgsL0Azv6XGt4VImJ/tSrFkJ8pIvWY0h6vWs8qdUKAcM8D1nYqcNUpJ/jukzefYZkoSK2Q80UZDSVbUfKiuRjuLs0S53r1mJh8M6fM3/hxFJLDJTpNtVB0AL8Ay08vPa931giwAA6YWH5X5PN/j0CgYA9Kcys0BMM2bI6y5Uf+DXfSwABY4c1bT+/NN6tRvE+OAtUgAJpCcafvwqvJc4fvp1ajmWxIQqZzqdhK2VV+Byoa+Y+VqJPKIFKH9lYDI60Q5akpXBkk2RpCWpVDvrWskeAJLdUtm0tnvuK2kuGFM0zprQDzSKQRojTMkMwN2zoLQKBgGL0ZfzMCvpwrOnoYF3fUkFIAaYEWIJUa0qCodcUck1hpVNp9/aqefIhykWr51z9QcN2YEE0tRJTJQ7+5bZalldheM3TSEXCxAONxL/YivEA06YuCPI9nlbd13bMx1ZMQNrG5uCwpkAkUhgh4nCrfiB6w76C9/K/6PSf5xUJcn9NAoGAJelAuzBKnnh4jfTJ/VZXCghgkxk3H8ND9aHcLsTEfQQYg4id/KVp9jpWkZKDtBRpbNN8Cl89LUFG4Y/JS5mB4mtBGn5JBgCuWaqoBZur8nl6ashzgAilVfPSU8YfV0QOY7RtMGwJ+vlhje+RnfHS7feRsVQp/lOyqoT3S9F53WE=";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MyRealApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //如果需要使用MultiDex，需要在此处调用。
        MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setSecretMetaData(appKey, appSecret, rsa)
                .setEnableDebug(true)//正式发布该参数必须为false, false会对补丁做签名校验, 否则就可能存在安全漏洞风险
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.e(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.e(TAG, "sophix preload patch success. restart app to make effect.");
                        }
                    }
                }).initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}