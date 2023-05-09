//package metro.controller.utils;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//
//public class LoginTask extends AsyncTask<String, Integer, Boolean> {
//	@Override
//	protected Boolean doInBackground(String... params) {
//		try {
//			Thread.sleep(4000); // Do your real work here
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		return Boolean.TRUE; // Return your real result here
//	}
//
//	@Override
//	protected void onPreExecute() {
//		showDialog(AUTHORIZING_DIALOG);
//	}
//
//	@Override
//	protected void onPostExecute(Boolean result) {
//		// result is the value returned from doInBackground
//		removeDialog(AUTHORIZING_DIALOG);
//		Intent i = new Intent(HelloAndroid.this, LandingActivity.class);
//		startActivity(i);
//	}
//}
