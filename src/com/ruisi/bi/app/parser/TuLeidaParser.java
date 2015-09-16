package com.ruisi.bi.app.parser;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Color;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class TuLeidaParser extends BaseParser {

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		RadarData data = null;
		if (jsonStr != null) {
			JSONObject obj = new JSONObject(jsonStr);
			JSONArray objArray = obj.getJSONArray("comps");
			for (int i = 0; i < objArray.length(); i++) {
				JSONObject objdata = objArray.getJSONObject(i);
				JSONArray headArray = objdata.getJSONArray("yVals");
				ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();
				for (int j = 0; j < headArray.length(); j++) {
					JSONObject itemData = headArray.getJSONObject(j);
					JSONArray itemArray = itemData.getJSONArray("Entry");
					ArrayList<Entry> yVals2 = new ArrayList<Entry>();
					for (int k = 0; k < itemArray.length(); k++) {
						JSONObject entryData = itemArray.getJSONObject(k);
						yVals2.add(new Entry(entryData.getInt("value"),
								entryData.getInt("xIndex")));
					}
					RadarDataSet set1 = new RadarDataSet(yVals2,
							itemData.getString("label"));
					set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
					set1.setDrawFilled(true);
					set1.setLineWidth(2f);
					dataSets.add(set1);
				}
				JSONArray xValsArray = objdata.getJSONArray("xVals");
				ArrayList<String> xVals = new ArrayList<String>();
				for (int j = 0; j < xValsArray.length(); j++) {
					xVals.add(xValsArray.getString(j));
				}
				data = new RadarData(xVals, dataSets);
				data.setValueTextColor(Color.WHITE);
				data.setValueTextSize(9f);
			}
		}

		return (T) data;
	}

}
