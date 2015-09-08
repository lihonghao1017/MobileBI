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
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class TuZhuxingParser extends BaseParser {

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		BarData data = null;
		if (jsonStr != null) {
			JSONObject obj = new JSONObject(jsonStr);
			JSONArray objArray = obj.getJSONArray("comps");
			for (int i = 0; i < objArray.length(); i++) {
				JSONObject objdata = objArray.getJSONObject(i);
				JSONArray headArray = objdata.getJSONArray("yVals");
				ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
				for (int j = 0; j < headArray.length(); j++) {
					JSONObject itemData = headArray.getJSONObject(j);
					JSONArray itemArray = itemData.getJSONArray("Entry");
					ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
					for (int k = 0; k < itemArray.length(); k++) {
						JSONObject entryData = itemArray.getJSONObject(k);
						yVals2.add(new BarEntry(entryData.getInt("value"), entryData.getInt("xIndex")));
					}
					BarDataSet set2 = new BarDataSet(yVals2, itemData.getString("label"));
				        set2.setAxisDependency(AxisDependency.RIGHT);
				        set2.setColor(Color.RED);
				        set2.setBarSpacePercent(35f);
				        set2.setHighLightColor(Color.rgb(244, 117, 117));
				        dataSets.add(set2);
				}
				JSONArray xValsArray = objdata.getJSONArray("xVals");
				ArrayList<String> xVals = new ArrayList<String>();
				for (int j = 0; j < xValsArray.length(); j++) {
					xVals.add(xValsArray.getString(j));
				}
				data = new BarData(xVals, dataSets);
				data.setValueTextColor(Color.WHITE);
				data.setValueTextSize(9f);
			}
		}

		return (T) data;
	}

}
