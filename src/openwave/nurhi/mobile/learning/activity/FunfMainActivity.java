//package openwave.nurhi.mobile.learning.activity;
//
//import openwave.nurhi.mobile.learning.R;
//import android.app.Activity;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//
//import edu.mit.media.funf.FunfManager;
//import edu.mit.media.funf.json.IJsonObject;
//import edu.mit.media.funf.pipeline.BasicPipeline;
//import edu.mit.media.funf.probe.Probe.DataListener;
//import edu.mit.media.funf.probe.builtin.SimpleLocationProbe;
//import edu.mit.media.funf.probe.builtin.WifiProbe;
//import edu.mit.media.funf.storage.NameValueDatabaseHelper;
//
//public class FunfMainActivity extends Activity implements DataListener {
//
//	public static final String PIPELINE_NAME = "default";
//	private FunfManager funfManager;
//	private BasicPipeline pipeline;
//	private WifiProbe wifiProbe;
//	private SimpleLocationProbe locationProbe;
//	private CheckBox enabledCheckbox;
//	private Button archiveButton, scanNowButton;
//	private TextView dataCountView;
//	private Handler handler;
//	private ServiceConnection funfManagerConn = new ServiceConnection() {
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			funfManager = ((FunfManager.LocalBinder) service).getManager();
//
//			Gson gson = funfManager.getGson();
//			wifiProbe = gson.fromJson(new JsonObject(), WifiProbe.class);
//			locationProbe = gson.fromJson(new JsonObject(),
//					SimpleLocationProbe.class);
//			pipeline = (BasicPipeline) funfManager
//					.getRegisteredPipeline(PIPELINE_NAME);
//			wifiProbe.registerPassiveListener(FunfMainActivity.this);
//			locationProbe.registerPassiveListener(FunfMainActivity.this);
//
//			// This checkbox enables or disables the pipeline
//			enabledCheckbox.setChecked(pipeline.isEnabled());
//			enabledCheckbox
//					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//						@Override
//						public void onCheckedChanged(CompoundButton buttonView,
//								boolean isChecked) {
//							if (funfManager != null) {
//								if (isChecked) {
//									funfManager.enablePipeline(PIPELINE_NAME);
//									pipeline = (BasicPipeline) funfManager
//											.getRegisteredPipeline(PIPELINE_NAME);
//								} else {
//									funfManager.disablePipeline(PIPELINE_NAME);
//								}
//							}
//						}
//					});
//
//			// Set UI ready to use, by enabling buttons
//			enabledCheckbox.setEnabled(true);
//			archiveButton.setEnabled(true);
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			funfManager = null;
//		}
//	};
//
//	// private static final String TOTAL_COUNT_SQL = "SELECT count(*) FROM " +
//	// NameValueDatabaseHelper.DATA_TABLE.name;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_funf_main);
//		// Check if android.hardware.telephony feature is available.
//
//		// if
//		// (getPackageManager().hasSystemFeature("android.hardware.telephony"))
//		// {
//		// Log.d("Mobile Test", "Running on phone");
//		//
//		// }
//		// // Check if android.hardware.touchscreen feature is available.
//		// else if
//		// (getPackageManager().hasSystemFeature("android.hardware.touchscreen"))
//		// {
//		// Log.d("Tablet Test",
//		// "Running on devices that don't support telphony but have a touchscreen.");
//		// } else {
//		// Log.d("TV Test", "Running on a TV!");
//		// }
//		//
//		// Log.d("",
//		// "Harware Test Tablet ==>"+getPackageManager().hasSystemFeature("android.hardware.touchscreen"));
//		// Log.d("",
//		// "Harware Test Mobile ==>"+getPackageManager().hasSystemFeature("android.hardware.telephony"));
//		// // Log.d("",
//		// "Harware Test TV"+getPackageManager().hasSystemFeature("android.hardware.touchscreen"));
//
//		scanNowButton = (Button) findViewById(R.id.scanNowButton);
//		scanNowButton.setEnabled(false);
//		scanNowButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (pipeline.isEnabled()) {
//					// Manually register the pipeline
//					wifiProbe.registerListener(pipeline);
//					locationProbe.registerListener(pipeline);
//				} else {
//					Toast.makeText(getBaseContext(),
//							"Pipeline is not enabled.", Toast.LENGTH_SHORT)
//							.show();
//				}
//			}
//		});
//
//		// Displays the count of rows in the data
//		dataCountView = (TextView) findViewById(R.id.dataCountText);
//
//		// Used to make interface changes on main thread
//		handler = new Handler();
//
//		enabledCheckbox = (CheckBox) findViewById(R.id.enabledCheckbox);
//		enabledCheckbox.setEnabled(false);
//
//		// Runs an archive if pipeline is enabled
//		archiveButton = (Button) findViewById(R.id.archiveButton);
//		archiveButton.setEnabled(false);
//		archiveButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (pipeline.isEnabled()) {
//					pipeline.onRun(BasicPipeline.ACTION_ARCHIVE, null);
//
//					// Wait 1 second for archive to finish, then refresh the UI
//					// (Note: this is kind of a hack since archiving is seamless
//					// and there are no messages when it occurs)
//					handler.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							Toast.makeText(getBaseContext(), "Archived!",
//									Toast.LENGTH_SHORT).show();
//							updateScanCount();
//						}
//					}, 1000L);
//				} else {
//					Toast.makeText(getBaseContext(),
//							"Pipeline is not enabled.", Toast.LENGTH_SHORT)
//							.show();
//				}
//			}
//		});
//
//		// Bind to the service, to create the connection with FunfManager
//		bindService(new Intent(FunfMainActivity.this, FunfManager.class),
//				funfManagerConn, BIND_AUTO_CREATE);
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.funf_main, menu);
//		return true;
//	}
//
//	private static final String TOTAL_COUNT_SQL = "SELECT count(*) FROM "
//			+ NameValueDatabaseHelper.DATA_TABLE.name;
//
//	private void updateScanCount() {
//		// Query the pipeline db for the count of rows in the data table
//		SQLiteDatabase db = pipeline.getDb();
//		Cursor mcursor = db.rawQuery(TOTAL_COUNT_SQL, null);
//		mcursor.moveToFirst();
//		final int count = mcursor.getInt(0);
//		// Update interface on main thread
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				dataCountView.setText("Data Count: " + count);
//			}
//		});
//	}
//
//	@Override
//	public void onDataCompleted(IJsonObject probeConfig, JsonElement checkpoint) {
//		updateScanCount();
//		// Re-register to keep listening after probe completes.
//		wifiProbe.registerPassiveListener(this);
//		locationProbe.registerPassiveListener(this);
//	}
//
//	public void onServiceConnected(ComponentName name, IBinder service) {
//		// …
//		scanNowButton.setEnabled(true);
//	}
//
//	@Override
//	public void onDataReceived(IJsonObject arg0, IJsonObject arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
