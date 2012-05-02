package com.pigandtiger.photocollector;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.SearchManager.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pigandtiger.photocollector.weibotools.PhotoCollector;
import com.pigandtiger.photocollector.weibotools.WeiboContext;
import com.pigandtiger.photocollector.weibotools.WeiboTools;

public class CollectWeiboPhotosActivity extends Activity implements OnClickListener{
	
	public static final String tag = "CollectWeiboPhotosActivity.java";
	
	private Button btnOk = null;
	
	private PhotoCollector collector = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnOk:{
	        try {
	        	WeiboContext context = new WeiboContext();
	        	WeiboTools.login(context, "hzk47st@126.com", "#dzw,tt");
				collector = new PhotoCollector(context);
				collector.request(1917331377);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		default:
			break;
		}
	}

}