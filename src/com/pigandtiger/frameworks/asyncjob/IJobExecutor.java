package com.pigandtiger.frameworks.asyncjob;

public interface IJobExecutor {

	public void execute(final CallBackTaskResult result,final Object... params) throws Exception;
	
}
