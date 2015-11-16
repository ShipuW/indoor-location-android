package com.example.packageactivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GForce extends Activity {

	SensorManager sensorManager;
	TextView textView;
	TextView resultTextView;
	/**
	 * 保存传感器的数据文件名
	 */
	EditText editText;
	/**
	 * 保存传感器的数据周期
	 */
	EditText timeButton;
	/**
	 * 保存算法处理后数据的文件名
	 */
	EditText resultText;
	/**
	 * 确定按钮
	 */
	Button editButton;
	/**
	 * 开始按钮
	 */
	Button beginButtton;
	/**
	 * 结束按钮
	 */
	Button endButton;
	/**
	 * 暂停按钮
	 */
	Button intButton;
	/**
	 * 画图按钮
	 */
	Button paintButton;
	/**
	 * 测试画图按钮
	 */
	Button test_paintButton;
	/**
	 * 技步数按钮
	 */
	Button stepCountButton;

	CheckBox checkBox;
	CheckBox checkBoxTwo;
	Boolean Flag = false;
	/**
	 * SD卡路径
	 */
	String SDPATH = Environment.getExternalStorageDirectory() + "/";
	/**
	 * 传感器采集数据输出
	 */
	OutputStream output = null;
	/**
	 * 算法结果数据输出
	 */
	OutputStream output_ten = null;
	/**
	 * 未使用
	 */
	OutputStream output_all = null;
	OutputStream output_acler = null;
	OutputStream output_gyro = null;
	OutputStream output_magnetic = null;
	int count = 0;
	byte[] data = new byte[3];
	/**
	 * 加速度传感器String
	 */
	String fileData;
	/**
	 * 加速度传感器(修改保存的格式)
	 */
	String fileDetailData;
	/**
	 * 加速度传感器x
	 */
	String fileData_x;
	/**
	 * 加速度传感器y
	 */
	String fileData_y;
	/**
	 * 加速度传感器z
	 */
	String fileData_z;
	/**
	 * 陀螺传感器
	 */
	String GyroscopeData;
	/**
	 * 陀螺传感器(修改保存的格式)
	 */
	String GyroscopeDetailData;
	/**
	 * 磁场传感器
	 */
	String magneticData;
	/**
	 * 磁场传感器(修改保存的格式)
	 */
	String magneticDetailData;
	/**
	 * Ori传感器
	 */
	String OrientionData;
	/**
	 * 磁场传感器(修改保存的格式)
	 */
	String OrientionDetailData;

	/**
	 * 压力传感器
	 */
	String PressureData;
	/**
	 * 用来保存算法处理结果的Sting类型
	 */
	String isdata;
	/**
	 * 用来保存姿态角数据
	 */
	String resultData;
	/**
	 * 用来保存姿态角数据
	 */
	String resultPDate;
	String isdata_z;
	/**
	 * 未使用
	 */
	Thread writeData;
	/**
	 * 采集的数据的文件名
	 */
	String dataFileName = "";
	/**
	 * 算法处理结果的文件名
	 */
	String resultFileName = "";
	/**
	 * 发送广播的Sting
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
	 * 接收身高值
	 */
	String setDvalue = "com.example.Distancevalue";
	/**
	 * 返回K值
	 */
	String getKvalue = "com.example.Kvalue";
	/**
	 * 初始0偏文件
	 */
	String zeroDate = "zeroDate.txt";
	/**
	 * 初始0偏文件夹
	 */
	String zeroDateDir = "zeroDateDir";
	/**
	 * 停止线程action
	 */
	String tPaint_ion = "com.example.tPaint_ion";
	/**
	 * 测试文件路径
	 */
	String testFilePath;
	/**
	 * 周期
	 */
	int dataTime = 0;
	/**
	 * 当确定按钮被按下时，才为true，此时按开始 updateTimer线程才开始，按画图按钮 其线程才开始
	 */
	Boolean isGo = false;
	/**
	 * CheckBox标志
	 */
	Boolean CheckBoxFlag = false;

	/**
	 * Handler
	 */
	Handler handler;
	Runnable myThread;
	Thread thread;
	/**
	 * 未使用
	 */
	Boolean isExistTime = false;
	/**
	 * 未使用
	 */
	Boolean isExistFileName = false;
	/**
	 * 停止线程action
	 */
	String threadStop = "com.example.stopthread";
	/**
	 * 开始采集数据的时候的毫秒数
	 */

	Boolean Base_Stop = true;
	long nowTime = 0;
	String ResultString = "";
	Boolean tenSeconds = false;

	int sensorVendor;
	// 传感器的分辨率，传感器所能识别的最小的精度是多少
	int GyroscropeVendor;
	int magenticendor;

	float x_value;
	float y_value;
	float z_value;

	float x_value_gyroscope;
	float y_value_gyroscope;
	float z_value_gyroscope;

	float x_value_magnetic;
	float y_value_magnetic;
	float z_value_magnetic;

	float x_value_ori;
	float y_value_ori;
	float z_value_ori;

	float x_value_pressure;
	/**
	 * 和锁屏有关
	 */
	WakeLock wl;
	SensorEventListener sensorEventListener;
	SensorEventListener GyroscopeEventListener;
	SensorEventListener magneticEventListener;
	SensorEventListener OrientionEventListener;
	SensorEventListener PressureEventListener;
	/**
	 * 保存数据线程
	 */
	Timer updateTimer;
	/**
	 * 画图线程
	 */
	Timer paintTimer;
	/**
	 * 测试画图线程
	 */
	Timer testPaintTimer;
	/**
	 * 计步器线程
	 */
	Timer stepTimer;
	/**
	 * 全局变量mApp
	 */
	DrawApplication mApp;
	/**
	 * 是否开始画图线程发送的action
	 */
	String isGoPaint = "com.example.go";
	public int x = 0;
	/**
	 * 用来向算法传递数据的初值
	 */
	float arrs_now[] = new float[15];
	float value_now[] = new float[1];
	float pr_now[] = new float[50];

	public float[] result = new float[3];// 返回的数据
	/**
	 * 保存处理完后的值(返回的是一个数组，所以要用数据接收)
	 */
	float[] isData;
	/**
	 * 保存步数(返回的是一个数组，所以要用数据接收)
	 */
	float[] StepData;
	/**
	 * 在画图的同时保存数据，文件名为 PaintFileName +时间
	 */
	String PaintFileName = "PaintResult";
	float newData[];

	String FileTime = "";

	static int tenFlag = 0;

	int paintScale = 20;

	int gyrScale = SensorManager.SENSOR_DELAY_FASTEST;

	static {
		System.loadLibrary("position");
	}

	/**
	 * 本地函数（计步器）
	 */
	// public native float[] MadgwickAHRSupdate(float[] arr);//用来传递数据和接收返还的数据

	/**
	 * 本地函数（清0）
	 */
	public native void ClearZero();

	/**
	 * 本地函数（打开写文件）
	 */
	// public native void OpenFile();

	/**
	 * 本地函数（关闭写文件）
	 */
	// public native void CloseFile();
	/**
	 * 本地函数（测试按钮）
	 */
	// public native void testFile();
	/**
	 * 本地函数(获得轨迹坐标)
	 */
	public native float[] KAgait(float[] arr);

	/**
	 * 本地函数(释放空间)
	 */
	public native float[] Exit();

	/**
	 * 本地函数(获得K值)
	 */
	public native float[] GetKValue(float[] value);

	public float[] acc_mag_to_gyr = new float[6];

	int Hp = 7290;
	double P0 = 1013.25;
	double Q;
	int p_n = 0;

	FileUtils zeroUtils;
	Thread testPaintThread;
	String math_sqrt_ion = "com.example.mathsqrt";
	float zero_x;
	float zero_y;
	float zero_z;
	float myheight = 170;
	Boolean do_paint_flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.finallayout);
		getWindow().setFlags(0x08000000, 0x08000000);

		getZeroDate();
		initViews();
		setListener();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		GForce.this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (wl != null) {
			wl.release();
			wl = null;
		}

		sensorManager.unregisterListener(sensorEventListener);
		sensorManager.unregisterListener(GyroscopeEventListener);
		sensorManager.unregisterListener(magneticEventListener);
		sensorManager.unregisterListener(OrientionEventListener);
		sensorManager.unregisterListener(PressureEventListener);
		unregisterReceiver(HasReceiver);
		if (updateTimer != null) {
			updateTimer.cancel();
			updateTimer = null;
		}
		if (paintTimer != null) {
			paintTimer.cancel();
			paintTimer = null;
		}
		if (testPaintTimer != null) {
			testPaintTimer.cancel();
			testPaintTimer = null;
		}
		if (testPaintThread != null) {
			testPaintThread.interrupt();
			testPaintThread = null;
			Log.e("close", "线程关闭");
		}
		if (stepTimer != null) {
			stepTimer.cancel();
			stepTimer = null;
		}
		Exit();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		wl.acquire();// 申请锁这个里面会调用PowerManagerService里面acquireWakeLock()
	}

	/* ====== 初始化界面 ======= */
	private void initViews() {
		// TODO Auto-generated method stub

		// ClearZero();
		textView = (TextView) this.findViewById(R.id.myfinalText);
		resultTextView = (TextView) this.findViewById(R.id.myresultText);

		editText = (EditText) findViewById(R.id.myfinalEditText);
		editButton = (Button) this.findViewById(R.id.myEditButton);

		beginButtton = (Button) this.findViewById(R.id.myfinalstartButton);// 确定按钮
		endButton = (Button) findViewById(R.id.myfinalEndButton);// 结束按钮
		intButton = (Button) findViewById(R.id.myinttartButton);// 暂停按钮
		paintButton = (Button) findViewById(R.id.myPaintEndButton);
		test_paintButton = (Button) findViewById(R.id.mytest_Button);
		stepCountButton = (Button) findViewById(R.id.myStepCountButton);
		timeButton = (EditText) findViewById(R.id.mytimeEditText);
		resultText = (EditText) findViewById(R.id.myResultEditText);
		checkBox = (CheckBox) findViewById(R.id.myCheckBox);
		checkBoxTwo = (CheckBox) findViewById(R.id.myCheck);
		mApp = (DrawApplication) GForce.this.getApplication();

		IntentFilter hasReceiverFilter = new IntentFilter(isGoPaint);
		this.registerReceiver(HasReceiver, hasReceiverFilter);

		IntentFilter has_tPaintFilter = new IntentFilter(tPaint_ion);
		this.registerReceiver(HasReceiver, has_tPaintFilter);

		IntentFilter clear_Filter = new IntentFilter(string_clearZero);
		this.registerReceiver(HasReceiver, clear_Filter);

		IntentFilter Thread_Filter = new IntentFilter(threadStop);
		this.registerReceiver(HasReceiver, Thread_Filter);
		IntentFilter DvalueFilter = new IntentFilter(setDvalue);
		this.registerReceiver(HasReceiver, DvalueFilter);
		beginButtton.setClickable(false);
		endButton.setClickable(false);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:
					Log.e("接受msg", "1");
					value_now[0] = mApp.PractiseDistance;
					newData = GetKValue(value_now);
					mApp.KValue = newData[0];
					Intent getintent = new Intent();
					getintent.setAction(getKvalue);
					sendBroadcast(getintent);
					break;
				case 2:
					Log.e("file", "接收");
					break;
				default:
					break;
				}

			}
		};
	}

	private void getZeroDate() {
		// TODO Auto-generated method stub
		zeroUtils = new FileUtils();

		if (zeroUtils.isFileExist(zeroDateDir)) {
			zeroUtils.changeSDPATH(zeroDateDir);
			try {
				if (zeroUtils.isFileExist(zeroDate)) {
					// zeroUtils.deleteSDFile(zeroDate);
					// zeroUtils.creatSDFile(zeroDate);
					// setZero();
					Log.e("zero", "零偏文件已存在");

					Toast.makeText(getApplicationContext(), "读取零偏成功",
							Toast.LENGTH_LONG).show();
				} else {
					zeroUtils.creatSDFile(zeroDate);
					setZero();
					Toast.makeText(getApplicationContext(), "创建零偏文件成功",
							Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			zeroUtils.creatSDDir(zeroDateDir);
			zeroUtils.changeSDPATH(zeroDateDir);
			try {
				if (zeroUtils.isFileExist(zeroDate)) {
					// zeroUtils.deleteSDFile(zeroDate);
					// zeroUtils.creatSDFile(zeroDate);
					// setZero();
					Log.e("zero", "已经存在文件");
					Toast.makeText(getApplicationContext(), "读取零偏数据文件成功",
							Toast.LENGTH_LONG).show();
				} else {
					zeroUtils.creatSDFile(zeroDate);
					setZero();
					Toast.makeText(getApplicationContext(), "创建零偏数据文件成功",
							Toast.LENGTH_LONG).show();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void setZero(String zeroString) {
		// TODO Auto-generated method stub
		Log.e("zero", "current setZero:" + zeroString);
		File file = new File(zeroUtils.getSDPATH() + zeroDate);
		// String helloWorld = "helloworld!";

		try {
			BufferedOutputStream output = new BufferedOutputStream(
					new FileOutputStream(file, false));
			try {
				output.write(zeroString.getBytes());
				output.flush();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setZero() {
		// TODO Auto-generated method stub
		Log.e("zero", "setZero");
		File file = new File(zeroUtils.getSDPATH() + zeroDate);
		String zeroString = "" + 0 + " " + 0 + " " + 0 + "";

		try {
			BufferedOutputStream output = new BufferedOutputStream(
					new FileOutputStream(file, false));
			try {
				output.write(zeroString.getBytes());
				output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ====== 监听器 ======= */
	private void setListener() {
		// TODO Auto-generated method stub
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		/* ====== 加速度传感器监听器 ======= */
		sensorEventListener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				arrs_now[0] = event.values[0];
				arrs_now[1] = event.values[1];
				arrs_now[2] = event.values[2];
				fileData = " " + x + " " + y + " " + z + " ";
				fileDetailData = "{" + x + "," + y + "," + z;
				fileData_x = " " + x + " ";
				fileData_y = " " + y + " ";
				fileData_z = " " + z + " ";
				x_value = event.values[0];
				y_value = event.values[1];
				z_value = event.values[2];
				sensorVendor = event.sensor.getMinDelay();
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		/* ====== 陀螺仪传感器监听器 ======= */
		GyroscopeEventListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				float a = (float) Math.acos(-1);
				tenFlag++;
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];

				arrs_now[3] = event.values[0];
				arrs_now[4] = event.values[1];
				arrs_now[5] = event.values[2];

				GyroscopeData = " " + x + " " + y + " " + z;
				GyroscopeDetailData = "," + x + "," + y + "," + z;
				// resultTextView.setText(" "+GyroscopeData);
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};

		/* ====== 磁力计仪传感器监听器 ======= */
		magneticEventListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub

				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				arrs_now[6] = event.values[0];
				arrs_now[7] = event.values[1];
				arrs_now[8] = event.values[2];
				magneticData = " " + x + " " + y + " " + z + " ";
				magneticDetailData = "," + x + "," + y + "," + z + "}" + ",";
				x_value_magnetic = event.values[0];
				y_value_magnetic = event.values[0];
				z_value_magnetic = event.values[0];
				magenticendor = event.sensor.getMinDelay();
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};

		/* ======Ori仪传感器监听器 ======= */
		OrientionEventListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub

				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];
				OrientionData = x + " " + y + " " + z + " ";
				OrientionDetailData = +x + "," + y + "," + z + "}" + ",";
				x_value_ori = event.values[0];
				y_value_ori = event.values[1];
				z_value_ori = event.values[2];
				magenticendor = event.sensor.getMinDelay();

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};

		/*
		 * PressureEventListener= new SensorEventListener() {
		 * 
		 * @Override public void onSensorChanged(SensorEvent event) { // TODO
		 * Auto-generated method stub
		 * 
		 * float x = event.values[0]; Q = x; PressureData = " "+x; }
		 * 
		 * @Override public void onAccuracyChanged(Sensor sensor, int accuracy)
		 * { // TODO Auto-generated method stub
		 * 
		 * } };
		 */

		/* ====== 开始按钮监听器 ======= */
		editText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				return false;
			}
		});

		timeButton.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				return false;
			}
		});
		resultText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				return false;
			}
		});
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					CheckBoxFlag = true;
					Toast.makeText(getApplicationContext(), "保存处理后的数据",
							Toast.LENGTH_SHORT).show();
				} else {
					CheckBoxFlag = false;
					Toast.makeText(getApplicationContext(), "不保存处理后的数据",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		checkBoxTwo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mApp.CheckBoxTwoFlag = true;
					Toast.makeText(getApplicationContext(), "手动平滑",
							Toast.LENGTH_SHORT).show();
				} else {
					mApp.CheckBoxTwoFlag = false;
					Toast.makeText(getApplicationContext(), "自动滑动",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/* ====== 确定按钮监听器 ======= */
		editButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Log.e("注册..........","sssssssss");
				tenFlag = 0;
				if ((editText.getText().toString().length() != 0)
						&& (timeButton.getText().toString().length() != 0)) {
					Date date = new Date(System.currentTimeMillis());
					dataFileName = editText.getText().toString() + "-"
							+ date.getHours() + "-" + date.getMinutes() + "-"
							+ date.getSeconds();
					resultFileName = resultText.getText().toString() + "-"
							+ date.getHours() + "-" + date.getMinutes() + "-"
							+ date.getSeconds();
					;
					dataTime = Integer
							.parseInt(timeButton.getText().toString());
					Date Filedate = new Date(System.currentTimeMillis());
					isGo = true;
					FileUtils fileUtils = new FileUtils();
					if (fileUtils.isFileExist(dataFileName + ".txt")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								GForce.this);
						builder.setTitle("文件已存在");
						builder.setMessage("点确定覆盖原文件");
						builder.setIcon(R.drawable.defense_sms);
						builder.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										FileUtils DeletefileUtils = new FileUtils();
										try {
											DeletefileUtils
													.deleteSDFile(dataFileName
															+ ".txt");
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										Toast.makeText(getApplicationContext(),
												"文件已删除", Toast.LENGTH_SHORT)
												.show();
									}
								});
						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});
						builder.show();
						//
					} else {
						count = 0;
						beginButtton.setClickable(true);
						endButton.setClickable(true);
						Toast.makeText(getApplicationContext(), "点击确定开始",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请输入完整",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		/* ====== 开始按钮监听器 ======= */
		beginButtton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isGo == true) {
					if ((editText.getText().toString().length() != 0)
							&& (timeButton.getText().toString().length() != 0)) {
						Flag = true;
						beginButtton.setClickable(false);
						beginButtton.setTextColor(Color.RED);
						endButton.setClickable(true);
						endButton.setTextColor(Color.BLACK);
						/* ======更新UI界面======= */
						tenSeconds = true;
						if (dataTime != 0) {
							nowTime = System.currentTimeMillis();
							updateTimer = new Timer("gForceUpdate");
							updateTimer.schedule(new TimerTask() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									updateGUI();
								}
							}, 2000, dataTime);
						}
					} else {
						Toast.makeText(getApplicationContext(), "请输入完整",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		/* ====== 暂停按钮监听器 ======= */

		intButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Flag = false;
				if ((editText.getText().toString().length() != 0)
						&& (timeButton.getText().toString().length() != 0)) {
					beginButtton.setClickable(true);
					beginButtton.setTextColor(Color.BLACK);
					endButton.setClickable(false);
					endButton.setTextColor(Color.RED);
					editButton.setClickable(true);
					editButton.setTextColor(Color.BLACK);
					Toast.makeText(getApplicationContext(), "已暂停，点击确定开始",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		/* ====== 结束按钮监听器 ======= */
		endButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tenFlag = 0;
				// TODO Auto-generated method stub
				if ((editText.getText().toString().length() != 0)
						&& (timeButton.getText().toString().length() != 0)) {
					Flag = false;
					isGo = false;
					tenSeconds = false;
					if (updateTimer != null) {
						updateTimer.cancel();
						updateTimer = null;
					}
					beginButtton.setClickable(false);
					beginButtton.setTextColor(Color.BLACK);
					editButton.setClickable(true);
					editButton.setTextColor(Color.BLACK);
					endButton.setClickable(false);
					editButton.setTextColor(Color.RED);
					// textView.setText("请重新输入保存数据！");
					Toast.makeText(getApplicationContext(), "已关闭，请重新输入",
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getApplicationContext(), "还未开始",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		/* ====== 画图按钮监听器 ======= */
		paintButton.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// OpenFile();
				do_paint_flag = false;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GForce.this);
				builder.setTitle("設置身高:单位(cm)");
				builder.setMessage("当前默認身高為为：" + myheight);
				/**
				 * 设置EditText
				 */
				final EditText input = new EditText(GForce.this);
				builder.setView(input);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								if (!input.getText().toString().equals("")) {
									float myScale = Float.parseFloat(input
											.getText().toString());
									myheight = myScale;
									do_paint_flag = true;
									do_paint();
								}
								if (do_paint_flag == false)
									do_paint();

								Toast.makeText(getApplicationContext(),
										"当前身高為为：" + myheight,
										Toast.LENGTH_SHORT).show();

							}
						});

				builder.show();

			}
		});

		test_paintButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mApp.testPaintFlag = true;
				Date date = new Date(System.currentTimeMillis());
				PaintFileName = "测试数据";
				PaintFileName = PaintFileName + "-" + (date.getMonth() + 1)
						+ "-" + date.getDate() + "-" + date.getHours() + "-"
						+ date.getMinutes() + "-" + date.getSeconds();
				Base_Stop = true;
				Intent intent = new Intent(getApplicationContext(),
						GForceFile.class);
				startActivityForResult(intent, 0);
			}
		});

		/* ====== 步数按钮监听器 ======= */
		stepCountButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "按", Toast.LENGTH_SHORT)
						.show();
				// TODO Auto-generated method stub
				if (updateTimer != null)
					updateTimer.cancel();
				if (paintTimer != null) {
					paintTimer.cancel();
					paintTimer = null;
				}
				if (stepTimer != null) {
					stepTimer.cancel();
					stepTimer = null;
				}
				Date date = new Date(System.currentTimeMillis());
				PaintFileName = " PaintResult";
				PaintFileName = PaintFileName + "-" + date.getHours() + "-"
						+ date.getMinutes() + "-" + date.getSeconds();
				Intent myPaintIntent = new Intent(GForce.this,
						StepActivity.class);
				startActivity(myPaintIntent);
			}
		});
		// 传入传感器类型
		/* ====== 加速度 ======= */
		Sensor acceleormeter = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(sensorEventListener, acceleormeter,
				SensorManager.SENSOR_DELAY_FASTEST);
		/* ====== 陀螺仪 ======= */
		Sensor gyroscrope = sensorManager
				.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		sensorManager.registerListener(GyroscopeEventListener, gyroscrope,
				SensorManager.SENSOR_DELAY_FASTEST);
		/* ====== 磁力计 ======= */
		Sensor MAGNETIC = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorManager.registerListener(magneticEventListener, MAGNETIC,
				SensorManager.SENSOR_DELAY_FASTEST);

		/* ====== Oriention ======= */
		Sensor Oriention = sensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(OrientionEventListener, Oriention,
				SensorManager.SENSOR_DELAY_FASTEST);
		/*
		 * Sensor Pressure =
		 * sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		 * sensorManager.registerListener(PressureEventListener, Pressure,
		 * SensorManager.SENSOR_DELAY_FASTEST);
		 */
	}

	public void do_paint() {
		x = 0;
		mApp.last_x = 0;
		mApp.last_y = 0;
		mApp.later_x = 0;
		mApp.later_y = 0;
		mApp.countNum = 0;
		if (updateTimer != null)
			updateTimer.cancel();
		if (paintTimer != null) {
			paintTimer.cancel();
			paintTimer = null;
		}
		Date date = new Date(System.currentTimeMillis());
		PaintFileName = "绘图数据";
		PaintFileName = PaintFileName + "-" + (date.getMonth() + 1) + "-"
				+ date.getDate() + "-" + date.getHours() + "-"
				+ date.getMinutes() + "-" + date.getSeconds();
		mApp.paintName = PaintFileName;
		/* ======更新UI界面======= */

		paintTimer = new Timer("gForceUpdate");
		paintTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				paintFunction();
			}
		}, 0, paintScale);
		Intent myPaintIntent = new Intent(GForce.this, SeocndActivity.class);
		startActivity(myPaintIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();

			String filePaths = bundle.getString("filePaths"); // 附加文件信息串,多个文件使用"\0"进行分隔
			// Toast.makeText(this, filePaths, Toast.LENGTH_SHORT).show();
			String[] filePathArray = filePaths.split("\0");
			Toast.makeText(getApplicationContext(), "路径为" + filePaths,
					Toast.LENGTH_SHORT).show();
			try {
				getFileDate(filePaths);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent myPaintIntent = new Intent(GForce.this, SeocndActivity.class);
			startActivity(myPaintIntent);

		}
	}

	/**
	 * 按行读取文件的数据
	 * 
	 * @throws IOException
	 */
	private void getFileDate(String filePaths) throws IOException {
		// TODO Auto-generated method stub

		// File inputFile = new File(filePaths);
		// if(inputFile != null)
		// Log.e("file", "读取成功");
		// FileInputStream fin = new FileInputStream(inputFile);
		// int length = fin.available();
		// byte [] buffer = new byte [length];
		// fin.read(buffer);
		// fin.close();
		// String res = "";
		// Log.e("zero", ""+length);
		// res = EncodingUtils.getString(buffer, "UTF-8");
		// String[] arrynew;
		// arrynew = res.split(" ");
		// Log.e("file", "读取的值"+arrynew.length);
		/* ======更新UI界面======= */

		testFilePath = filePaths;
		// testPaintTimer = new Timer("testPaint");
		// testPaintTimer.schedule(new TimerTask() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// testpaintFunction();
		// }
		//
		//
		// },0,paintScale);

		// Intent myPaintIntent = new Intent(GForce.this,SeocndActivity.class);
		// startActivity(myPaintIntent);

	}

	public class baseThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if (true) {
				StringBuffer sb = new StringBuffer("");
				FileReader reader;
				try {
					reader = new FileReader(testFilePath);
					if (reader != null)
						Log.e("file", "读取成功");
					int ii = 0;
					BufferedReader br = new BufferedReader(reader);
					String str = null;
					try {
						while (Base_Stop) {
							if ((str = br.readLine()) != null) {
								// sb.append(str+"/n");
								String[] arrnowS;
								arrnowS = str.split(" ");
								// Log.i("file1",
								// ii+"  "+arrnowS[0]+"  "+arrnowS[1]+"  "+arrnowS[2]+"  "+arrnowS[3]+"  "+arrnowS[5]
								// +"  "+arrnowS[6]+"  "+arrnowS[7]+"  "+arrnowS[8]+"  "+arrnowS[9]+"  "+arrnowS[10]);
								// Log.e("file",
								// i+"  "+arrnowS[0]+"  "+arrnowS[1]+"  "+arrnowS[2]);
								float arrnow[] = new float[15];
								float arrnow_q[] = new float[15];
								int j = 1;
//								Log.e("file1", "开始了1"+arrnowS.length);
								if(arrnowS.length == 17)//新数据
								{
									for (j = 1; j < 15; j++) {
										if (j != 4)
											arrnow[j] = Float
													.parseFloat(arrnowS[j]);
									}
									
//										Log.e("file1", "开始了2");
										for (j = 0; j < 12; j++) {
											if (j < 3)
												arrnow_q[j] = arrnow[j + 1];
											else if (j == 3)
												arrnow_q[j] = arrnow[j + 2];
											else
												arrnow_q[j] = arrnow[j + 2];
									}
									
								}
								else//原数据
								{
									for (j = 1; j < 11; j++) {
										if (j != 4)
											arrnow[j] = Float
													.parseFloat(arrnowS[j]);
									}
									
//									Log.e("file1", "开始了2");
									for (j = 0; j < 9; j++) {
										if (j < 3)
											arrnow_q[j] = arrnow[j + 1];
										else if (j == 3)
											arrnow_q[j] = arrnow[j + 2];
										else
											arrnow_q[j] = arrnow[j + 2];
									}
									arrnow_q[9] = 0;
									arrnow_q[10] = 0;
									arrnow_q[11] = 0;
								}
								int countNum = 0;
								arrnow_q[12] = 170;
								mApp.isGoPaint = true;
								
								mApp.magnetic = (float) (Math.sqrt(Math.pow(
										arrs_now[6], 2)
										+ Math.pow(arrs_now[7], 2)
										+ Math.pow(arrs_now[8], 2)));

							
								isData = KAgait(arrnow_q);
								String accDate = " ";
								countNum = (int) isData[99];
//								Log.e("file1", "算完了2");
								// setZero();
								// Log.e("zero", "VVV");
								/*
								 * 更新零偏数据
								 */
								if (isData[87] != 0 || isData[88] != 0
										|| isData[89] != 0) {
									String zeroSting = "" + isData[87] + " "
											+ isData[88] + " " + isData[89]
											+ "\n";
									mApp.zeroX = isData[87];
									mApp.zeroY = isData[88];
									mApp.zeroZ = isData[89];

									setZero(zeroSting);

								}

								mApp.Hi = (float) (Hp * (Math.log(P0 / Q) / (Math
										.log(2)))) - mApp.p_result;
								// Log.e("p",
								// ""+(Hp*(Math.log(P0/Q)/(Math.log(2)))));
								// Log.e("获得步数", ""+mApp.SignCountNum);
								mApp.Head = isData[95];
								mApp.Pitch = isData[94];
								mApp.Roll = isData[93];
								mApp.ore_x = x_value_ori;
								mApp.ore_y = y_value_ori;
								mApp.ore_z = z_value_ori;
								resultData = " " + isData[95] + " "
										+ isData[94] + " " + isData[93];
								mApp.StepsNum = (int) isData[96];
								mApp.distance = isData[97];
								mApp.Shift = isData[98];

								mApp.poly = isData[90];
								mApp.zeroPose = isData[92];
								mApp.var_m50 = isData[91];
								mApp.range = isData[86];
								
								mApp.mm = isData[80];
								
								// mApp.re_matlab = isData[85];
								resultData = resultData + " " + isData[85];
								
								// Log.e("file"," "+isData[85]);
								if (countNum != 0) {
									mApp.SignCountNum = countNum;
								}
								for (int i = 0; i < mApp.SignCountNum; i++) {
									/**
									 * 考虑用数组传参数
									 */
									mApp.pointArray[2 * i] = (float) isData[2 * i]
											* mApp.scale_number;
									mApp.pointArray[2 * i + 1] = (float) isData[2 * i + 1]
											* mApp.scale_number;
									// Log.e("数组", " "+mApp.pointArray[2*i]);
								}

								// Log.e("步数", ""+mApp.SignCountNum);

								if (mApp.SignCountNum != 0) {
									Intent intent = new Intent();
									intent.setAction(actionAddress);
									sendBroadcast(intent);
									mApp.countNum++;
									// Log.e("num", "画图");
									Log.e("file1", "画图2");
								} else {
									Intent myintent = new Intent();
									myintent.setAction(actionOther);
									sendBroadcast(myintent);
								}
								// }

								if (CheckBoxFlag == true) {
									try {
										FileUtils fileUtils = new FileUtils();

										if (!fileUtils
												.isFileExist(PaintFileName
														+ ".txt")) {
											try {
												fileUtils
														.creatSDFile(PaintFileName
																+ ".txt");
												fileUtils
														.creatSDFile(PaintFileName
																+ "TheResultforMatlab.txt");
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										} else {

											File filePaint = new File(SDPATH
													+ PaintFileName + ".txt");
											File ResultMatlabPaint = new File(
													SDPATH
															+ PaintFileName
															+ "TheResultforMatlab.txt");
											BufferedOutputStream output = new BufferedOutputStream(
													new FileOutputStream(
															filePaint, true));
											BufferedOutputStream ResultMatlabput = new BufferedOutputStream(
													new FileOutputStream(
															ResultMatlabPaint,
															true));
											try {
												Date Filedate = new Date(
														System.currentTimeMillis());
												String time = ""
														+ Filedate.getHours()
														+ ":"
														+ Filedate.getMinutes()
														+ ":"
														+ Filedate.getSeconds()
														+ ":"
														+ Filedate.getTime()
														% 1000;
												output.write(time.getBytes());
												// output.write(fileData.getBytes());
												// output.write(GyroscopeData.getBytes());
												// output.write(magneticData.getBytes());
												output.write(str.getBytes());
												output.write(" \n".getBytes());
												ResultMatlabput.write(time
														.getBytes());
												ResultMatlabput
														.write(resultData
																.getBytes());
												ResultMatlabput.write(" \n"
														.getBytes());
												// Log.e("date", " 222");
												output.flush();
												ResultMatlabput.flush();

											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
								try {
									sleep(5); // 仿真时间控制
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// Log.e("file",
								// i+"  "+arrnow[1]+"  "+arrnow[2]+"  "+arrnow[3]);

							} else
								break;
						}
						reader.close(); // 关闭文件
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Thread.currentThread().interrupt();

					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 读取零偏的数据
	 * 
	 * @throws IOException
	 */
	private void getinitDate() throws IOException {
		// TODO Auto-generated method stub
		File inputFile = new File(zeroUtils.getSDPATH() + zeroDate);
		FileInputStream fin = new FileInputStream(inputFile);
		int length = fin.available();
		byte[] buffer = new byte[length];
		fin.read(buffer);
		fin.close();
		String res = "";
		// Log.e("zero", ""+length);
		res = EncodingUtils.getString(buffer, "UTF-8");
		String[] arrynew;
		arrynew = res.split(" ");
		// Log.e("zero", "读取的值"+arrynew[0]+" "+arrynew[1]+" "+arrynew[2]);
		mApp.zeroX = Float.parseFloat(arrynew[0]);

		mApp.zeroY = Float.parseFloat(arrynew[1]);

		mApp.zeroZ = Float.parseFloat(arrynew[2]);

		mApp.zeroFlag = false;
	}

	/* ====== 文件写操作 ======= */
	private void updateGUI() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				textView.setText("文件保存在：" + dataFileName + " .txt  " + "已保存"
						+ count + " 组数据" + " 采样周期" + dataTime + "ms");
				textView.setTextColor(Color.BLACK);
				textView.setTextSize(20);
				textView.invalidate();
				if (Flag == true) {
					try {
						FileUtils fileUtils = new FileUtils();
						if ((!fileUtils.isFileExist(dataFileName + ".txt"))) {
							try {
								fileUtils.creatSDFile(dataFileName + ".txt");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							File fileBlue = new File(SDPATH + dataFileName
									+ ".txt");
							BufferedOutputStream output = new BufferedOutputStream(
									new FileOutputStream(fileBlue, true));
							try {
								Date Filedate = new Date(
										System.currentTimeMillis());
								String time = "" + Filedate.getHours() + ":"
										+ Filedate.getMinutes() + ":"
										+ Filedate.getSeconds() + ":"
										+ Filedate.getTime() % 1000;
								output.write(time.getBytes());
								output.write(fileData.getBytes());
								output.write(GyroscopeData.getBytes());
								output.write(magneticData.getBytes());
								output.write(OrientionData.getBytes());
								// output.write(PressureData.getBytes());
								output.write(" \n".getBytes());
								count++;
								output.flush();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	}

	/* 绘图 */
	private void paintFunction() {

		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			/**
		 * 
		 */
			@Override
			public void run() {

				if (true) {
					try {

						if (mApp.isGo == true) {
							int countNum = 0;
							mApp.isGoPaint = true;
							if (mApp.zeroFlag == true) {
								// Toast.makeText(getApplicationContext(),
								// "请将手机静置放置10秒左右", Toast.LENGTH_SHORT).show();
								getinitDate();

							}
							arrs_now[9] = zero_x;
							arrs_now[10] = zero_y;
							arrs_now[11] = zero_z;
							arrs_now[12] = myheight;
							mApp.magnetic = (float) (Math.sqrt(Math.pow(
									arrs_now[6], 2)
									+ Math.pow(arrs_now[7], 2)
									+ Math.pow(arrs_now[8], 2)));

							// ;
							isData = KAgait(arrs_now);
							String accDate = " ";
							countNum = (int) isData[99];
							// setZero();
							// Log.e("zero", "VVV");
							/*
							 * 更新零偏数据
							 */
							if (isData[87] != 0 || isData[88] != 0
									|| isData[89] != 0) {
								String zeroSting = "" + isData[87] + " "
										+ isData[88] + " " + isData[89] + "\n";
								mApp.zeroX = isData[87];
								mApp.zeroY = isData[88];
								mApp.zeroZ = isData[89];
								Log.e("zero", "" + isData[87] + " "
										+ isData[88] + " " + isData[89]);
								zero_x = isData[87];
								zero_y = isData[88];
								zero_z = isData[89];
								setZero(zeroSting);
								Toast.makeText(getApplicationContext(), "更新零偏",
										Toast.LENGTH_SHORT).show();

							}

							float math_sqrt = (float) (isData[87] * isData[87]
									+ isData[88] * isData[88] + isData[89]
									* isData[89]);

							if (math_sqrt >= 0.00007) { // 判断陀螺是否看可用
								Intent sqrt = new Intent();
								sqrt.setAction(math_sqrt_ion);
								sendBroadcast(sqrt);
								Log.e("zero", "" + math_sqrt);
							}
							mApp.Hi = (float) (Hp * (Math.log(P0 / Q) / (Math
									.log(2)))) - mApp.p_result;
							// Log.e("p",
							// ""+(Hp*(Math.log(P0/Q)/(Math.log(2)))));
							// Log.e("获得步数", ""+mApp.SignCountNum);
							mApp.Head = isData[95];
							mApp.Pitch = isData[94];
							mApp.Roll = isData[93];
							mApp.ore_x = x_value_ori;
							mApp.ore_y = y_value_ori;
							mApp.ore_z = z_value_ori;
							resultData = " " + isData[95] + " " + isData[94]
									+ " " + isData[93] + " " + zero_x + " "
									+ zero_y + " " + zero_z;
							resultPDate = zero_x + " " + zero_y + " " + zero_z
									+ " ";
							mApp.StepsNum = (int) isData[96];
							mApp.distance = isData[97];
							mApp.Shift = isData[98];

							mApp.poly = isData[90];
							mApp.zeroPose = isData[92];
							mApp.var_m50 = isData[91];
							mApp.range = isData[86];
							resultData = resultData + " " + isData[85];

							mApp.mm = isData[80];
							
							// Log.e("file"," "+isData[95]);
							if (countNum != 0) {
								mApp.SignCountNum = countNum;
							}
							for (int i = 0; i < mApp.SignCountNum; i++) {
								/**
								 * 考虑用数组传参数
								 */
								mApp.pointArray[2 * i] = (float) isData[2 * i]
										* mApp.scale_number;
								mApp.pointArray[2 * i + 1] = (float) isData[2 * i + 1]
										* mApp.scale_number;
								// Log.e("数组", " "+mApp.pointArray[2*i]);
							}

							// Log.e("步数", ""+mApp.SignCountNum);

							if (mApp.SignCountNum != 0) {
								Intent intent = new Intent();
								intent.setAction(actionAddress);
								sendBroadcast(intent);
								mApp.countNum++;
								// Log.e("num", "画图");
							} else {
								Intent myintent = new Intent();
								myintent.setAction(actionOther);
								sendBroadcast(myintent);
							}

							if (CheckBoxFlag == true) {
								try {
									FileUtils fileUtils = new FileUtils();

									if (!fileUtils.isFileExist(PaintFileName
											+ ".txt")) {
										try {
											fileUtils.creatSDFile(PaintFileName
													+ ".txt");
											fileUtils.creatSDFile(PaintFileName
													+ "TheResultforMatlab.txt");
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} else {

										File filePaint = new File(SDPATH
												+ PaintFileName + ".txt");
										File ResultMatlabPaint = new File(
												SDPATH
														+ PaintFileName
														+ "TheResultforMatlab.txt");
										BufferedOutputStream output = new BufferedOutputStream(
												new FileOutputStream(filePaint,
														true));
										BufferedOutputStream ResultMatlabput = new BufferedOutputStream(
												new FileOutputStream(
														ResultMatlabPaint, true));
										try {
											Date Filedate = new Date(
													System.currentTimeMillis());
											String time = ""
													+ Filedate.getHours() + ":"
													+ Filedate.getMinutes()
													+ ":"
													+ Filedate.getSeconds()
													+ ":" + Filedate.getTime()
													% 1000;
											output.write(time.getBytes());
											output.write(fileData.getBytes());
											output.write(GyroscopeData
													.getBytes());
											output.write(magneticData
													.getBytes());
											output.write(resultPDate.getBytes());
											output.write(OrientionData
													.getBytes());

											output.write(" \n".getBytes());
											ResultMatlabput.write(time
													.getBytes());
											ResultMatlabput.write(resultData
													.getBytes());
											ResultMatlabput.write(" \n"
													.getBytes());

											output.flush();
											ResultMatlabput.flush();

										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
						/**
						 * 求高度
						 */
						// if(mApp.pMean == true)
						// {
						// while(p_n<50)
						// {
						// pr_now[p_n] = (float)
						// (Hp*(Math.log(P0/Q)/(Math.log(2))));
						// p_n++;
						// }
						// if(p_n == 50)
						// {
						// int i = 0;
						// for(i = 0;i<p_n;i++)
						// {
						// mApp.p_result += pr_now[i];
						// }
						// mApp.p_result = mApp.p_result/50;
						// }
						// Log.e("pr", ""+mApp.p_result);
						// if(mApp.isGoPaint == true)
						// {
						// int i = 0;
						// for(i = 0;i<50;i++)
						// {
						// pr_now[i] = 0;;
						// }
						// mApp.pMean = false;
						// }
						// }

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}

			/**
			 * 读取零偏的数据
			 * 
			 * @throws IOException
			 */
			private void getinitDate() throws IOException {
				// TODO Auto-generated method stub
				File inputFile = new File(zeroUtils.getSDPATH() + zeroDate);
				FileInputStream fin = new FileInputStream(inputFile);
				int length = fin.available();
				byte[] buffer = new byte[length];
				fin.read(buffer);
				fin.close();
				String res = "";
				// Log.e("zero", ""+length);
				res = EncodingUtils.getString(buffer, "UTF-8");
				String[] arrynew;
				arrynew = res.split(" ");
				// Log.e("zero",
				// "读取的值"+arrynew[0]+" "+arrynew[1]+" "+arrynew[2]);
				mApp.zeroX = Float.parseFloat(arrynew[0]);

				mApp.zeroY = Float.parseFloat(arrynew[1]);

				mApp.zeroZ = Float.parseFloat(arrynew[2]);
				
				zero_x = Float.parseFloat(arrynew[0]);

				zero_y = Float.parseFloat(arrynew[1]);

				zero_z = Float.parseFloat(arrynew[2]);

				mApp.zeroFlag = false;
			}

		});

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		// ClearZero();
		if (paintTimer != null) {
			paintTimer.cancel();
			paintTimer = null;
			Log.e("重新开始", "countNum" + mApp.countNum);
		}
		if (testPaintTimer != null) {
			testPaintTimer.cancel();
			testPaintTimer = null;
			Log.e("重新开始", "countNum" + mApp.countNum);
		}
		if (testPaintThread != null) {
			testPaintThread.interrupt();
			testPaintThread = null;
			Log.e("close", "线程关闭");
		}
		mApp.countNum = 0;

		super.onRestart();
	}

	private final BroadcastReceiver HasReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String action = intent.getAction();
			Log.e("接受广播", "接受广播");
			if (action.equals(isGoPaint)) {
				Intent secintent = new Intent();
				secintent.setAction(actionAddress);
				sendBroadcast(secintent);
				// Log.e("发2", "发2");
			}

			if (action.equals("com.example.finish")) {
				if (updateTimer != null) {
					updateTimer.cancel();
					updateTimer = null;
				}

				// unregisterReceiver(HasReceiver);
				// Log.e("接触发送", "接触发送");
			}

			if (action.equals(threadStop)) {

				if (updateTimer != null) {
					updateTimer.cancel();
					updateTimer = null;

				}

				if (paintTimer != null) {
					paintTimer.cancel();
					paintTimer = null;

				}
				if (testPaintTimer != null) {
					testPaintTimer.cancel();
					testPaintTimer = null;
				}

				if (testPaintThread != null) {
					testPaintThread.interrupt();
					Base_Stop = false;
					Log.e("threadStop", "线程关闭");
				}

				if (stepTimer != null) {
					stepTimer.cancel();
					stepTimer = null;
				}
				// unregisterReceiver(HasReceiver);
				ClearZero();
				Log.e("接触发送", "接触发送");
			}
			if (action.equals(tPaint_ion)) {

				if (testPaintThread != null) {
					testPaintThread.interrupt();
					testPaintThread = null;
				}
				Log.e("gggg", "收到gogogo广播");
				mApp.isGoPaint = true;
				testPaintThread = new baseThread();
				testPaintThread.start();

			}

			if (action.equals(string_clearZero)) {
				ClearZero();

			}
			if (action.equals(setDvalue)) {
				Log.e("接受距离消息", "ok");
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);

			}

		}
	};

	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem CloseBlue = menu.add(Menu.NONE, 4, 4, "退出程序");// 添加菜单项

		MenuItem c_Scale = menu.add(Menu.NONE, 2, 2, "步数大小");// 添加菜单项

		MenuItem paint_Scale = menu.add(Menu.NONE, 1, 1, "画图周期");// 添加菜单项

		MenuItem my_Gyr_Scale = menu.add(Menu.NONE, 3, 3, "采集周期");// 添加菜单项

		MenuItem AboutVersion = menu.add(Menu.NONE, 5, 5, "版本介绍");// 添加菜单项

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getOrder()) {
		case 0:

			break;
		case 1:
			AlertDialog.Builder paintbuilder = new AlertDialog.Builder(
					GForce.this);
			paintbuilder.setTitle("画图周期");
			paintbuilder.setMessage("输入周期？");
			/**
			 * 设置EditText
			 */
			final EditText paintinput = new EditText(GForce.this);
			paintbuilder.setView(paintinput);
			paintbuilder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							int myScale = Integer.parseInt(paintinput.getText()
									.toString());
							paintScale = myScale;
							Toast.makeText(getApplicationContext(),
									"当前比例为：" + paintScale, Toast.LENGTH_SHORT)
									.show();
							dialog.dismiss();
						}
					});
			paintbuilder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getApplicationContext(),
									"当前比例为：" + paintScale, Toast.LENGTH_SHORT)
									.show();
							dialog.dismiss();
						}
					});
			paintbuilder.show();

			break;
		case 2:
			AlertDialog.Builder builder = new AlertDialog.Builder(GForce.this);
			builder.setTitle("像素点代表的步数");
			builder.setMessage("输入比例？" + "当前比例为：" + mApp.scale_number);
			/**
			 * 设置EditText
			 */
			final EditText input = new EditText(GForce.this);
			builder.setView(input);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							int myScale = Integer.parseInt(input.getText()
									.toString());
							mApp.scale_number = myScale;
							Toast.makeText(getApplicationContext(),
									"当前比例为：" + mApp.scale_number,
									Toast.LENGTH_SHORT).show();
						}
					});
			builder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getApplicationContext(),
									"当前比例为：" + mApp.scale_number,
									Toast.LENGTH_SHORT).show();
						}
					});
			builder.show();
			break;
		case 4:
			if (paintTimer != null) {
				paintTimer.cancel();
				paintTimer = null;
				Log.e("paintTimer离开", "ss");

			}
			if (testPaintThread != null) {
				testPaintThread.interrupt();
				testPaintThread = null;
				Log.e("close", "线程关闭");
			}
			if (testPaintTimer != null) {
				testPaintTimer.cancel();
				testPaintTimer = null;
				Log.e("testPaintTimer离开", "ss");

			}
			mApp.isGo = false;
			mApp.x = 0;
			mApp.y = 0;
			mApp.last_x = 0;
			mApp.last_y = 0;
			mApp.later_x = 0;
			mApp.later_y = 0;
			mApp.countNum = 0;

			GForce.this.finish();
			break;

		case 3:
			AlertDialog.Builder gyr_builder = new AlertDialog.Builder(
					GForce.this);
			gyr_builder.setTitle("比例");
			gyr_builder.setMessage("输入比例？");
			/**
			 * 设置EditText
			 */
			final EditText gyr_input = new EditText(GForce.this);
			gyr_builder.setView(gyr_input);
			gyr_builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							int myScale = Integer.parseInt(gyr_input.getText()
									.toString());
							gyrScale = myScale;
							Toast.makeText(getApplicationContext(),
									"当前比例为：" + mApp.scale_number,
									Toast.LENGTH_SHORT).show();
						}
					});
			gyr_builder.setNeutralButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Toast.makeText(getApplicationContext(),
									"当前比例为：" + gyrScale, Toast.LENGTH_SHORT)
									.show();
						}
					});
			gyr_builder.show();
			break;

		case 5:
			AlertDialog.Builder Version = new AlertDialog.Builder(GForce.this);
			Version.setTitle("V2.4");
			Version.setMessage("更新日期：2013.11.20 ");
			Version.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
