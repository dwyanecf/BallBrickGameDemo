package com.mct.ballbrickgamedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends Activity {


    ImageButton imageButton1,imageButton2,imageButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        imageButton1 = (ImageButton)findViewById(R.id.ImageButton01);
        imageButton2 = (ImageButton)findViewById(R.id.ImageButton02);
        imageButton3 = (ImageButton)findViewById(R.id.ImageButton03);

        //���·ֱ�Ϊÿ����ť�����¼����� setOnClickListener
        imageButton1.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstActivity.this, MainActivity.class);
                FirstActivity.this.startActivity(intent);
//                finish();
                //ֹͣ��ǰ��Activity,�����д,�򰴷��ؼ�����ת��ԭ����Activity
            }
        });
        imageButton2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstActivity.this, EndActivity.class);
                FirstActivity.this.startActivity(intent);

                //ֹͣ��ǰ��Activity,�����д,�򰴷��ؼ�����ת��ԭ����Activity
            }
        });

        imageButton3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {

                new AlertDialog.Builder(FirstActivity.this).setTitle("Really leave me alone��")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Bye", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // �����ȷ�ϡ���Ĳ���
                                FirstActivity.this.finish();

                            }
                        })
                        .setNegativeButton("stay", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // ��������ء���Ĳ���,���ﲻ����û���κβ���
                            }
                        }).show();
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                //ֹͣ��ǰ��Activity,�����д,�򰴷��ؼ�����ת��ԭ����Activity
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
//            case KeyEvent.KEYCODE_MENU:
//                if (mGameView.gameOver) {
//                    // Game over, Restart
//                    mGameView.resetGame();
//                } else if (mGameView.nextGame) {
//                    mGameView.missionNum++;
//                    mGameView.nextMission(mGameView.missionNum);
//                } else {
//                    mGameView.ball_isRun = !mGameView.ball_isRun;
//                }
//                break;
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
            Toast.makeText(getApplicationContext(), "Press back button again to exit",
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
