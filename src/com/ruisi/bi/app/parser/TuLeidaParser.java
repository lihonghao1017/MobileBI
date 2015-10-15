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
import com.ruisi.bi.app.bean.WeiduBean;
import com.ruisi.bi.app.bean.WeiduOptionBean;

public class TuLeidaParser extends BaseParser {

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		RadarData data = null;
		 ArrayList<Object> dataR=null;
		if (jsonStr != null) {
			dataR=new ArrayList<>();
			JSONObject obj = new JSONObject(jsonStr);
			ArrayList<WeiduBean> options = new ArrayList<>();
			JSONArray paramsArray = obj.optJSONArray("params");
			if(paramsArray!=null)
			for (int i = 0; i < paramsArray.length(); i++) {
				JSONObject paramsObj = paramsArray.getJSONObject(i);
				WeiduBean weiduBean = new WeiduBean();
				if (paramsObj.getString("name").toString().equals("null"))
					weiduBean.name = "";
				else
					weiduBean.name = paramsObj.getString("name");
				weiduBean.type = paramsObj.getString("type");
				weiduBean.value = paramsObj.getString("value");
				ArrayList<WeiduOptionBean> optionList=new ArrayList<>();
				JSONArray optionArray = paramsObj.getJSONArray("options");
				for (int j = 0; j < optionArray.length(); j++) {
					JSONObject optionObj = optionArray.getJSONObject(j);
					WeiduOptionBean optionBean=new WeiduOptionBean();
					optionBean.text=optionObj.getString("text");
					optionBean.value=optionObj.getString("value");
					optionList.add(optionBean);
				}
				weiduBean.options = optionList;
				options.add(weiduBean);
			}
			dataR.add(options);
			
			JSONArray objArray = obj.getJSONArray("comps");
			for (int i = 0; i < objArray.length(); i++) {
				JSONObject objdata = objArray.getJSONObject(i);
				JSONArray headArray = objdata.getJSONArray("yVals");
				ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();
				for (int j = 0; j < headArray.length(); j++) {
					if (j>5)break;
					JSONObject itemData = headArray.getJSONObject(j);
					JSONArray itemArray = itemData.getJSONArray("Entry");
					ArrayList<Entry> yVals2 = new ArrayList<Entry>();
					for (int k = 0; k < itemArray.length(); k++) {
						if (k>5)break;
						JSONObject entryData = itemArray.getJSONObject(k);
						yVals2.add(new Entry(entryData.getInt("value"),
								entryData.getInt("xIndex")));
					}
					RadarDataSet set1 = new RadarDataSet(yVals2,
							itemData.getString("label"));
					set1.setColor(ColorTemplate.VORDIPLOM_COLORS[j]);
					set1.setDrawFilled(true);
					set1.setLineWidth(2f);
					dataSets.add(set1);
				}
				JSONArray xValsArray = objdata.getJSONArray("xVals");
				ArrayList<String> xVals = new ArrayList<String>();
				for (int j = 0; j < xValsArray.length(); j++) {
					if (j>5)break;
					xVals.add(xValsArray.getString(j));
				}
				data = new RadarData(xVals, dataSets);
				data.setValueTextColor(Color.WHITE);
				data.setValueTextSize(9f);
			}
			dataR.add(data);
		}

		return (T) dataR;
	}

}
