package co.stutzen.shopzen.adapters;

import java.util.List;

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
import co.stutzen.shopzen.database.DBController;

public class HomeTopAdapter extends ArrayAdapter<ProductBO> {

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<ProductBO> items;
	private DBController dbCon;

	public HomeTopAdapter(Context context, int resource,List<ProductBO> items) {
		super(context, resource, items);
		mInflater = LayoutInflater.from(context);
		dbCon = new DBController(context);
		this.resource = resource;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LinearLayout alertView = null;
		holder = new ViewHolder();
		final ProductBO detail = items.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
		holder.likeimg = (ImageView)convertView.findViewById(R.id.likeimg);
		holder.proimg = (ImageView)convertView.findViewById(R.id.proimg);
		holder.name = (TextView)convertView.findViewById(R.id.productname);
		holder.price = (TextView)convertView.findViewById(R.id.productprice);
		holder.discountLay = (LinearLayout)convertView.findViewById(R.id.discountlay);
		holder.discountText = (TextView)convertView.findViewById(R.id.discounttext);
		PicassoTrustAll.getInstance(context).load(items.get(position).getImage()).into(holder.proimg);
		holder.name.setText(detail.getName());
		holder.price.setText(String.format("%.2f", detail.getAmount()));
		holder.discountText.setText(String.format("%.1f", detail.getDiscountPercentage())+"%");
		if(detail.getDiscountPercentage() > 0)
			holder.discountLay.setVisibility(View.GONE);
		else
			holder.discountLay.setVisibility(View.GONE);
        if(detail.isAddedBag())
			holder.likeimg.setImageResource(R.mipmap.likefilled);
		else
			holder.likeimg.setImageResource(R.mipmap.likenofill);
		holder.likeimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(items.get(position).isAddedBag()) {
					items.get(position).setIsAddedBag(false);
					dbCon.delMyFavorite(items.get(position).getId());
				}else {
					items.get(position).setIsAddedBag(true);
					dbCon.insertFavorite(items.get(position).getId(), items.get(position).getName(), items.get(position).getAmount()+"", items.get(position).getImage());
				}
				notifyDataSetChanged();
			}
		});

		holder.ratingstar1 = (ImageView)convertView.findViewById(R.id.ratingstar1);
		holder.ratingstar2 = (ImageView)convertView.findViewById(R.id.ratingstar2);
		holder.ratingstar3 = (ImageView)convertView.findViewById(R.id.ratingstar3);
		holder.ratingstar4 = (ImageView)convertView.findViewById(R.id.ratingstar4);
		holder.ratingstar5 = (ImageView)convertView.findViewById(R.id.ratingstar5);

		holder.reviewcount = (TextView)convertView.findViewById(R.id.reviewcount);
		holder.reviewcount.setText("("+detail.getReviewUserCount()+")");
		placeProductRating(detail.getRatingCount(), holder.ratingstar1, holder.ratingstar2, holder.ratingstar3, holder.ratingstar4, holder.ratingstar5);
		return convertView;
	}

	private void placeProductRating(int ratingCount, ImageView ratingstar1, ImageView ratingstar2, ImageView ratingstar3, ImageView ratingstar4, ImageView ratingstar5) {
		if(ratingCount <= 0){
			ratingstar1.setImageResource(R.mipmap.ratingnotfill);
			ratingstar2.setImageResource(R.mipmap.ratingnotfill);
			ratingstar3.setImageResource(R.mipmap.ratingnotfill);
			ratingstar4.setImageResource(R.mipmap.ratingnotfill);
			ratingstar5.setImageResource(R.mipmap.ratingnotfill);
		}else if(ratingCount == 1){
			ratingstar1.setImageResource(R.mipmap.ratingfill);
			ratingstar2.setImageResource(R.mipmap.ratingnotfill);
			ratingstar3.setImageResource(R.mipmap.ratingnotfill);
			ratingstar4.setImageResource(R.mipmap.ratingnotfill);
			ratingstar5.setImageResource(R.mipmap.ratingnotfill);
		}else if(ratingCount == 2){
			ratingstar1.setImageResource(R.mipmap.ratingfill);
			ratingstar2.setImageResource(R.mipmap.ratingfill);
			ratingstar3.setImageResource(R.mipmap.ratingnotfill);
			ratingstar4.setImageResource(R.mipmap.ratingnotfill);
			ratingstar5.setImageResource(R.mipmap.ratingnotfill);
		}else if(ratingCount == 3){
			ratingstar1.setImageResource(R.mipmap.ratingfill);
			ratingstar2.setImageResource(R.mipmap.ratingfill);
			ratingstar3.setImageResource(R.mipmap.ratingfill);
			ratingstar4.setImageResource(R.mipmap.ratingnotfill);
			ratingstar5.setImageResource(R.mipmap.ratingnotfill);
		}else if(ratingCount == 4){
			ratingstar1.setImageResource(R.mipmap.ratingfill);
			ratingstar2.setImageResource(R.mipmap.ratingfill);
			ratingstar3.setImageResource(R.mipmap.ratingfill);
			ratingstar4.setImageResource(R.mipmap.ratingfill);
			ratingstar5.setImageResource(R.mipmap.ratingnotfill);
		}else if(ratingCount > 4){
			ratingstar1.setImageResource(R.mipmap.ratingfill);
			ratingstar2.setImageResource(R.mipmap.ratingfill);
			ratingstar3.setImageResource(R.mipmap.ratingfill);
			ratingstar4.setImageResource(R.mipmap.ratingfill);
			ratingstar5.setImageResource(R.mipmap.ratingfill);
		}
	}

	static class ViewHolder {
		TextView name;
		ImageView proimg;
		TextView price;
		LinearLayout discountLay;
		TextView discountText;
		ImageView likeimg;
		ImageView ratingstar1;
		ImageView ratingstar2;
		ImageView ratingstar3;
		ImageView ratingstar4;
		ImageView ratingstar5;
		TextView reviewcount;
	}

}
