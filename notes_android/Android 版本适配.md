# Android 版本适配

## Android 6.0适配

### 动态权限申请

申请权限：

```
if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.CAMERA}, position);
    //Fragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, position);
}
```

## Android 7.0适配

### 应用间共享文件

在manifest中添加provider

```xml
<manifest>
    ...
    <application>
        ...
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
        ...
    </application>
</manifest>
```

配置需要获取文件的文件路径

```xml
<resources>
    <paths>
        <files-path name="name" path="" /><!--Context.getFilesDir()-->
        <cache-path name="name" path="" /><!--Context.getCacheDir()-->
        <external-path name="name" path="" /><!--Environment.getExternalStorageDirectory()-->
        <external-files-path name="name" path="" /><!--Context.getExternalFilesDir()-->
        <external-cache-path name="name" path="" /><!--Context.getExternalCacheDir()-->
        <root-path name="download" path="" />
    </paths>
</resources>
```

获取文件Uri

```
Uri contentUri = FileProvider.getUriForFile(getContext(), "包名.fileProvider", newFile);
```

## Android 8.0适配

### 通知适配

创建通知渠道

```
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel notificationChannel = new NotificationChannel(
            "channelId",
            "channelName",
            NotificationManager.IMPORTANCE_HIGH);
    mNotificationManager.createNotificationChannel(notificationChannel);
}
```

创建Notification

```
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle) //通知标题
        .setContentText(textContent) //通知内容
        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //设置优先级//@RequiresApi(api = Build.VERSION_CODES.N)
notificationManager.notify(notificationId, builder.build());
```

### 透明主题

> 只有全屏不透明的activity才可以设置方向，解决方案：要么去掉对应activity中的 screenOrientation 属性，或者对应设置方向的代码。

```xml
<item name="android:windowIsTranslucent">false</item>
```

### 未知来源

AndroidManifest文件中添加安装未知来源应用的权限

```xml
<!-- Android8.0 允许安装未知来源 -->
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

## Android 9.0适配

### HTTP请求限制

在资源文件新建xml目录，新建文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config xmlns:tools="http://schemas.android.com/tools">
    <base-config
        cleartextTrafficPermitted="true"
        tools:ignore="InsecureBaseConfiguration" />
</network-security-config>
```

清单文件配置

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"/>
```

## Android 10.0适配

### 作用域存储

请求旧版外部存储

```xml
<manifest ... >
  <application android:requestLegacyExternalStorage="true" ...>
    ...
  </application>
</manifest>
```

获取相册中的图片（无法获取图片绝对路径，通过MediaStore获取图片Uri）

```
Cursor cursor = getContentResolver().query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//查询数据库
        null,//返回ID的List
        null,//查询条件
        null,//查询条件参数，替换第三个参数中的?
        null);//排序
if (cursor != null) {
    while (cursor.moveToNext()) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
    }
    cursor.close();
}
```
