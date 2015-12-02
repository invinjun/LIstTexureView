package com.example.invinjun.listtexureviewdemo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class ImageUtils {

	private int screenWidth;

	/**
	 * ��ȡ��Ļ�ߴ�
	 */
	public ImageUtils(Activity context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		// Find screen dimensions
		screenWidth = displaymetrics.widthPixels;
	}

	public int getScaleHeight(int position, int height) {
		float scale = (float) screenWidth / 480;
		return (int) (/* height(position) */height * scale);
	}

	/**
	 * ��ȡˮӡ�����߶�
	 * 
	 * @param position
	 * @return
	 */
	public int getWaterMarkTop(LinearLayout waterMarkLayout, int height) {
		int top = waterMarkLayout.getHeight() - height/* height(position) */;
		if (top >= 480) {
			top = 480 - height/* height(position) */;
		}
		return top;
	}

	/**
	 * ��ȡˮӡͼƬ�Ŀ��
	 * 
	 * @param position
	 *            ˮӡ���б��е�����
	 * @return
	 */
	public int width(Bitmap[] water, int position) {
		return water[position].getWidth();
	}

	/**
	 * ��ȡˮӡͼƬ�ĸ߶�
	 * 
	 * @param position
	 *            ˮӡ���б��е�����
	 * @return
	 */
	public int height(Bitmap[] water, int position) {
		return water[position].getHeight();
	}

	public Bitmap resetBitmap(Bitmap bitmap) {

		Matrix matrix = new Matrix();

		float scale = (float) screenWidth / 480;

		matrix.postScale(scale, scale);

		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

	}
}
