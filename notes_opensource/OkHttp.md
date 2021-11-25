# OkHttp
> https://github.com/square/okhttp

• OkHttp是网络执行层，做底层网络请求

## OkHttp 简单使用

1. 创建 OkhttpClient 对象

```
OkHttpClient client = new OkHttpClient();
```

2. 创建请求

```
Request request = new Request.Builder()
        .url(url)
        .build();
Call call = client.newCall(request);
```

3. 加入调度

```
call.enqueue(new okhttp3.Callback() {
    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
    }
    @Override
    public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
    }
});
```
