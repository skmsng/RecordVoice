package com.example.recordvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//ネタばらし
public class Joke extends Activity {

	private Button btn;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke);
        
        //もう一度再生ボタン
        btn = (Button)this.findViewById(R.id.button2);
        btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				voice();
			}
        });
        
        //録音音声の再生
        voice();
	}
	
	//再生
  	private void voice() {
  		//インスタンス生成
      	VoicePlayer vplayer = new VoicePlayer();
      	vplayer.play();
  	}
	
	//ごめんなさいボタンでSorryアクティビティへ
	public void sorryOnClick(View v){
		Intent intent = new Intent(this, Sorry.class);
		this.startActivity(intent);
	}
}
