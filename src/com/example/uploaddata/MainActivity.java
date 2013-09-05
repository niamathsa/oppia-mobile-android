//package com.example.uploaddata;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.widget.Toast;
//
//public class MainActivity extends Activity {
//
//	public static String appurl = "http://192.9.200.11/nigerian/nigerian/api/?";
//	String file_upload = "http://192.9.200.11/nigerian/nigerian/api/commonfiles/"; // file_guna.txt
//
//	// action=usertrack&device=iphone&user_id=guna
//	public static String adduserbase64 = "action=usersadd&device=iphone&user_id=guna1234&mobile=2222&phone=3333&first_name=Allen&username=dsds&last_name=sdsdsd&password=232323&email=test@gmail.com&question_type_id=1&question_id=2&answer=west&tot_score=22&ind_score=22&password=openwave";
//	public static String addusermd5 = "action=usersadd&device=iphone&user_id=guna1234&mobile=2222&phone=3333&first_name=Allen&username=dsds&last_name=sdsdsd&password=232323&email=test@gmail.com&question_type_id=1&question_id=2&answer=west&tot_score=22&ind_score=22&password=openwave&passcode=ERT81E8213797F23651CF57E59B4BD8DA0";
//
//	public static final String app_passkey = "ERT81E8213797F23651CF57E59B4BD8DA0"; // ERT81E8213797F23651CF57E59B4BD8DA0
//
//	public static String trackerbase64 = "action=usertrack&device=iphone&user_id=guna&data=hello test data";
//	public static String trackermd5 = "action=usertrack&device=iphone&user_id=guna&data=hello test data&passcode";
//	String uploaded_file = "file_guna.txt";
//
//	String upLoadServerUri = file_upload;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		new trackertask().execute();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	class trackertask extends AsyncTask<Void, Void, Void> {
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//
//			WebParser web = new WebParser();
//
//			BaseUtill bs = new BaseUtill();
//			String base64 = bs.get_Convert_Base64(adduserbase64);
//			String md5 = bs.get_Convert_MD5key(addusermd5);
//
//			if ((InternetConnections.getInstance(MainActivity.this).isInternetAlive) == true) {
//				// ArrayList<String> as = web.get_Department_Detail(appurl,
//				// base64, md5);
//				//
//
//				// ArrayList<String> as = web.put_Favorite(appurl, base64, md5);
//
////				uploadFile("/sdcard/download/file_guna.txt");
//				
//				try{
//				upload();
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//
//			}
//
//			return null;
//		}
//	}
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	public void upload() throws Exception {
//        //Url of the server
//        String url = "http://192.9.200.11/nigerian/nigerian/api/uploadtest1.php";
//        HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost(url);
//        MultipartEntity mpEntity = new MultipartEntity();
//        //Path of the file to be uploaded
//        String filepath = "/sdcard/download/file_guna.txt";
//        File file = new File(filepath);
//        ContentBody cbFile = new FileBody(file, "image/jpeg");         
//
//        //Add the data to the multipart entity
//        mpEntity.addPart("uploadedfile", cbFile);
////        mpEntity.addPart("name", new StringBody("Test", Charset.forName("UTF-8")));
////        mpEntity.addPart("data", new StringBody("This is test report", Charset.forName("UTF-8")));
//        post.setEntity(mpEntity);
//        //Execute the post request
//        HttpResponse response1 = client.execute(post);
//        //Get the response from the server
//        HttpEntity resEntity = response1.getEntity();
//        String Response=EntityUtils.toString(resEntity);
//        Log.d("Response:", Response);
//        //Generate the array from the response
//        JSONArray jsonarray = new JSONArray("["+Response+"]");
//        JSONObject jsonobject = jsonarray.getJSONObject(0);
//        //Get the result variables from response 
//        String result = (jsonobject.getString("result"));
//        String msg = (jsonobject.getString("msg"));
//        //Close the connection
//        client.getConnectionManager().shutdown();
//    }
//	
//	
//	
//	
//	
//	
//	public int uploadFile(String sourceFileUri) {
//
//		try {
//
//			//
//			// String strCompleteUrl = strMainUrl + strUniqueUrl +
//			// strSyncUniqueId;
//
//			String path = "/mnt/sdcard/download/file_guna.txt";
//
//			HttpURLConnection connection = null;
//			DataOutputStream outputStream = null;
//			DataInputStream inputStream = null;
//
//			// String fileName =
//			// "411001142_f4655de9504f0d02f761c8c9eb67b2346fb4b6ac2";
//			// String fileName = "411001142_1234567890123";
//			// String pathToOurFile =
//			// "/data/data/com.openwave.checkineasy/files/"+fileName+".csv";
//			// String pathToOurFile =
//			// "/data/data/com.openwave.checkineasy/files/124.csv";
//
//			String urlServer = "http://192.9.200.11/nigerian/nigerian/api/uploadtest1.php";
//			// String urlServer ="http://192.9.200.11/uploadtest1.php";
//
//			// urlServer = url;
//			String lineEnd = "\r\n";
//			String twoHyphens = "--";
//			String boundary = "*****";
//			int serverResponseCode;
//			int bytesRead, bytesAvailable, bufferSize;
//			byte[] buffer;
//			int maxBufferSize = 1 * 1024 * 1024;
//
//			try {
//				FileInputStream fileInputStream = new FileInputStream(new File(
//						path));
//
//				URL url = new URL(urlServer);
//				connection = (HttpURLConnection) url.openConnection();
//
//				// Allow Inputs & Outputs
//				connection.setDoInput(true);
//				connection.setDoOutput(true);
//				connection.setUseCaches(false);
//
//				// Enable POST method
//				connection.setRequestMethod("POST");
//
//				connection.setRequestProperty("Connection", "Keep-Alive");
//				connection.setRequestProperty("Content-Type",
//						"multipart/form-data;boundary=" + boundary);
//
//				outputStream = new DataOutputStream(
//						connection.getOutputStream());
//				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//				outputStream
//						.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
//								+ path + "\"" + lineEnd);
//
//				// outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
//				// + pathToOurFile +"\"" + lineEnd);
//				// outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";file=\""
//				// + pathToOurFile +"\"" + lineEnd);
//				outputStream.writeBytes(lineEnd);
//
//				bytesAvailable = fileInputStream.available();
//				bufferSize = Math.min(bytesAvailable, maxBufferSize);
//				buffer = new byte[bufferSize];
//
//				// Read file
//				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//				System.out.println("****** guna data"+ bytesRead);
//
//				while (bytesRead > 0) {
//					outputStream.write(buffer, 0, bufferSize);
//					bytesAvailable = fileInputStream.available();
//					bufferSize = Math.min(bytesAvailable, maxBufferSize);
//					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//				}
//
//				outputStream.writeBytes(lineEnd);
//				outputStream.writeBytes(twoHyphens + boundary + twoHyphens
//						+ lineEnd);
//
//				// Responses from the server (code and message)
//				serverResponseCode = connection.getResponseCode();
//				String serverResponseMessage = connection.getURL().toString();
//
//				System.out.println("RRRRRRRRRRR*************"
//						+ serverResponseCode + "**************"
//						+ serverResponseMessage+"*****");
//
//				if (serverResponseCode == 200) {
//
//					Log.e("Success", "sucess");
//
//				} else if (serverResponseCode != 200) {
//					Log.e("Success", "fail");
//				}
//
//				fileInputStream.close();
//				outputStream.flush();
//				outputStream.close();
//
//			} catch (Exception ex) {
//
//			}
//
//			//
//			//
//			//
//			//
//			// String fileName = sourceFileUri;
//			//
//			// HttpURLConnection conn = null;
//			// DataOutputStream dos = null;
//			// String lineEnd = "\r\n";
//			// String twoHyphens = "--";
//			// String boundary = "*****";
//			// int bytesRead, bytesAvailable, bufferSize;
//			// byte[] buffer;
//			// int maxBufferSize = 1 * 1024 * 1024;
//			// File sourceFile = new File(sourceFileUri);
//			//
//			//
//			// //
//			// // if (!sourceFile.isFile()) {
//			// //
//			// // dialog.dismiss();
//			// //
//			// // Log.e("uploadFile", "Source File not exist :"
//			// // +uploadFilePath + "" + uploadFileName);
//			// //
//			// // runOnUiThread(new Runnable() {
//			// // public void run() {
//			// // messageText.setText("Source File not exist :"
//			// // +uploadFilePath + "" + uploadFileName);
//			// // }
//			// // });
//			// //
//			// // return 0;
//			// //
//			// // }
//			// // else
//			// // {
//			// try {
//			//
//			// // open a URL connection to the Servlet
//			// FileInputStream fileInputStream = new
//			// FileInputStream(sourceFile);
//			// URL url = new URL(upLoadServerUri);
//			//
//			// // Open a HTTP connection to the URL
//			// conn = (HttpURLConnection) url.openConnection();
//			// conn.setDoInput(true); // Allow Inputs
//			// conn.setDoOutput(true); // Allow Outputs
//			// conn.setUseCaches(false); // Don't use a Cached Copy
//			// conn.setRequestMethod("POST");
//			// conn.setRequestProperty("Connection", "Keep-Alive");
//			// conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//			// conn.setRequestProperty("Content-Type",
//			// "multipart/form-data;boundary=" + boundary);
//			// conn.setRequestProperty("upload_file", fileName);
//			//
//			// dos = new DataOutputStream(conn.getOutputStream());
//			//
//			// dos.writeBytes(twoHyphens + boundary + lineEnd);
//			// dos.writeBytes("Content-Disposition: form-data; name="
//			// + uploaded_file + ";filename=" + fileName + "" + lineEnd);
//			//
//			// dos.writeBytes(lineEnd);
//			//
//			// // create a buffer of maximum size
//			// bytesAvailable = fileInputStream.available();
//			//
//			// bufferSize = Math.min(bytesAvailable, maxBufferSize);
//			// buffer = new byte[bufferSize];
//			//
//			// // read file and write it into form...
//			// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//			//
//			// while (bytesRead > 0) {
//			//
//			// dos.write(buffer, 0, bufferSize);
//			// bytesAvailable = fileInputStream.available();
//			// bufferSize = Math.min(bytesAvailable, maxBufferSize);
//			// bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//			//
//			// }
//			//
//			// // send multipart form data necesssary after file data...
//			// dos.writeBytes(lineEnd);
//			// dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//			//
//			// // Responses from the server (code and message)
//			// int serverResponseCode = conn.getResponseCode();
//			// String serverResponseMessage = conn.getResponseMessage();
//			//
//			// Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
//			// + ": " + serverResponseCode);
//			//
//			// if (serverResponseCode == 200) {
//			//
//			// runOnUiThread(new Runnable() {
//			// public void run() {
//			//
//			// String msg =
//			// "File Upload Completed.\n\n See uploaded file here : \n\n"
//			// + " http://www.androidexample.com/media/uploads/"
//			// + uploaded_file;
//			//
//			//
//			// Toast.makeText(MainActivity.this,
//			// "File Upload Complete.", Toast.LENGTH_SHORT)
//			// .show();
//			// }
//			// });
//			// }
//			//
//			// // close the streams //
//			// fileInputStream.close();
//			// dos.flush();
//			// dos.close();
//
//		} catch (Exception ex) {
//
//			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
//		}
//
//		return 0;
//		//
//		// } // End else block
//	}
//}
