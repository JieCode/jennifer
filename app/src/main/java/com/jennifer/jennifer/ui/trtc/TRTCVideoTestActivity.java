package com.jennifer.jennifer.ui.trtc;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.lang.ref.WeakReference;

public class TRTCVideoTestActivity extends BaseActivity {

    private final static String TAG = TRTCVideoTestActivity.class.getSimpleName();
    private TXCloudVideoView txMine, txOther;
    private TRTCCloudDef.TRTCParams trtcParams;
    private TRTCCloud trtcCloud;
    private TRTCCloudListener trtcListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //获取前一个页面得到的进房参数
        int sdkAppId = 1400174671;
        int roomId = 6666;
//        String selfUserId = "Android_trtc_04";
//        String userSig = "eJxlj0FPgzAAhe-8CsIVoy1Qykx22AQXAecYDndrKC1YdcC6srAY-7sTl0jiu35f3sv71HRdN57j9DoviqarFVGnlhv6rW4A4*oPtq1gJFfEluwf5H0rJCd5qbgcIEQIWQCMHcF4rUQpLsasZrI5NyqpCgKckXhg72RY*21yAIDYcTEcK6Ia4GOQ3D0srKSjznxvNoswVOtjek-3-ZbGuySTwqeBFyN143fhR*TOxBy8xaa5oYA9Iddelmn0kmUrvIz9jfTkVsmK99GEBqv1azWdjiaV2PHLNQd7GGKIRvTI5UE09SBYACJo2eAnhvalfQM-al-x";
        String selfUserId = "Android_trtc_03";
        String userSig = "eJxlj11rgzAARd-9FeLzGIkmKoM*2Kyg4hz9mBVfgjVRwrpoY1a0Y-99my1U2H09h3u5X4ZpmtYu2T6WVdV*Sk312HHLfDItYD3cYdcJRktNHcX*QT50QnFa1pqrCUKMsQ3A3BGMSy1qcTMCyVT726iVrihwZmLP3um0dm1CAEAPuR6cK6KZ4MtqTaKQ7FuZtplfjLmIIhw0Lon71RLIQnVDczxvk3X2mh-HJQtEUPspCtC*2OTJabwUz6FyUfrGNlnsh*QS74aUnAA5FAenWSxmk1p88Ns15Pke9CCe0TNXvWjlJNgAYmg74C*W8W38AKA2YKE_";
        trtcParams = new TRTCCloudDef.TRTCParams(sdkAppId, selfUserId, userSig, roomId, "", "");

        //初始化 UI 控件
        initView();

        //创建 TRTC SDK 实例
        trtcListener = new TRTCVideoTestActivity.TRTCCloudListenerImpl(this);
        trtcCloud = TRTCCloud.sharedInstance(this);
        trtcCloud.setListener(trtcListener);

        //开始进入视频通话房间
        enterRoom();
    }

    private void initView() {
        setContentView(R.layout.activity_trtcvideo_test);
        txMine = (TXCloudVideoView) findViewById(R.id.tx_mine);
        txOther = (TXCloudVideoView) findViewById(R.id.tx_other);
    }

    /**
     * 设置视频通话的视频参数：需要 TRTCSettingDialog 提供的分辨率、帧率和流畅模式等参数
     */
    private void setTRTCCloudParam() {

        // 大画面的编码器参数设置
        // 设置视频编码参数，包括分辨率、帧率、码率等等，这些编码参数来自于 TRTCSettingDialog 的设置
        // 注意（1）：不要在码率很低的情况下设置很高的分辨率，会出现较大的马赛克
        // 注意（2）：不要设置超过25FPS以上的帧率，因为电影才使用24FPS，我们一般推荐15FPS，这样能将更多的码率分配给画质
        TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
        encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;
        encParam.videoFps = 15;
        encParam.videoBitrate = 450;
        encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
        trtcCloud.setVideoEncoderParam(encParam);

        TRTCCloudDef.TRTCNetworkQosParam qosParam = new TRTCCloudDef.TRTCNetworkQosParam();
        qosParam.controlMode = TRTCCloudDef.VIDEO_QOS_CONTROL_SERVER;
        qosParam.preference = TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_CLEAR;
        trtcCloud.setNetworkQosParam(qosParam);
        trtcCloud.setPriorRemoteVideoStreamType(TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);

    }

    /**
     * 加入视频房间：需要 TRTCNewViewActivity 提供的  TRTCParams 函数
     */
    private void enterRoom() {
        // 预览前配置默认参数
        setTRTCCloudParam();

        // 开启视频采集预览
        trtcCloud.startLocalPreview(true, txMine);
        trtcCloud.startLocalAudio();

        //进房
        trtcCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);

        Toast.makeText(this, "开始进房", Toast.LENGTH_SHORT).show();
    }


    private void exitRoom() {
        if (trtcCloud != null)
            trtcCloud.exitRoom();
    }

    private void finishActivity() {
        finish();
    }

    /**
     * SDK内部状态回调
     */
    static class TRTCCloudListenerImpl extends TRTCCloudListener {
        private WeakReference<TRTCVideoTestActivity> mContext;

        public TRTCCloudListenerImpl(TRTCVideoTestActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
        }

        /**
         * 加入房间
         */
        @Override
        public void onEnterRoom(long elapsed) {
            TRTCVideoTestActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "加入房间成功", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 离开房间
         */
        @Override
        public void onExitRoom(int reason) {
            TRTCVideoTestActivity activity = mContext.get();
            if (activity != null) {
                activity.finishActivity();
            }
        }

        /**
         * ERROR 大多是不可恢复的错误，需要通过 UI 提示用户
         */
        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            TRTCVideoTestActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    activity.exitRoom();
                }
            }
        }

        /**
         * WARNING 大多是一些可以忽略的事件通知，SDK内部会启动一定的补救机制
         */
        @Override
        public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onWarning");
        }

        /**
         * 有新的用户加入了当前视频房间
         */
        @Override
        public void onUserEnter(String userId) {
            TRTCVideoTestActivity activity = mContext.get();
            if (activity != null) {
                // 创建一个View用来显示新的一路画面
                // 启动远程画面的解码和显示逻辑，FillMode 可以设置是否显示黑边
                activity.trtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL);
                activity.trtcCloud.startRemoteView(userId, activity.txOther);
//                activity.trtcCloud.setRemoteViewRotation(userId, TRTCCloudDef.TRTC_VIDEO_ROTATION_180);
            }
        }

        /**
         * 有用户离开了当前视频房间
         */
        @Override
        public void onUserExit(String userId, int reason) {
            TRTCVideoTestActivity activity = mContext.get();
            if (activity != null) {
                //停止观看画面
                activity.trtcCloud.stopRemoteView(userId);
            }
        }

        /**
         * 有用户屏蔽了画面
         */
        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            Log.d(TAG, "sdk callback onUserVideoAvailable " + available);
            if (available) {
                TRTCVideoTestActivity activity = mContext.get();
                if (activity != null) {
                    // 创建一个View用来显示新的一路画面
                    // 启动远程画面的解码和显示逻辑，FillMode 可以设置是否显示黑边
                    activity.trtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL);
                    activity.trtcCloud.startRemoteView(userId, activity.txOther);
                }
            } else {
                TRTCVideoTestActivity activity = mContext.get();
                if (activity != null) {
                    //停止观看画面
                    activity.trtcCloud.stopRemoteView(userId);
                }
            }
        }

        /**
         * 有用户屏蔽了声音
         */
        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            Log.d(TAG, "sdk callback onUserAudioAvailable " + available);
        }

        @Override
        public void onFirstAudioFrame(String s) {
            super.onFirstAudioFrame(s);
            Log.e(TAG, "sdk callback onFirstAudioFrame " + s);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitRoom();
        if (trtcCloud != null) {
            trtcCloud.setListener(null);
            trtcCloud.destroySharedInstance();
        }
        trtcCloud = null;
    }
}
