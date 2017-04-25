package co.stutzen.shopzen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import co.stutzen.shopzen.adapters.CartAdapter;
import co.stutzen.shopzen.adapters.FavoritesAdapter;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.database.DBController;

public class MyFavoritesActivity extends AppCompatActivity {

    private ArrayList<ProductBO> favoriteList;
    private ListView productListView;
    private DBController dbCon;
    private LinearLayout cartemptylay;
    private TextView startshopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfavorites);
        dbCon = new DBController(MyFavoritesActivity.this);
        favoriteList = new ArrayList<ProductBO>();
        favoriteList = dbCon.getFavoritesList();
        productListView = (ListView) findViewById(R.id.productlist);
        FavoritesAdapter adapter = new FavoritesAdapter(MyFavoritesActivity.this, R.layout.myfavorite_item, favoriteList);
        productListView.setAdapter(adapter);
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartemptylay = (LinearLayout) findViewById(R.id.cartemptylay);
        if(favoriteList != null && favoriteList.size() > 0){
            cartemptylay.setVisibility(View.GONE);
            productListView.setVisibility(View.VISIBLE);
        }else{
            cartemptylay.setVisibility(View.VISIBLE);
            productListView.setVisibility(View.GONE);
        }
        startshopping = (TextView) findViewById(R.id.startshopping);
        startshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyFavoritesActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void setEmptyCart() {
        favoriteList = dbCon.getFavoritesList();
        if(favoriteList != null && favoriteList.size() > 0){
            cartemptylay.setVisibility(View.GONE);
            productListView.setVisibility(View.VISIBLE);
        }else{
            cartemptylay.setVisibility(View.VISIBLE);
            productListView.setVisibility(View.GONE);
        }
    }
}
