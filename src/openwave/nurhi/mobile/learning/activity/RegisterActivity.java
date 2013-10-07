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

package openwave.nurhi.mobile.learning.activity;

import java.util.ArrayList;

import openwave.nurhi.mobile.learning.R;
import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.listener.SubmitListener;
import openwave.nurhi.mobile.learning.model.User;
import openwave.nurhi.mobile.learning.task.Payload;
import openwave.nurhi.mobile.learning.task.RegisterTask;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uploaddata.BaseUtill;
import com.example.uploaddata.InternetConnections;
import com.example.uploaddata.WebParser;

public class RegisterActivity extends AppActivity implements SubmitListener {

	public static final String TAG = RegisterActivity.class.getSimpleName();

	private static final int ONCLICK_TASK_NULL = 0;
	private static final int ONCLICK_TASK_REGISTERED = 10;
	public static String Usernamemain = "";
	ArrayList<String> arraystate = new ArrayList<String>();
	private SharedPreferences prefs;

	private ProgressDialog pDialog;
	// editText3 g2 // edt_type_of_staff_are_you g3
	RadioGroup radiogroup1, radiogroup2, radiogroup3, radiogroup4, radiogroup5,
			radiogroup6, radiogroup7;
	CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkBox14,
			checkBox15, checkbox5, checkbox6, checkbox7, checkbox8, checkbox9,
			checkbox10;
	Spinner edt_states;
	EditText edt_your_professiona_training, edt_country,
			edt_work_at_currently_spc, edt_location_of_the_facility,
			edt_type_of_staff_are_you,
			edt_how_many_times_artend_nurhi_training, edt_age, edt_firstname,
			edt_lastname;

	private String strWorkCity = "", strProfTraining = "", strCity = "",
			strCountry = "", strWorkFacility = "",
			str_work_at_currently_spc = "", strFacilityManual = "",
			str_facility_where_you_currently_work = "",
			str_location_of_the_facility = "", strTypeOfStaff = "",
			strTypeOfStaffManual = "", str_training_for_family_planning = "",
			strNuhriTraining = "", strFamPlanMethods = "",
			str_higest_level_of_education = "", strReligion = "", strSex = "",
			strAge = "", str_training_provide_nurhi = "",
			str_training_provide_other = "",
			str_training_for_family_plaining = "";

	int pos;
	SQLiteDatabase sampleDB;
	private final String DB_NAME = "nurhi";
	private final String TABLE_NAME = "profile_details";

	private EditText edtRegisterUserName;
	private EditText edtRegisterPhoneNumber;
	private EditText edtRegisterEmailId;
	private EditText edtRegisterPassword;
	private EditText edtRegisterConfirmPassword;
	private EditText edtStateNew;
	EditText edt_city;
	AutoCompleteTextView edt_facility_where_you_currently_work;
	private Button btnRegisterSubmit;
	private Button btnRegisterCancel;

	private String strRegisterUserName;
	private String strFirstName = "";
	private String strLastName = "";
	private String StrRegisterPhoneNumber;
	private String StrRegisterEmailId;
	private String str_state;
	private String StrRegisterPassword;
	private String StrRegisterConfirmPassword;

	private ArrayList<String> listEdtTxtValue;
	private ArrayList<String> listAlertValue;
	Context context;
	private boolean validateEmail;
	private boolean validatEmailValue;
	private boolean validatePassword = false;
	private boolean validateResult = false;
	ArrayList<String> currentlyworkings = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		this.drawHeader();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		edtRegisterConfirmPassword = (EditText) findViewById(R.id.edt_register_confirm_password);
		edtRegisterEmailId = (EditText) findViewById(R.id.edt_register_email_id);
		edtRegisterPassword = (EditText) findViewById(R.id.edt_register_password);
		edtRegisterPhoneNumber = (EditText) findViewById(R.id.edt_register_phone_number);
		edtRegisterUserName = (EditText) findViewById(R.id.edt_register_user_name);
		edt_firstname = (EditText) findViewById(R.id.edt_firstname);
 
		edt_lastname = (EditText) findViewById(R.id.edt_lastname);

		currentlyworkings = new ArrayList<String>();
		currentlyworkings.add("Karshi General Hospital");
		currentlyworkings.add("Bwari General Hospital");
		currentlyworkings.add("Gwarinpa General Hospital");
		currentlyworkings.add("Asokoro General Hospital");
		currentlyworkings.add("Maitama Distric Hospital");
		currentlyworkings.add("WUSE General Hospital");
		currentlyworkings.add("Kubwa General Hospital");
		currentlyworkings.add("Nyanya General Hospital");
		currentlyworkings.add("Family Health Clinic");
		currentlyworkings.add("Mpape PHC");
		currentlyworkings.add("University College Hospital");
		currentlyworkings.add("Adeoyo Teach Mat Hospital");
		currentlyworkings.add("Sabo PHC");
		currentlyworkings.add("IDI-Ogunugun");
		currentlyworkings.add("Group Medical");
		currentlyworkings.add("Bashorun Phc");
		currentlyworkings.add("Sango Phc");
		currentlyworkings.add("Ayekale /Oja Lgobo");
		
		arraystate = new ArrayList<String>();
		arraystate.add("Plateau");
		arraystate.add("Delta");
		arraystate.add("Ekiti");
		arraystate.add("Borno");
		arraystate.add("FCT");
		arraystate.add("Osun");
		arraystate.add("Nasarawa");
		arraystate.add("Katsina");
		arraystate.add("Kano");
		arraystate.add("Ebonyi");
		arraystate.add("Adamawa");
		arraystate.add("zamfara");
		arraystate.add("Akwa lbom");
		arraystate.add("Taraba");
		arraystate.add("Rivers");
		arraystate.add("Enugu");
		arraystate.add("Oyo");
		arraystate.add("Lagos");
		arraystate.add("Kogi");

		 

		listEdtTxtValue = new ArrayList<String>();
		listAlertValue = new ArrayList<String>();

		btnRegisterSubmit = (Button) findViewById(R.id.btnsubmit);

		context = RegisterActivity.this;

		checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
		checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
		checkbox3 = (CheckBox) findViewById(R.id.checkbox3);
		checkbox4 = (CheckBox) findViewById(R.id.checkbox4);
		checkBox14 = (CheckBox) findViewById(R.id.checkBox14);
		checkBox15 = (CheckBox) findViewById(R.id.checkBox15);
		checkbox5 = (CheckBox) findViewById(R.id.checkbox5);
		checkbox6 = (CheckBox) findViewById(R.id.checkbox6);
		checkbox7 = (CheckBox) findViewById(R.id.checkbox7);
		checkbox8 = (CheckBox) findViewById(R.id.checkbox8);
		checkbox9 = (CheckBox) findViewById(R.id.checkbox9);
		checkbox10 = (CheckBox) findViewById(R.id.checkbox10);

		radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
		radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);

		radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);
		radiogroup6 = (RadioGroup) findViewById(R.id.radioGroup6);
		radiogroup7 = (RadioGroup) findViewById(R.id.radioGroup7);

		edt_your_professiona_training = (EditText) findViewById(R.id.edt_your_professiona_training);
		edt_facility_where_you_currently_work = (AutoCompleteTextView) findViewById(R.id.edt_facility_where_you_currently_work);
		edt_city = (EditText) findViewById(R.id.edt_city);
		edt_states = (Spinner) findViewById(R.id.edt_states);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, currentlyworkings);
		edt_facility_where_you_currently_work.setAdapter(adapter);

		ArrayAdapter<String> adapters1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arraystate);
		edt_states.setAdapter(adapters1);

		edt_country = (EditText) findViewById(R.id.edt_country); // edt_work_at_currently_spc
		edt_work_at_currently_spc = (EditText) findViewById(R.id.edt_work_at_currently_spc);
	
		edt_location_of_the_facility = (EditText) findViewById(R.id.edt_location_of_the_facility);
		edt_type_of_staff_are_you = (EditText) findViewById(R.id.edt_type_of_staff_are_you);
		edt_how_many_times_artend_nurhi_training = (EditText) findViewById(R.id.edt_how_many_times_artend_nurhi_training);
		edt_age = (EditText) findViewById(R.id.edt_age);

		/* Register Activity Start */

		InputFilter alphaNumericFilter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence arg0, int arg1, int arg2,
					Spanned arg3, int arg4, int arg5) {
				for (int k = arg1; k < arg2; k++) {
					if (!Character.isLetterOrDigit(arg0.charAt(k))
							|| Character.isSpace(arg0.charAt(k))) {
						return "";
					}
				}
				return null;
			}
		};
		InputFilter number = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence arg0, int arg1, int arg2,
					Spanned arg3, int arg4, int arg5) {
				for (int k = arg1; k < arg2; k++) {
					if (!Character.isDigit(arg0.charAt(k))) {
						return "";
					}
				}
				return null;
			}
		};

		InputFilter alphaemail = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence arg0, int arg1, int arg2,
					Spanned arg3, int arg4, int arg5) {
				for (int k = arg1; k < arg2; k++) {
					if (!Character.isLetterOrDigit(arg0.charAt(k))) {
						return "";
					}
				}
				return null;
			}
		};

		listEdtTxtValue = new ArrayList<String>();
		listAlertValue = new ArrayList<String>();

		checkBox14
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							str_training_provide_nurhi = (String) buttonView
									.getText();
						} else {
							str_training_provide_nurhi = "";
						}
					}
				});

		checkBox15
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							str_training_provide_other = (String) buttonView
									.getText();
						} else {
							str_training_provide_other = "";
						}
					}
				});

		radiogroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				int id = radiogroup1.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				strWorkCity = (String) b.getText();
				System.out.println(" What city do you currently work in ******"
						+ b.getText());

			}
		});

		radiogroup2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				int id = radiogroup2.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				strWorkFacility = (String) b.getText();
				System.out
						.println("What type of facility do you work at currently?***"
								+ b.getText());

			}
		});
		radiogroup3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int id = radiogroup3.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				strTypeOfStaff = (String) b.getText();
				System.out.println("***" + b.getText());
			}
		});

		radiogroup4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int id = radiogroup4.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				str_training_for_family_planning = (String) b.getText();
				System.out.println("***" + b.getText());
			}
		});

		radiogroup5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int id = radiogroup5.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				str_higest_level_of_education = (String) b.getText();
				System.out.println("***" + b.getText());
			}
		});
		radiogroup6.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int id = radiogroup6.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				strReligion = (String) b.getText();
				System.out.println("***" + b.getText());
			}
		});

		radiogroup7.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int id = radiogroup7.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				strSex = (String) b.getText();
				System.out.println("***" + b.getText());
			}
		});
	}

	public void showToast(String str) {
		Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
	}

	public void submitComplete(Payload response) {
		pDialog.dismiss();
		Log.d(TAG, "Login activity reports: " + response.getResultResponse());
		if (response.isResult()) {
			User u = (User) response.getData().get(0);
			// set params
			Editor editor = prefs.edit();
			editor.putString(getString(R.string.prefs_username),
					u.getUsername()); // edt_your_professiona_training.getText().toString()
										// chnage
			editor.putString(getString(R.string.prefs_api_key), u.getApi_key());
			editor.putString(getString(R.string.prefs_display_name),
					u.getDisplayName());
			editor.putInt(getString(R.string.prefs_points), u.getPoints());
			editor.putInt(getString(R.string.prefs_points), u.getBadges());
			editor.commit();

			showAlert("Register", "Registration successful",
					ONCLICK_TASK_REGISTERED);
		} else {
			showAlert("Register", response.getResultResponse(),
					ONCLICK_TASK_NULL);
		}

	}

	public void onRegisterClick(View view) {
		// get form fields

		Log.d("Entered ", "into Submit -------------------------");

		strRegisterUserName = edtRegisterUserName.getText().toString().trim();
		strFirstName = edt_firstname.getText().toString().trim();
		strLastName = edt_lastname.getText().toString().trim();
		StrRegisterEmailId = edtRegisterEmailId.getText().toString().trim();
		StrRegisterPhoneNumber = edtRegisterPhoneNumber.getText().toString()
				.trim();
		StrRegisterPassword = edtRegisterPassword.getText().toString().trim();
		StrRegisterConfirmPassword = edtRegisterConfirmPassword.getText()
				.toString().trim();
		str_state = edtStateNew.getText().toString().trim();
		listEdtTxtValue.clear();

		listEdtTxtValue.add(strRegisterUserName);
		listAlertValue.add("User Name");

		listEdtTxtValue.add(StrRegisterPhoneNumber);
		listAlertValue.add("Phone Number");

		listEdtTxtValue.add(StrRegisterEmailId);
		listAlertValue.add("Email Id");

		listEdtTxtValue.add(StrRegisterPassword);
		listAlertValue.add("Password");

		listEdtTxtValue.add(StrRegisterConfirmPassword);
		listAlertValue.add("Confirm Password");

		for (int m = 0; m < listEdtTxtValue.size(); m++) {

			if (listEdtTxtValue.get(m).contentEquals("") == true) {

				Toast.makeText(getApplicationContext(),
						listAlertValue.get(m) + " should not be empty",
						Toast.LENGTH_LONG).show();

				validateEmail = false; // This is for Email Validation

				break;
			} else
				validateEmail = true; // This is for Email Validation
		}

		if (validateEmail == true) {

			validatEmailValue = android.util.Patterns.EMAIL_ADDRESS.matcher(
					StrRegisterEmailId).matches();

			if (validatEmailValue == true)
				validatePassword = true; // This for Password Validation

			else
				Toast.makeText(getApplicationContext(),
						"Enter the valid Email ID", Toast.LENGTH_LONG).show();
		}

		if (validatePassword == true) {

			if (StrRegisterPassword.equals(StrRegisterConfirmPassword)) {
				validateResult = true;
			} else
				Toast.makeText(getApplicationContext(),
						"Password and Passord Again are mismatch",
						Toast.LENGTH_LONG).show();
		}

		strFamPlanMethods = "";

		if (checkbox1.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox1.getText().toString();
		if (checkbox2.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox2.getText().toString();
		if (checkbox3.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox3.getText().toString();
		if (checkbox4.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox4.getText().toString();
		if (checkbox5.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox5.getText().toString();
		if (checkbox6.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox6.getText().toString();
		if (checkbox7.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox7.getText().toString();
		if (checkbox8.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox8.getText().toString();
		if (checkbox9.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox9.getText().toString();
		if (checkbox10.isChecked())
			strFamPlanMethods = strFamPlanMethods + " "
					+ checkbox10.getText().toString();

		if (validateResult == true) {

			strProfTraining = edt_your_professiona_training.getText()
					.toString();

			System.out
					.println("In what city did you receive the majority of your professional training?**"
							+ strProfTraining);
			strCity = edt_city.getText().toString();
			System.out.println("city***" + strCity);

			// str_state = edt_state.getText().toString();
			System.out.println("State***" + str_state);

			strCountry = edt_country.getText().toString();

			strFacilityManual = edt_country.getText().toString();
			str_facility_where_you_currently_work = edt_facility_where_you_currently_work
					.getText().toString();
			str_location_of_the_facility = edt_location_of_the_facility
					.getText().toString();
			str_facility_where_you_currently_work = edt_facility_where_you_currently_work
					.getText().toString();
			strTypeOfStaffManual = edt_type_of_staff_are_you.getText()
					.toString();
			strNuhriTraining = edt_how_many_times_artend_nurhi_training
					.getText().toString();
			strAge = edt_age.getText().toString();

			Log.d("Guna ", "First Name " + strFirstName);
			Log.d("Guna ", "Last Name " + strLastName);

			Log.d("", "strWorkCity " + strWorkCity);
			Log.d("", "strProfTraining " + strProfTraining);

			Log.d("", "strCity " + strCity);
			Log.d("", "strCountry " + strCountry);
			Log.d("", "strWorkFacility " + strWorkFacility);
			Log.d("", "strFacilityManual " + strFacilityManual);
			Log.d("", "strNameFacility "
					+ str_facility_where_you_currently_work);
			Log.d("", "str_location_of_the_facility "
					+ str_location_of_the_facility);
			Log.d("", "strTypeOfStaff " + strTypeOfStaff);
			Log.d("", "strTypeOfStaffManual " + strTypeOfStaffManual);
			Log.d("", "str_training_for_family_planning "
					+ str_training_for_family_planning);
			Log.d("", "strNuhriTraining " + strNuhriTraining);
			Log.d("", "strFamPlanMethods " + strFamPlanMethods);
			Log.d("", "str_higest_level_of_education "
					+ str_higest_level_of_education);
			Log.d("", "strReligion " + strReligion);
			Log.d("", "strSex " + strSex);
			Log.d("", "strAge " + strAge);

			if (str_training_for_family_planning
					.equals(getString(R.string.questn_8_b))) {
				str_training_provide_nurhi = " ";
				str_training_provide_other = " ";

			}

			if (strFirstName.length() < 3) {
				showToast("First Name atleast 3 character");
			} else if (strLastName.length() < 3) {
				showToast("Last Name atleast 3 character");
			}

			else if (strWorkCity.equalsIgnoreCase("")) {
				showToast("Please select currently work in");
			} else if (StrRegisterConfirmPassword.length() < 6) {
				showToast("Password atleast 6 character");
			} else if (str_state.equalsIgnoreCase("")) {
				showToast("Please enter your State");
			} else if (strRegisterUserName.length() < 6) {
				showToast("User Name atleast 6 character");
			} else if (strProfTraining.equalsIgnoreCase("")) {
				showToast("Please enter training City");
			} else if (strCity.equalsIgnoreCase("")) {
				showToast("Please enter your City");
			} else if (strCountry.equalsIgnoreCase("")) {
				showToast("Please enter your Country");
			} /* <-------> */else if (strWorkFacility.equalsIgnoreCase("")
					|| strWorkFacility
							.equalsIgnoreCase(getString(R.string.questn_4_h))) {

				if (strWorkFacility.equalsIgnoreCase("")) {
					showToast("Please select facility work at currently");
				} else if (strWorkFacility
						.equalsIgnoreCase(getString(R.string.questn_4_h))) {
					if (edt_work_at_currently_spc.getText().toString()
							.equalsIgnoreCase("")) {
						showToast("Please select facility work at currently");
					} else {

						str_work_at_currently_spc = edt_work_at_currently_spc
								.getText().toString();
						strWorkFacility = str_work_at_currently_spc;
					}

				} /* <-------> */
			} else if (str_facility_where_you_currently_work
					.equalsIgnoreCase("")) {
				showToast("Please enter name of facility currently work");
			} else if (str_location_of_the_facility.equalsIgnoreCase("")) {
				showToast("Please enter location of the facility");
			} else if (strTypeOfStaff.equalsIgnoreCase("")
					&& strTypeOfStaffManual.equalsIgnoreCase("")) {
				showToast("Please select type of Staff");
			} else if (str_training_for_family_planning.equalsIgnoreCase("")) {
				showToast("Please select, Have you received any training for family planning?");

			} else if (strNuhriTraining.equalsIgnoreCase("")) {
				showToast("Please enter training attended in NURHI");
			} else if (strFamPlanMethods.equalsIgnoreCase("")) {
				showToast("Please select family planning method");
			} else if (str_higest_level_of_education.equalsIgnoreCase("")) {
				showToast("Please select high level Education you attended");
			} else if (strReligion.equalsIgnoreCase("")) {
				showToast("Please select your religion");
			} else if (str_training_provide_nurhi.equalsIgnoreCase("")
					&& str_training_provide_other.equalsIgnoreCase("")) {
				showToast("Please select your training, who provided it");
			} else if (strSex.equalsIgnoreCase("")) {
				showToast("Please select your gender");
			} else if (strAge.equalsIgnoreCase("")) {
				showToast("Please enter your age");
			} else {

				if (str_training_provide_nurhi.equalsIgnoreCase("")
						|| str_training_provide_other.equalsIgnoreCase("")) {
					if (str_training_provide_nurhi.equalsIgnoreCase("")) {
						str_training_for_family_plaining = str_training_provide_other;
					} else {
						str_training_for_family_plaining = str_training_provide_nurhi;
					}
				} else {
					str_training_for_family_plaining = str_training_provide_nurhi
							+ " and " + str_training_provide_other;
					System.out.println("both are success");
				}

				// showToast("all done enter into loading ----");
				Usernamemain = strRegisterUserName;
				pDialog = new ProgressDialog(this);
				pDialog.setTitle("Register");
				pDialog.setMessage("Registering...");
				pDialog.setCancelable(true);
				pDialog.show();

				ArrayList<Object> users = new ArrayList<Object>();
				User u = new User();
				u.setUsername(strRegisterUserName);
				u.setPassword(StrRegisterPassword);
				u.setPasswordAgain(StrRegisterConfirmPassword);
				u.setFirstname(strFirstName);
				u.setLastname(strLastName);
				u.setEmail(StrRegisterEmailId);
				u.settrainning2(str_training_for_family_plaining);
				if (strTypeOfStaffManual.equalsIgnoreCase("")) {

					strTypeOfStaffManual = strTypeOfStaff;
					System.out.println("must print values ****"
							+ strTypeOfStaffManual);
				} else {
					System.out.println("must printfail1");
				}

				if (strFacilityManual.equalsIgnoreCase("")) {
					strFacilityManual = strWorkFacility;
					System.out.println("must print values ****"
							+ strFacilityManual);
				} else {
					System.out.println("must print fail2");
				}

				u.setPhone(StrRegisterPhoneNumber);
				u.setcity(strWorkCity);
				u.setprofessional(strProfTraining);
				u.settown(strCity);
				u.setcountry(strCountry);
				u.setstate(str_state);
				u.setWorktype(str_facility_where_you_currently_work);
				u.setcurrentlyworking(str_location_of_the_facility);
				u.setstafftype(strTypeOfStaffManual);
				u.setfamilyplaning(strFamPlanMethods);
				u.setnurhitrainning(strNuhriTraining);
				u.seteducation(str_higest_level_of_education);
				u.setreligion(strReligion);
				u.setsex(strSex);
				// u.settrainning2(strTraining2);
				u.setAge(strAge);

				try {

					new WriteFile(RegisterActivity.this)
							.CreateFile("User Registraion Process started");
					new WriteFile(RegisterActivity.this)
							.CreateFile("\nUsername:" + strRegisterUserName
									+ "\nPassword:" + StrRegisterPassword
									+ "\nFirstname:" + strFirstName
									+ "\nLastname:" + strLastName + "\nEmail:"
									+ StrRegisterEmailId + "\ntrainning2:"
									+ strWorkFacility + "\nPhone:"
									+ StrRegisterPhoneNumber + "\ncity:"
									+ strWorkCity + "\nprofessional:"
									+ strProfTraining + "\ntown:" + strCity
									+ "\ncountry:" + strCountry + "\nstate:"
									+ str_state + "\nWorktype"
									+ str_facility_where_you_currently_work
									+ "\ncurrentlyworking:"
									+ str_location_of_the_facility
									+ "\nstafftype" + strTypeOfStaffManual
									+ "\nfamilyplaning:" + strFamPlanMethods
									+ "\nnurhitrainning:" + strNuhriTraining
									+ "\neducation:"
									+ str_higest_level_of_education
									+ "\nreligion:" + strReligion + "\nsex:"
									+ strSex + "\ntrainning2"
									+ str_training_for_family_plaining
									+ "\nAge" + strAge);

					new WriteFile(RegisterActivity.this)
							.CreateFile("User Registraion Process end");

				} catch (Exception e) {
					e.printStackTrace();
				}
				users.add(u);
				Payload p = new Payload(users);
				RegisterTask lt = new RegisterTask(this);
				lt.setLoginListener(this);
				lt.execute(p);
				// showAlert("Register", "Registration successful",
				// ONCLICK_TASK_REGISTERED);

				// = "action=usersadd&device=iphone&user_id=";
				// public static String adduserbase641 = "&mobile=";
				// public static String adduserbase642 = "&phone=";
				// public static String adduserbase643 = "&first_name=";
				// public static String adduserbase644 = "&username=";
				// public static String adduserbase645 = "&last_name=";
				// public static String adduserbase646 = "&password=";
				// public static String adduserbase647 = "&email=";
				// public static String adduserbase648 = "&question_type_id=";
				//

				new addusertask().execute(strRegisterUserName,
						StrRegisterPhoneNumber, StrRegisterPhoneNumber,
						strFirstName, strRegisterUserName, strLastName,
						StrRegisterPassword, StrRegisterEmailId, strCity);

			}
		}
	}

	private void showAlert(String title, String msg, int onClickTask) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				RegisterActivity.this);
		builder.setTitle(title);
		// String mess = msg.replace("\n\"}", "");
		// mess = mess.replace("{\"error\":\"", "");

		builder.setMessage(msg);
		switch (onClickTask) {
		case ONCLICK_TASK_NULL:
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// do nothing

						}

					});
			break;
		case ONCLICK_TASK_REGISTERED:
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// return to main activity
							RegisterActivity.this.finish();
						}

					});
			break;
		}
		builder.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent i = new Intent(this, PrefsActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class addusertask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			WebParser web = new WebParser();

			BaseUtill bs = new BaseUtill();
			String base64 = bs.get_Convert_Base64(MobileLearning.adduserbase64
					+ params[0] + MobileLearning.adduserbase641 + params[1]
					+ MobileLearning.adduserbase642 + params[2]
					+ MobileLearning.adduserbase643 + params[3]
					+ MobileLearning.adduserbase644 + params[4]
					+ MobileLearning.adduserbase645 + params[5]
					+ MobileLearning.adduserbase646 + params[6]
					+ MobileLearning.adduserbase647 + params[7]
					+ MobileLearning.adduserbase648 + params[8]
					+ MobileLearning.adduserbase649);
			String md5 = bs.get_Convert_MD5key(MobileLearning.adduserbase64
					+ params[0] + MobileLearning.adduserbase641 + params[1]
					+ MobileLearning.adduserbase642 + params[2]
					+ MobileLearning.adduserbase643 + params[3]
					+ MobileLearning.adduserbase644 + params[4]
					+ MobileLearning.adduserbase645 + params[5]
					+ MobileLearning.adduserbase646 + params[6]
					+ MobileLearning.adduserbase647 + params[7]
					+ MobileLearning.adduserbase648 + params[8]
					+ MobileLearning.adduserbase649
					+ MobileLearning.md5passcode);

			if ((InternetConnections.getInstance(RegisterActivity.this).isInternetAlive) == true) {

				try {

					web.get_Department_Detail(MobileLearning.appurl, base64,
							md5);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

	}
}
