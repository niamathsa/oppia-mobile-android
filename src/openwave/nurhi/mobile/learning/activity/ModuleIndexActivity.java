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
import java.util.Locale;
import java.util.concurrent.Callable;

import openwave.nurhi.mobile.learning.R;
import openwave.nurhi.mobile.learning.adapter.SectionListAdapter;
import openwave.nurhi.mobile.learning.application.DbHelper;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.exception.InvalidXMLException;
import openwave.nurhi.mobile.learning.model.Activity;
import openwave.nurhi.mobile.learning.model.Module;
import openwave.nurhi.mobile.learning.model.ModuleMetaPage;
import openwave.nurhi.mobile.learning.model.Section;
import openwave.nurhi.mobile.learning.utils.ImageUtils;
import openwave.nurhi.mobile.learning.utils.ModuleXMLReader;
import openwave.nurhi.mobile.learning.utils.UIUtils;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uploaddata.BaseUtill;
import com.example.uploaddata.InternetConnections;
import com.example.uploaddata.WebParser;

public class ModuleIndexActivity extends AppActivity {

	public static final String TAG = ModuleIndexActivity.class.getSimpleName();

	private Module module;
	private ModuleXMLReader mxr;
	private ArrayList<Section> sections;
	private SharedPreferences prefs;
	public static Activity Act;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_module_index);

		this.drawHeader();
		 
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			module = (Module) bundle.getSerializable(Module.TAG);
			try {
				mxr = new ModuleXMLReader(module.getModuleXMLLocation());

				module.setMetaPages(mxr.getMetaPages());

				String digest = (String) bundle.getSerializable("JumpTo");
				if (digest != null) {
					// code to directly jump to a specific activity
					sections = mxr.getSections(module.getModId(),
							ModuleIndexActivity.this);
					for (Section s : sections) {
						for (int i = 0; i < s.getActivities().size(); i++) {
							Activity a = s.getActivities().get(i);
							if (a.getDigest().equals(digest)) {
								Intent intent = new Intent(this,
										ModuleActivity.class);
								Bundle tb = new Bundle();
								tb.putSerializable(Section.TAG, (Section) s);
								tb.putSerializable(Module.TAG, (Module) module);
								tb.putSerializable(
										SectionListAdapter.TAG_PLACEHOLDER,
										(Integer) i);
								intent.putExtras(tb);
								startActivity(intent);
							}
						}
					}

				}
			} catch (InvalidXMLException e) {
				UIUtils.showAlert(this, R.string.error,
						R.string.error_reading_xml, new Callable<Boolean>() {
							public Boolean call() throws Exception {
								ModuleIndexActivity.this.finish();
								return true;
							}
						});
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		sections = mxr.getSections(module.getModId(), ModuleIndexActivity.this);
		setTitle(module.getTitle(prefs.getString(
				getString(R.string.prefs_language), Locale.getDefault()
						.getLanguage())));

		TextView tv = (TextView) getHeader().findViewById(R.id.page_title);
		tv.setText(module.getTitle(prefs.getString(
				getString(R.string.prefs_language), Locale.getDefault()
						.getLanguage())));

		// set image
		if (module.getImageFile() != null) {
			ImageView iv = (ImageView) getHeader().findViewById(R.id.page_icon);
			Bitmap bm = ImageUtils.LoadBMPsdcard(module.getImageFile(),
					this.getResources(), R.drawable.default_icon_module);
			iv.setImageBitmap(bm);
		}

		ListView listView = (ListView) findViewById(R.id.section_list);
		SectionListAdapter sla = new SectionListAdapter(this, module, sections);
		listView.setAdapter(sla);

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.activity_module_index, menu);
		ArrayList<ModuleMetaPage> ammp = module.getMetaPages();
		int order = 104;
		for (ModuleMetaPage mmp : ammp) {
			String title = mmp.getLang(
					prefs.getString(getString(R.string.prefs_language), Locale
							.getDefault().getLanguage())).getContent();
			menu.add(0, mmp.getId(), order, title).setIcon(
					android.R.drawable.ic_menu_info_details);
			order++;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		Bundle tb = new Bundle();
		// Handle item selection
		switch (item.getItemId()) {
		 case R.id.menu_about:
			 new trackertask().execute();
		 return true;
		// case R.id.menu_help:
		// startActivity(new Intent(this, HelpActivity.class));
		// return true;
//		case R.id.menu_download:
//			startActivity(new Intent(this, TagSelectActivity.class));
//			return true;
			// case R.id.menu_settings:
			// Intent i = new Intent(this, PrefsActivity.class);
			// Bundle tb = new Bundle();
			// ArrayList<Lang> langs = new ArrayList<Lang>();
			// for (Module m : modules) {
			// langs.addAll(m.getLangs());
			// }
			// tb.putSerializable("langs", langs);
			// i.putExtras(tb);
			// startActivity(i);
			// return true;
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

	private void createLanguageDialog() {
		// UIUtils ui = new UIUtils();
		// ui.createLanguageDialog(this, module.getLangs(), prefs, new
		// Callable<Boolean>() {
		// public Boolean call() throws Exception {
		// ModuleIndexActivity.this.onStart();
		// return true;
		// }
		// });

		Intent i = new Intent(ModuleIndexActivity.this, LibraryActivity.class);
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
						DbHelper db = new DbHelper(ModuleIndexActivity.this);
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
						// ModuleIndexActivity.this.onStart();

						Intent i = new Intent(ModuleIndexActivity.this,
								LoginActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);

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

		Intent i = new Intent(ModuleIndexActivity.this, LibraryActivity.class);
		startActivity(i);
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

			if ((InternetConnections.getInstance(ModuleIndexActivity.this).isInternetAlive) == true) {

				try {
					
//					web.get_Department_Detail(appurl, base64, md5);
					

					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> Nurhi app Tracker server");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate -->  time of login user detail");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> Module install time and access time ");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> time of event access video access ");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> hardware info into server");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> battery into server");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> android info");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> Mobile network info");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> running application");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> installed application");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> simple location");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> continuos location");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> nearby wifi devices");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> nearby bluetooth devices");
					new WriteFile(ModuleIndexActivity.this)
							.CreateFile("yet to activate --> the file is uploading into server!!");

					
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModuleIndexActivity.this);
					String usernames = prefs.getString(ModuleIndexActivity.this.getString(R.string.prefs_username), "");
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

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ModuleIndexActivity.this);
		String usernames = prefs.getString(ModuleIndexActivity.this.getString(R.string.prefs_username), "");
		System.out.println("Main here need to print username////"+ usernames);
		
			BaseUtill bs = new BaseUtill();
			String base64 = bs.get_Convert_Base64(MobileLearning.trackerbase64+usernames);
			String md5 = bs.get_Convert_MD5key(MobileLearning.trackerbase64+usernames+MobileLearning.md5passcode);

			if ((InternetConnections.getInstance(ModuleIndexActivity.this).isInternetAlive) == true) {

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
