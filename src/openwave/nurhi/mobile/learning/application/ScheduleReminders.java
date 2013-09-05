package openwave.nurhi.mobile.learning.application;

import java.util.ArrayList;

import openwave.nurhi.mobile.learning.activity.ModuleIndexActivity;
import openwave.nurhi.mobile.learning.adapter.ScheduleReminderListAdapter;
import openwave.nurhi.mobile.learning.model.Module;

import openwave.nurhi.mobile.learning.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ScheduleReminders extends LinearLayout {

	public static final String TAG = ScheduleReminders.class.getSimpleName();
	private Context ctx;
	
	public ScheduleReminders(Context context) {
		super(context);
		this.ctx = context;
	}

	public ScheduleReminders(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
	}

	public void initSheduleReminders(ArrayList<openwave.nurhi.mobile.learning.model.Activity> activities) {
		setOrientation(VERTICAL);
		LinearLayout ll = (LinearLayout) findViewById(R.id.schedule_reminders);
		ll.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.schedule_reminder, this);
		if (activities.size() > 0){
			ScheduleReminders.this.setVisibility(VISIBLE);
		} else {
			ScheduleReminders.this.setVisibility(GONE);
		}
		ScheduleReminderListAdapter srla = new ScheduleReminderListAdapter(ctx, activities);
		ListView listView = (ListView) findViewById(R.id.schedule_reminder_list);
		listView.setAdapter(srla);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Module m = (Module) view.getTag(R.id.TAG_MODULE_ID);
				String digest = (String) view.getTag(R.id.TAG_ACTIVITY_DIGEST);
				Intent i = new Intent(getContext(), ModuleIndexActivity.class);
				Bundle tb = new Bundle();
				tb.putSerializable(Module.TAG, m);
				tb.putSerializable("JumpTo", digest);
				i.putExtras(tb);
				getContext().startActivity(i);
			}
		});

	}

	
}
