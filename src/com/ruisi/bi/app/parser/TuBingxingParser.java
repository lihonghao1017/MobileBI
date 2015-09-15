package com.ruisi.bi.app.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

public class TuBingxingParser extends BaseParser {

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		PieData data = null;
		if (jsonStr != null) {
			JSONObject obj = new JSONObject(jsonStr);
			JSONArray objArray = obj.getJSONArray("comps");
			for (int i = 0; i < objArray.length(); i++) {
				JSONObject objdata = objArray.getJSONObject(i);
				JSONArray headArray = objdata.getJSONArray("yVals");
				ArrayList<Entry> yVals1 = new ArrayList<Entry>();
				for (int j = 0; j < headArray.length(); j++) {
					JSONObject yObj = headArray.getJSONObject(j);
					yVals1.add(new Entry(yObj.getInt("value"), yObj
							.getInt("index")));
				}
				JSONArray xArray = objdata.getJSONArray("xVals");
				ArrayList<String> xVals = new ArrayList<String>();
				for (int j = 0; j < xArray.length(); j++) {
					xVals.add(xArray.getString(j));
				}
				PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
				dataSet.setSliceSpace(3f);
				dataSet.setSelectionShift(5f);
				ArrayList<Integer> colors = new ArrayList<Integer>();

				for (int c : ColorTemplate.VORDIPLOM_COLORS)
					colors.add(c);

				for (int c : ColorTemplate.JOYFUL_COLORS)
					colors.add(c);

				for (int c : ColorTemplate.COLORFUL_COLORS)
					colors.add(c);

				for (int c : ColorTemplate.LIBERTY_COLORS)
					colors.add(c);

				for (int c : ColorTemplate.PASTEL_COLORS)
					colors.add(c);

				colors.add(ColorTemplate.getHoloBlue());

				dataSet.setColors(colors);
				data = new PieData(xVals, dataSet);
				  data.setValueFormatter(new PercentFormatter());
			        data.setValueTextSize(11f);
			        data.setValueTextColor(Color.WHITE);
			}
		}

		return (T) data;
	}

}
