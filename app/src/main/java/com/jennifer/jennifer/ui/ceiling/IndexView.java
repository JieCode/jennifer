package com.jennifer.jennifer.ui.ceiling;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by allen on 2016/10/26.
 */

public class IndexView extends View {

    private Context mContext;
    //private TextView mShowTextDialog;


    private Paint mPaint;
    private Paint mPaint2;

    private int mWidth;
    private int mHeight;

    private int mCellWidth;
    private int mCellHeight;

    private int mWordSize;
    private int mwordColor;

    private int GRAY = 0xFFe8e8e8;
    private int DEFAULT_TEXT_COLOR = 0xFF999999;


    private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener;
    private String[] WORDS = new String[]{};

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mWordSize = sp2px(mContext, 11);
        mwordColor = 0;
        initPaint();
    }


    public void setShowTextDialog(TextView textDialog) {
        //this.mShowTextDialog = textDialog;
    }

    public void setWords(String[] words) {
        this.WORDS = words;
        invalidate();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mWordSize);

        mPaint2 = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mCellHeight = mHeight / 32;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < WORDS.length; i++) {
            float xPos = mWidth / 2 - mPaint.measureText(WORDS[i]) / 2;
            float yPos = mCellHeight * i + mCellHeight;

            if (i == mChoose) {
                mPaint.setColor(Color.parseColor("#FFFFFF"));
                mPaint2.setColor(Color.parseColor("#59d1d6"));
                canvas.drawCircle(mWidth / 2, yPos - mPaint.measureText(WORDS[i]) / 2, mWidth / 3, mPaint2);
            } else {
                mPaint.setColor(Color.parseColor("#333333"));
            }
            canvas.drawText(WORDS[i], xPos, yPos, mPaint);
        }
    }

    // 选中的项
    private int mChoose = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = mChoose;
        final int c = (int) (y / (getHeight()  * WORDS.length / 32)  * WORDS.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.


        switch (action) {
            case MotionEvent.ACTION_UP:
                //setBackgroundColor(0x00000000);
                //mChoose = -1;//
                invalidate();
                /*if (mShowTextDialog != null) {
                    mShowTextDialog.setVisibility(View.INVISIBLE);
                }*/
                break;

            default:
                //setBackgroundColor(GRAY);
                if (oldChoose != c) {
                    if (c >= 0 && c < WORDS.length) {
                        if (mOnTouchingLetterChangedListener != null) {
                            mOnTouchingLetterChangedListener.onTouchingLetterChanged(WORDS[c]);
                        }
                        /*if (mShowTextDialog != null) {
                            mShowTextDialog.setText(WORDS[c]);
                            mShowTextDialog.setVisibility(View.VISIBLE);
                        }*/
                        mChoose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }


    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener letterChangedListener) {
        mOnTouchingLetterChangedListener = letterChangedListener;

    }

    public interface OnTouchingLetterChangedListener {
         void onTouchingLetterChanged(String letter);
    }

    /**
     * 文字字体大小sp转换px
     *
     * @param context 上下文对象
     * @param spValue sp的值
     * @return 返回值
     */
    public int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
