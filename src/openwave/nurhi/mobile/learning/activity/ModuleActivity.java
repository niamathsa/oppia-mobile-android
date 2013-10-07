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

package openwave.nurhi.mobile.learning.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Callable;

import openwave.nurhi.mobile.learning.adapter.SectionListAdapter;
import openwave.nurhi.mobile.learning.application.DbHelper;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.application.Tracker;
import openwave.nurhi.mobile.learning.model.Lang;
import openwave.nurhi.mobile.learning.model.Module;
import openwave.nurhi.mobile.learning.model.Section;
import openwave.nurhi.mobile.learning.utils.UIUtils;
import openwave.nurhi.mobile.learning.widgets.MQuizWidget;
import openwave.nurhi.mobile.learning.widgets.PageWidget;
import openwave.nurhi.mobile.learning.widgets.WidgetFactory;
import openwave.nurhimobile.learning.service.TrackerService;

import openwave.nurhi.mobile.learning.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.digitalcampus.mquiz.MQuiz;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.uploaddata.BaseUtill;
import com.example.uploaddata.InternetConnections;
import com.example.uploaddata.WebParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ModuleActivity extends AppActivity implements OnUtteranceCompletedListener, OnInitListener {

	public static final String TAG = ModuleActivity.class.getSimpleName();
	private Section section;
	private Module module;
	private int currentActivityNo = 0;
	private WidgetFactory currentActivity;
	private SharedPreferences prefs;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector pageGestureDetector;
	View.OnTouchListener pageGestureListener;
	
	private GestureDetector quizGestureDetector;
	View.OnTouchListener quizGestureListener;

	private static int TTS_CHECK = 0;
	static TextToSpeech myTTS;
	private boolean ttsRunning = false;

	private HashMap<String, Object> mediaPlayingState = new HashMap<String, Object>();
	private MQuiz mQuiz;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_module);
		this.drawHeader();
	 
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			section = (Section) bundle.getSerializable(Section.TAG);
			module = (Module) bundle.getSerializable(Module.TAG);
			currentActivityNo = (Integer) bundle.getSerializable(SectionListAdapter.TAG_PLACEHOLDER);
		}

		// Gesture detection for pages
		pageGestureDetector = new GestureDetector(new PageGestureDetector());
		pageGestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return pageGestureDetector.onTouchEvent(event);
			}
		};

		
		Button back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// Gesture detection for quizzes
		quizGestureDetector = new GestureDetector(new QuizGestureDetector());
		quizGestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return quizGestureDetector.onTouchEvent(event);
			}
		};
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putLong("activityStartTimeStamp", currentActivity.getStartTime());
		savedInstanceState.putBoolean("mediaPlaying", currentActivity.getMediaPlaying());
		savedInstanceState.putLong("mediaStartTimeStamp", currentActivity.getMediaStartTime());
		savedInstanceState.putString("mediaFileName", currentActivity.getMediaFileName());
		savedInstanceState.putInt("currentActivityNo", this.currentActivityNo);
		savedInstanceState.putSerializable("mquiz", currentActivity.getMQuiz());
		Log.d(TAG,"saved instance state");
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentActivity.setMediaPlaying(savedInstanceState.getBoolean("mediaPlaying"));
		currentActivity.setMediaStartTime(savedInstanceState.getLong("mediaStartTimeStamp"));
		currentActivity.setMediaFileName(savedInstanceState.getString("mediaFileName"));
		currentActivity.setStartTime(savedInstanceState.getLong("activityStartTimeStamp"));
		this.currentActivityNo = savedInstanceState.getInt("currentActivityNo");
		this.mQuiz = (MQuiz) savedInstanceState.getSerializable("mquiz");
		Log.d(TAG,"restored instance state");
	}

	@Override
	public void onStart() {
		super.onStart();
		setTitle(section.getTitle(prefs
				.getString(getString(R.string.prefs_language), Locale.getDefault().getLanguage())));
		loadActivity();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (myTTS != null) {
			myTTS.shutdown();
		}

		ArrayList<openwave.nurhi.mobile.learning.model.Activity> acts = section.getActivities();
		this.saveTracker(acts.get(this.currentActivityNo).getDigest());

		// start a new tracker service
		Log.d(TAG, "Starting tracker service");
		Intent service = new Intent(this, TrackerService.class);

		Bundle tb = new Bundle();
		tb.putBoolean("backgroundData", true);
		service.putExtras(tb);

		if (currentActivity != null) {
			mediaPlayingState.put("Media_Playing", currentActivity.getMediaPlaying());
			mediaPlayingState.put("Media_StartTime", currentActivity.getMediaStartTime());
			mediaPlayingState.put("Media_File", currentActivity.getMediaFileName());
		}
		this.startService(service);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (currentActivity != null) {
			if (mediaPlayingState.containsKey("Media_Playing")) {
				currentActivity.setMediaPlaying((Boolean) mediaPlayingState.get("Media_Playing"));
			}
			if (mediaPlayingState.containsKey("Media_StartTime")) {
				currentActivity.setMediaStartTime((Long) mediaPlayingState.get("Media_StartTime"));
			}
			if (mediaPlayingState.containsKey("Media_File")) {
				currentActivity.setMediaFileName((String) mediaPlayingState.get("Media_File"));
			}
			currentActivity.mediaStopped();
		}
		loadActivity();
	}

	@Override
	protected void onDestroy() {
		if (myTTS != null) {
			myTTS.shutdown();
			myTTS = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_module, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = (MenuItem) menu.findItem(R.id.menu_tts);
		if (ttsRunning) {
			item.setTitle(R.string.menu_stop_read_aloud);
		} else {
			item.setTitle(R.string.menu_read_aloud);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
//		case R.id.menu_about:
//			 new trackertask().execute();
//			return true;
//			
			
			
			
//		case R.id.menu_settings:
//			Intent i = new Intent(this, PrefsActivity.class);
//			Bundle tb = new Bundle();
//			ArrayList<Lang> langs = new ArrayList<Lang>();
//			for (Module m : modules) {
//				langs.addAll(m.getLangs());
//			}
//			tb.putSerializable("langs", langs);
//			i.putExtras(tb);
//			startActivity(i);
//			return true;
		
		case R.id.menu_download:
			startActivity(new Intent(this, TagSelectActivity.class));
			return true;
		case R.id.menu_language:
			// createLanguageDialog();
			createLibrary();
			return true;
//		case R.id.menu_help:
//			startActivity(new Intent(this, HelpActivity.class));
//			return true;
//		case R.id.menu_logout:
//			 
//			logout();
//			return true;			
			
		case R.id.menu_help:
			startActivity(new Intent(this, HelpActivity.class));
			return true;
		case R.id.menu_tts:
			if (myTTS == null && !ttsRunning) {
				// check for TTS data
				Intent checkTTSIntent = new Intent();
				checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
				startActivityForResult(checkTTSIntent, TTS_CHECK);
			} else if (myTTS != null && ttsRunning){
				this.stopReading();
			} else {
				// TTS not installed so show message
				Toast.makeText(this, this.getString(R.string.error_tts_start), Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void loadActivity() {
		ArrayList<openwave.nurhi.mobile.learning.model.Activity> acts = section.getActivities();
		TextView tb = (TextView) this.findViewById(R.id.module_activity_title);

		tb.setText(acts.get(this.currentActivityNo).getTitle(
				prefs.getString(getString(R.string.prefs_language), Locale.getDefault().getLanguage())));

		if (acts.get(this.currentActivityNo).getActType().equals("page")) {
			currentActivity = new PageWidget(ModuleActivity.this, module, acts.get(this.currentActivityNo));
			WebView wv = (WebView) this.findViewById(R.id.page_webview);
			wv.setOnTouchListener(pageGestureListener);
		}
		if (acts.get(this.currentActivityNo).getActType().equals("quiz")) {
			if(mQuiz != null){
				currentActivity = new MQuizWidget(ModuleActivity.this, module, acts.get(this.currentActivityNo), mQuiz);
				Log.d(TAG,"Sending mquiz object");
				//currentActivity.setMQuiz(mQuiz);
			} else {
				currentActivity = new MQuizWidget(ModuleActivity.this, module, acts.get(this.currentActivityNo));
			}
			ScrollView sv = (ScrollView) this.findViewById(R.id.quizScrollView);
			sv.setOnTouchListener(quizGestureListener);
		}
		this.setUpNav();
	}

	private void setUpNav() {
		Button prevB = (Button) ModuleActivity.this.findViewById(R.id.prev_btn);
		Button nextB = (Button) ModuleActivity.this.findViewById(R.id.next_btn);
		prevB.setVisibility(View.GONE);
		nextB.setVisibility(View.GONE);
//		if (this.hasPrev()) {
//			prevB.setVisibility(View.VISIBLE);
//			prevB.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					movePrev();
//				}
//			});
//		} else {
//			prevB.setVisibility(View.INVISIBLE);
//		}
//
//		if (this.hasNext()) {
//			nextB.setVisibility(View.VISIBLE);
//			nextB.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					moveNext();
//				}
//			});
//		} else {
//			nextB.setVisibility(View.INVISIBLE);
//		}
	}

	private boolean hasPrev() {
		if (this.currentActivityNo == 0) {
			return false;
		}
		return true;
	}

	private boolean hasNext() {
		int noActs = section.getActivities().size();
		if (this.currentActivityNo + 1 == noActs) {
			return false;
		} else {
			return true;
		}
	}

	private void moveNext() {
		this.stopReading();
		ArrayList<openwave.nurhi.mobile.learning.model.Activity> acts = section.getActivities();
		this.saveTracker(acts.get(currentActivityNo).getDigest());
		currentActivityNo++;
		loadActivity();
	}

	private void movePrev() {
		this.stopReading();
		ArrayList<openwave.nurhi.mobile.learning.model.Activity> acts = section.getActivities();
		this.saveTracker(acts.get(currentActivityNo).getDigest());
		currentActivityNo--;
		loadActivity();
	}

	private boolean saveTracker(String digest) {
		if (currentActivity != null && currentActivity.activityHasTracker()) {
			Tracker t = new Tracker(this);
			JSONObject json = currentActivity.getTrackerData();
			t.saveTracker(module.getModId(), digest, json, currentActivity.activityCompleted());
		}
		return true;
	}

	private void createLanguageDialog() {
//		UIUtils ui = new UIUtils();
//		ui.createLanguageDialog(this, module.getLangs(), prefs, new Callable<Boolean>() {
//			public Boolean call() throws Exception {
//				ModuleActivity.this.onStart();
//				return true;
//			}
//		});
		
		Intent i = new Intent(ModuleActivity.this,
				LibraryActivity.class);
		startActivity(i);
		
		
	}
	
	
	private void logout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle(R.string.logout);
		builder.setMessage(R.string.logout_confirm);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// wipe activity data
						DbHelper db = new DbHelper(ModuleActivity.this);
						db.onLogout();
						db.close();

						// wipe user prefs
						Editor editor = prefs.edit();
						editor.putString(getString(R.string.prefs_username), "");
						editor.putString(getString(R.string.prefs_api_key), "");
						editor.putInt(getString(R.string.prefs_badges), 0);
						editor.putInt(getString(R.string.prefs_points), 0);
						editor.commit();

						// restart this activity
//						ModuleActivity.this.onStart();
						startActivity(new Intent(ModuleActivity.this, LoginActivity.class)); 
					
						finish();
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return; // do nothing
					}
				});
		builder.show();
	}
	
	public void createLibrary() {

		Intent i = new Intent(ModuleActivity.this,
				LibraryActivity.class);
		startActivity(i);
	}

	class PageGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (ModuleActivity.this.hasNext()) {
						ModuleActivity.this.moveNext();
					}
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (ModuleActivity.this.hasPrev()) {
						ModuleActivity.this.movePrev();
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

	}

	
	class trackertask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

//			WebParser web = new WebParser();
//
//			BaseUtill bs = new BaseUtill();
//			String base64 = bs.get_Convert_Base64(adduserbase64);
//			String md5 = bs.get_Convert_MD5key(addusermd5);

			if ((InternetConnections.getInstance(ModuleActivity.this).isInternetAlive) == true) {

				try {
					
//					web.get_Department_Detail(appurl, base64, md5);
					

					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> Nurhi app Tracker server");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate -->  time of login user detail");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> Module install time and access time ");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> time of event access video access ");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> hardware info into server");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> battery into server");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> android info");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> Mobile network info");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> running application");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> installed application");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> simple location");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> continuos location");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> nearby wifi devices");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> nearby bluetooth devices");
					new WriteFile(ModuleActivity.this)
							.CreateFile("yet to activate --> the file is uploading into server!!");

					
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModuleActivity.this);
					String usernames = prefs.getString(ModuleActivity.this.getString(R.string.prefs_username), "");
					System.out.println("Main here need to print username////"+ usernames);
					upload("file_"+usernames+".txt");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return null;
		}
	}
	
	
	public void upload(String s) throws Exception {
		// Url of the server
		String url = "http://220.226.2.31/nigerian/nigerian/api/uploadtest1.php";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		MultipartEntity mpEntity = new MultipartEntity();
		// Path of the file to be uploaded
		String filepath = "/mnt/sdcard//NigerianProjectFile/documents/"+s;
		File file = new File(filepath);
		ContentBody cbFile = new FileBody(file, "image/jpeg");

		// Add the data to the multipart entity
		mpEntity.addPart("uploadedfile", cbFile);
		// mpEntity.addPart("name", new StringBody("Test",
		// Charset.forName("UTF-8")));
		// mpEntity.addPart("data", new StringBody("This is test report",
		// Charset.forName("UTF-8")));
		post.setEntity(mpEntity);
		// Execute the post request
		HttpResponse response1 = client.execute(post);
		// Get the response from the server
		HttpEntity resEntity = response1.getEntity();
		String Response = EntityUtils.toString(resEntity);
		Log.d("Response:", Response);
		// Generate the array from the response
		try{
		JSONArray jsonarray = new JSONArray("[" + Response + "]");
		JSONObject jsonobject = jsonarray.getJSONObject(0);
		// Get the result variables from response
		String result = (jsonobject.getString("result"));
		String msg = (jsonobject.getString("msg"));
		// Close the connection
		client.getConnectionManager().shutdown();
		}catch(Exception e){
			e.printStackTrace();
		}
//		if (true == true) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModuleActivity.this);
		String usernames = prefs.getString(ModuleActivity.this.getString(R.string.prefs_username), "");
		System.out.println("Main here need to print username////"+ usernames);
		
			BaseUtill bs = new BaseUtill();
			String base64 = bs.get_Convert_Base64(MobileLearning.trackerbase64+usernames);
			String md5 = bs.get_Convert_MD5key(MobileLearning.trackerbase64+usernames+MobileLearning.md5passcode);

			if ((InternetConnections.getInstance(ModuleActivity.this).isInternetAlive) == true) {

				try {

					WebParser web = new WebParser();
					web.hitpathtoappend(MobileLearning.appurl, base64, md5);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

//		}
	}

	class QuizGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (ModuleActivity.this.currentActivity instanceof MQuizWidget) {
						if (((MQuizWidget) ModuleActivity.this.currentActivity).getMquiz().hasNext()) {
							((MQuizWidget) ModuleActivity.this.currentActivity).nextBtn.performClick();
						}
					}

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (ModuleActivity.this.currentActivity instanceof MQuizWidget) {
						if (((MQuizWidget) ModuleActivity.this.currentActivity).getMquiz().hasPrevious()) {
							((MQuizWidget) ModuleActivity.this.currentActivity).prevBtn.performClick();
						}
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

	}

	public void onInit(int status) {
		// check for successful instantiation
		if (status == TextToSpeech.SUCCESS) {
			Log.d(TAG, "tts success");
			ttsRunning = true;
			currentActivity.setReadAloud(true);
			HashMap<String,String> params = new HashMap<String,String>();
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,TAG);
			myTTS.speak(currentActivity.getContentToRead(), TextToSpeech.QUEUE_FLUSH, params);
			myTTS.setOnUtteranceCompletedListener(this);
		} else {
			// TTS not installed so show message
			Toast.makeText(this, this.getString(R.string.error_tts_start), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TTS_CHECK) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// the user has the necessary data - create the TTS
				myTTS = new TextToSpeech(this, this);
				
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void stopReading() {
		if (myTTS != null) {
			myTTS.stop();
			myTTS = null;
		}
		this.ttsRunning = false;
	}

	public void onUtteranceCompleted(String utteranceId) {
		Log.d(TAG,"Finished reading");
		this.ttsRunning = false;
		myTTS = null;
	}
}
