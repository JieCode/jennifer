package com.jennifer.jennifer.ui.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jennifer.jennifer.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextActivity extends AppCompatActivity {

    private EditText edit;
    private Context context;
    private TextView tvPicture;
    private int maxImageWidth;
    private int maxImageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        context = this;
        initView();
    }

    private void initView() {
        edit = findViewById(R.id.edit);
        tvPicture = findViewById(R.id.tv_picture);
        maxImageWidth = getLayoutDisplayMetricsWidth(this) * 4 / 5;
        maxImageHeight = getLayoutDisplayMetricsHeight(this) / 3;
        initEditText();
        tvPicture.setOnClickListener(v -> {
            // 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片的地址
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CAMERA);
        });
    }

    private void initEditText() {
        //input是获取将被解析的字符串
        String input = "第一张图是本地的（换行）：\n" +
                "<img src=\"/storage/emulated/0/goots/micro/wares/得无人任务发热福尔 一月又一月晕.jpg\"/>" +
                "第二张图是网络的（不换行）：" +
                "<img src=\"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2018-01-04%2F5a4dea4b8ee92.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1670138977&t=0976ef98758adad1db050d81866b113e\"/>" +
                "第三张图是网络的（不换行）：" +
                "<img src=\"https://img1.baidu.com/it/u=1055222507,2630881149&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500\"/>" +
                "第四张图是网络的（不换行）：" +
                "<img src=\"https://img0.baidu.com/it/u=1996361062,3232650694&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800\"/>" +
                "没了就两张图，试试行不行";
        edit.setText(input);
        if (!TextUtils.isEmpty(edit.getText().toString())) {
            edit.setSelection(edit.getText().toString().length() - 1);
        }
        //将图片那一串字符串解析出来,即<img src=="xxx" />
        Pattern p = Pattern.compile("<img src=\".*?\"/>");
        Matcher m = p.matcher(input);
        //使用SpannableString了，这个不会可以看这里哦：http://blog.sina.com.cn/s/blog_766aa3810100u8tx.html#cmt_523FF91E-7F000001-B8CB053C-7FA-8A0
        SpannableString spannable = new SpannableString(input);
        while (m.find()) {
            //这里s保存的是整个式子，即<img src="xxx"/>，start和end保存的是下标
            String s = m.group();
            int start = m.start();
            int end = m.end();
            //利用spannableString和ImageSpan来替换掉这些图片
            getSmallBitmap(s, (replace, bitmap) -> {
                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                edit.setText(spannable);
            });
        }
    }

    private void getSmallBitmap(String replace, OnBitmapReadyListener listener) {
        //url 是去掉<img src=""/>的中间的图片路径
        String url = replace.replaceAll("<img src=\"|\"/>", "").trim();
        if (url.startsWith("http")) {
            Glide.with(context)
                    .load(url)
                    //如果传入的url是http://..... .gif(尾缀是.gif)
                    //需要添加 .asBitmap() 方法处理,其他格式的图片不需要添加
                    .asBitmap()
                    .placeholder(R.drawable.ic_launcher)
                    .dontAnimate()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            if (resource != null) {
                                try {
                                    Matrix matrix = new Matrix();
                                    double scale = getScale(resource);
                                    int width = (int) ((double) resource.getWidth() * scale);
                                    int height = (int) ((double) resource.getHeight() * scale);
                                    Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, width, height, matrix, false);
                                    if (bitmap != null) {
                                        if (listener != null) {
                                            listener.onBitmap(replace, bitmap);
                                        }
                                    }
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (listener != null) {
                                listener.onBitmap(replace, resource);
                            }
                        }

                    });
        } else {
            if (listener != null) {
                listener.onBitmap(replace, getBitmap(url));
            }
        }

    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public Bitmap getBitmap(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap resource = BitmapFactory.decodeFile(filePath, options);
        //重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
        options.inJustDecodeBounds = false;
        try {
            Matrix matrix = new Matrix();
            float scale = Float.parseFloat(String.valueOf(getScale(resource)));
            matrix.postScale(scale ,scale);
            Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, false);
            if (bitmap != null) {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resource;
    }

    /**
     * 缩放比例
     * @param bitmap
     * @return
     */
    private double getScale(Bitmap bitmap) {
        double scale = 1.00;
        if (bitmap == null) {
            return scale;
        }
        try {
            if (bitmap.getWidth() > maxImageWidth && bitmap.getHeight() > maxImageHeight) {
                scale = Math.min(((double) maxImageWidth / (double) bitmap.getWidth()), ((double) maxImageHeight / (double) bitmap.getHeight()));
            } else if (bitmap.getWidth() > maxImageWidth) {
                scale = (double) maxImageWidth / (double) bitmap.getWidth();
            } else if (bitmap.getHeight() > maxImageHeight) {
                scale = (double) maxImageHeight / (double) bitmap.getHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (scale>0) {
            return Math.min(1.00, scale);
        }
        return 1.00;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }

    interface OnBitmapReadyListener {
        void onBitmap(String replace, Bitmap bitmap);
    }


    private static final int REQUEST_CAMERA = 0x22;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = "<img src=\"" + getRealPathFromUri(context, uri) + "\"/>";
                    if (!TextUtils.isEmpty(path)) {
                        SpannableString spannable = new SpannableString(path);
                        //利用spannableString和ImageSpan来替换掉这些图片
                        getSmallBitmap(path, new OnBitmapReadyListener() {
                            @Override
                            public void onBitmap(String replace, Bitmap bitmap) {
                                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                                spannable.setSpan(imageSpan, 0, path.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                int index = edit.getSelectionStart();
                                if (index < 0 || index > edit.getText().toString().length()) {
                                    edit.getEditableText().append(spannable);
                                } else {
                                    edit.getEditableText().insert(index, spannable);
                                }
                            }
                        });

                    }
                }

            }
        }
    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getLayoutDisplayMetricsWidth(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getLayoutDisplayMetricsHeight(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }
}