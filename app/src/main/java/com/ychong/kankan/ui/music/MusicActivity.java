package com.ychong.kankan.ui.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.ychong.baselib.base.BaseActivity;
import com.ychong.kankan.R;

public class MusicActivity extends BaseActivity {
    private Button startMusicBtn;
    private Button stopMusicBtn;
    private static final int PUSH_NOTIFICATION_ID = (0x001);
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";

    public static void startAct(Context context){
        Intent intent = new Intent(context,MusicActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        startMusicBtn.setOnClickListener(v -> {
            startMusic();
        });
        stopMusicBtn.setOnClickListener(v -> {
            stopMusic();
        });

    }

    private void stopMusic() {

    }

    private void startMusic() {

    }

    private void initData() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = null;
            channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("音乐通知")
                    .setContentText("音乐内容")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.video_icon)
                    .setChannelId(PUSH_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_camera))
                    .build();

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(PUSH_NOTIFICATION_ID, notification);
            }
        }
    }

    private void initView() {
        startMusicBtn = (Button) findViewById(R.id.start_music_btn);
        stopMusicBtn = (Button) findViewById(R.id.stop_music_btn);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_music;
    }
}
