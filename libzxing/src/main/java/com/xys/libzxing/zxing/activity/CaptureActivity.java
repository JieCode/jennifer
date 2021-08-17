

/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xys.libzxing.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.xys.libzxing.R;
import com.xys.libzxing.zxing.camera.CameraManager;
import com.xys.libzxing.zxing.decode.DecodeThread;
import com.xys.libzxing.zxing.utils.BeepManager;
import com.xys.libzxing.zxing.utils.CaptureActivityHandler;
import com.xys.libzxing.zxing.utils.InactivityTimer;
import com.xys.libzxing.zxing.utils.ScanView;
import com.xys.libzxing.zxing.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;

	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;
	private ScanView scanView;

	private ImageView ivBack, ivCapture;
	private Uri uritempFile;

	private String photo_path;

	private Bitmap scanBitmap;

	private Rect mCropRect = null;
	private boolean isHasSurface = false;

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	private final int IMAGE_CODE = 0;

	private final int IMAGE_CAIJIAN = 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);

		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);
		scanView = (ScanView) findViewById(R.id.sv_line);
		ivBack = (ImageView) findViewById(R.id.iv_left);
		ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ivCapture = (ImageView) findViewById(R.id.iv_right);
		ivCapture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent innerIntent = new Intent(Intent.ACTION_PICK);
				innerIntent.setType("image/*");//相片类型
				startActivityForResult(innerIntent, IMAGE_CODE);
//				Intent innerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//				innerIntent.setType("image/*");//相片类型
//				Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//				startActivityForResult(wrapperIntent, IMAGE_CAIJIAN);
			}
		});
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
				.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 30.0f, Animation.RELATIVE_TO_PARENT,
				80.0f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);
//		scanView.startAnim();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case IMAGE_CAIJIAN:
				//裁剪
				if (data == null) {
					Toast.makeText(this, "请选择正确二维码自动扫描", Toast.LENGTH_SHORT).show();
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case IMAGE_CODE:
				handleAlbumPic(data);
//				String[] proj = {MediaStore.Images.Media.DATA};
//				// 获取选中图片的路径
//				Cursor cursor = getContentResolver().query(data.getData(),
//						proj, null, null, null);
//
//				if (cursor.moveToFirst()) {
//
//					int column_index = cursor
//							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//					photo_path = cursor.getString(column_index);
//					if (photo_path == null) {
//						photo_path = Utils.getRealFilePath(getApplicationContext(),
//								data.getData());
//						Log.i("123path  Utils", photo_path);
//					}
//					Log.i("123path", photo_path);
//
//					if (photo_path == null) {
//						photo_path = Utils.getRealFilePath(getApplicationContext(),
//								data.getData());
//						Log.i("123path  Utils", photo_path);
//					}
//					Log.i("123path", photo_path);
//					new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//
//							Result result = scanningImage(photo_path);
//							// String result = decode(photo_path);
//							if (result == null) {
//								Looper.prepare();
//								Toast.makeText(getApplicationContext(), "图片格式有误", Toast.LENGTH_SHORT)
//										.show();
//								Looper.loop();
//							} else {
//								Log.i("123result", result.toString());
//								// Log.i("123result", result.getText());
//								// 数据返回
//								String recode = recode(result.toString());
//								Intent data = new Intent();
//								data.putExtra("result", recode);
//								setResult(300, data);
//								finish();
//							}
//						}
//					}).start();
//
//				}
//
//				cursor.close();


//				Bundle extras = data.getExtras();
//				Bitmap photo = (Bitmap) extras.get("data");
//				Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
//						photo, null, null));
//				File file = new File(Utils.getRealFilePath(this, uri));
//				photo_path =file.getPath();

//				if (photo_path == null) {
//					photo_path = Utils.getRealFilePath(getApplicationContext(),
//							data.getData());
//					Log.i("123path  Utils", photo_path);
//				}
//				Log.i("123path", photo_path);
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//
//						Result result = scanningImage(photo_path);
//						// String result = decode(photo_path);
//						if (result == null) {
//							Looper.prepare();
//							Toast.makeText(getApplicationContext(), "图片格式有误", Toast.LENGTH_SHORT)
//									.show();
//							Looper.loop();
//						} else {
//							Log.i("123result", result.toString());
//							// Log.i("123result", result.getText());
//							// 数据返回
//							String recode = recode(result.toString());
//							Intent data = new Intent();
//							data.putExtra("result", recode);
//							setResult(300, data);
//							finish();
//						}
//					}
//				}).start();


//				String[] proj = {MediaStore.Images.Media.DATA};
//				// 获取选中图片的路径
//				Cursor cursor = this.getContentResolver().query(data.getData(),
//						proj, null, null, null);
//				if (cursor.moveToFirst()) {


//				Bitmap scanBitmap = null;
//				try {
//					scanBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				if (scanBitmap == null) {
//					Toast.makeText(this, "请选择正确二维码自动扫描", Toast.LENGTH_SHORT).show();
//					return;
//				}
////				scanBitmap = (Bitmap) data.getExtras().get("data");
//				LuminanceSource source1 = new PlanarYUVLuminanceSource(
//						rgb2YUV(scanBitmap), scanBitmap.getWidth(),
//						scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(),
//						scanBitmap.getHeight(), true);
//				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
//						source1));
//				MultiFormatReader reader1 = new MultiFormatReader();
//				Result result1;
//				try {
//					result1 = reader1.decode(binaryBitmap);
//					String content = result1.getText();
//					Intent resultIntent = new Intent();
//					Bundle bundle = new Bundle();
//					bundle.putString("result", content);
//					resultIntent.putExtras(bundle);
//					this.setResult(RESULT_OK, resultIntent);
//					CaptureActivity.this.finish();
//				} catch (Exception e1) {
//					Toast.makeText(this, "无效的二维码", Toast.LENGTH_SHORT).show();
//					e1.printStackTrace();
//				}

//				}
//				cursor.close();
				break;

		}


	}

	private void handleAlbumPic(Intent data) {
		//获取选中图片的路径
		final Uri uri = data.getData();


		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Result result = scanningImage(uri);
				if (result != null) {
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("result" ,result.getText());
					resultIntent.putExtras(bundle);
					CaptureActivity.this.setResult(RESULT_OK, resultIntent);
					finish();
				} else {
					Toast.makeText(CaptureActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public Result scanningImage(Uri uri) {
		if (uri == null) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码
		scanBitmap = Utils.decodeUri(this, uri, 500, 500);
		int[] data = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
		scanBitmap.getPixels(data, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(),scanBitmap.getHeight(),data);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		//XY 是裁剪框的大小
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
//		intent.putExtra("noFaceDetection", true);
		uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		//裁剪后返回
		startActivityForResult(intent, IMAGE_CODE);
	}

	protected Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {

			return null;

		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		hints.put(DecodeHintType.PURE_BARCODE, "true");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		int[] data = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
		scanBitmap.getPixels(data, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(), scanBitmap.getHeight(), data);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {

			return reader.decode(bitmap1, hints);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private String recode(String str) {
		String formart = "";

		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
					.canEncode(str);
			if (ISO) {
				formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
				Log.i("1234      ISO8859-1", formart);
			} else {
				formart = str;
				Log.i("1234      stringExtra", str);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formart;
	}


	public byte[] rgb2YUV(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

		int len = width * height;
		byte[] yuv = new byte[len * 3 / 2];
		int y, u, v;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int rgb = pixels[i * width + j] & 0x00FFFFFF;

				int r = rgb & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb >> 16) & 0xFF;

				y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
				u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
				v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

				y = y < 16 ? 16 : (y > 255 ? 255 : y);
				u = u < 0 ? 0 : (u > 255 ? 255 : u);
				v = v < 0 ? 0 : (v > 255 ? 255 : v);

				yuv[i * width + j] = (byte) y;
//                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
//                yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
			}
		}
		return yuv;
	}

	public Bitmap getBitmap() {
		Bitmap bitmap = null;
		if (TextUtils.isEmpty(photo_path)) {
			return bitmap;
		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		hints.put(DecodeHintType.TRY_HARDER, "true");
		hints.put(DecodeHintType.PURE_BARCODE, "true");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		bitmap = BitmapFactory.decodeFile(photo_path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		bitmap = BitmapFactory.decodeFile(photo_path, options);
		return bitmap;
	}


	@Override
	protected void onResume() {
		super.onResume();

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		handler = null;

		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		scanView.cancelAnim();
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 *
	 * @param rawResult The contents of the barcode.
	 * @param bundle    The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		inactivityTimer.onActivity();
		beepManager.playBeepSoundAndVibrate();

		Intent resultIntent = new Intent();
		bundle.putInt("width", mCropRect.width());
		bundle.putInt("height", mCropRect.height());
		bundle.putString("result", rawResult.getText());
		resultIntent.putExtras(bundle);
		this.setResult(RESULT_OK, resultIntent);
		CaptureActivity.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
			}

			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("Camera error");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


}

