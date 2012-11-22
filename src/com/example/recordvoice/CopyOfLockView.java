package com.example.recordvoice;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CopyOfLockView extends View {
	
	private int width;
    private int height;
    private Bitmap image;
    
    public CopyOfLockView(Context context, AttributeSet attrs){
    	super(context,attrs);
    	
    	// リソースの画像ファイルの読み込み
        Resources r = context.getResources();
        image = BitmapFactory.decodeResource(r, R.drawable.lockbar);
    	
//        width = 100+bitmap.getWidth();
//        height = (120 > bitmap.getHeight())?120:bitmap.getHeight();
    }

	@Override
	protected void onDraw(Canvas canvas) {
		// イメージ描画
        canvas.drawBitmap(image, 0, 0, null);
        
        int w = image.getWidth();
        int h = image.getHeight();
        // 描画元の矩形イメージ
        Rect src = new Rect(0, 0, w, h);
        // 描画先の矩形イメージ
        Rect dst = new Rect(0, 200, w*2, 200 + h*2);
        canvas.drawBitmap(image, src, dst, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		// 引数の情報から画面の横方向の描画領域のサイズを取得する
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
//	    // Viewの描画サイズを横方向を画面端まで使う指定
	    setMeasuredDimension(width,height);
		
		//setMeasuredDimension(width,height);
	}

    
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		
		// 再描画の指示
		invalidate();
		return true;
	}
}
