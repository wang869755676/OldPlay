package com.td.oldplay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.td.oldplay.contants.MContants;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class AppUtils {
	/**
	 * 匹配电话号码
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkPhone(String phone) {
		Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
		Matcher matcher = pattern.matcher(phone);

		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 匹配email
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkEmail(String input) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(input);
		return m.find();
	}

	/**
	 * 检查URL访问地址，去掉不合法的换行符、制表符等
	 * 
	 * @param url
	 * @return
	 */
	public static String checkURL(String url) {
		String dest = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(url);
		dest = m.replaceAll("");
		return dest;
	}

	public static double division(long a, long b) {
		double result = 0;
		if (b != 0) {
			result = a / b;
			BigDecimal big = new BigDecimal(result);
			result = big.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else {
			result = 0;
		}
		return result;
	}

	public static double getMax(double[] attr) {
		double max = attr[0];
		for (int i = 0; i < attr.length; i++) {
			if (attr[i] > max) {
				max = attr[i];
			}
		}
		return max;
	}

	/**
	 * 
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		list.clear();
		list.addAll(newList);
	}

	/**
	 * 检查url里是否存在str数组所包含的字符
	 * 
	 * @return
	 */
	public static boolean checkUrlContent(String url, String... str) {
		for (int i = 0; i < str.length; i++) {
			if (url.contains(str[i])) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检测字符串中是否包含汉字
	 * 
	 * @param sequence
	 * @return
	 */
	public static boolean checkChinese(String sequence) {
		final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
		boolean result = false;
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(sequence);
		result = matcher.find();
		return result;
	}

	/**
	 * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
	 * 
	 * @param sequence
	 * @return
	 */
	public static boolean checkNickname(String sequence) {
		final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(sequence);
		return !matcher.find();
	}

	/**
	 * 检测某程序是否安装
	 */
	public static boolean isInstalledApp(Context context, String packageName) {
		Boolean flag = false;
		try {
			PackageManager pm = context.getPackageManager();
			// 获取所有已安装程序的包信息
			List<PackageInfo> pinfo = pm.getInstalledPackages(0);
			for (PackageInfo pkg : pinfo) {
				// 当找到了名字和该包名相同的时候，返回
				if ((pkg.packageName).equals(packageName)) {
					return flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 安装.apk文件
	 * 
	 * @param context
	 */
	public void install(Context context, String fileName) {
		if (TextUtils.isEmpty(fileName) || context == null) {
			return;
		}
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(fileName)),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 *
	 * @return 返回像素值
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 *
	 * @return 返回dp值
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）
	 *
	 * @param ctx
	 * @return
	 */
	public static double getScreenPhysicalSize(Activity ctx) {
		DisplayMetrics dm = new DisplayMetrics();
		ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2)
				+ Math.pow(dm.heightPixels, 2));
		return diagonalPixels / (160 * dm.density);
	}

	/**
	 * 判断是否是平板（官方用法）
	 *
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 获取应用程序下所有Activity
	 *
	 * @param ctx
	 * @return
	 */
	public static ArrayList<String> getActivities(Context ctx) {
		ArrayList<String> result = new ArrayList<String>();
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setPackage(ctx.getPackageName());
		for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(
				intent, 0)) {
			result.add(info.activityInfo.name);
		}
		return result;
	}

	/**
	 * 文件夹排序（先文件夹排序，后文件排序）
	 *
	 * @param files
	 */
	public static void sortFiles(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File lhs, File rhs) {
				// 返回负数表示o1 小于o2，返回0 表示o1和o2相等，返回正数表示o1大于o2。
				boolean l1 = lhs.isDirectory();
				boolean l2 = rhs.isDirectory();
				if (l1 && !l2)
					return -1;
				else if (!l1 && l2)
					return 1;
				else {
					return lhs.getName().compareTo(rhs.getName());
				}
			}
		});
	}

	/**
	 * 解压一个压缩文档 到指定位置
	 *
	 * @param zipFileString
	 *            压缩包的名字
	 * @param outPathString
	 *            指定的路径
	 * @throws Exception
	 */
	public static void UnZipFolder(String zipFileString, String outPathString)
			throws Exception {
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(
				new java.io.FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";
		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();
			if (zipEntry.isDirectory()) {
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				java.io.File folder = new java.io.File(outPathString
						+ java.io.File.separator + szName);
				folder.mkdirs();
			} else {
				java.io.File file = new java.io.File(outPathString
						+ java.io.File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				java.io.FileOutputStream out = new java.io.FileOutputStream(
						file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}
		inZip.close();
	}





	/**
	 * 打开系统分享
	 * 
	 * @param context
	 * @param content
	 */
	public static void ShareText(Context context, String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Cnblogs");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent,
				((Activity) context).getTitle()));

	}

	/**
	 * 打开系统分享
	 * 
	 * @param context
	 * @param content
	 */
	public static void SharePicture(Context context, String content,
                                    String picture) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/png");
		intent.putExtra(Intent.EXTRA_STREAM, picture);
		intent.putExtra(Intent.EXTRA_SUBJECT, "Cnblogs");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent,
				((Activity) context).getTitle()));
	}

	/**
	 * 展开全部数据
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(
				listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LinearLayout.LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	/**
	 * 剪裁，打开相册
	 * 
	 * @param activity
	 */
	public static void startAlbum(Activity activity) {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			activity.startActivityForResult(intent, MContants.LAUNCH_DCIM_CODE);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			try {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				activity.startActivityForResult(intent,
						MContants.LAUNCH_DCIM_CODE);
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 剪裁，打开相机
	 * 
	 * @param activity
	 */
	public static void startCapture(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(),
				MContants.TEMP_FILE_PATH)));
		activity.startActivityForResult(intent, MContants.LAUNCH_CAMERA_CODE);
	}

	/**
	 * 是否有SD卡
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressLint("DefaultLocale")
	public static String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}
	
	public static float formatStringNumber(String number) {
		float fnumber = Float.parseFloat(number);
		BigDecimal b = new BigDecimal(fnumber);
		float result = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		return result;
	}
	
	public static float formatNumber(float number) {
		BigDecimal b = new BigDecimal(number);
		float result = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		return result;
	}
	
	/**
	 * login
	 * @param mContext
	 */
	public static void goToLogin(Context mContext) {
		//Intent intent = new Intent(mContext, LoginActivity.class);
		//mContext.startActivity(intent);
	}
	
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		// cmb.setText(content.trim());//SDK 11 之后已废弃
		cmb.setPrimaryClip(ClipData.newPlainText(null, content));
		if (cmb.hasPrimaryClip()) {
			cmb.getPrimaryClip().getItemAt(0).getText();
		}
	}
	
	public static String paste(Context context) {
		// 得到剪贴板管理器
		String resultStr = "";
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		//resultStr = cmb.getText().toString().trim(); //SDK 11 之后已废弃
		if (cmb.hasPrimaryClip()) {
			resultStr = (String) cmb.getPrimaryClip().getItemAt(0).getText();
		}
		return resultStr;
	}
}
