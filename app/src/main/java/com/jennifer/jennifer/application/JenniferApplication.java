package com.jennifer.jennifer.application;

import android.app.Application;

import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveRoomConfig;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

/**
 * @author jingjie
 * @date 2019/1/2
 */
public class JenniferApplication extends Application {
    private int appId = 1258379062;

    @Override
    public void onCreate() {
        super.onCreate();
        if (MsfSdkUtils.isMainProcess(this)) {
            //初始化iLiveSdk
            ILiveSDK.getInstance().initSdk(this, appId);
            //初始化iLiveSDK房间管理模块
            ILiveRoomManager.getInstance().init(new ILiveRoomConfig());
        }
    }
}
