# OkHttp

> https://github.com/square/okhttp

• OkHttp是网络执行层，做底层网络请求

## OkHttp 简单使用

### 同步请求

（1）创建 OkHttpClient 对象

```
OkHttpClient client = new OkHttpClient();
```

（2）创建 Request 对象

```
Request request = new Request.Builder()
        .url(url)
        .build();
```

（3）创建 Call 对象

```
Call call = client.newCall(request);
```

（4）发送请求并获取服务器返回的数据

```
Response response = call.execute();
```

### 异步请求

（4）发送请求并获取服务器返回的数据

```
call.enqueue(new okhttp3.Callback() {
    @Override
    public void onFailure( Call call,  IOException e) {
    }
    @Override
    public void onResponse( Call call,  okhttp3.Response response) throws IOException {
    }
});
```