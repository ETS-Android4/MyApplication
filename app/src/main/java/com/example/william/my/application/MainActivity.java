package com.example.william.my.application;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.navigation.LoginNavigationImpl;

/**
 * _______________#########_______________________
 * ______________############_____________________
 * ______________#############____________________
 * _____________##__###########___________________
 * ____________###__######_#####__________________
 * ____________###_#######___####_________________
 * ___________###__##########_####________________
 * __________####__###########_####_______________
 * ________#####___###########__#####_____________
 * _______######___###_########___#####___________
 * _______#####___###___########___######_________
 * ______######___###__###########___######_______
 * _____######___####_##############__######______
 * ____#######__#####################_#######_____
 * ____#######__##############################____
 * ___#######__######_#################_#######___
 * ___#######__######_######_#########___######___
 * ___#######____##__######___######_____######___
 * ___#######________######____#####_____#####____
 * ____######________#####_____#####_____####_____
 * _____#####________####______#####_____###______
 * ______#####______;###________###______#________
 * ________##_______####________####______________
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isRetry()) return;

        //setContentView(R.layout.activity_main);

        //ARouter.getInstance()
        //        .inject(this);//BaseActivity

        //ARouter.getInstance()
        //        .build(ARouterPath.Module)
        //        //.withTransition(R.anim.basics_slide_in, R.anim.basics_slide_out)
        //        .withString("param_key", "param_value")
        //        .navigation(this, new LoginNavigationImpl());
        //.greenChannel()//使用绿色通道(跳过所有的拦截器)
        //.navigation();
        startActivity(new Intent(this, MainFlutterActivity.class));
        finish();
    }

    /**
     * 避免从桌面启动程序后，会重新实例化入口类的activity
     */
    private Boolean isRetry() {
        if (!isTaskRoot()) {
            if (getIntent().hasCategory(Intent.CATEGORY_LAUNCHER) &&
                    Intent.ACTION_MAIN.equals(getIntent().getAction())) {
                finish();
                return true;
            }
        }
        return false;
    }
}