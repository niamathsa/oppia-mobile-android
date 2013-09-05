package com.example.uploaddata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import openwave.nurhi.mobile.learning.activity.ScoreActivity;
import openwave.nurhi.mobile.learning.activity.WriteFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

 

import android.util.Log;

public class WebParser {

	public ArrayList<String> get_Department_Detail(String url, String base64,
			String md5) {

		ArrayList<String> Department_data = new ArrayList<String>();
		Department_data.add("hello");
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url + "dothis=" + base64
					+ "&andthis=" + md5);
			System.out.println("***Total value is ***" + url + "dothis="
					+ base64 + "&andthis=" + md5);
			httppost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			// HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// // time out
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity httpEntity = response.getEntity();
			String json = EntityUtils.toString(httpEntity);
			Log.d("My Responce is user name is created",
					"**" + json.toString());
			ArrayList<String> list = new ArrayList<String>();

//			Department_data = call_Department_Parsing(json, list);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Department_data;
	}
	
	
	
	
	
	public ArrayList<String> put_Favorite(String appUrl, String base64,
			String md5) {

		ArrayList<String> staffFavs = new ArrayList<String>();
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(appUrl + "dothis=" + base64
					+ "&andthis=" + md5);
			System.out.println("***Total value is ***" + appUrl + "dothis="
					+ base64 + "&andthis=" + md5);
			httppost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity httpEntity = response.getEntity();
			String json = EntityUtils.toString(httpEntity);
			
			System.out.println("Data is printed****"+ json.toString());		
			

			JSONObject jsonv = new JSONObject(json);
			staffFavs.add(jsonv.getString("error"));

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return staffFavs;
	
	}
	
	
	
	public ArrayList<String> hitpathtoappend(String appUrl, String base64,
			String md5) {

		ArrayList<String> staffFavs = new ArrayList<String>();
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(appUrl + "dothis=" + base64
					+ "&andthis=" + md5);
			System.out.println("***Total value is ***" + appUrl + "dothis="
					+ base64 + "&andthis=" + md5);
			httppost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity httpEntity = response.getEntity();
			String json = EntityUtils.toString(httpEntity);
			
			System.out.println("Data uploaded into append file****"+ json.toString());		
			

//			if(true==true){
			
				File f = new File("/mnt/sdcard//NigerianProjectFile/documents/file_gunase.txt");
				f.delete();
//			}
			
			
			JSONObject jsonv = new JSONObject(json);
			staffFavs.add(jsonv.getString("error"));

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return staffFavs;
	
	}
	
	
	
	
	
	
	
//
//	public ArrayList<String> call_Department_Parsing(String jsonS,
//			ArrayList<String> val) {
//		ArrayList<String> depatvalues = new ArrayList<String>();
//
//		try {
//			JSONObject jsonv = new JSONObject(jsonS);
//
//			for (int i = 0; i < jsonv.length() - 1; i++) { // 25 items
//				JSONObject c = jsonv.getJSONObject("" + i);
//
//				// if(!c.getString("name").equalsIgnoreCase("Bill Rogers")){
//
//				depatvalues.add(c.getString("name")); // 0
//				depatvalues.add(c.getString("id"));
//				depatvalues.add(c.getString("image_url"));
//				depatvalues.add(c.getString("modified"));
//
//				depatvalues.add(c.getString("hospital_id"));
//				depatvalues.add(c.getString("hotspot_type_id"));
//				depatvalues.add(c.getString("hotspot_map"));
//				depatvalues.add(c.getString("hospital_building_id")); // 7
//				try {
//					String s = c.getString("floor_layer"); // changed
//															// floor_type_id
//					int d = Integer.parseInt(s); // -1
//					depatvalues.add(d + ""); // 8
//				} catch (Exception e) {
//					depatvalues.add("1");
//				}
//				depatvalues.add(c.getString("website_url")); // 9
//				depatvalues.add(c.getString("slug"));
//				depatvalues.add(c.getString("phone_no"));
//
//				depatvalues.add(c.getString("email"));
//				depatvalues.add(c.getString("video_link1"));
//				depatvalues.add(c.getString("video_link2"));
//				depatvalues.add(c.getString("other_references"));
//
//				depatvalues.add(c.getString("additional_info"));
//				depatvalues.add(c.getString("latitude"));
//				depatvalues.add(c.getString("longitude"));
//				depatvalues.add(c.getString("altitude"));
//
//				depatvalues.add(c.getString("adjacent_to"));
//				depatvalues.add(c.getString("opposite_to"));
//				depatvalues.add(c.getString("near_by"));
//				depatvalues.add(c.getString("description"));
//				// 23
//				depatvalues.add(c.getString("is_active"));
//
//				// System.out.println("*****Department Name****"
//				// + c.getString("name"));
//			}
//			// }
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//	}

}
