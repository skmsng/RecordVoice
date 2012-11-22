package com.example.recordvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

//ロック画面
public class Lock extends Activity{
	
	Button button;
	ImageView dragView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lock);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //リスナー登録
        dragView = (ImageView)findViewById(R.id.imageView1);
        LockViewListener listener = new LockViewListener(dragView, this);
        dragView.setOnTouchListener(listener);
	}

	//ロック解除ボタンでCallクラスへ移動
	public void kaijo(){
		Intent intent = new Intent(this, Call.class);
		//Intent intent = new Intent(this, Sorry.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	
	
	
//	//録音再生テストボタンでRecordVoiceクラスへ移動
//	public void test(View v){
//		Intent intent = new Intent(this, RecordVoice.class);
//		this.startActivity(intent);
//		//this.finish();	//このアクティビティを消滅する
//	}

	//メニューから設定画面へ（もしものために実装）
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.item1:
			Intent intent = new Intent("android.settings.SETTINGS");
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//戻るボタン無効
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	            return false;
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
	
	//隠しボタン（設定画面）
	public void setting(View v){
		Intent intent = new Intent("android.settings.SETTINGS");
		startActivity(intent);
	}
	
	//アプリを終了させる
	//異常終了
	//フロントカメラ
	

}
