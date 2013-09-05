package openwave.nurhi.mobile.learning.utils;

import java.util.LinkedList;
import java.util.List;

import openwave.nurhi.mobile.learning.application.MobileLearning;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import openwave.nurhi.mobile.learning.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

public class HTTPConnectionUtils extends DefaultHttpClient {
	
	private HttpParams httpParameters;
	
	public HTTPConnectionUtils(Context ctx){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(
				httpParameters,
				Integer.parseInt(prefs.getString(ctx.getString(R.string.prefs_server_timeout_connection),
						ctx.getString(R.string.prefServerTimeoutConnection))));
		HttpConnectionParams.setSoTimeout(
				httpParameters,
				Integer.parseInt(prefs.getString(ctx.getString(R.string.prefs_server_timeout_response),
						ctx.getString(R.string.prefServerTimeoutResponseDefault))));
		// add user agent 
        
		String v = "0";
		try {
			v = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        
		super.setParams(httpParameters);
		super.getParams().setParameter(CoreProtocolPNames.USER_AGENT, MobileLearning.USER_AGENT + v);
	}
	
	public static String createUrlWithCredentials(Context ctx, SharedPreferences prefs, String baseUrl, boolean addServer){
		if(addServer){
			baseUrl = prefs.getString(ctx.getString(R.string.prefs_server), ctx.getString(R.string.prefServerDefault)) + baseUrl;
		}
		List<NameValuePair> pairs = new LinkedList<NameValuePair>();
		pairs.add(new BasicNameValuePair("username", prefs.getString(ctx.getString(R.string.prefs_username), "")));
		pairs.add(new BasicNameValuePair("api_key", prefs.getString(ctx.getString(R.string.prefs_api_key), "")));
		pairs.add(new BasicNameValuePair("format", "json"));
		String paramString = URLEncodedUtils.format(pairs, "utf-8");
		if(!baseUrl.endsWith("?"))
			baseUrl += "?";
		baseUrl += paramString;
		return baseUrl;
	}

}
