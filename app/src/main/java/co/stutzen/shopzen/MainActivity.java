package co.stutzen.shopzen;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Interpolator;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import co.stutzen.shopzen.adapters.HomeTopAdapter;
import co.stutzen.shopzen.adapters.ProductAdapter;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.custom.HorizontalListView;
import co.stutzen.shopzen.custom.slider.JazzyViewPager;
import co.stutzen.shopzen.custom.slider.OutlineContainer;
import co.stutzen.shopzen.database.DBController;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    private HorizontalListView groomProductListView;
    private ArrayList<ProductBO> groomList = new ArrayList<ProductBO>();
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Client client;
    private JazzyViewPager pager;
    private boolean swipeVal =true;
    private ArrayList<Integer> dataImage;
    private Timer timerUpdate;

    private int start=0;
    private DBController dbCon;
    private HomeTopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        client = new Client(AppConstants.ALGOLIA_APP_ID, AppConstants.ALGOLIA_APP_KEY);
        setContentView(R.layout.activity_main);
        dbCon = new DBController(MainActivity.this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearLayout menuclick = (LinearLayout) findViewById(R.id.menuclick);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        groomProductListView = (HorizontalListView) findViewById(R.id.groomproductlist);

        menuclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        groomProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, SingleProductActivity.class);
                intent.putExtra("id", groomList.get(i).getId());
                intent.putExtra("name", groomList.get(i).getName());
                intent.putExtra("image", groomList.get(i).getImage());
                intent.putExtra("price", groomList.get(i).getAmount());
                intent.putExtra("type", groomList.get(i).getType());
                startActivity(intent);
            }
        });
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        LinearLayout cardclick = (LinearLayout) headerView.findViewById(R.id.cardclick);
        LinearLayout myorderclick = (LinearLayout) headerView.findViewById(R.id.myorderclick);
        LinearLayout addressclick = (LinearLayout) headerView.findViewById(R.id.addressclick);
        TextView clientname = (TextView) headerView.findViewById(R.id.clientname);
        clientname.setText(dbCon.getCustomerName());
        LinearLayout categoryclick = (LinearLayout) findViewById(R.id.categoryclick);
        LinearLayout saleclick = (LinearLayout) findViewById(R.id.saleclick);
        LinearLayout cartclick = (LinearLayout) findViewById(R.id.cartclick);
        LinearLayout favclick = (LinearLayout) findViewById(R.id.favclick);
        LinearLayout notclick = (LinearLayout) findViewById(R.id.notclick);
        categoryclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });
        saleclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
            }
        });
        cartclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
        myorderclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
            }
        });
        addressclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyStoreMap.class));
            }
        });
        LinearLayout tshirtclick = (LinearLayout) findViewById(R.id.tshirtclick);
        LinearLayout jeanclick = (LinearLayout) findViewById(R.id.jeanclick);
        LinearLayout slipperclick = (LinearLayout) findViewById(R.id.slipperclick);
        LinearLayout shoeclick = (LinearLayout) findViewById(R.id.shoeclick);
        tshirtclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProductActivity.class));
            }
        });
        jeanclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProductActivity.class));
            }
        });
        slipperclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProductActivity.class));
            }
        });
        shoeclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProductActivity.class));
            }
        });
        favclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyFavoritesActivity.class));
            }
        });
        cardclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CardsActivity.class));
            }
        });
        notclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });
        GetProductTask task1 = new GetProductTask();
        task1.execute(AppConstants.PRODUCT_LIST_API + "&per_page=10&order=desc&orderby=id");
        //getProductData();
        setupJazziness(JazzyViewPager.TransitionEffect.Standard);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void uncheckAllMenuItems(NavigationView navigationView) {
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_category) {
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
        } else if (id == R.id.nav_wallet) {
            startActivity(new Intent(MainActivity.this, WalletActivity.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
        } else if (id == R.id.nav_referearn) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Download Shopzen app at https://play.google.com/store/apps/details?id=co.stutzen.shopzen");
            startActivity(Intent.createChooser(intent, "Refer Us via"));
        }  else if (id == R.id.nav_logout) {
            dbCon.dropCart();
            dbCon.dropFavorites();
            dbCon.dropCustomer();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_rate) {
            showrateus(MainActivity.this);
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(MainActivity.this, ContactusActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getProductData(){
        Index index = client.initIndex("shopzen_products");
        Query query = new Query();
        query.setFacets("*");
        query.setHitsPerPage(5);
        // query.setFilters("brand:\"Apple\" AND brand:\"Amazon\"");
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                if (e == null) {
                    try {
                        JSONArray array = jsonObject.getJSONArray("hits");
                        if (array.length() > 0) {
                            groomList = new ArrayList<ProductBO>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject singleObj = array.getJSONObject(i);
                                groomList.add(new ProductBO(singleObj.getInt("objectID"), singleObj.getString("name"), singleObj.getString("image"), singleObj.getInt("rating"), singleObj.getInt("popularity"), singleObj.getDouble("price"), false, 10.5, singleObj.getString("type")));
                            }
                            ArrayList<ProductBO> favlist = dbCon.getFavoritesList();
                            for(int j=0; j<favlist.size(); j++){
                                for(int k=0; k<groomList.size(); k++){
                                    if(groomList.get(k).getId() == favlist.get(j).getId()){
                                        groomList.get(k).setIsAddedBag(true);
                                    }
                                }
                            }
                            adapter = new HomeTopAdapter(MainActivity.this, R.layout.homeproduct_item, groomList);
                            groomProductListView.setAdapter(adapter);
                        } else {
                            Snackbar.make(groomProductListView, "No products found.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Snackbar.make(groomProductListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupJazziness(JazzyViewPager.TransitionEffect effect) {
        pager = (JazzyViewPager) findViewById(R.id.Pager);
        dataImage = new ArrayList<Integer>();
        dataImage.add(R.mipmap.banner1);
        dataImage.add(R.mipmap.banner2);
        dataImage.add(R.mipmap.banner3);
        dataImage.add(R.mipmap.banner5);
        pager.setAdapter(new MainAdapter());
        pager.setTransitionEffect(effect);
        pager.setPageMargin(30);
        try {
            Field mScroller;
            android.view.animation.Interpolator sInterpolator = new DecelerateInterpolator();
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(),   sInterpolator);
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private class MainAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imgDisplay;

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.image_view, container,
                    false);
            imgDisplay = (ImageView) viewLayout.findViewById(R.id.browsebackground);
            imgDisplay.setBackgroundResource(dataImage.get(position));

            container.addView(viewLayout);
            pager.setObjectForPosition(viewLayout, position);
            return viewLayout;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(pager.findViewFromObject(position));
        }
        @Override
        public int getCount() {
            return dataImage.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
    }


    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 2000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, android.view.animation.Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context,android.view.animation.Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callBGChange();
        try{
            ArrayList<ProductBO> favlist = dbCon.getFavoritesList();
            for(int i=0; i<groomList.size(); i++)
                groomList.get(i).setIsAddedBag(false);
            for(int j=0; j<favlist.size(); j++){
                for(int k=0; k<groomList.size(); k++){
                    if(groomList.get(k).getId() == favlist.get(j).getId()){
                        groomList.get(k).setIsAddedBag(true);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("onResume", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerUpdate.cancel();
        Log.i("onStop","onStop");
    }

    public void callBGChange() {
        final Handler handler = new Handler();
        timerUpdate= new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try{
                            if (start == (dataImage.size() -1)){
                                swipeVal = false;
                            }if(start == 0){
                                swipeVal = true;
                            }
                            if(swipeVal)
                                pager.setCurrentItem(start++, true);
                            else
                                pager.setCurrentItem(start--, true);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerUpdate.schedule(doAsynchronousTask, 1000, 5000);
    }

    private class GetProductTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("GetProductTask", "started");
        }

        protected String doInBackground(String... param) {
            Log.i("GetProductTask", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            if(resp != null){
                try{
                    JSONArray array = new JSONArray(resp);
                    groomList = new ArrayList<ProductBO>();
                    for(int i=0; i<array.length(); i++){
                        JSONObject singleObj = array.getJSONObject(i);
                        JSONArray imgs = singleObj.getJSONArray("images");
                        String url = imgs.length() > 0 ? imgs.getJSONObject(0).getString("src") : "";
                        Log.i("utttttttt", url+"");
                        groomList.add(new ProductBO(singleObj.getInt("id"), singleObj.getString("name"), url, singleObj.getInt("average_rating"), singleObj.getInt("rating_count"), singleObj.getDouble("price"), false, 10.5, singleObj.getString("type")));
                    }
                    ArrayList<ProductBO> favlist = dbCon.getFavoritesList();
                    for(int j=0; j<favlist.size(); j++){
                        for(int k=0; k<groomList.size(); k++){
                            if(groomList.get(k).getId() == favlist.get(j).getId()){
                                groomList.get(k).setIsAddedBag(true);
                            }
                        }
                    }
                    adapter = new HomeTopAdapter(MainActivity.this, R.layout.homeproduct_item, groomList);
                    groomProductListView.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(groomProductListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(groomProductListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    public void showrateus(Context context) {
        final Dialog open1 = new Dialog(context);
        open1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View popup = getLayoutInflater().inflate(R.layout.rate_us, null);
        LinearLayout happy=(LinearLayout)popup.findViewById(R.id.happy);
        LinearLayout bad=(LinearLayout)popup.findViewById(R.id.bad);
        open1.setContentView(popup);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width= (int) (displaymetrics.widthPixels*1);
        open1.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        open1.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lparam = new WindowManager.LayoutParams();
        lparam.copyFrom(open1.getWindow().getAttributes());

        open1.getWindow().setLayout((int) (width- 40), ActionBar.LayoutParams.WRAP_CONTENT);
        open1.show();
        bad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "mohana@stutzen.me" });
                    startActivity(Intent.createChooser(intent, ""));
                }
                catch(NullPointerException e){
                    Toast.makeText(getApplicationContext(), "No application can perform this operation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        happy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=co.stutzen.shopzen"));
                startActivity(browserIntent);
            }
        });
    }
}
