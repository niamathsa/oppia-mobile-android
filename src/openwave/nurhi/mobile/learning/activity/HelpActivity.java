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

import java.util.Locale;


import openwave.nurhi.mobile.learning.utils.FileUtils;

import openwave.nurhi.mobile.learning.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;

public class HelpActivity extends AppActivity {

	public static final String TAG = HelpActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		this.drawHeader();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String url = "file:///android_asset/" + FileUtils.getLocalizedFilePath(this,prefs.getString(getString(R.string.prefs_language), Locale.getDefault().getLanguage()) , "help.html");
		WebView wv = (WebView) findViewById(R.id.about_webview);
		wv.loadUrl(url);
		
		
		new WriteFile(HelpActivity.this).CreateFile("Help Page Viewed!!");
		
	}
}

