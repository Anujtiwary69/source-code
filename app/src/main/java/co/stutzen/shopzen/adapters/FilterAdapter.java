package co.stutzen.shopzen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ColorBO;


public class FilterAdapter extends ArrayAdapter<ColorBO> {

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private ViewHolder holder;
	private ArrayList<ColorBO> items;

	public FilterAdapter(Context context, int resource, ArrayList<ColorBO> item) {
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
        holder.check = (CheckBox)convertView.findViewById(R.id.checkilter);
		holder.menutxt = (TextView) convertView.findViewById(R.id.txt);
		holder.counttxt = (TextView) convertView.findViewById(R.id.txt1);
		holder.menutxt.setText(items.get(position).getCatcolor());
		holder.counttxt.setText("");
		if(items.get(position).isSelected())
			holder.check.setChecked(true);
		else
			holder.check.setChecked(false);
		 holder.check.setClickable(false);
		 holder.check.setFocusable(false);
		 holder.check.setFocusableInTouchMode(false);
		convertView.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View view) {
				 if(items.get(position).isSelected()) {
					 items.get(position).setSelected(false);
				 }else {
					 items.get(position).setSelected(true);
				 }
				 notifyDataSetChanged();
			 }
		 });
		return convertView;
	}

	 class ViewHolder {
		 CheckBox check;
		 TextView menutxt;
		 TextView counttxt;
	 }
}
