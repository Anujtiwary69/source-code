package co.stutzen.shopzen;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.stutzen.shopzen.adapters.ImageAdapter;
import co.stutzen.shopzen.bo.ImageBO;
import co.stutzen.shopzen.custom.HorizontalListView;
import co.stutzen.shopzen.custom.LockableViewPager;
import co.stutzen.shopzen.database.DBController;

public class ZoomingActivity extends FragmentActivity {

	private LockableViewPager pager;
	private ArrayList<Fragment> list;
	private MyPageAdapter pageAdapter;
	private int position;
	private DBController dbCon;
	private Dialog probar;
	private HorizontalListView horizlist;
	private ArrayList<ImageBO> horidata;
	private ImageAdapter pointadp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.viewpager_layout);
		LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
		backclick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		TextView produtname=(TextView)findViewById(R.id.sname);
		produtname.setText(getIntent().getStringExtra("name"));
		dbCon = new DBController(ZoomingActivity.this);
		LinearLayout mainlay = (LinearLayout) findViewById(R.id.mainlay);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		mainlay.setPadding((metrics.widthPixels / 2 - 30), 0, 0, 0);
		probar = new Dialog(ZoomingActivity.this);
		probar.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pager = (LockableViewPager) findViewById(R.id.viewpager);
		pager.setSwipeLocked(true);
		horizlist = (HorizontalListView) findViewById(R.id.horizlist);
		list = new ArrayList<Fragment>();
		horidata = new ArrayList<ImageBO>();
		pointadp = new ImageAdapter(ZoomingActivity.this, R.layout.round_lay, horidata, 0);
		horizlist.setAdapter(pointadp);
		Bundle bundle=getIntent().getExtras();
		position=bundle.getInt("position");
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				pager.setSwipeLocked(true);
				for (int i = 0; i < horidata.size(); i++)
					horidata.get(i).setMenucatid(0);
				horidata.get(arg0).setMenucatid(1);
				pointadp.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		for(int i=0; i<bundle.getInt("size"); i++) {
			list.add(Zoomfragment.newInstance(getIntent().getStringExtra("image"+i), pager));
			if(i == 0){
				ImageBO bo = new ImageBO();
				bo.setMenucatid(1);
				horidata.add(bo);
			}else{
				horidata.add(new ImageBO());
			}
		}
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), list);
		pager.setAdapter(pageAdapter);
		pointadp.notifyDataSetChanged();
		pager.setCurrentItem(position);
	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
	            return this.fragments.get(position);
	        }
	     
		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.i("onRestoreInstanceState", "onRestoreInstanceState");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i("onSaveInstanceState", "onSaveInstanceState");
	}

	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
		    
}


