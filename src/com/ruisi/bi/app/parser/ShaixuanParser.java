package com.ruisi.bi.app.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ruisi.bi.app.bean.WeiduOptionBean;

public class ShaixuanParser extends BaseParser {

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		ArrayList<WeiduOptionBean> options=null;
		if (jsonStr != null) {
			JSONObject obj = new JSONObject(jsonStr);
			options=new ArrayList<>();
			JSONArray array=obj.getJSONArray("options");
			for (int i = 0; i < array.length(); i++) {
				JSONObject option =array.getJSONObject(i);
				WeiduOptionBean bean=new WeiduOptionBean();
				bean.text=option.getString("name");
				bean.value=option.getString("id");
				options.add(bean);
			}
		}

		return (T) options;
	}

}
