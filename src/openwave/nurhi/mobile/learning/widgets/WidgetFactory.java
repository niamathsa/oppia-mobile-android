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

package openwave.nurhi.mobile.learning.widgets;

import openwave.nurhi.mobile.learning.model.Module;

import openwave.nurhi.mobile.learning.R;
import org.digitalcampus.mquiz.MQuiz;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public abstract class WidgetFactory extends Activity {
	
	public final static String TAG = WidgetFactory.class.getSimpleName();
	private LayoutInflater li;
	private LinearLayout ll;
	
	public WidgetFactory(Context context, Module module, openwave.nurhi.mobile.learning.model.Activity activity ) {
		super();
		ll = (LinearLayout) ((Activity) context).findViewById(R.id.activity_widget);
		ll.removeAllViews();
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public LayoutInflater getLayoutInflater(){
		return li;
	}
	
	public LinearLayout getLayout(){
		return ll;
	}
	
	public abstract boolean activityHasTracker();
	public abstract boolean activityCompleted();
	
	public abstract long getTimeTaken();
	public abstract void setStartTime(long startTime);
	public abstract long getStartTime();
	
	public abstract JSONObject getTrackerData();
	
	public abstract String getContentToRead();
	
	public abstract String getMediaFileName();
	public abstract void setMediaFileName(String mediaFileName);
	public abstract void mediaStopped();
	public abstract boolean getMediaPlaying();
	public abstract long getMediaStartTime();
	public abstract void setMediaPlaying(boolean playing);
	public abstract void setMediaStartTime(long mediaStartTime);
	
	public abstract void setReadAloud(boolean reading);
	public abstract boolean getReadAloud();
	
	public abstract MQuiz getMQuiz();
	public abstract void setMQuiz(MQuiz mquiz);
	
}
