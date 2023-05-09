package metro.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import metro.controller.activities.AgreementActivity;
import metro.controller.activities.RegisterActivity;
import metro.controller.utils.SHACheckSum;
import metro.controller.utils.SharedVars;
import metro.controller.utils.StopPackage;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.util.Installation;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EntranceActivity extends Activity implements OnClickListener {
	ProgressDialog progDialog;
	Thread progThread;
	Handler progHandler;
	ConnectivityManager conMgr;
	NetworkInfo info;
	protected static int userID = 1;

	public static int getCityID() {
		return cityID;
	}

	protected static int cityID = 1;

	public static int getModuleID() {
		return moduleID;
	}

	protected static int moduleID = 1;
	protected EditText usernameET = null;
	protected EditText passwordET = null;
	protected boolean error = false;
	protected boolean loginError = false;
	protected boolean internetError = false;
	protected SharedVars sv;
	String prefUsername = null;
	String prefPassword = null;
	boolean autologin = true;
	protected static String[] strArray;
	protected static ArrayList<StopPackage> spArrayList;

	public static String[] getStrArray() {
		return strArray;
	}

	public static void setStrArray(String[] strArray) {
		EntranceActivity.strArray = strArray;
	}

	public static void setSpArray(ArrayList<StopPackage> spArrayList) {
		EntranceActivity.spArrayList = spArrayList;
	}

	public static ArrayList<StopPackage> getSpArray() {
		return spArrayList;
	}

	public static int getUser() {
		return userID;
	}

	public static int getCity() {
		return cityID;
	}

	public static int getModule() {
		return moduleID;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(this.getClass().toString(),
				"public void onCreate(Bundle savedInstanceState)");
		super.onCreate(savedInstanceState);

		Installation.id(this);
		String line = null;

		sv = new SharedVars();
		SharedPreferences settings = getSharedPreferences(
				SharedVars.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		/*
		 * File directory = Environment.getRootDirectory(); // Assumes that a
		 * file article.rss is available on the SD card File file = new
		 * File("/sys/class/net/wlan0/address"); if (!file.exists()) {
		 * editor.putString("mac_address", "unknown"); editor.commit(); } else {
		 * Log.i(this.toString(), "Starting to read"); BufferedReader reader =
		 * null; try { reader = new BufferedReader(new FileReader(file)); line =
		 * reader.readLine(); } catch (Exception e) { e.printStackTrace(); }
		 * finally { if (reader != null) { try { reader.close();
		 * editor.putString("mac_address", line); editor.commit();
		 * Log.i("File loaded: ", line); } catch (IOException e) {
		 * e.printStackTrace(); } } } }
		 */

		/*
		 * log.debug("CHECKING PREFERENCES FILE"); autologin =
		 * settings.getBoolean("autologin", false);
		 * 
		 * log.debug("AUTO_LOGIN CHECKED: " + autologin); if (autologin) {
		 * prefUsername = settings.getString("prefun", null); prefPassword =
		 * settings.getString("prefpw", null);
		 * 
		 * autoLogin(); } else
		 */
		showAnonymousUI();
	}

	public void autoLogin() {
		Log.i(this.getClass().toString(), "public void autoLogin()");
		conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		info = conMgr.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			showDialog(0);
		} else {
			Toast.makeText(this, "ERRO: Não há conectividade à internet.",
					Toast.LENGTH_LONG).show();
		}
	}

	public void onClick(View v) {
		Log.i(this.getClass().toString(), "public void onClick(View v)");
		switch (v.getId()) {
		case R.id.textview_agree:
			Intent agreement = new Intent(getBaseContext(),
					AgreementActivity.class);
			startActivity(agreement);
			break;
		case R.id.botao_inicio:
			autoLogin();
			break;
		case R.id.LoginB:
			conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			info = conMgr.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (!autologin) {
					usernameET = (EditText) findViewById(R.id.UsernameTB);
					passwordET = (EditText) findViewById(R.id.PasswordTB);
					if (usernameET.getText().length() == 0
							|| passwordET.getText().length() == 0) {
						Toast.makeText(this,
								"Ambos os campos são necessários.",
								Toast.LENGTH_LONG).show();
						break;
					}
				}
				showDialog(0);

			} else {
				Toast.makeText(this, "ERRO: Não há conectividade à internet.",
						Toast.LENGTH_LONG).show();
				break;
			}
			break;
		case R.id.RegisterB:
			Intent registration = new Intent(getBaseContext(),
					RegisterActivity.class);
			startActivity(registration);
			break;
		case R.id.SkipB:
			conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			info = conMgr.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				showLoggedUI();
			} else {
				Toast.makeText(this, "ERRO: Não há conectividade à internet.",
						Toast.LENGTH_LONG).show();
				break;
			}
			break;
		}
	}

	private void showLogUI() {
		Log.i(this.getClass().toString(), "private void showLogUI()");
		if (userID == SharedVars.ERROR_NO_INTERNET) {
			Toast.makeText(this, "ERRO: Não há conectividade à internet.",
					Toast.LENGTH_LONG).show();
		} else if (userID == SharedVars.WRONG_LOGIN) {
			Toast.makeText(this,
					"ERRO: Nome de utilizador ou password invalido.",
					Toast.LENGTH_LONG).show();
		} else if (userID != 1) {
			Log.e("SETLOGGEDSTATE", "INVOKED SETLOGGEDSTATE METHOD.");

			// final CheckBox checkBox = (CheckBox)
			// findViewById(R.id.AutoLoginCB);
			/*
			 * if (checkBox != null && checkBox.isChecked()) { SharedPreferences
			 * settings = getSharedPreferences(PREFS_NAME, 0);
			 * SharedPreferences.Editor editor = settings.edit();
			 * editor.putBoolean("autologin", true); editor.putString("prefun",
			 * usernameET.getText().toString()); editor.putString("prefpw",
			 * passwordET.getText().toString()); editor.commit(); }
			 */
			showLoggedUI(); // 17.02.2012
		} else if (userID == 1) {
			showLoggedUI();
		}
	}

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i(this.getClass().toString(), "handleMessage(Message msg)");
			int total = msg.getData().getInt("total");
			if (total <= 0) {
				dismissDialog(0);
				((ProgressThread) progThread).setState(ProgressThread.DONE);
				removeDialog(0);
				showLogUI();
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.i(this.getClass().toString(),
				"protected Dialog onCreateDialog(int id)");
		switch (id) {
		case 0:
			progDialog = new ProgressDialog(this);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("A registar, por favor aguarde...");
			progThread = new ProgressThread(handler);
			progThread.start();
			return progDialog;
		default:
			return null;
		}
	}

	private void showLoggedUI() {
		Log.i(this.getClass().toString(), "private void showLoggedUI()");
		conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		info = conMgr.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			moduleID = 1;
			Intent beginLoggedUserActivityMetro = new Intent(getBaseContext(),
					MetroController.class);
			startActivity(beginLoggedUserActivityMetro);
			// showLoggedUI();
		} else {
			Log.i(this.getClass().toString(),
					"ERRO: Não há conectividade à internet.");
			Toast.makeText(this, "ERRO: Não há conectividade à internet.",
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		/*
		 * setContentView(R.layout.logged_entrance);
		 * 
		 * Button logoutButton = (Button) findViewById(R.id.LogoutB);
		 * logoutButton.setOnClickListener(this);
		 * 
		 * Button reportMetroButton = (Button) findViewById(R.id.ReportMetro);
		 * reportMetroButton.setOnClickListener(this);
		 * 
		 * Button reportTrainButton = (Button) findViewById(R.id.ReportTrain);
		 * reportTrainButton.setOnClickListener(this);
		 * 
		 * Button reportBusButton = (Button) findViewById(R.id.ReportBus);
		 * reportBusButton.setOnClickListener(this);
		 */
	}

	private void showAnonymousUI() {
		Log.i(this.getClass().toString(), "private void showAnonymousUI()");
		setContentView(R.layout.entrance);

		ImageView botaoInicio = (ImageView) findViewById(R.id.botao_inicio);
		botaoInicio.setOnClickListener(this);

		TextView agreement = (TextView) findViewById(R.id.textview_agree);
		agreement.setOnClickListener(this);

		/*
		 * Button registerButton = (Button) findViewById(R.id.RegisterB);
		 * registerButton.setOnClickListener(this);
		 * 
		 * Button skipButton = (Button) findViewById(R.id.SkipB);
		 * skipButton.setOnClickListener(this);
		 *//*
			 * conMgr = (ConnectivityManager)
			 * getSystemService(Context.CONNECTIVITY_SERVICE); info =
			 * conMgr.getActiveNetworkInfo(); if (info != null &&
			 * info.isConnected()) { moduleID = 1; Intent
			 * beginLoggedUserActivityMetro = new Intent(getBaseContext(),
			 * MetroController.class);
			 * startActivity(beginLoggedUserActivityMetro); // showLoggedUI(); }
			 * else { Log.i(this.getClass().toString(),
			 * "ERRO: Não há conectividade à internet."); Toast.makeText(this,
			 * "ERRO: Não há conectividade à internet.",
			 * Toast.LENGTH_LONG).show(); Intent intent = new
			 * Intent(Intent.ACTION_MAIN);
			 * intent.addCategory(Intent.CATEGORY_HOME);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * startActivity(intent); }
			 */
	}

	@Deprecated
	public int postData(String userName, String passWord) {
		Log.i(this.getClass().toString(),
				"public int postData(String userName, String passWord)");
		StringBuilder sb = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://project.vascosousa.com/mcws2.php");

			List<NameValuePair> loginInfo = new ArrayList<NameValuePair>(2);
			loginInfo.add(new BasicNameValuePair("username", userName));
			loginInfo.add(new BasicNameValuePair("password", passWord));
			httppost.setEntity(new UrlEncodedFormEntity(loginInfo));

			HttpResponse response = httpclient.execute(httppost);

			sb = inputStreamToString(response.getEntity().getContent());

		} catch (ClientProtocolException e) {
			return SharedVars.ERROR_NO_INTERNET;
		} catch (IOException e) {
			return SharedVars.ERROR_NO_INTERNET;
		}

		if (sb != null)
			if (sb.toString().compareTo("ERROR") != 0)
				return Integer.parseInt(sb.toString());
			else
				return SharedVars.WRONG_LOGIN;
		else {
			return SharedVars.ERROR_NO_INTERNET;
		}
	}

	public int postData() {
		Log.i(this.getClass().toString(), "public int postData()");
		StringBuilder sb = null;

		TelephonyManager tManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String uid = tManager.getDeviceId();
		/*
		 * SharedPreferences settings = getSharedPreferences(
		 * SharedVars.PREFS_NAME, 0); String mac_address =
		 * settings.getString("mac_address", "unknown");
		 */
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://project.vascosousa.com/mcws2.php");

			List<NameValuePair> loginInfo = new ArrayList<NameValuePair>(1);
			try {
				loginInfo.add(new BasicNameValuePair("identifier", SHACheckSum
						.Hash(android.provider.Settings.Secure.ANDROID_ID
								+ android.os.Build.SERIAL + uid)));
				Log.i(this.getClass().toString(), SHACheckSum
						.Hash(android.provider.Settings.Secure.ANDROID_ID
								+ android.os.Build.SERIAL + uid));
			} catch (Exception e) {
				Log.i(this.getClass().toString(), e.getMessage());
			}
			httppost.setEntity(new UrlEncodedFormEntity(loginInfo));

			HttpResponse response = httpclient.execute(httppost);

			sb = inputStreamToString(response.getEntity().getContent());

		} catch (ClientProtocolException e) {
			return SharedVars.ERROR_NO_INTERNET;
		} catch (IOException e) {
			return SharedVars.ERROR_NO_INTERNET;
		}

		if (sb != null) {
			Log.i(this.getClass().toString(), sb.toString());
			return Integer.parseInt(sb.toString());
		} else {
			return SharedVars.ERROR_NO_INTERNET;
		}
	}

	private StringBuilder inputStreamToString(InputStream is)
			throws IOException {
		Log.i(this.getClass().toString(),
				"private StringBuilder inputStreamToString(InputStream is) throws IOException");
		String line = "";
		StringBuilder result = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result;
	}

	private class ProgressThread extends Thread {

		final static int DONE = 0;
		final static int RUNNING = 1;

		Handler mHandler = null;
		int mState;

		ProgressThread(Handler h) {
			Log.i(this.getClass().toString(), "ProgressThread(Handler h)");
			mHandler = h;
		}

		@Override
		public void run() {
			Log.i(this.getClass().toString(), "public void run()");
			mState = RUNNING;
			while (mState == RUNNING) {
				try {
					if (autologin) {
						userID = postData();
					} else {
						userID = postData(usernameET.getText().toString(),
								passwordET.getText().toString());
					}
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
			Log.i(this.getClass().toString(),
					"public void setState(int state) " + state);
			mState = state;
		}
	}
}
