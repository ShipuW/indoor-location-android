package com.example.packageactivity;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SeocndActivity extends Activity implements OnGestureListener{
	/**
	 * 全局变量
	 */
	DrawApplication mApp;
	/**
	 * 画笔1（画平面图，为黑色）
	 */
	Paint paint;
	/**
	 * 画笔2（画点，为红色）
	 */
	Paint redPaint;
	/**
	 * 进行标注
	 */
	Paint RectPaint;
	/**
	 * 画笔3（画Text）
	 */
	Paint InterPaint;
	
	Paint GreenPaint;
	/**
	 * 画板
	 */
	Canvas canvas;
	/**
	 * 画板
	 */
	Canvas newCanvas ;


	/**
	 * Action名字广播
	 */
	String actionAddress = "com.example.getdate";
	/**
	 * 用于更新姿态角的广播
	 */
	String actionOther = "com.example.getdateother";
	/**
	 * 发送清0广播的Sting
	 */
	String string_clearZero = "com.example.clearzero";
	
	/**
	 * 停止线程action
	 */
	String threadStop = "com.example.stopthread";
	/**
	 * 停止线程action
	 */
	String tPaint_ion = "com.example.tPaint_ion";
	
	String math_sqrt_ion = "com.example.mathsqrt";
	/**
	 * SD卡路径
	 */
	String SDPATH = Environment.getExternalStorageDirectory() + "/";
	/**
	 * 图片名称
	 */
	String savePngName = "";

	Timer updateTimer;
	int StepNum = 0;
	float distance = 0;
	float Shift = 0;

	ImageView myImageView;	
	IntentFilter hasReceiverFilter;
	IntentFilter ReceiverFilter;
	IntentFilter clearReceiverFilter;
	IntentFilter sqrtFilter;
	Drawable myGameDrawable;
	Handler handler;
	
	Bitmap bitmap;
	Bitmap Abitmap;
	Bitmap Bbitmap;
	Bitmap Rbitmap;
	Canvas Acanvas;
	Canvas Bcanvas ;
	Canvas resultcanvas;
	LinearLayout mylayout;
	int i = 100;
	Button mButton;
	/**
	 *  手势
	 */
	private GestureDetector gd; 

	/**
	 *  获取屏幕高宽
	 */
	Display display;
	/**
	 * 屏幕宽度
	 */
	int width;
	/**
	 * 屏幕高度
	 */
	int height;
	/**
	 * 移动后屏幕宽度位置
	 */
	int newWidth ;
	/**
	 * 移动后屏幕高度位置
	 */
	int newHeight ;
	/**
	 * 图片宽度
	 */
	int LastWidth ;
	/**
	 * 图片高度
	 */
	int LastHeight ;
	/**
	 * 标志相对距离x
	 */
	float Flagx = 0;
	/**
	 * 标志相对距离y
	 */
	float Flagy = 0;
	/**
	 * 标志点坐标x
	 */
	float FlagPointx = 0;
	/**
	 * 标志点坐标y
	 */
	float FlagPointy = 0;
	float[][] map = new float[2][3000];
	
	int map_Num = 0;
	private int verticalMinDistance = 0;  
	private int minVelocity         = 0; 
	
	int Xdeta = 0;
	int Ydeta = 0;
	float  GetX = 0;
	float  GetY = 0;
	TextView mytext;
	TextView mytext_ori;
	int AddWidth;
	int AddHeight;
	
	Boolean var_falg = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
    
		/**
		 *  去Title
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drawlayout);
		getWindow().setFlags(0x08000000, 0x08000000); 
		/**
		 *  初始化
		 */
		initViews();
		

		/**
		 *  画一个简单的一教平面图
		 */
		DrawFunction();    	 
	}
	
	private void initViews() {
		// TODO Auto-generated method stub
		mytext =(TextView) findViewById(R.id.draw_textview);
		mytext_ori = (TextView) findViewById(R.id.draw_textview_ori);
		hasReceiverFilter = new IntentFilter(actionAddress);
        this.registerReceiver(HasReceiver, hasReceiverFilter);
		ReceiverFilter = new IntentFilter(actionOther);
        this.registerReceiver(HasReceiver, ReceiverFilter);
		sqrtFilter = new IntentFilter(math_sqrt_ion);
        this.registerReceiver(HasReceiver, sqrtFilter);
        mApp = (DrawApplication)SeocndActivity.this.getApplication();
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);  
        mylayout = (LinearLayout)findViewById(R.id.draw_main);
        
        paint = new Paint();    
        redPaint = new Paint();
        InterPaint = new Paint();
        RectPaint = new Paint();
        
        
        // 2013.11.20 Devin
        GreenPaint = new Paint();
        GreenPaint.setStrokeWidth(4);  
        GreenPaint.setColor(Color.GREEN);//设置为绿笔
        GreenPaint.setAntiAlias(true);  
        
        
        paint.setStrokeWidth(2);  //线宽
        paint.setColor(Color.BLACK);//设置为黑笔  
        paint.setAntiAlias(true);
       
        RectPaint.setStrokeWidth(6);  //线宽
        RectPaint.setColor(Color.BLACK);//设置为黑笔  
        RectPaint.setStyle(Style.STROKE);//这样画矩形就为空心了
        
        redPaint.setStrokeWidth(4);  
        redPaint.setColor(Color.RED);//设置为红笔   
        redPaint.setAntiAlias(true);  
        
        InterPaint.setStrokeWidth(4);  
        InterPaint.setColor(Color.BLUE);//设置为红笔   
        InterPaint.setAntiAlias(true);  
        
        width =getWindowManager().getDefaultDisplay().getWidth();
        height =getWindowManager().getDefaultDisplay().getHeight();
        LastWidth = 3*width;
        LastHeight = 3*height;
        newWidth = LastWidth/2 - width/2;
        newHeight = LastHeight/2 -height/2;
        
        Abitmap = Bitmap.createBitmap(LastWidth, LastHeight, Bitmap.Config.ARGB_8888); //设置位图的宽高,bitmap为透明  
        Acanvas =new Canvas(Abitmap);
        Acanvas.drawColor(Color.WHITE);
        
        Bbitmap = Bitmap.createBitmap(LastWidth, LastHeight, Bitmap.Config.ARGB_8888); //设置位图的宽高,bitmap为透明  
        Bcanvas =new Canvas(Bbitmap);
        Bcanvas.drawColor(Color.WHITE);
		Bcanvas.drawBitmap(Abitmap, 0, 0, paint);
        bitmap=Bitmap.createBitmap(Abitmap, newWidth, newHeight, width, height);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        
        Rbitmap = Bitmap.createBitmap(Abitmap, 10, 10, 200, 170);
        resultcanvas = new Canvas(Rbitmap);
        resultcanvas.drawColor(Color.WHITE);
        
        display = getWindowManager().getDefaultDisplay();     
        gd=new GestureDetector(this);
        //创建手势监听对象
        updateTimer = new Timer("gForceUpdate");
        handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					
					mApp.test_Num += mApp.SignCountNum;
					distance = mApp.distance;
					Shift = mApp.Shift;
					mApp.isFirstPoint++;
					if (mApp.StepsNum<=5) {
						if(var_falg == true)
						{
							if(mApp.var_m50 == 1)
							{
								var_falg = false;
								AlertDialog.Builder builder = new AlertDialog.Builder(SeocndActivity.this);
								builder.setTitle("存在干扰！");
								
								final TextView text = new TextView(SeocndActivity.this);
								
								builder.setView(text);  
								
								text.setText("干扰时启动可能导致最终轨迹误差较大,建议更换起始点继续行走！");
								text.setTextSize(20);
								text.setTextColor(Color.argb(255, 255, 255, 255));
								
								builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										var_falg = true;
									}
								});
								builder.show();
								
							
//								Log.e("inter", "mApp.var_m50"+mApp.var_m50);	
							}
						}
						
//						Log.e("inter", "mApp.StepsNum"+mApp.StepsNum);
					}
					if(mApp.SignCountNum!=0){
//						if(mApp.sqrt_zero == true)
//						{
//							mApp.sqrt_zero = false;
//							Toast.makeText(getApplicationContext(), "零偏过大，陀螺无法使用", Toast.LENGTH_SHORT).show();
//							Log.e("zero_f", "begin222" );
//						}
						for(int i=0;i<mApp.SignCountNum;i++)
						{
							if(mApp.CheckBoxTwoFlag==false){
							if(mApp.startPointX+mApp.pointArray[2*i]>width||mApp.startPointX+mApp.pointArray[2*i]<=0||mApp.startPointY-mApp.pointArray[2*i+1]>height||mApp.startPointY-mApp.pointArray[2*i+1]<=0)
							{
								Xdeta = 0;
								Ydeta = 0;
								if(mApp.startPointX+mApp.pointArray[2*i]<0)
								{
									AddWidth = newWidth;
									newWidth = newWidth - width/12;
									Xdeta = width/12;	
									if(newWidth <=0)
									{
										newWidth = 0;
										Xdeta = newWidth;
									}
									
								}
								else if(mApp.startPointX+mApp.pointArray[2*i]>width)
								{		
									newWidth = newWidth + width/12;
									Xdeta = -width/12;
									if(newWidth ==LastWidth - width)
									{
										newWidth = LastWidth - width;
									}
								}
								
								if(mApp.startPointY-mApp.pointArray[2*i+1]<0)
								{
									
									AddHeight = newHeight;
									newHeight = newHeight - height/12;
									Ydeta = height/12;	
									if(newHeight <=0)
									{
										newHeight = 0;
										Xdeta = AddHeight;
									}
									
								}
								else if (mApp.startPointY-mApp.pointArray[2*i+1]>height)
								{
									
									AddHeight = LastHeight - newHeight;
									newHeight = newHeight + height/12;
									Ydeta = -height/12;
									if(newHeight >=LastHeight - height)
									{
										newHeight = LastHeight - height;
										Ydeta = AddHeight;
									}
								}
								mApp.startPointX = mApp.startPointX+Xdeta;
						    	mApp.startPointY = mApp.startPointY+Ydeta;																				
						    	bitmap=Bitmap.createBitmap(Abitmap,(int) (newWidth), (int)(newHeight), width, height);
						        canvas.setBitmap(bitmap);
						        Drawable Begindrawable = new BitmapDrawable(bitmap) ;  
								mylayout.setBackgroundDrawable(Begindrawable); 
								
							}
							}
							if(mApp.startPointX+mApp.pointArray[2*i]>=0&&mApp.startPointX+mApp.pointArray[2*i]<=width&&mApp.startPointY-mApp.pointArray[2*i+1]<=height&&mApp.startPointY-mApp.pointArray[2*i+1]>=0)
							{
								if(mApp.var_m50 == 1)
								{
									canvas.drawPoint((mApp.startPointX+mApp.pointArray[2*i]),(mApp.startPointY-mApp.pointArray[2*i+1]) , InterPaint);	
								}
								else if(mApp.var_m50 == 0)
								{
									canvas.drawPoint((mApp.startPointX+mApp.pointArray[2*i]),(mApp.startPointY-mApp.pointArray[2*i+1]) , redPaint);	
								}
								else
								{
									canvas.drawPoint((mApp.startPointX+mApp.pointArray[2*i]),(mApp.startPointY-mApp.pointArray[2*i+1]) , GreenPaint);	
								}

							}
							else
							{
								if(mApp.var_m50 == 1)
								{
									Acanvas.drawPoint((newWidth+mApp.startPointX+mApp.pointArray[2*i]),(newHeight+mApp.startPointY-mApp.pointArray[2*i+1]) , InterPaint);	
								}
								else if(mApp.var_m50 == 0)
								{
									Acanvas.drawPoint((newWidth+mApp.startPointX+mApp.pointArray[2*i]),(newHeight+mApp.startPointY-mApp.pointArray[2*i+1]) , redPaint);	
								}
								else
								{
									Acanvas.drawPoint((newWidth+mApp.startPointX+mApp.pointArray[2*i]),(newHeight+mApp.startPointY-mApp.pointArray[2*i+1]) , GreenPaint);		
								}						
							}
							Flagx = mApp.pointArray[2*i];
							Flagy = mApp.pointArray[2*i+1];
							canvas.drawBitmap(bitmap, 0, 0, paint);
							Acanvas.drawBitmap(bitmap, newWidth, newHeight, paint);
							Acanvas.drawBitmap(Abitmap, 0, 0,paint);
							mylayout.invalidate();
							
							// DecimalFormat df = new DecimalFormat("#.0");
							mApp.Head =get_accuracy(mApp.Head, 2);
							mApp.Pitch=get_accuracy(mApp.Pitch, 2 );
							mApp.Roll =get_accuracy(mApp.Roll, 2 );
							mApp.magnetic=get_accuracy(mApp.magnetic, 2);
							mApp.distance =get_accuracy(mApp.distance, 2 );
							mApp.Shift =get_accuracy(mApp.Shift, 2 );
							mApp.ore_x=get_accuracy(mApp.ore_x, 2 );
							mApp.ore_y =get_accuracy(mApp.ore_y, 2 );
							mApp.ore_z =get_accuracy(mApp.ore_z, 2);
							
							mApp.zeroPose = get_accuracy(mApp.zeroPose, 2);
							mApp.var_m50 = get_accuracy(mApp.var_m50, 2);
							mApp.poly = get_accuracy(mApp.poly, 2);
							
							mApp.zeroX = get_accuracy(mApp.zeroX, 3);
							mApp.zeroY = get_accuracy(mApp.zeroY, 3);
							mApp.zeroZ = get_accuracy(mApp.zeroZ, 3);
							mApp.range = get_accuracy(mApp.range, 2);
							mApp.mm = get_accuracy(mApp.mm, 2);
							/*
							mytext.setText("\n"+"  航向角："+mApp.Head+"\n"+"  俯仰角："+mApp.Pitch+"\n"+"  横滚角："+mApp.Roll+"\n"+
							"  磁力计："+mApp.mm+"\n"+"  总步数："+mApp.StepsNum+"\n"+"  总距离："+mApp.distance+"\n"+"  总偏差："
									+mApp.Shift+"m"+"\n"+  "  零偏："+mApp.zeroX+" "+mApp.zeroY+"   "+mApp.zeroZ);
							
							mytext_ori.setText("\n"+"  参考航向角："+mApp.ore_x+"\n"+"  参考俯仰角："+mApp.ore_y+"\n"+"  参考横滚角："+mApp.ore_z+"\n " +
									" 参考航向：" + mApp.zeroPose + "\n" + "  差值：" + get_accuracy(mApp.Head - mApp.zeroPose, 2) + "\n" + "  干扰：" 
									+ mApp.var_m50 + "\n" + "  拟合：" + mApp.poly + "\n" + "range：" + mApp.range + "\n");
									*/
							mytext.setText("\n"+"  航向角："+mApp.Head+"\n"+"  俯仰角："+mApp.Pitch+"\n"+"  横滚角："+mApp.Roll+"\n"+
									"  磁力计："+mApp.mm+"\n"+"  总步数："+mApp.StepsNum+"\n"+"  总距离："+mApp.distance+"\n"+"  总偏差："
											+mApp.Shift+"m");
							mytext_ori.setText("\n"+"  参考航向角："+mApp.ore_x+"\n"+"  参考俯仰角："+mApp.ore_y+"\n"+"  参考横滚角："+mApp.ore_z+"\n " +
									" 参考航向：" + mApp.zeroPose + "\n" + "  差值：" + get_accuracy(mApp.Head - mApp.zeroPose, 2) + "\n" + "  干扰：" 
									+ mApp.var_m50);
						}
					}
					
					mApp.SignCountNum = 0;
					
					break;
				case 2:
					mApp.isGo = true;
					Toast.makeText(getApplicationContext(), "延时2秒", Toast.LENGTH_SHORT).show();
					break;
				case 3:
				//	Log.e("mApp.Shift"," "+mApp.Shift);
					mApp.Head =get_accuracy(mApp.Head, 2 );
					mApp.Pitch=get_accuracy(mApp.Pitch, 2 );
					mApp.Roll =get_accuracy(mApp.Roll , 2);
					mApp.magnetic=get_accuracy(mApp.magnetic, 2);
					mApp.distance =get_accuracy(mApp.distance, 2 );
					mApp.Shift =get_accuracy(mApp.Shift , 2);
					mApp.ore_x=get_accuracy(mApp.ore_x , 2);
					mApp.ore_y =get_accuracy(mApp.ore_y , 2);
					mApp.ore_z =get_accuracy(mApp.ore_z, 2);
					
					mApp.zeroPose = get_accuracy(mApp.zeroPose, 2);
					
					mApp.var_m50 = get_accuracy(mApp.var_m50, 2);
					mApp.poly = get_accuracy(mApp.poly, 2);
					mApp.zeroX = get_accuracy(mApp.zeroX, 3);
					mApp.zeroY = get_accuracy(mApp.zeroY, 3);
					mApp.zeroZ = get_accuracy(mApp.zeroZ, 3);
	
					
					mApp.range = get_accuracy(mApp.range, 2);
					mApp.mm = get_accuracy(mApp.mm, 2);
					
					/*
					mytext.setText("\n"+"  航向角："+mApp.Head+"\n"+"  俯仰角："+mApp.Pitch+"\n"+"  横滚角："+mApp.Roll+"\n"+
					"  磁力计："+mApp.mm+"\n"+"  总步数："+mApp.StepsNum+"\n"+"  总距离："+mApp.distance+"\n"+"  总偏差："
							+mApp.Shift+"m"+"\n"+  "  零偏："+mApp.zeroX+" "+mApp.zeroY+"   "+mApp.zeroZ);
					
					mytext_ori.setText("\n"+"  参考航向角："+mApp.ore_x+"\n"+"  参考俯仰角："+mApp.ore_y+"\n"+"  参考横滚角："+mApp.ore_z+"\n " +
							" 参考航向：" + mApp.zeroPose + "\n" + "  差值：" + get_accuracy(mApp.Head - mApp.zeroPose, 2) + "\n" + "  干扰：" 
							+ mApp.var_m50 + "\n" + "  拟合：" + mApp.poly + "\n" + "range：" + mApp.range + "\n");
							*/
					mytext.setText("\n"+"  航向角："+mApp.Head+"\n"+"  俯仰角："+mApp.Pitch+"\n"+"  横滚角："+mApp.Roll+"\n"+
							"  磁力计："+mApp.mm+"\n"+"  总步数："+mApp.StepsNum+"\n"+"  总距离："+mApp.distance+"\n"+"  总偏差："
									+mApp.Shift+"m");
					mytext_ori.setText("\n"+"  参考航向角："+mApp.ore_x+"\n"+"  参考俯仰角："+mApp.ore_y+"\n"+"  参考横滚角："+mApp.ore_z+"\n " +
							" 参考航向：" + mApp.zeroPose + "\n" + "  差值：" + get_accuracy(mApp.Head - mApp.zeroPose, 2) + "\n" + "  干扰：" 
							+ mApp.var_m50);
					
					break;
				case 4:
					mApp.pMean = true;
					break;
				case 5:
					AlertDialog.Builder builder = new AlertDialog.Builder(SeocndActivity.this);
					builder.setTitle("警告");
					builder.setMessage("陀螺不可用");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
					break;
				default:
					break;
				}

	
				super.handleMessage(msg);
			}

			
        };   

	}

	private float get_accuracy(float head, int bit) {
		// TODO Auto-generated method stub
		if(bit == 3)
			return (float)(Math.round(head*1000))/1000;
		else
			return (float)(Math.round(head*100))/100;
	}	
	
	private void DrawFunction() {
		// TODO Auto-generated method stub
			//绘制边框图形区域

        canvas.drawLine(10, 10, getWindowManager().getDefaultDisplay().getWidth()-10, 10, paint); 
        canvas.drawLine(10, 10, 10,getWindowManager().getDefaultDisplay().getHeight()-10, paint); 
        canvas.drawLine(10, getWindowManager().getDefaultDisplay().getHeight()-10, getWindowManager().getDefaultDisplay().getWidth()-10, getWindowManager().getDefaultDisplay().getHeight()-10, paint); 
        canvas.drawLine(getWindowManager().getDefaultDisplay().getWidth()-10, 10, getWindowManager().getDefaultDisplay().getWidth()-10,getWindowManager().getDefaultDisplay().getHeight()-10, paint); 
        paint.setTextSize(20); 

		canvas.drawBitmap(bitmap, 0, 0,paint);
		Acanvas .drawBitmap(bitmap,newWidth, newHeight, paint);
		Acanvas.drawBitmap(Abitmap, 0, 0,null);	
        Drawable Begindrawable = new BitmapDrawable(bitmap) ;  
        mylayout.setBackgroundDrawable(Begindrawable); 

	}	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mApp.last_x=0;
		mApp.last_y=0;
		mApp.countNum = 0;
		mApp.startPointX = 0;
		mApp.startPointY =0;
		mApp.isGoPaint = false;
		mApp.distance = 0;
		mApp.Shift = 0;
		mApp.test_Num = 0;
		mApp.Head = 0;
		mApp.Roll = 0;
		mApp.Pitch = 0;
		mApp.magnetic = 0;
		mApp.StepsNum = 0;
		mApp.Hi = 0;
		mApp.pMean = false;
		mApp.p_result = 0;
		mApp.testPaintFlag = false;
		mApp.zeroPose = 0;
		
		if(updateTimer!=null)
		{
			updateTimer.cancel();
			updateTimer = null;
		}
		Intent Stopintent = new Intent();
		Stopintent.setAction(threadStop);
		sendBroadcast(Stopintent);
		mApp.isGo = false; 
		unregisterReceiver(HasReceiver);
//		Log.e("mApp.distance"," "+mApp.distance);
//		Log.e("acc", "end");
		super.onBackPressed();
	}

	private final BroadcastReceiver HasReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			String action = intent.getAction();
			if (action.equals(actionAddress)) {
				Message myMessage = new Message();
				myMessage.what = 1;
				handler.sendMessage(myMessage);
				//Log.e("发送UI更新广播","发送UI更新广播");	
			
			}
		if (action.equals(actionOther)) {
			Message myMessage = new Message();
			myMessage.what = 3;
			handler.sendMessage(myMessage);
		}
		if (action.equals(math_sqrt_ion)) {
			Message myMessage = new Message();
			myMessage.what = 5;
			handler.sendMessage(myMessage);
		}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
	
		super.onDestroy();
	}
	


	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event); //通知手势识别方法 

		return true;
	}
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		 if(mApp.CheckBoxTwoFlag == true)
		 {
	    if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) 
	    { 
	    	if(newWidth+(e1.getX() - e2.getX())>LastWidth - width)
	    	{
	    		if(newWidth == LastWidth - width)
	    		{
	    			Xdeta = 0;
	    		}
	    		else
	    		{
	    			Xdeta = -( LastWidth - width - newWidth);
	    		}
	    		newWidth = LastWidth - width;
	    		
	    		Toast.makeText(this, "移动超过边框", Toast.LENGTH_SHORT).show(); 
	    	}
	    	else
	    	{
				newWidth = (newWidth+(int)(e1.getX() - e2.getX()));
			  	Xdeta = -(int)( e1.getX() - e2.getX());
	    	}
	    	
	        
	    } 
	    else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) 
	    {  
	    	if(newWidth+(e1.getX() - e2.getX())<0)
	    	{
	    		if(newWidth == 0)
	    		{
	    			Xdeta = 0;
	    		}
	    		else
	    		{
	    			Xdeta = newWidth;
	    		}
	    		newWidth = 0;
	    		
	    		Toast.makeText(this, "移动超过边框", Toast.LENGTH_SHORT).show(); 
	    	}
	    	else 
	    	{
	    		newWidth = newWidth+(int)(e1.getX() - e2.getX()); 
	    		Xdeta = -(int)( e1.getX() - e2.getX());
	    	}
	    }  
	    
	    if (e1.getY() - e2.getY() > verticalMinDistance && Math.abs(velocityY) > minVelocity) { 
	    	if(newHeight+(e1.getY() - e2.getY())>LastHeight -height)
	    	{
	    		if(newHeight == LastHeight -height)
	    		{
	    			Ydeta = 0;
	    		}
	    		else
	    		{
	    			Ydeta = -( LastHeight -height - newHeight);
	    		}
	    		newHeight = LastHeight -height;
	    		
	    		Toast.makeText(this, "移动超过边框", Toast.LENGTH_SHORT).show(); 
	    	}
	    	else
	    	{
	    		newHeight = (newHeight+(int)(e1.getY() - e2.getY()));
	    		Ydeta = -(int)( e1.getY() - e2.getY());
	    	}
	    	
	        
	    } 
	    else if (e2.getY() - e1.getY() > verticalMinDistance && Math.abs(velocityY) > minVelocity) 
	    {  
	    	if(newHeight+(e1.getY() - e2.getY())<0)
	    	{
	    		if(newHeight == 0)
	    		{
	    			Ydeta = 0;
	    		}
	    		else
	    		{
	    			Ydeta = newHeight;
	    		}
	    		newHeight = 0;
	    		
	    		Toast.makeText(this, "移动超过边框", Toast.LENGTH_SHORT).show(); 
	    	}
	    	else 
	    	{
	    		newHeight = newHeight+(int)(e1.getY() - e2.getY()); 
	    		Ydeta = -(int)( e1.getY() - e2.getY());
	    	}
	    } 
    	mApp.startPointX = mApp.startPointX+Xdeta;
    	mApp.startPointY = mApp.startPointY+Ydeta;
    	bitmap=Bitmap.createBitmap(Abitmap,(int) (newWidth), (int)(newHeight), width, height);
        canvas.setBitmap(bitmap);	     
		Acanvas.drawBitmap(bitmap, (int)newWidth, (int)newHeight, paint);
		Drawable Begindrawable = new BitmapDrawable(bitmap) ;  
		mylayout.setBackgroundDrawable(Begindrawable); 
        mylayout.invalidate();
	}
		return false;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		GetX = e.getX();
		GetY = e.getY();
		AlertDialog.Builder builder = new AlertDialog.Builder(SeocndActivity.this);
		builder.setTitle("选择");
	
		builder.setPositiveButton("开始走", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mApp.startPointX =  GetX ;
				mApp.startPointY =  GetY;
				mApp.last_x= 0;
				mApp.last_y= 0;	
				mApp.countNum=0;
				
				mApp.zeroPose = 0;	// 每次重新走时参考航向清零
				mApp.var_m50 = 0;
				
				mApp.isGoPaint = true;
				Message message = new Message();
				message.what = 2;
				handler.sendMessageDelayed(message, 2000);
				
				Message message_p = new Message();
				message_p .what = 4;
				handler.sendMessage(message_p);
				
				canvas.drawPoint(mApp.startPointX,mApp.startPointY , redPaint);	
				canvas.drawBitmap(bitmap, 0, 0,paint);
				Acanvas.drawBitmap(bitmap, newWidth, newHeight, paint);
				mylayout.invalidate();
				//Log.e("画第一个点"+mApp.startPointX, "画第一个点"+mApp.startPointY );
				Toast.makeText(getApplicationContext(), "你长按了屏幕", Toast.LENGTH_SHORT).show();
				if(mApp.testPaintFlag == true)
				{
				Intent tPaint_intent = new Intent();
				 tPaint_intent.setAction(tPaint_ion);
				sendBroadcast(tPaint_intent);
				}
			}
		});
		builder.setNegativeButton("保存图片", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mApp.isGo = false;
				Intent Stopintent = new Intent();
				Stopintent.setAction(threadStop);
				sendBroadcast(Stopintent);
				mApp.Head =get_accuracy(mApp.Head, 2);
				mApp.Pitch=get_accuracy(mApp.Pitch, 2);
				mApp.Roll =get_accuracy(mApp.Roll, 2);
				mApp.magnetic=get_accuracy(mApp.magnetic, 2);
				mApp.distance =get_accuracy(mApp.distance, 2 );
				mApp.Shift =get_accuracy(mApp.Shift, 2 );
				mApp.ore_x=get_accuracy(mApp.ore_x, 2 );
				mApp.ore_y =get_accuracy(mApp.ore_y, 2 );
				mApp.ore_z =get_accuracy(mApp.ore_z, 2);
				resultcanvas.drawText("航向角："+mApp.Head, 5, 35, paint);
				resultcanvas.drawText("俯仰角："+mApp.Pitch, 5, 55, paint);
				resultcanvas.drawText("横滚角："+mApp.Roll, 5, 75, paint);
				resultcanvas.drawText("磁力计："+mApp.magnetic, 5, 95, paint);
				resultcanvas.drawText("总步数："+mApp.StepsNum, 5, 115, paint);
				resultcanvas.drawText("总距离："+mApp.distance, 5, 135, paint);
				resultcanvas.drawText("总偏差："+mApp.Shift+"m", 5, 155, paint);	
				resultcanvas.drawText("参考航向角："+mApp.ore_x+"m", 400, 35, paint);
				resultcanvas.drawText("参考俯仰角："+mApp.ore_y, 400, 55, paint);
				resultcanvas.drawText("参考横滚角："+mApp.ore_z, 400, 75, paint);
				//resultcanvas.drawText("高度："+mApp.Hi, 5, 175, paint);	
				Acanvas.drawBitmap(Rbitmap,  15, 15, paint);
				writePic();	
				resultcanvas.drawColor(Color.WHITE);
				Acanvas.drawBitmap(Rbitmap, 15, 15, paint);
			    Toast.makeText(getApplicationContext(), "保存成功，文件名为:"+savePngName, Toast.LENGTH_SHORT).show();
				 }

			private void writePic() {
				// TODO Auto-generated method stub
				Acanvas.save();
				Acanvas.restore();
				Date date = new Date(System.currentTimeMillis());
				savePngName = "轨迹";
				savePngName = savePngName+"-"+(date.getMonth()+1)+"-"+date.getDate()+"-"+date.getHours()+"-"+date.getMinutes()+"-"+date.getSeconds();
				savePngName = mApp.paintName;
				FileUtils fileUtils = new FileUtils();
				try 
					{
						fileUtils.creatSDFile(savePngName +".png");
					} catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				 FileOutputStream fos = null;
			     try 
			        {
			    	 	File Fileoutput = new File(SDPATH + savePngName +".png");
				        fos = new FileOutputStream(Fileoutput);
				        Abitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				        } 
			     catch (Exception e) 
				    {
				        e.printStackTrace();
			        } 
			        finally 
			        {
				        if (fos != null)
				        {
				        try 
				        {
				            fos.close();
				        } catch (IOException e)
				        {
				           e.printStackTrace();
				        }
				        }
			        }
				
			}
		});
		builder.setNeutralButton("标注", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(mApp.isGoPaint = true)
				{
					FlagPointx = mApp.startPointX+Flagx;
					FlagPointy = mApp.startPointY-Flagy;
					if(FlagPointx>=0&&FlagPointx<=width&&FlagPointy<=height&&FlagPointy>=0)
					{
						canvas.drawPoint(FlagPointx,FlagPointy, RectPaint);	
					}
					else
					{
						Acanvas.drawPoint(newWidth+FlagPointx,newHeight+FlagPointy, RectPaint);
					}
					canvas.drawBitmap(bitmap, 0, 0, paint);
					Acanvas .drawBitmap(bitmap, newWidth, newHeight, paint);
					Acanvas.drawBitmap(Abitmap, 0, 0,paint);
					mylayout.invalidate();
					Toast.makeText(getApplicationContext(), "进行标注了", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "还未开始行走", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.show();
	
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem paintFlag = menu.add(Menu.NONE, 1, 1, "步数大小");
		MenuItem CloseBlue = menu.add(Menu.NONE, 2, 2, "退出程序");//添加菜单项
		return super.onCreateOptionsMenu(menu);
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			
			break;
		case 1:
			AlertDialog.Builder builder = new AlertDialog.Builder(SeocndActivity.this);
			builder.setTitle("像素点代表的步数");
			builder.setMessage("输入比例？"+"当前比例为："+mApp.scale_number);
			/**
			 * 设置EditText
			 */
			final EditText input = new EditText(SeocndActivity.this);
			builder.setView(input);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					int myScale = Integer.parseInt(input.getText().toString());
					mApp.scale_number = myScale;
					Toast.makeText(getApplicationContext(), "当前比例为："+mApp.scale_number, Toast.LENGTH_SHORT).show();
				}
			});
			builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getApplicationContext(), "当前比例为："+mApp.scale_number, Toast.LENGTH_SHORT).show();
				}
			});
			builder.show();
			break;
		case 2:
			if(updateTimer!=null)
			{
				updateTimer.cancel();
				updateTimer=null;
				Log.e("paintTimer离开", "ss");				
			}
			mApp.isGo = false; 
			mApp.x= 0;
			mApp.y =0;
			mApp.last_x = 0;
			mApp.last_y = 0;
			mApp.later_x = 0;
			mApp.later_y = 0;
			mApp.countNum = 0;					
			SeocndActivity.this.finish();
			break;
		default:
			break;
		}
		 return true;
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);	
//
//	}	
	
}

