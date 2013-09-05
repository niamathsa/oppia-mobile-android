package openwave.nurhi.mobile.learning.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import openwave.nurhi.mobile.learning.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;


public class WriteFile {

	Context c;

	public WriteFile(Context con) {
		this.c = con;
	}

	public void CreateFile(String data) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		String usernames = prefs.getString(c.getString(R.string.prefs_username), "");
		try{
		if(usernames.equals("")==true){
			usernames = RegisterActivity.Usernamemain;
		}
		}catch(Exception e){
			e.printStackTrace();
			usernames = "onlydevice";
		}
		System.out.println("Main here need to print username////"+ usernames);
		
		try {

			
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// handle case of no SDCARD present
			} else {		
				File direct = new File(Environment.getExternalStorageDirectory()
						+ "/NigerianProjectFile");

				File direct2 = new File(Environment.getExternalStorageDirectory()
						+ "/NigerianProjectFile/documents");

				if (direct.exists() == false) {
					direct.mkdir();
				}
				
				if (direct2.exists() == false) {
					direct2.mkdir();
				}
				
				
				
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/NigerianProjectFile/documents" // folder
																		// name
						+"/file_"+usernames+".txt"); // file name

				if (file.exists() == false) {

					file.createNewFile();
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		FileOutputStream fop = null;
		File file;
		 
		try {

			file = new File(Environment.getExternalStorageDirectory()
					+ "/NigerianProjectFile/documents"  															
					+"/file_"+usernames+".txt");
 
			PrintWriter out = new PrintWriter(new FileWriter(file, true));
			 
			String seconds = DateFormat.getDateTimeInstance().format(new Date());
			out.append("Time::" + seconds + ":: \t" + data.toString() + "\n");
			System.out.println("Log " + "Time::" + seconds + "::\t\t"
					+ data.toString() + "\n");
			out.close();

			 

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
