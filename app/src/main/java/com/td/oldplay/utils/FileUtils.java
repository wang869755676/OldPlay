package com.td.oldplay.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtils {

	/**
	 * 判断sd卡是否可以用
	 */
	private static boolean isSDAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static String getCamerPath(Context cxt, String fileName) {
		String path = "";
		if (isSDAvailable()) {
			if (Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) != null) {
				path = Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES).getAbsolutePath()
						+ File.separator + fileName;
			} else {
				path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator + fileName;
			}

		}else{
			path=cxt.getFilesDir().getAbsolutePath()+ File.separator+fileName;
		}
		return path;
	}
}