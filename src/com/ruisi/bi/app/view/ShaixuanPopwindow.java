package com.ruisi.bi.app.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruisi.bi.app.ConditionAcitivity;
import com.ruisi.bi.app.R;
import com.ruisi.bi.app.bean.WeiduOptionBean;

public class ShaixuanPopwindow {
	public static PopupWindow getShaixuanPopwindow(final Activity context, final ArrayList<WeiduOptionBean> options,View vs,String head) {
		View mView = LayoutInflater.from(context).inflate(
				R.layout.select_popwindow_layout, null);
		
		((TextView) mView.findViewById(R.id.MyPopwindow_title)).setText(head);
		ListView lv = (ListView) mView.findViewById(R.id.MyPopwindow_listview);
		lv.setAdapter(new ShaixuanAdapter(context,options));
		final PopupWindow menuWindow = new PopupWindow(mView,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		menuWindow.setOutsideTouchable(true);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new ColorDrawable(-00000));
		menuWindow.setTouchable(true);
		menuWindow.setContentView(mView);
//		int[] location = new int[2];
//		vs.getLocationOnScreen(location);
		menuWindow.showAtLocation(vs, Gravity.TOP | Gravity.LEFT, 0, 0);
		mView.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
					}
				});

		mView.findViewById(R.id.commit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						StringBuffer value=new StringBuffer();
						for (int i = 0; i < options.size(); i++) {
							WeiduOptionBean bean=options.get(i);
							if (bean.isChecked) {
								value.append(bean.value);
								value.append(",");
							}
						}
						if (value.length()>0) {
							value.delete(value.length()-1, value.length());
						}
						((ConditionAcitivity)context).setShaixuan(value.toString());
						menuWindow.dismiss();
					}
				});
		mView.findViewById(R.id.MyPopwindow_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
					}
				});

		return menuWindow;
	}
	static class ShaixuanAdapter extends BaseAdapter {
		private ArrayList<WeiduOptionBean> Options;
		private LayoutInflater inflater;

		public ShaixuanAdapter(Context context, ArrayList<WeiduOptionBean> Options) {
			this.inflater = LayoutInflater.from(context);
			this.Options = Options;
		}

		@Override
		public int getCount() {
			return Options.size();
		}

		@Override
		public Object getItem(int position) {
			return Options.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.theme_item, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
				holder.cb = (CheckBox) convertView
						.findViewById(R.id.cb_contacts);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_contacts_item_name);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final WeiduOptionBean weiduBean=Options.get(position);
			holder.tv_name.setText(weiduBean.text);
			holder.cb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					setChecked(position);
				}
			});
			holder.tv_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					setChecked(position);
				}
			});
			holder.cb.setChecked(weiduBean.isChecked);
			return convertView;
		}
		private void setChecked(int position){
			Options.get(position).isChecked=!Options.get(position).isChecked;
			notifyDataSetChanged();
		}

		private final class ViewHolder {
			TextView tv_name;
			CheckBox cb;
		}
	}
}
