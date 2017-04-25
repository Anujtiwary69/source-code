package co.stutzen.shopzen.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.OrderBO;

public class MyOrderAdapter extends ArrayAdapter<OrderBO> {

	private LayoutInflater mInflater;
	private int resource;
	private Context context;
	private List<OrderBO> items;

	public MyOrderAdapter(Context context, int resource, List<OrderBO> items) {
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
		OrderBO detail = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resource, alertView, true);
			convertView.setTag(holder);
		}
		holder.orderplaced = (TextView)convertView.findViewById(R.id.orderplaced);
		holder.orderid = (TextView)convertView.findViewById(R.id.orderid);
		holder.orderqty = (TextView)convertView.findViewById(R.id.orderqty);
		holder.estimatedate = (TextView)convertView.findViewById(R.id.estimatedate);

		holder.shipline1 = (View)convertView.findViewById(R.id.shipline1);
		holder.shipline2 = (View)convertView.findViewById(R.id.shipline2);
		holder.shipgreen = (ImageView)convertView.findViewById(R.id.shipgreen);
		holder.shipblack = (ImageView)convertView.findViewById(R.id.shipblack);

		holder.deliblack = (ImageView)convertView.findViewById(R.id.deliblack);
		holder.deligreen = (ImageView)convertView.findViewById(R.id.deligreen);
		holder.deliline1 = (View)convertView.findViewById(R.id.deliline1);
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat format2 = new SimpleDateFormat("d MMM yyyy hh:mm a");
			Date date = format1.parse(detail.getOrderDate());
			holder.orderplaced.setText(format2.format(date));
		}catch(Exception e){
			e.printStackTrace();
		}
		holder.orderid.setText(detail.getOrderId());
		holder.orderqty.setText("Total:  "+String.format("%.2f", detail.getAmount()));
		holder.estimatedate.setText(detail.getPayment());

		if(detail.getStatus().equalsIgnoreCase("processing")){
			holder.shipline1.setBackgroundColor(Color.parseColor("#6bc16a"));
			holder.shipline2.setBackgroundColor(Color.parseColor("#6bc16a"));
			holder.shipgreen.setVisibility(View.VISIBLE);
			holder.shipblack.setVisibility(View.GONE);
		}else if(detail.getStatus().equalsIgnoreCase("completed")){
			holder.shipline1.setBackgroundColor(Color.parseColor("#6bc16a"));
			holder.shipline2.setBackgroundColor(Color.parseColor("#6bc16a"));
			holder.shipgreen.setVisibility(View.VISIBLE);
			holder.shipblack.setVisibility(View.GONE);

			holder.deliline1.setBackgroundColor(Color.parseColor("#6bc16a"));
			holder.deligreen.setVisibility(View.VISIBLE);
			holder.deliblack.setVisibility(View.GONE);
		}else{
			holder.shipline1.setBackgroundColor(Color.parseColor("#dedede"));
			holder.shipline2.setBackgroundColor(Color.parseColor("#dedede"));
			holder.shipgreen.setVisibility(View.GONE);
			holder.shipblack.setVisibility(View.VISIBLE);

			holder.deliline1.setBackgroundColor(Color.parseColor("#dedede"));
			holder.deligreen.setVisibility(View.GONE);
			holder.deliblack.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	static class ViewHolder {
		TextView orderplaced;
		TextView orderid;
		TextView orderqty;
		TextView estimatedate;
		View shipline1;
		View shipline2;
		ImageView shipgreen;
		ImageView shipblack;
		ImageView deliblack;
		ImageView deligreen;
		View deliline1;
	}

}
