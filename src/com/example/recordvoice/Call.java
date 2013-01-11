package com.example.recordvoice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//発信中
public class Call extends Activity implements OnClickListener,Camera.PreviewCallback{//Camera.PictureCallback
	
	Timer timer;
	int counter;
	int limit = 10;	//次の画面へ移動するまでの秒
	//TextView tv2;
	MediaPlayer mp;
	//boolean onPicture;	//写真を撮ったかどうか
	int startNum;	//毎回の最初の画像ナンバー

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call);

// 		// 利用可能なカメラの個数を取得
// 	    int numberOfCameras = Camera.getNumberOfCameras();
// 	    System.out.println(numberOfCameras);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
        //終了ボタン（写真を撮る）
        button = (Button)this.findViewById(R.id.button1);
        button.setOnClickListener(this);
        //カメラをオープン
// 		camera = Camera.open();		//Android2.2まで
        // 利用可能なカメラの個数を取得//Android2.3以降のみ
 	    if(Camera.getNumberOfCameras() == 1) camera = Camera.open(0);//カメラ１つの場合
 	    else if(Camera.getNumberOfCameras() == 2) camera = Camera.open(1);//カメラ2つの場合
 	    //プレビューサイズ調整
 	    this.previewSize();
 		//プレビュー開始
 		camera.startPreview();
		
		//5秒経過でInCallアクティビティへ
        this.startTimer();
        
        //コール音の再生
        this.call();
        
        //画像ナンバーの取得
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        number = pref.getInt("number", 1);
        startNum = number;
	}



	//コール音の再生
	public void call() {
		// リソースID指定
		mp = MediaPlayer.create(this,R.raw.call_sound);
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
							//showCount();
						}
					}
				});
			}
		},0,1000);
	}
	
	//カウントの表示
//	public void showCount(){
//		tv2.setText(String.valueOf(counter));
//	}
	
	//5秒経過でInCallアクティビティへ
	public void next(){
        mp.stop();	//再生停止
        
		//if(!onPicture) camera.takePicture(null,null,null,this);
		//mp.stop();	//再生停止
        this.finish();	//このアクティビティを消滅する
		Intent intent = new Intent(this, InCall.class);
		intent.putExtra("number", this.number);
		intent.putExtra("startNum", this.startNum);
		this.startActivity(intent);
	}
	
	
	
	Button button;	//写真を撮るボタン
    Camera camera;	//カメラオブジェクト
    
	//プレビューサイズ調整
    public void previewSize(){
    	/* 画像を保存するときの画像サイズを変更 *
		//カメラパラメータオブジェクトの取得
		Camera.Parameters param = camera.getParameters();
		//カメラのピクチャーサイズを入れるインスタンス
		Size pictureSize;
		//プレビューサイズを入れる変数
		Size previewSize;
		//端末がサポートするpictureサイズを取得する
		List<Size> supportedPictureSizes = param.getSupportedPictureSizes();
		int i = 0;
		for (Size size : supportedPictureSizes) {
			System.out.println("縦の長さ：" + supportedPictureSizes.get(i).height + " 横の長さ：" + supportedPictureSizes.get(i).width);
			i++;
		}
		//端末がサポートするプレビューサイズを取得する
		i = 0;
		List<Size> supportPreviewSizes = param.getSupportedPreviewSizes();
		System.out.println("プレビューサイズ");
		for (Size size : supportPreviewSizes) {
			System.out.println("縦の長さ:" + supportPreviewSizes.get(i).height + " 横の長さ:" + supportPreviewSizes.get(i).width);
			i++;
		}
		//縦の長さ480*横の長さ640
		pictureSize = supportedPictureSizes.get(0);
		previewSize = supportPreviewSizes.get(2);
		//paramにサイズをセットする
		param.setPictureSize(pictureSize.width, pictureSize.height);
		param.setPreviewSize(previewSize.width, previewSize.height);
		//cameraにサイズをsetする
		camera.setParameters(param);
		* 画像サイズ変更終了 */
    	
    	Camera.Parameters param = camera.getParameters();
		param.setPreviewSize(640, 480);
    	camera.setParameters(param);
    }
    
	//終了ボタンを押したとき(写真を撮る)
	public void onClick(View v){
		//写真を撮った後、自動的にonPictureTaken()を呼び出す
//		camera.takePicture(null,null,null,this);
        
        //onPicture = true;	//写真を撮ったかどうか
        //button.setEnabled(false);//グレー無効状態
        //button.setClickable(false);//クリックできない状態
        //button.setSelected(false);//選択できない状態
		
		takePreviewRawData();
    }
/*	
	@Override
	//写真を撮った後、自動的に呼ばれる
	public void onPictureTaken(byte[] data, Camera c) {
		try{
			// SDカードのディレクトリ
	        File dir = Environment.getExternalStorageDirectory();
	        // アプリ名で
	        File appDir = new File(dir, "RecordVoice");
	        // ディレクトリを作る
	        if (!appDir.exists()) appDir.mkdir();
	        // ファイル名（現在時刻.jpg)
	        //String name = System.currentTimeMillis() + ".jpg";
	        String name = "testImage.jpg";
	        // 出力ファイルのパス
	        String path = new File(appDir, name).getAbsolutePath();
	        //String path = Environment.getExternalStorageDirectory()+ "/Android/" + System.currentTimeMillis()+ ".jpg";
	        
	        //写真データをファイルに書き込み
			data2file(data,path);
			
			//ギャラリーに登録（APIレベル8）
			//第1引数:context,第2引数:path配列,第3引数:MimeType配列,第4引数:OnScanCompletedリスナー
			String[] paths = {path};
			String[] mimeTypes = {"image/jpeg"};
			MediaScannerConnection.scanFile(this, paths, mimeTypes, sc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//プレビュー再開
		//camera.startPreview();
	}
	
	//写真データをファイルに書き込み
    private void data2file(byte[] data,String fileName) throws Exception {
        FileOutputStream out=null;
        try {
            out=new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            if (out!=null) {
            	out.close();
            }
            throw e;
        }
    }
*/
	
	
	//ギャラリーに登録したあと呼ばれる
    OnScanCompletedListener sc = new OnScanCompletedListener() {
		@Override
		public void onScanCompleted(String path, Uri uri) {
			
		}
	};
    
    //アクティビティ終了時
	@Override
	protected void onStop() {
		super.onStop();
//	protected void onPause(){
//		super.onPause();
		// プレビューコールバックを解除
		camera.setPreviewCallback(null);
		// プレビューを停止
        camera.stopPreview();
        // カメラをリリース
        camera.release();
        camera = null;
        //タイマーのキャンセル
        this.timer.cancel();
        //再生停止
        mp.stop();
        //画像ナンバー保存
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putInt("number", this.number);
        editor.commit();
        this.finish();	//このアクティビティを消滅する
	}
	
	//戻るボタン無効
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	            return false;
	        }else if(event.getKeyCode() == KeyEvent.KEYCODE_POWER){
	        	return false;
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
	
	private boolean finish;
	//隠しボタン（設定画面）
	public void setting(View v){
		if(finish){
			this.finish();	//このアクティビティを消滅する
			//Intent intent = new Intent("android.settings.SETTINGS");
			//startActivity(intent);
		}else{
			finish = true;
		}
	}
	
	
	
    private boolean mProgressFlag = false;	//写真保存中
    private int number ;	//写真ナンバー
    
    public void takePreviewRawData() {
        if (!mProgressFlag) {
            mProgressFlag = true;
            camera.setPreviewCallback(this);	//プレビューコールバックをセット
        }
    }
    
	//プレビューコールバック(自動的に呼ばれる)
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		
		camera.setPreviewCallback(null);  // プレビューコールバックを解除
        camera.stopPreview();	//プレビューを一端停止
        
        //final int width = 640;//getWidth();	//プレビューの幅
        //final int height = 320;//getHeight();	//プレビューの高さ
        Size size = camera.getParameters().getPreviewSize();
        int width = size.width;
        int height = size.height;
        //int[] rgb = new int[width * height]; //画素配列
        
        try {
        	//ARGB8888で空のビットマップ作成
        	Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        	
        	//decodeYUV420SP(rgb, data, width, height); //変換
        	int[] rgb = decodeYUV(data, width, height);
        	bmp.setPixels(rgb, 0, width, 0, 0, width, height);//変換した画素からビットマップにセット
        	
        	//90°回転
        	Matrix matrix = new Matrix();
            matrix.postRotate(90.0f);
        	bmp = Bitmap.createBitmap(bmp, 0, 0, size.width, size.height, matrix, true);
        	
        	// 画像を保存
        	// SDカードのディレクトリ
	        File dir = Environment.getExternalStorageDirectory();
	        // アプリ名で
	        File appDir = new File(dir, "RecordVoice");
	        // ディレクトリを作る
	        if (!appDir.exists()) appDir.mkdir();
	        // ファイル名（現在時刻.jpg)
//	        String name = System.currentTimeMillis() + ".jpg";
	        String name = "joke"+ number++ +".jpg";
	        // 出力ファイルのパス
	        String path = new File(appDir, name).getAbsolutePath();
	        
        	File file = new File(path);
        	FileOutputStream fos = new FileOutputStream(file);
        	
        	//bmpから変換
        	bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
			
			//ギャラリーに登録（APIレベル8）
			//第1引数:context,第2引数:path配列,第3引数:MimeType配列,第4引数:OnScanCompletedリスナー
			String[] paths = {path};
			String[] mimeTypes = {"image/jpeg"};
			MediaScannerConnection.scanFile(this, paths, mimeTypes, sc);
            
        } catch (IOException e) {
            Log.e("CAMERA", e.getMessage());
        } catch (Exception e){
        	e.printStackTrace();
        }
        camera.startPreview();	//プレビュー再開
        mProgressFlag = false;
	}
	
/*	
	public static final void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
		Log.d("","rgb:"+rgb.length + " yuv420sp:"+yuv420sp.length + " width:"+width + " height:"+height);
		final int frameSize = width * height;
		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0) y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}
				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);
				if (r < 0) r = 0; else if (r > 262143) r = 262143;
				if (g < 0) g = 0; else if (g > 262143) g = 262143;
				if (b < 0) b = 0; else if (b > 262143) b = 262143;
				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}
*/	
	public static int[] decodeYUV(byte[] tempData, int width, int height) throws NullPointerException, IllegalArgumentException {
        int size = width * height;
        if (tempData == null) {
            throw new NullPointerException("buffer tempData is null");
        }
        if (tempData.length < size) {
            throw new IllegalArgumentException("buffer tempData is illegal");
        }

        int[] out = new int[size];

        int Y, Cr = 0, Cb = 0;
        for (int i = 0; i < height; i++) {
            int index = i * width;
            int jDiv2 = i >> 1;
            for (int i2 = 0; i2 < width; i2++) {
                Y = tempData[index];
                if (Y < 0) {
                    Y += 255;
                }
                if ((i2 & 0x1) != 1) {
                    int c0ff = size + jDiv2 * width + (i2 >> 1) * 2;
                    Cb = tempData[c0ff];
                    if (Cb < 0) {
                        Cb += 127;
                    } else {
                        Cb -= 128;
                    }
                    Cr = tempData[c0ff + 1];
                    if (Cr < 0) {
                        Cr += 127;
                    } else {
                        Cr -= 128;
                    }
                }

                // red
                int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }
                // green
                int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }
                // blue
                int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                out[index] = 0xff000000 + (B << 16) + (G << 8 ) + R;
                index++;
            }
        }
        Log.d("","rgb:"+out.length + " width:"+width + " height:"+height);
        return out;
    }
}
