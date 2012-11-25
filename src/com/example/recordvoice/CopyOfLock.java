package com.example.recordvoice;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

//ロック画面
public class CopyOfLock extends Activity implements OnTouchListener{
	
	Button button;
	ImageView dragView;
	Resources resources;
	private Drawable drawable;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lock);
        
        // 画面のロックを防ぐ
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //リスナー登録
        dragView = (ImageView)findViewById(R.id.imageView1);
        dragView.setOnTouchListener(this);
//        dragView.getLeft();
//        dragView.getTop();
        dragView.setScaleType(ScaleType.CENTER_CROP);
        //dragView.layout(200, top, left + dragView.getWidth(), top + dragView.getHeight());
        //dragView.layout(Integer.valueOf(-310), Integer.valueOf(0), Integer.valueOf(-310) + dragView.getWidth(), dragView.getHeight());
        
        //LockViewListener listener = new LockViewListener(dragView, this);
        //dragView.setOnTouchListener(listener);
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
	
	
	private int oldx, x;
	private int left, top;
	private boolean down;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		x = (int)event.getRawX();
		//x = (int)event.getX();
//		if(down && x > dragView.getWidth()*2/3){
//			kaijo();
//		}
		
		switch(event.getAction()){
		
		case MotionEvent.ACTION_DOWN:
			oldx = x;
			if(oldx < dragView.getWidth()/3) down = true;
			break;
		case MotionEvent.ACTION_MOVE:
			left = dragView.getLeft() + (x - oldx);
			// Viewを移動する
			if(down && left>0){
				dragView.layout(left, top, left + dragView.getWidth(), top + dragView.getHeight());
			}
			break;
		case MotionEvent.ACTION_UP:
			down = false;
			//dragView.layout(0, 0, dragView.getWidth(), dragView.getHeight());
			break;
		}
		
		Log.d("x"+x,"oldx"+oldx);
		
		// イベント処理完了
		return true;
	}

	//アプリを終了させる
	//異常終了
	//フロントカメラ
	

}
