package metro.controller.activities;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import metro.controller.EntranceActivity;
import metro.controller.R;
import metro.controller.utils.StopPackage;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends ListActivity {

	private StopAdapter s_adapter;
	private ArrayList<StopPackage> spList;
	private static AlertDialog alertDialog = null;

	static class ViewHolder {
		TextView text;
		ImageView linha_azul;
		ImageView linha_amarela;
		ImageView linha_verde;
		ImageView linha_vermelha;
		ImageView linha_lilas;
		ImageView linha_laranja;
		StopPackage stop;
	}

	private class StopAdapter extends ArrayAdapter<StopPackage> {
		// ADDED
		// private ListActivity activity;
		// private int resource;
		// END ADDED
		private ArrayList<StopPackage> items;

		public StopAdapter(Context context, int textViewResourceId,
				ArrayList<StopPackage> items) {
			super(context, textViewResourceId, items);
			Log.i(this.getClass().toString(),
					"public StopAdapter(Context context, int textViewResourceId, ArrayList<StopPackage> items)");
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(this.getClass().toString(),
					"public View getView(int position, View convertView, ViewGroup parent)");
			final ViewHolder holder;
			View v = convertView;
			// ViewHolder holder;
			StopPackage o = items.get(position);
			Log.i(this.getClass().toString(), String.valueOf(position));
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_stop, null);
				holder = new ViewHolder();
				Log.i(this.getClass().toString(), String.valueOf(position));

				holder.text = (TextView) v.findViewById(R.id.toptext);
				holder.linha_amarela = (ImageView) v.findViewById(R.id.stop_d);
				holder.linha_azul = (ImageView) v.findViewById(R.id.stop_a);
				holder.linha_laranja = (ImageView) v.findViewById(R.id.stop_f);
				holder.linha_lilas = (ImageView) v.findViewById(R.id.stop_e);
				holder.linha_verde = (ImageView) v.findViewById(R.id.stop_c);
				holder.linha_vermelha = (ImageView) v.findViewById(R.id.stop_b);
				holder.stop = o;
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			if (holder.stop != null) {

				if (holder.linha_azul != null) {
					holder.linha_azul.setTag(getItem(position).getStopID());
					if (!getItem(position).isLinhaAzul())
						holder.linha_azul
								.setVisibility(android.view.View.INVISIBLE);
					else
						holder.linha_azul
								.setVisibility(android.view.View.VISIBLE);
				}

				if (holder.linha_vermelha != null) {
					holder.linha_vermelha.setTag(getItem(position).getStopID());
					if (!getItem(position).isLinhaVermelha())
						holder.linha_vermelha
								.setVisibility(android.view.View.INVISIBLE);
					else
						holder.linha_vermelha
								.setVisibility(android.view.View.VISIBLE);
				}

				if (holder.linha_verde != null) {
					holder.linha_verde.setTag(getItem(position).getStopID());
					if (!getItem(position).isLinhaVerde())
						holder.linha_verde
								.setVisibility(android.view.View.INVISIBLE);
					else
						holder.linha_verde
								.setVisibility(android.view.View.VISIBLE);
				}

				if (holder.linha_amarela != null) {
					holder.linha_amarela.setTag(getItem(position).getStopID());
					if (!getItem(position).isLinhaAmarela())
						holder.linha_amarela
								.setVisibility(android.view.View.INVISIBLE);
					else
						holder.linha_amarela
								.setVisibility(android.view.View.VISIBLE);
				}

				if (holder.linha_lilas != null) {
					holder.linha_lilas.setTag(getItem(position).getStopID());
					if (!getItem(position).isLinhaLilas())
						holder.linha_lilas
								.setVisibility(android.view.View.INVISIBLE);
					else
						holder.linha_lilas
								.setVisibility(android.view.View.VISIBLE);
				}

				if (holder.linha_laranja != null) {
					holder.linha_laranja.setTag(getItem(position).getStopID());
					if (!getItem(position).isLinhaLaranja())
						holder.linha_laranja
								.setVisibility(android.view.View.INVISIBLE);
					else
						holder.linha_laranja
								.setVisibility(android.view.View.VISIBLE);
				}
				if (holder.text != null) {
					holder.text.setText(getItem(position).getStopDescription());
					holder.text.setTag(getItem(position).getStopID());
				}
				if (v != null) {
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							final int stopID = (Integer) holder.text.getTag();
							final int city = EntranceActivity.getCity();
							final int module = EntranceActivity.getModule();
							final int user = EntranceActivity.getUser();
							final GregorianCalendar just_now = new GregorianCalendar();
							alertDialog.setTitle("Reportar");
							alertDialog
									.setMessage("Os controladores estão na paragem: "
											+ (holder.text.getText() + "?"));
							alertDialog.setButton("Sim",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											postData(stopID, city, module, user, just_now.getTimeInMillis());
										}
									});

							alertDialog.setButton2("Não",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											return;
										}
									});

							alertDialog.show();

						};
					});
				}
			}
			/*
			 * final OnClickListener itemListener = new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { LinearLayout ll =
			 * (LinearLayout) v.getParent(); TextView tv = (TextView)
			 * ll.getChildAt(0); Integer pos = (Integer) tv.getTag(); //
			 * main.makeInfo(pos); ((ReportActivity)
			 * activity).testListener(pos); } };
			 */
			// convertView.tt.setOnClickListener(itemListener);

			return v;
		}
	}

	public void testListener(int pos) {
		Log.i("testListener", "=" + pos);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(this.getClass().toString(),
				"public void onCreate(Bundle savedInstanceState)");
		super.onCreate(savedInstanceState);

		spList = EntranceActivity.getSpArray();

		this.s_adapter = new StopAdapter(this, R.layout.list_stop, spList);
		setListAdapter(new StopAdapter(getApplicationContext(),
				R.layout.list_stop, spList));

		alertDialog = new AlertDialog.Builder(this).create();
	}

	public void postData(int stopID, int cityid, int moduleid, int userId, long timestamp) {
		Log.i(this.getClass().toString(),
				"public void postData(int stopID, int cityid, int moduleid, int userId)");
		HttpClient httpclient = new DefaultHttpClient();

		URI uri = null;
		String auxiliar = "http://project.vascosousa.com/mcws2.php?stopid="
				+ stopID + "&cityid=" + cityid + "&moduleid=" + moduleid
				+ "&userid=" + userId + "&timestamp=" + timestamp;
		try {
			uri = new URI(auxiliar.replace(" ", "%20"));
		} catch (Exception e) {
			Log.getStackTraceString(e);
			Toast.makeText(this, "Sem acesso à internet.", Toast.LENGTH_LONG);
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