package com.jennifer.jennifer.ui.touch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jennifer.jennifer.R;

public class TouchEventConflictActivity extends AppCompatActivity {

    private static final int MSG_TIME = 1001;
    private TextView tvTime;
    private Button fab;
    private long time = 0;
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time += 1000;
            tvTime.setText(getDoublePlaceholderMinSecond(time));
            mHandler.sendEmptyMessageDelayed(MSG_TIME, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event_conflict);
        initView();
    }

    private void initView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - 450;

        tvTime = findViewById(R.id.tv_time);
        fab = findViewById(R.id.fab);

        tvTime.setText(getDoublePlaceholderMinSecond(time));
        //如果想让按钮拖动时不复位那么需要给界面中的其他UI设置固定宽高
        fab.setOnTouchListener(fbTouchListener);

        mHandler.sendEmptyMessageDelayed(MSG_TIME, 1000);
    }

    private long startTime = 0;
    private long endTime = 0;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLastX;
    private int mLastY;
    private boolean isTuodong;
    View.OnTouchListener fbTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isTuodong = false;
                    mLastX = (int) event.getRawX();
                    mLastY = (int) event.getRawY();
                    startTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isTuodong = false;
                    int dx = (int) event.getRawX() - mLastX;
                    int dy = (int) event.getRawY() - mLastY;

                    int left = v.getLeft() + dx;
                    int top = v.getTop() + dy;
                    int right = v.getRight() + dx;
                    int bottom = v.getBottom() + dy;
                    if (left < 0) {
                        left = 0;
                        right = left + v.getWidth();
                    }
                    if (right > mScreenWidth) {
                        right = mScreenWidth;
                        left = right - v.getWidth();
                    }
                    if (top < 0) {
                        top = 0;
                        bottom = top + v.getHeight();
                    }
                    if (bottom > mScreenHeight) {
                        bottom = mScreenHeight;
                        top = bottom - v.getHeight();
                    }
                    v.layout(left, top, right, bottom);
                    mLastX = (int) event.getRawX();
                    mLastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
//                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.1 * 1000L) {
                        isTuodong = true;
                    } else {
                        isTuodong = false;
                    }
                    break;
            }
            return isTuodong;
        }
    };

    public String getDoublePlaceholderMinSecond(long lTime) {
        StringBuilder strTime = new StringBuilder();
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        day = lTime / (24 * 60 * 60 * 1000);
        hour = (lTime / (60 * 60 * 1000) - day * 24);
        min = ((lTime / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (lTime / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//		strTime.append(min).append("分").append(sec).append("秒");
        if (min < 10) {
            strTime.append("0");
        }
        strTime.append(min).append("'");
        if (sec < 10) {
            strTime.append("0");
        }
        strTime.append(sec).append("''");
        return strTime.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            if (mHandler.hasMessages(MSG_TIME)) {
                mHandler.removeMessages(MSG_TIME);
            }
            mHandler = null;
        }
    }
}