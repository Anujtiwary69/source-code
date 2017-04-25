package co.stutzen.shopzen.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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

import co.stutzen.shopzen.CartActivity;
import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.custom.PicassoTrustAll;
import co.stutzen.shopzen.database.DBController;

public class CartAdapter extends ArrayAdapter<ProductBO> {

	private DBController dbCon;
	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<ProductBO> items;

	public CartAdapter(Context context, int resource, List<ProductBO> items) {
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
		holder.img = (ImageView)convertView.findViewById(R.id.tabimg);
		holder.name = (TextView)convertView.findViewById(R.id.productname);
		holder.price = (TextView)convertView.findViewById(R.id.productprice);
		holder.cartqty = (TextView)convertView.findViewById(R.id.cartqty);
		holder.plus = (TextView) convertView.findViewById(R.id.plus);
		holder.minus = (TextView) convertView.findViewById(R.id.minus);
		holder.remove=(LinearLayout)convertView.findViewById(R.id.remove);
		holder.variablelay = (LinearLayout)convertView.findViewById(R.id.variablelay);
		holder.size = (TextView)convertView.findViewById(R.id.size);
		holder.color = (TextView)convertView.findViewById(R.id.colorid);

		holder.name.setText(detail.getName());
		holder.price.setText(String.format("%.2f", detail.getAmount()));
		holder.cartqty.setText("" + detail.getQuantity());

		if(items.get(position).getType().equalsIgnoreCase("simple")){
			holder.variablelay.setVisibility(View.GONE);
		}else{
			holder.variablelay.setVisibility(View.VISIBLE);
			holder.size.setText(items.get(position).getSize());
			GradientDrawable drawable1 = (GradientDrawable) holder.color.getBackground();
			drawable1.setColor(Color.parseColor(items.get(position).getColor()));
		}
		holder.plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				items.get(position).setQuantity(items.get(position).getQuantity() + 1);
				dbCon.updateCart(items.get(position).getQuantity(), items.get(position).getId());
				notifyDataSetChanged();

			}
		});

		holder.minus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((items.get(position).getQuantity() - 1) > 0) {
					items.get(position).setQuantity(items.get(position).getQuantity() - 1);
					dbCon.updateCart(items.get(position).getQuantity(), items.get(position).getId());
					notifyDataSetChanged();
				}
			}
		});
		holder.remove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRemovePopup(items.get(position).getName(), position);

			}
		});

		PicassoTrustAll.getInstance(context).load(items.get(position).getImage()).into(holder.img);
		return convertView;
	}

	public void showRemovePopup(final String proName, final int propos) {

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
		produtname.setText("Are you sure to delete the product "+proName+" ?");
		open.show();
		TextView yes=(TextView)popup.findViewById(R.id.yes);
		TextView no=(TextView)popup.findViewById(R.id.no);

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dbCon.delCart(items.get(propos).getId());
				open.dismiss();
				items.remove(propos);
				if(items.size() == 0){
					((CartActivity)context).setEmptyCart();
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
		TextView price;
		TextView cartqty;
		TextView plus;
		TextView minus;
		LinearLayout remove;
		ImageView img;
		public LinearLayout variablelay;
		public TextView size;
		public TextView color;
	}

}
