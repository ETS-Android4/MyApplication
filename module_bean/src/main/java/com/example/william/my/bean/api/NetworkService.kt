package com.example.william.my.bean.api

import com.example.william.my.bean.base.Urls
import com.example.william.my.bean.data.ArticleDataBean
import com.example.william.my.bean.data.LoginBean
import com.example.william.my.core.retrofit.response.RetrofitResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
interface NetworkService {

    @POST(Urls.Url_Login)
    fun getInfo(@Query("username") username: String, @Query("password") password: String): Call<ResponseBody>

    @POST(Urls.Url_Login)
    suspend fun login(@Query("username") username: String, @Query("password") password: String): LoginBean

    //@Headers(Header.RETROFIT_CACHE_ALIVE_SECOND + ":" + 10)
    @GET(Urls.Url_Article)
    fun getArticleResponse(@Path("page") page: Int): Single<RetrofitResponse<ArticleDataBean>>

    // 提供挂起功能的网络请求接口
    // Interface that provides a way to make network requests with suspend functions
    @GET(Urls.Url_Article)
    suspend fun getArticleSuspend(@Path("page") page: Int): RetrofitResponse<ArticleDataBean>

    //@Multipart
    //@POST(Urls.URL_UPLOAD)
    //Call<ResponseBody> uploadFile(@Part MultipartBody.Part part);
    //@Multipart
    //@POST(Urls.upload)
    //Call<ResponseBody> uploadFiles(@PartMap Map<String, RequestBody> map);
    //@Streaming
    //@GET()
    //Call<ResponseBody> downloadFile(@Url String url);
}