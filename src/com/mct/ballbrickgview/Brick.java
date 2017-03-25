package com.mct.ballbrickgview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * ש����
 * 
 * @author u
 * 
 */
class Brick {

	/**
	 * ש������
	 */
	Rect rect;

	/**
	 * ש����ɫ
	 */
	int color;

	/**
	 * ש���Ƿ���ڵı�־
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
	 * ש�����
	 * 
	 * @param canvas
	 * @param paint
	 */
	void draw(Canvas canvas, Paint paint) {
		paint.setColor(color);
		// ���û�����ʽΪʵ��
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(rect, paint);
		// ���û�����ʽΪ���ģ�����ש���Ե��
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(0xff999999);
		paint.setShadowLayer(0, 0, 0, 0);
		canvas.drawRect(rect, paint);
	}

}
