package co.stutzen.shopzen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import co.stutzen.shopzen.custom.LockableViewPager;
import co.stutzen.shopzen.custom.PicassoTrustAll;
import co.stutzen.shopzen.custom.zooming.GestureImageView;

public class Zoomfragment extends Fragment {

	public static double Width;
	private static final String ARG_PAGE_NUMBER = "pageNumber";
	private GestureImageView image ;
	private static String PIMAGE = "image";
	private static LockableViewPager pager1;

	public static final Zoomfragment newInstance(String image, LockableViewPager pa) {
		Zoomfragment f = new Zoomfragment();
		Bundle bdl = new Bundle();
		pager1 = pa;
		bdl.putString(PIMAGE, image);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.image_popup, container, false);
		image = (GestureImageView) rootView.findViewById(R.id.zoomimage);
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(pager1.getSwipeLocked())
				pager1.setSwipeLocked(false);
			}
		});
		image.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				if (!pager1.getSwipeLocked())
					pager1.setSwipeLocked(true);
				return false;
			}
		});
		PicassoTrustAll.getInstance(getActivity()).load(getArguments().getString(PIMAGE)).into(image);
		return rootView;
	}
	
	
}
