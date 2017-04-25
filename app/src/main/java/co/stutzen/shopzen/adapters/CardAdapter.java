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

import com.squareup.picasso.Picasso;

import java.util.List;

import co.stutzen.shopzen.CardsActivity;
import co.stutzen.shopzen.CartActivity;
import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.database.DBController;

public class CardAdapter extends ArrayAdapter<String> {

	private DBController dbCon;
	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<String> items;

	public CardAdapter(Context context, int resource, List<String> items) {
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
		final String detail = items.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
		holder.name = (TextView)convertView.findViewById(R.id.productname);
		holder.remove=(LinearLayout)convertView.findViewById(R.id.remove);

		holder.name.setText(detail);
		holder.remove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRemovePopup(position, detail);

			}
		});
		return convertView;
	}

	public void showRemovePopup(final int propos, String name) {

		final Dialog open = new Dialog(context);
		open.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View popup = mInflater.inflate(R.layout.remove_product, null);
		final TextView produtname=(TextView)popup.findViewById(R.id.txt);
		open.setContentView(popup);
		open.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		open.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width= (int) (displaymetrics.widthPixels*1);
		open.getWindow().setLayout(width-50, ActionBar.LayoutParams.WRAP_CONTENT);
		produtname.setText("Are you sure to delete the card detail " + name + "? ");
		open.show();
		TextView yes=(TextView)popup.findViewById(R.id.yes);
		TextView no=(TextView)popup.findViewById(R.id.no);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				items.remove(propos);
				open.dismiss();
				if(items.size() == 0){
					((CardsActivity)context).setEmptyCart(items);
				}
				notifyDataSetChanged();
			}
		});
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				open.dismiss();
			}
		});

	}

	static class ViewHolder {
		TextView name;
		LinearLayout remove;
	}

}
