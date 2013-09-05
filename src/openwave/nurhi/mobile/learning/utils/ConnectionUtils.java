package openwave.nurhi.mobile.learning.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {

	public final static String TAG = ConnectionUtils.class.getSimpleName();
	
	public static boolean isOnWifi(Context ctx){
		ConnectivityManager conMan = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMan.getActiveNetworkInfo();
		if (netInfo == null || netInfo.getType() != ConnectivityManager.TYPE_WIFI) {
			return false;
		} else {
			return true;
		}
	}
}
