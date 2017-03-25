package com.mct.ballbrickgview;
import java.util.ArrayList;

import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import com.mct.ballbrickgamedemo.MainActivity;
import com.mct.ballbrickgamedemo.R;
import android.content.Context;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.media.AudioManager;
import android.media.SoundPool;

import static android.content.Context.AUDIO_SERVICE;

public class AnimationView extends SurfaceView implements SurfaceHolder.Callback,
		Runnable {

	private SensorManager sm;
	private Sensor sensor;
	private SensorEventListener mySensorListener;
	private float x =0;
	private int arc_x;//center of board
	private Board board, board_hand;

	private boolean IsOnFire;
	private boolean soundflag=true;
	private boolean soundflag0=true;
	private boolean soundflag1=true;
	private SoundPool sp;//声明一个SoundPool
	private int music,music2,music3,music4,music5;//定义一个整型用load（）；来设置suondID


	private SurfaceHolder Holder;
	// picture for passing all stage
	private Bitmap pic;
	// area for placing victory picture
	private Rect mSrcRect;
	//implement gravity sensor


	public static int ScreenWidth;
	public static double Ballspeed;
	public static double Boardspeed;
	private int ScreenHeight;
	private int top= ScreenHeight - ScreenHeight / 10; // initial the top of the board
	private int down=ScreenHeight - ScreenHeight / 12; // initial the bottom of the board

	private boolean prohibit_gravity=false;

	// The thickness of the brick
	private static int Brick_Thickness;
	// bricks' row and column
	private static final int ROW = 8;
	private static final int COLUNM = 11;

	//	start stage
	public int missionNum = 1;

	//default life
	private int chanceNum = 2;

	//score
	private int TotalScore;

	//score drop down
	private ArrayList<Score> scoreDropList;

	// your pen
	private Paint mPaint;

	//brick array
	private Brick[][] brickList;

	private Obstacle obstacle;
	private Ball ball;

	private int BoardWidth;
	private int Board_handWidth;

	private boolean IsRunning = true;
	private boolean BrickListIsNull;

	public boolean Ball_IsRun;
	private boolean Obstacle_isRun;
//	private boolean Obstacle_IsExist;


	/**
	 * make decision to process next game?
	 */
	public boolean NextStage = false;
	/**
	 * judge gameOver or not?
	 */
	public boolean GameOver = false;
	public boolean KeepMoving = false;


	/**
	 * stages with special color bricks
	 */
	private int[][] SpecialColor;


	// No.1 stage special bricks pattern:  UTD
	private int[][] color_mission1 = {
			{ 1, 0 },{ 1, 2 },{ 2, 0 },{ 2, 2 },{ 3, 0 },{ 3, 2 },{4, 0 },{4, 2 },{5, 0 },{5, 2 },{6, 0 },{6,1 },{6, 2 },
			{ 1, 4 },{ 1, 5 },{ 1, 6 },{ 2, 5 },{ 3, 5 },{ 4, 5 },{ 5, 5 },{ 6, 5 },
			{ 1, 8 },{ 1, 9 },{ 2, 10 },{ 3, 10 },{ 4, 10 },{ 5, 10 },{ 6, 9 },{2, 8 },{ 3, 8 },{ 4, 8 },{ 5, 8 },{ 6, 8 }
	};

	// No.2 stage special bricks pattern:  CS: Computer_Science
	private int[][] color_mission2 = { { 2, 1 },{3,1},{ 4, 1 },{1, 2 },{ 5, 2 },{ 0, 4 },{ 0, 3 },{ 6, 3 },{6, 3 },{6, 4 },
			{ 0, 9 },{0,8},{ 2, 7 },{1, 7 },{ 3, 8 },{ 4, 9 },{ 5, 9 },{ 6, 7 },{6, 8 },
	};

	// No.3 stage special bricks pattern:  cycle
	private int[][] color_mission3 = {{ 0, 0 },{ 0, 1 },{0, 2 },{ 0, 3 },{ 0, 4 },{ 0, 5 },{ 0, 6 },{0, 7 },{ 0, 8 },{ 0, 9 }, { 0, 10 },
			{ 7, 0 },{ 7, 1 },{7, 2 },{ 7, 3 },{ 7, 4 },{ 7, 5 },{ 7, 6 },{7, 7 },{ 7, 8 },{ 7, 9 }, { 7, 10 },
			{ 1, 0 },{ 2, 0 },{ 3, 0 },{ 4, 0 },{ 5, 0 },{ 6, 0 },{ 1, 10 },{ 2, 10 },{ 3, 10 },{ 4, 10 },{ 5, 10 },{ 6, 10 },
	};

	public AnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AnimationView(Context context) {
		super(context);
		init(context);
	}
	/**
	 * program start to initial
	 *
	 */
	SharedPreferences preferences1=MainActivity.ma.getSharedPreferences("user", Context.MODE_WORLD_WRITEABLE);
	Boolean IsSoundOn=preferences1.getBoolean("soundon",true);

	SharedPreferences preferences2=MainActivity.ma.getSharedPreferences("move", Context.MODE_WORLD_WRITEABLE);
	Float ParaBall=preferences2.getFloat("ball",1);


	private void init(Context context) {
		sm = (SensorManager) MainActivity.ma.getSystemService(Service.SENSOR_SERVICE);
		AudioManager audioManager ;
		audioManager= (AudioManager) MainActivity.ma. getSystemService(AUDIO_SERVICE);

		Ballspeed = (double)ParaBall;

		if(IsSoundOn){
			Toast.makeText(MainActivity.ma, "Sound on", Toast.LENGTH_SHORT).show();
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			audioManager.setStreamMute(AudioManager.STREAM_MUSIC , false);


		}else{
			Toast.makeText(MainActivity.ma, "Sound off", Toast.LENGTH_SHORT).show();
			audioManager.setStreamMute(AudioManager.STREAM_MUSIC , true);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

		}
		sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//得到一个重力传感器实例
		mPaint = new Paint();
		Holder = getHolder();
		Holder.addCallback(this);
		sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		music = sp.load(MainActivity.ma, R.raw.key_sound, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
		music2 = sp.load(MainActivity.ma, R.raw.key_sound, 2);
		music3 = sp.load(MainActivity.ma, R.raw.victory, 1);
		music4 = sp.load(MainActivity.ma, R.raw.fail, 1);
		music5 = sp.load(MainActivity.ma, R.raw.finish, 1);
		// load the picture
		pic = ((BitmapDrawable) getResources().getDrawable(R.drawable.over))
				.getBitmap();
		WindowManager manager = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE));
		// get the width of screen
		ScreenWidth = manager.getDefaultDisplay().getWidth();
		// get the height of screen
		ScreenHeight = manager.getDefaultDisplay().getHeight();

		// initial board

		// I make the board longer by change screenWidth/8
		board = new Board(arc_x - ScreenWidth / 8, ScreenHeight
				- ScreenHeight / 10, arc_x + ScreenWidth / 8,
				ScreenHeight - ScreenHeight / 12, 0xFF7FFFD4);

		board_hand= new Board(ScreenWidth / 2 - ScreenWidth / 8, ScreenHeight
				- ScreenHeight / 10, ScreenWidth / 2 + ScreenWidth / 8,
				ScreenHeight - ScreenHeight / 12, 0xFF7FFFD4);
		// calculate the width of board
		BoardWidth = board.rect.right - board.rect.left;
		Board_handWidth = board_hand.rect.right - board_hand.rect.left;

/*############ Here is the gravity control function############################*/

		SharedPreferences preferences2=MainActivity.ma.getSharedPreferences("move", Context.MODE_WORLD_WRITEABLE);
		Float ParaBoard=preferences2.getFloat("board",1);
		mySensorListener = new SensorEventListener() {
			@Override
			//传感器获取值发生改变时在响应此函数

			public void onSensorChanged(SensorEvent event1) {//备注1

				//传感器获取值发生改变，在此处理
				x = event1.values[0]*3*ParaBoard; //手机横向翻滚
				arc_x -= x;//备注2
				if(arc_x < BoardWidth / 2)
				{
					arc_x=BoardWidth / 2;
				}else if(arc_x > ScreenWidth - BoardWidth / 2){
					arc_x=ScreenWidth - BoardWidth / 2;
				}
				changex(arc_x);

			}
			@Override
			//传感器的精度发生改变时响应此函数

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		sm.registerListener(mySensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);



		// initial brick's thickness
		Brick_Thickness = (int) ScreenHeight / 3/10;

		// initial brickList
		brickList = new Brick[ROW][COLUNM];
		// define the thickness of each brick
		int brickWidth = ScreenWidth / COLUNM;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUNM; j++) {
				int left = j * brickWidth;  	//		left 2 Row blank for placing Score and Chance left
				int top = (i+2) * Brick_Thickness;
				int right = left + brickWidth;
				int bottom = top + Brick_Thickness; // 		the basic white color for bricks (1 hit disappear).
				int color = 0xFFFAEBD7;// white
				if(i==7 && j==1){
					color = 0xFF7FFFD4;
				}
				// set the special color bricks for stage 1
				SpecialColor = color_mission1;
				for (int k = 0; k < SpecialColor.length; k++) {
					if (i == SpecialColor[k][0] && j == SpecialColor[k][1]) {
						//set the number color as red which are 2-hit disappear brick.
						color = 0xFF556B2F; //green
					}
				}
				//set every brick with left,top,right,bottom location and its color.
				brickList[i][j] = new Brick(left, top, right, bottom, color,
						true);
			}
		}

		// initial ball
		ball = new Ball(ScreenWidth / 2, ScreenHeight / 2 - ScreenHeight / 8,
				(int) (ScreenWidth * 0.02), 0xffffff00);
		Ball_IsRun = false; // let the ball motionless


		// initial score dropping
		scoreDropList = new ArrayList<Score>();

		// initial obstacles(appear after stage 1)
		if (missionNum == 1) {

			obstacle = new Obstacle((ScreenWidth ) / 4,
					ScreenHeight / 2 + 10, (ScreenWidth )*3 / 4,
					ScreenHeight / 2 + 35);
			Obstacle_isRun = false;
		}
		if (missionNum != 1) {

			obstacle = new Obstacle((ScreenWidth - ScreenWidth / 4) / 2,
					ScreenHeight / 2 + 10, (ScreenWidth + ScreenWidth / 4) / 2,
					ScreenHeight / 2 + 35);
			Obstacle_isRun = false;
		}

	}
	//this is the end of the initial part with board,brick,ball....


	private void draw() {
		Canvas canvas = Holder.lockCanvas();
		if (canvas == null) {
			return;
		}

		// Background color
		canvas.drawColor(0xFF000000);

		// draw the bricks
		for (Brick[] bs : brickList) {
			for (Brick b : bs) {
				if (b.isExist) {
					b.draw(canvas, mPaint);
				}
			}
		}
		// draw the ball
		ball.draw(canvas, mPaint);
		if(prohibit_gravity==false)
		// draw the board
		{board.draw(canvas, mPaint);}
		else{
			board_hand.draw(canvas, mPaint);
		}


		// draw the obstacles

		if (missionNum == 2 ) {
			obstacle.draw(canvas, mPaint);
			Obstacle_isRun = false;
		}
		if (missionNum == 3) {
			obstacle.draw(canvas, mPaint);
			Obstacle_isRun = true;
		}
		// draw scores dropping down
		for (Score s : scoreDropList) {
			if (s.getX() >= -10 && s.getY() > 0 && s.getY() < ScreenHeight) {
				s.setY(s.getY() + 3);
				mPaint.setColor(s.getColor());
				canvas.drawText(s.getScoreStr(), s.getX() + 80, s.getY(),
						mPaint);
			}
		}
		// show the score
		mPaint.setTextSize(50);
		mPaint.setColor(0xFFFF69B4);
		mPaint.setShadowLayer(0.05f, 0, -1, 0x22ffffff);
		mPaint.setTextAlign(Align.LEFT);
		canvas.drawText("grade: " + TotalScore, 10, ScreenHeight/32, mPaint);

		// show life left
		mPaint.setTextSize(50);
		mPaint.setColor(0xFFFF69B4);
		mPaint.setShadowLayer(0.05f, 0, -1, 0x22ffffff);
		mPaint.setTextAlign(Align.RIGHT);
		canvas.drawText("chance: " + chanceNum, ScreenWidth - 10,
				ScreenHeight/32, mPaint);

		// Show hint when game pause
		if (!Ball_IsRun && !GameOver ) {
			mPaint.setColor(0x99ffffff);
			mPaint.setTextSize(60);
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setShadowLayer(0.2f, 0, 1, 0x99000000);
			canvas.drawText("Press menu button to start or pause", ScreenWidth / 2, ScreenHeight / 2
					, mPaint);
		}

		// pass stage prompt message
		if (missionNum == 1 && BrickListIsNull) {
			mPaint.setColor(0xffff0000);
			mPaint.setShadowLayer(0.2f, 0, 1, 0x99000000);
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setTextSize(55);
			mPaint.setColor(0xff9B30FF);
			canvas.drawText("[ You good ！！！ ]", ScreenWidth / 2, ScreenHeight / 2
					+ ScreenHeight / 3, mPaint);
			canvas.drawText("[ Stage passed! life+1 ]", ScreenWidth / 2, ScreenHeight / 2
					+ ScreenHeight / 4, mPaint);
			if(IsSoundOn){

				if(soundflag==true){
				sp.play(music3, (float) 0.5, (float) 0.5, 0, 0, 1);
				soundflag=false;
				}
			}
		}

		if (missionNum == 2 && BrickListIsNull) {
			mPaint.setColor(0xffff0000);
			mPaint.setShadowLayer(0.2f, 0, 1, 0x99000000);
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setTextSize(55);
			mPaint.setColor(0xff9B30FF);
			canvas.drawText("[ You good ！！！ ]", ScreenWidth / 2, ScreenHeight / 2
					+ ScreenHeight / 3, mPaint);
			canvas.drawText("[ Stage passed! life+1 ]", ScreenWidth / 2, ScreenHeight / 2
					+ ScreenHeight / 4, mPaint);

		}
		// Mission complete prompt
		if (missionNum == 3 && BrickListIsNull) {
			mPaint.setColor(0xffff0000);
			mPaint.setTextSize(60);
			mPaint.setShadowLayer(0.2f, 0, 1, 0x99000000);
			mPaint.setTextAlign(Align.CENTER);
			mSrcRect = new Rect(0, ScreenHeight/16, ScreenWidth, ScreenHeight/2);
			canvas.drawBitmap(pic,null,mSrcRect,mPaint);
			canvas.drawText("Congratulations，you passed!", ScreenWidth / 2, ScreenHeight / 2
					+ ScreenHeight / 4, mPaint);

		}
		// GAME OVER
		if (GameOver ) {
			mPaint.setColor(0xffff0000);
			mPaint.setTextSize(60);
			mPaint.setShadowLayer(0.2f, 0, 1, 0x99000000);
			mPaint.setTextAlign(Align.CENTER);

			canvas.drawText("Never give up", ScreenWidth / 2, ScreenHeight / 2
					+ ScreenHeight / 6, mPaint);
		}// GAME OVER
		if (KeepMoving ) {
			mPaint.setColor(0xffff0000);
			mPaint.setTextSize(60);
			mPaint.setShadowLayer(0.2f, 0, 1, 0x99000000);
			mPaint.setTextAlign(Align.CENTER);

			canvas.drawText("To be continued...", ScreenWidth / 2, ScreenHeight / 2
					+ ScreenHeight / 6, mPaint);
		}

		// unlock and submit canvas
		Holder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (IsRunning) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Ball_IsRun) {
				// let the ball move
				ball.move();
			}
			if (Obstacle_isRun) {
				obstacle.move();
			}

			// check the obstacle and ball hitting wall?
			hitWallCheck();
			// check if all bricks are exist?
			BrickListIsNull = false;
			for (Brick[] bs : brickList) {
				for (Brick b : bs) {
					if (!b.isExist) {
						BrickListIsNull = true;
					} else {
						BrickListIsNull = false;
						break;
					}
				}
				if (!BrickListIsNull) {
					break;
				}
			}
			// to judge if you pass current stage
			if (missionNum == 1 && BrickListIsNull) {
				Ball_IsRun = false;
				Obstacle_isRun = false;
				NextStage = true;
			}
			if (missionNum == 2 && BrickListIsNull) {
				Ball_IsRun = false;
				Obstacle_isRun = false;
				NextStage = true;
				if(soundflag0==true){
					sp.play(music3, (float) 0.5, (float) 0.5, 0, 0, 1);
					soundflag0=false;
				}
			}
			// to judge if you pass all  stage
			if (missionNum == 3 && BrickListIsNull) {
				Ball_IsRun = false;
				Obstacle_isRun = false;
				KeepMoving = true;

				if(soundflag1==true){
					sp.play(music5, (float) 0.5, (float) 0.5, 0, 0, 1);
					soundflag1=false;
				}
			}

			//  implement hit obstacle function
			if (missionNum == 2 ||missionNum == 3 ) {
				hitObstacleCheck();
			}

			// implement hit brick function
			hitBrickCheck();

			// implement hit board function
			if(prohibit_gravity==false)
			{hitBoardCheck();}
			else {
				hitBoard_handCheck();
			}
			synchronized (Holder) {
				draw();
			}
		}
	}


	/*~~~~~~~~~~~~~~~~~~~ Functions begin~~~~~~~~~~~~~~~~~~~~~~~*/
	/**
	 * implement hit wall function
	 */
	private void hitWallCheck() {
		// hit wall horizontal
		if (ball.x - ball.r <= 0 || ball.x + ball.r >= ScreenWidth) {
			ball.hitHorizental();
		}
		// hit wall vertical
		if (ball.y - ball.r <= 0) {
			ball.hitVertical();
		}
		// the ball drop down bellow the board
		if (ball.y + ball.r >= ScreenHeight) {
			Ball_IsRun = false;
			Obstacle_isRun = false;
			if (chanceNum > 0) {
				chanceNum--;

				sp.play(music4, (float) 0.1, (float) 0.1, 0, 0, 1);
				continueGame();
			} else {
			//	sp.play(music4, (float) 0.1, (float) 0.1, 0, 0, 1);
				GameOver = true; // if chance <0 then game over
			}
		}
		// obstacle hit the wall

			if (obstacle.left <= 0 || obstacle.right >= ScreenWidth) {
				obstacle.hitHorizental();

		}
	}

	/**
	 * ball and obstacle hit
	 */
	private void hitObstacleCheck() {
		// ball hit the bottom of the obstacle
		if (ball.x >= obstacle.left && ball.x <= obstacle.right
				&& ball.y + ball.r >= obstacle.top && ball.y < obstacle.top) {
			ball.hitVertical(); // move vertically
			if (ball.x == obstacle.left) {
				ball.hitVertical();
			}
			if (ball.y == obstacle.right) {
				ball.hitVertical();
			}
		}

		// ball hit the top of the obstacle
		if (ball.x >= obstacle.left && ball.x <= obstacle.right
				&& ball.y - ball.r <= obstacle.bottom
				&& ball.y > obstacle.bottom) {
			ball.hitVertical(); // move vertically
			if (ball.x == obstacle.left) {
				ball.hitVertical();
			}
			if (ball.y == obstacle.right) {
				ball.hitVertical();
			}
		}

		// ball hit the left side of the obstacle
		if (ball.y >= obstacle.top && ball.y <= obstacle.bottom
//		if (ball.y <= obstacle.top && ball.y >= obstacle.bottom
				&& ball.x + ball.r >= obstacle.left && ball.x < obstacle.left) {
			ball.hitHorizental(); // move horizontally
			if (ball.y == obstacle.top) {
				ball.hitHorizental();
			}
		}

		// ball hit the right side of the obstacle
		if (ball.y >= obstacle.top && ball.y <= obstacle.bottom
//		if (ball.y <= obstacle.top && ball.y >= obstacle.bottom
				&& ball.x - ball.r <= obstacle.right && ball.x > obstacle.right) {
			ball.hitHorizental(); //  move horizontally
			if (ball.y == obstacle.top) {
				ball.hitHorizental();
			}
		}

		// ball hit the left-top corner of the obstacle
		if (Math.pow(ball.x - obstacle.left, 2)
				+ Math.pow(ball.y - obstacle.top, 2) <= Math.pow(ball.r, 2)
				&& ball.x < obstacle.left && ball.y < obstacle.top) {
			ball.hitCorner();
		}

		// ball hit the right-top corner of the obstacle
		if (Math.pow(ball.x - obstacle.right, 2)
				+ Math.pow(ball.y - obstacle.top, 2) <= Math.pow(ball.r, 2)
				&& ball.x > obstacle.right && ball.y < obstacle.top) {
			ball.hitCorner();
		}

		// ball hit the left-bottom corner of the obstacle
		if (Math.pow(ball.x - obstacle.left, 2)
				+ Math.pow(ball.y - obstacle.bottom, 2) <= Math.pow(ball.r, 2)
				&& ball.x < obstacle.left && ball.y > obstacle.bottom) {
			ball.hitCorner();
		}

		// ball hit the right-bottom corner of the obstacle
		if (Math.pow(ball.x - obstacle.right, 2)
				+ Math.pow(ball.y - obstacle.bottom, 2) <= Math.pow(ball.r, 2)
				&& ball.x > obstacle.right && ball.y > obstacle.bottom) {
			ball.hitCorner();
		}
	}

	/**
	 * ball and bricks check
	 */
	private void hitBrickCheck() {
		int brickLeft, brickTop, brickRight, brickBottom;
		for (int i = 0; i < brickList.length; i++) {
			for (int j = 0; j < brickList[i].length; j++) {
				brickLeft = brickList[i][j].rect.left;
				brickTop = brickList[i][j].rect.top;
				brickRight = brickList[i][j].rect.right;
				brickBottom = brickList[i][j].rect.bottom;

				if (brickList[i][j].isExist) {

					// check ball hitting top of the brick
					if (ball.x >= brickLeft && ball.x <= brickRight
							&& ball.y + ball.r >= brickTop && ball.y < brickTop) {
						ball.hitVertical();
						hitColorChange(i, j, brickLeft, brickBottom); // hit and change brick's color
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
						if (ball.x == brickLeft && brickList[i][j - 1].isExist) { // eliminate another brick
							ball.hitVertical();
							hitColorChange(i, j - 1, brickLeft, brickBottom); // hit and change brick's color
							sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
						}
						if (ball.y == brickRight && brickList[i][j + 1].isExist) { //  eliminate another brick
							ball.hitVertical(); // 水平方向偏移
							hitColorChange(i, j + 1, brickLeft, brickBottom); // hit and change brick's color
							sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
						}
					}

					// check ball hitting bottom of the brick
					if (ball.x >= brickLeft && ball.x <= brickRight
							&& ball.y - ball.r <= brickBottom
							&& ball.y > brickBottom) {
						ball.hitVertical();
						hitColorChange(i, j, brickLeft, brickBottom);
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
						if (ball.x == brickLeft && brickList[i][j - 1].isExist) {
							ball.hitVertical();
							hitColorChange(i, j - 1, brickLeft, brickBottom);
							sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
						}
						if (ball.y == brickRight && brickList[i][j + 1].isExist) {
							ball.hitVertical();
							hitColorChange(i, j + 1, brickLeft, brickBottom);
							sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
						}
					}

					// check ball hitting left of the brick
					if (ball.y >= brickTop && ball.y <= brickBottom
//					if (ball.y >= brickBottom && ball.y <= 	brickTop
							&& ball.x + ball.r >= brickLeft
							&& ball.x < brickLeft) {
						ball.hitHorizental();
						hitColorChange(i, j, brickLeft, brickBottom);
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
						if (ball.y == brickTop && brickList[i - 1][j].isExist) {
							ball.hitHorizental();
							hitColorChange(i - 1, j, brickLeft, brickBottom);
							sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
						}
						if (i != brickList.length - 1) { // when ball drop down
							if (ball.y == brickBottom
									&& brickList[i + 1][j].isExist) {
								ball.hitHorizental();
								hitColorChange(i + 1, j, brickLeft, brickBottom);
								sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
							}
						}
					}

					// check ball hitting right of the brick
					if (ball.y >= brickTop && ball.y <= brickBottom
//					if (ball.y >= brickBottom && ball.y <= brickTop
							&& ball.x - ball.r <= brickRight
							&& ball.x > brickRight) {
						ball.hitHorizental();
						hitColorChange(i, j, brickLeft, brickBottom);
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
						if (ball.y == brickTop && brickList[i - 1][j].isExist) {
							ball.hitHorizental();
							hitColorChange(i - 1, j, brickLeft, brickBottom);
							sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
						}
						if (i != brickList.length - 1) {
							if (brickList[i + 1][j].isExist
									&& ball.y == brickBottom) {
								ball.hitHorizental();
								hitColorChange(i + 1, j, brickLeft, brickBottom);
								sp.play(music2, (float) 0.2, (float) 0.2, 0, 0, 1);
							}
						}
					}
					/* #############	// check ball hitting corners of the brick   ##############*/
					//				left-top corner
					if (Math.pow(ball.x - brickLeft, 2)
							+ Math.pow(ball.y - brickTop, 2) <= Math.pow(
							ball.r, 2)
							&& ball.x < brickLeft
							&& ball.y < brickTop) {
						ball.hitCorner();
						hitColorChange(i, j, brickLeft, brickBottom);
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
					}

					//				right-top corner
					if (Math.pow(ball.x - brickRight, 2)
							+ Math.pow(ball.y - brickTop, 2) <= Math.pow(
							ball.r, 2)
							&& ball.x > brickRight
							&& ball.y < brickTop) {
						ball.hitCorner();
						hitColorChange(i, j, brickLeft, brickBottom);
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
					}

					//				left-bottom corner
					if (Math.pow(ball.x - brickLeft, 2)
							+ Math.pow(ball.y - brickBottom, 2) <= Math.pow(
							ball.r, 2)
							&& ball.x < brickLeft
							&& ball.y > brickBottom) {
						ball.hitCorner();
						hitColorChange(i, j, brickLeft, brickBottom);
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
					}

					// 			right-bottom corner
					if (Math.pow(ball.x - brickRight, 2)
							+ Math.pow(ball.y - brickBottom, 2) <= Math.pow(
							ball.r, 2)
							&& ball.x > brickRight
							&& ball.y > brickBottom) {
						ball.hitCorner();
						hitColorChange(i, j, brickLeft, brickBottom);
						sp.play(music, (float) 0.2, (float) 0.2, 0, 0, 1);
					}
				}
			}
		}
	}

	/**
	 * grades dropping down
	 * 
	 * @param brickLeft
	 * @param brickBottom
	 */
	private void scoreDropList(int brickLeft, int brickBottom) {
		// TODO Auto-generated method stub
		if (DropScoreColor == 20) {
			scoreDropList.add(new Score(brickLeft, brickBottom, "+20",
					0xcc00ff00));
			DropScoreColor = 0;
		} else if (DropScoreColor == 15) {
			scoreDropList.add(new Score(brickLeft, brickBottom, "+15",
					0xcc8A2BE2));
			DropScoreColor = 0;
		} else if (DropScoreColor == 10) {
			scoreDropList.add(new Score(brickLeft, brickBottom, "+10",
					0xFF556B2F));
			DropScoreColor = 0;
		} else {
			scoreDropList.add(new Score(brickLeft, brickBottom, "+5",
					0xccffffff));
		}
	}

	/**
	 * to judge the bricks disappear or change color
	 * 
	 * @param i
	 * @param j
	 */
	int DropScoreColor = 0; // dropping down color of grades

	private void hitColorChange(int i, int j, int brickLeft, int brickBottom) {
		if (brickList[i][j].color == 0xFFFF69B4) {
			brickList[i][j].isExist = true; // keep bricks
			brickList[i][j].color = 0xFFFFD700; // turn to yellow
			DropScoreColor = 20;
			scoreDropList(brickLeft, brickBottom);
			TotalScore += 20;
		} else if (brickList[i][j].color == 0xFFFFD700) {
			brickList[i][j].isExist = true; //  keep bricks
			brickList[i][j].color = 0xFF556B2F; // turn to green
			DropScoreColor = 15;
			scoreDropList(brickLeft, brickBottom);
			TotalScore += 15;
		} else if (brickList[i][j].color == 0xFF556B2F) {
			brickList[i][j].isExist = true; //  keep bricks
			brickList[i][j].color = 0xFFFAEBD7;
//			brickList[i][j].color = 0xff191970; // turn to white
			DropScoreColor = 10;
			scoreDropList(brickLeft, brickBottom);
			TotalScore += 10;
		} else {
			brickList[i][j].isExist = false; // make brick disappear
			scoreDropList(brickLeft, brickBottom);
			TotalScore += 5;
		}

		// brickList[6][1],[6][5],[6][9] are special bricks, the following bricks will disappear. when they are hit.
		if (i == 6 && j == 9) {
			if (brickList[1][8].isExist) {
				brickList[1][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 8, Brick_Thickness*2);
				TotalScore += 15;
			}
			if (brickList[1][9].isExist) {
				brickList[1][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*9, Brick_Thickness*2);
				TotalScore += 15;
			}
			if (brickList[2][10].isExist) {
				brickList[2][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness*3);
				TotalScore += 15;
			}
			if (brickList[3][10].isExist) {
				brickList[3][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness*4);
				TotalScore += 15;
			}
			if (brickList[4][10].isExist) {
				brickList[4][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness*5);
				TotalScore += 15;
			}
			if (brickList[5][10].isExist) {
				brickList[5][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness*6);
				TotalScore += 15;
			}
			if (brickList[2][8].isExist) {
				brickList[2][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*8, Brick_Thickness*3);
				TotalScore += 15;
			}
			if (brickList[3][8].isExist) {
				brickList[3][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*8, Brick_Thickness*4);
				TotalScore += 15;
			}
			if (brickList[4][8].isExist) {
				brickList[4][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*8, Brick_Thickness*5);
				TotalScore += 15;
			}
			if (brickList[5][8].isExist) {
				brickList[5][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*8, Brick_Thickness*6);
				TotalScore += 15;
			}
			if (brickList[6][8].isExist) {
				brickList[6][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*8, Brick_Thickness*7);
				TotalScore += 15;
			}
			if (brickList[6][9].isExist) {
				brickList[6][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*9, Brick_Thickness * 7);
				TotalScore += 15;
			}


		}
		if (i == 6 && j == 5) {
			if (brickList[1][4].isExist) {
				brickList[1][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*4, Brick_Thickness*2);
				TotalScore += 15;
			}
			if (brickList[1][5].isExist) {
				brickList[1][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*5, Brick_Thickness*2);
				TotalScore += 15;
			}
			if (brickList[1][6].isExist) {
				brickList[1][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness*2);
				TotalScore += 15;
			}
			if (brickList[2][5].isExist) {
				brickList[2][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 5, Brick_Thickness*3);
				TotalScore += 15;
			}
			if (brickList[3][5].isExist) {
				brickList[3][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3, Brick_Thickness*4);
				TotalScore += 15;
			}
			if (brickList[4][5].isExist) {
				brickList[4][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness*5);
				TotalScore += 15;
			}
			if (brickList[5][5].isExist) {
				brickList[5][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 5, Brick_Thickness*6);
				TotalScore += 15;
			}
			if (brickList[6][5].isExist) {
				brickList[6][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 5, Brick_Thickness*6);
				TotalScore += 15;
			}


		}
		if (i == 6 && j == 1) {
			if (brickList[1][0].isExist) {
				brickList[1][0].isExist = false;
				scoreDropList(0, Brick_Thickness*2);
				TotalScore += 15;
			}
			if (brickList[1][2].isExist) {
				brickList[1][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*2, Brick_Thickness*2);
				TotalScore += 15;
			}
			if (brickList[2][0].isExist) {
				brickList[2][0].isExist = false;
				scoreDropList(0, Brick_Thickness*3);
				TotalScore += 15;
			}
			if (brickList[2][2].isExist) {
				brickList[2][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 2, Brick_Thickness*3);
				TotalScore += 15;
			}
			if (brickList[3][0].isExist) {
				brickList[3][0].isExist = false;
				scoreDropList(0, Brick_Thickness*4);
				TotalScore += 15;
			}
			if (brickList[3][2].isExist) {
				brickList[3][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 2, Brick_Thickness*4);
				TotalScore += 15;
			}
			if (brickList[4][0].isExist) {
				brickList[4][0].isExist = false;
				scoreDropList(0, Brick_Thickness*5);
				TotalScore += 15;
			}
			if (brickList[4][2].isExist) {
				brickList[4][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 2, Brick_Thickness*5);
				TotalScore += 15;
			}
			if (brickList[5][0].isExist) {
				brickList[5][0].isExist = false;
				scoreDropList(0, Brick_Thickness*6);
				TotalScore += 15;
			}
			if (brickList[5][2].isExist) {
				brickList[5][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 2, Brick_Thickness*6);
				TotalScore += 15;
			}
			if (brickList[6][0].isExist) {
				brickList[6][0].isExist = false;
				scoreDropList(0, Brick_Thickness*7);
				TotalScore += 15;
			}
			if (brickList[6][2].isExist) {
				brickList[6][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM*2, Brick_Thickness * 7);
				TotalScore += 15;
			}
			if (brickList[6][1].isExist) {
				brickList[6][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness * 7);
				TotalScore += 15;
			}

		}
		// brickList[7][1]is special brick, the following bricks will disappear. when [7][1] is hit.
		if (i == 7 && j == 1) {
			if (brickList[0][0].isExist) {
				brickList[0][0].isExist = false;
				scoreDropList(0, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][1].isExist) {
				brickList[0][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][2].isExist) {
				brickList[0][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 2, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][3].isExist) {
				brickList[0][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][4].isExist) {
				brickList[0][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][5].isExist) {
				brickList[0][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 5, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][6].isExist) {
				brickList[0][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][7].isExist) {
				brickList[0][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][8].isExist) {
				brickList[0][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 8, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][9].isExist) {
				brickList[0][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 9, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[0][10].isExist) {
				brickList[0][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness);
				TotalScore += 5;
			}
			if (brickList[1][1].isExist) {
				brickList[1][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness * 2);
				TotalScore += 5;
			}
			if (brickList[1][3].isExist) {
				brickList[1][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3, Brick_Thickness * 2);
				TotalScore += 5;
			}
			if (brickList[1][7].isExist) {
				brickList[1][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness * 2);
				TotalScore += 5;
			}
			if (brickList[1][10].isExist) {
				brickList[1][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness * 2);
				TotalScore += 5;
			}
			if (brickList[2][1].isExist) {
				brickList[2][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness * 3);
				TotalScore += 5;
			}
			if (brickList[2][3].isExist) {
				brickList[2][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3 , Brick_Thickness * 3);
				TotalScore += 5;
			}
			if (brickList[2][4].isExist) {
				brickList[2][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness * 3);
				TotalScore += 5;
			}
			if (brickList[2][6].isExist) {
				brickList[2][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness * 3);
				TotalScore += 5;
			}
			if (brickList[2][7].isExist) {
				brickList[2][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness * 3);
				TotalScore += 5;
			}
			if (brickList[2][9].isExist) {
				brickList[2][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 9, Brick_Thickness * 3);
				TotalScore += 5;
			}

			if (brickList[3][1].isExist) {
				brickList[3][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness * 4);
				TotalScore += 5;
			}
			if (brickList[3][3].isExist) {
				brickList[3][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3 , Brick_Thickness * 4);
				TotalScore += 5;
			}
			if (brickList[3][4].isExist) {
				brickList[3][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness * 4);
				TotalScore += 5;
			}
			if (brickList[3][6].isExist) {
				brickList[3][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness * 4);
				TotalScore += 5;
			}
			if (brickList[3][7].isExist) {
				brickList[3][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness * 4);
				TotalScore += 5;
			}
			if (brickList[3][9].isExist) {
				brickList[3][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 9, Brick_Thickness * 4);
				TotalScore += 5;
			}
			if (brickList[4][1].isExist) {
				brickList[4][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness * 5);
				TotalScore += 5;
			}
			if (brickList[4][3].isExist) {
				brickList[4][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3 , Brick_Thickness * 5);
				TotalScore += 5;
			}
			if (brickList[4][4].isExist) {
				brickList[4][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness * 5);
				TotalScore += 5;
			}
			if (brickList[4][6].isExist) {
				brickList[4][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness * 5);
				TotalScore += 5;
			}
			if (brickList[4][7].isExist) {
				brickList[4][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness * 5);
				TotalScore += 5;
			}
			if (brickList[4][9].isExist) {
				brickList[4][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 9, Brick_Thickness * 5);
				TotalScore += 5;
			}
			if (brickList[5][1].isExist) {
				brickList[5][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness * 6);
				TotalScore += 5;
			}
			if (brickList[5][3].isExist) {
				brickList[5][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3 , Brick_Thickness * 6);
				TotalScore += 5;
			}
			if (brickList[5][4].isExist) {
				brickList[5][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness * 6);
				TotalScore += 5;
			}
			if (brickList[5][6].isExist) {
				brickList[5][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness * 6);
				TotalScore += 5;
			}
			if (brickList[5][7].isExist) {
				brickList[5][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness * 6);
				TotalScore += 5;
			}
			if (brickList[5][9].isExist) {
				brickList[5][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 9, Brick_Thickness * 6);
				TotalScore += 5;
			}
			if (brickList[6][3].isExist) {
				brickList[6][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3 , Brick_Thickness * 7);
				TotalScore += 5;
			}
			if (brickList[6][4].isExist) {
				brickList[6][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness * 7);
				TotalScore += 5;
			}
			if (brickList[6][6].isExist) {
				brickList[6][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness * 7);
				TotalScore += 5;
			}
			if (brickList[6][7].isExist) {
				brickList[6][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness * 7);
				TotalScore += 5;
			}
			if (brickList[6][10].isExist) {
				brickList[6][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness * 7);
				TotalScore += 5;
			}
			if (brickList[7][0].isExist) {
				brickList[7][0].isExist = false;
				scoreDropList(0, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][1].isExist) {
				brickList[7][1].isExist = false;
				scoreDropList(ScreenWidth / COLUNM, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][2].isExist) {
				brickList[7][2].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 2, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][3].isExist) {
				brickList[7][3].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 3, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][4].isExist) {
				brickList[7][4].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 4, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][5].isExist) {
				brickList[7][5].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 5, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][6].isExist) {
				brickList[7][6].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 6, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][7].isExist) {
				brickList[7][7].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 7, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][8].isExist) {
				brickList[7][8].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 8, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][9].isExist) {
				brickList[7][9].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 9, Brick_Thickness * 8);
				TotalScore += 5;
			}
			if (brickList[7][10].isExist) {
				brickList[7][10].isExist = false;
				scoreDropList(ScreenWidth / COLUNM * 10, Brick_Thickness * 8);
				TotalScore += 5;
			}
		}

	}

	/**
	 * ball and board hit check
	 */
	private void hitBoardCheck() {

		float center = (board.rect.right+board.rect.left)/2;
		// hit the top of board
		if (ball.x >= board.rect.left && ball.x <= board.rect.right
				&& ball.y + ball.r >= board.rect.top && ball.y < board.rect.top) {
				ball.hitVertical();

			if( ball.x >= board.rect.left && ball.x <= (board.rect.left+(board.rect.right-board.rect.left)*3/8)){
				double bias = (center-ball.x)/(center-board.rect.left);
				double suplement= Math.sqrt((1-bias*bias)/(AnimationView.ScreenWidth *0.01)/(AnimationView.ScreenWidth *0.01)+1);
				ball.MinusHorizental(bias,suplement);
			}
			else if( ball.x > (board.rect.right-(board.rect.right-board.rect.left)*3/8) && ball.x <= board.rect.right){
				double bias = (ball.x-center)/( board.rect.right-center);
				double suplement= Math.sqrt((1-bias*bias)/(AnimationView.ScreenWidth *0.01)/(AnimationView.ScreenWidth *0.01)+1);
				ball.PLusHorizental(bias,suplement);
			}
		}
		// hit hte left-top of the board
		if (Math.pow(ball.x - board.rect.left, 2)
				+ Math.pow(ball.y - board.rect.top, 2) <= ball.r
				&& ball.x < board.rect.left && ball.y < board.rect.top) {
			ball.hitCorner();
		}

		//  hit hte right-top of the board
		if (Math.pow(ball.x - board.rect.right, 2)
				+ Math.pow(ball.y - board.rect.top, 2) <= ball.r
				&& ball.x > board.rect.right && ball.y < board.rect.top) {
			ball.hitCorner();
		}
	}


	private void hitBoard_handCheck() {

		float center = (board_hand.rect.right+board_hand.rect.left)/2;
		// hit the top of board
		if (ball.x >= board_hand.rect.left && ball.x <= board_hand.rect.right
				&& ball.y + ball.r >= board_hand.rect.top && ball.y < board_hand.rect.top) {
			ball.hitVertical();

			if( ball.x >= board_hand.rect.left && ball.x <= (board_hand.rect.left+(board_hand.rect.right-board_hand.rect.left)*3/8)){
				double bias = (center-ball.x)/(center-board_hand.rect.left);
				double suplement= Math.sqrt((1-bias*bias)/(AnimationView.ScreenWidth *0.01)/(AnimationView.ScreenWidth *0.01)+1);
				ball.MinusHorizental(bias,suplement);
			}
			else if( ball.x > (board_hand.rect.right-(board_hand.rect.right-board_hand.rect.left)*3/8) && ball.x <= board_hand.rect.right){
				double bias = (ball.x-center)/( board_hand.rect.right-center);
				double suplement= Math.sqrt((1-bias*bias)/(AnimationView.ScreenWidth *0.01)/(AnimationView.ScreenWidth *0.01)+1);
				ball.PLusHorizental(bias,suplement);
			}
		}
		// hit hte left-top of the board
		if (Math.pow(ball.x - board_hand.rect.left, 2)
				+ Math.pow(ball.y - board_hand.rect.top, 2) <= ball.r
				&& ball.x < board_hand.rect.left && ball.y < board_hand.rect.top) {
			ball.hitCorner();
		}

		//  hit hte right-top of the board
		if (Math.pow(ball.x - board_hand.rect.right, 2)
				+ Math.pow(ball.y - board_hand.rect.top, 2) <= ball.r
				&& ball.x > board_hand.rect.right && ball.y < board_hand.rect.top) {
			ball.hitCorner();
		}
	}
	/*
	 * movement control for board
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			prohibit_gravity=true;
			return true;
		case MotionEvent.ACTION_MOVE:
			changePosition(event);
			prohibit_gravity=true;
			return true;
		case MotionEvent.ACTION_UP:
			changePosition(event);
			prohibit_gravity=false;
			return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * control the directions for board
	 *
	 * @param event
	 */
	private void changePosition(MotionEvent event) {
		int X = (int) event.getX();
		if (X >= BoardWidth / 2 && X <= ScreenWidth - BoardWidth / 2) {
			board_hand.rect.left = X - BoardWidth / 2;
			board_hand.rect.right = X + BoardWidth / 2;
		} else if (X < BoardWidth / 2) {
			board_hand.rect.left = 0;
			board_hand.rect.right = BoardWidth;
		} else if (X > ScreenWidth - BoardWidth / 2) {
			board_hand.rect.left = ScreenWidth - BoardWidth;
			board_hand.rect.right = ScreenWidth;
		}
		invalidate();
	}
	private void changex(float x) {
		int X= (int)(x);

		if (X >= BoardWidth / 2 && X <= ScreenWidth - BoardWidth / 2) {
			board.rect.left = X - BoardWidth / 2;
			board.rect.right = X + BoardWidth / 2;
		} else if (X < BoardWidth / 2) {
			board.rect.left = 0;
			board.rect.right = BoardWidth;
		} else if (X > ScreenWidth - BoardWidth / 2) {
			board.rect.left = ScreenWidth - BoardWidth;
			board.rect.right = ScreenWidth;
		}
		invalidate();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		arc_x = ScreenWidth/2;
		new Thread(this).start(); // Thread start to work
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		IsRunning = false;
	}

	/**
	 * After game over, you decide to game again, all parameters are reset.
	 */
	public void resetGame() {
		Ball_IsRun = false;
		Obstacle_isRun = false;
		missionNum = 1;
		TotalScore = 0; //clear your grade
		chanceNum = 2; // give your start lives
		ball.x = ScreenWidth / 2;
		ball.y = ScreenHeight / 2 - ScreenHeight / 8;
		board.rect.left = ScreenWidth / 2 - ScreenWidth / 10;
		board.rect.top = ScreenHeight - ScreenHeight / 10;
		board.rect.right = ScreenWidth / 2 + ScreenWidth / 10;
		board.rect.bottom = ScreenHeight - ScreenHeight / 12;
		int brickWidth = ScreenWidth / COLUNM;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUNM; j++) {
				int left = j * brickWidth;
				int top = (i+2) * Brick_Thickness;
				int right = left + brickWidth;
				int bottom = top + Brick_Thickness;
				int color = 0xFFFAEBD7;
				SpecialColor = color_mission1;
				if(i==7 && j==1){
					color = 0xFF7FFFD4;
				}
				for (int k = 0; k < SpecialColor.length; k++) {
					if (i == SpecialColor[k][0] && j == SpecialColor[k][1]) {
						color = 0xFF556B2F;
					}
				}
				brickList[i][j] = new Brick(left, top, right, bottom, color,
						true);
			}
		}
		GameOver = false;
		IsRunning = true;
	}

	/**
	 * jump to next Stage
	 */
	public void nextMission(int missionNum) {
		switch (missionNum) {

		case 2:
			SpecialColor = color_mission2;

			break;
		case 3:
			SpecialColor = color_mission3;
			break;

		}
		Ball_IsRun = true;
		Obstacle_isRun = true;
		chanceNum++; // life +1
		ball.x = ScreenWidth / 2 - missionNum * 5; // the offset of ball at each start of game
		ball.y = ScreenHeight / 2 - ScreenHeight / 8;
		board.rect.left = ScreenWidth / 2 - ScreenWidth / 8;
		board.rect.top = ScreenHeight - ScreenHeight / 10;
		board.rect.right = ScreenWidth / 2 + ScreenWidth / 8;
		board.rect.bottom = ScreenHeight - ScreenHeight / 12;
		int brickWidth = ScreenWidth / COLUNM;
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUNM; j++) {
				int left = j * brickWidth;
				int top = (i+2) * Brick_Thickness;
				int right = left + brickWidth;
				int bottom = top + Brick_Thickness;
				int color = 0xFFFAEBD7;//white
//				int color = 0xff191970; // dark blue
				for (int k = 0; k < SpecialColor.length; k++) {
					if (i == SpecialColor[k][0] && j == SpecialColor[k][1]) {
						if (missionNum == 2) {
							color = 0xFFFFD700; // gold
						} else if (missionNum == 3 ) {
							color = 0xFFFF69B4; // purple
						}
					}
				}
				brickList[i][j] = new Brick(left, top, right, bottom, color,
						true);
			}
		}

		GameOver = false;
		NextStage = false;
		IsRunning = true;
	}

	/**
	 * If your life > 0, then continue
	 */
	private void continueGame() {
		Ball_IsRun = false;
		Obstacle_isRun = false;
		ball.x = ScreenWidth / 2;
		ball.y = ScreenHeight / 2 - ScreenHeight / 8;
		board.rect.left = ScreenWidth / 2 - ScreenWidth / 10;
		board.rect.top = ScreenHeight - ScreenHeight / 10;
		board.rect.right = ScreenWidth / 2 + ScreenWidth / 10;
		board.rect.bottom = ScreenHeight - ScreenHeight / 12;
		IsRunning = true;
	}
	/*~~~~~~~~~~~~~~~~~~~ Functions end~~~~~~~~~~~~~~~~~~~~~~~*/
}
