package metro.controller.activities;

import metro.controller.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

public class PreferencesActivity extends PreferenceActivity {
	public static final String PREFS_NAME = "MCPFile";
	SharedPreferences prefs;
	PreferenceScreen screen;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		screen = getPreferenceScreen();

		screen.findPreference("teste").setOnPreferenceClickListener(
				onSelectedCityClick);

	}

	public OnPreferenceClickListener onSelectedCityClick = new OnPreferenceClickListener() {
		public boolean onPreferenceClick(Preference pref) {

			if (pref == findPreference("teste")) {
//				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//				SharedPreferences.Editor editor = settings.edit();
//				editor.putInt("cityPref", );
//				editor.commit();
				return true;
			} else {
				Log.d("ERROR",
						"onSelectCalendarsClick.run(),unknown list clicked: "
								+ pref.getTitle());
				return false;
			}
		}
	};
}
