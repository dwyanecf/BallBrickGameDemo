package com.mct.ballbrickgview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 砖块类
 * 
 * @author u
 * 
 */
class Brick {

	/**
	 * 砖块区域
	 */
	Rect rect;

	/**
	 * 砖块颜色
	 */
	int color;

	/**
	 * 砖块是否存在的标志
	 */
	boolean isExist;

	public Brick(Rect rect, int color, boolean isExist) {
		super();
		this.rect = rect;
		this.color = color;
		this.isExist = isExist;
	}

	public Brick(int left, int top, int right, int bottom, int color,
			boolean isExist) {
		super();
		this.rect = new Rect(left, top, right, bottom);
		this.color = color;
		this.isExist = isExist;
	}

	/**
	 * 砖块绘制
	 * 
	 * @param canvas
	 * @param paint
	 */
	void draw(Canvas canvas, Paint paint) {
		paint.setColor(color);
		// 设置画笔样式为实心
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(rect, paint);
		// 设置画笔样式为空心（画出砖块边缘）
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(0xff999999);
		paint.setShadowLayer(0, 0, 0, 0);
		canvas.drawRect(rect, paint);
	}

}
