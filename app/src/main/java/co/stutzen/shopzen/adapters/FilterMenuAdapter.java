package co.stutzen.shopzen.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.stutzen.shopzen.ProductActivity;
import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ColorBO;


public class FilterMenuAdapter extends ArrayAdapter<ColorBO> {

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private ViewHolder holder;
	private ArrayList<ColorBO> items;

	public FilterMenuAdapter(Context context, int resource, ArrayList<ColorBO> item) {
		super(context,resource,item);
		mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.context = context;
		this.items=item;
	}

	 public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout alertView = null;
		holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
        holder.menusel=(View)convertView.findViewById(R.id.dividerlay);
		holder.menutxt= (TextView) convertView.findViewById(R.id.txt);
		holder.menutxt.setText(items.get(position).getCatcolor());
		if(items.get(position).isSelected())
			holder.menusel.setVisibility(View.VISIBLE);
		else
			holder.menusel.setVisibility(View.INVISIBLE);
		 convertView.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View view) {
				 if(items.get(position).isSelected()) {
					 items.get(position).setSelected(false);
				 }else {
					 for(int i=0; i<items.size(); i++)
						 items.get(i).setSelected(false);
					 items.get(position).setSelected(true);
				 }
				 notifyDataSetChanged();
				 ((ProductActivity)context).loadViewFilter(position);
			 }
		 });
		return convertView;
	}

	 class ViewHolder {
		 View menusel;
		 TextView menutxt;
	 }
}
