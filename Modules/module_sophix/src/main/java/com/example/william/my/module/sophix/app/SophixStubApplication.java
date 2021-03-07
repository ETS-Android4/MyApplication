package com.example.william.my.module.sophix.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;

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

    private static final String appKey = "333389330";

    private static final String appSecret = "9c1ea737f5e14863879f5d9cd60bda76";

    private static final String rsa = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCBPXWdu69j33CSO6QJKL7qamk055AtV9ge1yXzS4OrZz1LVDlilzq4jwpRrMVblh+62RhVvz4XXl2WsZpAdUTlq9yongY+iuPu+S9/rYdg99WPE51NDzfwAldgNdRTVG9QAfk6GgLhDXOBHSmdSE6n2NK6pgFEBhTdcIDbPbpmEloJQ0xLXztInuBytaNaxE0zBtzwFAy5J+I+qWnVKpYwzfXPvtEcl5ptgNP0aQd0ugWMuPu0Yd7Mj3FJKHvt3DyT+doyHXmn/5hWGJIwo244Eey8h1ZBx4XYSnfuDd+e1Td7DaRnD+/j995riw9OGAgPjEFLep/KAiag64PZy6G/AgMBAAECggEARPmpKRq/G51nthPwkxbU9cT/C0Y+84Q7ixImZfe6eSMrWmXEDrNJmrB0wGxmFGBF5PHyg+kCWXR6nG+DNL2hnLYhmThlesGjrqn9SLYzV3RN3QOJ41Oo9gY53dY80JByrA+xlOnG+Ze1OoZ27ENn/zrAFO9+I3Dfd+OQUfJa5hhIp27lg1SKPwuIyBfNrliI1mP7jatoN5Wh2RsLfnTrHlMxO4/uATeBnQ8aw6+xne5aPM69yRVHMYCw8i5ftKPSmWdVQEkpKF23QJmf6ZspqQlsSavLWzeKw31qv2fv5AcKxOepo14S/LB0gPePXcMxCnzR9TG8QjWkVT6tVMseKQKBgQDC+MJwuOXrJstSG121JaxnHfHtTIPHfEXMuy7yaTlrY90LmW9iyNc4PwwjxB4KhOMKmkIiAprZXrUBad+aBNFSS3+HfFvYronmKvsurGWEkcvcsXCuhlUfB16bukE6/xmexU5vZU3nSG6IIXkrltDf3UuJFwzRjY8eZ7QoK/cmAwKBgQCpsZIWVeSxzvnQb3LDkpQd+pSlu7WkgNPQ4mSolJE9KZG7uRnwWY1CySWfEED+yLZMnMeem9HWOPlBIiZM8dYZTKDU1OdGZZQO8WW2WFdjdbI8jM/BQtnT8Y5MZeahiRpHH9dip+VtGbjFTWKNAvXsfy1x8t3hLah+gDu88GTWlQKBgFo48QlmloatqhKxtThZWaFSGxy7dpO+bzJhQEVO6hJG4Qg4FXI6DFXr3vtCCsDFY6cJYScJ24xRJiOkUaOdLRwO8dHJe1sW0ZtgMmITxtqE+TiEJ+erJAK6z03y77KUT8/H8BbeYyFuxC9chgzkun2NkyG6N+VUw3DHfnfEeJbvAoGAPa/fe18eE8U56xuYjJ/sWDmPb/AcyABM/9JM5Tuc0K8b2zIey9DCGVUl5/zwo64zFwanSnE6hrWPZq4TXkVLn0bL8JK0wFKM0CHQ58iVfAJ9GKXXoFm89d8J65vfizlb5B8fHS4LqO2Azbij6r74x1A0JQg0wHbomZPkI3sALUECgYANWw6pC9YGXwKbSDycgtepHFVx+eWdecasVUZAvQ38p6DuZM1IhWF0xWiujclBj2UswhIrGjwPtOQ+nw3MUlIQea6+06dOZLjQInC4/3NyDwYX/pxkCT42wAbKWdS0Ix1iDHUMkkJGioER3zn5nrefcL93N6iJEyXeWC38zAXAwA==";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MyRealApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //如果需要使用MultiDex，需要在此处调用。
        //MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setSecretMetaData(appKey, appSecret, rsa)
                .setEnableDebug(true)
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
}