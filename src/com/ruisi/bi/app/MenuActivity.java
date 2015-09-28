package com.ruisi.bi.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ruisi.bi.app.adapter.MenuAdapter;
import com.ruisi.bi.app.bean.MenuBean;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.MenuParser;
import com.ruisi.bi.app.view.MenuPopwindow;

public class MenuActivity extends Activity implements ServerCallbackInterface,
		OnItemClickListener, OnClickListener {
	private ListView lv;
	private MenuAdapter mainAdapter;
	private ArrayList<MenuBean> datas;
	private String MenuUUID;
	private ImageView setting_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.MainActivity_listview);
		lv.setOnItemClickListener(this);
		setting_iv=(ImageView) findViewById(R.id.setting);
		setting_iv.setOnClickListener(this);
		datas = new ArrayList<>();
		mainAdapter = new MenuAdapter(this, datas);
		lv.setAdapter(mainAdapter);
		sendRequest();
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this;
		rv.functionPath = APIContext.Menu;
		// rv.modulePath = APIContext.Login;
		rv.parser = new MenuParser();
		rv.Type = APIContext.GET;
		MenuUUID = UUID.randomUUID().toString();
		rv.uuId = MenuUUID;
		rv.isSaveToLocation = false;
		// HashMap<String, String> map = new HashMap<String, String>();
		// map.put("userName", "admin");
		// map.put("password", "123456");
		// rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if (MenuUUID.equals(uuid)) {
			datas.clear();
			datas.addAll((Collection<? extends MenuBean>) object);
			mainAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (MenuUUID.equals(uuid))
			Toast.makeText(this, errorMessage.getErrorDes(), 1000).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 0) {
			startActivity(new Intent(this, AnalysisActivity.class));
		}
	}

	@Override
	public void onClick(View v) {
		if (v==setting_iv) {
			MenuPopwindow.getMenuPopwindow(this, lv);
		}
	}

}
