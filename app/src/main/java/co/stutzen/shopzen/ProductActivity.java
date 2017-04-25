package co.stutzen.shopzen;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.stutzen.shopzen.adapters.CategoryAdapter;
import co.stutzen.shopzen.adapters.FilterAdapter;
import co.stutzen.shopzen.adapters.FilterMenuAdapter;
import co.stutzen.shopzen.adapters.ProductAdapter;
import co.stutzen.shopzen.adapters.SortAdapter;
import co.stutzen.shopzen.bo.CategoryBO;
import co.stutzen.shopzen.bo.ColorBO;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.bo.SortBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.CommonFunctions;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.database.DBController;

public class ProductActivity extends AppCompatActivity {

    private ArrayList<ProductBO> groomList;
    private GridView productGridView;
    private ListView productListView;
    private DBController dbCon;
    private Client client;
    private JSONObject searchTopResp;
    private ArrayList<ColorBO> brandFilterList;
    private ListView brandlistview;
    private LinearLayout pricefilterlay;
    private ArrayList<ColorBO> priceFilterList;
    private ProgressBar progbar;
    private String sorttext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new Client(AppConstants.ALGOLIA_APP_ID, AppConstants.ALGOLIA_APP_KEY);
        setContentView(R.layout.activity_product);
        dbCon = new DBController(ProductActivity.this);
        groomList = new ArrayList<ProductBO>();
        progbar = (ProgressBar) findViewById(R.id.progbar);
        LinearLayout listtogridclick = (LinearLayout) findViewById(R.id.listtogridclick);
        LinearLayout refineclick = (LinearLayout) findViewById(R.id.refineclick);
        LinearLayout sortclick = (LinearLayout) findViewById(R.id.sortclick);
        final ImageView listogridicon = (ImageView) findViewById(R.id.listogridicon);
        listtogridclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productGridView.getVisibility() == View.VISIBLE) {
                    productGridView.setVisibility(View.GONE);
                    productListView.setVisibility(View.VISIBLE);
                    listogridicon.setImageResource(R.mipmap.list2grid);
                } else {
                    productGridView.setVisibility(View.VISIBLE);
                    productListView.setVisibility(View.GONE);
                    listogridicon.setImageResource(R.mipmap.gridtolist);
                }
            }
        });
        refineclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup();
            }
        });
        sortclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingItem();
            }
        });
        productGridView = (GridView) findViewById(R.id.productgrid);

        productListView = (ListView) findViewById(R.id.productlist);

        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductActivity.this, SingleProductActivity.class);
                intent.putExtra("id", groomList.get(i).getId());
                intent.putExtra("name", groomList.get(i).getName());
                intent.putExtra("image", groomList.get(i).getImage());
                intent.putExtra("price", groomList.get(i).getAmount());
                intent.putExtra("type", groomList.get(i).getType());
                startActivity(intent);
            }
        });

        productGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductActivity.this, SingleProductActivity.class);
                intent.putExtra("id", groomList.get(i).getId());
                intent.putExtra("name", groomList.get(i).getName());
                intent.putExtra("image", groomList.get(i).getImage());
                intent.putExtra("price", groomList.get(i).getAmount());
                intent.putExtra("type", groomList.get(i).getType());
                startActivity(intent);
            }
        });
        progbar.setVisibility(View.VISIBLE);
        GetCategoryTask task = new GetCategoryTask();
        task.execute(AppConstants.CATEGORY_LIST_API+"&per_page=100");

        GetProductTask task1 = new GetProductTask();
        task1.execute(AppConstants.PRODUCT_LIST_API+"&category="+getIntent().getIntExtra("category", 0)+"&per_page=100");
       // getProductData();
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            ArrayList<ProductBO> cartList = dbCon.getCartList();
            for (int j = 0; j < groomList.size(); j++) {
                groomList.get(j).setIsAddedBag(false);
                groomList.get(j).setQuantity(0);
            }
                for (int j = 0; j < groomList.size(); j++) {
                    for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getId() == groomList.get(j).getId()) {
                        groomList.get(j).setIsAddedBag(true);
                        groomList.get(j).setQuantity(cartList.get(i).getQuantity());
                    }
                }
            }
            ProductAdapter adapter = new ProductAdapter(ProductActivity.this, R.layout.productgrid_item, groomList, false);
            productGridView.setAdapter(adapter);
            ProductAdapter adapter1 = new ProductAdapter(ProductActivity.this, R.layout.productlist_item, groomList, true);
            productListView.setAdapter(adapter1);
        }catch (Exception e){}
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
            progbar.setVisibility(View.GONE);
            if(resp != null){
                try{
                    JSONArray array = new JSONArray(resp);
                    groomList = new ArrayList<ProductBO>();
                    for(int i=0; i<array.length(); i++){
                        JSONObject singleObj = array.getJSONObject(i);
                        JSONArray imgs = singleObj.getJSONArray("images");
                        String url = imgs.length() > 0 ? imgs.getJSONObject(0).getString("src") : "";
                        groomList.add(new ProductBO(singleObj.getInt("id"), singleObj.getString("name"), url, singleObj.getInt("average_rating"), singleObj.getInt("rating_count"), singleObj.getDouble("price"), false, 10.5, singleObj.getString("type")));
                    }
                    ArrayList<ProductBO> cartList = dbCon.getCartList();
                        for (int j = 0; j < groomList.size(); j++) {
                            for (int i = 0; i < cartList.size(); i++) {
                            if (cartList.get(i).getId() == groomList.get(j).getId()) {
                                groomList.get(j).setIsAddedBag(true);
                                groomList.get(j).setQuantity(cartList.get(i).getQuantity());
                            }
                        }
                    }
                    ProductAdapter adapter = new ProductAdapter(ProductActivity.this, R.layout.productgrid_item, groomList, false);
                    productGridView.setAdapter(adapter);
                    ProductAdapter adapter1 = new ProductAdapter(ProductActivity.this, R.layout.productlist_item, groomList, true);
                    productListView.setAdapter(adapter1);
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(productGridView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(productGridView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    public void getProductData(){
        progbar.setVisibility(View.VISIBLE);
        Index index = client.initIndex("shopzen_products");
        Query query = new Query();
        query.setFacets("*");
        query.setFilters("categories:\""+getIntent().getStringExtra("category")+"\"");
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                if( e == null){
                    progbar.setVisibility(View.GONE);
                    try{
                        searchTopResp = jsonObject;
                        JSONArray array = jsonObject.getJSONArray("hits");
                        if(array.length() > 0){
                            groomList = new ArrayList<ProductBO>();
                            for(int i=0; i<array.length(); i++){
                                JSONObject singleObj = array.getJSONObject(i);
                                groomList.add(new ProductBO(singleObj.getInt("objectID"), singleObj.getString("name"), singleObj.getString("image"), singleObj.getInt("rating"), singleObj.getInt("popularity"), singleObj.getDouble("price"), false, 10.5, singleObj.getString("type")));
                            }
                            ProductAdapter adapter = new ProductAdapter(ProductActivity.this, R.layout.productgrid_item, groomList, false);
                            productGridView.setAdapter(adapter);
                            ProductAdapter adapter1 = new ProductAdapter(ProductActivity.this, R.layout.productlist_item, groomList, true);
                            productListView.setAdapter(adapter1);
                            JSONObject facets = jsonObject.getJSONObject("facets");
                            JSONObject branddata = facets.getJSONObject("brand");
                            brandFilterList = new ArrayList<ColorBO>();
                            Iterator<String> iter = branddata.keys();
                            while (iter.hasNext()) {
                                ColorBO bo = new ColorBO();
                                String key = iter.next();
                                bo.setCatcolor(key);
                                try {
                                    int value = (int) branddata.get(key);
                                    bo.setCount(value);
                                } catch (JSONException e2) {
                                    // Something went wrong!
                                }
                                brandFilterList.add(bo);
                            }
                            JSONObject pricedata = facets.getJSONObject("price_range");
                            priceFilterList = new ArrayList<ColorBO>();
                            Iterator<String> iter1 = pricedata.keys();
                            while (iter1.hasNext()) {
                                ColorBO bo = new ColorBO();
                                String key = iter1.next();
                                bo.setCatcolor(key);
                                try {
                                    int value = (int) pricedata.get(key);
                                    bo.setCount(value);
                                } catch (JSONException e2) {
                                    // Something went wrong!
                                }
                                priceFilterList.add(bo);
                            }
                        }else{
                            Snackbar.make(productListView, "No products found.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }catch (Exception e1){
                        e1.printStackTrace();
                        Snackbar.make(productListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else{
                    progbar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Snackbar.make(productListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    public void loadViewFilter(int pos) {
        try{
            if(pos == 0){
                brandlistview.setVisibility(View.VISIBLE);
                pricefilterlay.setVisibility(View.GONE);
            }else if(pos == 1){
                brandlistview.setVisibility(View.GONE);
                pricefilterlay.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){

        }
    }

    public void showFilterPopup() {
        final Dialog open = new Dialog(ProductActivity.this);
        open.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View popup = LayoutInflater.from(ProductActivity.this).inflate(R.layout.filter_layout, null);
        ListView menufilter =(ListView)popup.findViewById(R.id.filtermenulist);
        brandlistview =(ListView)popup.findViewById(R.id.brandlistview);
        FilterMenuAdapter adapterMenu = new FilterMenuAdapter(ProductActivity.this, R.layout.filtermenu_item, CommonFunctions.getFilterMenu());
        open.setContentView(popup);
        open.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        open.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width= (int) displaymetrics.widthPixels;
        int height= (int) displaymetrics.heightPixels;
        open.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        open.show();
        menufilter.setAdapter(adapterMenu);
        FilterAdapter adapterBrand = new FilterAdapter(ProductActivity.this, R.layout.filtersub_item, brandFilterList);
        brandlistview.setAdapter(adapterBrand);
        LinearLayout closeclick =(LinearLayout)popup.findViewById(R.id.closeclick);
        TextView applyfilter =(TextView)popup.findViewById(R.id.applyfilter);
        TextView clearfilter =(TextView)popup.findViewById(R.id.clearfilter);
        applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open.dismiss();
                String brand = "";
                for (int i = 0; i < brandFilterList.size(); i++) {
                    if (brandFilterList.get(i).isSelected()) {
                        if (brand.trim().length() == 0)
                            brand = brandFilterList.get(i).getCount()+"";
                        else
                            brand = brand + "," + brandFilterList.get(i).getCount();
                    }
                }
                progbar.setVisibility(View.VISIBLE);
                GetProductTask task = new GetProductTask();
                task.execute(AppConstants.PRODUCT_LIST_API + "&per_page=100"+(brand.trim().length() > 0 ? "&category="+brand : ""));
                //getProductFilteredData();
            }
        });
        clearfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open.dismiss();
                GetProductTask task = new GetProductTask();
                task.execute(AppConstants.PRODUCT_LIST_API+"&per_page=100");
            }
        });
        closeclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open.dismiss();
            }
        });
    }

    public void getProductFilteredData() {
        progbar.setVisibility(View.VISIBLE);
        Index index = client.initIndex("shopzen_products");
        Query query = new Query();
        query.setFacets("*");
        String brand = "";
        String price = "";
        for (int i = 0; i < brandFilterList.size(); i++) {
            if (brandFilterList.get(i).isSelected()) {
                if (brand.trim().length() == 0)
                    brand = "brand:\"" + brandFilterList.get(i).getCatcolor() + "\"";
                else
                    brand = brand + " OR brand:\"" + brandFilterList.get(i).getCatcolor() + "\"";
            }
        }
        for (int i = 0; i < priceFilterList.size(); i++) {
            if (priceFilterList.get(i).isSelected()) {
                if (price.trim().length() == 0)
                    price = "price_range:\"" + priceFilterList.get(i).getCatcolor() + "\"";
                else
                    price = price + " OR price_range:\"" + priceFilterList.get(i).getCatcolor() + "\"";
            }
        }
        String filterString = brand.trim().length() == 0 ? (price.trim().length() == 0 ? "" : price) : (price.trim().length() == 0 ? brand : "(" + brand + ") AND (" + price + ")");
        Log.i("filterString", filterString + "");
        if (filterString.trim().length() > 0)
            query.setFilters("categories:\"" + getIntent().getStringExtra("category") + "\" AND (" + filterString + ")");
        else
            query.setFilters("categories:\"" + getIntent().getStringExtra("category") + "\"");
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                if (e == null) {
                    progbar.setVisibility(View.GONE);
                    try {
                        searchTopResp = jsonObject;
                        JSONArray array = jsonObject.getJSONArray("hits");
                        if (array.length() > 0) {
                            groomList = new ArrayList<ProductBO>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject singleObj = array.getJSONObject(i);
                                groomList.add(new ProductBO(singleObj.getInt("objectID"), singleObj.getString("name"), singleObj.getString("image"), singleObj.getInt("rating"), singleObj.getInt("popularity"), singleObj.getDouble("price"), false, 10.5, singleObj.getString("type")));
                            }
                            ProductAdapter adapter = new ProductAdapter(ProductActivity.this, R.layout.productgrid_item, groomList, false);
                            productGridView.setAdapter(adapter);
                            ProductAdapter adapter1 = new ProductAdapter(ProductActivity.this, R.layout.productlist_item, groomList, true);
                            productListView.setAdapter(adapter1);
                        } else {
                            Snackbar.make(productListView, "No products found.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Snackbar.make(productListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    progbar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Snackbar.make(productListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private class GetCategoryTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("GetCategoryTask", "started");
        }

        protected String doInBackground(String... param) {
            Log.i("GetCategoryTask", param[0]);
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
                    brandFilterList = new ArrayList<ColorBO>();
                    for(int i =0; i<array.length(); i++){
                        JSONObject jobj = array.getJSONObject(i);
                        ColorBO bo = new ColorBO();
                        bo.setCount(jobj.getInt("id"));
                        bo.setCatcolor(jobj.getString("name"));
                        if(getIntent().getIntExtra("category", 0) == jobj.getInt("id"))
                            bo.setSelected(true);
                        brandFilterList.add(bo);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
            }
        }
    }

        public void sortingItem(){
            final Dialog open1 = new Dialog(ProductActivity.this);
            open1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View popup = getLayoutInflater().inflate(R.layout.sort_dialog, null);
            ListView sortlistvw = (ListView) popup.findViewById(R.id.sortlist);
            open1.setContentView(popup);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = (int) (displaymetrics.widthPixels * 1);
            open1.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            open1.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams lparam = new WindowManager.LayoutParams();
            lparam.copyFrom(open1.getWindow().getAttributes());

            open1.getWindow().setLayout((int) (width * 0.83),
                    ActionBar.LayoutParams.WRAP_CONTENT);
            open1.show();
            final ArrayList<SortBO> list = new ArrayList<SortBO>();
            String sortitem[] = { "Popularity", "Price - Low to High",
                    "Price - High to Low", "Newest First" };
            for (int i = 0; i < sortitem.length; i++) {
                SortBO bo = new SortBO();
                bo.setSorttype(sortitem[i]);
                list.add(bo);
            }
            final SortAdapter adap = new SortAdapter(getApplicationContext(),
                    R.layout.sortby_item, list);
            sortlistvw.setAdapter(adap);
            for (int k = 0; k < sortitem.length; k++) {
                if (sorttext.equalsIgnoreCase(sortitem[k])) {
                    adap.select(k);
                    adap.notifyDataSetChanged();
                }
            }
            sortlistvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    adap.select(arg2);
                    adap.notifyDataSetChanged();
                    sorttext = list.get(arg2).getSorttype();
                    open1.dismiss();
                }
            });
        }
    }

