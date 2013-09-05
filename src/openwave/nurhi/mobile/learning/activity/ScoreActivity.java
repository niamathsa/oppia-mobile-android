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

import java.util.ArrayList;
import java.util.concurrent.Callable;

import openwave.nurhi.mobile.learning.adapter.PointsListAdapter;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.listener.APIRequestListener;
import openwave.nurhi.mobile.learning.model.Points;
import openwave.nurhi.mobile.learning.task.APIRequestTask;
import openwave.nurhi.mobile.learning.task.Payload;
import openwave.nurhi.mobile.learning.utils.UIUtils;

import openwave.nurhi.mobile.learning.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;

import com.bugsense.trace.BugSenseHandler;

public class ScoreActivity extends AppActivity implements APIRequestListener{

	public static final String TAG = ScoreActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	private JSONObject json;
	private SharedPreferences prefs;
	private Button btn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorecard);
		this.drawHeader();
		//this.getPoints();
		this.getScorecard();
		
		 btn = (Button) findViewById(R.id.scores_help);
		 btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							ScoreActivity.this);
					alertDialogBuilder.setTitle("Score Guide");
					alertDialogBuilder
							.setMessage(
									"You can earn points and badges for completing activites, quizzes and watching videos. Points are currently awarded as follows: \n\n" +
									"100 ? creating an account \n\n50 ? downloading a learning module \n\n20 ? first time you attempt a quiz  \n\n?? percentage score you got for a quiz on your first attempt. E.g. if you score 75% on your first attempt you get 75 points   \n\n50 ? bonus if you get 100% on your first attempt at a quiz\n\n" +
									"10 ? each subsequent attempt at a quiz (max. once per day per quiz)   \n\n10 ? completing an activity (max. once per day per activity)  \n\n20 ? watching a video (max. once per day per video) \n\n" +
									"The points scoring is designed to encourage you to return to the course regularly and review the content. \n\n"
											)
							.setCancelable(false)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {
											dialog.dismiss();
										}
									});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			});
		 
		new WriteFile(ScoreActivity.this).CreateFile("Visited Score Access page!!");
	}
	
	private void getScorecard(){
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		WebView webView = (WebView) findViewById(R.id.scorecard_webview);
		webView.getSettings().setJavaScriptEnabled(true);
		String url = prefs.getString(getString(R.string.prefs_server), getString(R.string.prefServer)) + "mobile/scorecard/?";
		url += "username=" + prefs.getString(getString(R.string.prefs_username), "");
		url += "&api_key=" + prefs.getString(getString(R.string.prefs_api_key), "");
		webView.loadUrl(url);
	}
	
	private void getPoints(){
		// show progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setTitle(R.string.loading);
		pDialog.setMessage(getString(R.string.loading_points));
		pDialog.setCancelable(true);
		pDialog.show();
		
		APIRequestTask task = new APIRequestTask(this);
		Payload p = new Payload(MobileLearning.SERVER_POINTS_PATH);
		task.setAPIRequestListener(this);
		task.execute(p);
	}

	private void refreshPointsList() {
		try {
			ArrayList<Points> points = new ArrayList<Points>();
			
			for (int i = 0; i < (json.getJSONArray("objects").length()); i++) {
				JSONObject json_obj = (JSONObject) json.getJSONArray("objects").get(i);
				Points p = new Points();
				p.setDescription(json_obj.getString("description"));
				p.setDateTime(json_obj.getString("date"));
				p.setPoints(json_obj.getInt("points"));

				points.add(p);
			}
			
			PointsListAdapter pla = new PointsListAdapter(this, points);
			ListView listView = (ListView) findViewById(R.id.points_list);
			listView.setAdapter(pla);

		} catch (Exception e) {
			e.printStackTrace();
			BugSenseHandler.sendException(e);
			UIUtils.showAlert(this, R.string.loading, R.string.error_processing_response);
		}

	}
	
	public void apiRequestComplete(Payload response) {
		pDialog.dismiss();
		if(response.isResult()){
			try {
				json = new JSONObject(response.getResultResponse());
				refreshPointsList();
				
				
				System.out.println("result ** "+json.toString());
				System.out.println("result ** "+json.toString());
				System.out.println("result ** "+json.toString());
			} catch (JSONException e) {
				BugSenseHandler.sendException(e);
				UIUtils.showAlert(this, R.string.loading, R.string.error_connection);
				e.printStackTrace();
			}
		} else {
			UIUtils.showAlert(this, R.string.error, R.string.error_connection_required1, new Callable<Boolean>() {
				public Boolean call() throws Exception {
					ScoreActivity.this.finish();
					return true;
				}
			});
		}
		
	}
}
