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

package openwave.nurhi.mobile.learning.application;

import java.io.File;

import openwave.nurhi.mobile.learning.task.SubmitTrackerMultipleTask;

import openwave.nurhi.mobile.learning.R;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class MobileLearning extends Application {

	public static final String TAG = MobileLearning.class.getSimpleName();

	// local storage vars
	public static final String OPPIAMOBILE_ROOT = Environment
			.getExternalStorageDirectory() + "/digitalcampus/";
	public static final String MODULES_PATH = OPPIAMOBILE_ROOT + "modules/";
	public static final String MEDIA_PATH = OPPIAMOBILE_ROOT + "media/";
	public static final String DOWNLOAD_PATH = OPPIAMOBILE_ROOT + "download/";
	public static final String MODULE_XML = "module.xml";
	public static final String MODULE_SCHEDULE_XML = "schedule.xml";
	public static final String MODULE_TRACKER_XML = "tracker.xml";

	// server path vars - new version
	public static final String OPPIAMOBILE_API = "api/v1/";
	public static final String LOGIN_PATH = OPPIAMOBILE_API + "user/";
	public static final String REGISTER_PATH = OPPIAMOBILE_API + "register/";
	public static final String MQUIZ_SUBMIT_PATH = OPPIAMOBILE_API
			+ "quizattempt/";
	public static final String SERVER_COURSES_PATH = OPPIAMOBILE_API
			+ "course/";
	public static final String SERVER_TAG_PATH = OPPIAMOBILE_API + "tag/";
	public static final String TRACKER_PATH = OPPIAMOBILE_API + "tracker/";
	public static final String SERVER_POINTS_PATH = OPPIAMOBILE_API + "points/";
	public static final String SERVER_COURSES_NAME = "courses";

	// general other settings
	public static final String BUGSENSE_API_KEY = "4a9e17d2";
	public static final int PASSWORD_MIN_LENGTH = 6;
	public static final int PAGE_READ_TIME = 3;
	public static final String USER_AGENT = "OppiaMobile Android: ";
	public static final int MQUIZ_PASS_THRESHOLD = 100;
	public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormat
			.forPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat
			.forPattern("yyyy-MM-dd");
	public static final DateTimeFormatter TIME_FORMAT = DateTimeFormat
			.forPattern("HH:mm:ss");
	public static final int MAX_TRACKER_SUBMIT = 10;

	// only used in case a module doesn't have any lang specified
	public static final String DEFAULT_LANG = "en";

	// for tracking if SubmitTrackerMultipleTask is already running
	public SubmitTrackerMultipleTask omSubmitTrackerMultipleTask = null;

	// nurhi server
	public static String appurl = "http://220.226.2.31/nigerian/nigerian/api/?";
	String file_upload = "http://220.226.2.31/nigerian/nigerian/api/commonfiles/"; // file_guna.txt

	// action=usertrack&device=iphone&user_id=guna
	public static String adduserbase64 = "action=usersadd&device=iphone&user_id=";
	public static String adduserbase641 = "&mobile=";
	public static String adduserbase642 = "&phone=";
	public static String adduserbase643 = "&first_name=";
	public static String adduserbase644 = "&username=";
	public static String adduserbase645 = "&last_name=";
	public static String adduserbase646 = "&password=";
	public static String adduserbase647 = "&email=";
	public static String adduserbase648 = "&question_type_id=";
	public static String adduserbase649 = "&question_id=0&answer=0&tot_score=0&ind_score=0";
	
	public static String md5passcode = "&passcode=ERT81E8213797F23651CF57E59B4BD8DA0";

	public static final String app_passkey = "ERT81E8213797F23651CF57E59B4BD8DA0"; // ERT81E8213797F23651CF57E59B4BD8DA0

	public static String trackerbase64 = "action=usertrack&device=iphone&user_id=";
	

	public static boolean createDirs() {
		String cardstatus = Environment.getExternalStorageState();
		if (cardstatus.equals(Environment.MEDIA_REMOVED)
				|| cardstatus.equals(Environment.MEDIA_UNMOUNTABLE)
				|| cardstatus.equals(Environment.MEDIA_UNMOUNTED)
				|| cardstatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)
				|| cardstatus.equals(Environment.MEDIA_SHARED)) {
			return false;
		}

		String[] dirs = { OPPIAMOBILE_ROOT, MODULES_PATH, MEDIA_PATH,
				DOWNLOAD_PATH };

		for (String dirName : dirs) {
			File dir = new File(dirName);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					return false;
				}
			} else {
				if (!dir.isDirectory()) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isLoggedIn(Activity act) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(act.getBaseContext());
		String username = prefs.getString(
				act.getString(R.string.prefs_username), "");
		String apiKey = prefs.getString(act.getString(R.string.prefs_api_key),
				"");
		if (username.trim().equals("") || apiKey.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}

}
