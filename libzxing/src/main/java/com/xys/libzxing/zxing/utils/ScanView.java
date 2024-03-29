package com.xys.libzxing.zxing.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xys.libzxing.R;

public class ScanView extends View {

    private int scanMaginWith;
    private int scanMaginheight;

    private Paint paint;
    private Bitmap scanLine;
    private Rect scanRect;
    private Rect lineRect;

    //扫描线位置
    private int scanLineTop;
    //透明度
    private int alpha = 100;

    private int bitmapHigh;
    private ValueAnimator valueAnimator;

    public ScanView(Context context) {
        super(context);
        init();
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scanLine = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan);

        bitmapHigh = scanLine.getHeight();

        scanRect = new Rect();
        lineRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        scanMaginWith = getMeasuredWidth() / 10;
        scanMaginheight = getMeasuredHeight() >> 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        scanRect.set(scanMaginWith, scanMaginheight, getWidth() - scanMaginWith, getHeight() - scanMaginheight);
        startAnim();
        paint.setAlpha(alpha);
        lineRect.set(scanMaginWith, scanLineTop, getWidth() - scanMaginWith, scanLineTop + bitmapHigh);
        canvas.drawBitmap(scanLine, null, lineRect, paint);
    }

    public void startAnim() {
        if(valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(scanRect.top, scanRect.bottom);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setDuration(4000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scanLineTop = (int) animation.getAnimatedValue();
                    int startHideHeight = (scanRect.bottom - scanRect.top) / 6;
                    alpha = scanRect.bottom - scanLineTop <= startHideHeight ? (int) (((double) (scanRect.bottom - scanLineTop) / startHideHeight) * 100) : 100;
                    postInvalidate();
                }
            });
            valueAnimator.start();
        }
    }

    public void cancelAnim() {
        if(valueAnimator != null){
            valueAnimator.cancel();
        }
    }
}
