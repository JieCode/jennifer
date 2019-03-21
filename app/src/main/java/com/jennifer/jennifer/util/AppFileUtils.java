package com.jennifer.jennifer.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 文件工具类
 */
public class AppFileUtils {

    public static final String SIMPLE_ROOT_PATH = "jennifer/";
    public static final String ROOT_PATH = "jennifer";
    public static final String IMAGE_PATH = "image";
    public static final String DB_PATH = "db";
    public static final String LOG_PATH = "log";
    public static final String UPDATE_PATH = "update";
    public static final String UPLOAD_COMPRESS_PATH = "upload_compress";

    public static final String CACHE = getMyDefaultCacheabsolute_dir("cache");

    public static final String REPLAY = "replay";
    public static final String REPLAY_AUDIO = "replay/audio";
    public static final String REPLAY_FILE = "replay/file";


    /**
     * 默认路径， /mnt/sdcard/wutong_tutorial/  如果absolute_dir 不为空或空字符串， 那默认路径为 /mnt/sdcard/wutong_tutorial/(absolute_dir)
     * @param dir 目录
     * @return 返回路径字符串
     */
    public static String getMyDefaultCacheabsolute_dir(String dir) {
        String absolute_dir;
        // 保证目录名称正确
        if (dir != null) {
            if (!dir.equals("")) {
                if (!dir.endsWith("/")) {
                    dir = dir + "/";
                }
            }
        }

        String joyrun_default = "/jennifer/";

        if (isSDCardExist()) {
            absolute_dir = Environment.getExternalStorageDirectory().toString() + joyrun_default + dir;
        } else {
            absolute_dir = Environment.getExternalStorageDirectory().toString() + joyrun_default + dir;
        }

        File f = new File(absolute_dir);
        if (!f.exists()) {
            f.mkdirs();
        }
        return absolute_dir;
    }

    /**
     * sd卡是否存在
     * @return true存在 false不存在
     */
    public static boolean isSDCardExist() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return false;
        return true;
    }

    public static String getReplayPath(){
        return getMyDefaultCacheabsolute_dir(REPLAY);
    }

    public static String getReplayAudioPath(){
        return getMyDefaultCacheabsolute_dir(REPLAY_AUDIO);
    }

    public static String getReplayFilePath(){
        return getMyDefaultCacheabsolute_dir(REPLAY_FILE);
    }

    /**
     * 获取日志文件路径
     * @return 日志文件路径
     */
    public static String getLogFilePath(){
        return getMyDefaultCacheabsolute_dir("log");
    }

    /** 把Uri转化成文件路径 */
    public static String uri2filePath(Activity context, Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.managedQuery(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }

    // 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
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
                        Long.valueOf(id));
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
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 测试 --- 将数据保存到本地文件
     * @param response 目标数据
     */
    public static void saveFile(Object response, String path) throws IOException {
        String res = (String) response;
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(res.getBytes("utf-8"));
        fos.flush();
        fos.close();
    }


    /**
     * 复制文件到指定位置
     * @param fromFile 源文件
     * @param toFile 目标文件
     * @param rewrite 是否重写
     */
    public static boolean copyfile(File fromFile, File toFile, Boolean rewrite ) {

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

            fosfrom = new java.io.FileInputStream(fromFile);

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

        }finally {
            if(fosfrom != null){
                try {
                    fosfrom.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }

            if(fosto != null){
                try {
                    fosto.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * 读取指定的文本 utf-8
     * @param filePath 文件路径
     * @return 文本内容
     */
    public static String getFileContent(String filePath){
        File file = new File(filePath);
        String result = "";
        if(!file.exists()){
            return result;
        }
        if(!file.canRead()){
            return result;
        }
        FileInputStream is = null;
        BufferedReader reader = null;
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
            StringBuffer sb = new StringBuffer("");
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取文件的后缀名
     * @param fileName 文件名
     * @return 后缀
     */
    public static String getFileExt(String fileName) {

        if (android.text.TextUtils.isEmpty(fileName)) {

            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
    }

    /**
     * 创建一个apk更新的文件 名称如 update_v_2.0_app.apk
     * @param versionName 版本号
     * @return 返回一个file文件
     */
    public static File create(String versionName) {
        String cacheDir = AppFileUtils.getMyDefaultCacheabsolute_dir(UPDATE_PATH);
        return new File(cacheDir,"update_v_" + versionName + "app.apk");
    }


    /**
     * 获取文件夹大小
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(java.io.File file){

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     * @param deleteThisPath 是否删除该路径
     * @param filePath 文件路径
     * @return 无
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 格式化单位
     * @param length 大小
     * @return 返回string文本
     */
    public static String getFormatSize(long length) {
        if (length >> 30 > 0L) {
            float sizeGb = Math.round(10.0F * (float) length / 1.073742E+009F) / 10.0F;
            return sizeGb + " GB";
        }
        if (length >> 20 > 0L) {
            return formatSizeMb(length);
        }
        if (length >> 9 > 0L) {
            float sizekb = Math.round(10.0F * (float) length / 1024.0F) / 10.0F;
            return sizekb + " KB";
        }
        return length + " B";
    }

    /**
     * 转换成Mb单位
     *
     * @param length 大小
     * @return
     */
    public static String formatSizeMb(long length) {
        float mbSize = Math.round(10.0F * (float) length / 1048576.0F) / 10.0F;
        return mbSize + " MB";
    }

    public static boolean hasExtentsion(String filename) {
        int dot = filename.lastIndexOf('.');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            return true;
        } else {
            return false;
        }
    }

    static InputStream in;
    static BufferedReader br;

    public static Intent openFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return null;
		/* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
//		end = end.substring(0, end.length() - 1);
		/* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")||end.equals("pptx")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls")||end.equals("xlsx")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc")||end.equals("docx")||end.equals("rtf")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(param);
        if (file.exists()) {
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }


    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


}
