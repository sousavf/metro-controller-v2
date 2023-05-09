package metro.controller.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Stops {

	public static ArrayList<StopPackage> getStops(int module, int city) {
		Log.i("metro.controller.utils.Stops",
				"public static ArrayList<StopPackage> getStops(int module, int city)");
		String result = "";
		InputStream is = null;
		HttpPost httppost = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://project.vascosousa.com/mcws2.php?gstopcity=" + city
							+ "&gstoptype=" + module);
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
		ArrayList<StopPackage> rpArray = new ArrayList<StopPackage>();

		try {
			JSONArray jArray = new JSONArray(result);

			for (int i = 0; i < jArray.length(); i++) {
				json_data = jArray.getJSONObject(i);
				StopPackage rp = new StopPackage();

				rp.setCityDescription(json_data.getString("CityDescription"));
				rp.setCityID(json_data.getInt("CityID"));
				rp.setLatitude((float) json_data.getLong("Latitude"));
				rp.setLongitude((float) json_data.getLong("Longitude"));
				rp.setModuleDescription(json_data
						.getString("ModuleDescription"));
				rp.setCityID(json_data.getInt("ModuleID"));
				rp.setStopDescription(json_data.getString("StopDescription"));
				rp.setStopID(json_data.getInt("StopID"));

				if (json_data.getInt("LinhaAmarela") == 1)
					rp.setLinhaAmarela(true);
				else
					rp.setLinhaAmarela(false);

				if (json_data.getInt("LinhaAzul") == 1)
					rp.setLinhaAzul(true);
				else
					rp.setLinhaAzul(false);

				if (json_data.getInt("LinhaLaranja") == 1)
					rp.setLinhaLaranja(true);
				else
					rp.setLinhaLaranja(false);

				if (json_data.getInt("LinhaLilas") == 1)
					rp.setLinhaLilas(true);
				else
					rp.setLinhaLilas(false);

				if (json_data.getInt("LinhaVerde") == 1)
					rp.setLinhaVerde(true);
				else
					rp.setLinhaVerde(false);

				if (json_data.getInt("LinhaVermelha") == 1)
					rp.setLinhaVermelha(true);
				else
					rp.setLinhaVermelha(false);

				rpArray.add(rp);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return rpArray;
	}
}
