package com.jennifer.jennifer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.jennifer.jennifer.R;
import com.jennifer.jennifer.util.JsonParser;

public class SpeechRecognizerLayout extends RelativeLayout implements View.OnTouchListener {
    private static final String TAG = "SpeechRecognizerLayout";
    private static final int STATUS_DEFAULT = 0;
    private static final int STATUS_SELECTED = 1;
    private static final int STATUS_RECORDING = 2;
    private static final int STATUS_CANCEL = 3;
    private static final int STATUS_SEND = 4;
    private Context context;
    private RelativeLayout rlDefault;
    private TextView tvDefault;
    private RelativeLayout rlSpeech;
    private FloatingVolume floatingVolume;
    private RelativeLayout rlCancel;
    private SpeechRecognizer mIat;
    private InitListener mInitListener;
    private RecognizerListener mRecogListener;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private OnSpeechListener listener;
    private int status = STATUS_DEFAULT;
    private static final int DISTANCE_Y_CANCEL = 50;//超过50显示松手取消

    public SpeechRecognizerLayout(Context context) {
        this(context, null);
    }

    public SpeechRecognizerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeechRecognizerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        View rootView = mInflater.inflate(R.layout.layout_speech, null);
        initView(rootView);
        addView(rootView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View rootView) {
        rlDefault = rootView.findViewById(R.id.rl_default);
        tvDefault = rootView.findViewById(R.id.tv_default);
        rlSpeech = rootView.findViewById(R.id.rl_speech);
        floatingVolume = rootView.findViewById(R.id.volume);
        rlCancel = rootView.findViewById(R.id.rl_cancel);

        rlDefault.setOnTouchListener(this);
//        ivDefault.setOnTouchListener(this);

        mInitListener = new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS)
                    Log.e(TAG, "初始化失败,错误码：" + code);
            }
        };

        mRecogListener = new RecognizerListener() {
            @Override
            public void onVolumeChanged(int volume, byte[] data) {
//                Log.e(TAG, "当前正在说话，音量大小：" + volume);
//                Log.e(TAG, "返回音频数据：" + data.length);
                floatingVolume.updateFraction(volume / 30.00);
            }

            @Override
            public void onResult(final RecognizerResult result, boolean isLast) {
                if (null != result) {
                    Log.e(TAG, "recognizer result：" + result.getResultString());
                    // 显示
                    if (status == STATUS_SEND || status == STATUS_RECORDING) {
                        if (listener != null)
                            listener.onSend(JsonParser.parseIatResult(result.getResultString()));
                    } else if (status == STATUS_CANCEL) {
                        if (listener != null)
                            listener.onCancel();
                    }
                    resetSpeech();
                    status = STATUS_DEFAULT;
                } else {
                    Log.e(TAG, "recognizer result : null");
                }
            }

            @Override
            public void onEndOfSpeech() {
                // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
                Log.e(TAG, "结束说话");
                status = STATUS_DEFAULT;
            }

            @Override
            public void onBeginOfSpeech() {
                // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
                Log.e(TAG, "开始说话");
                status = STATUS_RECORDING;
            }

            @Override
            public void onError(SpeechError error) {
                Log.e(TAG, "onError Code：" + error.getErrorCode());
                if (error.getErrorCode() == ErrorCode.MSP_ERROR_NO_DATA)
                    if (listener != null)
                        listener.onSend("没有监听到语音");
                resetSpeech();
                status = STATUS_DEFAULT;
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
                // 若使用本地能力，会话id为null
                //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                //		Log.e(TAG, "session id =" + sid);
                //	}
            }
        };
    }

    private void resetSpeech() {
        tvDefault.setText(R.string.ai_speech_start);
        rlSpeech.setVisibility(GONE);
        rlCancel.setVisibility(GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent:" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.rl_default:
            case R.id.iv_default:
                int x = (int) event.getX();// 获得x轴坐标
                int y = (int) event.getY();// 获得y轴坐标
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        status = STATUS_SELECTED;
                        tvDefault.setText(R.string.ai_speech_send);
                        rlSpeech.setVisibility(VISIBLE);
                        rlCancel.setVisibility(GONE);
                        audioRecord();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (status == STATUS_RECORDING) {
                            // 如果想要取消，根据x,y的坐标看是否需要取消
                            if (wantToCancel(x, y)) {
                                rlSpeech.setVisibility(GONE);
                                rlCancel.setVisibility(VISIBLE);
                            } else {
                                rlSpeech.setVisibility(VISIBLE);
                                rlCancel.setVisibility(GONE);
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        if (status == STATUS_RECORDING) {
                            if (rlCancel.getVisibility() == VISIBLE)
                                status = STATUS_CANCEL;
                            else
                                status = STATUS_SEND;
                        }
                        if (mIat != null && mIat.isListening())
                            mIat.stopListening();
                        resetSpeech();
                        break;
                }
                break;
        }
        return true;
    }

    /**
     * 显示上滑取消
     *
     * @param x
     * @param y
     * @return
     */
    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > rlDefault.getWidth()) { // 超过按钮的宽度
            return true;
        }
        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > rlDefault.getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }

    private void audioRecord() {
        //初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);

        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mIat.setParameter(SpeechConstant.SUBJECT, null);
        //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //此处engineType为“cloud”
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        // 取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        //自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "10000");//设置后并不起作用
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        //开始识别，并设置监听器
        mIat.startListening(mRecogListener);
    }

    public void release() {
        if (mIat != null)
            mIat.destroy();
    }

    public void setListener(OnSpeechListener listener) {
        this.listener = listener;
    }

    public interface OnSpeechListener {
        void onSend(String result);

        void onCancel();
    }
}
