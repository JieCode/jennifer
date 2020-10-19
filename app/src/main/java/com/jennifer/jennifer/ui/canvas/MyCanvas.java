package com.jennifer.jennifer.ui.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jingjie
 * @date 2019-11-20 11:41
 * TODO 绘制圆角矩形+倒三角会话样式
 */
public class MyCanvas extends View {
    private Paint paint;
    private int measureW, measureH;//布局宽高
    private int radius = 8;//圆角矩形圆角半径
    private int triangleW = 16, triangleH = 16;//倒三角底边长和高
    private int offset = 12;//倒三角最右边的位置相对于布局的偏移量Ø

    public MyCanvas(Context context) {
        this(context, null);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(5, 0, 0, Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        measureW = getMeasuredWidth();
        measureH = getMeasuredHeight();
        Path path = new Path();
        if (paint == null)
            initPaint();
        path.moveTo(radius, 0);
        path.arcTo(new RectF(0, 0, radius, radius), 90, 90);
        path.lineTo(0, measureH - triangleH - radius);
        path.arcTo(new RectF(0, measureH - triangleH - radius, radius, measureH - triangleH), 180, 90);
        path.lineTo(measureW - radius - offset - triangleW, measureH - triangleH);
        path.lineTo(measureW - radius - offset - (triangleW / 2), measureH);
        path.lineTo(measureW - radius - offset, measureH - triangleH);
        path.lineTo(measureW - radius, measureH - triangleH);
        path.arcTo(new RectF(measureW - radius, measureH - triangleH - radius, measureW, measureH - triangleH), 270, 90);
        path.lineTo(measureW, radius);
        path.arcTo(new RectF(measureW - radius, 0, measureW, radius), 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }
}
