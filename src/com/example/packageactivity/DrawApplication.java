package com.example.packageactivity;


import android.app.Application;
import android.util.FloatMath;

public class DrawApplication extends Application {
	static DrawApplication application;
	

// 2013.08.28 Devin 添加显示参考航向
	float zeroPose = 0;
	float var_m50 = 0;	// 磁力计的方差
	float poly = 0;		// 拟合结果
	float range = 0;
	float mm = 0;	// 磁力计模值
	float x = 0;
	float y = 0;
	float later_x = 0;
	float later_y = 0;
	float last_x = 0;
	float last_y = 0;
	/**
	 * 用来判断长按屏幕的初始点
	 */
	float startPointX = 0;
	float startPointY = 0;
	/**
	 * 用来存储算法的结果 因为返回的步数不一定 用一个数组存储
	 */
	float []pointArray = new float[200];
	/**
	 * 用来统计已画的点数（现在不用了）
	 */
	int countNum = 0;
	/**
	 * 波峰值，既算法返回几个点
	 */
	int SignCountNum = 0;
	/**
	 * 是否是第一个点
	 */
	int isFirstPoint = 0;
	/**
	 * 判断是否开始画图
	 */
	Boolean isGoPaint = false;
	
	/**
	 * 用来判断是否开始把传感器数据写入文件
	 */
	Boolean isGo = false;
	/**
	 * 判断是否按下返回键
	 */
	Boolean isBack = false;//是否按返回键
	Boolean isChangeFlag=false;
	/**
	 * CheckBoxTwo标志
	 */
	Boolean CheckBoxTwoFlag = false;
	/**
	 * 是否开始计步数
	 */
	Boolean isStepBegin = false;
	/**
	 * 步数全局变量
	 */
	int stepCount_num = 0;
	/**
	 * 步数总统计变量
	 */
	int stepAllcount = 0;
	/**
	 * 比例变量
	 */
	int scale_number = 3;
	/**
	 * 总距离
	 */
	
	float distance = 0;
	/**
	 * 总偏差
	 */
	float Shift = 0;
	/**
	 * 比例变量
	 */
	int test_Num = 0;
	/**
	 * 总行走步数
	 */
	int StepsNum = 0;
	/**
	 * 训练距离
	 */
	float  PractiseDistance = 0;
	/**
	 * K值
	 */	
	float KValue =0;
	/**
	 * 俯仰角值
	 */	
	float Pitch=0;
	/**
	 * 航向角值
	 */	
	float Head=0;
	/**
	 * 横滚角值
	 */	
	float Roll=0;
	/**
	 * 磁力计膜值
	 */	
	float magnetic=0;
	
	float Hi = 0;
	
	Boolean pMean = false;
	float p_result = 0;
	
	float ore_x = 0;
	float ore_y = 0;
	float ore_z = 0;
	
	float zeroX = 0;
	float zeroY = 0;
	float zeroZ = 0;
	
	float re_matlab = 0;
	Boolean zeroFlag = true;
	Boolean testPaintFlag = false;
	String paintName;
	Boolean sqrt_zero = false;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application = this;
	}
	
}
