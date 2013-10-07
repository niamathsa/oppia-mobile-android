package openwave.nurhi.mobile.learning.activity;

import openwave.nurhi.mobile.learning.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MobileLearningLoginActivity extends Activity {

	Button login_btn1, register_btn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.registerlogin);

		register_btn1 = (Button) findViewById(R.id.register_btn1);

		login_btn1 = (Button) findViewById(R.id.login_btn1);

		register_btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MobileLearningLoginActivity.this,
						RegisterActivity.class));
				finish();
			}
		});

		login_btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MobileLearningLoginActivity.this,MobileLearningActivity.class));
				finish();

			}
		});

	}

}
