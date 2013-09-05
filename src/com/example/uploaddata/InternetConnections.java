package com.example.uploaddata;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnections {

	private static InternetConnections instance = null;

	public boolean isInternetAlive = false;

	//private static ConnectivityManager mgr;
	//private static NetworkInfo netInfo;
	private Context context;

	private InternetConnections() {

	}// End of private constructor to hide constructor call

	public static synchronized InternetConnections getInstance(Context context) {

		if (instance == null) {

			instance = new InternetConnections();
			

		}

		instance.context = context;
		// Check Net work availability
		instance.isInternetAlive = instance.isNetworkAvailable();
		return instance;
	}

	public boolean isNetworkAvailable() {
		Context context = this.context;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			// boitealerte(this.getString(R.string.alert),
			// "getSystemService rend null");
		 
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					 
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
