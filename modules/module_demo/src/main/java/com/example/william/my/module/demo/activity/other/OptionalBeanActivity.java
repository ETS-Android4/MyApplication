package com.example.william.my.module.demo.activity.other;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.william.my.library.utils.OptionalBean;
import com.example.william.my.module.activity.BaseResponseActivity;

/**
 * @see OptionalBean
 */
public class OptionalBeanActivity extends BaseResponseActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initView() {
        super.initView();

//        LoginUserBean loginUserBean = new LoginUserBean();
//        loginUserBean.setNickname("nick name");
//        LoginBean loginBean = new LoginBean();
//        loginBean.setUserData(loginUserBean);
//
//        // 1. 基本调用
//        String nickName = OptionalBean.ofNullable(loginBean)
//                .getBean(LoginBean::getUserData)
//                .getBean(LoginUserBean::getNickname).get();
//
//        // 2. 扩展的 isPresent方法 用法与 Optional 一样
//        boolean present = OptionalBean.ofNullable(loginBean)
//                .getBean(LoginBean::getUserData)
//                .getBean(LoginUserBean::getNickname).isPresent();
//
//        // 3. 扩展的 ifPresent 方法
//        OptionalBean.ofNullable(loginBean)
//                .getBean(LoginBean::getUserData)
//                .getBean(LoginUserBean::getNickname)
//                .ifPresent(new Consumer<String>() {
//                    @Override
//                    public void accept(String nickName) {
//
//                    }
//                });
//
//        // 4. 扩展的 orElse
//        String nickName2 = OptionalBean.ofNullable(loginBean)
//                .getBean(LoginBean::getUserData)
//                .getBean(LoginUserBean::getNickname).orElse("昵称");
//        System.out.println(nickName2);
//
//        // 5. 扩展的 orElseThrow
//        try {
//            String nickName3 = OptionalBean.ofNullable(loginBean)
//                    .getBean(LoginBean::getUserData)
//                    .getBean(LoginUserBean::getNickname).orElseThrow(new Supplier<Throwable>() {
//                        @Override
//                        public Throwable get() {
//                            return new RuntimeException("空指针");
//                        }
//                    });
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
    }
}