package co.stutzen.shopzen;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

import co.stutzen.shopzen.adapters.CategoryAdapter;
import co.stutzen.shopzen.adapters.ProductAdapter;
import co.stutzen.shopzen.bo.CategoryBO;
import co.stutzen.shopzen.bo.ColorBO;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;

public class CategoryActivity extends Activity {

    private ArrayList<CategoryBO> categoryList;
    private GridView categoryGridView;
    private Client client;
    private ProgressBar progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryList = new ArrayList<CategoryBO>();
        progbar = (ProgressBar) findViewById(R.id.progbar);
        client = new Client(AppConstants.ALGOLIA_APP_ID, AppConstants.ALGOLIA_APP_KEY);
        categoryGridView = (GridView) findViewById(R.id.categrid);
        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(CategoryActivity.this, ProductActivity.class);
                in.putExtra("category", categoryList.get(i).getId());
                startActivity(in);
            }
        });
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
       // getCategoryData();
        progbar.setVisibility(View.VISIBLE);
        GetCategoryTask task = new GetCategoryTask();
        task.execute(AppConstants.CATEGORY_LIST_API+"&per_page=100");
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    public void getCategoryData(){
        progbar.setVisibility(View.VISIBLE);
        Index index = client.initIndex("shopzen_products");
        Query query = new Query();
        query.setFacets("*");
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                if (e == null) {
                    progbar.setVisibility(View.GONE);
                   try {
                        JSONArray array = jsonObject.getJSONArray("hits");
                        if (array.length() > 0) {
                            categoryList = new ArrayList<CategoryBO>();
                            int[] iconarray = {R.mipmap.lipstick, R.mipmap.cloth, R.mipmap.applicane, R.mipmap.baby, R.mipmap.car, R.mipmap.scooter, R.mipmap.computers, R.mipmap.electronice, R.mipmap.kitchen, R.mipmap.scientific, R.mipmap.jewel, R.mipmap.brief};
                            JSONObject facets = jsonObject.getJSONObject("facets");
                            JSONObject category = facets.getJSONObject("categories");
                            Iterator<String> iter = category.keys();
                            int i=1;
                            while (iter.hasNext()) {
                                String key = iter.next();
                                categoryList.add(new CategoryBO(key, iconarray[i%11]));
                                i++;
                            }
                            CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, R.layout.category_item, categoryList);
                            categoryGridView.setAdapter(adapter);
                        } else {
                            Snackbar.make(categoryGridView, "No categories found.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Snackbar.make(categoryGridView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    progbar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Snackbar.make(categoryGridView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
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
            progbar.setVisibility(View.GONE);
            if(resp != null){
                try{
                    JSONArray array = new JSONArray(resp);
                    categoryList = new ArrayList<CategoryBO>();
                    int[] iconarray = {R.mipmap.lipstick, R.mipmap.cloth, R.mipmap.applicane, R.mipmap.baby, R.mipmap.car, R.mipmap.scooter, R.mipmap.computers, R.mipmap.electronice, R.mipmap.kitchen, R.mipmap.scientific, R.mipmap.jewel, R.mipmap.brief};
                    for(int i =0; i<array.length(); i++){
                        JSONObject jobj = array.getJSONObject(i);
                        categoryList.add(new CategoryBO(jobj.getInt("id"), jobj.getString("name"), iconarray[i%11]));
                        CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, R.layout.category_item, categoryList);
                        categoryGridView.setAdapter(adapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(categoryGridView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(categoryGridView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
}
