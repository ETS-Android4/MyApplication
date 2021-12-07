package com.example.william.my.module.router;

/**
 * ARouter
 * https://github.com/alibaba/ARouter
 * 一般以模块名称作为一级目录，Activity名称作为二级目录。当分不同包时，前两个要不一样。
 */
public class ARouterPath {

    public static final String Module = "/module/main";

    public static final String Module_WeChat = "/module_wx/main";

    public static class Service {
        public static final String FileIOUtilsService = "/service/file_io";
        public static final String ImageUtilsService = "/service/image";
        public static final String ResourceUtilsService = "/service/resource";
    }

    public static class Fragment {
        public static final String FragmentPrimary = "/base/fragment/primary";
        public static final String FragmentPrimaryDark = "/base/fragment/primary_dark";
        public static final String FragmentBasicRecycler = "/base/fragment/basic_recycler";
    }

    public static class Util {
        public static final String Util = "/module_util/util";
        public static final String Util_AdaptScreen = "/module_util/adapt";
        public static final String Util_BusUtils = "/module_util/bus";
        public static final String Util_Cache = "/module_util/cache";
        public static final String Util_Crash = "/module_util/crash";
        public static final String Util_Encrypt = "/module_util/encrypt";
        public static final String Util_ImageUtils = "/module_util/image";
        public static final String Util_Language = "/module_util/language";
        public static final String Util_Permission = "/module_util/permission";
    }

    public static class OpenSource {
        public static final String OpenSource = "/module_open/open";
        //
        public static final String OpenSource_MMKV = "/module_open/mmkv";
        public static final String OpenSource_GreenDao = "/module_open/green_dao";
        public static final String OpenSource_EventBus = "/module_open/event_bus";
        public static final String OpenSource_LiveEventBus = "/module_open/live_event_bus";
        public static final String OpenSource_SmartEventBus = "/module_open/smart_event_bus";
        //
        public static final String OpenSource_Background = "/module_open/background";
        public static final String OpenSource_BadgeView = "/module_open/badge_view";
        public static final String OpenSource_Banner = "/module_open/banner";
        public static final String OpenSource_CityPicker = "/module_open/city_picker";
        public static final String OpenSource_Countdown = "/module_open/countdown";
        public static final String OpenSource_FloatWindow = "/module_open/float_window";
        public static final String OpenSource_FlycoTabLayout = "/module_open/flyco_tab_layout";
        public static final String OpenSource_GSYPlayer = "/module_open/gsy_player";
        public static final String OpenSource_ImageSelector = "/module_open/image_picker";
        public static final String OpenSource_ImmersionBar = "/module_open/immersion_bar";
        public static final String OpenSource_LoadSir = "/module_open/load_sir";
        public static final String OpenSource_Lottie = "/module_open/lottie";
        public static final String OpenSource_PhotoView = "/module_open/photo_view";
        public static final String OpenSource_PickerView = "/module_open/picker_view";
        public static final String OpenSource_PopWindow = "/module_open/pop_window";
        public static final String OpenSource_SmartRefresh = "/module_open/smart_refresh";
        public static final String OpenSource_SvagPlayer = "/module_open/svga_player";
        public static final String OpenSource_SwipeLayout = "/module_open/swipe_layout";
        public static final String OpenSource_SwipeToLoad = "/module_open/swipe_to_load";
        public static final String OpenSource_Tangram = "/module_open/tangram";
        public static final String OpenSource_X5 = "/module_open/x5";
    }


    public static class NetWork {
        public static final String NetWork = "/module_net/net";
        public static final String NetWork_Glide = "/module_net/glide";
        public static final String NetWork_HttpURL = "/module_net/http_url";
        public static final String NetWork_Volley = "/module_net/volley";
        public static final String NetWork_VolleyUtils = "/module_net/volley_utils";
        public static final String NetWork_OkHttp = "/module_net/ok_http";
        public static final String NetWork_Retrofit = "/module_net/retrofit";
        public static final String NetWork_RetrofitRxJava = "/module_net/retrofit_rx_java";
        public static final String NetWork_RetrofitRxJavaUtils = "/module_net/retrofit_rx_java_utils";
        public static final String NetWork_RxRetrofit = "/module_net/rx_retrofit";
        public static final String NetWork_Download = "/module_net/rx_download";
        public static final String NetWork_WebSocket = "/module_net/web_socket";
        public static final String NetWork_NanoHttpD = "/module_net/nano_http_d";
        public static final String NetWork_WebServer = "/module_net/web_server";
        public static final String NetWork_Netty = "/module_net/netty";
    }

    public static class Sample {
        public static final String Sample = "/module_sample/sample";
        public static final String Sample_Bind = "/module_sample/bing";
        public static final String Sample_Navigation = "/module_sample/navigation";
        public static final String Sample_LiveData = "/module_sample/live_data";
        public static final String Sample_Room = "/module_sample/room";
        public static final String Sample_WorkManager = "/module_sample/work_manager";
        //
        public static final String Sample_Coil = "/module_sample/coil";
        public static final String Sample_DataStore = "/module_sample/dataStore";
        public static final String Sample_Suspend = "/module_sample/suspend";
        public static final String Sample_FLow = "/module_sample/flow";
        public static final String Sample_Paging = "/module_sample/paging";
        public static final String Sample_Result = "/module_sample/result";
        //
        public static final String Sample_MVP = "/module_sample/mvp";
        public static final String Sample_MVVM = "/module_sample/mvvm";
        public static final String Sample_BindingAdapter = "/module_sample/data_bing";
    }

    public static class Lib {
        public static final String Lib = "/module_libraries/libraries";
        //
        public static final String Lib_KeyValue = "/module_lib/key_value";
        public static final String Lib_ImageLoader = "/module_lib/image_loader";
        //
        public static final String Lib_Banner = "/module_lib/banner";
        public static final String Lib_InfiniteImage = "/module_lib/infinite_image";
        public static final String Lib_NinePatch = "/module_lib/nine_patch";
        public static final String Lib_VerifyCode = "/module_lib/verify_code";
    }

    public static class Demo {
        public static final String Demo = "/module_demo/demo";
        public static final String Demo_Animator = "/module_demo/animator";
        public static final String Demo_AsyncTask = "/module_demo/async";
        public static final String Demo_AudioPlayer = "/module_demo/audio";
        public static final String Demo_Broadcast = "/module_demo/broadcast";
        public static final String Demo_FloatWindow = "/module_demo/float_window";
        public static final String Demo_HandlerThread = "/module_demo/handler_thread";
        public static final String Demo_Hook = "/module_demo/hook";
        public static final String Demo_JobScheduler = "/module_demo/job_scheduler";
        public static final String Demo_Messenger = "/module_demo/messenger";
        public static final String Demo_NetworkStatus = "/module_demo/net_status";
        public static final String Demo_Permission = "/module_demo/permission";
        public static final String Demo_PicCrop = "/module_demo/pic_crop";
        public static final String Demo_Service = "/module_demo/service";
        public static final String Demo_TouchEvent = "/module_demo/touch_event";
        public static final String Demo_Transition = "/module_demo/transition";
        public static final String Demo_Transparent = "/module_demo/Transparent";
        public static final String Demo_Turntable = "/module_demo/turntable";
        public static final String Demo_Typeface = "/module_demo/typeface";
        //
        public static final String Demo_AppBar = "/module_demo/app_bar";
        public static final String Demo_Dialog = "/module_demo/dialog";
        public static final String Demo_FlexBox = "/module_demo/flex_box";
        public static final String Demo_Fragment = "/module_demo/fragment";
        public static final String Demo_FragmentGroup = "/module_demo/fragment_group";
        public static final String Demo_FragmentTabHost = "/module_demo/fragment_tab_host";
        public static final String Demo_FragmentViewPager = "/module_demo/fragment_view_pager";
        public static final String Demo_Notification = "/module_demo/notification";
        public static final String Demo_RecyclerView = "/module_demo/recycler_view";
        public static final String Demo_RecyclerViewNested = "/module_demo/recycler_view_nested";
        public static final String Demo_ViewFlipper = "/module_demo/view_flipper";
        public static final String Demo_ViewPager = "/module_demo/view_pager";
        public static final String Demo_ViewPager2 = "/module_demo/view_pager_2";
        public static final String Demo_WebView = "/module_demo/web_view";
        //
        public static final String Demo_BlurView = "/module_demo/blur_view";
        public static final String Demo_IosDialog = "/module_demo/basics_ios_dialog";
        public static final String Demo_MarqueeView = "/module_demo/marquee_view";
        public static final String Demo_Spinner = "/module_demo/spinner";
        public static final String Demo_TitleBar = "/module_demo/title";
    }

    public static class NeRtc {
        public static final String Audio = "/module_ne_rtc/audio";
    }

    public static class Flutter {
        public static final String Flutter = "/module_flutter/flutter";
    }
}
