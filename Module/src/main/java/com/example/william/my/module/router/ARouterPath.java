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
        public static final String ModuleService = "/service/module";
        public static final String FileIOUtilsService = "/service/file_io";
        public static final String ResourceUtilsService = "/service/resource";
    }

    public static class Fragment {
        public static final String FragmentPrimary = "/base/fragment/primary";
        public static final String FragmentPrimaryDark = "/base/fragment/primary_dark";
        public static final String FragmentPrimaryLight = "/base/fragment/primary_light";
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

    public static class NetWork {
        public static final String NetWork = "/module_net/net";
        public static final String NetWork_HttpURL = "/module_net/http_url";
        public static final String NetWork_Volley = "/module_net/volley";
        public static final String NetWork_OkHttp = "/module_net/ok_http";
        public static final String NetWork_Retrofit = "/module_net/retrofit";
        public static final String NetWork_RetrofitRxJava = "/module_net/retrofit_rx_java";
        public static final String NetWork_RxRetrofit = "/module_net/rx_retrofit";
        public static final String NetWork_RetrofitUtils = "/module_net/retrofit_utils";
        public static final String NetWork_WebSocket = "/module_net/web_socket";
        public static final String NetWork_Glide = "/module_net/glide";
        public static final String NetWork_NanoHttpD = "/module_net/nano_http_d";
        public static final String NetWork_WebServer = "/module_net/web_server";
    }

    public static class CustomView {
        public static final String CustomView = "/module_custom/custom";
        public static final String CustomView_BlurView = "/module_custom/blur_view";
        public static final String CustomView_Gallery = "/module_custom/gallery";
        public static final String CustomView_GestureLock = "/module_custom/gesture_lock";
        public static final String CustomView_IosDialog = "/module_custom/basics_ios_dialog";
        public static final String CustomView_MarqueeView = "/module_custom/marquee_view";
        public static final String CustomView_ScrollPage = "/module_custom/scroll_page";
        public static final String CustomView_Spinner = "/module_custom/spinner";
        public static final String CustomView_TitleBar = "/module_custom/title";
    }

    public static class JetPack {
        public static final String JetPack = "/module_jet/jet";
        public static final String JetPack_ViewModel = "/module_jet/view_model";
        public static final String JetPack_Paging = "/module_jet/paging";
        public static final String JetPack_Room = "/module_jet/room";
        public static final String JetPack_Navigation = "/module_jet/navigation";
        public static final String JetPack_WorkManager = "/module_jet/work_manager";
    }

    public static class OpenSource {
        public static final String OpenSource = "/module_open/open";
        public static final String OpenSource_RxJava = "/module_open/rx_java";
        public static final String OpenSource_EventBus = "/module_open/event_bus";
        public static final String OpenSource_LiveEventBus = "/module_open/live_event_bus";
        public static final String OpenSource_SmartEventBus = "/module_open/smart_event_bus";
        public static final String OpenSource_Background = "/module_open/background";
        public static final String OpenSource_BadgeView = "/module_open/badge_view";
        public static final String OpenSource_Banner = "/module_open/banner";
        public static final String OpenSource_CityPicker = "/module_open/city_picker";
        public static final String OpenSource_Countdown = "/module_open/countdown";
        public static final String OpenSource_Crop = "/module_open/crop";
        public static final String OpenSource_FlycoTabLayout = "/module_open/flyco_tab_layout";
        public static final String OpenSource_GSYPlayer = "/module_open/gsy_player";
        public static final String OpenSource_ImageSelector = "/module_open/image_picker";
        public static final String OpenSource_PicSelector = "/module_open/pic_picker";
        public static final String OpenSource_PhotoView = "/module_open/photo_view";
        public static final String OpenSource_PickerView = "/module_open/picker_view";
        public static final String OpenSource_PopWindow = "/module_open/pop_window";
        public static final String OpenSource_SmartRefresh = "/module_open/smart_refresh";
        public static final String OpenSource_SwipeLayout = "/module_open/swipe_layout";
        public static final String OpenSource_SwipeToLoad = "/module_open/swipe_to_load";
        public static final String OpenSource_Tangram = "/module_open/tangram";
    }

    public static class Sample {
        public static final String Sample = "/module_sample/sample";

        public static final String Sample_AppBar = "/module_sample/app_bar";
        public static final String Sample_Dialog = "/module_sample/dialog";
        public static final String Sample_Fragment = "/module_sample/fragment";
        public static final String Sample_FragmentGroup = "/module_sample/fragment_group";
        public static final String Sample_FragmentTabHost = "/module_sample/fragment_tab_host";
        public static final String Sample_Notification = "/module_sample/notification";
        public static final String Sample_RecyclerView = "/module_sample/recycler_view";
        public static final String Sample_ViewPager = "/module_sample/view_pager";
        public static final String Sample_ViewPager2 = "/module_sample/view_pager_2";
        public static final String Sample_WebView = "/module_sample/web_view";


        public static final String Sample_Animator = "/module_sample/animator";
        public static final String Sample_AsyncTask = "/module_sample/async";
        public static final String Sample_Broadcast = "/module_sample/broadcast";
        public static final String Sample_Messenger = "/module_sample/messenger";
        public static final String Sample_NetStatus = "/module_sample/net_status";
        public static final String Sample_Permission = "/module_sample/permission";
        public static final String Sample_PicCrop = "/module_sample/pic_crop";
        public static final String Sample_Service = "/module_sample/service";
        public static final String Sample_Transition = "/module_sample/transition";
        public static final String Sample_Turntable = "/module_sample/turntable";
        public static final String Sample_Typeface = "/module_sample/typeface";
    }

    public static class Kotlin {
        public static final String Kotlin = "/module_k/kotlin";
        public static final String Kotlin_Coroutines = "/module_kotlin/coroutines";
    }
}
