package com.example.william.my.core.network.retrofit.compat.ssl;

import android.annotation.SuppressLint;

import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class HttpsSSLCompat {

    public static void ignoreSSLForOkHttp(OkHttpClient.Builder builder) {
        builder.hostnameVerifier(getIgnoreHostnameVerifier())
                .sslSocketFactory(getIgnoreSSLSocketFactory(), getDefaultTrustManager());
    }

    public static void ignoreSSLForHttpsURLConnection() {
        HttpsURLConnection.setDefaultHostnameVerifier(getIgnoreHostnameVerifier());
        HttpsURLConnection.setDefaultSSLSocketFactory(getIgnoreSSLSocketFactory());
    }

    /**
     * 获取忽略证书的HostnameVerifier
     * 与{@link #getIgnoreSSLSocketFactory()}同时配置使用
     */
    private static HostnameVerifier getIgnoreHostnameVerifier() {
        return new HostnameVerifier() {
            @SuppressLint("BadHostnameVerifier")
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
    }

    /**
     * 获取忽略证书的SSLSocketFactory
     * 与{@link #getIgnoreHostnameVerifier()}同时配置使用
     */
    private static SSLSocketFactory getIgnoreSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager = getDefaultTrustManager();
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static X509TrustManager getDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            return null;
        }
    }
}