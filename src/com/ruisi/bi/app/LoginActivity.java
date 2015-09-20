package com.ruisi.bi.app;

import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.LoginParser;

public class LoginActivity extends Activity implements OnClickListener,
		ServerCallbackInterface {
	private EditText et_username, et_pwd;
	private Button bt_commit;
	private String LoginUUID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atc_login_layout);
		initView();
		initData();
		setLisener();
	}

	private void initView() {
		et_username = (EditText) findViewById(R.id.LoginActivity_username);
		et_pwd = (EditText) findViewById(R.id.LoginActivity_pwd);
		bt_commit = (Button) findViewById(R.id.LoginActivity_commit);
	}

	private void initData() {

	}

	private void setLisener() {
		bt_commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.LoginActivity_commit:
			sendData();
			break;

		default:
			break;
		}
	}

	private void sendData() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this;
		rv.functionPath = APIContext.Login;
		// rv.modulePath = APIContext.Login;
	 rv.parser = new LoginParser();
		rv.Type = APIContext.GET;
		LoginUUID = UUID.randomUUID().toString();
		rv.uuId = LoginUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userName", et_username.getText().toString());
		map.put("password", et_pwd.getText().toString());
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
      if(LoginUUID.equals(uuid)){
    	  startActivity(new Intent(this,MenuActivity.class));
      }
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		Toast.makeText(this, errorMessage.getErrorDes(), 1000).show();
	}
}
