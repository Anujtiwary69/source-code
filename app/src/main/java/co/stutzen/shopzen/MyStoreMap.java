package co.stutzen.shopzen;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyStoreMap extends FragmentActivity {

    private GoogleMap map;
    private Marker marker;
    String lat[]={"9.448540","10.3673","9.925201","13.082680","8.713913","11.016844", "10.2381"};
    String lng[]={"77.799435","77.9803","78.119775","80.270718","77.756652","76.955832", "77.4892"};
    String saddress[]={"Sivakasi","Dindigul","Madurai","Chennai","Tirunelveli","Coimbatore", "Kodaikanal"};
    int simg[]={R.mipmap.rsz_blue, R.mipmap.rsz_moon, R.mipmap.festival, R.mipmap.rsz_moon, R.mipmap.rsz_blue, R.mipmap.festival, R.mipmap.festival};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_list);
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maphome)).getMap();
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        for (int i = 0; i < lat.length; i++) {
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat[i]), Double.parseDouble(lng[i])))
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(createBitmapFromLayoutWithText(MyStoreMap.this, simg[i], saddress[i]))));
        }
        if (map != null) {
            map.getUiSettings().setZoomControlsEnabled(false);
            map.setTrafficEnabled(true);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(9.925201, 78.119775));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);
            map.moveCamera(center);
            map.animateCamera(zoom);
        }
    }


    public Bitmap createBitmapFromLayoutWithText(Context context, int img, String address) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = new LinearLayout(context);
        View dataView = mInflater.inflate(R.layout.marker_view, view, true);
        ImageView imge = (ImageView) dataView.findViewById(R.id.img);
        imge.setImageResource(img);
        view.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }

}
