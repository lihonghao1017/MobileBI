package com.ruisi.bi.app.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ruisi.bi.app.adapter.TableAdapter.TableCell;
import com.ruisi.bi.app.adapter.TableAdapter.TableRow;
import com.ruisi.bi.app.adapter.TableAdapter.TableRowHead;
import com.ruisi.bi.app.bean.FormDataChildBean;
import com.ruisi.bi.app.bean.QuxianBean;
import com.ruisi.bi.app.bean.YVals;
import com.ruisi.bi.app.bean.YValsEntry;

public class TuParser extends BaseParser {

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		 LineData data = null;
		if (jsonStr != null) {
			JSONObject obj = new JSONObject(jsonStr);
			JSONArray objArray = obj.getJSONArray("comps");
			for (int i = 0; i < objArray.length(); i++) {
				JSONObject objdata = objArray.getJSONObject(i);
				JSONArray headArray = objdata.getJSONArray("yVals");
				ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
				for (int j = 0; j < headArray.length(); j++) {
					JSONObject itemData = headArray.getJSONObject(j);
					JSONArray itemArray = itemData.getJSONArray("Entry");
					ArrayList<Entry> yVals2 = new ArrayList<Entry>();
					for (int k = 0; k < itemArray.length(); k++) {
						JSONObject entryData = itemArray.getJSONObject(k);
						yVals2.add(new Entry(entryData.getInt("value"), entryData.getInt("xIndex")));
					}
					 LineDataSet set2 = new LineDataSet(yVals2, itemData.getString("label"));
				        set2.setAxisDependency(AxisDependency.RIGHT);
				        set2.setColor(Color.RED);
				        set2.setCircleColor(Color.WHITE);
				        set2.setLineWidth(2f);
				        set2.setCircleSize(3f);
				        set2.setFillAlpha(65);
				        set2.setFillColor(Color.RED);
				        set2.setDrawCircleHole(false);
				        set2.setHighLightColor(Color.rgb(244, 117, 117));
				        dataSets.add(set2);
				}
				JSONArray xValsArray = objdata.getJSONArray("xVals");
				ArrayList<String> xVals = new ArrayList<String>();
				for (int j = 0; j < xValsArray.length(); j++) {
					xVals.add(xValsArray.getString(j));
				}
				data = new LineData(xVals, dataSets);
				data.setValueTextColor(Color.WHITE);
				data.setValueTextSize(9f);
			}
		}

		return (T) data;
	}

}
