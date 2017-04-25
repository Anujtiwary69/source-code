package co.stutzen.shopzen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.CategoryBO;

public class CategoryAdapter extends ArrayAdapter<CategoryBO> {

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<CategoryBO> items;

	public CategoryAdapter(Context context, int resource, List<CategoryBO> items) {
		super(context, resource, items);
		mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LinearLayout alertView = null;
		holder = new ViewHolder();
		CategoryBO detail = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
		holder.name = (TextView)convertView.findViewById(R.id.categorytitle);
		holder.icon = (ImageView)convertView.findViewById(R.id.categoryicon);
		holder.name.setText(detail.getCateName());
		holder.icon.setImageResource(detail.getCateIcon());
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		ImageView icon;
	}

}
