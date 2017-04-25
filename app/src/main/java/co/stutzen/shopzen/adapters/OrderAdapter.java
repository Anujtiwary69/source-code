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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.stutzen.shopzen.CartActivity;
import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.custom.PicassoTrustAll;
import co.stutzen.shopzen.database.DBController;

public class OrderAdapter extends ArrayAdapter<ProductBO> {

	private DBController dbCon;
	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<ProductBO> items;

	public OrderAdapter(Context context, int resource, List<ProductBO> items) {
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
		ProductBO detail = items.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
		holder.name = (TextView)convertView.findViewById(R.id.productname);
		holder.price = (TextView)convertView.findViewById(R.id.price);
		holder.cartqty = (TextView)convertView.findViewById(R.id.desc);

		holder.name.setText(detail.getName());
		holder.price.setText(String.format("%.2f", detail.getAmount()));
		holder.cartqty.setText( detail.getRatingCount()+" x "+String.format("%.2f",detail.getDiscountPercentage()));

		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView price;
		TextView cartqty;
	}

}
