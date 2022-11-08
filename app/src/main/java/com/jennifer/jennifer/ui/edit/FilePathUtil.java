package com.jennifer.jennifer.ui.edit;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.jennifer.jennifer.application.JenniferApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author jingjie
 * @date :2020/12/2 16:23
 * TODO:获取文件存储路径
 */
public class FilePathUtil {

    /**
     * 默认路径， /mnt/sdcard/wutong_tutorial/  如果absolute_dir 不为空或空字符串， 那默认路径为 /mnt/sdcard/wutong_tutorial/(absolute_dir)
     * 通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * 通过Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     * 如果使用上面的方法，当你的应用在被用户卸载后，SDCard/Android/data/你的应用的包名/ 这个目录下的所有文件都会被删除，不会留下垃圾信息。
     * 而且上面二个目录分别对应 设置->应用->应用详情里面的”清除数据“与”清除缓存“选项
     * 如果要保存下载的内容，就不要放在以上目录下
     *
     * @param dir 目录
     * @return 返回路径字符串
     */
    private static String getMyDefaultDir(String dir) {
        String absoluteDir;
        // 保证目录名称正确
        if (dir != null) {
            if (!"".equals(dir)) {
                if (!dir.endsWith("/")) {
                    dir = dir + "/";
                }
            }
        }

        absoluteDir = Objects.requireNonNull(JenniferApplication.getContext().getExternalFilesDir(dir)).toString();

        File f = new File(absoluteDir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return absoluteDir;
    }

    /**
     * sd卡是否存在
     *
     * @return true存在 false不存在
     */
    public static boolean isSDCardExist() {
        return !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Environment.isExternalStorageRemovable();
    }

    /**
     * 获取文件的后缀名
     *
     * @param filePath 文件名
     * @return 后缀
     */
    public static String getFileExt(String filePath) {
        if (TextUtils.isEmpty(filePath)) {

            return "";
        }
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件名
     *
     * @param filePath 文件名带后缀
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 复制文件到指定位置
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @param rewrite  是否重写
     */
    public static boolean copyfile(File fromFile, File toFile, Boolean rewrite) {

        if (!fromFile.exists()) {
            return false;
        }

        if (!fromFile.isFile()) {
            return false;
        }

        if (!fromFile.canRead()) {
            return false;
        }

        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }

        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        FileInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {

            fosfrom = new FileInputStream(fromFile);

            fosto = new FileOutputStream(toFile);

            byte bt[] = new byte[8192];

            int c;

            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
            fosto.flush();

            return true;

        } catch (Exception ex) {

//            MyLog.e("readfile", ex.getMessage());

            return false;

        } finally {
            if (fosfrom != null) {
                try {
                    fosfrom.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }

            if (fosto != null) {
                try {
                    fosto.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * SDk 版本大于4.4时，Uri 转换成真实路径的方法
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi") //作用仅仅是屏蔽android lint错误，所以在方法中还要判断版本做不同的操作.
    public static String getPathByUrikitkat(final Context context, final Uri uri) {
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.parseLong(id));
                    return getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(uri)) {// MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && isAndroidNUri(uri)) {
                    final String[] split = uri.getPath().split("/");
                    StringBuilder path = new StringBuilder("");
                    boolean begin = false;
                    for (String s : split) {
                        if (!begin && s.equals("storage")) {
                            begin = true;
                        }
                        if (begin) {
                            path.append("/");
                            path.append(s);
                        }
                    }
                    return path.toString();
                }
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = new String[]{MediaStore.Images.Media.DATA};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * content获取文件路径
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getFilePathFromURI(Context context, Uri contentUri) {
        File rootDataDir = context.getFilesDir();
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(rootDataDir + File.separator + fileName);
            copyFile(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) {
            return null;
        }
        String fileName = null;
        String path = uri.getPath();
        if (!TextUtils.isEmpty(path)) {
            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                fileName = path.substring(cut + 1);
            }
            return fileName;
        }
        return null;
    }

    public static void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) {
                return;
            }
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return count;
    }

    private static boolean isAndroidNUri(Uri uri) {
        return (JenniferApplication.getContext().getApplicationInfo().packageName + ".provider").equals(uri.getAuthority());
    }

    /**
     * 日志保存路径
     */
    public static String getLogPath() {
        return getMyDefaultDir("log");
    }

    /**
     * 临时存储路径
     */
    public static String getTempPath() {
        return getMyDefaultDir("temp");
    }

    /**
     * 图片保存路径
     */
    public static String getImagePath() {
        return getMyDefaultDir(".image");
    }

    /**
     * 安装包保存路径
     */
    public static String getApkPath() {
        return getMyDefaultDir("apk");
    }

    /**
     * 回放保存路径
     */
    public static String getReplayPath() {
        return getMyDefaultDir("replay");
    }
    /**
     * 创建一个apk更新的文件 名称如 update_v2.1.0.apk
     *
     * @param versionName 版本号
     * @return 返回一个file文件
     */
    public static File create(String versionName) {
        return new File(FilePathUtil.getApkPath(), "update_" + versionName + ".apk");
    }
}
