package com.xys.libzxing.zxing.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Utils
 *
 * @author Heguanxing  2018/8/9.
 *         Function Describe
 * @modify Heguanxing  2018/8/9.
 * Function Describe
 */


public class Utils {

	public static String recode(String str) {
		String format = "";

		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
					.canEncode(str);
			if (ISO) {
				format = new String(str.getBytes("ISO-8859-1"), "GB2312");
				Log.i("1234      ISO8859-1", format);
			} else {
				format = str;
				Log.i("1234      stringExtra", str);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return format;
	}

	public static String getRealFilePath(Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}

				}
				cursor.close();
			}
			if (data == null) {
				data = getImageAbsolutePath(context, uri);
			}

		}
		return data;
	}

	public static Uri getUri(final String filePath) {
		return Uri.fromFile(new File(filePath));
	}

	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 *
	 * @param context
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@TargetApi(19)
	public static String getImageAbsolutePath(Context context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *                The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *                The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *                The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *                The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}



	/**
	 * 读取一个缩放后的图片，限定图片大小，避免OOM
	 *
	 * @param uri       图片uri，支持“file://”、“content://”
	 * @param maxWidth  最大允许宽度
	 * @param maxHeight 最大允许高度
	 * @return 返回一个缩放后的Bitmap，失败则返回null
	 */
	public static Bitmap decodeUri(Context context, Uri uri, int maxWidth, int maxHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; //只读取图片尺寸
		readBitmapScale(context, uri, options);

		//计算实际缩放比例
		int scale = 1;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			if ((options.outWidth / scale > maxWidth &&
					options.outWidth / scale > maxWidth * 1.4) ||
					(options.outHeight / scale > maxHeight &&
							options.outHeight / scale > maxHeight * 1.4)) {
				scale++;
			} else {
				break;
			}
		}

		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;//读取图片内容
		options.inPreferredConfig = Bitmap.Config.RGB_565; //根据情况进行修改
		Bitmap bitmap = null;
		try {
			bitmap = readBitmapData(context, uri, options);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static void readBitmapScale(Context context, Uri uri, BitmapFactory.Options options) {
		if (uri == null) {
			return;
		}
		String scheme = uri.getScheme();
		if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
				ContentResolver.SCHEME_FILE.equals(scheme)) {
			InputStream stream = null;
			try {
				stream = context.getContentResolver().openInputStream(uri);
				BitmapFactory.decodeStream(stream, null, options);
			} catch (Exception e) {
				Log.w("readBitmapScale", "Unable to open content: " + uri, e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						Log.e("readBitmapScale", "Unable to close content: " + uri, e);
					}
				}
			}
		} else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
			Log.e("readBitmapScale", "Unable to close content: " + uri);
		} else {
			Log.e("readBitmapScale", "Unable to close content: " + uri);
		}
	}

	private static Bitmap readBitmapData(Context context, Uri uri, BitmapFactory.Options options) {
		if (uri == null) {
			return null;
		}
		Bitmap bitmap = null;
		String scheme = uri.getScheme();
		if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
				ContentResolver.SCHEME_FILE.equals(scheme)) {
			InputStream stream = null;
			try {
				stream = context.getContentResolver().openInputStream(uri);
				bitmap = BitmapFactory.decodeStream(stream, null, options);
			} catch (Exception e) {
				Log.e("readBitmapData", "Unable to open content: " + uri, e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						Log.e("readBitmapData", "Unable to close content: " + uri, e);
					}
				}
			}
		} else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
			Log.e("readBitmapData", "Unable to close content: " + uri);
		} else {
			Log.e("readBitmapData", "Unable to close content: " + uri);
		}
		return bitmap;
	}


}
