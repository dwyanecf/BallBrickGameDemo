package com.mct.ballbrickgview;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * �м��ϰ���
 * @author Whunf
 *
 */
public class Obstacle {

	int left;
	int top;
	int right;
	int bottom;

	int offset = 4;

	public Obstacle(){

	}

	public Obstacle(int left, int top, int right, int bottom) {
		super();
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	/**
	 * �ϰ����ƶ�����
	 */
	public void move(){
		left += offset;
		right += offset;
	}

	/**
	 * ����ˮƽ������ײʱ�ı䷽��
	 */
	void hitHorizental() {
		offset *= -1;
	}

	/**
	 * ���ϰ���
	 * @param canvas
	 * @param paint
	 */
	void draw(Canvas canvas, Paint paint){
		paint.setColor(0x66ff0000);
//		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(left, top, right, bottom, paint);
	}
}
