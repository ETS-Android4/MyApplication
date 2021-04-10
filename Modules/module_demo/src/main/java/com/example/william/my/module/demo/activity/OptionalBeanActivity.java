package com.example.william.my.module.demo.activity;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.william.my.library.utils.OptionalBean;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.bean.LoginBean;
import com.example.william.my.module.bean.LoginUserBean;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link OptionalBean}
 */
public class OptionalBeanActivity extends BaseResponseActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initView() {
        super.initView();

        LoginUserBean loginUserBean = new LoginUserBean();
        loginUserBean.setNickname("nick name");
        LoginBean loginBean = new LoginBean();
        loginBean.setUserData(loginUserBean);

        // 1. 基本调用
        String nickName = OptionalBean.ofNullable(loginBean)
                .getBean(LoginBean::getUserData)
                .getBean(LoginUserBean::getNickname).get();
        Log.e("TAG", nickName);

        // 2. 扩展的 isPresent方法 用法与 Optional 一样
        boolean present = OptionalBean.ofNullable(loginBean)
                .getBean(LoginBean::getUserData)
                .getBean(LoginUserBean::getNickname).isPresent();
        Log.e("TAG", String.valueOf(present));

        // 3. 扩展的 ifPresent 方法
        OptionalBean.ofNullable(loginBean)
                .getBean(LoginBean::getUserData)
                .getBean(LoginUserBean::getNickname)
                .ifPresent(new Consumer<String>() {
                    @Override
                    public void accept(String nickName) {
                        Log.e("TAG", nickName);
                    }
                });

        // 4. 扩展的 orElse
        String nickName2 = OptionalBean.ofNullable(loginBean)
                .getBean(LoginBean::getUserData)
                .getBean(LoginUserBean::getNickname).orElse("昵称");
        System.out.println(nickName2);

        // 5. 扩展的 orElseThrow
        try {
            String nickName3 = OptionalBean.ofNullable(loginBean)
                    .getBean(LoginBean::getUserData)
                    .getBean(LoginUserBean::getNickname).orElseThrow(new Supplier<Throwable>() {
                        @Override
                        public Throwable get() {
                            return new RuntimeException("空指针");
                        }
                    });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}