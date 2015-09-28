package com.ruisi.bi.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ruisi.bi.app.adapter.TableAdapter;
import com.ruisi.bi.app.adapter.TableAdapter.TableRow;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.FormParser;

public class FormActivity extends Activity implements ServerCallbackInterface {
	private ListView ListView01;
	private String formUUID;
	private ArrayList<TableRow> TableRows;
	private TableAdapter adapter;
	private static String strJsons;

	public static void startThis(Context context, String strJson) {
		strJsons = strJson;
		context.startActivity(new Intent(context, FormActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_fragment_layout);
		ListView01 = (ListView) findViewById(R.id.ListView01);
		TableRows = new ArrayList<>();
		adapter = new TableAdapter(this, TableRows);
		ListView01.setAdapter(adapter);
		sendRequest();
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this;
		rv.functionPath = APIContext.form;
		rv.parser = new FormParser();
		rv.Type = APIContext.POST;
		formUUID = UUID.randomUUID().toString();
		rv.uuId = formUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageInfo", strJsons);
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if (formUUID.equals(uuid)) {
			TableRows.clear();
			TableRows.addAll((Collection<? extends TableRow>) object);
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "成功", 2000).show();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (formUUID.equals(uuid)) {
			Toast.makeText(this, errorMessage.getErrorDes(), 1000).show();
		}
	}
}
