package com.mct.ballbrickgamedemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created by fan on 2016/4/13.
 */
public class EndActivity extends Activity {
    private ToggleButton mToggleButton;
    private SeekBar seekBarBall;
    private SeekBar seekBarBoard;
    private TextView balltext;
    private TextView boardtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_activity);
        seekBarBall = (SeekBar) findViewById(R.id.seekBar);
        seekBarBoard = (SeekBar) findViewById(R.id.seekBar2);

        balltext = (TextView)findViewById(R.id.ballspeedtext);
        boardtext = (TextView)findViewById(R.id.boardspeedtext);

        seekBarBall.setOnSeekBarChangeListener(seekbarChangeListenerBall);
        seekBarBoard.setOnSeekBarChangeListener(seekbarChangeListenerBoard);
        initView();//��ʼ���ؼ�����
    }

    private OnSeekBarChangeListener seekbarChangeListenerBall = new OnSeekBarChangeListener() {

        // ֹͣ�϶�ʱִ��
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }
        // �ڽ��ȿ�ʼ�ı�ʱִ��
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
        // �����ȷ����ı�ʱִ��
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            balltext.setText("You are moving");
            Message message = new Message();

            Bundle bundle = new Bundle();// �������

            float pro = seekBar.getProgress();

            float num = seekBar.getMax();

            float result = (pro / num)* 10;
            bundle.putFloat("key", result);


            SharedPreferences preferences=EndActivity.this.getSharedPreferences("move", Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putFloat("ball", result/5);
            editor.commit();
            Toast.makeText(EndActivity.this, "Ball speed saved", Toast.LENGTH_SHORT).show();

            message.setData(bundle);

            message.what = 0;

            handler.sendMessage(message);

        }
    };

    /**
     * ��Handler������UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            balltext.setText("Current Speed :       "
                    + msg.getData().getFloat("key") + "/10");

        }

    };


    private OnSeekBarChangeListener seekbarChangeListenerBoard = new OnSeekBarChangeListener() {

        // ֹͣ�϶�ʱִ��
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }
        // �ڽ��ȿ�ʼ�ı�ʱִ��
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }
        // �����ȷ����ı�ʱִ��
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            boardtext.setText("You are moving");
            Message message = new Message();

            Bundle bundle = new Bundle();// �������

            float pro = seekBar.getProgress();

            float num = seekBar.getMax();

            float result = (pro / num) * 10;
            bundle.putFloat("key", result);

            SharedPreferences preferences=EndActivity.this.getSharedPreferences("move", Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putFloat("board", result/5);
            editor.commit();
            Toast.makeText(EndActivity.this, "Board speed saved", Toast.LENGTH_SHORT).show();

            message.setData(bundle);

            message.what = 0;

            handler1.sendMessage(message);

        }
    };

    /**
     * ��Handler������UI
     */
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boardtext.setText("Current Speed :       "
                    + msg.getData().getFloat("key") + "/10");

        }

    };
    public void initView() {

        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton); //��ȡ���ؼ�
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // ����ť��һ�α����ʱ����Ӧ���¼�
                if (mToggleButton.isChecked()) {

                    SharedPreferences preferences=EndActivity.this.getSharedPreferences("user", Context.MODE_WORLD_WRITEABLE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("soundon", true);
                    editor.commit();
                    Toast.makeText(EndActivity.this, "On fire", Toast.LENGTH_SHORT).show();

                }
                // ����ť�ٴα����ʱ����Ӧ���¼�
                else {
                    SharedPreferences preferences=EndActivity.this.getSharedPreferences("user", Context.MODE_WORLD_WRITEABLE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("soundon", false);
                    editor.commit();
                    Toast.makeText(EndActivity.this, "Silent", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
