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

package openwave.nurhi.mobile.learning.task;

import java.io.File;
import java.util.ArrayList;

import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.exception.InvalidXMLException;
import openwave.nurhi.mobile.learning.listener.ScanMediaListener;
import openwave.nurhi.mobile.learning.model.Media;
import openwave.nurhi.mobile.learning.model.Module;
import openwave.nurhi.mobile.learning.utils.ModuleXMLReader;


import android.os.AsyncTask;

public class ScanMediaTask extends AsyncTask<Payload, String, Payload>{

	public final static String TAG = ScanMediaTask.class.getSimpleName();
	private ScanMediaListener mStateListener;
	
	protected Payload doInBackground(Payload... params) {
		Payload payload = params[0];
		for (Object obj: payload.getData()){
			Module module = (Module) obj;
			ModuleXMLReader mxr;
			try {
				mxr = new ModuleXMLReader(module.getModuleXMLLocation());
				ArrayList<Media> media = mxr.getMedia();
				for(Media m: media){
					publishProgress(m.getFilename());
					String filename = MobileLearning.MEDIA_PATH + m.getFilename();
					File mediaFile = new File(filename);
					if(!mediaFile.exists()){
						payload.addResponseData(m);
						payload.setResult(true);
					}
				}
			} catch (InvalidXMLException e) {
				e.printStackTrace();
				payload.setResult(false);
			}
			
		}
		return payload;
	}
	
	@Override
	protected void onPreExecute(){
		synchronized (this) {
            if (mStateListener != null) {
               mStateListener.scanStart();
            }
        }
	}
	
	@Override
	protected void onProgressUpdate(String... progress){
		synchronized (this) {
            if (mStateListener != null) {
                // update progress
                mStateListener.scanProgressUpdate(progress[0]);
            }
        }
	}
	
	@Override
	protected void onPostExecute(Payload response) {
		synchronized (this) {
            if (mStateListener != null) {
               mStateListener.scanComplete(response);
            }
        }
	}
	
	public void setScanMediaListener(ScanMediaListener srl) {
        synchronized (this) {
            mStateListener = srl;
        }
    }

}
