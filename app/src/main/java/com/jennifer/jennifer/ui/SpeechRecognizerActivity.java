package com.jennifer.jennifer.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;
import com.jennifer.jennifer.util.JsonParser;

public class SpeechRecognizerActivity extends BaseActivity implements View.OnTouchListener {
    private static final String TAG = SpeechRecognizerActivity.class.getSimpleName();
    private TextView tvSpeech;
    private Button btnSpeech;
    private SpeechRecognizer mIat;
    private InitListener mInitListener;
    private RecognizerListener mRecogListener;
    private Toast mToast;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognizer);
        initData();
    }

    private void initData() {
        setTitleText(R.string.speech);
        tvSpeech = findViewById(R.id.tv_speech);
        btnSpeech = findViewById(R.id.btn_speech);

        btnSpeech.setOnTouchListener(this);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mInitListener = new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS)
                    showTip("初始化失败,错误码：" + code);
            }
        };

        mRecogListener = new RecognizerListener() {
            @Override
            public void onVolumeChanged(int volume, byte[] data) {
                showTip("当前正在说话，音量大小：" + volume);
                Log.e(TAG, "返回音频数据：" + data.length);
            }

            @Override
            public void onResult(final RecognizerResult result, boolean isLast) {
                if (null != result) {
                    Log.e(TAG, "recognizer result：" + result.getResultString());
                    StringBuilder builder = new StringBuilder(tvSpeech.getText().toString());
                    if ("cloud".equalsIgnoreCase(mEngineType)) {
                        builder.append(JsonParser.parseGrammarResult(result.getResultString()));
                    } else {
                        builder.append(JsonParser.parseLocalGrammarResult(result.getResultString()));
                    }

                    // 显示
                    tvSpeech.setText(builder.toString());
                } else {
                    Log.e(TAG, "recognizer result : null");
                }
            }

            @Override
            public void onEndOfSpeech() {
                // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
                showTip("结束说话");
            }

            @Override
            public void onBeginOfSpeech() {
                // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
                showTip("开始说话");
            }

            @Override
            public void onError(SpeechError error) {
                showTip("onError Code：" + error.getErrorCode());
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

    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                audioRecord();
                break;
            case MotionEvent.ACTION_UP:
                mIat.stopListening();
                break;
        }
        return false;
    }

    private void audioRecord() {
        //初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

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
//取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
//设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
//自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
//设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

//开始识别，并设置监听器
        mIat.startListening(mRecogListener);
    }
}
