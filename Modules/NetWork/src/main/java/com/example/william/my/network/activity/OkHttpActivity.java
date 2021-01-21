package com.example.william.my.network.activity;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.core.network.retrofit.body.CountingRequestBody;
import com.example.william.my.core.network.retrofit.interceptor.RetrofitInterceptorProgress;
import com.example.william.my.core.network.retrofit.listener.RetrofitRequestListener;
import com.example.william.my.core.network.retrofit.listener.RetrofitResponseListener;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.FileIOUtilsService;
import com.example.william.my.module.utils.UriUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * https://github.com/square/okhttp
 */
@Route(path = ARouterPath.NetWork.NetWork_OkHttp)
public class OkHttpActivity extends ResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        //login();
        download();
    }

    private void login() {
        //创建Client对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建请求体
        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody formBody = formBuilder
                .add("username", "17778060027")
                .add("password", "ww123456")
                .build();
        //创建表单
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody multipartBody = multipartBuilder
                .addFormDataPart("username", "17778060027")
                .addFormDataPart("password", "ww123456")
                .build();
        //创建请求
        Request request = new Request.Builder()
                .url(Urls.login)
                .post(formBody)//请求体
                //.post(multipartBody)//表单
                .build();
        Call call = okHttpClient.newCall(request);
        //加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                String net_error = "Error: " + e.getMessage();
                showResponse(net_error);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (response.body() != null) {
                    String net_success = "Success: " + response.body().string();
                    showResponse(net_success);
                }
            }
        });
    }

    private void download() {
        //创建Client对象
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new RetrofitInterceptorProgress(new RetrofitResponseListener() {
                    @Override
                    public void onProgress(String url, long bytesRead, long contentLength) {
                        int progress = (int) (bytesRead * 1f / contentLength * 100);
                        showResponse("下载进度：" + progress + "%");
                    }
                }))
                .build();
        //创建请求
        Request request = new Request.Builder()
                .url(Urls.download)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        //加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                String net_error = "Error: " + e.getMessage();
                showResponse(net_error);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    //FileIOUtilsService fileIOUtils = (FileIOUtilsService) ARouter.getInstance().build(ARouterPath.Service.FileIOUtilsService).navigation();
                    //File file = new File(getExternalCacheDir() + File.separator + "ok_http_download.apk");
                    //boolean successful = fileIOUtils.writeFileFromIS(file, response.body().byteStream());
                    Uri uri = UriUtils.save(response.body().byteStream(), "uri.apk");
                    Log.e("TAG", uri.toString());
                }
            }
        });
    }

    private void upload() {
        File file = new File(getExternalCacheDir() + File.separator + "ok_http__update.txt");

        FileIOUtilsService fileIOUtils = (FileIOUtilsService) ARouter.getInstance().build(ARouterPath.Service.FileIOUtilsService).navigation();
        boolean successful = fileIOUtils.writeFileFromString(file, "update");

        //创建Client对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建表单
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody multipartBody = multipartBuilder
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        //监听上传进度
        RequestBody countingRequestBody = new CountingRequestBody(multipartBody, new RetrofitRequestListener() {
            @Override
            public void onProgress(long bytesWritten, long contentLength) {
                int progress = (int) (bytesWritten * 1f / contentLength * 100);
                showResponse("上传进度：" + progress + "%");
            }
        });
        //创建请求
        Request request = new Request.Builder()
                .url(Urls.upload)
                //.post(multipartBody)
                .post(countingRequestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        //加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                String net_error = "Error: " + e.getMessage();
                showResponse(net_error);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (response.body() != null) {
                    String net_success = "Success: " + response.body().string();
                    showResponse(net_success);
                }
            }
        });
    }
}
