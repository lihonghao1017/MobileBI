package com.ruisi.bi.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ruisi.bi.app.adapter.OptionAdapter;
import com.ruisi.bi.app.adapter.TableAdapter;
import com.ruisi.bi.app.adapter.TableAdapter.TableRow;
import com.ruisi.bi.app.bean.FormBean;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.bean.WeiduOptionBean;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.FormParser;
import com.ruisi.bi.app.view.LoadingDialog;

public class FormActivity extends Activity implements ServerCallbackInterface {
	private ListView ListView01;
	private String formUUID;
	private ArrayList<TableRow> TableRows;
	private TableAdapter adapter;
	private static String strJsons;
	private Spinner spinner01,spinner02;
	private OptionAdapter oAdapter01,oAdapter02;
	private ArrayList<WeiduOptionBean> options01,options02;
	private TextView option_name;

	public static void startThis(Context context, String strJson) {
		strJsons = strJson;
		context.startActivity(new Intent(context, FormActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_fragment_layout);
		spinner01=(Spinner) findViewById(R.id.FormActivity_spinner01);
		spinner02=(Spinner) findViewById(R.id.FormActivity_spinner02);
		options01=new ArrayList<>();
		options02=new ArrayList<>();
		oAdapter01=new OptionAdapter(this, options01);
		oAdapter02=new OptionAdapter(this, options02);
		spinner01.setAdapter(oAdapter01);
		spinner02.setAdapter(oAdapter02);
		option_name=(TextView) findViewById(R.id.FormActivity_option_name);
		
		ListView01 = (ListView) findViewById(R.id.ListView01);
		TableRows = new ArrayList<>();
		adapter = new TableAdapter(this, TableRows);
		ListView01.setAdapter(adapter);
		
		LoadingDialog.createLoadingDialog(this);
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
			LoadingDialog.dimmissLoading();
			FormBean formBean=(FormBean) object;
			TableRows.clear();
			TableRows.addAll(formBean.TableRows);
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "成功", 2000).show();
			
			if(formBean.options.size()==1){
				spinner01.setVisibility(View.VISIBLE);
				options01.addAll(formBean.options.get(0).options);
				oAdapter01.notifyDataSetChanged();
				option_name.setText(formBean.options.get(0).name);
			}if(formBean.options.size()>=2){
				spinner01.setVisibility(View.VISIBLE);
				spinner02.setVisibility(View.VISIBLE);
				option_name.setText(formBean.options.get(0).name);
				options01.addAll(formBean.options.get(0).options);
				options02.addAll(formBean.options.get(1).options);
				oAdapter01.notifyDataSetChanged();
				oAdapter02.notifyDataSetChanged();
			}
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
			LoadingDialog.dimmissLoading();
			Toast.makeText(this, errorMessage.getErrorDes(), 1000).show();
		}
	}
}
