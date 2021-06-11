package com.example.william.my.module.network.activity;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.core.okhttp.body.RequestProgressBody;
import com.example.william.my.core.okhttp.listener.RequestProgressListener;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.api.NetworkService;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.FileIOUtilsService;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * https://github.com/square/retrofit
 */
@Route(path = ARouterPath.NetWork.NetWork_Retrofit)
public class RetrofitActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        login();
    }

    private void login() {
        Retrofit retrofit = new Retrofit.Builder()
                // baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
                .baseUrl(Urls.baseUrl)
                .build();
        NetworkService service = retrofit.create(NetworkService.class);

        Call<ResponseBody> call = service.call("17778060027", "ww123456");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String netSuccess = "Success: " + (response.body().string());
                        showResponse(netSuccess);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull final Throwable t) {
                String netError = "Error: " + t.getMessage();
                showResponse(netError);
            }
        });
    }

    private void download() {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new RetrofitInterceptorProgress(new RetrofitResponseListener() {
//                    @Override
//                    public void onProgress(String url, long bytesRead, long contentLength) {
//                        int progress = (int) (bytesRead * 1f / contentLength * 100);
//                        showResponse("下载进度：" + progress + "%");
//                    }
//                }))
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                //baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
//                .baseUrl(Urls.baseUrl)
//                .client(okHttpClient)
//                .build();
//        NetworkService service = retrofit.create(NetworkService.class);
//
//        Call<ResponseBody> call = service.downloadFile(Urls.download);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    FileIOUtilsService fileIOUtils = (FileIOUtilsService) ARouter.getInstance().build(ARouterPath.Service.FileIOUtilsService).navigation();
//                    File file = new File(getExternalCacheDir() + File.separator + "retrofit_download.apk");
//                    boolean successful = fileIOUtils.writeFileFromIS(file, response.body().byteStream());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull final Throwable t) {
//                String netError = "Error: " + t.getMessage();
//                showResponse(netError);
//            }
//        });
    }


    private void update() {
        File file = new File(getExternalCacheDir() + File.separator + "retrofit_update.txt");

        FileIOUtilsService fileIOUtils = (FileIOUtilsService) ARouter.getInstance().build(ARouterPath.Service.FileIOUtilsService).navigation();
        boolean successful = fileIOUtils.writeFileFromString(file, "update");

        Retrofit retrofit = new Retrofit.Builder()
                // baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
                .baseUrl(Urls.baseUrl)
                .build();
        NetworkService service = retrofit.create(NetworkService.class);

        //创建表单
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody multipartBody = multipartBuilder
                .addFormDataPart("file", file.getName(), RequestBody.Companion.create(file, MediaType.parse("multipart/form-data")))
                .build();
        //监听上传进度
        RequestBody requestBody = new RequestProgressBody(multipartBody, new RequestProgressListener() {
            @Override
            public void onProgress(long bytesWritten, long contentLength) {
                int progress = (int) (bytesWritten * 1f / contentLength * 100);
                showResponse("上传进度：" + progress + "%");
            }
        });
        //创建Part
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        // RetrofitUtils -> buildMultipart()
        //MultipartBody filePart = RetrofitUtils.buildMultipart("file", mFile, new RetrofitRequestListener() {
        //    @Override
        //    public void onProgress(long bytesWritten, long contentLength) {
        //        showResponse("上传进度：" + (bytesWritten * 1f / contentLength * 100));
        //    }
        //});

        Call<ResponseBody> call = service.uploadFile(filePart);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String netSuccess = "Success: " + new Gson().toJson(response.body());
                    showResponse(netSuccess);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull final Throwable t) {
                String netError = "Error: " + t.getMessage();
                showResponse(netError);
            }
        });
    }
}
