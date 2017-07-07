package com.td.oldplay.utils;

import android.content.Context;

import java.io.File;
import java.text.DecimalFormat;

public class CacheClearUtil {
	/**
	 * 返回兆单位
	 * 
	 * @param context
	 * @return
	 */
	public static long getCachSize(Context context) {
		File file1 = context.getCacheDir();
		File file2 = context.getExternalCacheDir();
		long size = 0;
		if (file1 != null) {
			try {
				size += getFileSize(file1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (file2 != null) {
			try {
				size += getFileSize(file2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return size;
	}

	/**
	 * 清除缓存
	 * 
	 * @param context
	 */
	public static void clearCach(Context context) {
		File file1 = context.getCacheDir();
		File file2 = context.getExternalCacheDir();
		if (file1 != null) {
			deleteFile(file1);
		}
		if (file2 != null) {
			deleteFile(file2);
		}
	}

	// 递归
	private static long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String formetFileSize(long fileS) {// 转换文件大小
		if (fileS == 0)
			return "0B";
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 文件删除
	 * 
	 * @param file
	 */
	private static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
			// 如果它是一个目录
			else if (file.isDirectory()) {
				// 声明目录下所有的文件 files[];
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
		}
	}

}
