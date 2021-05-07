# Retrofit
> https://github.com/square/retrofit

* Retrofit是网络调度层，做具体业务请求、线程切换、数据转换

1. 创建 Retrofit 实例

```
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(url)//baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//设置RxJava
    .addConverterFactory(GsonConverterFactory.create())//设置数据解析器
    .build();
```

2. 定义API接口，并获取API实例

```
public interface InfoService {
  @POST("app/user/main.html")
  Call<ResponseBody> requestInfo(@Query("userId") String userId);
}
InfoService service = retrofit.create（InfoService.class）;
```

3. 创建HTTP请求

```
Call<ResponseBody> repos = service.requestInfo("");
```

4. 执行网络请求

```
repos.enqueue(new Callback<ResponseBody>() {
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
    }
    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
    }
});
```