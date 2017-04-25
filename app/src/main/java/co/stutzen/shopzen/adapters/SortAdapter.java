package co.stutzen.shopzen.adapters;



import java.util.ArrayList;

import android.R.drawable;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.SortBO;


public class SortAdapter extends ArrayAdapter<SortBO>

{

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private ViewHolder holder;
	ArrayList<SortBO> items;
	int pos=-1;
	Handler handler = new Handler();

	public SortAdapter(Context context, int resource,ArrayList<SortBO> item) {
		super(context,resource,item);
		mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.context = context;
		this.items=item;
	}

	public void select(int newClickedChildPosition){
		    this.pos = newClickedChildPosition;
		    Log.i("pos",pos+"");
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LinearLayout alertView = null;
		holder = new ViewHolder();
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.sortby_item,null);
			convertView.setTag(holder);

		}
        holder.text= (TextView) convertView.findViewById(R.id.sorttxt);
		holder.sortview=(View)convertView.findViewById(R.id.vieww);
		holder.tic=(ImageView)convertView.findViewById(R.id.greentic);
		
		
		holder.text.setText(items.get(position).getSorttype());
		if(pos==position){
			holder.tic.setVisibility(View.VISIBLE);
			holder.text.setTypeface(Typeface.DEFAULT_BOLD);
		}
		else{
			holder.tic.setVisibility(View.GONE);
			holder.text.setTypeface(Typeface.DEFAULT);
		}
		
		return convertView;

	}
	 class ViewHolder {
		TextView text;
		ImageView tic;
		View sortview;
	}

}
