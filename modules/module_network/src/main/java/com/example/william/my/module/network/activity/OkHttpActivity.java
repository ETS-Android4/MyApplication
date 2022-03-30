package com.example.william.my.module.network.activity;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * https://github.com/square/okhttp
 */
@Route(path = ARouterPath.NetWork.NetWork_OkHttp)
public class OkHttpActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        login();
    }

    private void login() {
        // （1）创建 OkHttpClient 对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        // 创建请求体
        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody formBody = formBuilder
                .add("username", "17778060027")
                .add("password", "ww123456")
                .build();
        // 创建表单
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody multipartBody = multipartBuilder
                .addFormDataPart("username", "17778060027")
                .addFormDataPart("password", "ww123456")
                .build();
        // （2）创建 Request 对象
        Request request = new Request.Builder()
                .url(Urls.Url_Login)
                // 请求体
                .post(formBody)
                // 表单
                //.post(multipartBody)
                .build();
        // （3）创建 Call 对象
        Call call = mOkHttpClient.newCall(request);
        // （4）发送请求并获取服务器返回的数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                String netError = "Error: " + e.getMessage();
                showResponse(netError);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                ResponseBody body = response.body();
                if (body != null) {
                    String netSuccess = "Success: " + body.string();
                    showResponse(netSuccess);
                }
            }
        });
    }

//    private void download() {
//        //创建Client对象
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new RetrofitInterceptorProgress(new RetrofitResponseListener() {
//                    @Override
//                    public void onProgress(String url, long bytesRead, long contentLength) {
//                        int progress = (int) (bytesRead * 1f / contentLength * 100);
//                        showResponse("下载进度：" + progress + "%");
//                    }
//                }))
//                .build();
//        //创建请求
//        Request request = new Request.Builder()
//                .url(Urls.download)
//                .get()
//                .build();
//        Call call = okHttpClient.newCall(request);
//        //加入调度
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure( Call call,  final IOException e) {
//                String netError = "Error: " + e.getMessage();
//                showResponse(netError);
//            }
//
//            @Override
//            public void onResponse( Call call,  final Response response) throws IOException {
//                ResponseBody body = response.body();
//                if (response.isSuccessful() && body != null) {
//                    FileIOUtilsService fileIOUtils = (FileIOUtilsService) ARouter.getInstance().build(ARouterPath.Service.FileIOUtilsService).navigation();
//                    File file = new File(getExternalCacheDir() + File.separator + "ok_http_download.apk");
//                    boolean successful = fileIOUtils.writeFileFromIS(file, body.byteStream());
//                }
//            }
//        });
//    }
//
//    private void upload() {
//        File file = new File(getExternalCacheDir() + File.separator + "ok_http__update.txt");
//
//        FileIOUtilsService fileIOUtils = (FileIOUtilsService) ARouter.getInstance().build(ARouterPath.Service.FileIOUtilsService).navigation();
//        boolean successful = fileIOUtils.writeFileFromString(file, "update");
//
//        //创建表单
//        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody multipartBody = multipartBuilder
//                .addFormDataPart("file", file.getName(), RequestBody.Companion.create(file, MediaType.parse("multipart/form-data")))
//                .build();
//
//        //监听上传进度
//        RequestBody requestProgressBody = new RequestProgressBody(multipartBody, new RequestProgressListener() {
//            @Override
//            public void onProgress(long bytesWritten, long contentLength) {
//                int progress = (int) (bytesWritten * 1f / contentLength * 100);
//                showResponse("上传进度：" + progress + "%");
//            }
//        });
//
//        /*
//         *  OkHttpUtils -> buildRequestProgressBody
//         */
//        //requestProgressBody = OkHttpUtils.buildRequestProgressBody("file", file, new RequestProgressListener() {
//        //    @Override
//        //    public void onProgress(long bytesWritten, long contentLength) {
//        //        int progress = (int) (bytesWritten * 1f / contentLength * 100);
//        //        showResponse("上传进度：" + progress + "%");
//        //    }
//        //});
//
//        //创建请求
//        Request request = new Request.Builder()
//                .url(Urls.URL_UPLOAD)
//                //.post(multipartBody)
//                .post(requestProgressBody)
//                .build();
//        Call call = mOkHttpClient.newCall(request);
//
//        //加入调度
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure( Call call,  final IOException e) {
//                String netError = "Error: " + e.getMessage();
//                showResponse(netError);
//            }
//
//            @Override
//            public void onResponse( Call call,  final Response response) throws IOException {
//                ResponseBody body = response.body();
//                if (body != null) {
//                    String netSuccess = "Success: " + body.string();
//                    showResponse(netSuccess);
//                }
//            }
//        });
//    }
}
