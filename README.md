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

#### 2. 基于 OkHttp + RxJava 封装的 WebSocket 请求框架

### Libraries : lib_customview

项目中用过的自定义控件

1. 高斯模糊
2. ViewPager画廊效果
3. 手势密码
4. 轮播广告
5. ScrollView分页
6. 统一添加ToolBar

### Modules : NetWork

关于网络方面开发的demo。

1. HttpURLConnection
2. Volley
3. OkHttp
4. Retrofit
5. WebSocket网络请求
6. NanoHttpD的Android端的服务器搭建
7. WebServer的Android端的服务器搭建

### Modules : JetPack

根据Google官方推出的JetPack库写的demo。包括了ViewModel，Room，Navigation，WorkManager。

* LiveData + ViewModel + Room + Retrofit + RxJava搭建MVVM架构。
使用 Retrofit 把返回的网络数据转化为本地数据存储在 Repository
通过 ViewModel 把 Repository 数据与 View 绑定，使更新 ViewModel 层的数据的时候，View 层会相应的更新 UI 。

### Modules : OpenSource

记录了一些用过的开源框架的demo

1. 万能适配器器： https://github.com/CymChad/BaseRecyclerViewAdapterHelper
2. Banner轮播图控件： https://github.com/saiwu-bigkoo/Android-ConvenientBanner
3. 城市选择器： https://github.com/zaaach/CityPicker)
4. 倒计时控件： https://github.com/iwgang/CountdownView
5. 知乎图片选择框架： https://github.com/zhihu/Matisse
6. 图片查看： https://github.com/chrisbanes/PhotoView
7. PickerView控件： https://github.com/Bigkoo/Android-PickerView
8. 智能刷新控件： https://github.com/scwang90/SmartRefreshLayout
9. Android滑动布局： https://github.com/daimajia/AndroidSwipeLayout
10. 刷新加载控件： https://github.com/Aspsine/SwipeToLoadLayout