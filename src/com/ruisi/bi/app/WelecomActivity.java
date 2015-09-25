package com.ruisi.bi.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ruisi.bi.app.common.UserMsg;

public class WelecomActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welecome_layout);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(UserMsg.getName()==null){
					startActivity(new Intent(WelecomActivity.this, LoginActivity.class));
				}else {
					startActivity(new Intent(WelecomActivity.this, MenuActivity.class));
				}
				WelecomActivity.this.finish();
			}
		}, 3000);
	}
}
