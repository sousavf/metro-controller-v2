package metro.controller.activities;

import metro.controller.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AgreementActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(this.getClass().toString(),
				"public void onCreate(Bundle savedInstanceState)");

		setContentView(R.layout.agreement_page);
	}

}
