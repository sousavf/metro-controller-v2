package metro.controller.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import metro.controller.EntranceActivity;
import metro.controller.R;
import metro.controller.utils.History;
import metro.controller.utils.ResponsePackage;
import metro.controller.utils.SharedVars;
import metro.controller.utils.Stops;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BusHistoryActivity extends ListActivity {
	protected ProgressDialog dialog;
	protected ListView lv = null;
	ProgressDialog progDialog;
	Thread progThread;
	Handler progHandler;
	private ResponsePackageAdapter rp_adapter;
	private ArrayList<ResponsePackage> rpList;

	public void onCreate(Bundle savedInstanceState) {
		Log.i(this.getClass().toString(), "public void onCreate(Bundle savedInstanceState)");
		super.onCreate(savedInstanceState);

		showDialog(0);

		this.rp_adapter = new ResponsePackageAdapter(this, R.layout.list_item, rpList);
		setListAdapter(this.rp_adapter);
	}
	
	private class ResponsePackageAdapter extends ArrayAdapter<ResponsePackage> {

		private ArrayList<ResponsePackage> items;

		public ResponsePackageAdapter(Context context, int textViewResourceId,
				ArrayList<ResponsePackage> items) {
			super(context, textViewResourceId, items);
			Log.i(this.getClass().toString(), "public ResponsePackageAdapter(Context context, int textViewResourceId, ArrayList<ResponsePackage> items)");
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(this.getClass().toString(), "public View getView(int position, View convertView, ViewGroup parent) ");
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			ResponsePackage o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText(o.getStopDescription());
					tt.setTag(o.getUserID());
				}
				if (bt != null) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					bt.setText(new StringBuilder( dateFormat.format(o.getReportTime())));
				}
			}
			return v;
		}
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
					rpList = History.getHistory(SharedVars.BUS_MODULE);
					EntranceActivity.setSpArray(Stops.getStops(EntranceActivity.getModuleID(), EntranceActivity.getCityID()));
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

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i(this.getClass().toString(), "public void handleMessage(Message msg)");
			int total = msg.getData().getInt("total");
			if (total <= 0) {
				dismissDialog(0);
				((ProgressThread) progThread).setState(ProgressThread.DONE);
				removeDialog(0);
				if (!rpList.isEmpty())
					setListAdapter(new ArrayAdapter<ResponsePackage>(
							getApplicationContext(), R.layout.list_item, rpList));

				lv = getListView();
				lv.setTextFilterEnabled(true);

				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(getApplicationContext(),
								((TextView) view).getText(), Toast.LENGTH_SHORT)
								.show();
					}
				});
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		Log.i(this.getClass().toString(), "protected Dialog onCreateDialog(int id)");
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
}