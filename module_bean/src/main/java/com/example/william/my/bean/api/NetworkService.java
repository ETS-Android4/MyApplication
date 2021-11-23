package com.example.william.my.bean.api;

import com.example.william.my.bean.base.Urls;
import com.example.william.my.bean.data.ArticleBean;
import com.example.william.my.bean.data.BannerBean;
import com.example.william.my.bean.data.BannerDetailBean;
import com.example.william.my.core.retrofit.response.RetrofitResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @PATH 动态URL
 * @Query 查询参数，拼接在URL后
 * @Field 表单键值对，需要添加@FormUrlEncoded
 * @Part 文件表单键值对，需要添加@Multipart，
 * @Body 请求体
 * @Streaming 字节流形式返回
 * @Url 请求地址
 * @Header("Range") 断点续传
 */
public interface NetworkService {

    /**
     * RetrofitActivity
     *
     * @param username
     * @param password
     * @return
     */
    @POST(Urls.URL_LOGIN)
    Call<ResponseBody> call(@Query("username") String username, @Query("password") String password);

    /**
     * @param page
     * @return
     */
    @GET(Urls.URL_ARTICLE)
    Single<ArticleBean> getArticleList(@Path("page") int page);

    /**
     * RetrofitRxJavaActivity & RetrofitUtilsActivity & Repository
     *
     * @return
     */
    @GET(Urls.URL_BANNER)
    Observable<BannerBean> getBanner();

    /**
     * RetrofitRxJavaActivity & RetrofitUtilsActivity & Repository
     *
     * @return
     */
    @GET(Urls.URL_BANNER)
    Observable<RetrofitResponse<List<BannerDetailBean>>> getBannerList();

    /**
     * 上传文件
     *
     * @param part
     * @return
     */
    @Multipart
    @POST(Urls.URL_UPLOAD)
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part part);

    //@Multipart
    //@POST(Urls.upload)
    //Call<ResponseBody> uploadFiles(@PartMap Map<String, RequestBody> map);

    //@Streaming
    //@GET()
    //Call<ResponseBody> downloadFile(@Url String url);
}
