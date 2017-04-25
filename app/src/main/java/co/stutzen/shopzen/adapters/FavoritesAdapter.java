package co.stutzen.shopzen.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import co.stutzen.shopzen.CartActivity;
import co.stutzen.shopzen.MyFavoritesActivity;
import co.stutzen.shopzen.R;
import co.stutzen.shopzen.SingleProductActivity;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.custom.PicassoTrustAll;
import co.stutzen.shopzen.database.DBController;

public class FavoritesAdapter extends ArrayAdapter<ProductBO> {

	private DBController dbCon;
	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<ProductBO> items;

	public FavoritesAdapter(Context context, int resource, List<ProductBO> items) {
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
		holder.remove=(LinearLayout)convertView.findViewById(R.id.remove);

		holder.name.setText(detail.getName());
		holder.price.setText(String.format("%.2f", detail.getAmount()));
		holder.remove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dbCon.delMyFavorite(items.get(position).getId());
				items.remove(position);
				if (items.size() == 0) {
					((MyFavoritesActivity) context).setEmptyCart();
				}
				notifyDataSetChanged();
			}
		});
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, SingleProductActivity.class);
				intent.putExtra("id", items.get(position).getId());
				intent.putExtra("name", items.get(position).getName());
				intent.putExtra("image", items.get(position).getImage());
				intent.putExtra("price", items.get(position).getAmount());
				intent.putExtra("type", items.get(position).getType());
				context.startActivity(intent);
			}
		});
		PicassoTrustAll.getInstance(context).load(items.get(position).getImage()).into(holder.img);
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView price;
		LinearLayout remove;
		ImageView img;
	}

}
