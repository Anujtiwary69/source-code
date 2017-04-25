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
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.database.DBController;

public class CartActivity extends AppCompatActivity {

    private ArrayList<ProductBO> cartList;
    private ListView productListView;
    private DBController dbCon;
    private TextView placeorder;
    private LinearLayout cartemptylay;
    private TextView startshopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbCon = new DBController(CartActivity.this);
        cartList = new ArrayList<ProductBO>();
        cartList = dbCon.getCartList();
        productListView = (ListView) findViewById(R.id.productlist);
        placeorder = (TextView) findViewById(R.id.placeorder);
        CartAdapter adapter = new CartAdapter(CartActivity.this, R.layout.cart_item, cartList);
        productListView.setAdapter(adapter);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, PlaceOrderActivity.class));
            }
        });
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartemptylay = (LinearLayout) findViewById(R.id.cartemptylay);
        if(cartList != null && cartList.size() > 0){
            cartemptylay.setVisibility(View.GONE);
            placeorder.setVisibility(View.VISIBLE);
            productListView.setVisibility(View.VISIBLE);
        }else{
            cartemptylay.setVisibility(View.VISIBLE);
            placeorder.setVisibility(View.GONE);
            productListView.setVisibility(View.GONE);
        }
        startshopping = (TextView) findViewById(R.id.startshopping);
        startshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void setEmptyCart() {
        cartList = dbCon.getCartList();
        if(cartList != null && cartList.size() > 0){
            cartemptylay.setVisibility(View.GONE);
            placeorder.setVisibility(View.VISIBLE);
            productListView.setVisibility(View.VISIBLE);
        }else{
            cartemptylay.setVisibility(View.VISIBLE);
            placeorder.setVisibility(View.GONE);
            productListView.setVisibility(View.GONE);
        }
    }
}
