package com.ruisi.bi.app.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.ruisi.bi.app.R;
import com.ruisi.bi.app.adapter.ThemeAdapter;
import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.bean.ThemeBean;
import com.ruisi.bi.app.common.APIContext;
import com.ruisi.bi.app.net.ServerCallbackInterface;
import com.ruisi.bi.app.net.ServerEngine;
import com.ruisi.bi.app.net.ServerErrorMessage;
import com.ruisi.bi.app.parser.ThemeParser;

public class ThemeFragment extends Fragment implements ServerCallbackInterface {
	private ExpandableListView ThemeFragment_listView;
	private ArrayList<ThemeBean> themes;
	private ThemeAdapter themeAdapter;
	private String themeUUID;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		((ProgressBar) activity.findViewById(R.id.progress_bar)).setProgress(0);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.thiem_fragment_layout, null);
		ThemeFragment_listView = (ExpandableListView) v
				.findViewById(R.id.ThemeFragment_listView);
		themes = new ArrayList<ThemeBean>();
		themeAdapter = new ThemeAdapter(getActivity(), themes);
		ThemeFragment_listView.setAdapter(themeAdapter);
		sendRequest();
		return v;
	}

	private void sendRequest() {
		ServerEngine serverEngine = new ServerEngine(this);
		RequestVo rv = new RequestVo();
		rv.context = this.getActivity();
		rv.functionPath = APIContext.Theme;
		// rv.modulePath = APIContext.Login;
		rv.parser = new ThemeParser();
		rv.Type = APIContext.GET;
		themeUUID = UUID.randomUUID().toString();
		rv.uuId = themeUUID;
		rv.isSaveToLocation = false;
		// HashMap<String, String> map = new HashMap<String, String>();
		// map.put("userName", "admin");
		// map.put("password", "123456");
		// rv.requestDataMap = map;
		serverEngine.addTaskWithConnection(rv);
	}

	@Override
	public <T> void succeedReceiveData(T object, String uuid) {
		if (themeUUID.equals(uuid)) {
			themes.clear();
			themes.addAll((Collection<? extends ThemeBean>) object);
			themeAdapter.notifyDataSetChanged();
			for (int i = 0; i < themes.size(); i++) {
				ThemeFragment_listView.expandGroup(i);
			}
		}
	}

	@Override
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid) {

	}
}
