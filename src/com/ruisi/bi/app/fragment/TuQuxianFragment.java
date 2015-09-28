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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ruisi.bi.app.R;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.TuParser;

public class TuQuxianFragment extends Fragment implements
		ServerCallbackInterface, OnChartValueSelectedListener {
	private LineChart mChart;
	private String quxianUUID;
	private String requestJson;

	public TuQuxianFragment(String requestJson) {
		this.requestJson = requestJson;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tu_quxian_fragment, null);
		mChart = (LineChart) v.findViewById(R.id.chart1);
		initLineChart();
		sendRequest();
		return v;
	}

	private void initLineChart() {
		mChart.setOnChartValueSelectedListener(this);
		mChart.setDescription("");
		mChart.setNoDataTextDescription("You need to provide data for the chart.");

		// enable value highlighting
		mChart.setHighlightEnabled(true);

		// enable touch gestures
		mChart.setTouchEnabled(true);

		mChart.setDragDecelerationFrictionCoef(0.9f);

		// enable scaling and dragging
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setDrawGridBackground(false);
		mChart.setHighlightPerDragEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		mChart.setPinchZoom(true);

		// set an alternative background color
		mChart.setBackgroundColor(Color.LTGRAY);
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this.getActivity();
		rv.functionPath = APIContext.tu;
		rv.parser = new TuParser();
		rv.Type = APIContext.POST;
		quxianUUID = UUID.randomUUID().toString();
		rv.uuId = quxianUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageInfo", requestJson);
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if (uuid.equals(quxianUUID)) {
			mChart.setData((LineData) object);
			updataChart();
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (uuid.equals(quxianUUID))
			Toast.makeText(this.getActivity(), errorMessage.getErrorDes(), 1000).show();
	}

	private void updataChart() {
		mChart.animateX(2500);
		mChart.zoom(4.0f, 4.0f, 0.0f, 0.0f);
		Typeface tf = Typeface.createFromAsset(this.getActivity().getAssets(),
				"OpenSans-Regular.ttf");

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();

		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.LINE);
		l.setTypeface(tf);
		l.setTextSize(11f);
		l.setTextColor(Color.WHITE);
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		// l.setYOffset(11f);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setTypeface(tf);
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setTextSize(12f);
		xAxis.setTextColor(Color.WHITE);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(false);
		xAxis.setSpaceBetweenLabels(1);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setTypeface(tf);
		leftAxis.setTextColor(ColorTemplate.getHoloBlue());
		leftAxis.setAxisMaxValue(200f);
		leftAxis.setDrawGridLines(true);

		mChart.getAxisRight().setEnabled(false);
	}

	@Override
	public void onNothingSelected() {

	}

	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {

	}

}
