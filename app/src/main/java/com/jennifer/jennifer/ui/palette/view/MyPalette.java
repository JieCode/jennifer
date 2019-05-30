package com.jennifer.jennifer.ui.palette.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.jennifer.jennifer.R;
import com.jennifer.jennifer.ui.palette.bean.LinePath;
import com.jennifer.jennifer.ui.palette.bean.PaintInfo;
import com.jennifer.jennifer.ui.palette.bean.PathInfo;
import com.jennifer.jennifer.ui.palette.util.ModelParser;
import com.jennifer.jennifer.ui.palette.util.ThreadPoolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by wutongtech_shengmao on 2017/6/9 09:06.
 * 作用：画板类
 */
public class MyPalette extends View {

    private static final String TAG = "MyPalette";
    public static final int MODE_PAINT = 1;
    public static final int MODE_ERASER = 2;
    /**
     * 本人的画笔
     */
    private Paint mPaint;
    private Paint mReceiverPaint;

    private Path mPath;
    private Path mReceiverPath;

    /**
     * 内存中创建的Canvas  画面
     */
    private Canvas mCanvas;

    private Bitmap mBitmap;
    private Paint mBitmapPaint;
    /**
     * 用来保存画的  每一笔  的路径集合，每再完一笑就清空。
     **/
    private PathInfo pathInfo;
    private List<LinePath> list = new ArrayList<>();

    private CopyOnWriteArrayList<DrawPath> savePath;
    private CopyOnWriteArrayList<DrawPath> deletePath;
    private DrawPath dp;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private boolean isMoving = false;

    private int currentColor = Color.BLUE;//默认是蓝色
    private int receiverColor = R.color.purple;//默认是紫色
    private int curColorIndex = 2;//默认是蓝色的
    private int receiverColorIndex = 0;

    private int currentSize = 5;
    private int currentStyle = 1;

    private int paintSize = 2;//默认为2
    private int receivePaintSize = 2;//默认为2

    private int width, height;
    private int screenWidth, screenHeight;//画板的宽高

    private int curPage = 1;

    private Map<Integer, CopyOnWriteArrayList<DrawPath>> savePathMap = new HashMap<>();
    private Map<Integer, CopyOnWriteArrayList<DrawPath>> deletePathMap = new HashMap<>();
    private Map<Integer, MyCanvas> canvasMap = new HashMap<>();
    /**
     * 每一笔画完，　本地保存的　id .记录下次匹配
     **/
    private String Paint_id_time;

    // 画笔颜色 0紫色 1红色 2蓝色 3黑色
//    private int[] paintColor = {R.color.purple,R.color.red, R.color.blue, R.color.black};
    private int[] paintColor = {Color.parseColor("#FF800080"), Color.RED, Color.BLUE, Color.BLACK};

    private boolean canDraw = true;//是否能画

    //需要在该页面绘制的信息
    private Map<Integer, CopyOnWriteArrayList<List<PaintInfo>>> needDraw = new HashMap<>();

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public void setPaintId(PathInfo pathInfo, int paint_id, String paint) {
        if (pathInfo == null || pathInfo.getSendTime() == null)
            return;
        String target_paint_id = pathInfo.getSendTime();
        for (DrawPath path : savePath) {
            if (path.paint_id != null && path.paint_id.equals(target_paint_id)) {
                path.paint_id = paint_id + "";
                path.info = paint;
                break;
            }
        }
    }

    public boolean isNotEmpty() {
        if (savePath.size() > 0) {
            return true;
        }
        return false;
    }


    public interface OnFinishDrawingListener {
        void onFinishDraw(String path);
    }

    public OnFinishDrawingListener onFinishDrawingListener;

    public void setOnFinishDrawingListener(OnFinishDrawingListener onFinishDrawingListener) {
        this.onFinishDrawingListener = onFinishDrawingListener;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        if (this.curPage == curPage) {
            return;
        }
        savePathMap.put(this.curPage, savePath);
        deletePathMap.put(this.curPage, deletePath);

//        MyCanvas myCanvas = new MyCanvas();
//        myCanvas.mBitmap = this.mBitmap;
//        myCanvas.mPaint = this.mPaint;
//        myCanvas.mCanvas = this.mCanvas;
//
//        canvasMap.put(this.curPage, myCanvas);

        this.curPage = curPage;

        savePath = savePathMap.get(curPage) == null ? new CopyOnWriteArrayList<DrawPath>() : savePathMap.get(curPage);
        deletePath = deletePathMap.get(curPage) == null ? new CopyOnWriteArrayList<DrawPath>() : deletePathMap.get(curPage);
//        if (canvasMap.get(curPage) != null) {
//            mBitmap = canvasMap.get(curPage).mBitmap;
//            mPaint = canvasMap.get(curPage).mPaint;
//            mCanvas = canvasMap.get(curPage).mCanvas;
//            mPath = null;
//
//        }else{
//
//        }
        initCanvas();

        invalidate();

//        reDrawForSwitch();
        reDraw();

        checkNeedDraw();

    }

    /**
     * 检查需要绘制的笔记
     */
    private void checkNeedDraw() {
        CopyOnWriteArrayList<List<PaintInfo>> drawPaths = needDraw.get(curPage);
        if (drawPaths == null || drawPaths.isEmpty()) {
            return;
        }
        for (List<PaintInfo> infos : drawPaths) {
            if (infos != null && !infos.isEmpty()) {
                drawPath(infos);
            }
        }
        drawPaths.clear();
    }

    /**
     * 添加需要绘制的笔记
     *
     * @param page  页码
     * @param infos 笔记
     */
    public void addNeedDrawInfo(int page, List<PaintInfo> infos) {
        if (page == curPage) {
            return;
        }
        if (infos == null || infos.isEmpty()) {
            return;
        }
        CopyOnWriteArrayList<List<PaintInfo>> drawPaths = needDraw.get(curPage);
        if (drawPaths == null) {
            drawPaths = new CopyOnWriteArrayList<>();
            needDraw.put(page, drawPaths);
        }
        drawPaths.add(infos);

    }


    public MyPalette(Context context) {
        this(context, null);
    }

    public MyPalette(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPalette(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        savePath = new CopyOnWriteArrayList<>();
        deletePath = new CopyOnWriteArrayList<>();

        detector = new GestureDetector(context, onGestureListener);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        // 初始化 画布大小bitmap

        screenWidth = width;
        screenHeight = height;

        //点阵笔相关
//        if ( width > height ) {
//            m_scaleX = ((float) (width) / 7093f);
//            m_scaleY = ((float) (height) / 4017f);
//        } else {
//            m_scaleX = ((float) (width) / 4017f);
//            m_scaleY = ((float) (height) / 7093f);
//        }

        initCanvas();

//        Log.e("onMeasure width is " + width + ",height is " + height);
    }

    private void initCanvas() {
        if (width > 0) {
            setPaintStyle(currentColor);
            setReceiverPaintStyle(receiverColor);
            if (mBitmap != null) {
                if (!mBitmap.isRecycled()) {
                    mBitmap = null;
                }
            }
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);  // 所有mCanvas画的东西都被保存在了mBitmap中

            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

    }

    private void setReceiverPaintStyle(int receiverColorResId) {
        mReceiverPaint = new Paint();
        mReceiverPaint.setColor(receiverColorResId);
        mReceiverPaint.setAntiAlias(true);
        mReceiverPaint.setDither(true);
        mReceiverPaint.setStyle(Paint.Style.STROKE);
        mReceiverPaint.setStrokeJoin(Paint.Join.ROUND);
        mReceiverPaint.setStrokeCap(Paint.Cap.ROUND);
        mReceiverPaint.setStrokeWidth(receivePaintSize);
    }

    // 设置画笔样式
    public void setPaintStyle(int colorResId) {
        if (currentStyle == MODE_ERASER) {
            mPaint = getEraserPaintStyle();
        } else {
            mPaint = new Paint();
            mPaint.setColor(colorResId);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(paintSize);
        }
    }

    private Paint getEraserPaintStyle() {
        Paint mEraserPaint = new Paint();
        mEraserPaint.setAlpha(0);//橡皮擦透明度为0
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//设置橡皮擦的混合模式,只在原图像和目标图像橡胶的地方绘制目标图像
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeWidth(10);
        return mEraserPaint;
    }

    // 设置画笔样式
    public void selectPaintStyle(int which) {
        currentStyle = which;
        setPaintStyle(currentColor);
    }

//    // 选择画笔大小
//    public void selectPaintSize(int which) {
//        int size = Integer.parseInt(this.getResources().getStringArray(
//                R.array.paintsize)[which]);
//        currentSize = size;
//        setPaintStyle(currentColor);
//    }

    // 设置画笔颜色
    public void selectPaintColor(int which) {
        curColorIndex = which;
        currentColor = paintColor[which];
        setPaintStyle(currentColor);
    }

    /**
     * 选择对方的画笔颜色
     *
     * @param which
     */
    public void selectReceiverPaintColor(int which) {
        receiverColorIndex = which;
        receiverColor = paintColor[which];
        setReceiverPaintStyle(receiverColor);
    }

    /**
     * 撤销某一笔。
     *
     * @param paint_id
     */
    public void undo(String paint_id) {
        if (!"0".equals(paint_id)) {

            // 调用初始化画布函数以清空画布
            List<DrawPath> list = savePath;
            for (DrawPath path : list) {
                if (path.paint_id.equals(paint_id)) {
                    System.out.println("undo get paint have the same paint");
                    initCanvas();

                    deletePath.add(path);
                    savePath.remove(path);

                    // 将路径保存列表中的路径重绘在画布上
                    Iterator<DrawPath> iter = savePath.iterator(); // 重复保存
                    while (iter.hasNext()) {
                        DrawPath dp = iter.next();
                        mCanvas.drawPath(dp.path, dp.paint);
                    }
                    invalidate();// 刷新

                    break;
                }
            }
        }

    }

    /**
     * 撤销的核心思想就是将画布清空， 将保存下来的Path路径最后一个移除掉， 重新将路径画在画布上面。
     */
    public String undo() {

        if (savePath != null && savePath.size() > 0) {
            // 调用初始化画布函数以清空画布

            // 将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
            DrawPath drawPath = savePath.get(savePath.size() - 1);

            initCanvas();
            mPath = null;

            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);

            // 将路径保存列表中的路径重绘在画布上
            Iterator<DrawPath> iter = savePath.iterator(); // 重复保存
            while (iter.hasNext()) {
                DrawPath dp = iter.next();
                mCanvas.drawPath(dp.path, dp.paint);
            }
            invalidate();// 刷新
            return drawPath.paint_id;
        }

        return "0";
    }

    /**
     * 清空所有的数据
     */
    public void clearAllData() {
        savePathMap.clear();
        deletePathMap.clear();
        needDraw.clear();
        setCurPage(1);
    }

    /**
     * 重绘，恢复之前画的
     */
    public void reDraw() {
        if (savePath == null) {
            return;
        }

        initCanvas();
        mPath = null;
        Log.d(TAG, "reDraw savepath size is " + savePath.size());

        Iterator<DrawPath> iter = savePath.iterator(); // 重复保存
        while (iter.hasNext()) {
            try {
                DrawPath dp = iter.next();

                mCanvas.drawPath(dp.path, dp.paint);
            } catch (Exception e) {

            }
        }
        invalidate();// 刷新
    }

    /**
     * 全屏切换后重绘之前画的东西
     */
    public void reDrawForSwitch() {
        if (savePath == null) {
            return;
        }
        initCanvas();
        mPath = null;
        Log.d(TAG, "reDraw savepath size is " + savePath.size());


        ThreadPoolUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Path myPath;

                Iterator<DrawPath> iter = savePath.iterator(); // 重复保存
                while (iter.hasNext()) {
                    DrawPath dp = iter.next();

                    List<LinePath> pathPoints = dp.linePaths;

                    if (pathPoints == null) {
                        continue;
                    }

                    myPath = new Path();
                    myPath.reset();
                    for (int i = 0; i < pathPoints.size(); i++) {
                        if (i == 0) {
                            int x = (pathPoints.get(0).getX() * screenWidth / 1000);
                            int y = (pathPoints.get(0).getY() * screenHeight / 1000);
                            myPath.moveTo(x, y);
                        } else {
                            int x = pathPoints.get(i).getX() * screenWidth / 1000;
                            int y = pathPoints.get(i).getY() * screenHeight / 1000;
                            int preX = pathPoints.get(i - 1).getX() * screenWidth / 1000;
                            int preY = pathPoints.get(i - 1).getY() * screenHeight / 1000;
                            int dx = Math.abs(x - preX);
                            int dy = Math.abs(y - preY);
                            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                                myPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);// 让线条圆滑
                            } else {
                                myPath.lineTo(x, y);
                            }
                            if (i == pathPoints.size() - 1) {
                                myPath.lineTo(x, y);
                            }
                        }
                        postInvalidate();
                    }
                    mCanvas.drawPath(myPath, dp.paint);
                    dp.path = myPath;

                }

                myPath = null;
                postInvalidate();
            }
        });

        invalidate();// 刷新
    }

    /**
     * 恢复的核心思想就是将撤销的路径保存到另外一个列表里面(栈)， 然后从redo的列表里面取出最顶端对象， 画在画布上面即可
     */
    public void redo() {
        if (deletePath.size() > 0) {
            // 将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            // 将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);
            // 将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }

    /**
     * 清空当前页面的数据
     * 清空的主要思想就是初始化画布 将保存路径的两个List清空
     */
    public void removeAllPaint() {
        // 调用初始化画布函数以清空画布
        initCanvas();
        invalidate();// 刷新
        savePath.clear();
        deletePath.clear();
    }

    /**
     * 清空某一页的画板数据
     *
     * @param page 页码
     */
    public void removePaint(int page) {
        if (page == curPage) {
            removeAllPaint();
            return;
        }
        CopyOnWriteArrayList<DrawPath> drawPaths = savePathMap.get(page);
        if (drawPaths != null) {
            drawPaths.clear();
        }
        CopyOnWriteArrayList<DrawPath> delete = deletePathMap.get(page);
        if (delete != null) {
            delete.clear();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint); // 显示旧的画布

        if (mPath != null) {
            // 实时的显示
            if (currentStyle != MODE_ERASER)
                canvas.drawPath(mPath, mPaint);
        }

        if (mReceiverPath != null) {
//            Log.e("TAG", "draw width = " +  mReceiverPaint.getStrokeWidth());
            if (receiverColorIndex != 9)
                canvas.drawPath(mReceiverPath, mReceiverPaint);
        }

        //点阵笔相关
//        if(penPath != null){
//            canvas.drawPath(penPath, mPaint);
//        }

//        Log.e("onDraw");

    }

    class MyCanvas {
        Bitmap mBitmap;
        Paint mPaint;
        Canvas mCanvas;
    }

    public void drawPath(final List<PaintInfo> paint) {  // 画了

        ThreadPoolUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                draw(paint);
            }
        });

    }

    private synchronized void draw(List<PaintInfo> paint) {
        for (PaintInfo info : paint) {
            mReceiverPath = new Path();
            mReceiverPath.reset();

            String json = info.getPaint();
            json = json.replace(" ", "");
            PathInfo pathInfo = ModelParser.getObjectfromJson(json, PathInfo.class);
            List<LinePath> list = pathInfo.getLineData();
            int colorIndex = pathInfo.getLineColor();
            //加入每一笔的宽度
            receivePaintSize = pathInfo.getLineWidth();
            if (receivePaintSize <= 0) {
                receivePaintSize = paintSize;
            }
            selectReceiverPaintColor(colorIndex);

            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    int x = list.get(0).getX() * screenWidth / 1000;
                    int y = list.get(0).getY() * screenHeight / 1000;
                    mReceiverPath.moveTo(x, y);
                } else {
                    int x = list.get(i).getX() * screenWidth / 1000;
                    int y = list.get(i).getY() * screenHeight / 1000;
                    int preX = list.get(i - 1).getX() * screenWidth / 1000;
                    int preY = list.get(i - 1).getY() * screenHeight / 1000;
                    int dx = Math.abs(x - preX);
                    int dy = Math.abs(y - preY);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        mReceiverPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);// 让线条圆滑
                    } else {
                        mReceiverPath.lineTo(x, y);
                    }
                    if (i == list.size() - 1) {
                        mReceiverPath.lineTo(x, y);
                    }
                }
                postInvalidate();
            }

            //获取像素点的颜色
//            Log.e("mCanvas = " + mCanvas);
//            Log.e("mReceiverPath = " + mReceiverPath);
//            Log.e("mReceiverPaint = " + mReceiverPaint);

            mCanvas.drawPath(mReceiverPath, mReceiverPaint);

            DrawPath drawPath = new DrawPath();
            drawPath.path = mReceiverPath;
            drawPath.paint_id = info.getPaint_id() + "";
            drawPath.paint = mReceiverPaint;
            drawPath.linePaths = list;
            drawPath.info = info.getPaint();

            savePath.add(drawPath);

        }  //第一笔

        mReceiverPath = null;
        postInvalidate();
    }


    /**
     * 会议中的绘制，筛选绘制过的paint_id不再绘制了
     *
     * @param paint
     * @param meeting
     */
    public void drawPath(final List<PaintInfo> paint, boolean meeting) {  // 画了
        if (!meeting) {
            drawPath(paint);
            return;
        }


        ThreadPoolUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                drawForMeeting(paint);
            }
        });

    }

    /**
     * 会议中的绘制，筛选绘制过的paint_id不再绘制了
     *
     * @param paint
     * @param meeting
     */
    public void drawPath(final List<PaintInfo> paint, boolean meeting, final int page) {  // 画了
        if (!meeting) {
            drawPath(paint);
            return;
        }


        ThreadPoolUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                drawForMeeting(paint, page);
            }
        });

    }

    //处理多线程的数据安全问题 ---筛选绘制，如果已经存在，则不在进行绘制
    private synchronized void drawForMeeting(List<PaintInfo> paint) {

        for (PaintInfo info : paint) {
            //绘制过的paint_id不再绘制了
            if (savePath.contains(new DrawPath(info.getPaint_id() + "", info.getPaint()))) {
                continue;
            }

            mReceiverPath = new Path();
            mReceiverPath.reset();

            String json = info.getPaint();
            json = json.replace(" ", "");
            PathInfo pathInfo = ModelParser.getObjectfromJson(json, PathInfo.class);
            List<LinePath> list = pathInfo.getLineData();
            int colorIndex = pathInfo.getLineColor();
            //加入每一笔的宽度
            receivePaintSize = pathInfo.getLineWidth();
            if (receivePaintSize <= 0) {
                receivePaintSize = paintSize;
            }
            selectReceiverPaintColor(colorIndex);

            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    int x = list.get(0).getX() * screenWidth / 1000;
                    int y = list.get(0).getY() * screenHeight / 1000;
                    mReceiverPath.moveTo(x, y);
                } else {
                    int x = list.get(i).getX() * screenWidth / 1000;
                    int y = list.get(i).getY() * screenHeight / 1000;
                    int preX = list.get(i - 1).getX() * screenWidth / 1000;
                    int preY = list.get(i - 1).getY() * screenHeight / 1000;
                    int dx = Math.abs(x - preX);
                    int dy = Math.abs(y - preY);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        mReceiverPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);// 让线条圆滑
                    } else {
                        mReceiverPath.lineTo(x, y);
                    }
                    if (i == list.size() - 1) {
                        mReceiverPath.lineTo(x, y);
                    }
                }
                postInvalidate();
            }

            mCanvas.drawPath(mReceiverPath, mReceiverPaint);

            DrawPath drawPath = new DrawPath();
            drawPath.path = mReceiverPath;
            drawPath.paint_id = info.getPaint_id() + "";
            drawPath.paint = mReceiverPaint;
            drawPath.linePaths = list;
            drawPath.info = info.getPaint();

            savePath.add(drawPath);

        }  //第一笔

        mReceiverPath = null;
        postInvalidate();
    }

    //处理多线程的数据安全问题 ---筛选绘制，如果已经存在，则不在进行绘制
    private synchronized void drawForMeeting(List<PaintInfo> paint, int page) {
        if (curPage != page) {
            return;
        }

        for (PaintInfo info : paint) {
            //绘制过的paint_id不再绘制了
            if (savePath.contains(new DrawPath(info.getPaint_id() + "", info.getPaint()))) {
                continue;
            }

            mReceiverPath = new Path();
            mReceiverPath.reset();

            String json = info.getPaint();
            json = json.replace(" ", "");
            PathInfo pathInfo = ModelParser.getObjectfromJson(json, PathInfo.class);
            List<LinePath> list = pathInfo.getLineData();
            int colorIndex = pathInfo.getLineColor();
            //加入每一笔的宽度
            receivePaintSize = pathInfo.getLineWidth();
            if (receivePaintSize <= 0) {
                receivePaintSize = paintSize;
            }
            selectReceiverPaintColor(colorIndex);

            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    int x = list.get(0).getX() * screenWidth / 1000;
                    int y = list.get(0).getY() * screenHeight / 1000;
                    mReceiverPath.moveTo(x, y);
                } else {
                    int x = list.get(i).getX() * screenWidth / 1000;
                    int y = list.get(i).getY() * screenHeight / 1000;
                    int preX = list.get(i - 1).getX() * screenWidth / 1000;
                    int preY = list.get(i - 1).getY() * screenHeight / 1000;
                    int dx = Math.abs(x - preX);
                    int dy = Math.abs(y - preY);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        mReceiverPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);// 让线条圆滑
                    } else {
                        mReceiverPath.lineTo(x, y);
                    }
                    if (i == list.size() - 1) {
                        mReceiverPath.lineTo(x, y);
                    }
                }
                postInvalidate();
            }

            mCanvas.drawPath(mReceiverPath, mReceiverPaint);

            DrawPath drawPath = new DrawPath();
            drawPath.path = mReceiverPath;
            drawPath.paint_id = info.getPaint_id() + "";
            drawPath.paint = mReceiverPaint;
            drawPath.linePaths = list;
            drawPath.info = info.getPaint();

            savePath.add(drawPath);

        }  //第一笔

        mReceiverPath = null;
        postInvalidate();
    }

    // 路径对象
    class DrawPath {
        Path path;
        Paint paint;
        String paint_id;
        List<LinePath> linePaths;
        String info;

//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof DrawPath)) return false;
//
//            DrawPath drawPath = (DrawPath) o;
//
//            return paint_id.equals(drawPath.paint_id);
//
//        }
//
//        @Override
//        public int hashCode() {
//            return paint_id.hashCode();
//        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DrawPath)) return false;

            DrawPath drawPath = (DrawPath) o;

            if (paint_id != null ? !paint_id.equals(drawPath.paint_id) : drawPath.paint_id != null)
                return false;
            return info != null ? info.equals(drawPath.info) : drawPath.info == null;

        }

        @Override
        public int hashCode() {
            int result = paint_id != null ? paint_id.hashCode() : 0;
            result = 31 * result + (info != null ? info.hashCode() : 0);
            return result;
        }

        public DrawPath(String paint_id, String info) {
            this.paint_id = paint_id;
            this.info = info;
        }

//        public DrawPath(String paint_id) {
//            this.paint_id = paint_id;
//        }

        public DrawPath() {
        }
    }


    private void touch_start(float x, float y) {
        mPath.reset();// 清空path
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        this.isMoving = false;
        LinePath path = new LinePath();
        path.setX((int) (x * 1000 / screenWidth));
        path.setY((int) (y * 1000 / screenHeight));
        list.add(path);
        if (currentStyle == MODE_ERASER)
            mCanvas.drawPath(mPath, mPaint);

    }

    private void touch_move(float x, float y) {
        if (mPath == null) {
            return;
        }

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//             mPath.quadTo(mX, mY, x, y);
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);// 让线条圆滑
//            mX = x;
//            mY = y;
        } else {
            mPath.lineTo(x, y);
        }


        this.isMoving = true;

        mX = x;
        mY = y;
        this.isMoving = true;

        LinePath path = new LinePath();
        path.setX((int) (x * 1000 / screenWidth));
        path.setY((int) (y * 1000 / screenHeight));
        list.add(path);
        if (currentStyle == MODE_ERASER)
            mCanvas.drawPath(mPath, mPaint);
    }

    private void touch_up(float x, float y) {

        if (mPath == null) {
            return;
        }
        mPath.lineTo(mX, mY);
        Paint_id_time = System.currentTimeMillis() + "";
        dp.paint_id = Paint_id_time;
        mCanvas.drawPath(mPath, mPaint);

        savePath.add(dp);
        mPath = null;
        this.isMoving = false;

//        LinePath path = new LinePath();
//        path.setX((int) (x * 1000 / screenWidth));
//        path.setY((int) (y * 1000 / screenHeight));
//        list.add(path);

        List<LinePath> linePaths = new ArrayList<>();
        if (null != list) {
            linePaths.addAll(list);
        }
        dp.linePaths = linePaths;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        if (!canDraw) {//如果不能画，则不继续下面操作
            return true;
        }

        if (event.getPointerCount() > 1) {
            return true;
        }


        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = mPaint;
                selectPaintColor(curColorIndex);

                pathInfo = new PathInfo();
                list.clear();
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                touch_move(x, y);
                invalidate();

                break;
            case MotionEvent.ACTION_UP:

                touch_up(x, y);
                invalidate();


                if (onFinishDrawingListener != null) {
                    if (pathInfo != null) {
                        pathInfo.setLineColor(curColorIndex);
                        pathInfo.setLineData(list);
                        pathInfo.setSendTime(Paint_id_time + "");
                        pathInfo.setLineWidth(paintSize);
                        onFinishDrawingListener.onFinishDraw(new Gson().toJson(pathInfo));
                    }


                    pathInfo = null;
                    list.clear();
                }
                break;
        }

        return true;
    }

    private GestureDetector detector;

    private GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        //点击事件
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //这个事情稍微慢些，用下面那个 这个双击不会响应，下面的方法双击也会走
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (tapListener != null) {
                tapListener.onSingleTap();
            }
            return super.onSingleTapUp(e);
        }
    };

    public SingleTapListener getTapListener() {
        return tapListener;
    }

    public void setTapListener(SingleTapListener tapListener) {
        this.tapListener = tapListener;
    }

    private SingleTapListener tapListener;

    public interface SingleTapListener {
        void onSingleTap();
    }


    /*********************************点阵笔的数据*******************************/
//    public synchronized void penDown(float x,float y){
////        Log.e("TAG", "pen down");
//
//        penPath = new Path();
//        penDrawPath = new DrawPath();
//        penDrawPath.path = penPath;
//        penDrawPath.paint = mPaint;
//        selectPaintColor(curColorIndex);
//
//        penPathInfo = new PathInfo();
//
//        penPath.reset();// 清空path
//        penPath.moveTo(x * m_scaleX, y * m_scaleY);
//        penX = x;
//        penY = y;
//
//        LinePath path = new LinePath();
//        path.setX((int) (x * m_scaleX * 1000 / screenWidth));
//        path.setY((int) (y * m_scaleY * 1000 / screenHeight));
//
//        penLineList.add(path);
//
//
//        postInvalidate();
//    }
//
//    public synchronized void penUp(float x,float y){
//
//        if(penPath == null){
//            return;
//        }
//        penPath.lineTo(penX * m_scaleX, penY * m_scaleY);
//        penPaintIdTime = System.currentTimeMillis() + "";
//        penDrawPath.paint_id = penPaintIdTime;
//        mCanvas.drawPath(penPath, mPaint);
//
//        savePath.add(penDrawPath);
//        penPath = null;
//
//        LinePath path = new LinePath();
//        path.setX((int) (x * m_scaleX * 1000 / screenWidth));
//        path.setY((int) (y * m_scaleY * 1000 / screenHeight));
//        penLineList.add(path);
//
//        List<LinePath> linePaths = new ArrayList<>();
//        if(null != penLineList){
//            linePaths.addAll(penLineList);
//        }
//        penDrawPath.linePaths = linePaths;
//
//
//        if (onFinishDrawingListener != null) {
//            penPathInfo.setLineColor(curColorIndex);
//            penPathInfo.setLineData(penLineList);
//            penPathInfo.setSendTime(penPaintIdTime + "");
//
//            onFinishDrawingListener.onFinishDraw(new Gson().toJson(penPathInfo));
//
//            penPathInfo = null;
//            penLineList.clear();
//        }
//
//        penPathInfo = null;
//        penLineList.clear();
//        postInvalidate();
//    }
//
//    public synchronized void penMove(float x,float y){
//
////        Log.e("TAG", "pen move");
//
//        if (penPath == null) {
////            Log.e("TAG", "move but path is null");
//            return;
//        }
//        penPath.lineTo(x * m_scaleX, y * m_scaleY);
//
//        penX = x;
//        penY = y;
//
//        LinePath path = new LinePath();
//        path.setX((int) (x * m_scaleX * 1000 / screenWidth));
//        path.setY((int) (y * m_scaleY * 1000 / screenHeight));
//        penLineList.add(path);
//
//        postInvalidate();
//    }
//
//    float m_scaleX;
//    float m_scaleY;
//
//    private float penX, penY;
//
//    //笔的绘制对象
//    private PathInfo penPathInfo;
//    private List<LinePath> penLineList = new ArrayList<>();
//    private DrawPath penDrawPath;
//    private Path penPath;
//    private String penPaintIdTime;
//

    /**
     * 获取比例换算后的x
     *
     * @param x x值
     * @return
     */
    public float getRealX(float x) {
        return x * screenWidth / 1000;
    }

    /**
     * 获取比例换算过的y
     *
     * @param y y值
     * @return
     */
    public float getRealY(float y) {
        return y * screenHeight / 1000;
    }


}
