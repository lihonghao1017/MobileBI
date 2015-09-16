package com.ruisi.bi.app.fragment;

import java.util.HashMap;
import java.util.UUID;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ruisi.bi.app.R;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.TuLeidaParser;
import com.ruisi.bi.app.view.MyMarkerView;

public class TuLeidaFragment extends Fragment implements
		ServerCallbackInterface, OnChartValueSelectedListener {
	private String requestJson;
	private String bingxingUUID;
	private RadarChart mChart;

	private Typeface tf;

	public TuLeidaFragment(String requestJson) {
		this.requestJson = requestJson;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tu_leida_fragment, null);
		mChart = (RadarChart) v.findViewById(R.id.chart1);
		initLineChart();
		sendRequest();
		return v;
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this.getActivity();
		rv.functionPath = APIContext.tu;
		rv.parser = new TuLeidaParser();
		rv.Type = APIContext.GET;
		bingxingUUID = UUID.randomUUID().toString();
		rv.uuId = bingxingUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageInfo", requestJson);
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	private void initLineChart() {
		tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        mChart.setDescription("");

        mChart.setWebLineWidth(1.5f);
        mChart.setWebLineWidthInner(0.75f);
        mChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

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
			mChart.setData((RadarData) object);

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

	}
}
