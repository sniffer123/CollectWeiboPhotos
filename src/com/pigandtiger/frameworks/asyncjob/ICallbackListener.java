package com.pigandtiger.frameworks.asyncjob;

public interface ICallbackListener {

	public void onCallbackComplete(CallBackTaskResult result);
	public void onCallbackFailed(CallBackTaskResult result);
	
}
