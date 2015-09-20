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
import com.ruisi.bi.app.view.MyValueFormatter;

public class TuZhuxingFragment extends Fragment implements
		ServerCallbackInterface, OnChartValueSelectedListener {
	private BarChart mChart1;;
	private String requestJson;
	 private Typeface mTf;
	 private String zhuxingUUID;

	public TuZhuxingFragment(String requestJson) {
		this.requestJson = requestJson;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tu_zhuxing_fragment, null);
		mChart1 = (BarChart) v.findViewById(R.id.chart2);
		initLineChart();
		sendRequest();
		return v;
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this.getActivity();
		rv.functionPath = APIContext.tu;
		rv.parser =new TuZhuxingParser();
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

        mTf = Typeface.createFromAsset(this.getActivity().getAssets(), "OpenSans-Regular.ttf");

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

        Legend l = mChart1.getLegend();//图表标签
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.LINE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
	}

	@Override
	public void onNothingSelected() {
		
	}

	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {
		
	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if(uuid.equals(zhuxingUUID)){
			mChart1.setData((BarData) object);
			mChart1.animateY(3000);
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {
		if (uuid.equals(zhuxingUUID))
			Toast.makeText(this.getActivity(), errorMessage.getErrorDes(), 1000).show();
	}

}
