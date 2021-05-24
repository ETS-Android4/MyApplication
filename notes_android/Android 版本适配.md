# Android 版本适配

## Android 6.0适配

### 动态权限申请

危险权限：
```
<!--android.permission-group.CALENDAR-->
<!-- 允许应用程序读取用户的日历数据。 -->
<uses-permission android:name="android.permission.READ_CALENDAR" />
<!-- 允许应用程序写入用户的日历数据。 -->
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
<!--android.permission-group.CAMERA-->
<!-- 允许应用程序访问相机设备。 -->
<uses-permission android:name="android.permission.CAMERA" />
<!--android.permission-group.CONTACTS-->
<!-- 允许应用程序读取用户联系人数据。 -->
<uses-permission android:name="android.permission.READ_CONTACTS" />
<!-- 允许应用程序写入用户的联系人数据。 -->
<uses-permission android:name="android.permission.WRITE_CONTACTS" />
<!-- 允许访问的帐户服务帐户列表。 -->
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<!--android.permission-group.LOCATION-->
<!-- 允许应用访问的精确位置。 -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<!-- 允许应用程序访问的大致位置。 -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<!--android.permission-group.MICROPHONE-->
<!-- 允许应用程序录制音频。 -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<!--android.permission-group.PHONE-->
<!-- 允许应用程序读取手机识别码 -->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<!-- 允许应用程序拨打电话 -->
<uses-permission android:name="android.permission.CALL_PHONE" />
<!-- 允许应用程序读取用户的通话记录。 -->
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<!-- 允许一个程序写入用户的通话记录。 -->
<uses-permission android:name="android.permission.WRITE_CALL_LOG" />
<!--&lt;!&ndash;允许该应用接听来电。&ndash;&gt;-->
<!--<uses-permission android:name="android.permission.ANSWER_PHONE_CALLS" />-->
<!--&lt;!&ndash;允许应用程序将语音邮件添加到系统中。&ndash;&gt;-->
<!--<uses-permission android:name="android.permission.ADD_VOICEMAIL" />-->
<!--&lt;!&ndash;允许应用程序使用SIP服务。&ndash;&gt;-->
<!--<uses-permission android:name="android.permission.USE_SIP" />-->
<!--android.permission-group.SENSORS-->
<!-- 允使用传感器 -->
<uses-permission android:name="android.permission.BODY_SENSORS" />
<!--android.permission-group.SMS-->
<!-- 允许应用程序发送短信。 -->
<uses-permission android:name="android.permission.SEND_SMS" />
<!-- 允许应用程序接收短信。 -->
<uses-permission android:name="android.permission.RECEIVE_SMS" />
<!-- 允许应用程序读取短信。 -->
<uses-permission android:name="android.permission.READ_SMS" />
<!--&lt;!&ndash;允许应用程序接收WAP推送消息。&ndash;&gt;-->
<!--<uses-permission android:name="android.permission.RECEIVE_WAP_PUSH" />-->
<!--&lt;!&ndash;允许应用程序监控传入的MMS消息。&ndash;&gt;-->
<!--<uses-permission android:name="android.permission.RECEIVE_MMS" />-->
<!--android.permission-group.STORAGE-->
<!-- 允许应用程序从外部存储器读取。 -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<!-- 允许应用程序写入到外部存储器。 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

申请权限：
```
if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.CAMERA}, position);
    //Fragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA}, position);
}
```
## Android 7.0适配

### 应用间共享文件

1. 在mainfest中添加provider

```
<manifest>
    ...
    <application>
        ...
        <provider
            android:name="androidx.core.content.FileProvider"   //provider的类名
            android:authorities="${applicationId}.fileProvider" ////没有特定要求，自定义
            android:exported="false"    //不建议设置未true，只会被当前Application调用
            android:grantUriPermissions="true"> //允许你有给其赋予临时访问权限的权力
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
        ...
    </application>
</manifest>
```

2. 配置需要获取文件的文件路径
```
<resources>
    <paths>
        <files-path name="name" path="" /><!--Context.getFilesDir()-->
        <cache-path name="name" path="" /> <!--Context.getCacheDir()-->
        <external-path name="name" path="" /><!--Environment.getExternalStorageDirectory()-->
        <external-files-path name="name" path="" /><!--Context.getExternalFilesDir()-->
        <external-cache-path name="name" path="" /><!--Context.getExternalCacheDir()-->
        <root-path name="download" path="" />
    </paths>
</resources>
```

3. 获取文件Uri
```
Uri contentUri = FileProvider.getUriForFile(getContext(), "包名.fileProvider", newFile);
```

## Android 8.0适配

### 通知适配

1. 创建通知渠道
```
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel notificationChannel = new NotificationChannel(
            "channelId",
            "channelName",
            NotificationManager.IMPORTANCE_HIGH);
    mNotificationManager.createNotificationChannel(notificationChannel);
}
```
2. 创建Notification

```
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle) //通知标题
        .setContentText(textContent)    //通知内容
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);//设置优先级//@RequiresApi(api = Build.VERSION_CODES.N)
notificationManager.notify(notificationId, builder.build());
```

### 透明主题

只有全屏不透明的activity才可以设置方向，解决方案：1.要么去掉对应activity中的 screenOrientation 属性，2.或者对应设置方向的代码。

```
<item name="android:windowIsTranslucent">false</item>
```

### 未知来源

AndroidManifest文件中添加安装未知来源应用的权限
```
<!-- Android8.0 允许安装未知来源 -->
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

## Android 9.0适配

### HTTP请求限制

1. 在资源文件新建xml目录，新建文件
```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config xmlns:tools="http://schemas.android.com/tools">
    <base-config
        cleartextTrafficPermitted="true"
        tools:ignore="InsecureBaseConfiguration" />
</network-security-config>
```

2. 清单文件配置
```
<application
    android:networkSecurityConfig="@xml/network_security_config"/>
```

## Android 10.0适配

### 作用域存储

请求旧版外部存储
```
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
