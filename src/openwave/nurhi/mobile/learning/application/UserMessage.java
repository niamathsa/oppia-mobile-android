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

package openwave.nurhi.mobile.learning.application;

import openwave.nurhi.mobile.learning.model.MessageFeed;

import openwave.nurhi.mobile.learning.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserMessage extends LinearLayout {

	public static final String TAG = UserMessage.class.getSimpleName();

	private TextView messageTV;
	private MessageFeed mf;

	public UserMessage(Context context) {
		super(context);
	}

	public UserMessage(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
	}

	public void initUserMessage() {
		setOrientation(HORIZONTAL);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.user_message, this);
		messageTV = (TextView) findViewById(R.id.user_message);
	}

	public void stopMessages(){
		mHandler.removeCallbacks(mUpdateMessageTask);
	}
	
	public void updateUserMessages(MessageFeed mf) {
		this.mf = mf;
		mHandler.postDelayed(mUpdateMessageTask, 0);
	}

	private Handler mHandler = new Handler();
	private final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
	private final Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

	private Runnable mUpdateMessageTask = new Runnable() {
		public void run() {
			if (mf.count() > 0) {
				UserMessage.this.setVisibility(VISIBLE);

				animationFadeOut.setAnimationListener(new Animation.AnimationListener() {

					public void onAnimationStart(Animation animation) {
						// do nothing
					}

					public void onAnimationRepeat(Animation animation) {
						// do nothing
					}

					public void onAnimationEnd(Animation animation) {
						messageTV.setText(mf.getNextMessage());
						messageTV.startAnimation(animationFadeIn);
					}
				});
				
				messageTV.startAnimation(animationFadeOut);
				mHandler.postDelayed(this, 5000);
			}
		}
	};
}
