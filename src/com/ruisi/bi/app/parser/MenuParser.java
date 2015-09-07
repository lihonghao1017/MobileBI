package com.ruisi.bi.app.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ruisi.bi.app.bean.MenuBean;

public class MenuParser extends BaseParser{

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		JSONArray array=new JSONArray(jsonStr);
		ArrayList<MenuBean> menus=null;
		if(array.length()>0){
			menus=new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				MenuBean menuBean=new MenuBean();
				JSONObject obj=array.getJSONObject(i);
				menuBean.name=obj.getString("name");
				menuBean.note=obj.getString("note");
				menuBean.pic=obj.getString("pic");
				menuBean.uri=obj.getString("url");
				menus.add(menuBean);
			}
		}
		return (T) menus;
	}

}
