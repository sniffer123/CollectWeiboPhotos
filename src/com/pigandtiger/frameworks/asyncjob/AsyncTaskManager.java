package com.pigandtiger.frameworks.asyncjob;

import java.util.LinkedHashMap;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.pigandtiger.utility._Log;

public class AsyncTaskManager {
	
	private static final String tag = "AsyncTaskManager.java";
	
	private static long currentKeyIndex = 0;

	private static LinkedHashMap<Long,CallBackTask> callbackSet = new LinkedHashMap<Long,CallBackTask>();
	
	public static void init(){
		callbackSet.clear();
		currentKeyIndex = System.currentTimeMillis() & (~( ~0 << 56));
	}
	
	public static synchronized long requestNewKeyIndex() {
		return currentKeyIndex++;
	}
	
	public static void doJob(final Looper srcLooper,final ICallbackListener callback,final IJobExecutor executor,Object...params ){
		
		final Handler handler = new Handler(srcLooper){
			@Override
			public void handleMessage(Message msg) {
				final CallBackTaskResult result = (CallBackTaskResult)msg.obj;
				switch(msg.what){
				case CallBackTaskResult.ON_JOB_SUCCESSFULLY:{
					callback.onCallbackComplete(result);
					break;
				}
				case CallBackTaskResult.ON_JOB_FAILED:{
					callback.onCallbackFailed(result);
					break;
				}
				}
				callbackSet.remove(result.index);
				_Log.i(tag, "remove task with index: " + result.index);
			}
		};
		
		final long index = requestNewKeyIndex(); 
		CallBackTask task = new CallBackTask(index,handler, executor);
		callbackSet.put(index,task);
		task.execute(params);
		
	}
	
	public static void doJob(final ICallbackListener callback,final IJobExecutor executor,Object...params ){
		
		final long index = requestNewKeyIndex(); 
		CallBackTask task = new CallBackTask(index,callback, new IJobExecutor() {
			
			public void execute(CallBackTaskResult result, Object... params)
					throws Exception {
				try{
					executor.execute(result, params);
				}finally{
					callbackSet.remove(result.index);
				}
			}
		});
		callbackSet.put(index,task);
		task.execute(params);
		
	}
	
	public static void print(){
		_Log.i(tag, "Remain jobs: " + callbackSet.size());
		
		
	}
	
}
