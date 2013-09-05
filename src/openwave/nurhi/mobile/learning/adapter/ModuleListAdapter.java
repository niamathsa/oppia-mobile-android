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

package openwave.nurhi.mobile.learning.adapter;

import java.util.ArrayList;
import java.util.Locale;

import openwave.nurhi.mobile.learning.model.Module;
import openwave.nurhi.mobile.learning.utils.ImageUtils;

import openwave.nurhi.mobile.learning.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ModuleListAdapter extends ArrayAdapter<Module> {

	public static final String TAG = ModuleListAdapter.class.getSimpleName();

	private final Context ctx;
	private final ArrayList<Module> moduleList;
	private SharedPreferences prefs;
	
	public ModuleListAdapter(Activity context, ArrayList<Module> moduleList) {
		super(context, R.layout.module_list_row, moduleList);
		this.ctx = context;
		this.moduleList = moduleList;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.module_list_row, parent, false);
	    Module m = moduleList.get(position);
	    rowView.setTag(m);
	    
	    TextView moduleTitle = (TextView) rowView.findViewById(R.id.module_title);
	    moduleTitle.setText(m.getTitle(prefs.getString(ctx.getString(R.string.prefs_language), Locale.getDefault().getLanguage())));
	    
	    ProgressBar pb = (ProgressBar) rowView.findViewById(R.id.module_progress_bar);
	    pb.setProgress((int) m.getProgress());
	    
		// set image
		if(m.getImageFile() != null){
			ImageView iv = (ImageView) rowView.findViewById(R.id.module_image);
			Bitmap bm = ImageUtils.LoadBMPsdcard(m.getImageFile(), ctx.getResources(), R.drawable.default_icon_module);
			iv.setImageBitmap(bm);
		}
	    return rowView;
	}

}
