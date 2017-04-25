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

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ColorBO;


public class ColorAdapter extends ArrayAdapter<ColorBO> {

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private ViewHolder holder;
	private ArrayList<ColorBO> items;
	private int pos = -1;
	
	public ColorAdapter(Context context, int resource,ArrayList<ColorBO> item) {
		super(context,resource,item);
		mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.context = context;
		this.items=item;
	}

	 public void setClickedChild1(int newClickedChildPosition){
		    this.pos=newClickedChildPosition;
	 }

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout alertView = null;
		holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
        holder.colortic=(LinearLayout)convertView.findViewById(R.id.colortic);
		holder.color= (TextView) convertView.findViewById(R.id.colortxt);
		GradientDrawable drawable = (GradientDrawable) holder.color.getBackground();
		drawable.setColor(Color.parseColor(items.get(position).getCatcolor()));
		if(position==pos){
			 holder.colortic.setVisibility(View.VISIBLE);
		} else{
			 holder.colortic.setVisibility(View.GONE);
		}
		return convertView;
	}

	 class ViewHolder {
		 LinearLayout colortic;
		 TextView color;
	 }
}
