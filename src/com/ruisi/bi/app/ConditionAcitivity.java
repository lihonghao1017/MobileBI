package com.ruisi.bi.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ruisi.bi.app.adapter.WeiduAdapter;
import com.ruisi.bi.app.adapter.WeiduShowAdapter;
import com.ruisi.bi.app.adapter.ZhibiaoAdapter;
import com.ruisi.bi.app.adapter.ZhibiaoShowAdapter;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.bean.WeiduBean;
import com.ruisi.bi.app.bean.ZhibiaoBean;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.WeiduParser;
import com.ruisi.bi.app.parser.ZhibiaoParser;
import com.ruisi.bi.app.view.MyPopwindow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConditionAcitivity extends Activity implements
		ServerCallbackInterface, OnClickListener {

	public int ThemeId;
	private String zhibiaoUUID = "", weiduUUID = "";
	private Button weidu, weidu_hang, weidu_lie, zhibiao;
	private ArrayList<WeiduBean> weidus = new ArrayList<>();
	private ArrayList<ZhibiaoBean> zhibiaos = new ArrayList<>();
	private WeiduAdapter weiduAdapter;
	private ZhibiaoAdapter zhibiaoAdapter;
	private WeiduShowAdapter weiduShowAdapter, weiduShowHangAdapter,
			weiduShowLieAdapter;
	private ZhibiaoShowAdapter zhibiaoShowAdapter;
	private ListView weidu_lv, weidu_hang_lv, weidu_lie_lv, zhibiao_lv;

	private ArrayList<WeiduBean> weidusShow = new ArrayList<>();
	private ArrayList<WeiduBean> weidusHangShow = new ArrayList<>();
	private ArrayList<WeiduBean> weidusLieShow = new ArrayList<>();
	private ArrayList<ZhibiaoBean> zhibiaosShow = new ArrayList<>();
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ThemeId = getIntent().getIntExtra("ThemeId", 0);
		setContentView(R.layout.act_condition_fragment_layout);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);

		weidu = (Button) findViewById(R.id.ConditionFragment_Weidu_actv);
		weidu_hang = (Button) findViewById(R.id.ConditionFragment_Weidu_hang_actv);
		weidu_lie = (Button) findViewById(R.id.ConditionFragment_Weidu_lie_actv);
		zhibiao = (Button) findViewById(R.id.ConditionFragment_Zhibiao_actv);

		weidu_lv = (ListView) findViewById(R.id.ConditionFragment_Weidu_listview);
		weidu_hang_lv = (ListView) findViewById(R.id.ConditionFragment_Weidu_hang_listview);
		weidu_lie_lv = (ListView) findViewById(R.id.ConditionFragment_Weidu_lie_listview);
		zhibiao_lv = (ListView) findViewById(R.id.ConditionFragment_Weidu_zhibiao_listview);

		weiduShowAdapter = new WeiduShowAdapter(this, weidusShow);
		weidu_lv.setAdapter(weiduShowAdapter);
		zhibiaoShowAdapter = new ZhibiaoShowAdapter(this, zhibiaosShow);
		zhibiao_lv.setAdapter(zhibiaoShowAdapter);
		weiduShowHangAdapter = new WeiduShowAdapter(this, weidusHangShow);
		weidu_hang_lv.setAdapter(weiduShowHangAdapter);
		weiduShowLieAdapter = new WeiduShowAdapter(this, weidusLieShow);
		weidu_lie_lv.setAdapter(weiduShowLieAdapter);

		zhibiao.setOnClickListener(this);
		weidu.setOnClickListener(this);
		weidu_hang.setOnClickListener(this);
		weidu_lie.setOnClickListener(this);
		weiduAdapter = new WeiduAdapter(this, weidus);
		zhibiaoAdapter = new ZhibiaoAdapter(this, zhibiaos);

		sendZhibiaoRequest();
		sendWeiduRequest();
	}

	private void sendZhibiaoRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this;
		rv.functionPath = APIContext.Zhibiao;
		// rv.modulePath = APIContext.Login;
		rv.parser = new ZhibiaoParser();
		rv.Type = APIContext.GET;
		zhibiaoUUID = UUID.randomUUID().toString();
		rv.uuId = zhibiaoUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("tableid", ThemeId + "");
		// map.put("password", "123456");
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	private void sendWeiduRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this;
		rv.functionPath = APIContext.Weidu;
		// rv.modulePath = APIContext.Login;
		rv.parser = new WeiduParser();
		rv.Type = APIContext.GET;
		weiduUUID = UUID.randomUUID().toString();
		rv.uuId = weiduUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("tableid", ThemeId + "");
		// map.put("password", "123456");
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if (weiduUUID.equals(uuid)) {
			weidus.clear();
			weidus.addAll((Collection<? extends WeiduBean>) object);
			weiduAdapter.notifyDataSetChanged();
		}
		if (zhibiaoUUID.equals(uuid)) {
			zhibiaos.clear();
			zhibiaos.addAll((Collection<? extends ZhibiaoBean>) object);
			zhibiaoAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ConditionFragment_Zhibiao_actv:
			if (zhibiaos.size() > 0) {
				MyPopwindow.getSaveNotePoPwindow(this, zhibiao, zhibiaoAdapter,
						3);
			}
			break;
		case R.id.ConditionFragment_Weidu_actv:
			if (weidus.size() > 0) {
				for (int i = 0; i < weidus.size(); i++) {
					weidus.get(i).isChecked = false;
					for (int j = 0; j < weidusShow.size(); j++) {
						if (weidus.get(i).text.equals(weidusShow.get(j).text))
							weidus.get(i).isChecked = true;
					}
				}
				weiduAdapter.notifyDataSetChanged();
				MyPopwindow.getSaveNotePoPwindow(this, weidu, weiduAdapter, 0);
			}
			break;
		case R.id.ConditionFragment_Weidu_hang_actv:
			if (weidus.size() > 0) {
				for (int i = 0; i < weidus.size(); i++) {
					weidus.get(i).isChecked = false;
					for (int j = 0; j < weidusHangShow.size(); j++) {
						if (weidus.get(i).text
								.equals(weidusHangShow.get(j).text))
							weidus.get(i).isChecked = true;
					}
				}
				weiduAdapter.notifyDataSetChanged();
				MyPopwindow.getSaveNotePoPwindow(this, weidu, weiduAdapter, 1);
			}
			break;
		case R.id.ConditionFragment_Weidu_lie_actv:
			if (weidus.size() > 0) {
				for (int i = 0; i < weidus.size(); i++) {
					weidus.get(i).isChecked = false;
					for (int j = 0; j < weidusLieShow.size(); j++) {
						if (weidus.get(i).text
								.equals(weidusLieShow.get(j).text))
							weidus.get(i).isChecked = true;
					}
				}
				weiduAdapter.notifyDataSetChanged();
				MyPopwindow.getSaveNotePoPwindow(this, weidu, weiduAdapter, 2);
			}
			break;
		case R.id.back:
			this.finish();
			break;
		case R.id.tofrom:
			if(zhibiaosShow.size()>0&&(weidusHangShow.size()>0||weidusLieShow.size()>0)){
				try {
					FormActivity.startThis(this, getConditionJson());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else {
				Toast.makeText(this, "请正确选择！！！", 1000).show();
			}
			break;
		case R.id.totu:
			if(zhibiaosShow.size()>0&&(weidusHangShow.size()>0||weidusLieShow.size()>0)){
				try {
					TuActivity.startThis(this, getChatJson());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else {
				Toast.makeText(this, "请正确选择！！！", 1000).show();
			}
			break;
		default:
			break;
		}
	}

	public void setListViewData(int flag) {
		if (flag == 0) {
			weidusShow.clear();
			for (int i = 0; i < weidus.size(); i++) {
				WeiduBean weiduBean = weidus.get(i);
				if (weiduBean.isChecked)
					weidusShow.add(weiduBean);
			}
			weiduShowAdapter.notifyDataSetChanged();
		} else if (flag == 1) {
			weidusHangShow.clear();
			for (int i = 0; i < weidus.size(); i++) {
				WeiduBean weiduBean = weidus.get(i);
				if (weiduBean.isChecked)
					weidusHangShow.add(weiduBean);
			}
			weiduShowHangAdapter.notifyDataSetChanged();
		} else if (flag == 2) {
			weidusLieShow.clear();
			for (int i = 0; i < weidus.size(); i++) {
				WeiduBean weiduBean = weidus.get(i);
				if (weiduBean.isChecked)
					weidusLieShow.add(weiduBean);
			}
			weiduShowLieAdapter.notifyDataSetChanged();
		} else if (flag == 3) {
			zhibiaosShow.clear();
			for (int i = 0; i < zhibiaos.size(); i++) {
				ZhibiaoBean weiduBean = zhibiaos.get(i);
				if (weiduBean.isChecked)
					zhibiaosShow.add(weiduBean);
			}
			zhibiaoShowAdapter.notifyDataSetChanged();
		}
	}

	public String getConditionJson() throws JSONException {
		JSONObject objALL = new JSONObject();
		JSONObject obj = new JSONObject();
		JSONArray zhibiaoArray = new JSONArray();
		for (int i = 0; i < zhibiaosShow.size(); i++) {
			JSONObject zhibiaoObj = new JSONObject();
			ZhibiaoBean zhibiaoBean = zhibiaosShow.get(i);
			zhibiaoObj.put("tid", zhibiaoBean.tid);
			zhibiaoObj.put("unit", zhibiaoBean.unit);
			zhibiaoObj.put("rate", zhibiaoBean.rate);
			zhibiaoObj.put("alias", zhibiaoBean.alias);
			zhibiaoObj.put("fmt", zhibiaoBean.fmt);
			zhibiaoObj.put("aggre", zhibiaoBean.aggre);
			zhibiaoObj.put("col_name", zhibiaoBean.col_name);
			zhibiaoObj.put("aggre", zhibiaoBean.aggre);
			zhibiaoObj.put("kpi_name", zhibiaoBean.text);
			zhibiaoObj.put("kpi_id", zhibiaoBean.col_id);
			zhibiaoArray.put(zhibiaoObj);
		}
		obj.put("kpiJson", zhibiaoArray);

		JSONObject objTable = new JSONObject();
		JSONArray arrayHang = new JSONArray();
		for (int i = 0; i < weidusHangShow.size(); i++) {
			JSONObject weidusHangObj = new JSONObject();
			WeiduBean weiduhangObj = weidusHangShow.get(i);
			weidusHangObj.put("tid", weiduhangObj.tid);
			weidusHangObj.put("valType", weiduhangObj.valType);
			weidusHangObj.put("iscas", weiduhangObj.iscas);
			weidusHangObj.put("grouptype", weiduhangObj.grouptype);
			weidusHangObj.put("dim_name", weiduhangObj.dim_name);
			weidusHangObj.put("dimord", weiduhangObj.dimord);
			weidusHangObj.put("tableName", weiduhangObj.tableName);
			weidusHangObj.put("tableColName", weiduhangObj.tableColName);
			weidusHangObj.put("tableColKey", weiduhangObj.tableColKey);
			weidusHangObj.put("tname", weiduhangObj.tname);
			weidusHangObj.put("id", weiduhangObj.col_id);
			weidusHangObj.put("dimdesc", weiduhangObj.text);
			weidusHangObj.put("colname", weiduhangObj.col_name);
			weidusHangObj.put("type", weiduhangObj.dim_type);
			arrayHang.put(weidusHangObj);
		}
		objTable.put("rows", arrayHang);

		JSONArray arrayLie = new JSONArray();
		for (int i = 0; i < weidusLieShow.size(); i++) {
			JSONObject weidusLieObj = new JSONObject();
			WeiduBean weiduLieObj = weidusLieShow.get(i);
			weidusLieObj.put("tid", weiduLieObj.tid);
			weidusLieObj.put("valType", weiduLieObj.valType);
			weidusLieObj.put("iscas", weiduLieObj.iscas);
			weidusLieObj.put("grouptype", weiduLieObj.grouptype);
			weidusLieObj.put("dim_name", weiduLieObj.dim_name);
			weidusLieObj.put("dimord", weiduLieObj.dimord);
			weidusLieObj.put("tableName", weiduLieObj.tableName);
			weidusLieObj.put("tableColName", weiduLieObj.tableColName);
			weidusLieObj.put("tableColKey", weiduLieObj.tableColKey);
			weidusLieObj.put("tname", weiduLieObj.tname);
			weidusLieObj.put("id", weiduLieObj.col_id);
			weidusLieObj.put("dimdesc", weiduLieObj.text);
			weidusLieObj.put("colname", weiduLieObj.col_name);
			weidusLieObj.put("type", weiduLieObj.dim_type);
			arrayLie.put(weidusLieObj);
		}
		JSONObject weidusLieObjWei = new JSONObject();
		weidusLieObjWei.put("id", "kpi");
		weidusLieObjWei.put("type", "kpiOther");
		arrayLie.put(weidusLieObjWei);
		objTable.put("cols", arrayLie);

		obj.put("tableJson", objTable);

		objALL.put("table", obj);

		JSONArray WeiduArray = new JSONArray();
		for (int i = 0; i < weidusShow.size(); i++) {
			JSONObject weidusObj = new JSONObject();
			WeiduBean weiduLieObj = weidusShow.get(i);
			weidusObj.put("id", weiduLieObj.col_id);
			weidusObj.put("name", weiduLieObj.text);
			weidusObj.put("type", weiduLieObj.dim_type);
			weidusObj.put("colname", weiduLieObj.col_name);
			weidusObj.put("tname", weiduLieObj.tname);
			weidusObj.put("tid", weiduLieObj.tid);
			weidusObj.put("valType", weiduLieObj.valType);
			weidusObj.put("tableName", weiduLieObj.tableName);
			weidusObj.put("tableColKey", weiduLieObj.tableColKey);
			weidusObj.put("tableColName", weiduLieObj.tableColName);
			weidusObj.put("dimord", weiduLieObj.dimord);
			weidusObj.put("grouptype", weiduLieObj.grouptype);
			WeiduArray.put(weidusObj);
		}
		objALL.put("params", WeiduArray);
		return objALL.toString();
	}
	public String getChatJson() throws JSONException {
		JSONObject obj = new JSONObject();
		JSONArray zhibiaoArray = new JSONArray();
		for (int i = 0; i < zhibiaosShow.size(); i++) {
			JSONObject zhibiaoObj = new JSONObject();
			ZhibiaoBean zhibiaoBean = zhibiaosShow.get(i);
			zhibiaoObj.put("tid", zhibiaoBean.tid);
			zhibiaoObj.put("unit", zhibiaoBean.unit);
			zhibiaoObj.put("rate", zhibiaoBean.rate);
			zhibiaoObj.put("alias", zhibiaoBean.alias);
			zhibiaoObj.put("fmt", zhibiaoBean.fmt);
			zhibiaoObj.put("aggre", zhibiaoBean.aggre);
			zhibiaoObj.put("col_name", zhibiaoBean.col_name);
			zhibiaoObj.put("aggre", zhibiaoBean.aggre);
			zhibiaoObj.put("kpi_name", zhibiaoBean.text);
			zhibiaoObj.put("kpi_id", zhibiaoBean.col_id);
			zhibiaoArray.put(zhibiaoObj);
		}
		obj.put("kpiJson", zhibiaoArray);

		JSONObject objTable = new JSONObject();
//		JSONArray arrayHang = new JSONArray();
//		for (int i = 0; i < weidusHangShow.size(); i++) {
			JSONObject weidusHangObj = new JSONObject();
			WeiduBean weiduhangObj = weidusHangShow.get(0);
			weidusHangObj.put("tid", weiduhangObj.tid);
			weidusHangObj.put("valType", weiduhangObj.valType);
			weidusHangObj.put("iscas", weiduhangObj.iscas);
			weidusHangObj.put("grouptype", weiduhangObj.grouptype);
			weidusHangObj.put("dim_name", weiduhangObj.dim_name);
			weidusHangObj.put("dimord", weiduhangObj.dimord);
			weidusHangObj.put("tableName", weiduhangObj.tableName);
			weidusHangObj.put("tableColName", weiduhangObj.tableColName);
			weidusHangObj.put("tableColKey", weiduhangObj.tableColKey);
			weidusHangObj.put("tname", weiduhangObj.tname);
			weidusHangObj.put("id", weiduhangObj.col_id);
			weidusHangObj.put("dimdesc", weiduhangObj.text);
			weidusHangObj.put("colname", weiduhangObj.col_name);
			weidusHangObj.put("type", weiduhangObj.dim_type);
//			arrayHang.put(weidusHangObj);
//		}
		objTable.put("scol", weidusHangObj);
		objTable.put("type", "line");
//		JSONArray arrayLie = new JSONArray();
//		for (int i = 0; i < weidusLieShow.size(); i++) {
			JSONObject weidusLieObj = new JSONObject();
			WeiduBean weiduLieObj = weidusLieShow.get(0);
			weidusLieObj.put("tid", weiduLieObj.tid);
			weidusLieObj.put("valType", weiduLieObj.valType);
			weidusLieObj.put("iscas", weiduLieObj.iscas);
			weidusLieObj.put("grouptype", weiduLieObj.grouptype);
			weidusLieObj.put("dim_name", weiduLieObj.dim_name);
			weidusLieObj.put("dimord", weiduLieObj.dimord);
			weidusLieObj.put("tableName", weiduLieObj.tableName);
			weidusLieObj.put("tableColName", weiduLieObj.tableColName);
			weidusLieObj.put("tableColKey", weiduLieObj.tableColKey);
			weidusLieObj.put("tname", weiduLieObj.tname);
			weidusLieObj.put("id", weiduLieObj.col_id);
			weidusLieObj.put("dimdesc", weiduLieObj.text);
			weidusLieObj.put("colname", weiduLieObj.col_name);
			weidusLieObj.put("type", weiduLieObj.dim_type);
//			arrayLie.put(weidusLieObj);
//		}
//		JSONObject weidusLieObjWei = new JSONObject();
//		weidusLieObjWei.put("id", "kpi");
//		weidusLieObjWei.put("type", "kpiOther");
//		arrayLie.put(weidusLieObjWei);
		objTable.put("xcol", weidusLieObj);

		obj.put("chartJson", objTable);

//		objALL.put("table", obj);

		JSONArray WeiduArray = new JSONArray();
		for (int i = 0; i < weidusShow.size(); i++) {
			JSONObject weidusObj = new JSONObject();
			WeiduBean weiduLieObj01 = weidusShow.get(i);
			weidusObj.put("id", weiduLieObj01.col_id);
			weidusObj.put("name", weiduLieObj01.text);
			weidusObj.put("type", weiduLieObj01.dim_type);
			weidusObj.put("colname", weiduLieObj01.col_name);
			weidusObj.put("tname", weiduLieObj01.tname);
			weidusObj.put("tid", weiduLieObj01.tid);
			weidusObj.put("valType", weiduLieObj01.valType);
			weidusObj.put("tableName", weiduLieObj01.tableName);
			weidusObj.put("tableColKey", weiduLieObj01.tableColKey);
			weidusObj.put("tableColName", weiduLieObj01.tableColName);
			weidusObj.put("dimord", weiduLieObj01.dimord);
			weidusObj.put("grouptype", weiduLieObj01.grouptype);
			WeiduArray.put(weidusObj);
		}
//		objALL.put("params", WeiduArray);
		obj.put("params", WeiduArray);
		return obj.toString();
	}

}
