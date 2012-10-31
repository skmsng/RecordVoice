package com.example.recordvoice;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//発信中
public class Call extends Activity {
	
	Timer timer;
	int counter;
	int limit = 5;	//次の画面へ移動するまでの秒
	TextView tv2;
	MediaPlayer mp;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call);
        tv2 = (TextView)this.findViewById(R.id.textView2);
        
        //5秒経過でInCallアクティビティへ
        this.startTimer();
        
        //コール音の再生
        this.call();
	}
	
	//コール音の再生
	public void call() {
		// リソースID指定
		mp = MediaPlayer.create(this,R.raw.call);
		mp.setLooping(true);//ループ再生
		mp.seekTo(0);		//再生位置0ミリ秒
		mp.start();			//再生開始
    }
	
	//5秒タイマー
	public void startTimer(){
		if(timer != null) timer.cancel();
		timer = new Timer();
		final android.os.Handler handler = new android.os.Handler();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					public void run(){
						if(counter == limit){
							timer.cancel();
							next();
							//if(wl.isHeld()) wl.release();
							//showAlarm();
						}else{
							//CountdownTimerActivity.countdown(counter);
							counter = counter+1;
							showCount();
						}
					}
				});
			}
		},0,1000);
	}
	
	//カウントの表示
	public void showCount(){
		tv2.setText(String.valueOf(counter));
	}
	
	//5秒経過でInCallアクティビティへ
	public void next(){
		mp.stop();	//再生停止
		Intent intent = new Intent(this, InCall.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	
	//終了ボタンを押したとき
	public void onClick(View v){
	}
}
