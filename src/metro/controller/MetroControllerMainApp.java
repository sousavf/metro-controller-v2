package metro.controller;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.util.Log;

@ReportsCrashes(formKey = "dHRGejBPemVlOU1DRmJyN2dYWmFTU2c6MQ", mode = ReportingInteractionMode.TOAST,
forceCloseDialogAfterToast = false, // optional, default false
resToastText = R.string.crash_toast_text)
public class MetroControllerMainApp extends Application {
	@Override
	public void onCreate() {
		Log.i(this.getClass().toString(),
				"public void onCreate()");
		ACRA.init(this);
		super.onCreate();
	}
}
