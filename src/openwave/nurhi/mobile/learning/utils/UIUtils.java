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

package openwave.nurhi.mobile.learning.utils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Callable;

import openwave.nurhi.mobile.learning.activity.ScoreActivity;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.model.Lang;

import openwave.nurhi.mobile.learning.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UIUtils {

	public final static String TAG = UIUtils.class.getSimpleName();
	private ArrayList<String> langStringList;
	private ArrayList<Lang> langList;
	private SharedPreferences prefs;
	private Context ctx;
	
	/**
	 * Displays the users points and badges scores in the app header
	 * @param act
	 */
	public static void showUserData(final Activity act) {
		TextView points = (TextView) act.findViewById(R.id.userpoints);
		TextView badges = (TextView) act.findViewById(R.id.userbadges);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(act.getBaseContext());
		if (MobileLearning.isLoggedIn(act)) {
			points.setVisibility(View.GONE);
			badges.setVisibility(View.GONE);
			points.setText(String.valueOf(prefs.getInt(act.getString(R.string.prefs_points), 0)));
			badges.setText(String.valueOf(prefs.getInt(act.getString(R.string.prefs_badges), 0)));

			if (!(act instanceof ScoreActivity)) {
				points.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						act.startActivity(new Intent(act, ScoreActivity.class));
					}
				});
			}
		}
	}

	/**
	 * @param ctx
	 * @param title
	 * @param msg
	 * @return
	 */
	public static AlertDialog showAlert(Context ctx, int title, int msg) {
		return UIUtils.showAlert(ctx, ctx.getString(title), ctx.getString(msg));
	}

	/**
	 * @param ctx
	 * @param R
	 * @param msg
	 * @return
	 */
	public static AlertDialog showAlert(Context ctx, int R, String msg) {
		return UIUtils.showAlert(ctx, ctx.getString(R), msg);
	}

	/**
	 * @param ctx
	 * @param title
	 * @param msg
	 * @return
	 */
	public static AlertDialog showAlert(Context ctx, String title, String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setButton(ctx.getString(R.string.close), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alertDialog.show();
		return alertDialog;
	}

	/**
	 * @param ctx
	 * @param title
	 * @param msg
	 * @param funct
	 * @return
	 */
	public static AlertDialog showAlert(Context ctx, int title, int msg, Callable<Boolean> funct) {
		return UIUtils.showAlert(ctx, ctx.getString(title), ctx.getString(msg), funct);
	}

	/**
	 * @param ctx
	 * @param R
	 * @param msg
	 * @param funct
	 * @return
	 */
	public static AlertDialog showAlert(Context ctx, int R, CharSequence msg, Callable<Boolean> funct) {
		return UIUtils.showAlert(ctx, ctx.getString(R), msg, funct);
	}

	/**
	 * @param ctx
	 * @param title
	 * @param msg
	 * @param funct
	 * @return
	 */
	public static AlertDialog showAlert(Context ctx, String title, CharSequence msg, final Callable<Boolean> funct) {
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setCancelable(true);
		alertDialog.setButton(ctx.getString(R.string.close), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				try {
					funct.call();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		alertDialog.show();
		return alertDialog;
	}
	
	
	/**
	 * 
	 * @param ctx
	 * @param langs
	 * @param prefs
	 * @param funct
	 */
	public void createLanguageDialog(Context ctx, ArrayList<Lang> langs, SharedPreferences prefs, final Callable<Boolean> funct) {
		this.langStringList = new ArrayList<String>();
		this.langList = new ArrayList<Lang>();
		this.prefs = prefs;
		this.ctx = ctx;
		
		// make sure there aren't any duplicates
		for(Lang l: langs){
			boolean found = false;
			for(Lang ln: langList){
				if(ln.getLang().equals(l.getLang())){
					found = true;
				}
			}
			if(!found){
				langList.add(l);
			}
		}
		
		int selected = -1;
		int i = 0;
		for(Lang l: langList){
			Locale loc = new Locale(l.getLang());
			String langDisp = loc.getDisplayLanguage(loc);
			langStringList.add(langDisp);
			if (l.getLang().equals(prefs.getString(ctx.getString(R.string.prefs_language), Locale.getDefault().getLanguage()))) {
				selected = i;
			}
			i++;
		}
		
		// only show if at least one language
		if (i > 0) {
			ArrayAdapter<String> arr = new ArrayAdapter<String>(ctx, android.R.layout.select_dialog_singlechoice,langStringList);

			AlertDialog mAlertDialog = new AlertDialog.Builder(ctx)
					.setSingleChoiceItems(arr, selected, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String newLang = langList.get(whichButton).getLang();
							Editor editor = UIUtils.this.prefs.edit();
							editor.putString(UIUtils.this.ctx.getString(R.string.prefs_language), newLang);
							editor.commit();
							dialog.dismiss();
							try {
								funct.call();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).setTitle(ctx.getString(R.string.change_language))
					.setNegativeButton(ctx.getString(R.string.cancel), new DialogInterface.OnClickListener() {
	
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
	
					}).create();
			mAlertDialog.show();
		}
	}
	

}
