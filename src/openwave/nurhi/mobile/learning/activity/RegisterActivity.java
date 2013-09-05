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

import com.example.uploaddata.BaseUtill;
import com.example.uploaddata.InternetConnections;
import com.example.uploaddata.WebParser;

import openwave.nurhi.mobile.learning.application.MobileLearning;
import openwave.nurhi.mobile.learning.listener.SubmitListener;
import openwave.nurhi.mobile.learning.model.User;
import openwave.nurhi.mobile.learning.task.Payload;
import openwave.nurhi.mobile.learning.task.RegisterTask;

import openwave.nurhi.mobile.learning.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends AppActivity implements SubmitListener {

	public static final String TAG = RegisterActivity.class.getSimpleName();

	private static final int ONCLICK_TASK_NULL = 0;
	private static final int ONCLICK_TASK_REGISTERED = 10;
	public static String Usernamemain = "";

	private SharedPreferences prefs;

	private ProgressDialog pDialog;

	RadioGroup radiogroup1, radiogroup2, radiogroup3, radiogroup4, radiogroup5,
			radiogroup6, radiogroup7;
	CheckBox checkbox1, checkbox2, checkbox3, checkBox4, checkBox14,
			checkBox15, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9,
			checkBox10;

	EditText edttext01, edttext1, edttext2, edttext3, edttext4, edttext5,
			edttext6, edttext7, edttext8, edt_firstname, edt_lastname;

	private String strWorkCity = "", strProfTraining = "", strCity = "",
			strCountry = "", strWorkFacility = "", strFacilityManual = "",
			strNameFacility = "", strLocFacility = "", strTypeOfStaff = "",
			strTypeOfStaffManual = "", strReceivedTraining = "",
			strNuhriTraining = "", strFamPlanMethods = "", strEduAttended = "",
			strReligion = "", strSex = "", strAge = "", strTraining221 = "",
			strTraining222 = "" , strTraining21 = "";
 

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

	private Button btnRegisterSubmit;
	private Button btnRegisterCancel;

	private String strRegisterUserName;
	private String strFirstName = "";
	private String strLastName = "";
	private String StrRegisterPhoneNumber;
	private String StrRegisterEmailId;
	private String StrStateNew;
	private String StrRegisterPassword;
	private String StrRegisterConfirmPassword;

	private ArrayList<String> listEdtTxtValue;
	private ArrayList<String> listAlertValue;
	Context context;
	private boolean validateEmail;
	private boolean validatEmailValue;
	private boolean validatePassword = false;
	private boolean validateResult = false;

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
		edtStateNew = (EditText) findViewById(R.id.edtstatenew);
		edt_lastname = (EditText) findViewById(R.id.edt_lastname);
		// InputFilter filter = new InputFilter() {
		//
		// @Override
		// public CharSequence filter(CharSequence source, int start, int end,
		// Spanned dest, int dstart, int dend) {
		// // TODO Auto-generated method stub
		//
		//
		// for (int i = start; i < end; i++) {
		// if ( !Character.isLetterOrDigit(source.charAt(i)) ||
		// !Character.toString(source.charAt(i)) .equals("_") ||
		// !Character.toString(source.charAt(i)) .equals("-")) {
		// return "";
		// }
		// }
		// return null;
		// }
		// };

		listEdtTxtValue = new ArrayList<String>();
		listAlertValue = new ArrayList<String>();

		// btnRegisterCancel = (Button) findViewById(R.id.btn_register_cancel);
		// btnRegisterCancel.setOnClickListener(this);
		//
		btnRegisterSubmit = (Button) findViewById(R.id.btnsubmit);

		context = RegisterActivity.this;

		checkbox1 = (CheckBox) findViewById(R.id.checkBox1);
		checkbox2 = (CheckBox) findViewById(R.id.checkBox2);
		checkbox3 = (CheckBox) findViewById(R.id.checkBox3);
		checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
		checkBox14 = (CheckBox) findViewById(R.id.checkBox14);
		checkBox15 = (CheckBox) findViewById(R.id.checkBox15);
		checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
		checkBox6 = (CheckBox) findViewById(R.id.checkBox6);
		checkBox7 = (CheckBox) findViewById(R.id.checkBox7);
		checkBox8 = (CheckBox) findViewById(R.id.checkBox8);
		checkBox9 = (CheckBox) findViewById(R.id.checkBox9);
		checkBox10 = (CheckBox) findViewById(R.id.checkBox10);

		radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
		radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);

		radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);
		radiogroup6 = (RadioGroup) findViewById(R.id.radioGroup6);
		radiogroup7 = (RadioGroup) findViewById(R.id.radioGroup7);

		edttext01 = (EditText) findViewById(R.id.editText01);
		edttext1 = (EditText) findViewById(R.id.editText1);
		edttext2 = (EditText) findViewById(R.id.editText2);
		edttext3 = (EditText) findViewById(R.id.editText3);
		edttext4 = (EditText) findViewById(R.id.editText4);
		edttext5 = (EditText) findViewById(R.id.editText5);
		edttext6 = (EditText) findViewById(R.id.editText6);
		edttext7 = (EditText) findViewById(R.id.editText7);
		edttext8 = (EditText) findViewById(R.id.editText8);

		/* Register Activity Start */

		edtRegisterConfirmPassword = (EditText) findViewById(R.id.edt_register_confirm_password);
		edtRegisterEmailId = (EditText) findViewById(R.id.edt_register_email_id);
		edtRegisterPassword = (EditText) findViewById(R.id.edt_register_password);
		edtRegisterPhoneNumber = (EditText) findViewById(R.id.edt_register_phone_number);
		edtRegisterUserName = (EditText) findViewById(R.id.edt_register_user_name);

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
		//
		// edtRegisterUserName.setFilters(new InputFilter[] {
		// new InputFilter() {
		// public CharSequence filter(CharSequence src, int start,
		// int end, Spanned dst, int dstart, int dend) {
		//
		// if(src.equals("")){ // for backspace
		// return src;
		// }
		// if(src.toString().matches("[a-zA-Z0-9 ]")) //put your constraints
		// here
		// {
		// return src;
		// }
		// return "";
		// }
		// }
		// });

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
		// edtRegisterUserName
		// .setFilters(new InputFilter[] { alphaNumericFilter });
		// edtRegisterConfirmPassword
		// .setFilters(new InputFilter[] { alphaNumericFilter });
		// edtRegisterPassword
		// .setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext01.setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext1.setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext2.setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext3.setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext4.setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext5.setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext6.setFilters(new InputFilter[] { alphaNumericFilter });
		// edttext7.setFilters(new InputFilter[] { alphaNumericFilter });
		//
		// // .setFilters(new InputFilter[] { alphaNumericFilter });
		//
		// edtRegisterPhoneNumber.setFilters(new InputFilter[] { number });
		// edttext8.setFilters(new InputFilter[] { number });

		listEdtTxtValue = new ArrayList<String>();
		listAlertValue = new ArrayList<String>();

		// btnRegisterCancel = (Button) findViewById(R.id.btn_register_cancel);
		// btnRegisterCancel.setOnClickListener(this);

		checkbox1
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkbox2
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkbox3
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkBox4
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkBox14
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strTraining221 = (String) buttonView.getText();
						} else {
							strTraining221 = "";
						}
					}
				});

		checkBox15
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strTraining222 = (String) buttonView.getText();
						} else {
							strTraining222 = "";
						}
					}
				});

		checkBox5
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkBox6
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkBox7
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkBox8
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkBox9
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
						}
					}
				});

		checkBox10
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							strFamPlanMethods = (String) buttonView.getText();
						} else {
							strFamPlanMethods = "";
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
				System.out.println("******" + b.getText());

			}
		});

		radiogroup2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				int id = radiogroup2.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				strWorkFacility = (String) b.getText();
				System.out.println("***" + b.getText());

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
				strReceivedTraining = (String) b.getText();
				System.out.println("***" + b.getText());
			}
		});

		radiogroup5.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int id = radiogroup5.getCheckedRadioButtonId();
				RadioButton b = (RadioButton) findViewById(id);
				strEduAttended = (String) b.getText();
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
					u.getUsername()); // edttext01.getText().toString() chnage
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
		StrStateNew = edtStateNew.getText().toString().trim();
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

		if (validateResult == true) {

			strProfTraining = edttext01.getText().toString();
			strCity = edttext1.getText().toString();

			strCountry = edttext2.getText().toString();
			strFacilityManual = edttext3.getText().toString();
			strNameFacility = edttext4.getText().toString();
			strLocFacility = edttext5.getText().toString();
			strTypeOfStaffManual = edttext6.getText().toString();

			strNuhriTraining = edttext7.getText().toString();
			strAge = edttext8.getText().toString();

			Log.d("Guna ", "First Name " + strFirstName);
			Log.d("Guna ", "Last Name " + strLastName);

			Log.d("", "strWorkCity " + strWorkCity);
			Log.d("", "strProfTraining " + strProfTraining);

			Log.d("", "strCity " + strCity);
			Log.d("", "strCountry " + strCountry);
			Log.d("", "strWorkFacility " + strWorkFacility);
			Log.d("", "strFacilityManual " + strFacilityManual);
			Log.d("", "strNameFacility " + strNameFacility);
			Log.d("", "strLocFacility " + strLocFacility);
			Log.d("", "strTypeOfStaff " + strTypeOfStaff);
			Log.d("", "strTypeOfStaffManual " + strTypeOfStaffManual);
			Log.d("", "strReceivedTraining " + strReceivedTraining);
			Log.d("", "strNuhriTraining " + strNuhriTraining);
			Log.d("", "strFamPlanMethods " + strFamPlanMethods);
			Log.d("", "strEduAttended " + strEduAttended);
			Log.d("", "strReligion " + strReligion);
			Log.d("", "strSex " + strSex);
			Log.d("", "strAge " + strAge);

			if (strFirstName.length() < 3) {
				showToast("First Name atleast 3 character");
			} else if (strLastName.length() < 3) {
				showToast("Last Name atleast 3 character");
			}

			else if (strWorkCity.equalsIgnoreCase("")) {
				showToast("Please select currently work in");
			} else if (StrRegisterConfirmPassword.length() < 6) {
				showToast("Password atleast 6 character");
			} else if (StrStateNew.equalsIgnoreCase("")) {
				showToast("Please enter your State");
			} else if (strRegisterUserName.length() < 6) {
				showToast("User Name atleast 6 character");
			} else if (strProfTraining.equalsIgnoreCase("")) {
				showToast("Please enter training City");
			} else if (strCity.equalsIgnoreCase("")) {
				showToast("Please enter your City");
			} else if (strCountry.equalsIgnoreCase("")) {
				showToast("Please enter your Country");
			} else if (strWorkFacility.equalsIgnoreCase("")
					&& strFacilityManual.equalsIgnoreCase("")) {
				showToast("Please select facility work at currently");
			} else if (strNameFacility.equalsIgnoreCase("")) {
				showToast("Please enter name of facility currently work");
			} else if (strLocFacility.equalsIgnoreCase("")) {
				showToast("Please enter location of the facility");
			} else if (strTypeOfStaff.equalsIgnoreCase("")
					&& strTypeOfStaffManual.equalsIgnoreCase("")) {
				showToast("Please select type of Staff");
			} else if (strReceivedTraining.equalsIgnoreCase("")) {
				showToast("Please select, Have you received any training for family planning?");
			} else if (strNuhriTraining.equalsIgnoreCase("")) {
				showToast("Please enter training attended in NURHI");
			} else if (strFamPlanMethods.equalsIgnoreCase("")) {
				showToast("Please select family planning method");
			} else if (strEduAttended.equalsIgnoreCase("")) {
				showToast("Please select high level Education you attended");
			} else if (strReligion.equalsIgnoreCase("")) {
				showToast("Please select your religion");
			} else if (strTraining221.equalsIgnoreCase("")
					&& strTraining222.equalsIgnoreCase("")) {
				showToast("Please select your training, who provided it");
			} else if (strSex.equalsIgnoreCase("")) {
				showToast("Please select your gender");
			} else if (strAge.equalsIgnoreCase("")) {
				showToast("Please enter your age");
			} else {
				 
				if (strTraining221.equalsIgnoreCase("")
						|| strTraining222.equalsIgnoreCase("")) {
					if (strTraining221.equalsIgnoreCase("")) {
						strTraining21 = strTraining222;
					} else {
						strTraining21 = strTraining221;
					}
				}else{
					strTraining21 =  strTraining221 + " and " + strTraining222;
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
				u.settrainning2(strTraining21);
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
				u.setcountry(StrStateNew);
				u.setstate(strCountry);
				u.setWorktype(strNameFacility);
				u.setcurrentlyworking(strLocFacility);
				u.setstafftype(strTypeOfStaffManual);
				u.setfamilyplaning(strFamPlanMethods);
				u.setnurhitrainning(strNuhriTraining);
				u.seteducation(strEduAttended);
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
									+ "\ncountry:" + StrStateNew + "\nstate:"
									+ strCountry + "\nWorktype"
									+ strNameFacility + "\ncurrentlyworking:"
									+ strLocFacility + "\nstafftype"
									+ strTypeOfStaffManual + "\nfamilyplaning:"
									+ strFamPlanMethods + "\nnurhitrainning:"
									+ strNuhriTraining + "\neducation:"
									+ strEduAttended + "\nreligion:"
									+ strReligion + "\nsex:" + strSex
									+ "\ntrainning2" + strTraining21 + "\nAge"
									+ strAge);

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
