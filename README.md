# MyApplication

使用 buildSrc 管理 gradle 依赖，可以有效解决依赖管理的混乱问题。

1. 统一管理项目中的 Gradle
2. 动态管理模块依赖，可以自由选择调试的模块

整体项目使用组件化开发，使用ARouter进行模块内通信。

## 依赖库

### Libraries : lib_network

#### 1. 基于 Retrofit 封装的网络请求框架

1. 支持常用的GET，POST，PUT，请求
2. 统一处理返回错误信息，与返回实体类
3. 绑定生命周期，防止内存泄漏
4. 针对ViewModel进行封装，返回LiveData数据
4. 针对ViewModel进行封装，返回Flow数据

#### 2. 基于 OkHttp + RxJava 封装的 WebSocket 请求框架

### modules_custom_view

项目中用过的自定义控件

1. 高斯模糊
2. ViewPager画廊效果
3. 手势密码
4. 轮播广告
5. ScrollView分页
6. 统一添加ToolBar

### modules_netWork

关于网络方面开发的demo。

1. Glide
2. HttpURLConnection
3. Volley
4. OkHttp
5. Retrofit
6. WebSocket网络请求
7. NanoHttpD的Android端的服务器搭建
8. WebServer的Android端的服务器搭建
9. Netty

### modules_jet_pack

根据Google官方推出的JetPack库写的demo。包括了ViewModel，Room，Navigation，WorkManager。

1. LiveData + ViewModel + Room + Retrofit + RxJava搭建MVVM架构。
使用 Retrofit 把返回的网络数据转化为本地数据存储在 Repository
通过 ViewModel 把 Repository 数据与 View 绑定，使更新 ViewModel 层的数据的时候，View 层会相应的更新 UI 。
2. ViewBinding & DataBinding
3. Navigation导航
4. Paging3 分页
5. Room 数据
6. WorkManager调度任务

### modules_sample

1. Kolint +  Retrofit + Flow + LiveData + ViewModel
2. Kolint +  Retrofit + Flow + LiveData + ViewModel + DataBinding
3. MVP gooogle 官方 Demo
4. MVVM gooogle 官方 Demo

### modules_open_source

记录了一些用过的开源框架的demo

1. 可拖拽小红点： https://github.com/qstumn/BadgeView
2. 万能适配器器： https://github.com/CymChad/BaseRecyclerViewAdapterHelper
3. Banner轮播图控件：https://github.com/youth5201314/banner
5. 城市选择器： https://github.com/zaaach/CityPicker)
6. 倒计时控件： https://github.com/iwgang/CountdownView
7. 悬浮窗：https://github.com/princekin-f/EasyFloat
8. TabLayout：https://github.com/H07000223/FlycoTabLayout
9. GreenDao： https://greenrobot.org/greendao/features/
10. 视频播放：https://github.com/CarGuo/GSYVideoPlayer
11. 知乎图片选择框架： https://github.com/zhihu/Matisse
12. MMKV： https://github.com/Tencent/MMKV
13. 图片查看： https://github.com/chrisbanes/PhotoView
14. PickerView控件： https://github.com/Bigkoo/Android-PickerView
15. PopuWindow： https://github.com/pinguo-zhouwei/CustomPopwindow
16. 智能刷新控件： https://github.com/scwang90/SmartRefreshLayout
17. SVGA动画：https://github.com/svga/SVGAPlayer-Android
18. Android滑动布局： https://github.com/daimajia/AndroidSwipeLayout
19. 刷新加载控件： https://github.com/Aspsine/SwipeToLoadLayout
20. 七巧板布局： https://github.com/alibaba/Tangram-Android
21. X5浏览器：https://x5.tencent.com/
