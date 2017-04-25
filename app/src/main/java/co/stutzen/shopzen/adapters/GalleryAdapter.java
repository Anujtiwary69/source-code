package co.stutzen.shopzen.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.custom.PicassoTrustAll;

public class GalleryAdapter extends ArrayAdapter<String> {

	private int resource;
	private Context context;
	private List<String> plotsImages;
	private ViewHolder holder;
	private LayoutInflater mInflater;

	public GalleryAdapter(Context context, int resource, List<String> plotsImages) {
		super(context, resource, plotsImages);
		mInflater = LayoutInflater.from(context);
		this.resource = resource;
		this.context = context;
		this.plotsImages = plotsImages;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout alertView = null;
		holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
		holder.imageView = (ImageView) convertView.findViewById(R.id.img);
		holder.layout = (LinearLayout) convertView.findViewById(R.id.lay);
		PicassoTrustAll.getInstance(context).load(plotsImages.get(position)).into(holder.imageView);
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		LinearLayout layout;
	}

}
