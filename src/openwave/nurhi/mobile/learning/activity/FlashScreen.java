package openwave.nurhi.mobile.learning.activity;

// import com.example.funfintegration.FunfMainActivity;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


import edu.mit.media.funf.FunfManager;
import edu.mit.media.funf.pipeline.BasicPipeline;
import edu.mit.media.funf.probe.builtin.SimpleLocationProbe;
import edu.mit.media.funf.probe.builtin.WifiProbe;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import openwave.nurhi.mobile.learning.R;
import openwave.nurhi.mobile.learning.activity.MobileLearningActivity.download_task;

public class FlashScreen extends Activity {

	public static final String PIPELINE_NAME = "default";
	private FunfManager funfManager;
	private BasicPipeline pipeline;
	private WifiProbe wifiProbe;
	private SimpleLocationProbe locationProbe;
	private CheckBox enabledCheckbox;
	private Button archiveButton, scanNowButton;
	private TextView dataCountView;
	private Handler handler;

	private File mainDirectory = new File(
			Environment.getExternalStorageDirectory() + "/NigerianProjectFile");

	private File direct = new File(Environment.getExternalStorageDirectory()
			+ "/NigerianProjectFile/documents");

	private File directVideo = new File(
			Environment.getExternalStorageDirectory()
					+ "/NigerianProjectFile/video");

	File dirdigitalcampus = new File(Environment.getExternalStorageDirectory()
			+ "/digitalcampus");
	File dirmedia = new File(Environment.getExternalStorageDirectory()
			+ "/digitalcampus/media");

	public static ArrayList<InputStream> input;
	public static ArrayList<InputStream> input1;

	private ArrayList<String> urlList;

	ArrayList<String> downloadPathList = new ArrayList<String>();
	Button btnllibrary;
	private ArrayList<File> fileList;

	private ArrayList<File> fileList1;

	DataOutputStream fos;
	OutputStream out;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	ArrayList<String> fileName = new ArrayList<String>();
	ArrayList<String> fileName1;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.flashscreen);

		// if (pipeline.isEnabled()) {
		// // Manually register the pipeline
		// wifiProbe.registerListener(pipeline);
		// locationProbe.registerListener(pipeline);
		// } else {
		// Toast.makeText(getBaseContext(), "Pipeline is not enabled.",
		// Toast.LENGTH_SHORT).show();
		// }

		 createLibrary();
//
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//
//				Intent intent = new Intent(FlashScreen.this,
//						MobileLearningActivity.class);
//				startActivity(intent);
//
//				finish();
//
//			}
//		}, 3000);

	}

	public void createLibrary() {

		urlList = new ArrayList<String>();

		// fileList is Used to check the Folder is Exist or not in the SD Card
		// to perform Download From Assests

		// fileName is Used to Read The File From Sdcard to Show in the APP When
		// User Click the Corresponding Button

		fileList = new ArrayList<File>();

		/* 23 */fileName.add("mec_wheel.pdf");

		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/NigerianProjectFile/documents/mec_wheel.pdf"));

		/* 24 */fileName.add("gather_clinical_poster.pdf");

		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/NigerianProjectFile/documents/gather_clinical_poster.pdf"));

		/* 25 */fileName.add("gather_cue_card_clinical.pdf");

		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/NigerianProjectFile/documents/gather_cue_card_clinical.pdf"));

		/* 26 */fileName.add("gathercue_card_non_clinical.pdf");		
		
		fileList.add(new File(
				Environment.getExternalStorageDirectory()
						+ "/NigerianProjectFile/documents/gathercue_card_non_clinical.pdf"));

		/* 27 */fileName.add("faqs.pdf");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/NigerianProjectFile/documents/faqs.pdf"));
		
		/* 277 */fileName.add("nationalservice.pdf");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/NigerianProjectFile/documents/nationalservice.pdf"));
		

		/* 2777 */fileName.add("ojtmanual.pdf");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/NigerianProjectFile/documents/ojtmanual.pdf"));
		
		

		/* 2777 */fileName.add("quickreference.pdf");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/NigerianProjectFile/documents/quickreference.pdf"));
		
		
		
		
		fileName.add("Module1Section1option2withpics.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/Module1Section1option2withpics.mp4"));

		
		fileName.add("distanceed_module_2text.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/distanceed_module_2text.mp4"));
	
		fileName.add("cs4_unsupportive_part1.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/cs4_unsupportive_part1.mp4"));
		
		fileName.add("cs4_supportive.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/cs4_supportive.mp4"));
		
		fileName.add("cs3_unsupportive.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/cs3_unsupportive.mp4"));
		
		fileName.add("cs3_supportive.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/cs3_supportive.mp4"));
		
		fileName.add("cs2_supportive.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/cs2_supportive.mp4"));
		
		fileName.add("cs2_unsupportive.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/cs2_unsupportive.mp4"));
		
		fileName.add("CS1supportive.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/CS1supportive.mp4"));
		
		fileName.add("cs1_unsupportive.mp4");
		fileList.add(new File(Environment.getExternalStorageDirectory()
				+ "/digitalcampus/media/cs1_unsupportive.mp4"));
		
	 

		fileName1 = new ArrayList<String>();
		fileList1 = new ArrayList<File>();

		try {

			input = new ArrayList<InputStream>();

			input.add(getAssets().open("mec_wheel.pdf")); // 23
			input.add(getAssets().open("gather_clinical_poster.pdf")); // 24
			input.add(getAssets().open("gather_cue_card_clinical.pdf")); // 25
			input.add(getAssets().open("gathercue_card_non_clinical.pdf")); // 26
			input.add(getAssets().open("faqs.pdf")); // 27

			input.add(getAssets().open("nationalservice.pdf")); // 277
			input.add(getAssets().open("ojtmanual.pdf")); // 2777
			input.add(getAssets().open("quickreference.pdf")); // 27777
			
			// Module1Section1option2withpics.mp4
			// distanceed_module_ 2text.mp4
			// cs4_unsupportive_part1.mp4
			// cs4_supportive.mp4
			// cs3_unsupportive.mp4
			// cs3_supportive.mp4
			// cs2_unsupportive.mp4
			// cs2_supportive.mp4
			// CS1supportive.mp4
			// cs1_unsupportive.mp4

			input.add(getAssets().open("Module1Section1option2withpics.mp4"));
			input.add(getAssets().open("distanceed_module_2text.mp4"));
			input.add(getAssets().open("cs4_unsupportive_part.mp4"));

			input.add(getAssets().open("cs4_supportive.mp4"));
			input.add(getAssets().open("cs3_unsupportive.mp4"));
			input.add(getAssets().open("cs3_supportive.mp4"));
			
			input.add(getAssets().open("cs2_supportive.mp4"));
			input.add(getAssets().open("cs2_unsupportive.mp4"));
			input.add(getAssets().open("CS1supportive.mp4"));
			
			input.add(getAssets().open("cs1_unsupportive.mp4"));

			input1 = new ArrayList<InputStream>();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new download_task().execute();
	}

	class download_task extends AsyncTask<String, String, String> {
		ProgressDialog dialog = null;
		ArrayList<String> department_data;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = ProgressDialog.show(FlashScreen.this, "",
					"Downloading file...", true);
		}

		@Override
		protected String doInBackground(String... params) {

			if (dialog.isShowing()) {

				dialog.setCancelable(false);
			}

			if (mainDirectory.exists() == false) {
				mainDirectory.mkdir();
			}

			if (direct.exists() == false) {
				direct.mkdir();
			}

			if (directVideo.exists() == false) {
				directVideo.mkdir();
			}

			if (dirdigitalcampus.exists() == false) {
				dirdigitalcampus.mkdir();
			}

			if (dirmedia.exists() == false) {
				dirmedia.mkdir();
			}

			Log.d("Error For This", "InputSize ---- >" + input.size());

			for (int i = 0; i < input.size(); i++) { // down load from asset
				// folder

				Log.v("Error For This", "i ---- >" + i);
				File f2;
				if (fileList.get(i).exists() == false) {

					if (i <= 7) {

						f2 = new File(Environment.getExternalStorageDirectory()
								+ "/NigerianProjectFile/documents");
					} else {
						f2 = new File(Environment.getExternalStorageDirectory()
								+ "/digitalcampus/media");
					}

					try {

						out = new FileOutputStream(f2 + "/" + fileName.get(i));

						byte[] buf = new byte[1024];
						int len;
						while ((len = input.get(i).read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						input.get(i).close();
						out.close();

						Log.v("Download",
								"had been Succeed for   -----------------> "
										+ fileName.get(i));

					} catch (FileNotFoundException ex) {

						Log.d("File", "Error is" + ex);

					} catch (IOException e) {
						System.out.println(e.getMessage());
					} finally {
						try {
							input.get(i).close();
							out.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else
					Log.d("Document File", "Exit  ------  "
							+ fileName.get(i).toString());

			}

			// for (int i = 0; i < input1.size(); i++) {
			// // down load from asset
			// // folder
			//
			// Log.v("Error For This", "i ---- >" + i);
			//
			// if (fileList1.get(i).exists() == false) {
			//
			// File f2 = new File(
			// Environment.getExternalStorageDirectory()
			// + "/NigerianProjectFile/video");
			//
			// try {
			//
			// out = new FileOutputStream(f2 + "/" + fileName1.get(i));
			//
			// byte[] buf = new byte[1024];
			// int len;
			// while ((len = input1.get(i).read(buf)) > 0) {
			// out.write(buf, 0, len);
			// }
			//
			// input1.get(i).close();
			// out.close();
			//
			// Log.v("Download",
			// "had been Succeed for   -----------------> "
			// + fileName.get(i));
			//
			// } catch (FileNotFoundException ex) {
			//
			// Log.d("File", "Error is" + ex);
			//
			// } catch (IOException e) {
			// System.out.println(e.getMessage());
			// } finally {
			// try {
			// input1.get(i).close();
			// out.close();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			//
			// } else
			// Log.d("Video File", "Exit  ------  "
			// + fileName1.get(i).toString());
			//
			// }

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					Intent intent = new Intent(FlashScreen.this,
							MobileLearningActivity.class);
					startActivity(intent);

					finish();

				}
			}, 3000);

			// boolean v = sharedPreferences.getBoolean("install", false);

		}
	}

}
