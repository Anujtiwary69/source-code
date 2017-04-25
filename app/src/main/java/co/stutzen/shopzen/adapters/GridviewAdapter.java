package co.stutzen.shopzen.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.custom.PicassoTrustAll;

public class GridviewAdapter extends ArrayAdapter<ProductBO> {

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private ViewHolder holder;
	private ArrayList<ProductBO> items;
	int pos = -1;

	public GridviewAdapter(Context context, int resource, ArrayList<ProductBO> item) {
		super(context, resource, item);
		mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.context = context;
		this.items = item;
	}

	public void clickchild(int newClickedChildPosition){
		this.pos=newClickedChildPosition;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LinearLayout alertView = null;
		holder = new ViewHolder();
		if (convertView == null) {

			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);

		}
		holder.row = (LinearLayout) convertView.findViewById(R.id.row);
		holder.name = (TextView) convertView.findViewById(R.id.itemname);
		holder.price = (TextView) convertView.findViewById(R.id.itemprice);
		holder.frame=(LinearLayout)convertView.findViewById(R.id.gridframe);
		holder.name.setText(items.get(position).getName());
		holder.price.setText("Rs " + items.get(position).getAmount() + "");
		if(position==pos){
			holder.frame.setVisibility(View.VISIBLE);
		}
		else{
			holder.frame.setVisibility(View.GONE);
		}
		holder.img = (ImageView) convertView.findViewById(R.id.item_image);
		PicassoTrustAll.getInstance(context).load(items.get(position).getImage()).into(holder.img);
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView name, price;
		LinearLayout row,frame;
	}

}