package com.ychong.kankan.ui.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.ychong.kankan.R;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.BaseContract;

import java.io.File;

/**
 * 预览视频  图片  文件
 */
public class PreViewActivity extends BaseActivity {
    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;
    private ImageView picIv;
    private int preViewType;//预览类型

    public static  void startAct(Context context,int preViewType,String path){
        Intent intent = new Intent(context,PreViewActivity.class);
        intent.putExtra(BaseContract.PREVIEW_TYPE,preViewType);
        intent.putExtra(BaseContract.PATH,path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview;
    }

    private void initListener() {

    }

    private void initData() {
        preViewType = getIntent().getIntExtra(BaseContract.PREVIEW_TYPE, -1);
        String path = getIntent().getStringExtra(BaseContract.PATH);
        if (preViewType == -1 || TextUtils.isEmpty(path)) {
            onBackPressed();
            Toast.makeText(this, "数据错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (preViewType == BaseContract.VIDEO_TYPE) {
            //预览视频
            videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.gsy_video_player);
            videoPlayer.setVisibility(View.VISIBLE);
            preViewVideo(path);
        } else if (preViewType == BaseContract.PIC_TYPE) {
            //预览图片
            picIv.setVisibility(View.VISIBLE);
            preViewPic(path);
        } else if (preViewType == BaseContract.FILE_TYPE) {
            //预览文件
            preViewFile(path);
        }

    }

    /**
     * 预览文件
     *
     * @param path
     */
    private void preViewFile(String path) {

    }

    /**
     * 预览图片
     *
     * @param path
     */
    private void preViewPic(String path) {
        //需要判断图片类型 来自本地还是网络
        if (path.contains("http://") || path.contains("https://")) {
            //来自网络
            Glide.with(this).load(path).placeholder(R.drawable.kankan).override(1200, 1500).into(picIv);
        } else {
            //来自本地
            File file = new File(path);
            if (file.exists()) {
                Glide.with(this).load(file).placeholder(R.drawable.kankan).override(1200, 1500).into(picIv);
            } else {
                Toast.makeText(this, "本地文件不存在", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

        }


    }

    /**
     * 预览视频
     *
     * @param path
     */
    private void preViewVideo(String path) {
        videoPlayer.setUp(path, true, "预览视频");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.kankan);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能，这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(view -> orientationUtils.resolveByClick());
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(view -> onBackPressed());
        videoPlayer.startPlayLogic();

    }

    private void initView() {
        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.gsy_video_player);
        picIv = (ImageView) findViewById(R.id.pic_iv);

    }

    private void initLayout() {
        //setContentView(R.layout.activity_preview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (preViewType == BaseContract.VIDEO_TYPE) {
            videoPlayer.onVideoPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preViewType == BaseContract.VIDEO_TYPE) {
            videoPlayer.onVideoResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preViewType == BaseContract.VIDEO_TYPE) {
            GSYVideoManager.releaseAllVideos();
            if (orientationUtils != null) {
                orientationUtils.releaseListener();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (preViewType == BaseContract.VIDEO_TYPE) {
            //先返回正常状态
            if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                videoPlayer.getFullscreenButton().performClick();
                return;
            }
            //释放所有
            videoPlayer.setVideoAllCallBack(null);
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //监听横竖屏切换
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
            Log.e("dddd","横屏");
            setStatusBarColor(R.color.green);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
            Log.e("dddd","竖屏");
        }
    }
    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param colorId
     */
    public  void setStatusBarColor(int colorId) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(colorId));
    }
}
