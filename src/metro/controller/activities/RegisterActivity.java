package metro.controller.activities;

import java.io.IOException;
import java.net.URI;

import metro.controller.EntranceActivity;
import metro.controller.R;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	public static final String PREFS_NAME = "MCPFile";
	ProgressDialog progDialog;
	Thread progThread;
	Handler progHandler;
	ConnectivityManager conMgr;
	NetworkInfo info;
	protected EditText usernameET = null;
	protected EditText passwordET = null;
	protected EditText confirmPasswordET = null;
	protected EditText emailET = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showUI();
	}

	private void showUI() {
		setContentView(R.layout.register);

		Button registerButton = (Button) findViewById(R.id.RegisterConfirm);
		registerButton.setOnClickListener(this);

		Button cancelButton = (Button) findViewById(R.id.cancelB);
		cancelButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.RegisterConfirm:
			conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			info = conMgr.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				usernameET = (EditText) findViewById(R.id.UidET);
				passwordET = (EditText) findViewById(R.id.PwdET);
				confirmPasswordET = (EditText) findViewById(R.id.CPwdET);
				emailET = (EditText) findViewById(R.id.emailET);
				if (usernameET.getText().length() == 0
						|| passwordET.getText().length() == 0
						|| confirmPasswordET.getText().length() == 0
						|| emailET.getText().length() == 0) {
					Toast.makeText(this, "All fields are required",
							Toast.LENGTH_LONG).show();
					break;
				}
				Editable var1 = null;
				Editable var2 = null;
				var1 = passwordET.getText();
				var2 = confirmPasswordET.getText();
				if (var1.toString().compareTo(var2.toString()) != 0) {
					Toast.makeText(this, "Passwords do not match.",
							Toast.LENGTH_LONG).show();
					break;
				}
				showDialog(0);

			} else {
				Toast.makeText(this, "ERROR: No internet connection",
						Toast.LENGTH_LONG).show();
				break;
			}
			break;
		case R.id.cancelB:
			Intent startActivity = new Intent(getBaseContext(),
					EntranceActivity.class);
			startActivity(startActivity);
			break;
		}
	}

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int total = msg.getData().getInt("total");
			if (total <= 0) {
				dismissDialog(0);
				((ProgressThread) progThread).setState(ProgressThread.DONE);
				removeDialog(0);
				completeRegistration();
			}
		}
	};

	private void completeRegistration() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("autologin", true);
		editor.putString("prefun", usernameET.getText().toString());
		editor.putString("prefpw", passwordET.getText().toString());
		editor.commit();

		Intent startActivity = new Intent(getBaseContext(),
				EntranceActivity.class);
		startActivity(startActivity);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			progDialog = new ProgressDialog(this);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("Loading, please wait...");
			progThread = new ProgressThread(handler);
			progThread.start();
			return progDialog;
		default:
			return null;
		}
	}

	private class ProgressThread extends Thread {

		final static int DONE = 0;
		final static int RUNNING = 1;

		Handler mHandler = null;
		int mState;

		ProgressThread(Handler h) {
			mHandler = h;
		}

		@Override
		public void run() {
			mState = RUNNING;
			while (mState == RUNNING) {
				try {
					postData(usernameET.getText().toString(), passwordET
							.getText().toString(), emailET.getText().toString());
				} catch (Exception e) {
					Log.e("ERROR", "Thread was Interrupted");
				}
				setState(DONE);
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt("total", 0);
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		}

		public void setState(int state) {
			mState = state;
		}
	}

	public void postData(String username, String password, String email) {
		HttpClient httpclient = new DefaultHttpClient();

		URI uri = null;
		String auxiliar = "http://project.vascosousa.com/mcwebservice.php?uid="
				+ username + "&pwd=" + password + "&ema=" + email;
		try {
			uri = new URI(auxiliar.replace(" ", "%20"));
		} catch (Exception e) {
			Log.getStackTraceString(e);
			Toast.makeText(this, "Sem acesso ˆ internet.", Toast.LENGTH_LONG);
		}

		HttpPost httppost = new HttpPost(uri);

		try {
			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			Log.getStackTraceString(e);
		} catch (IOException e) {
			Log.getStackTraceString(e);
		}
	}
}
