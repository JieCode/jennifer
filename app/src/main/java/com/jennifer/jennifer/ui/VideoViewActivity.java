package com.jennifer.jennifer.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.util.TimeUtils;

public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private static final int PROGRESS = 1;
    private static final int HIDE_MEDIA_CONTROLLER = 2;
    private static final int DELAY_PROGRESS = 1 * 1000;
    private static final int DELAY_HIDE_MEDIA_CONTROLLER = 5 * 1000;
    private RelativeLayout rlVideo;
    private VideoView videoView;
    private TextView textView;
    private RelativeLayout controller;
    private ImageView ivPlay;
    private SeekBar seekBar;
    private TextView current;
    private TextView total;
    private ImageView fullscreen;
    private GestureDetector detector;

    private boolean isFull = false;//是否为全屏播放
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    //1、得到当前的视频播放进度
                    int currentPosition = videoView.getCurrentPosition();
                    //2、SeekBar设置当前进度
                    seekBar.setProgress(currentPosition);
                    //3、更新文本播放时间进度
                    current.setText(TimeUtils.getTimeVideo(currentPosition));
                    //4、每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, DELAY_PROGRESS);
                    break;
                case HIDE_MEDIA_CONTROLLER:
                    showOrHideMediaController(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        initView();
        initData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig == null) return;
        Window window = getWindow();
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            portraitOrLandscape(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            portraitOrLandscape(false);
        }
    }

    private void initView() {
        rlVideo = findViewById(R.id.rl_video);
        videoView = findViewById(R.id.video_view);
        textView = findViewById(R.id.tv_content);
        controller = findViewById(R.id.controller);
        ivPlay = findViewById(R.id.iv_play);
        seekBar = findViewById(R.id.seek_bar);
        current = findViewById(R.id.current);
        total = findViewById(R.id.total);
        fullscreen = findViewById(R.id.fullscreen);
        detector = new GestureDetector(this, new MySimpleOnGestureListener());

        ivPlay.setOnClickListener(this);
        fullscreen.setOnClickListener(this);

        //设置视频准备好的监听
        videoView.setOnPreparedListener(this);
        //设置视频播放出错的监听
        videoView.setOnErrorListener(this);
        //设置视频播放完成的监听
        videoView.setOnCompletionListener(this);

        seekBar.setOnSeekBarChangeListener(this);
    }

    private void initData() {
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.video_butterfly;
        videoView.setVideoURI(Uri.parse(uri));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                startOrPause();
                break;
            case R.id.fullscreen:
                changeScreen();
                break;
        }
        handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, DELAY_HIDE_MEDIA_CONTROLLER);
    }

    private void startOrPause() {
        if (videoView.isPlaying()) {
            videoView.pause();
            ivPlay.setImageResource(R.mipmap.icon_play);
            handler.removeMessages(PROGRESS);
        } else {
            videoView.start();
            ivPlay.setImageResource(R.mipmap.livetv_icon_pauseplay);
            handler.sendEmptyMessageDelayed(PROGRESS, DELAY_PROGRESS);
        }
    }

    private void changeScreen() {
        if (this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else if (this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void portraitOrLandscape(boolean portrait) {
        fullscreen.setImageResource(portrait ? R.mipmap.livetv_icon_deploy : R.mipmap.livetv_icon_retract_white);
        if (portrait)
            videoView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        else
            videoView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //视频准备好了，开始播放
        mp.start();
        //视频总时长
        int totalTime = videoView.getDuration();
        // 关联SeekBar的总长度
        seekBar.setMax(totalTime);
        //设置视频总时长
        total.setText(TimeUtils.getTimeVideo(totalTime));
        //发消息
        handler.sendEmptyMessage(PROGRESS);
        //默认隐藏控制面板
        showOrHideMediaController(false);
    }

    private void showOrHideMediaController(boolean show) {
        if (show)
            controller.setVisibility(View.VISIBLE);
        else
            controller.setVisibility(View.GONE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        ivPlay.setImageResource(R.mipmap.icon_play);
        videoView.seekTo(0);
        seekBar.setProgress(0);
        current.setText(TimeUtils.getTimeVideo(videoView.getCurrentPosition()));
        handler.removeMessages(PROGRESS);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "视频加载失败，请重试！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 当手指滑动时会调用该方法
     *
     * @param seekBar
     * @param progress
     * @param fromUser 用户引起true，否则false
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            videoView.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeMessages(HIDE_MEDIA_CONTROLLER);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, DELAY_HIDE_MEDIA_CONTROLLER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(PROGRESS);
            handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            handler = null;
        }
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
    }

    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (controller.getVisibility() == View.VISIBLE) {
                showOrHideMediaController(false);
                handler.removeMessages(HIDE_MEDIA_CONTROLLER);
            } else {
                showOrHideMediaController(true);
                handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, DELAY_HIDE_MEDIA_CONTROLLER);
            }
            return super.onSingleTapConfirmed(e);
        }
    }
}
