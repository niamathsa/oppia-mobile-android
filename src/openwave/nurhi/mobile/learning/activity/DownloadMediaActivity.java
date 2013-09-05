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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import com.bugsense.trace.BugSenseHandler;

import openwave.nurhi.mobile.learning.adapter.DownloadMediaListAdapter;
import openwave.nurhi.mobile.learning.listener.DownloadMediaListener;
import openwave.nurhi.mobile.learning.model.DownloadProgress;
import openwave.nurhi.mobile.learning.model.Media;
import openwave.nurhi.mobile.learning.task.Payload;
import openwave.nurhi.mobile.learning.utils.UIUtils;

import openwave.nurhi.mobile.learning.R;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


public class DownloadMediaActivity extends AppActivity implements DownloadMediaListener{

	public static final String TAG = DownloadMediaActivity.class.getSimpleName();
	private ArrayList<Media> missingMedia = new ArrayList<Media>();
	private DownloadMediaListAdapter dmla;
	private AlertDialog alertDialog;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_media);
		this.drawHeader(getString(R.string.title_download_media));
		
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			missingMedia = (ArrayList<Media>) bundle.getSerializable(DownloadMediaActivity.TAG);
		}
		
		dmla = new DownloadMediaListAdapter(this, missingMedia);
		ListView listView = (ListView) findViewById(R.id.missing_media_list);
		dmla.setDownloadMediaListener(this);
		listView.setAdapter(dmla);
		
		Button downloadViaPCBtn = (Button) this.findViewById(R.id.download_media_via_pc_btn);
		downloadViaPCBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				downloadViaPC();
				
				new WriteFile(DownloadMediaActivity.this).CreateFile("Download Video Via PC Page Visited!!");
			}
		});
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor e = prefs.edit();
		e.putLong(getString(R.string.prefs_last_media_scan), 0);
		e.commit();
	}
	
	@Override
	public void onPause(){
		// kill any open dialogs
		if (alertDialog != null){
			alertDialog.dismiss();
		}
		if (dmla != null){
			dmla.closeDialogs();
		}
		super.onPause();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if (dmla != null){
			dmla.openDialogs();
		}

	}
	private void downloadViaPC(){
		String filename = "mobile-learning-media.html";
		String strData = "<html>";
		strData += "<head><title>"+this.getString(R.string.download_via_pc_title)+"</title></head>";
		strData += "<body>";
		strData += "<h3>"+this.getString(R.string.download_via_pc_title)+"</h3>";
		strData += "<p>"+this.getString(R.string.download_via_pc_intro)+"</p>";
		strData += "<ul>";
		for(Object o: missingMedia){
			Media m = (Media) o;
			strData += "<li><a href='"+m.getDownloadUrl()+"'>"+m.getFilename()+"</a></li>";
		}
		strData += "</ul>";
		strData += "</body></html>";
		strData += "<p>"+this.getString(R.string.download_via_pc_final,"/digitalcampus/media/")+"</p>";
		
		File file = new File(Environment.getExternalStorageDirectory(),filename);
		try {
			FileOutputStream f = new FileOutputStream(file);
			Writer out = new OutputStreamWriter(new FileOutputStream(file));
			out.write(strData);
			out.close();
			f.close();
			UIUtils.showAlert(this, R.string.info, this.getString(R.string.download_via_pc_message,filename));
		} catch (FileNotFoundException e) {
			BugSenseHandler.sendException(e);
			e.printStackTrace();
		} catch (IOException e) {
			BugSenseHandler.sendException(e);
			e.printStackTrace();
		}
	}

	public void downloadProgressUpdate(DownloadProgress msg) {

	}

	public void downloadComplete(Payload response) {
		if (response.isResult()){
			missingMedia.remove((Media) response.getData().get(0));
			dmla.notifyDataSetChanged();
			alertDialog = UIUtils.showAlert(this, R.string.info,response.getResultResponse());
		} else {
			alertDialog = UIUtils.showAlert(this, R.string.error,response.getResultResponse());
		}
	}
}
