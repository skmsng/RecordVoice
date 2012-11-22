package com.example.recordvoice;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class LockViewListener implements OnTouchListener {
	
	private ImageView dragView;
	private int oldx;
	//private int oldy;
	private int left;
	private int top;
	private boolean down;
	Lock lock;

	public LockViewListener(ImageView dragView, Context context) {
		this.dragView = dragView;
		this.lock = (Lock)context;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = (int)event.getX();
		//int y = (int)event.getY();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			oldx = x;
			//oldy = y;
			if(oldx < dragView.getWidth()/3) down = true;
			break;
		case MotionEvent.ACTION_MOVE:
			left = dragView.getLeft() + (x - oldx);
			//int top = dragView.getTop() + (y - oldy);
			// Viewを移動する
			if(down && left>0){
				dragView.layout(left, top, left + dragView.getWidth(), top + dragView.getHeight());
				
				if(x > dragView.getWidth()*2/3){
					lock.kaijo();
				}
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
}
