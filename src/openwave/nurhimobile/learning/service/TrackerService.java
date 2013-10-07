/* 
 * This file is part of OppiaMobile - http://oppia-mobile.org/
 * 
 * OppiaMobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OppiaMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OppiaMobile. If not, see <http://www.gnu.org/licenses/>.
 */

package openwave.nurhimobile.learning.service;

import openwave.nurhi.mobile.learning.activity.DownloadActivity;
import openwave.nurhi.mobile.learning.application.DbHelper;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.listener.APIRequestListener;
import openwave.nurhi.mobile.learning.task.APIRequestTask;
import openwave.nurhi.mobile.learning.task.Payload;
import openwave.nurhi.mobile.learning.task.SubmitMQuizTask;
import openwave.nurhi.mobile.learning.task.SubmitTrackerMultipleTask;
import openwave.nurhi.mobile.learning.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;

public class TrackerService extends Service implements APIRequestListener{

	public static final String TAG = TrackerService.class.getSimpleName();

	private final IBinder mBinder = new MyBinder();
	private SharedPreferences prefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
		BugSenseHandler.initAndStartSession(this,MobileLearning.BUGSENSE_API_KEY);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean backgroundData = true;
		Bundle b = intent.getExtras();
		if (b != null) {
			backgroundData = b.getBoolean("backgroundData");
		}

		if (isOnline() && backgroundData) {
			DbHelper db = new DbHelper(this);
			Payload p = null;
			
			// check for updated modules
			// should only do this once a day or so....
			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			long lastRun = prefs.getLong("lastModuleUpdateCheck", 0);
			long now = System.currentTimeMillis()/1000;
			if((lastRun + (150*12)) < now){   //3600   change 150
				APIRequestTask task = new APIRequestTask(this);
				p = new Payload(MobileLearning.SERVER_COURSES_PATH);
				task.setAPIRequestListener(this);
				task.execute(p);
				
				Editor editor = prefs.edit();
				editor.putLong("lastModuleUpdateCheck", now);
				editor.commit();
			}

			// send activity trackers
			MobileLearning app = (MobileLearning) this.getApplication();
			if(app.omSubmitTrackerMultipleTask == null){
				app.omSubmitTrackerMultipleTask = new SubmitTrackerMultipleTask(this);
				app.omSubmitTrackerMultipleTask.execute();
			}
			
			// send quiz results
			Payload mqp = db.getUnsentMquiz();
			SubmitMQuizTask smqt = new SubmitMQuizTask(this);
			smqt.execute(mqp);

			db.close();

		}
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class MyBinder extends Binder {
		public TrackerService getService() {
			return TrackerService.this;
		}
	}

	public boolean isOnline() {
		getApplicationContext();
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public void apiRequestComplete(Payload response) {
		DbHelper db = new DbHelper(this);
		Log.d(TAG,"completed getting module list");
	
		boolean updateAvailable = false;
		try {
			JSONObject json = new JSONObject(response.getResultResponse());
			for (int i = 0; i < (json.getJSONArray("modules").length()); i++) {
				JSONObject json_obj = (JSONObject) json.getJSONArray("modules").get(i);
				String shortName = json_obj.getString("shortname");
				Double version = json_obj.getDouble("version");
				if(db.toUpdate(shortName,version)){
					updateAvailable = true;
				}
				if(json_obj.has("schedule")){
					Double scheduleVersion = json_obj.getDouble("schedule");
					if(db.toUpdateSchedule(shortName, scheduleVersion)){
						updateAvailable = true;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		db.close();
		
		if(updateAvailable){
			Bitmap icon = BitmapFactory.decodeResource(getResources(),
	                R.drawable.dc_logo);
			NotificationCompat.Builder mBuilder =
				    new NotificationCompat.Builder(this)
				    .setSmallIcon(R.drawable.ic_stat_notification)
				    .setLargeIcon(icon)
				    .setContentTitle(getString(R.string.notification_module_update_title))
				    .setContentText(getString(R.string.notification_module_update_text));
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Intent resultIntent = new Intent(this, DownloadActivity.class);
			PendingIntent resultPendingIntent =
				    PendingIntent.getActivity(
				    this,
				    0,
				    resultIntent,
				    PendingIntent.FLAG_UPDATE_CURRENT
				);
			mBuilder.setContentIntent(resultPendingIntent);
			int mId = 001;
			notificationManager.notify(mId, mBuilder.getNotification());
		}
	}

}
