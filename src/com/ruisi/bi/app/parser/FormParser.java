package com.ruisi.bi.app.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ruisi.bi.app.adapter.TableAdapter.TableCell;
import com.ruisi.bi.app.adapter.TableAdapter.TableRow;
import com.ruisi.bi.app.adapter.TableAdapter.TableRowHead;
import com.ruisi.bi.app.bean.FormDataChildBean;

public class FormParser extends BaseParser{

	@Override
	public <T> T parse(String jsonStr) throws JSONException {
		ArrayList<TableRow> TableRows=null;
		if(jsonStr!=null){
			TableRows=new ArrayList<>();
			JSONObject obj=new JSONObject(jsonStr);
			JSONArray objArray=obj.getJSONArray("comps");
			for (int i = 0; i < objArray.length(); i++) {
				JSONObject objdata=objArray.getJSONObject(i);
				
					JSONArray headArray=objdata.getJSONArray("head");
//					TableRowHead trh=new TableRowHead();
					TableCell headCell=null;
					ArrayList<TableCell> tableCell01=new ArrayList<>();
					ArrayList<TableCell> tableCell02=new ArrayList<>();
					for (int k = 0; k < headArray.length(); k++) {
						JSONArray heads=headArray.getJSONArray(k);
//						ArrayList<TableCell> hangCells=new ArrayList<TableCell>();
						
						for (int l = 0; l < heads.length(); l++) {
							if(heads.get(l).toString().equals("null"))continue;
							JSONObject hang=heads.getJSONObject(l);
							if(k==0&&l==0){
								FormDataChildBean fdcb=new FormDataChildBean();
								fdcb.colSpan=hang.getInt("colSpan");
								fdcb.rowSpan=hang.getInt("rowSpan");
								fdcb.value=hang.getString("name");
								if(headArray.length()==1)
									headCell=new TableCell(fdcb, 350, 80, 0);
								else
									headCell=new TableCell(fdcb, 350, 80*headArray.length()+1, 0);
								
//								hangCells.add(headCell);
							}else if(k==0&&l!=0){
								FormDataChildBean fdcb=new FormDataChildBean();
								fdcb.colSpan=hang.getInt("colSpan");
								fdcb.rowSpan=hang.getInt("rowSpan");
								fdcb.value=hang.getString("name");
								TableCell cell=new TableCell(fdcb, 350, 80, 0);
								tableCell01.add(cell);
//								hangCells.add(cell);
							}else if(k==1){
								FormDataChildBean fdcb=new FormDataChildBean();
								fdcb.colSpan=hang.getInt("colSpan");
								fdcb.rowSpan=hang.getInt("rowSpan");
								fdcb.value=hang.getString("name");
								TableCell cell=new TableCell(fdcb, 350, 80, 0);
								tableCell02.add(cell);
							}
							
							
//							if(heads.get(l).toString().equals("null")){
//								FormDataChildBean fdcb=new FormDataChildBean();
//								fdcb.colSpan=1;
//								fdcb.rowSpan=1;
//								fdcb.value="";
//								TableCell cell=new TableCell(fdcb, 350, 80, 0);
//								hangCells.add(cell);
//							}else{
//								JSONObject hang=heads.getJSONObject(l);
//								FormDataChildBean fdcb=new FormDataChildBean();
//								fdcb.colSpan=hang.getInt("colSpan");
//								fdcb.rowSpan=hang.getInt("rowSpan");
//								fdcb.value=hang.getString("name");
//								TableCell cell=new TableCell(fdcb, 350, 80, 0);
//								hangCells.add(cell);
//							}
						}
					}
					TableRowHead thead=new TableRowHead(tableCell01,tableCell02,headCell);
					TableRows.add(thead);
					
					
					JSONArray dataArray=objdata.getJSONArray("data");
					for (int k = 0; k < dataArray.length(); k++) {
						JSONArray dataArrays=dataArray.getJSONArray(k);
						ArrayList<TableCell> hangCells=new ArrayList<TableCell>();
						for (int l = 0; l < dataArrays.length(); l++) {
							JSONObject hang=dataArrays.getJSONObject(l);
							FormDataChildBean fdcb=new FormDataChildBean();
							fdcb.colSpan=hang.getInt("colSpan");
							fdcb.type=hang.getInt("type");
							fdcb.rowSpan=hang.getInt("rowSpan");
							fdcb.fmt=hang.getString("fmt");
							fdcb.trueValue=hang.getString("trueValue");
							fdcb.value=hang.getString("value");
							TableCell cell=new TableCell(fdcb, 350, 80, 0);
							hangCells.add(cell);
						}
						TableRow t=new TableRow(hangCells,false);
						TableRows.add(t);
					}
			}
		}
		return (T) TableRows;
	}

}
