package com.ruisi.bi.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.BaseParser;
import com.ruisi.bi.app.parser.TuParser;
import com.ruisi.bi.app.parser.TuZhuxingParser;
import com.ruisi.bi.app.view.MyValueFormatter;

public class TuActivity extends Activity implements ServerCallbackInterface,
		OnChartValueSelectedListener, OnItemSelectedListener {
	private static String strJsons;
	private String tuUUID;
	private LineChart mChart;
	private BarChart mChart1;
	 private Typeface mTf;
	private int postion;

	public static void startThis(Context context, String strJson) {
		strJsons = strJson;
		context.startActivity(new Intent(context, TuActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tu_activity_layout);
		mChart = (LineChart) findViewById(R.id.chart1);
		initChart();
		mChart1 = (BarChart) findViewById(R.id.chart2);
		initChart1();

		ArrayList data_list = new ArrayList<String>();
		data_list.add("曲线图");
		data_list.add("柱状图");
		data_list.add("饼图");
		data_list.add("条形图");
		data_list.add("面积图");
		data_list.add("雷达图");

		ArrayAdapter arr_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data_list);
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		((Spinner) findViewById(R.id.TuActivity_spinner))
				.setAdapter(arr_adapter);
		((Spinner) findViewById(R.id.TuActivity_spinner))
				.setOnItemSelectedListener(this);
		sendRequest(new TuParser());
	}
	private void initChart1(){
		mChart1.setOnChartValueSelectedListener(this);

        mChart1.setDrawBarShadow(false);
        mChart1.setDrawValueAboveBar(true);

        mChart1.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart1.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart1.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart1.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart1.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = mChart1.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart1.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);

        Legend l = mChart.getLegend();//图表标签
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.LINE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
	}
	private void initChart() {
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

	private void updataChart() {
		mChart.animateX(2500);

		Typeface tf = Typeface.createFromAsset(getAssets(),
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

	private void sendRequest(BaseParser parser) {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this;
		rv.functionPath = APIContext.tu;
		rv.parser =parser  ;
		rv.Type = APIContext.GET;
		tuUUID = UUID.randomUUID().toString();
		rv.uuId = tuUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageInfo", strJsons);
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
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
	public <T> void succeedReceiveData(T object, String uuid) {
		if (uuid.equals(tuUUID)) {
			if(postion==0){
				mChart.setVisibility(View.VISIBLE);
				mChart1.setVisibility(View.GONE);
				mChart.setData((LineData) object);
				updataChart();
			}else if(postion==1){
				mChart.setVisibility(View.GONE);
				mChart1.setVisibility(View.VISIBLE);
				mChart1.setData((BarData) object);
			}
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (uuid.equals(tuUUID)) {
			Toast.makeText(this, "异常拉！！！", 1000).show();
		}
	}

	@Override
	public void onNothingSelected() {

	}

	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			postion = 0;
			JSONObject obj = null;
			try {
				obj = new JSONObject(strJsons);
				obj.getJSONObject("chartJson").put("type", "line");
				strJsons = obj.toString();
				sendRequest(new TuParser());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 1:
			postion = 1;
			JSONObject obj1 = null;
			try {
				obj1 = new JSONObject(strJsons);
				obj1.getJSONObject("chartJson").put("type", "column");
				strJsons = obj1.toString();
				sendRequest(new TuZhuxingParser());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:

			break;
		case 3:

			break;

		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
