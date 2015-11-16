package com.example.packageactivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StepActivity extends Activity{
	/**
	 *  Action
	 */
	String actionAddress = "com.example.getdate";
	Handler handler;
	IntentFilter hasReceiverFilter;
	DrawApplication mApp;
	Button stepcount_button;
	Button stepend_button;
	Button stepZero_button;
	TextView stepText;
	EditText editDistance;
	/**
	 * 停止线程action
	 */
	String threadStop = "com.example.stopthread";
	/**
	 * 接收身高值
	 */
	String setDvalue = "com.example.Distancevalue";
	/**
	 * 返回K值
	 */
	String getKvalue = "com.example.Kvalue";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stepcount);
		
		mApp = (DrawApplication)StepActivity.this.getApplication();
		stepcount_button = (Button)findViewById(R.id.stepcount_button);
		stepend_button = (Button)findViewById(R.id.stepend_button);
		stepZero_button= (Button)findViewById(R.id.stepZeor_button);
		stepText = (TextView)findViewById(R.id.myStepText);
		editDistance =(EditText) findViewById(R.id.editText);
	
		hasReceiverFilter = new IntentFilter(actionAddress);
	    this.registerReceiver(HasReceiver, hasReceiverFilter);
	    IntentFilter KvalueFilter = new IntentFilter(getKvalue);
	    this.registerReceiver(HasReceiver, KvalueFilter);
	       
	        /**
	         * step 监听器
	         */
	    stepcount_button.setOnClickListener(new Button.OnClickListener() {
				
				@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mApp.stepAllcount = 0;
				//mApp.isStepBegin = true;
				mApp.PractiseDistance = Float.parseFloat(editDistance.getText().toString()); 
				Intent setvalue = new Intent();
				setvalue.setAction(setDvalue);
				sendBroadcast(setvalue);
				Log.e("发送距离广播", "ok");
				Toast.makeText(getApplicationContext(), "开始走"+ mApp.PractiseDistance, Toast.LENGTH_SHORT).show();
				
			}
		});
	    	
	    stepend_button .setOnClickListener(new Button.OnClickListener() {
						
				@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mApp.isStepBegin = false;
				
			}
					});
	    	
	    stepZero_button .setOnClickListener(new Button.OnClickListener() {
				
				@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mApp.isStepBegin = false;
				mApp.stepCount_num = 0;
				mApp.stepAllcount = 0;
				stepText.setText("步数:"+mApp.stepCount_num);
				stepText.setTextColor(Color.RED);
				
				
			}
			});
	    	 /**
	         * 更新步数Handler
	         */
        handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					stepText.setText("步数:"+mApp.stepAllcount);
					stepText.setTextColor(Color.RED);
					break;

				default:
					break;
				}
	
			}
        	
        };
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mApp.isStepBegin = false;
		mApp.stepAllcount = 0;
		mApp.stepCount_num = 0;
		Intent secintent = new Intent();
		secintent.setAction(threadStop);
		sendBroadcast(secintent);
		super.onBackPressed();
	
	}
	private final BroadcastReceiver HasReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			String action = intent.getAction();
			
			if(action.equals(actionAddress)){			
				if(mApp.isStepBegin == true)
					{
					mApp.stepAllcount = mApp.stepAllcount+ mApp.stepCount_num;
					Message myMessage = new Message();
					myMessage.what = 1;
					handler.sendMessage(myMessage);
					}		
			}
			if(action.equals(getKvalue))
			{	
		       stepText.setText("K值："+ mApp.KValue);
		    }
		}
	};
}
