# APK 编译流程

1. 打包资源文件，生成R.java文件
2. 处理AIDL文件，生成对应的.java文件（当然，有很多工程没有用到AIDL，那这个过程就可以省了
3. 编译Java文件，生成对应的.class文件
4. 把.class文件转化成Dalvik VM支持的.dex文件
5. 打包生成未签名的.apk文件
6. 对未签名.apk文件进行签名
7. 对签名后的.apk文件进行对齐处理（不进行对齐处理是不能发布到Google Market的）