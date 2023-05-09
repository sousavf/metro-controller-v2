package metro.controller.activities;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import metro.controller.EntranceActivity;
import metro.controller.R;
import metro.controller.utils.History;
import metro.controller.utils.ResponsePackage;
import metro.controller.utils.SharedVars;
import metro.controller.utils.Stops;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MetroHistoryActivity extends ListActivity {
	protected ProgressDialog dialog;
	ProgressDialog progDialog;
	Thread progThread, ratingThread;
	Handler progHandler;
	// private ResponsePackageAdapter rp_adapter;
	private ArrayList<ResponsePackage> rpList;
	private static int rating = 0;
	private static int userId = 0;
	private static MetroHistoryActivity contextHelper = null;

	public static class ViewHolder {
		TextView tt;
		TextView mt;
		TextView bt;
		ImageView up;
		ImageView down;
	}

	public void onCreate(Bundle savedInstanceState) {
		Log.i(this.getClass().toString(),
				"public void onCreate(Bundle savedInstanceState)");
		super.onCreate(savedInstanceState);
		contextHelper = this;
		showDialog(0);

		// this.rp_adapter = new ResponsePackageAdapter(this,
		// R.layout.list_item, rpList);
		// setListAdapter(this.rp_adapter);
	}

	public class ProgressThread extends Thread {

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
					rpList = History.getHistory(SharedVars.METRO_MODULE);
					EntranceActivity.setSpArray(Stops.getStops(
							EntranceActivity.getModuleID(),
							EntranceActivity.getCityID()));
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
			Log.i(this.getClass().toString(), "public void setState(int state)");
			if (mState != 0)
				mState = state;
		}
	}

	private class ResponsePackageAdapter extends ArrayAdapter<ResponsePackage> {

		private ArrayList<ResponsePackage> items;

		public ResponsePackageAdapter(Context context, int textViewResourceId,
				ArrayList<ResponsePackage> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		/*
		 * @Override public void unregisterDataSetObserver(DataSetObserver
		 * observer) { if (observer != null) {
		 * super.unregisterDataSetObserver(observer); } }
		 */

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(this.getClass().toString(),
					"public View getView(int position, View convertView, ViewGroup parent)");
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			final ResponsePackage o = items.get(position);
			if (o != null) {

				SharedPreferences settings = getSharedPreferences(
						SharedVars.PREFS_NAME, 0);
				final SharedPreferences.Editor editor = settings.edit();

				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView mt = (TextView) v.findViewById(R.id.middletext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				final ImageView up = (ImageView) v.findViewById(R.id.up_arrow);
				final ImageView down = (ImageView) v
						.findViewById(R.id.down_arrow);
				if (tt != null) {
					tt.setText(o.getStopDescription());
					tt.setTag(o.getUserID());
				}
				if (mt != null) {
					TimeZone thisTimezone = TimeZone.getDefault();
					GregorianCalendar historyGC = new GregorianCalendar(thisTimezone);
					historyGC.setTimeInMillis(o.getReportTime());
					historyGC.setTimeZone(TimeZone.getDefault());
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd/MM/yyyy");
					SimpleDateFormat dateFormat2 = new SimpleDateFormat(
									"HH:mm:ss");
					mt.setText("no dia: " + new StringBuilder(dateFormat.format(historyGC.getTime())) + "\npelas " + new StringBuilder(dateFormat2.format(historyGC.getTime())));
				}
				if (bt != null) {
					if (o.getUserID() == 1) {
						bt.setText("Anónimo");
					} else {
						bt.setText("Confirmo: " + o.getUserRating());
					}
				}
				if (up != null) {
					up.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.i(this.getClass().toString(),
									"UP_ARROW: userID " + o.getUserID());
							up.setVisibility(android.view.View.INVISIBLE);
							down.setVisibility(android.view.View.INVISIBLE);
							rating = 1;
							userId = o.getUserID();
							editor.putBoolean(String.valueOf(o.getReportTime()),
									true);
							editor.commit();
							showDialog(1);
						}
					});
					if (settings
							.getBoolean(String.valueOf(o.getReportTime()), false)
							|| o.getUserID() == EntranceActivity.getUser()) {
						up.setVisibility(android.view.View.INVISIBLE);
						down.setVisibility(android.view.View.INVISIBLE);
					}
				}
				if (down != null) {
					down.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.i(this.getClass().toString(),
									"DOWN_ARROW: userID " + o.getUserID());
							up.setVisibility(android.view.View.INVISIBLE);
							down.setVisibility(android.view.View.INVISIBLE);
							rating = 2;
							userId = o.getUserID();
							editor.putBoolean(String.valueOf(o.getReportTime()),
									true);
							editor.commit();
							showDialog(1);
						};
					});
					if (settings
							.getBoolean(String.valueOf(o.getReportTime()), false)
							|| o.getUserID() == EntranceActivity.getUser()
							|| o.getUserID() == 1) {
						up.setVisibility(android.view.View.INVISIBLE);
						down.setVisibility(android.view.View.INVISIBLE);
					} else {
						up.setVisibility(android.view.View.VISIBLE);
						down.setVisibility(android.view.View.VISIBLE);
					}
				}
			}
			return v;
		}
	}

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i(this.getClass().toString(),
					"public void handleMessage(Message msg)");
			int total = msg.getData().getInt("total");
			if (total <= 0) {
				dismissDialog(0);
				((ProgressThread) progThread).setState(ProgressThread.DONE);
				try {
					removeDialog(0);
				} catch (Exception e) {
					Log.i(this.getClass().toString(), e.toString());
				}

				if (!rpList.isEmpty())
					setListAdapter(new ResponsePackageAdapter(
							getApplicationContext(), R.layout.list_item, rpList));
			}
			if (total == 1) {
				dismissDialog(1);
				((ChangeUserRatingThread) ratingThread)
						.setState(ChangeUserRatingThread.DONE);
				try {
					removeDialog(1);
				} catch (Exception e) {
					Log.i(this.getClass().toString(), e.toString());
				}

				showDialog(0);
				if (!rpList.isEmpty())
					setListAdapter(new ResponsePackageAdapter(
							getApplicationContext(), R.layout.list_item, rpList));
			}
		}
	};

	public class ChangeUserRatingThread extends Thread {

		final static int DONE = 0;
		final static int RUNNING = 1;

		Handler mHandler = null;
		int mState;

		ChangeUserRatingThread(Handler h) {
			Log.i(this.getClass().toString(),
					"ChangeUserRatingThread(Handler h)");
			mHandler = h;
		}

		@Override
		public void run() {
			Log.i(this.getClass().toString(), "public void run()");
			mState = RUNNING;
			while (mState == RUNNING) {
				try {
					MetroHistoryActivity.postRating(
							MetroHistoryActivity.getUserId(),
							MetroHistoryActivity.getRating());
				} catch (Exception e) {
					Log.e("ERROR", "Thread was Interrupted");
				}
				setState(DONE);
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt("total", 1);
				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		}

		public void setState(int state) {
			Log.i(this.getClass().toString(), "public void setState(int state)");
			if (mState != 0) {
				mState = state;
			}
		}
	}

	public static int getRating() {
		return rating;
	}

	public static int getUserId() {
		return userId;
	}

	public static MetroHistoryActivity getContext() {
		return contextHelper;
	}

	public static void postRating(int userID, int rating) {
		Log.i("metro.controller.activities.MetroHistoryActivity",
				"public void postRating(int userID, int rating)");
		HttpClient httpclient = new DefaultHttpClient();

		URI uri = null;
		String auxiliar = "http://project.vascosousa.com/mcws2.php?useridrating="
				+ userID + "&rating=" + rating;
		Log.i("metro.controller.activities.MetroHistoryActivity", auxiliar);
		try {
			uri = new URI(auxiliar.replace(" ", "%20"));
		} catch (Exception e) {
			Log.getStackTraceString(e);
			Toast.makeText(MetroHistoryActivity.getContext(),
					"Sem acesso à internet.", Toast.LENGTH_LONG);
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

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.i(this.getClass().toString(),
				"protected Dialog onCreateDialog(int id)");
		switch (id) {
		case 0:
			progDialog = new ProgressDialog(this);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("À procura deles...");
			progThread = new ProgressThread(handler);
			progThread.start();
			return progDialog;
		case 1:
			progDialog = new ProgressDialog(this);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setMessage("A confirmar utilizador...");
			ratingThread = new ChangeUserRatingThread(handler);
			ratingThread.start();
			return progDialog;
		default:
			return null;
		}
	}
}