package co.stutzen.shopzen.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.stutzen.shopzen.R;
import co.stutzen.shopzen.bo.ImageBO;

public class ImageAdapter extends ArrayAdapter<ImageBO> {

    private LayoutInflater mInflater;
    private int resource;
    private Context context;
    private List<ImageBO> items;

    public ImageAdapter(Context context, int resource, List<ImageBO> items, int from) {
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
        ImageBO detail = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(resource, alertView, true);
            convertView.setTag(holder);
        }
        holder.name = (TextView)convertView.findViewById(R.id.tagname);
        if(detail.getMenucatid() == 0)
            holder.name.setBackgroundResource(R.drawable.round_gray);
        else
            holder.name.setBackgroundResource(R.drawable.round_lgreen);
        return convertView;
    }

    static class ViewHolder {
        TextView name;
    }

}
