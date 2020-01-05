package com.ychong.picandvideo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GestureDetectorCompat;

import com.ychong.picandvideo.R;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class BigView extends AppCompatImageView implements GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener {
    private static final String TAG = BigView.class.getSimpleName();
    private Context context;
    private int slideX = 0;
    private int slideY = 0;
    private BitmapRegionDecoder bitmapRegionDecoder;
    private Paint paint;
    private int mImageWidth;
    private int mImageHeight;
    private BitmapFactory.Options options;
    private Rect mRect;
    private GestureDetectorCompat gestureDetectorCompat;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix;
    private float scale = 1;
    private float maxScale = 4.0f;
    private float minScale = 0.2f;


    public BigView(Context context) {
        this(context, null);
        this.context = context;
        init();
    }

    public BigView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        init();
    }

    public BigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        gestureDetectorCompat = new GestureDetectorCompat(context, this);
        scaleGestureDetector = new ScaleGestureDetector(context,this);
        matrix = new Matrix();

        mRect = new Rect();
        paint = new Paint();
        try {
            InputStream inputStream = getResources().getAssets().open("2.jpg");

            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            mImageWidth = options.outWidth;
            mImageHeight = options.outHeight;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    float downX = 0;
    float downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetectorCompat.onTouchEvent(event)){
            return true;
        }
        if (scaleGestureDetector.onTouchEvent(event)){
            return true;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //拿到原来宽高，高度的mode和size
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);



        mRect.left = 0;
        mRect.right = getMeasuredWidth() + mRect.left;
        mRect.top = 0;
        mRect.bottom = getMeasuredHeight() + mRect.top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = bitmapRegionDecoder.decodeRegion(mRect, options);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //手指刚触碰到屏幕的瞬间  触发该方法
        Log.e(TAG, "手指刚触碰到屏幕的瞬间  触发该方法");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //手指按在屏幕上，它的时间范围在按下生效 ， 长按之前
        Log.e(TAG, "手指按在屏幕上，它的时间范围在按下生效 ， 长按之前");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //手指离开屏幕的那一刹那
        Log.e(TAG, "手指离开屏幕的那一刹那");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //手指在触摸屏上滑动
        Log.e(TAG, "手指在触摸屏上滑动");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //手指按在屏幕一段时间，并且没有松开
        Log.e(TAG, "手指按在屏幕一段时间，并且没有松开");
        //显示弹窗保存图片至本地
        showDialog(e.getX(),e.getY());

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //手指在触摸屏上迅速移动，并松开的动作
        Log.e(TAG, "手指在触摸屏上迅速移动，并松开的动作");
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        float currentScale = getScaleX();
        if (currentScale>maxScale&&scaleFactor<1.0f||currentScale<minScale
                && scaleFactor>1.0f || currentScale<maxScale && currentScale>minScale){
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    private void showDialog(float x, float y){
        PopupWindow popupWindow = new PopupWindow(this, GridView.MarginLayoutParams.WRAP_CONTENT,GridView.MarginLayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_menu,null);
        TextView saveTv = view.findViewById(R.id.save_tv);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);//true点击空白处消失
        popupWindow.showAtLocation(this, Gravity.NO_GRAVITY,(int) (x),(int)(y));

        saveTv.setOnClickListener(v -> {
            popupWindow.dismiss();
        });
    }

}
