package com.pigandtiger.frameworks.asyncjob;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.pigandtiger.utility._Log;


public class CallBackTask extends AsyncTask<Object, Integer, CallBackTaskResult>{
	
	private static final String tag = "CallBackTask.java";
	
	private long index = 0;
	private IJobExecutor executor = null;
	private Handler handler = null;
	private ICallbackListener callbackListener = null;
	
	public CallBackTask(final long index,final Handler handler,final IJobExecutor executor) {
		this.index = index;
		this.executor = executor;
		this.handler = handler;
	}
	
	public CallBackTask(final long index,final ICallbackListener callbackListener,final IJobExecutor executor) {
		this.index = index;
		this.executor = executor;
		this.callbackListener = callbackListener;
	}
	
	@Override
	protected CallBackTaskResult doInBackground(Object... params) {
		final CallBackTaskResult result = new CallBackTaskResult();
		try{
			executor.execute(result,params);
			result.isSuccess = true;
		}catch(Exception e){
			_Log.e(tag,e, "Can not execute job.");
			result.isSuccess = false;
		}finally{
			result.index = index;
			
			if( handler != null ){
				Message msg = new Message();
				msg.what = result.isSuccess ? CallBackTaskResult.ON_JOB_SUCCESSFULLY : CallBackTaskResult.ON_JOB_FAILED;
				msg.obj = result;
				handler.sendMessage(msg);
			}else{
				// return result direct
				if( result.isSuccess ){
					callbackListener.onCallbackComplete(result);
				}else{
					callbackListener.onCallbackFailed(result);
				}
			}
			
			executor = null;
			handler = null;
		}

		
		return result;
	}

}
