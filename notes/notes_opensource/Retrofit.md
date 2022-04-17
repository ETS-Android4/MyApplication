# Retrofit

> https://github.com/square/retrofit

* Retrofit是网络调度层，做具体业务请求、线程切换、数据转换

## Retrofit 简单使用

### 同步请求

（1）定义API接口

```
public interface NetworkService {
  @POST("app/user/main.html")
  Call<ResponseBody> getInfo(@Query("userId") String id);
} 
```

（2）创建 Retrofit 对象

```
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Urls.Url_Base)// baseUlr必须以 /（斜线）结束，不然会抛出一个IllegalArgumentException
        .build();
```

（3）创建网络请求接口的实例，并调用接口中的方法获取 Call 对象：

```
NetworkService service = retrofit.create(NetworkService.class);
Call<ResponseBody> call = service.getInfo("");
```

（4）进行网络请求

```
Response<ResponseBody> response = call.execute();
```


### 异步请求

（4）进行网络请求

```
call.enqueue(new Callback<ResponseBody>() {
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull final Throwable t) {
    }
});
```