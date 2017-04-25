package co.stutzen.shopzen.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import co.stutzen.shopzen.R;

public class DropDownAdapter extends ArrayAdapter<String>{
	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private ArrayList<String> items;
	private ViewHolder holder;
	
	public DropDownAdapter(Context context, int resource, ArrayList<String> items) {
		super(context,resource,items);
		mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getDropDownView(int position, View convertView,ViewGroup parent) {
		LinearLayout alertView = null;
		holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.select_grid, null);
			convertView.setTag(holder);
			if(parent!=null&&(parent instanceof ListView)){
				ListView lv=null;
				try{
					lv=(ListView)parent;
				}catch(Exception e){}
				if(lv!=null){
					lv.setDivider(null);
					lv.setDividerHeight(0);
				}
			}
		}
		holder.txt=(TextView) convertView.findViewById(R.id.txt);
		holder.div=(View)convertView.findViewById(R.id.div);
		holder.txt.setText((CharSequence) items.get(position));
		if(position==items.size()-1){
			holder.div.setVisibility(View.GONE);
		} else{
			 holder.div.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new ViewHolder();
		if (convertView == null) {
			view = mInflater.inflate(resource, null);
			view.setTag(holder);
			if(parent!=null&&(parent instanceof ListView)){
				ListView lv=null;
				try{
					lv=(ListView)parent;
				}catch(Exception e){}
				if(lv!=null){
					lv.setDivider(null);
					lv.setDividerHeight(0);
				}
			}
		}
		holder.tview=(TextView) view.findViewById(R.id.txt);
		holder.tview.setText((CharSequence) items.get(position));
		return view;
	}

	class ViewHolder {
		TextView tview;
		TextView txt;
		View div;
	}
}
