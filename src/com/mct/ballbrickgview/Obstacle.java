package com.mct.ballbrickgview;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 中间障碍物
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
	 * 障碍物移动方法
	 */
	public void move(){
		left += offset;
		right += offset;
	}

	/**
	 * 球在水平方向碰撞时改变方向
	 */
	void hitHorizental() {
		offset *= -1;
	}

	/**
	 * 画障碍物
	 * @param canvas
	 * @param paint
	 */
	void draw(Canvas canvas, Paint paint){
		paint.setColor(0x66ff0000);
//		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(left, top, right, bottom, paint);
	}
}
