# Retrofit

> https://github.com/square/retrofit

* Retrofit是网络调度层，做具体业务请求、线程切换、数据转换

## Retrofit 简单使用

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

## Retrofit 工作原理

通过java接口以及注解来描述网络请求，并用动态代理的方式生成网络请求的request，然后通过client调用相应的网络框架（默认OkHttp）去发起网络请求，并将返回的response通过ConverterFactory转换成相应的数据model，最后通过CallAdapter转换成其他数据方式

## Retrofit 工作流程

1. 通过解析 网络请求接口的注解 配置 网络请求参数
2. 通过 动态代理 生成 网络请求对象
3. 通过 网络请求适配器 将 网络请求对象 进行平台适配
4. 通过 网络请求执行器 发送网络请求
5. 通过 数据转换器 解析服务器返回的数据
6. 通过 回调执行器 切换线程（子线程 ->>主线程）
7. 用户在主线程处理返回结果
