package com.example.william.my.module.service;

import com.example.william.my.core.network.retrofit.response.RetrofitResponse;
import com.example.william.my.module.base.Urls;
import com.example.william.my.module.bean.BannerBean;
import com.example.william.my.module.bean.BannersBean;
import com.example.william.my.module.bean.LoginData;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/*
 * @PATH 动态URL
 * @Query 查询参数，拼接在URL后
 * @Field 表单键值对，需要添加@FormUrlEncoded
 * @Part  文件表单键值对，需要添加@Multipart，
 * @Body 请求体
 * @Streaming 字节流形式返回
 * @Url 请求地址
 * @Header("Range") 断点续传
 */
public interface NetworkService {

    @POST(Urls.login)
    Call<ResponseBody> call(@Query("username") String username, @Query("password") String password);

    @POST(Urls.login)
    Observable<RetrofitResponse<LoginData>> login(@Query("username") String username, @Query("password") String password);

    /**
     * GsonConverterFactory
     */
    @GET(Urls.banner)
    Observable<BannersBean> getBanners();

    /**
     * RetrofitConverterFactory
     */
    @GET(Urls.banner)
    Observable<RetrofitResponse<List<BannerBean>>> getBannersResponse();

    @Multipart
    @POST(Urls.upload)
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part part);

    @Multipart
    @POST(Urls.upload)
    Call<ResponseBody> uploadFiles(@PartMap Map<String, RequestBody> map);

    @Streaming
    @GET()
    Call<ResponseBody> downloadFile(@Url String url);
}
