package com.example.recordvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

//ロック画面
public class Lock extends Activity{
	
	Button button;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lock);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
	}

	//ロック解除ボタンでCallクラスへ移動
	public void kaijo(View v){
		Intent intent = new Intent(this, Call.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	
	//録音再生テストボタンでRecordVoiceクラスへ移動
	public void test(View v){
		Intent intent = new Intent(this, RecordVoice.class);
		this.startActivity(intent);
		//this.finish();	//このアクティビティを消滅する
	}
	
	
	//隠しボタン（設定画面）
	public void setting(View v){
		Intent intent = new Intent("android.settings.SETTINGS");
		startActivity(intent);
	}
	
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
	
	//アプリを終了させる
	//異常終了
	//フロントカメラ
	

}
