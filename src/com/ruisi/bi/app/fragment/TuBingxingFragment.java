package com.ruisi.bi.app.fragment;

import java.util.HashMap;
import java.util.UUID;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ruisi.bi.app.R;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.TuBingxingParser;

public class TuBingxingFragment extends Fragment implements
		ServerCallbackInterface, OnChartValueSelectedListener {
	private String requestJson;
	private String bingxingUUID;
	private PieChart mChart;

	private Typeface tf;

	public TuBingxingFragment(String requestJson) {
		this.requestJson = requestJson;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tu_bingxing_fragment, null);
		mChart = (PieChart) v.findViewById(R.id.TuBingxingFragment_chart);
		initLineChart();
		sendRequest();
		return v;
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this.getActivity();
		rv.functionPath = APIContext.tu;
		rv.parser = new TuBingxingParser();
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
		mChart.setUsePercentValues(true);
		mChart.setDescription("");

		mChart.setDragDecelerationFrictionCoef(0.95f);

		mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity()
				.getAssets(), "OpenSans-Light.ttf"));

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColorTransparent(true);

		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(110);

		mChart.setHoleRadius(58f);
		mChart.setTransparentCircleRadius(61f);

		mChart.setDrawCenterText(true);

		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);

		// mChart.setUnit(" 鈧�);
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);

		mChart.setCenterText("MPAndroidChart\nby Philipp Jahoda");
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
			mChart.setData((PieData) object);
			mChart.highlightValues(null);
			mChart.invalidate();
			mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
			// mChart.spin(2000, 0, 360);

			Legend l = mChart.getLegend();
			l.setPosition(LegendPosition.RIGHT_OF_CHART);
			l.setXEntrySpace(7f);
			l.setYEntrySpace(0f);
			l.setYOffset(0f);
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {

	}
}