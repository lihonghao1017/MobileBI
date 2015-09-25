package com.ruisi.bi.app.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ruisi.bi.app.bean.UserBean;


public class UserMsg {
	private static SharedPreferences sp;
	public static void initUserMsg(Context context){
		sp=context.getSharedPreferences("UserMsg", Context.MODE_PRIVATE);
	}

	public static void saveRigester(UserBean info){
		Editor editor = sp.edit();
		editor.putString("name", info.name);
		editor.putString("pwd", info.pwd);
		editor.commit();
	}
	public static String getUserId(){
		return sp.getString("UserId", null);
	}
	public static String getToken(){
		return sp.getString("Token", null);
	}
	public static String getPwd(){
		return sp.getString("pwd", null);
	}
	public static String getName(){
		return sp.getString("name", null);
	}
}
