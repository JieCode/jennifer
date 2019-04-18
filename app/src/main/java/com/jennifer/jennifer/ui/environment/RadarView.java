package com.jennifer.jennifer.ui.environment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.jennifer.jennifer.R;

/**
 * @author jingjie
 * @date 2019/3/28
 */
public class RadarView extends View {

    private Context mContext;
    private Paint mPaint;
    private int mProgress = 0;
    private static int MAX_PROGRESS = 100;
    private int mAngle;//弧度
    private String mText = "检测中";//中间文字
    private int outRoundColor;//外圆颜色
    private int inRoundColor;//内圆颜色
    private int roundWidth;//外圆内圆间隔宽度
    private int lineWidth;//线的宽度
    private int progressRadius;//小圆点半径
    private int progressLength;//尾巴长度，0-180
    private int style;
    private int textColor;//字体颜色
    private float textSize = 20;//字体大小
    private boolean isBold;//子弟是否加粗
    private int progressBarColor;//进度条颜色

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    /**
     * 解析自定义属性
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        mPaint = new Paint();
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.RadarView);
        outRoundColor = typedArray.getColor(R.styleable.RadarView_outCircleColor, getResources().getColor(R.color.white));
        inRoundColor = typedArray.getColor(R.styleable.RadarView_inCircleColor, getResources().getColor(R.color.white));
        progressBarColor = typedArray.getColor(R.styleable.RadarView_progressColor, getResources().getColor(R.color.white));
        textColor = typedArray.getColor(R.styleable.RadarView_textColor, getResources().getColor(R.color.white));
        isBold = typedArray.getBoolean(R.styleable.RadarView_textBold, true);
        roundWidth = typedArray.getDimensionPixelOffset(R.styleable.RadarView_roundWidth, 20);
        lineWidth = typedArray.getDimensionPixelOffset(R.styleable.RadarView_lineWidth, 3);
        textSize = typedArray.getDimensionPixelOffset(R.styleable.RadarView_textSize, 40);
        progressRadius = typedArray.getDimensionPixelOffset(R.styleable.RadarView_progressRadius, 5);
        progressLength = typedArray.getDimensionPixelOffset(R.styleable.RadarView_progressLength, 120);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外圆
        int center = getWidth() / 2;//圆心
        int radius = center - lineWidth;//半径
        mPaint.setColor(outRoundColor);//外圆颜色
        mPaint.setAlpha(125);
        mPaint.setStyle(Paint.Style.STROKE);//只绘制轮廓，空心圆
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setAntiAlias(true);//消除锯齿
        canvas.drawCircle(center, center, radius, mPaint);

        //画内圆
        mPaint.setColor(inRoundColor);
        mPaint.setAlpha(125);
        radius = radius - roundWidth;
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(center, center, radius, mPaint);

        //画进度是个弧线
        mPaint.setColor(progressBarColor);
        //圆弧范围的外切矩形
        mPaint.setStyle(Paint.Style.STROKE);//只绘制轮廓，空心圆
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rectF, -90 + mAngle, 90, false, mPaint);
        canvas.drawArc(rectF, -270 + mAngle, 90, false, mPaint);
        canvas.save();

        // 画文字将坐标平移至圆心
        canvas.translate(center,center);
        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        if (isBold) { //字体加粗
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
        if (!TextUtils.isEmpty(mText)) {
            // 动态设置文字长为圆半径,计算字体大小
            mPaint.setTextSize(textSize);
            // 将文字画到中间
            float textWidth = mPaint.measureText(mText);
            canvas.drawText(mText, - textWidth / 2, textSize / 2, mPaint);
            canvas.restore();
        }
        canvas.save();

        //画进度终点的小球，旋转画布的方式实现
        //将画布坐标原点移动至圆心
        canvas.translate(center, center);
        //旋转和进度相同的角度，因为进度是从-90度开始的所以-90度
        canvas.rotate(mAngle);
        //同理从圆心出发直接将原点平移至画小球的位置
        canvas.translate(radius, 0);
        mPaint.setColor(progressBarColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, progressRadius, mPaint);

        //画完后恢复画布坐标
        canvas.restore();

        //画进度终点的小球，旋转画布的方式实现
        //将画布坐标原点移动至圆心
        canvas.translate(center, center);
        //旋转和进度相同的角度，因为进度是从-180度开始的所以-180度
        canvas.rotate(mAngle - 180);
        //同理从圆心出发直接将原点平移至画小球的位置
        canvas.translate(radius, 0);
        canvas.drawCircle(0, 0, progressRadius, mPaint);
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * 设置进度
     *
     * @return
     */
    public void setProgress(int p) {
        if (p > MAX_PROGRESS) {
            mProgress = MAX_PROGRESS;
            mAngle = 360;
        } else {
            mProgress = p;
            mAngle = 360 * p / MAX_PROGRESS;
        }
        //更新画布
        invalidate();
    }

}
