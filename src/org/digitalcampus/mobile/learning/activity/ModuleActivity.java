package org.digitalcampus.mobile.learning.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.digitalcampus.mobile.learning.application.Tracker;
import org.digitalcampus.mobile.learning.model.Module;
import org.digitalcampus.mobile.learning.model.Section;
import org.digitalcampus.mobile.learning.service.TrackerService;
import org.digitalcampus.mobile.learning.widgets.MQuizWidget;
import org.digitalcampus.mobile.learning.widgets.PageWidget;
import org.digitalcampus.mobile.learning.widgets.WidgetFactory;
import org.digitalcampus.mobile.learning.R;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ModuleActivity extends Activity {

	public static final String TAG = "ModuleActivity";
	private Section section;
	private Module module;
	private int currentActivityNo = 0;
	private WidgetFactory currentActivity;
	private SharedPreferences prefs;
	
	private HashMap<String, String> langMap = new HashMap<String, String>();
	private String[] langArray;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        Bundle bundle = this.getIntent().getExtras(); 
        if(bundle != null) {
        	section = (Section) bundle.getSerializable(Section.TAG);
        	module = (Module) bundle.getSerializable(Module.TAG);
        }
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	rebuildLangs();
    	setTitle(section.getTitle(prefs.getString("prefLanguage", Locale.getDefault().getLanguage())));
    	loadActivity();
    	
    }
    @Override
    public void onPause(){
    	super.onPause();
    	ArrayList<org.digitalcampus.mobile.learning.model.Activity> acts = section.getActivities();
    	markIfComplete(acts.get(this.currentActivityNo).getDigest());
    	// start a new tracker service
    	Log.d(TAG,"Starting tracker service");
    	Intent service = new Intent(this, TrackerService.class);
    	
    	Bundle tb = new Bundle();
		tb.putBoolean("backgroundData", true);
		service.putExtras(tb);
		
		this.startService(service);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_module, menu);
        return true;
    }
    
    @Override
   	public boolean onOptionsItemSelected(MenuItem item) {
       	Intent i;
   		// Handle item selection
   		switch (item.getItemId()) {
   			case R.id.menu_module_about:
   				i = new Intent(this, ModuleAboutActivity.class);
   				Bundle tb = new Bundle();
   				tb.putSerializable(Module.TAG, module);
   				i.putExtras(tb);
   				startActivity(i);
   				return true;
   			case R.id.menu_language:
   				createLanguageDialog();
   				return true;
   			case R.id.menu_help:
   				startActivity(new Intent(this, HelpActivity.class));
   				return true;
   			default:
   				return super.onOptionsItemSelected(item);
   		}
   	}
    
    private void loadActivity(){
    	ArrayList<org.digitalcampus.mobile.learning.model.Activity> acts = section.getActivities();
    	TextView tb = (TextView) this.findViewById(R.id.module_activity_title);
    	
    	tb.setText(acts.get(this.currentActivityNo).getTitle(prefs.getString("prefLanguage", Locale.getDefault().getLanguage())));
    	
    	if(acts.get(this.currentActivityNo).getActType().equals("page")){
    		currentActivity = new PageWidget(ModuleActivity.this, module, acts.get(this.currentActivityNo));
    	}
    	if(acts.get(this.currentActivityNo).getActType().equals("quiz")){
    		currentActivity = new MQuizWidget(ModuleActivity.this, module, acts.get(this.currentActivityNo));
    		
    	}
    	this.setUpNav();
    }
    
    private void setUpNav(){
    	Button prevB = (Button) ModuleActivity.this.findViewById(R.id.prev_btn);
    	Button nextB = (Button) ModuleActivity.this.findViewById(R.id.next_btn);
    	if(this.hasPrev()){
    		prevB.setEnabled(true);
    		prevB.setOnClickListener(new View.OnClickListener() {
             	public void onClick(View v) {
             		ArrayList<org.digitalcampus.mobile.learning.model.Activity> acts = section.getActivities();
             		markIfComplete(acts.get(currentActivityNo).getDigest());
             		currentActivityNo--;
             		loadActivity();
             	}
             });
    	} else {
    		prevB.setEnabled(false);
    	}
    	
    	if(this.hasNext()){
    		nextB.setEnabled(true);
    		nextB.setOnClickListener(new View.OnClickListener() {
             	public void onClick(View v) {
             		ArrayList<org.digitalcampus.mobile.learning.model.Activity> acts = section.getActivities();
             		markIfComplete(acts.get(currentActivityNo).getDigest());
             		currentActivityNo++;
             		loadActivity();
             	}
             });
    	} else {
    		nextB.setEnabled(false);
    	}
    }
    
    private boolean hasPrev(){
    	if(this.currentActivityNo == 0){
    		return false;
    	}
    	return true;
    }
    
    private boolean hasNext(){
    	int noActs = section.getActivities().size();
    	if(this.currentActivityNo + 1 == noActs){
    		return false;
    	} else {
    		return true;
    	}
    }
    
    private boolean markIfComplete(String digest){
    	// TODO also check any media has been played
    	if(currentActivity != null && currentActivity.isComplete()){
    		Tracker t = new Tracker(this);
    		JSONObject json = currentActivity.getActivityCompleteData();
    		t.activityComplete(module.getModId(), digest, json);
    	}    	
    	return true;
    }
    
    private void rebuildLangs() {
		// recreate langMap
		langMap = new HashMap<String, String>();
		Iterator<String> itr = module.getAvailableLangs().iterator();
		while (itr.hasNext()) {
			String lang = itr.next();
			Locale l = new Locale(lang);
			String langDisp = l.getDisplayLanguage(l);
			langMap.put(langDisp, lang);
		}

	}
    
    private void createLanguageDialog() {
		int selected = -1;
		// TODO this is all quite untidy - fix it up!
		
		langArray = new String[langMap.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : langMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			langArray[i] = key;
			if (value.equals(prefs.getString("prefLanguage", Locale.getDefault().getLanguage()))) {
				selected = i;
			}
			i++;
		}

		AlertDialog mAlertDialog = new AlertDialog.Builder(this)
				.setSingleChoiceItems(langArray, selected, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String newLang = langMap.get(langArray[whichButton]);
						Editor editor = prefs.edit();
						editor.putString("prefLanguage", newLang);
						editor.commit();
						dialog.dismiss();
						onStart();
					}
				}).setTitle(getString(R.string.change_language))
				.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}

				}).create();
		mAlertDialog.show();
	}
}