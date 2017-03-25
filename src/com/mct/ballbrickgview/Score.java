package com.mct.ballbrickgview;



public class Score {

	private int X;	// X点
	private int Y;	// Y点
	private String ScoreStr;	// 分数数字
	private int Color;


	public Score(){
		super();
	}
	
	public Score(int x, int y, String scoreStr, int color) {
		super();
		X = x;
		Y = y;
		ScoreStr = scoreStr;
		Color = color;
	}
	
	
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public String getScoreStr() {
		return ScoreStr;
	}
	public void setScoreStr(String scoreStr) {
		ScoreStr = scoreStr;
	}
	
	
	public int getColor() {
		return Color;
	}

	public void setColor(int color) {
		this.Color = color;
	}
}
