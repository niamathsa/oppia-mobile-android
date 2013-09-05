package openwave.nurhi.mobile.learning.listener;

import openwave.nurhi.mobile.learning.task.Payload;

public interface UpgradeListener {

	void upgradeComplete(Payload p);
    void upgradeProgressUpdate(String s);
}
