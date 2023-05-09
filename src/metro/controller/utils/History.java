package metro.controller.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class History {

	public static ArrayList<ResponsePackage> getHistory(int module) {
		Log.i("metro.controller.utils.History",
				"public static ArrayList<ResponsePackage> getHistory(int module)");
		String result = "";
		InputStream is = null;
		HttpPost httppost = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			if (module == SharedVars.BUS_MODULE)
				httppost = new HttpPost(
						"http://project.vascosousa.com/mcws2.php?gmoduleid=3");
			else if (module == SharedVars.METRO_MODULE)
				httppost = new HttpPost(
						"http://project.vascosousa.com/mcws2.php?gmoduleid=1");
			else if (module == SharedVars.TRAIN_MODULE)
				httppost = new HttpPost(
						"http://project.vascosousa.com/mcws2.php?gmoduleid=2");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		JSONObject json_data = null;
		ArrayList<ResponsePackage> rpArray = new ArrayList<ResponsePackage>();
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				ResponsePackage rp = new ResponsePackage();

				try {
					Timestamp timestamp = new java.sql.Timestamp(
							json_data.getLong("ReportTime"));
					rp.setReportTime(timestamp.getTime());
				} catch (Exception e) {

				}

				rp.setCityDescription(json_data.getString("CityDescription"));
				rp.setCityID(json_data.getInt("CityID"));
				rp.setLatitude((float) json_data.getLong("Latitude"));
				rp.setLongitude((float) json_data.getLong("Longitude"));
				rp.setModuleDescription(json_data
						.getString("ModuleDescription"));
				rp.setModuleID(json_data.getInt("ModuleID"));
				rp.setStopDescription(json_data.getString("StopDescription"));
				rp.setStopID(json_data.getInt("StopID"));
				rp.setUserID(json_data.getInt("UserID"));
				rp.setUserRating(json_data.getInt("Rating"));

				rpArray.add(rp);

				Log.i("log_tag",
						"StopDescription: " + json_data.getString("StopID")
								+ ", ReportTime: "
								+ json_data.getString("ReportTime"));
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return rpArray;
	}
}
