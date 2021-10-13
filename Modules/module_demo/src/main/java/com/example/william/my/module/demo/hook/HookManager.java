package com.example.william.my.module.demo.hook;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookManager {

    /**
     * 通过java的反射机制进行hook
     *
     * @param view
     */
    @SuppressLint("PrivateApi,DiscouragedPrivateApi")
    public static void hookOnClickListener(Context context, View view) {
        try {
            // 1. 得到 View 的 ListenerInfo 对象
            Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            //修改getListenerInfo为可访问(View中的getListenerInfo不是public)
            getListenerInfo.setAccessible(true);
            Object listenerInfo = getListenerInfo.invoke(view);

            // 2. 得到 原始的 OnClickListener 对象
            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field field = listenerInfoClz.getDeclaredField("mOnClickListener");

            final View.OnClickListener clickListener = (View.OnClickListener) field.get(listenerInfo);
            // 3. 创建我们自己的点击事件代理类
            // 方式1：自己创建代理类
            //View.OnClickListener proxyOnClickListener = new OnClickListenerProxy(clickListener);

            // 方式2：由于View.OnClickListener是一个接口，所以可以直接用动态代理模式
            // Proxy.newProxyInstance的3个参数依次分别是：
            // 本地的类加载器;
            // 代理类的对象所继承的接口（用Class数组表示，支持多个接口）
            // 代理类的实际逻辑，封装在new出来的InvocationHandler内
            Object proxyOnClickListener = Proxy.newProxyInstance(context.getClass().getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Log.e("TAG", "点击事件被hook到了");//加入自己的逻辑
                    return method.invoke(clickListener, args);//执行被代理的对象的逻辑
                }
            });

            // 4. 用我们自己的点击事件代理类，设置到"持有者"中
            field.set(listenerInfo, proxyOnClickListener);
        } catch (Exception e) {
            Log.e("TAG", "hook clickListener failed!", e);
        }
    }
}
