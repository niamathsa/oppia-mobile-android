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

import java.io.File;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {
	
	public static final String TAG = ImageUtils.class.getSimpleName();
	
	public static Bitmap LoadBMPsdcard(String path, Resources res, int defaultImageResource){  
        File imageFile = new File(path);  
        //if the file exists  
        if(imageFile.exists()) {  
            //load the bitmap from the given path  
            return BitmapFactory.decodeFile(path);  
        } else {  
            //return the standard 'Image not found' bitmap placed on the res folder  
            return BitmapFactory.decodeResource(res, defaultImageResource);  
        }  
    }  

}
