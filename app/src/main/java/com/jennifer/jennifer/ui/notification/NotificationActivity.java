package com.jennifer.jennifer.ui.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.jennifer.jennifer.R;
import com.jennifer.jennifer.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class NotificationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvStart;
    private TextView tvPause;
    private TextView tvStop;
    private TextView tvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
    }

    private void initView() {
        setTitleText(R.string.notification_title);
        tvStart = findViewById(R.id.tv_start);
        tvPause = findViewById(R.id.tv_pause);
        tvStop = findViewById(R.id.tv_stop);
        tvAdd = findViewById(R.id.tv_add);

        tvAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_start:
                break;
            case R.id.tv_pause:
                break;
            case R.id.tv_stop:
                break;
            case R.id.tv_add:
                final int[] progress = {0};
                final ProgressChange progressChange = new NotificationProgress(this);
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        progress[0] = progress[0] + 10;
                        if (progress[0] < 110) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    progressChange.onProgress(progress[0]);
                                }
                            }).start();
                        } else {
                            progressChange.onComplete();
                            timer.cancel();
                        }
                    }
                }, 0, 1000);
                break;
        }
    }

    public class NotificationProgress implements ProgressChange {
        private Activity activity;

        NotificationManager manager;
        NotificationCompat.Builder builder;
        int id = Math.abs(UUID.randomUUID().hashCode());
        private RemoteViews remoteViews;

        public NotificationProgress(Activity activity) {
            this.activity = activity;
            initNotification();
        }

        private void initNotification() {
            this.manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
            remoteViews.setTextViewText(R.id.content_title, "下载id：" + id);
            remoteViews.setProgressBar(R.id.progress, 100, 0, false);
            builder = new NotificationCompat.Builder(activity);
            builder.setContent(remoteViews)
                    .setSmallIcon(R.mipmap.ic_launcher);

            // 下载开始时的通知回调。运行于主线程
            manager.notify(id, builder.build());
        }

        @Override
        public void onComplete() {
            manager.cancel(id);
        }

        @Override
        public void onProgress(int progress) {
            remoteViews.setProgressBar(R.id.progress, 100, progress, false);
            builder.setContent(remoteViews);
            manager.notify(id, builder.build());
        }
    }


    public interface ProgressChange {
        void onComplete();

        void onProgress(int progress);
    }
}
