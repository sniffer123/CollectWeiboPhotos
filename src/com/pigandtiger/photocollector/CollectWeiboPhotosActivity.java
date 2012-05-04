package com.pigandtiger.photocollector;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pigandtiger.frameworks.asyncjob.AsyncTaskManager;
import com.pigandtiger.frameworks.asyncjob.CallBackTaskResult;
import com.pigandtiger.frameworks.asyncjob.ICallbackListener;
import com.pigandtiger.frameworks.asyncjob.IJobExecutor;
import com.pigandtiger.photocollector.weibotools.PhotoCollector;
import com.pigandtiger.photocollector.weibotools.WeiboContext;
import com.pigandtiger.photocollector.weibotools.WeiboTools;
import com.pigandtiger.utility._Log;

import entites.AppData;
import entites.WeiboPhotoEntity;

public class CollectWeiboPhotosActivity extends Activity implements OnClickListener,ICallbackListener{
	
	public static final String tag = "CollectWeiboPhotosActivity.java";
	
	private Button btnOk = null;
	private Button btnOk2 = null;
	
	private PhotoCollector collector = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        btnOk2 = (Button)findViewById(R.id.btnOk2);
        btnOk2.setOnClickListener(this);

    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnOk:{
			AsyncTaskManager.doJob(this.getMainLooper(), this, new IJobExecutor() {
				public void execute(final CallBackTaskResult result,final Object... params) throws Exception {
		        	WeiboContext context = new WeiboContext();
		        	WeiboTools.login(context, "hzk47st@126.com", "#dzw,tt");
					collector = new PhotoCollector(context);
					collector.request(1917331377);
				}
			});
			
			break;
		}
		case R.id.btnOk2:{

			break;
		}
		default:
			break;
		}
	}

	public void onCallbackComplete(CallBackTaskResult result) {
		_Log.i(tag, "call back successfully");
		Intent intent = new Intent();
		intent.setClass(this, WeiboAlbumsActivity.class);
		startActivity(intent);
	}

	public void onCallbackFailed(CallBackTaskResult result) {
		_Log.i(tag, "call back failed");
	}
	
	
}