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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Callable;

import openwave.nurhi.mobile.learning.R;
import openwave.nurhi.mobile.learning.R.id;
import openwave.nurhi.mobile.learning.adapter.ModuleListAdapter;
import openwave.nurhi.mobile.learning.application.DbHelper;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.exception.ModuleNotFoundException;
import openwave.nurhi.mobile.learning.listener.InstallModuleListener;
import openwave.nurhi.mobile.learning.listener.ScanMediaListener;
import openwave.nurhi.mobile.learning.listener.UpgradeListener;
import openwave.nurhi.mobile.learning.model.Activity;
import openwave.nurhi.mobile.learning.model.DownloadProgress;
import openwave.nurhi.mobile.learning.model.Lang;
import openwave.nurhi.mobile.learning.model.Module;
import openwave.nurhi.mobile.learning.task.InstallDownloadedModulesTask;
import openwave.nurhi.mobile.learning.task.Payload;
import openwave.nurhi.mobile.learning.task.ScanMediaTask;
import openwave.nurhi.mobile.learning.task.UpgradeManagerTask;
import openwave.nurhi.mobile.learning.utils.UIUtils;
import openwave.nurhi.mobile.learning.utils.UpgradeUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.example.uploaddata.BaseUtill;
import com.example.uploaddata.InternetConnections;
import com.example.uploaddata.WebParser;

public class MobileLearningActivity extends AppActivity implements
		InstallModuleListener, OnSharedPreferenceChangeListener,
		ScanMediaListener, UpgradeListener {

	public static final String TAG = MobileLearningActivity.class
			.getSimpleName();
	private SharedPreferences prefs;
	private Module tempMod;
	private ArrayList<Module> modules;
	TextView quizcall;
	
	String uploaded_file = "file_guna.txt";

//	String upLoadServerUri = file_upload;

	private File mainDirectory = new File(
			Environment.getExternalStorageDirectory() + "/NigerianProjectFile");

	private File direct = new File(Environment.getExternalStorageDirectory()
			+ "/NigerianProjectFile/documents");

	private File directVideo = new File(
			Environment.getExternalStorageDirectory()
					+ "/NigerianProjectFile/video");

	public static ArrayList<InputStream> input;
	public static ArrayList<InputStream> input1;

	private ArrayList<String> urlList;

	ArrayList<String> downloadPathList = new ArrayList<String>();
	Button btnllibrary;
	private ArrayList<File> fileList;

	private ArrayList<File> fileList1;

	DataOutputStream fos;
	OutputStream out;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	ArrayList<String> fileName = new ArrayList<String>();
	ArrayList<String> fileName1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(this,
				MobileLearning.BUGSENSE_API_KEY);
		setContentView(R.layout.activity_main);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

		this.drawHeader();
		this.drawMessages();

		// set preferred lang to the default lang
		if (prefs.getString(getString(R.string.prefs_language), "").equals("")) {
			Editor editor = prefs.edit();
			editor.putString(getString(R.string.prefs_language), Locale
					.getDefault().getLanguage());
			editor.commit();
		}

		// set up local dirs
		if (!MobileLearning.createDirs()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setTitle(R.string.error);
			builder.setMessage(R.string.error_sdcard);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							MobileLearningActivity.this.finish();
						}
					});
			builder.show();
			return;
		}
		// do upgrade if required
		UpgradeManagerTask umt = new UpgradeManagerTask(this);
		umt.setUpgradeListener(this);
		ArrayList<Object> data = new ArrayList<Object>();
		Payload p = new Payload(data);
		umt.execute(p);

		UpgradeUtils uu = new UpgradeUtils(this);
		uu.show();

		quizcall = (TextView) findViewById(R.id.quizcall);
		quizcall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// MQuizWidget(MobileLearningActivity.this, modules,new
				// Activity());
				DownloadActivity dw = new DownloadActivity();

				dw.refreshModuleList();
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		if (!MobileLearning.isLoggedIn(this)) {
			startActivity(new Intent(MobileLearningActivity.this,
					LoginActivity.class));
			return;
		}

		displayModules();

		// install any new modules
		File dir = new File(MobileLearning.DOWNLOAD_PATH);
		String[] children = dir.list();
		if (children != null) {
			ArrayList<Object> data = new ArrayList<Object>();
			Payload p = new Payload(data);
			InstallDownloadedModulesTask imTask = new InstallDownloadedModulesTask(
					MobileLearningActivity.this);
			imTask.setInstallerListener(this);
			imTask.execute(p);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		/*
		 * DbHelper db = new DbHelper(this); MessageFeed mf =
		 * db.getMessageFeed(); db.close(); this.updateMessages(mf);
		 */
		this.updateHeader();
		this.updateReminders();
	}

	@Override
	public void onPause() {
		this.stopMessages();
		super.onPause();
	}

	private void displayModules() {

		DbHelper db = new DbHelper(this);
		modules = db.getModules();
		db.close();

		if (MobileLearning.createDirs()) {
			// only remove modules if the SD card is present
			// - else it will remove the modules just because the SD card isn't
			// in
			ArrayList<Module> removeModules = new ArrayList<Module>();
			for (Module m : modules) {
				try {
					m.validate();
				} catch (ModuleNotFoundException mnfe) {
					// remove from database
					mnfe.deleteModule(this, m.getModId());
					removeModules.add(m);
				}
			}

			for (Module m : removeModules) {
				// remove from current list
				modules.remove(m);
			}
		}

		LinearLayout llLoading = (LinearLayout) this
				.findViewById(R.id.loading_modules);
		llLoading.setVisibility(View.GONE);
		LinearLayout llNone = (LinearLayout) this.findViewById(R.id.no_modules);
		if (modules.size() > 0) {
			llNone.setVisibility(View.GONE);
		} else {
			llNone.setVisibility(View.VISIBLE);
			Button manageBtn = (Button) this
					.findViewById(R.id.manage_modules_btn);
			manageBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(MobileLearningActivity.this,
							TagSelectActivity.class));
				}
			});

		}

		ModuleListAdapter mla = new ModuleListAdapter(this, modules);
		ListView listView = (ListView) findViewById(R.id.module_list);
		listView.setAdapter(mla);
		registerForContextMenu(listView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Module m = (Module) view.getTag();
				Intent i = new Intent(MobileLearningActivity.this,
						ModuleIndexActivity.class);
				Bundle tb = new Bundle();
				tb.putSerializable(Module.TAG, m);
				i.putExtras(tb);
				startActivity(i);
			}
		});

		this.updateReminders();

		// scan media
		this.scanMedia();
	}

	private void updateReminders() {
		if (prefs.getBoolean(getString(R.string.prefs_schedule_reminders_show),
				true)) {
			DbHelper db = new DbHelper(MobileLearningActivity.this);
			int max = Integer.valueOf(prefs.getString(
					getString(R.string.prefs_schedule_reminders_no), "3"));
			ArrayList<Activity> activities = db.getActivitiesDue(max);
			db.close();
			this.drawReminders(activities);
		} else {
			LinearLayout ll = (LinearLayout) findViewById(R.id.schedule_reminders);
			ll.setVisibility(View.GONE);
		}
	}

	private void scanMedia() {
		long now = System.currentTimeMillis() / 1000;
		if (prefs.getLong(getString(R.string.prefs_last_media_scan), 0) + 3600 > now) {
			LinearLayout ll = (LinearLayout) this
					.findViewById(id.home_messages);
			ll.setVisibility(View.GONE);
			return;
		}
		ScanMediaTask task = new ScanMediaTask();
		Payload p = new Payload(this.modules);
		task.setScanMediaListener(this);
		task.execute(p);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
//		case R.id.menu_about:		 
//			new trackertask().execute();	 
//			return true;
		case R.id.menu_download:
			startActivity(new Intent(this, TagSelectActivity.class));
			return true;
		case R.id.menu_settings:
			Intent i = new Intent(this, PrefsActivity.class);
			Bundle tb = new Bundle();
			ArrayList<Lang> langs = new ArrayList<Lang>();
			for (Module m : modules) {
				langs.addAll(m.getLangs());
			}
			tb.putSerializable("langs", langs);
			i.putExtras(tb);
			startActivity(i);
			return true;
		case R.id.menu_language:
			// createLanguageDialog();
			createLibrary();
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpActivity.class));
			return true;
		case R.id.menu_logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle item selection
//		switch (item.getItemId()) {
//		case R.id.menu_about:
//			// startActivity(new Intent(this,
//			// openwave.nurhi.mobile.learning.activity.FunfMainActivity.class));
//			// //AboutActivity
//
//			new trackertask().execute();
//
//			// / here need to sync data with server and connect 2 urls
//
//			//
//			// boolean applanch = false;
//			//
//			// try {
//			// PackageManager pm = getPackageManager();
//			// pm.getApplicationInfo("edu.mit.media.funf.journal",
//			// pm.GET_UNINSTALLED_PACKAGES);
//			// Intent intent1 = pm
//			// .getLaunchIntentForPackage("edu.mit.media.funf.journal");
//			// startActivity(intent1);
//			// applanch = true;
//			// } catch (NameNotFoundException e) {
//			// e.printStackTrace();
//			// applanch = false;
//			// }
//			//
//			// if (applanch == false) {
//			//
//			// if (InternetConnections
//			// .getInstance(MobileLearningActivity.this)
//			// .isNetworkAvailable() == true) {
//			// try {
//			// startActivity(new Intent(
//			// Intent.ACTION_VIEW,
//			// Uri.parse("https://play.google.com/store/apps/details?id=edu.mit.media.funf.journal")));//
//			// market://details?id="+appName
//			// } catch (android.content.ActivityNotFoundException a) {
//			// a.printStackTrace();
//			// }
//			// } else {
//			// Toast.makeText(MobileLearningActivity.this,
//			// "Please Connect your Internet", Toast.LENGTH_LONG)
//			// .show();
//			//
//			// }
//			// }
//
//			return true;
//		case R.id.menu_download:
//			startActivity(new Intent(this, TagSelectActivity.class));
//			return true;
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
//		case R.id.menu_language:
//			// createLanguageDialog();
//			createLibrary();
//			return true;
//		case R.id.menu_help:
//			startActivity(new Intent(this, HelpActivity.class));
//			return true;
//		case R.id.menu_logout:
//			
//			logout();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}

	private void createLanguageDialog() {
		ArrayList<Lang> langs = new ArrayList<Lang>();
		for (Module m : modules) {
			langs.addAll(m.getLangs());
		}

		UIUtils ui = new UIUtils();
		ui.createLanguageDialog(this, langs, prefs, new Callable<Boolean>() {
			public Boolean call() throws Exception {
				MobileLearningActivity.this.onStart();
				return true;
			}
		});
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
						DbHelper db = new DbHelper(MobileLearningActivity.this);
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
						MobileLearningActivity.this.onStart();

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

	public void installComplete(Payload p) {
		if (p.getResponseData().size() > 0) {
			Editor e = prefs.edit();
			e.putLong(getString(R.string.prefs_last_media_scan), 0);
			e.commit();
			displayModules();
		}
	}

	public void installProgressUpdate(DownloadProgress dp) {
		// do nothing
	}

	public void createLibrary() {

		Intent i = new Intent(MobileLearningActivity.this,
				LibraryActivity.class);
		startActivity(i);
	 
	}

	class download_task extends AsyncTask<String, String, String> {
		ProgressDialog dialog = null;
		ArrayList<String> department_data;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = ProgressDialog.show(MobileLearningActivity.this, "",
					"Downloading file...", true);
		}

		@Override
		protected String doInBackground(String... params) {

			if (dialog.isShowing()) {

				dialog.setCancelable(false);
			}

			if (mainDirectory.exists() == false) {
				mainDirectory.mkdir();
			}

			if (direct.exists() == false) {
				direct.mkdir();
			}

			if (directVideo.exists() == false) {
				directVideo.mkdir();
			}

			Log.d("Error For This", "InputSize ---- >" + input.size());

			for (int i = 0; i < input.size(); i++) { // down load from asset
				// folder

				Log.v("Error For This", "i ---- >" + i);

				if (fileList.get(i).exists() == false) {

					File f2 = new File(
							Environment.getExternalStorageDirectory()
									+ "/NigerianProjectFile/documents");

					try {

						out = new FileOutputStream(f2 + "/" + fileName.get(i));

						byte[] buf = new byte[1024];
						int len;
						while ((len = input.get(i).read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						input.get(i).close();
						out.close();

						Log.v("Download",
								"had been Succeed for   -----------------> "
										+ fileName.get(i));

					} catch (FileNotFoundException ex) {

						Log.d("File", "Error is" + ex);

					} catch (IOException e) {
						System.out.println(e.getMessage());
					} finally {
						try {
							input.get(i).close();
							out.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else
					Log.d("Document File", "Exit  ------  "
							+ fileName.get(i).toString());

			}

			for (int i = 0; i < input1.size(); i++) { // down load from asset
				// folder

				Log.v("Error For This", "i ---- >" + i);

				if (fileList1.get(i).exists() == false) {

					File f2 = new File(
							Environment.getExternalStorageDirectory()
									+ "/NigerianProjectFile/video");

					try {

						out = new FileOutputStream(f2 + "/" + fileName1.get(i));

						byte[] buf = new byte[1024];
						int len;
						while ((len = input1.get(i).read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						input1.get(i).close();
						out.close();

						Log.v("Download",
								"had been Succeed for   -----------------> "
										+ fileName.get(i));

					} catch (FileNotFoundException ex) {

						Log.d("File", "Error is" + ex);

					} catch (IOException e) {
						System.out.println(e.getMessage());
					} finally {
						try {
							input1.get(i).close();
							out.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else
					Log.d("Video File", "Exit  ------  "
							+ fileName1.get(i).toString());

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			// boolean v = sharedPreferences.getBoolean("install", false);

		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.module_context_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		tempMod = (Module) info.targetView.getTag();
		switch (item.getItemId()) {
		case R.id.module_context_delete:
			confirmModuleDelete();
			return true;
		case R.id.module_context_reset:
			confirmModuleReset();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void confirmModuleDelete() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle(R.string.module_context_delete);
		builder.setMessage(R.string.module_context_delete_confirm);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// remove db records
						DbHelper db = new DbHelper(MobileLearningActivity.this);
						db.deleteModule(tempMod.getModId());
						db.close();
						// remove files
						File f = new File(tempMod.getLocation());
						// FileUtils.deleteDir(f);
						Editor e = prefs.edit();
						e.putLong(getString(R.string.prefs_last_media_scan), 0);
						e.commit();
						displayModules();
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						tempMod = null;
					}
				});
		builder.show();
	}

	private void confirmModuleReset() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle(R.string.module_context_reset);
		builder.setMessage(R.string.module_context_reset_confirm);
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						DbHelper db = new DbHelper(MobileLearningActivity.this);
						db.resetModule(tempMod.getModId());
						db.close();
						displayModules();
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						tempMod = null;
					}
				});
		builder.show();
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(TAG, key + " changed");
		if (key.equalsIgnoreCase(getString(R.string.prefs_server))) {
			Editor editor = sharedPreferences.edit();
			if (!sharedPreferences.getString(getString(R.string.prefs_server),
					"").endsWith("/")) {
				String newServer = sharedPreferences.getString(
						getString(R.string.prefs_server), "") + "/";
				editor.putString(getString(R.string.prefs_server), newServer);
				editor.commit();
			}
		}
		if (key.equalsIgnoreCase(getString(R.string.prefs_schedule_reminders_show))
				|| key.equalsIgnoreCase(getString(R.string.prefs_schedule_reminders_no))) {
			displayModules();
		}
		super.onSharedPreferenceChanged(sharedPreferences, key);
	}

	public void downloadComplete(Payload p) {
		// do nothing

	}

	public void scanStart() {
		TextView tv = (TextView) this.findViewById(id.home_message);
		tv.setText(this.getString(R.string.info_scan_media_start));
	}

	public void scanProgressUpdate(String msg) {
		TextView tv = (TextView) this.findViewById(id.home_message);
		tv.setText(this.getString(R.string.info_scan_media_checking, msg));
	}

	public void scanComplete(Payload response) {
		Editor e = prefs.edit();
		LinearLayout ll = (LinearLayout) this.findViewById(id.home_messages);
		TextView tv = (TextView) this.findViewById(id.home_message);
		Button btn = (Button) this.findViewById(R.id.message_action_button);

		if (response.getResponseData().size() > 0) {
			ll.setVisibility(View.VISIBLE);
			tv.setText(this.getString(R.string.info_scan_media_missing));
			btn.setText(this.getString(R.string.scan_media_download_button));
			btn.setTag(response.getResponseData());
			btn.setOnClickListener(new OnClickListener() {

				public void onClick(View view) {
					@SuppressWarnings("unchecked")
					ArrayList<Object> m = (ArrayList<Object>) view.getTag();
					Intent i = new Intent(MobileLearningActivity.this,
							DownloadMediaActivity.class);
					Bundle tb = new Bundle();
					tb.putSerializable(DownloadMediaActivity.TAG, m);
					i.putExtras(tb);
					startActivity(i);
				}
			});
			e.putLong(getString(R.string.prefs_last_media_scan), 0);
			e.commit();
		} else {
			ll.setVisibility(View.GONE);
			tv.setText("");
			btn.setText("");
			btn.setOnClickListener(null);
			btn.setTag(null);
			long now = System.currentTimeMillis() / 1000;
			e.putLong(getString(R.string.prefs_last_media_scan), now);
			e.commit();
		}
	}

	public void downloadProgressUpdate(DownloadProgress dp) {
		// do nothing

	}

	public void upgradeComplete(Payload p) {
		if (p.isResult()) {
			displayModules();
		}
	}

	public void upgradeProgressUpdate(String s) {
		// do nothing

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

			if ((InternetConnections.getInstance(MobileLearningActivity.this).isInternetAlive) == true) {

				try {
					
//					web.get_Department_Detail(appurl, base64, md5);
					

					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> Nurhi app Tracker server");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate -->  time of login user detail");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> Module install time and access time ");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> time of event access video access ");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> hardware info into server");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> battery into server");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> android info");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> Mobile network info");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> running application");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> installed application");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> simple location");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> continuos location");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> nearby wifi devices");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> nearby bluetooth devices");
					new WriteFile(MobileLearningActivity.this)
							.CreateFile("yet to activate --> the file is uploading into server!!");

					
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MobileLearningActivity.this);
					String usernames = prefs.getString(MobileLearningActivity.this.getString(R.string.prefs_username), "");
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

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MobileLearningActivity.this);
		String usernames = prefs.getString(MobileLearningActivity.this.getString(R.string.prefs_username), "");
		System.out.println("Main here need to print username////"+ usernames);
		
			BaseUtill bs = new BaseUtill();
			String base64 = bs.get_Convert_Base64(MobileLearning.trackerbase64+usernames);
			String md5 = bs.get_Convert_MD5key(MobileLearning.trackerbase64+usernames+MobileLearning.md5passcode);

			if ((InternetConnections.getInstance(MobileLearningActivity.this).isInternetAlive) == true) {

				try {

					WebParser web = new WebParser();
					web.hitpathtoappend(MobileLearning.appurl, base64, md5);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

//		}
	}

}
