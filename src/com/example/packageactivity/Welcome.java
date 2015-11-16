package com.example.packageactivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

public class Welcome extends Activity{
	Handler  handler;
	ImageView imageView;
	 private final int SPLASH_DISPLAY_LENGHT = 1000; //延迟三秒  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/**
		 * 让imageView全屏显示
		 */
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welocme);
	
		  imageView = (ImageView)findViewById(R.id.welcomeImage);
		  AnimationSet  animationset=new AnimationSet(true);
	      AlphaAnimation alphaAnimation=new AlphaAnimation(1, 0);
	      alphaAnimation.setDuration(5500);
	      animationset.addAnimation(alphaAnimation);
	      imageView.startAnimation(animationset);
	      new Handler().postDelayed(new Runnable(){ 
	        	  
	            @Override 
	            public void run() { 
	                Intent mainIntent = new Intent(Welcome.this,GForce.class); 
	                Welcome.this.startActivity(mainIntent); 
	                Welcome.this.finish(); 
	            } 
	               
	           }, SPLASH_DISPLAY_LENGHT); 
	}
		
}
