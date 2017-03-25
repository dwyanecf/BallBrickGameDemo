package com.mct.ballbrickgamedemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mct.ballbrickgview.AnimationView;

public class MainActivity extends Activity {
	int count;
	public static MainActivity ma;
	private AnimationView mGameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ma = this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mGameView = new AnimationView(this);
		setContentView(mGameView);
		// mGameView.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				if (mGameView.GameOver) {
					// 游戏结束，重新开始
					mGameView.resetGame();
				} else if (mGameView.NextStage) {
					mGameView.missionNum++;
					mGameView.nextMission(mGameView.missionNum);
				} else {
					mGameView.Ball_IsRun = !mGameView.Ball_IsRun;
				}
				break;
			case KeyEvent.KEYCODE_BACK:
				// openOptionsMenu();
				exitByDoubleClick();
				break;

			default:
				break;
		}
		return false;
	}

	private static boolean isExit = false;

	private void exitByDoubleClick() {
		Timer tExit = null;
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "Press again to quit",
					Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					isExit = false;
				}
			}, 2000);

		} else {
			finish();
			System.exit(0);

		}
	}
}
