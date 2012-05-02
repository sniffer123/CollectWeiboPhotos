package com.pigandtiger.utility;

import android.util.Log;

public class _Log {

	public static final String tag = "_Log";
	
	public static void i(String msg,Object... args){
		Log.i(tag, String.format(msg, args));
	}
	
	public static void i(String tag,String msg,Object... args){
		if(args.length == 0){
			Log.i(tag, msg);
		}else{
			Log.i(tag, String.format(msg, args));
		}
	}
	
	public static void e(String msg,Object... args){
		_Log.e(tag,msg,args);
	}
	
	public static void e(String tag,String msg,Object... args){
		_Log.e(tag, null,msg,args);
	}
	
	public static void e(String tag,Exception e,String msg,Object... args){
		Log.e(tag, String.format(msg, args));
		if( e != null ){
			e.printStackTrace();
		}
	}
}
