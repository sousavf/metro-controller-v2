package metro.controller;

import metro.controller.activities.BusHistoryActivity;
import metro.controller.activities.PreferencesActivity;
import metro.controller.activities.ReportActivity;
import metro.controller.utils.SharedVars;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class BusController extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	Log.i(this.getClass().toString(), "public void onCreate(Bundle savedInstanceState)");
		super.onCreate(savedInstanceState);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, BusHistoryActivity.class);
		spec = tabHost
				.newTabSpec("history")
				.setIndicator("Histórico",
						res.getDrawable(R.drawable.ic_tab_history))
				.setContent(intent);
		try {
			tabHost.addTab(spec);
		} catch (Exception e) {
			Log.getStackTraceString(e);
		}

		intent = new Intent().setClass(this, ReportActivity.class);
		spec = tabHost
				.newTabSpec("report")
				.setIndicator("Reportar",
						res.getDrawable(R.drawable.ic_tab_report))
				.setContent(intent);
		try {
			tabHost.addTab(spec);
		} catch (Exception e) {
			Log.getStackTraceString(e);
		}
		tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	Log.i(this.getClass().toString(), "public boolean onCreateOptionsMenu(Menu menu)");
		menu.add(SharedVars.OPTIONS_MENU, SharedVars.BUTTON_REFRESH, 1,
				"Actualizar a lista");
		menu.add(SharedVars.OPTIONS_MENU, SharedVars.BUTTON_PREFERENCES, 2,
				"Preferências");
		menu.add(SharedVars.OPTIONS_MENU, SharedVars.BUTTON_EXIT, 3, "Sa’r");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i(this.getClass().toString(), "public boolean onOptionsItemSelected(MenuItem item)");
		switch (item.getItemId()) {
		case SharedVars.BUTTON_REFRESH:
			restartActivity(this);
			break;
		case SharedVars.BUTTON_PREFERENCES:
			Intent settingsActivity = new Intent(getBaseContext(),
					PreferencesActivity.class);
			startActivity(settingsActivity);
			break;
		case SharedVars.BUTTON_EXIT:
			System.exit(SharedVars.BUTTON_EXIT);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static void restartActivity(TabActivity act) {
    	Log.i("BusController", "public static void restartActivity(TabActivity act)");
		Intent intent = new Intent();
		intent.setClass(act, act.getClass());
		act.startActivity(intent);
		act.finish();
	}

	protected Dialog onCreateDialog(int id) {
    	Log.i(this.getClass().toString(), "protected Dialog onCreateDialog(int id)");
		Dialog dialog;
		switch (id) {
		case SharedVars.REGISTER_DIALOG:
			dialog = showRegisterDialog();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	public Dialog showRegisterDialog() {
    	Log.i(this.getClass().toString(), "public Dialog showRegisterDialog()");
		Context mContext = getApplicationContext();
		Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.register_dialog);
		dialog.setTitle("--");

		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("texto");
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.ic_tab_report_white);

		return dialog;
	}
}