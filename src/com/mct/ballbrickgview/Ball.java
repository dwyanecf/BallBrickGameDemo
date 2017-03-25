package com.mct.ballbrickgview;

import android.graphics.Canvas;
import android.graphics.Paint;

import static java.lang.Math.abs;

/**
 * ball object
 * 
 * @author dwyanecf
 * 
 */
class Ball {
	/**
	 * ball center x-aix
	 */
	float x;

	/**
	 * ball center y-aix
	 */
	float y;

	/**
	 * ball radius
	 */
	float r;

	/**
	 * 运动的x方向步长
	 */
	double x_offset = (int) (AnimationView.ScreenWidth *0.012)*(AnimationView.Ballspeed);

	/**
	 * 运动的y方向步长
	 */
	double y_offset = (int) (AnimationView.ScreenWidth *0.012)*(AnimationView.Ballspeed);

	/**
	 * ball color
	 */
	int color;

	Ball(float x, float y, float r, int color) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.color = color;
	}

	Ball() {
	}

	/**
	 * 球运动方法
	 */
	void move() {
		x += x_offset;
		y += y_offset;
	}

	/**
	 * 球在水平方向碰撞时改变方向
	 */
	void hitHorizental() {
		x_offset *= -1;
	}

	void MinusHorizental( double bias, double sup){
		x_offset = -abs(x_offset)*1.55*bias;
		y_offset=sup*sup*y_offset;
	}
	void PLusHorizental( double bias, double sup){
		x_offset = abs(x_offset)*1.55*bias  ;
		y_offset=sup*sup*y_offset;
	}
//	void MinusVertical(){ y_offset = -abs(y_offset) ;}
	/**
	 * 球在垂直方向碰撞时改变方向
	 */
	void hitVertical() {
		y_offset *= -1;
	}

	/**
	 * 球撞击到顶角时改变方向
	 */
	void hitCorner() {
		x_offset *= -1;
		y_offset *= -1;
	}

	/**
	 * 绘制球的方法
	 * @param canvas
	 * @param paint
	 */
	void draw(Canvas canvas, Paint paint) {
		paint.setColor(color);
		//设置画笔样式为实心
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setShadowLayer(0.2f, 0, 1, 0x99000000);
		canvas.drawCircle(x, y, r, paint);
	}

}
