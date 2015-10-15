package com.ruisi.bi.app.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ruisi.bi.app.R;
import com.ruisi.bi.app.adapter.OptionAdapter;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.bean.WeiduBean;
import com.ruisi.bi.app.bean.WeiduOptionBean;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.TuLeidaParser;
import com.ruisi.bi.app.view.LoadingDialog;
import com.ruisi.bi.app.view.MyMarkerView;

public class TuLeidaFragment extends Fragment implements
		ServerCallbackInterface, OnChartValueSelectedListener,OnItemSelectedListener, OnClickListener {
	private String requestJson;
	private String bingxingUUID;
	private RadarChart mChart;

	private Typeface tf;
	
	private Spinner spinner01, spinner02;
	private OptionAdapter oAdapter01, oAdapter02;
	private ArrayList<WeiduOptionBean> options01, options02;
	private TextView option_name;
	private String shaixuan01, shaixuan02;
	private int index01, index02;
	
	private Button check;
	

	public TuLeidaFragment(String requestJson) {
		this.requestJson = requestJson;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tu_leida_fragment, null);
		mChart = (RadarChart) v.findViewById(R.id.chart1);
		spinner01 = (Spinner) v.findViewById(R.id.zhuxing_spinner01);
		spinner02 = (Spinner) v.findViewById(R.id.zhuxing_spinner02);

		spinner02.setOnItemSelectedListener(this);
		spinner01.setOnItemSelectedListener(this);
		options01 = new ArrayList<>();
		options02 = new ArrayList<>();
		oAdapter01 = new OptionAdapter(getActivity(), options01);
		oAdapter02 = new OptionAdapter(getActivity(), options02);
		spinner01.setAdapter(oAdapter01);
		spinner02.setAdapter(oAdapter02);
		option_name = (TextView) v.findViewById(R.id.zhuxing_option_name);
		check = (Button) v.findViewById(R.id.zhuxing_check);
		check.setOnClickListener(this);
		
		initLineChart();
		sendRequest();
		return v;
	}

	private void sendRequest() {
		LoadingDialog.createLoadingDialog(getActivity());
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this.getActivity();
		rv.functionPath = APIContext.tu;
		rv.parser = new TuLeidaParser();
		rv.Type = APIContext.POST;
		bingxingUUID = UUID.randomUUID().toString();
		rv.uuId = bingxingUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageInfo", requestJson);
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	private void initLineChart() {
		tf = Typeface.createFromAsset(getActivity().getAssets(),
				"OpenSans-Regular.ttf");

		mChart.setDescription("");

		mChart.setWebLineWidth(1.5f);
		mChart.setWebLineWidthInner(0.75f);
		mChart.setWebAlpha(100);

		// create a custom MarkerView (extend MarkerView) and specify the layout
		// to use for it
		MyMarkerView mv = new MyMarkerView(getActivity(),
				R.layout.custom_marker_view);

		// set the marker to the chart
		mChart.setMarkerView(mv);
	}

	@Override
	public void onNothingSelected() {

	}

	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {

	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if (uuid.equals(bingxingUUID)) {
			ArrayList<Object> dataR = (ArrayList<Object>) object;
if(((ArrayList<WeiduBean>) dataR.get(0)).size()==1){
	getActivity().findViewById(R.id.zhuxing_check).setVisibility(View.VISIBLE);
	getActivity().findViewById(R.id.zhuxing_option_name).setVisibility(View.VISIBLE);
	options01.clear();
	spinner01.setVisibility(View.VISIBLE);
	options01.addAll(((ArrayList<WeiduBean>) dataR.get(0)).get(0).options);
	oAdapter01.notifyDataSetChanged();
	spinner01.setSelection(index01);
	option_name.setText(((ArrayList<WeiduBean>) dataR.get(0)).get(0).name);
}
if (((ArrayList<WeiduBean>) dataR.get(0)).size() >= 2) {
	getActivity().findViewById(R.id.zhuxing_check).setVisibility(View.VISIBLE);
	getActivity().findViewById(R.id.zhuxing_option_name).setVisibility(View.VISIBLE);
	options01.clear();
	options02.clear();
	spinner01.setVisibility(View.VISIBLE);
	spinner02.setVisibility(View.VISIBLE);
	option_name.setText(((ArrayList<WeiduBean>) dataR.get(0)).get(0).name);
	options01.addAll(((ArrayList<WeiduBean>) dataR.get(0)).get(0).options);
	options02.addAll(((ArrayList<WeiduBean>) dataR.get(0)).get(1).options);
	oAdapter01.notifyDataSetChanged();
	oAdapter02.notifyDataSetChanged();
	spinner01.setSelection(index01);
	spinner02.setSelection(index02);
}
			LoadingDialog.dimmissLoading();
			RadarData data = (RadarData) dataR.get(1);
			data.setValueTextSize(10f);
			data.setValueTypeface(tf);
			mChart.setData(data);
			mChart.invalidate();
			XAxis xAxis = mChart.getXAxis();
			xAxis.setTypeface(tf);
			xAxis.setTextSize(9f);

			YAxis yAxis = mChart.getYAxis();
			yAxis.setTypeface(tf);
			yAxis.setLabelCount(5, false);
			yAxis.setTextSize(9f);
			yAxis.setStartAtZero(true);

			Legend l = mChart.getLegend();
			l.setPosition(LegendPosition.RIGHT_OF_CHART);
			l.setTypeface(tf);
			l.setXEntrySpace(7f);
			l.setYEntrySpace(5f);
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (uuid.equals(bingxingUUID)) {
			Toast.makeText(this.getActivity(), errorMessage.getErrorDes(), 1000)
					.show();
			LoadingDialog.dimmissLoading();
		}
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.zhuxing_check) {
			setParams();
		}
	}
	private void setParams() {
		if (spinner01.getVisibility() == View.VISIBLE
				&& spinner02.getVisibility() == View.VISIBLE) {
			if (shaixuan01 == null) {
				Toast.makeText(getActivity(), "第一个未选择", 1000).show();
				return;
			}
			if (shaixuan02 == null) {
				Toast.makeText(getActivity(), "第二个未选择", 1000).show();
				return;
			}
			
			try {
				JSONObject obj = new JSONObject(requestJson);
				obj.getJSONObject("chartJson").getJSONArray("params").getJSONObject(0)
						.put("vals", shaixuan01 + "," + shaixuan02);
				requestJson = obj.toString();
				sendRequest();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return;
		}
		if (spinner01.getVisibility() == View.VISIBLE) {
			try {
				JSONObject obj = new JSONObject(requestJson);
				obj.getJSONObject("chartJson").getJSONArray("params").getJSONObject(0)
						.put("vals", shaixuan01);
				requestJson = obj.toString();
				sendRequest();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.zhuxing_spinner01:
			index01 = position;
			shaixuan01 = options01.get(position).value;
			break;
		case R.id.zhuxing_spinner02:
			index02 = position;
			shaixuan02 = options02.get(position).value;
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
