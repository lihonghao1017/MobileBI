package com.ruisi.bi.app.fragment;

import java.util.HashMap;
import java.util.UUID;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.ruisi.bi.app.R;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.TuZhuxingParser;
import com.ruisi.bi.app.view.LoadingDialog;
import com.ruisi.bi.app.view.MyValueFormatter;

public class TuTiaoxingFragment extends Fragment implements
		ServerCallbackInterface, OnChartValueSelectedListener {
	private HorizontalBarChart mChart;;
	private String requestJson;
	private Typeface tf;
	private String zhuxingUUID;

	public TuTiaoxingFragment(String requestJson) {
		this.requestJson = requestJson;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tu_tiaoxing_fragment, null);
		mChart = (HorizontalBarChart) v.findViewById(R.id.chart1);
		initLineChart();
		LoadingDialog.createLoadingDialog(getActivity());
		sendRequest();
		return v;
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this.getActivity();
		rv.functionPath = APIContext.tu;
		rv.parser = new TuZhuxingParser();
		rv.Type = APIContext.POST;
		zhuxingUUID = UUID.randomUUID().toString();
		rv.uuId = zhuxingUUID;
		rv.isSaveToLocation = false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageInfo", requestJson);
		rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	private void initLineChart() {
		mChart.setOnChartValueSelectedListener(this);
		// mChart.setHighlightEnabled(false);

		mChart.setDrawBarShadow(false);

		mChart.setDrawValueAboveBar(true);

		mChart.setDescription("");

		// if more than 60 entries are displayed in the chart, no values will be
		// drawn
		mChart.setMaxVisibleValueCount(60);

		// scaling can now only be done on x- and y-axis separately
		mChart.setPinchZoom(false);

		// draw shadows for each bar that show the maximum value
		// mChart.setDrawBarShadow(true);

		// mChart.setDrawXLabels(false);

		mChart.setDrawGridBackground(false);

		// mChart.setDrawYLabels(false);

		tf = Typeface.createFromAsset(getActivity().getAssets(),
				"OpenSans-Regular.ttf");

		XAxis xl = mChart.getXAxis();
		xl.setPosition(XAxisPosition.BOTTOM);
		xl.setTypeface(tf);
		xl.setDrawAxisLine(true);
		xl.setDrawGridLines(true);
		xl.setGridLineWidth(0.3f);

		YAxis yl = mChart.getAxisLeft();
		yl.setTypeface(tf);
		yl.setDrawAxisLine(true);
		yl.setDrawGridLines(true);
		yl.setGridLineWidth(0.3f);
		// yl.setInverted(true);

		YAxis yr = mChart.getAxisRight();
		yr.setTypeface(tf);
		yr.setDrawAxisLine(true);
		yr.setDrawGridLines(false);
		// yr.setInverted(true);

	}

	@Override
	public void onNothingSelected() {

	}

	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {

	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if (uuid.equals(zhuxingUUID)) {
			LoadingDialog.dimmissLoading();
			BarData data = (BarData) object;
			data.setValueTextSize(10f);
			data.setValueTypeface(tf);
			mChart.setData(data);
			mChart.animateY(2000);
			Legend l = mChart.getLegend();
			l.setPosition(LegendPosition.BELOW_CHART_LEFT);
			l.setFormSize(8f);
			l.setXEntrySpace(4f);
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (uuid.equals(zhuxingUUID)) {
			Toast.makeText(this.getActivity(), errorMessage.getErrorDes(), 1000)
					.show();
			LoadingDialog.dimmissLoading();
		}
	}

}
