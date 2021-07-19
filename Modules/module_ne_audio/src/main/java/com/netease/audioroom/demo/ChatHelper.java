package com.netease.audioroom.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.netease.audioroom.demo.BuildConfig;
import com.netease.audioroom.demo.base.ChatLoginManager;
import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.model.NESDKConfig;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.util.Map;

public class ChatHelper {

    private static LoginInfoProvider sProvider;

    /**
     * 播放器初始化，用于 CDN 拉流
     */
    public static void initPlayer(Context context) {
        NESDKConfig config = new NESDKConfig();
        config.dataUploadListener = new NELivePlayer.OnDataUploadListener() {
            @Override
            public boolean onDataUpload(String url, String data) {
                Log.e("Player===>", "stream url is " + url + ", detail data is " + data);
                return true;
            }

            @Override
            public boolean onDocumentUpload(String url, Map<String, String> params, Map<String, String> filepaths) {
                return false;
            }
        };
        NELivePlayer.init(context.getApplicationContext(), config);
    }

    /**
     * IM 初始化
     */
    public static void initIM(Context context, LoginInfoProvider info) {
        sProvider = info;
        NIMClient.init(context, null, getOptions(context));
        if (NIMUtil.isMainProcess(context)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            //ALog.init(context, BuildConfig.DEBUG ? ALog.LEVEL_ALL : ALog.LEVEL_INFO);
        }
    }

    /**
     * 登陆
     */
    public static void loginIM(ChatLoginManager.Callback callback) {
        ChatLoginManager chatLoginManager = ChatLoginManager.getInstance();
        chatLoginManager.tryLogin();
        chatLoginManager.setCallback(callback);
    }

    /**
     * 退出登录
     */
    public static void logoutIM() {
        ChatLoginManager chatLoginManager = ChatLoginManager.getInstance();
        chatLoginManager.logout();
    }

    /**
     * 是否登陆
     */
    public static boolean isLogin() {
        return ChatLoginManager.isLogin();
    }

    private static LoginInfo getLoginInfo() {
        if (sProvider != null) {
            return new LoginInfo(sProvider.getIMAccount(), sProvider.getIMToken());
        }
        return null;
    }

    /**
     * 配置信息
     */
    private static SDKOptions getOptions(Context context) {
        SDKOptions options = new SDKOptions();

        options.appKey = BuildConfig.NIM_APP_KEY;

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        //options.statusBarNotificationConfig = initStatusBarNotificationConfig();

        // 配置保存图片，文件，log等数据的目录
        //options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/nim";

        // 配置是否需要预下载附件缩略图，默认为 true
        //options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        //options.thumbnailSize = getThumbnailSize(context);

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        //options.userInfoProvider = getUserInfo();

        //是否提高SDK进程优先级（默认提高，可以降低SDK核心进程被系统回收的概率）
        //options.improveSDKProcessPriority = true;
        //预加载服务，默认true，不建议设置为false，预加载连接可以优化登陆流程
        //options.preLoadServers = true;
        //是否开启会话已读多端同步
        //options.sessionReadAck = true;
        //开启对动图缩略图的支持，默认为 false，截取第一帧
        //options.animatedImageThumbnailEnabled = true;
        //是否在 SDK 初始化时检查清单文件配置是否完全，默认为 false，建议开发者在调试阶段打开，上线时关掉
        //options.checkManifestConfig = BuildConfig.DEBUG;
        //是否使用随机退避重连策略，默认true，强烈建议打开。如需关闭，请咨询云信技术支持。
        //options.enableBackOffReconnectStrategy = true;

        //options.shouldConsiderRevokedMessageUnreadCount = true;

        //异步初始化
        //options.asyncInitSDK = true;

        return options;
    }

    /**
     * 消息提醒配置
     */
    private static StatusBarNotificationConfig initStatusBarNotificationConfig() {
        // load 应用的状态栏配置
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        //config.notificationEntrance = .class;
        //config.notificationSmallIconId = R.mipmap.ic_launcher;

        // 通知铃声的uri字符串//这里需要更正为正确地铃声地址
        //config.notificationSound = "android.resource://cn.weli.story/raw/push_volunteer";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        return config;
    }

    /**
     * 消息缩略图的尺寸
     */
    private static int getThumbnailSize(Context context) {
        return (int) (165.0 / 320.0 * context.getResources().getDisplayMetrics().widthPixels);
    }

    /**
     * 用户资料提供者, 目前主要用于通知栏显示用户昵称和头像
     */

    private static UserInfoProvider getUserInfo() {
        UserInfo userInfo = new UserInfo() {
            @Override
            public String getAccount() {
                return sProvider != null ? sProvider.getUid() : "";
            }

            @Override
            public String getName() {
                return sProvider != null ? sProvider.getNick() : "";
            }

            @Override
            public String getAvatar() {
                return sProvider != null ? sProvider.getAvatar() : "";
            }
        };
        return new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return userInfo;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {
                return null;
            }

        };
    }

    public interface LoginInfoProvider {

        /**
         * loginInfo
         */
        String getIMAccount();

        String getIMToken();

        /**
         * userInfo
         */
        String getUid();

        String getNick();

        String getAvatar();
    }
}