package openwave.nurhi.mobile.learning.activity;

import java.io.File;
import openwave.nurhi.mobile.learning.R;
import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LibraryActivity extends Activity {

	private TextView txtScreenTitle;
	private ListView listViewForLib;
	private String listValues[] = new String[] { "Medical Eligibility Criteria Chart","Balanced Counseling Strategy Plus",
			"FP/RH Protocols", "OJT FP Handouts", "GATHER", "FAQs", "App for Contraceptive Eligibility" }; // "MEC",
	private String listGatherPDFs[] = new String[] { "nationalservices.pdf","pluss.pdf", 
			"quickreferences.pdf", " nationalservices.pdf",
			"gathercue_card_non_clinicals.pdf" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.menu_library_activity);

		// ArrayList<String> a = new ArrayList<String>();
		// a.get(2);

		listViewForLib = (ListView) findViewById(R.id.listViewMenuLib);
		txtScreenTitle = (TextView) findViewById(R.id.txtScreenTitle);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				listValues);
		listViewForLib.setAdapter(adapter);

		listViewForLib.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Log.e("", "position " + position);
				Log.e("", "arg3 " + arg3);
				boolean b = false;
				switch (position) {

				// case 0:
				// Intent intent = new Intent(LibraryActivity.this,
				// Second.class);
				// intent.putExtra(
				// PdfViewerActivity.EXTRA_PDFFILENAME,
				// Environment.getExternalStorageDirectory()
				// + "/NigerianProjectFile/documents/mec_wheel.pdf");
				// startActivity(intent);
				//
				// break;

				case 0:
					b = false;
					File file = new File(
							Environment.getExternalStorageDirectory()
									+ "/NigerianProjectFile/documents/quickreferences.pdf");

					if (file.exists()) {
						Uri path = Uri.fromFile(file);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							b = true;
						}
					}
					if (b == true) {
						Intent intent2 = new Intent(LibraryActivity.this,
								Second.class);
						intent2.putExtra(
								PdfViewerActivity.EXTRA_PDFFILENAME,
								Environment.getExternalStorageDirectory()
										+ "/NigerianProjectFile/documents/quickreferences.pdf");
						startActivity(intent2);
					}
					new WriteFile(LibraryActivity.this)
							.CreateFile("Visited quickreferences.pdf Libray page!!");
					break;
					

				case 1:
					b = false;
					File file11 = new File(
							Environment.getExternalStorageDirectory()
									+ "/NigerianProjectFile/documents/pluss.pdf");

					if (file11.exists()) {
						Uri path = Uri.fromFile(file11);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							b = true;
						}
					}
					if (b == true) {
						Intent intent2 = new Intent(LibraryActivity.this,
								Second.class);
						intent2.putExtra(
								PdfViewerActivity.EXTRA_PDFFILENAME,
								Environment.getExternalStorageDirectory()
										+ "/NigerianProjectFile/documents/pluss.pdf");
						startActivity(intent2);
					}
					new WriteFile(LibraryActivity.this)
							.CreateFile("Visited pluss.pdf Libray page!!");
					break;
						
					
				case 3:
					b = false;
					File file2 = new File(
							Environment.getExternalStorageDirectory()
									+ "/NigerianProjectFile/documents/ojtmanuals.pdf");

					if (file2.exists()) {
						Uri path = Uri.fromFile(file2);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							b = true;
						}
					}
					if (b == true) {
						Intent intent2 = new Intent(LibraryActivity.this,
								Second.class);
						intent2.putExtra(
								PdfViewerActivity.EXTRA_PDFFILENAME,
								Environment.getExternalStorageDirectory()
										+ "/NigerianProjectFile/documents/ojtmanuals.pdf");
						startActivity(intent2);
					}
					new WriteFile(LibraryActivity.this)
							.CreateFile("Visited ojtmanuals.pdf Libray page!!");
					break;
				case 2:
					b = false;
					File files = new File(
							Environment.getExternalStorageDirectory()
									+ "/NigerianProjectFile/documents/nationalservices.pdf");

					if (files.exists()) {
						Uri path = Uri.fromFile(files);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							b = true;
						}
					}
					if (b == true) {
						Intent intent3 = new Intent(LibraryActivity.this,
								Second.class);
						intent3.putExtra(
								PdfViewerActivity.EXTRA_PDFFILENAME,
								Environment.getExternalStorageDirectory()
										+ "/NigerianProjectFile/documents/nationalservices.pdf");
						startActivity(intent3);
					}
					new WriteFile(LibraryActivity.this)
							.CreateFile("Visited nationalservices.pdf Libray page!!");

					break;
				case 4:
					b = false;
					File filess = new File(
							Environment.getExternalStorageDirectory()
									+ "/NigerianProjectFile/documents/gathercue_card_non_clinicals.pdf");

					if (filess.exists()) {
						Uri path = Uri.fromFile(filess);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							b = true;
						}
					}
					if (b == true) {
						Intent intent4 = new Intent(LibraryActivity.this,
								Second.class);
						intent4.putExtra(
								PdfViewerActivity.EXTRA_PDFFILENAME,
								Environment.getExternalStorageDirectory()
										+ "/NigerianProjectFile/documents/gathercue_card_non_clinicals.pdf");
						startActivity(intent4);
					}
					new WriteFile(LibraryActivity.this)
							.CreateFile("Visited gathercue_card_non_clinicals.pdf Libray page!!");
					break;
				case 5:
					b = false;
					File file1 = new File(Environment
							.getExternalStorageDirectory()
							+ "/NigerianProjectFile/documents/faqss.pdf");

					if (file1.exists()) {
						Uri path = Uri.fromFile(file1);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							b = true;
						}
					}
					if (b == true) {
						Intent intent5 = new Intent(LibraryActivity.this,
								Second.class);
						intent5.putExtra(
								PdfViewerActivity.EXTRA_PDFFILENAME,
								Environment.getExternalStorageDirectory()
										+ "/NigerianProjectFile/documents/faqss.pdf");
						startActivity(intent5);
					}
					new WriteFile(LibraryActivity.this)
							.CreateFile("Visited faqss.pdf Libray page!!");

					break;
				case 6:

					boolean applanch = false;

					try {
						PackageManager pm = getPackageManager();
						pm.getApplicationInfo("org.k4health.Ace",
								pm.GET_UNINSTALLED_PACKAGES);
						Intent intent1 = pm
								.getLaunchIntentForPackage("org.k4health.Ace");
						startActivity(intent1);
						applanch = true;
						new WriteFile(LibraryActivity.this)
								.CreateFile("Visited ACE App!!");

					} catch (NameNotFoundException e) {
						e.printStackTrace();
						applanch = false;
					}

					if (applanch == false) {
						// Toast.makeText(LibrayActivity.this,
						// "app not available", Toast.LENGTH_LONG).show();
						// check internet connect
						// if yes connect app goto google play
						// else . please connect internet for app launch

						if (InternetConnections.getInstance(
								LibraryActivity.this).isNetworkAvailable() == true) {
							try {
								startActivity(new Intent(
										Intent.ACTION_VIEW,
										Uri.parse("https://play.google.com/store/apps/details?id=org.k4health.Ace&hl=en")));// market://details?id="+appName
								new WriteFile(LibraryActivity.this)
										.CreateFile("Visited Google play for install ACE App page!!");
							} catch (android.content.ActivityNotFoundException anfe) {
								// startActivity(new Intent(Intent.ACTION_VIEW,
								// Uri.parse("https://play.google.com/store/apps/details?id=org.k4health.Ace&hl=en")));//
								// http://play.google.com/store/apps/details?id="+appName
							}
						} else {
							Toast.makeText(LibraryActivity.this,
									"Please Connect your Internet",
									Toast.LENGTH_LONG).show();
							new WriteFile(LibraryActivity.this)
									.CreateFile("User without internet connection tryed the Access ACE App Download!!");
						}

						break;
					}
				}
			}
		});
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

}
