package com.ruisi.bi.app.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
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
import com.ruisi.bi.app.adapter.OptionAdapter;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.bean.WeiduBean;
import com.ruisi.bi.app.bean.WeiduOptionBean;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.TuParser;
import com.ruisi.bi.app.view.LoadingDialog;

public class TuQuxianFragment extends Fragment implements
		ServerCallbackInterface, OnChartValueSelectedListener,
		OnItemSelectedListener, OnClickListener {
	private LineChart mChart;
	private String quxianUUID;
	private String requestJson;

	private Spinner spinner;
	private OptionAdapter oAdapter;
	private ArrayList<WeiduOptionBean> options;
	private Button check;
	private String shaixuanStr;

	public TuQuxianFragment(String requestJson) {
		this.requestJson = requestJson;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tu_quxian_fragment, null);
		mChart = (LineChart) v.findViewById(R.id.chart1);
		spinner = (Spinner) v.findViewById(R.id.quxian_spinner);
		options = new ArrayList<>();
		oAdapter = new OptionAdapter(getActivity(), options);
		spinner.setAdapter(oAdapter);
		spinner.setOnItemSelectedListener(this);
		check = (Button) v.findViewById(R.id.quxian_check);
		check.setOnClickListener(this);
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
		LoadingDialog.createLoadingDialog(getActivity());
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
			ArrayList<Object> dataR = (ArrayList<Object>) object;
			spinner.setVisibility(View.VISIBLE);
			check.setVisibility(View.VISIBLE);
			options.clear();

			options.addAll(((ArrayList<WeiduBean>) dataR.get(0)).get(0).options);
			oAdapter.notifyDataSetChanged();
			LoadingDialog.dimmissLoading();
			mChart.setData((LineData) dataR.get(1));
			updataChart();
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (uuid.equals(quxianUUID)) {
			Toast.makeText(this.getActivity(), errorMessage.getErrorDes(), 1000)
					.show();
			LoadingDialog.dimmissLoading();
		}
	}

	private void updataChart() {
		mChart.animateX(2500);
		// mChart.zoom(4.0f, 4.0f, 0.0f, 0.0f);
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.quxian_check) {
			try {
				JSONObject obj = new JSONObject(requestJson);
				obj.getJSONObject("chartJson").getJSONArray("params").getJSONObject(0)
						.put("vals", shaixuanStr);
				requestJson = obj.toString();
				sendRequest();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		shaixuanStr = options.get(position).value;

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
