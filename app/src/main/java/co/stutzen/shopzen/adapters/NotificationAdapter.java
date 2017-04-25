package co.stutzen.shopzen.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.stutzen.shopzen.CardsActivity;
import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.NotificationBO;
import co.stutzen.shopzen.database.DBController;

public class NotificationAdapter extends ArrayAdapter<NotificationBO> {

	private DBController dbCon;
	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<NotificationBO> items;

	public NotificationAdapter(Context context, int resource, List<NotificationBO> items) {
		super(context, resource, items);
		mInflater = LayoutInflater.from(context);
		this.dbCon = new DBController(context);
		this.resource = resource;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LinearLayout alertView = null;
		holder = new ViewHolder();
		final NotificationBO detail = items.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
		holder.name = (TextView)convertView.findViewById(R.id.name);
		holder.time=(TextView)convertView.findViewById(R.id.time);

		holder.name.setText(detail.getMessage());
		holder.time.setText(detail.getTime());
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView time;
	}

}
