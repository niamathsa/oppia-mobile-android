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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import openwave.nurhi.mobile.learning.activity.RegisterActivity;
import openwave.nurhi.mobile.learning.activity.WriteFile;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.listener.SubmitListener;
import openwave.nurhi.mobile.learning.model.User;
import openwave.nurhi.mobile.learning.utils.HTTPConnectionUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import openwave.nurhi.mobile.learning.R;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bugsense.trace.BugSenseHandler;

public class RegisterTask extends AsyncTask<Payload, Object, Payload> {

	public static final String TAG = RegisterTask.class.getSimpleName();

	private Context ctx;
	private SharedPreferences prefs;
	private SubmitListener mStateListener;

	public RegisterTask(Context ctx) {
		this.ctx = ctx;
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	@Override
	protected Payload doInBackground(Payload... params) {

		Payload payload = params[0];
		User u = (User) payload.getData().get(0);
		HTTPConnectionUtils client = new HTTPConnectionUtils(ctx);

		String url = prefs.getString("prefServer",
				ctx.getString(R.string.prefServerDefault))
				+ MobileLearning.REGISTER_PATH;
		System.out.println("here getting method name" + url);
		HttpPost httpPost = new HttpPost(url);
		try {

			// -------------------------------------
			// >'username': bundle.data['username'],
			// >'password': bundle.data['password'],
			// >'password_again': bundle.data['passwordagain'],
			// >'email': bundle.data['email'],
			// >'phoneno': bundle.data['phoneno'],
			// >'professional': bundle.data['professional'],
			// >'town': bundle.data['town'],
			// >'city': bundle.data['city'],
			// >'country': bundle.data['country'],
			// >'worktype': bundle.data['worktype'],
			// >'currentlyworking': bundle.data['currentlyworking'],
			// >'stafftype': bundle.data['stafftype'],
			// >'familyplaning': bundle.data['familyplaning'],
			// >'nurhitraining': bundle.data['nurhitraining'],
			// >'education': bundle.data['education'],
			// >'religion': bundle.data['religion'],
			// >'sex': bundle.data['sex'],
			// >'age': bundle.data['age'],
			// >'first_name': bundle.data['firstname'],
			// >'last_name': bundle.data['lastname']

			// update progress dialog
			publishProgress(ctx.getString(R.string.register_process));
			Log.d(TAG, "Registering... " + u.getUsername());
			// add post params
			JSONObject json = new JSONObject();
			
			
//			
//			bundle.obj.first_name = first_name
//		            bundle.obj.last_name = last_name
//		            bundle.obj.phoneno = phoneno
//		            bundle.obj.professional = professional
			
//		            bundle.obj.town = town
			
//		            bundle.obj.city = city
//		            bundle.obj.state = state
//		            bundle.obj.country = country
			
			
//		            bundle.obj.worktype = worktype
//		            bundle.obj.currentlyworking = currentlyworking
//		            bundle.obj.stafftype = stafftype
//		            bundle.obj.familyplaning = familyplaning
			
//		            bundle.obj.nurhitraining = nurhitraining
//		            bundle.obj.education = education
//		            bundle.obj.religion = religion
//		            bundle.obj.sex = sex
//		            bundle.obj.age = age
//		            bundle.obj.providedit = providedit 
//			
			
			
			
			
			
			
		/*	
			json.put("username", u.getUsername());
			json.put("password", u.getPassword());
			json.put("passwordagain", u.getPasswordAgain());
			json.put("email", u.getEmail());
		 
			json.put("firstname", u.getFirstname()); // u.getFirstname()
			json.put("lastname", u.getLastname()); // u.getLastname()
			json.put("phoneno", u.getPhone());
//			json.put("city", u.getCity());
//			 json.put("state",u.getstate());
			json.put("professional", u.getprofessional());
			json.put("town", u.gettown());
			json.put("country", u.getcountry());
			json.put("worktype", u.getWorktype());
			json.put("currentlyworking", u.getcurrentlyworking());
			json.put("stafftype", u.getstafftype());
			json.put("familyplaning", u.getfamilyplaning());
			json.put("nurhitraining", u.getnurhitrainning());
			json.put("education", u.geteducation());
			json.put("religion", u.getreligion());
			json.put("sex", u.getSex());
			json.put("age", u.getAge());
		
//			json.put("providedit", u.getTrainning2());
			
			*/
			
			
			
			json.put("username", u.getUsername());
            json.put("password", u.getPassword());
            json.put("passwordagain",u.getPasswordAgain());
            json.put("email",u.getEmail());
            //            json.put("age","100");
            json.put("firstname",u.getFirstname()); //u.getFirstname()
            json.put("lastname",u.getLastname()); //u.getLastname()
            json.put("phoneno",u.getPhone());
            json.put("city","hello city");          
            json.put("state","hello state"); 
            json.put("professional",u.getprofessional());
            json.put("town",u.gettown());
            json.put("country",u.getcountry());
            json.put("worktype",u.getWorktype());
            json.put("currentlyworking",u.getcurrentlyworking());
            json.put("stafftype",u.getstafftype());
            json.put("familyplaning",u.getfamilyplaning());
            json.put("nurhitraining",u.getnurhitrainning());
            json.put("education",u.geteducation());
            json.put("religion",u.getreligion());
            json.put("sex",u.getSex());
            json.put("age",u.getAge());
            json.put("providedit","provid edit");  //u.getTrainning2()
			
			
			
			
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httpPost.setEntity(se);

			// make request
			HttpResponse response = client.execute(httpPost);

			Log.d("Hello1 ", "" + response.toString());

			// read response
			InputStream content = response.getEntity().getContent();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content), 4096);
			String responseStr = "";
			String s = "";
			while ((s = buffer.readLine()) != null) {
				responseStr += s;
			}

			

//			try {
//
//				new WriteFile(ctx)
//						.CreateFile("User Registraion Process Responce is::"+responseStr);
//			
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			// system.out.println()
			Log.d("Hello2 ", "**" + responseStr);

			Log.d("Hello3 ", "**" + response.getEntity().toString());
			switch (response.getStatusLine().getStatusCode()) {
			case 400: // unauthorised
				payload.setResult(false);
				payload.setResultResponse(responseStr);
				break;
			case 201: // logged in
				JSONObject jsonResp = new JSONObject(responseStr);
				u.setApi_key(jsonResp.getString("api_key"));
				try {
					u.setPoints(jsonResp.getInt("points"));
					u.setBadges(jsonResp.getInt("badges"));
				} catch (JSONException e) {
					u.setPoints(0);
					u.setBadges(0);
				}
				u.setFirstname(jsonResp.getString("first_name"));
				u.setLastname(jsonResp.getString("last_name"));
				payload.setResult(true);
				payload.setResultResponse(ctx
						.getString(R.string.register_complete));
				break;
			default:
				payload.setResult(false);
				payload.setResultResponse(ctx
						.getString(R.string.error_connection));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			payload.setResult(false);
			payload.setResultResponse(ctx.getString(R.string.error_connection));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			payload.setResult(false);
			payload.setResultResponse(ctx.getString(R.string.error_connection));
		} catch (IOException e) {
			e.printStackTrace();
			payload.setResult(false);
			payload.setResultResponse(ctx.getString(R.string.error_connection));
		} catch (JSONException e) {
			BugSenseHandler.sendException(e);
			e.printStackTrace();
			payload.setResult(false);
			payload.setResultResponse(ctx
					.getString(R.string.error_processing_response));
		}
		return payload;
	}

	@Override
	protected void onPostExecute(Payload response) {
		synchronized (this) {
			if (mStateListener != null) {
				mStateListener.submitComplete(response);
			}
		}
	}

	public void setLoginListener(SubmitListener srl) {
		synchronized (this) {
			mStateListener = srl;
		}
	}
}
