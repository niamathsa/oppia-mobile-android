package com.example.uploaddata;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.EditText;

public class BaseUtill {
	
	
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
 
    
    public final  Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile( 
			
			"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
          +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
      );
	

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String EMAIL_MSG = "invalid email";
    private static final String PHONE_MSG = "###-#######";
 

	public String get_Convert_Base64(String data) {
		String base64String = new String(Base64.encode(data.getBytes()));
		System.out.println("Result of base64 **" + base64String);
		return base64String;
	}

	public String get_Convert_MD5key(String s) {
		String val = "";
		byte[] defaultBytes = s.getBytes();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			val = hexString + "";
			System.out.println("pass " + val + "   md5 version is "
					+ hexString.toString());
		} catch (NoSuchAlgorithmException nsae) {
		}
		return val;
	}
	
	public boolean hasConnection(Context con) {
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

//	public boolean hasConnection(Context con) {
//		ConnectivityManager cm = (ConnectivityManager) con
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//		NetworkInfo wifiNetwork = cm
//				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		if (wifiNetwork != null && wifiNetwork.isConnected()) {
//			return true;
//		}
//
//		NetworkInfo mobileNetwork = cm
//				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//		if (mobileNetwork != null && mobileNetwork.isConnected()) {
//			return true;
//		}
//
//		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//		if (activeNetwork != null && activeNetwork.isConnected()) {
//			return true;
//		}
//
//		return false;
//	}

	public ArrayList<String> orderAscDsc(ArrayList<String> staff_data, int i) {
	
			System.out.println("method reached ****"+staff_data.size());
			ArrayList<String> staff_Arrage = new ArrayList<String>();
			int kk=0;
			try {
			for (int il = 0; il < staff_data.size(); il = il + 39) {
				String s = "";
				for (int l = 0; l < 39; l++) {

					s = s + staff_data.get(il + l) + "$";
				}
				staff_Arrage.add(s);
			}

			System.out.println("stage 1 ****");
			// Collections.sort(staff_Arrage, Collections.reverseOrder());
			Collections.sort(staff_Arrage);

			staff_data = new ArrayList<String>();
			System.out.println("compressed size"+staff_Arrage.size());
			
			kk=0;
			for (int im = 0; im < staff_Arrage.size(); im++) {

				String val = staff_Arrage.get(im);

				String[] staffdata = val.split("$");
				

				for (String field : staffdata) {
//					System.out.println(field);
					staff_data.add(field);
					kk++;
				}

			}
			System.out.println("Tottal size*****"+staff_data.size());
			System.out.println("Ascending done*****");
		} catch (Exception e) {

		}
		return staff_data;

	}
	
	
	public ArrayList<String> orderAscDsc1(ArrayList<String> staff_data, int i) {
		
		System.out.println("method reached ****"+staff_data.size());
		ArrayList<String> staff_Arrage = new ArrayList<String>();
		int kk=0;
		try {
		for (int il = 0; il < staff_data.size(); il = il + i) {
			String s = "";
			for (int l = 0; l < i; l++) {

				//s = s + staff_data.get(il + l) + "$";
				s = s + staff_data.get(il + l);
			}
			staff_Arrage.add(s);
		}

		System.out.println("stage 1 ****");
		// Collections.sort(staff_Arrage, Collections.reverseOrder());
		Collections.sort(staff_Arrage);

		staff_data = new ArrayList<String>();
		System.out.println("compressed size"+staff_Arrage.size());
		
		kk=0;
		
		for (int im = 0; im < staff_Arrage.size(); im++) {

			
			String val = staff_Arrage.get(im);

			//String[] staffdata = val.split("$");
			
			String[] staffdata = val.split("$");
			

			for (String field : staffdata) {
//				System.out.println(field);
				staff_data.add(field);
				kk++;
			}

		}
		
		System.out.println("Tottal size*****"+staff_data.size());
		System.out.println("Ascending done*****");
	} catch (Exception e) {

	}
	return staff_data;

}

	

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }
 
    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }
 
    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {
 
        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);
 
        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;
 
        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };
 
        return true;
    }
 
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {
 
        String text = editText.getText().toString().trim();
        editText.setError(null);
 
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
 
        return true;
    }
	

	
	
//	
//	public static boolean isEmailValid(String email) {
//        boolean isValid = false;
//        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//        CharSequence inputStr = email;
//        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(inputStr);
//        if (matcher.matches()) {
//               isValid = true;
//        }
//        return isValid;
// }
	
	public String getUdId(Context context) {
		// This will get the Real Device ID (IMEI NO)
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String strDeviceId = TelephonyMgr.getDeviceId(); // Requires

		return strDeviceId;

	}

	public boolean isNetworkAvailable(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			// boitealerte(this.getString(R.string.alert),
			// "getSystemService rend null");

		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {

						return true;
					}
				}
			}
		}
		return false;
	}

	// Validating the Email
	public boolean isValidEmail(String strEmail) {
		final Pattern EMAIL_ADDRESS_PATTERN = Pattern
				.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
						+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
						+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

		if (EMAIL_ADDRESS_PATTERN.matcher(strEmail).matches()) {

			return true;
		} else {

			return false;
		}

	}

	//
	// public static Bitmap loadBitmap(String url) {
	// Bitmap bitmap = null;
	// InputStream in = null;
	// int IO_BUFFER_SIZE = 2000;
	// BufferedOutputStream out = null;
	//
	// try {
	// in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
	//
	// final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
	// out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
	// copy(in, out);
	// out.flush();
	//
	// final byte[] data = dataStream.toByteArray();
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// //options.inSampleSize = 1;
	//
	// bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	// } catch (IOException e) {
	// Log.e("Image Error", "Could not load Bitmap from: " + url);
	// } finally {
	// closeStream(in);
	// closeStream(out);
	// }
	//
	// return bitmap;
	// }
	// Drawable drawable_from_url(String url, String src_name) throws
	// java.net.MalformedURLException, java.io.IOException
	// {
	// return Drawable.createFromStream(((java.io.InputStream)
	// new java.net.URL(url).awagetContent()), src_name);
	// }
	
}
