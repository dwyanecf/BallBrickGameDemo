package com.mct.ballbrickgview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

class Board {
	/**
	 * µµ°åÇøÓò
	 */
	Rect rect;
	
	/**
	 * µµ°åµÄÑÕÉ«
	 */
	int color;
	
	
	Board(){
		
	}

	public Board(Rect rect, int color) {
		super();
		this.rect = rect;
		this.color = color;
	}
	
	public Board(int left, int top, int right, int bottom, int color) {
		super();
		this.rect = new Rect(left, top, right, bottom);
		this.color = color;
	}
	

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * »­µµ°å
	 * @param canvas
	 * @param paint
	 */
	void draw(Canvas canvas, Paint paint){
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(rect, paint);
	}
	
	
}
