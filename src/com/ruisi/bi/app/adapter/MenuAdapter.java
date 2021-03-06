package com.ruisi.bi.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ruisi.bi.app.R;
import com.ruisi.bi.app.bean.MenuBean;
import com.ruisi.bi.app.common.APIContext;

public class MenuAdapter extends BaseAdapter{
	private ArrayList<MenuBean> datas;
	private LayoutInflater inflater;
	public MenuAdapter(Context context,ArrayList<MenuBean> datas) {
		this.datas=datas;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v=inflater.inflate(R.layout.activity_main_item, null);
		ImageView icon=(ImageView) v.findViewById(R.id.MainActivity_icon);
		TextView name=(TextView) v.findViewById(R.id.MainActivity_name);
		TextView note=(TextView) v.findViewById(R.id.MainActivity_note);
		name.setText(datas.get(arg0).name);
		note.setText(datas.get(arg0).note);
		ImageLoader.getInstance().displayImage(APIContext.HOST+datas.get(arg0).pic,icon);
		return v;
	}

}
