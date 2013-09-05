package openwave.nurhi.mobile.learning.listener;

import openwave.nurhi.mobile.learning.model.DownloadProgress;
import openwave.nurhi.mobile.learning.task.Payload;


public interface UpdateScheduleListener {
	
	void updateComplete(Payload p);
    void updateProgressUpdate(DownloadProgress dp);

}
