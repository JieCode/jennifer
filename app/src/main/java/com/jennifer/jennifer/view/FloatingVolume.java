package com.jennifer.jennifer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jennifer.jennifer.util.DensityUtils;

/**
 * 音量梯度显示
 * Created by Jingjie on 2019/6/14
 */
public class FloatingVolume extends View {

    public int mWidth;
    public int mHeight;

    private static int mStartX;
    public int mDy;
    public int mDx;
    public Paint mPaintVolume;
    public Paint mPaintMax;
    public int mDyFill;
    public int mDyEmpty;
    public int mNumMax;//可描绘的梯度片段最大数目
    public int mCurNum;//当前可描绘梯度片段数目

    public FloatingVolume(Context context) {
        this(context, null);
    }

    public FloatingVolume(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingVolume(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStartX = DensityUtils.dip2px(context, 10);
        mDx = DensityUtils.dip2px(context, 3);//mDy=3*mDx

        mDyFill = DensityUtils.dip2px(context, 4);
        mDyEmpty = DensityUtils.dip2px(context, 5);
        mDy = mDyEmpty + mDyFill;
        initVolumePaint();
        initMaxPaint();
    }

    private void initVolumePaint() {
        //init pen
        mPaintVolume = new Paint();
        mPaintVolume.setColor(Color.WHITE);
        mPaintVolume.setStyle(Paint.Style.FILL);
        mPaintVolume.setAntiAlias(true);
        mPaintVolume.setStrokeWidth(1);
    }

    private void initMaxPaint() {
        //init pen
        mPaintMax = new Paint();
        mPaintMax.setColor(Color.parseColor("#9b9b9b"));
        mPaintMax.setStyle(Paint.Style.FILL);
        mPaintMax.setAntiAlias(true);
        mPaintMax.setStrokeWidth(1);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //可以绘制的着墨区域的最大数
        int numDesignY = mHeight / mDy;
        int numDesignX = (mWidth - mStartX) / mDx;
        mNumMax = Math.min(numDesignX, numDesignY);
        //绘制多边形
        Path path = new Path();
        if (mPaintMax == null)
            initMaxPaint();
        for (int i = 0; i < mNumMax; i++) {
            path = new Path();
            path.moveTo(mDx, mHeight - i * (mDy));
            path.arcTo(new RectF(0, mHeight - mDyFill - i * mDy, mDx * 2, mHeight - i * mDy), 90, 180);
            path.lineTo(mStartX + i * mDx - mDx, mHeight - mDyFill - i * mDy);
            path.arcTo(new RectF(mStartX + i * mDx - mDx * 2, mHeight - mDyFill - i * mDy, mStartX + i * mDx, mHeight - i * mDy), 270, 180);
            path.close();
            canvas.drawPath(path, mPaintMax);
        }
        if (mPaintVolume == null)
            initVolumePaint();
        for (int i = 0; i < mCurNum; i++) {
            path = new Path();
            path.moveTo(mDx, mHeight - i * (mDy));
            path.arcTo(new RectF(0, mHeight - mDyFill - i * mDy, mDx * 2, mHeight - i * mDy), 90, 180);
            path.lineTo(mStartX + i * mDx - mDx, mHeight - mDyFill - i * mDy);
            path.arcTo(new RectF(mStartX + i * mDx - mDx * 2, mHeight - mDyFill - i * mDy, mStartX + i * mDx, mHeight - i * mDy), 270, 180);
            path.close();
            canvas.drawPath(path, mPaintVolume);
        }


    }

    /**
     * 设置当前可描绘层数,then 重绘
     * @param curFraction 当前音量占总音量百分比
     */
    public void updateFraction(double curFraction) {
        mCurNum = (int) (curFraction * mNumMax);
        postInvalidate();
    }
}
