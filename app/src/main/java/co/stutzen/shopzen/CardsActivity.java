package co.stutzen.shopzen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.stutzen.shopzen.adapters.CardAdapter;
import co.stutzen.shopzen.adapters.CartAdapter;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.database.DBController;

public class CardsActivity extends AppCompatActivity {

    private ArrayList<String> cardList;
    private ListView productListView;
    private DBController dbCon;
    private LinearLayout cartemptylay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        dbCon = new DBController(CardsActivity.this);
        cardList = new ArrayList<String>();
        cardList.add("**** **** **** 4334");
        cardList.add("**** **** **** 5454");
        cardList.add("**** **** **** 4555");
        cardList.add("**** **** **** 3434");
        cardList.add("**** **** **** 5555");
        cardList.add("**** **** **** 5445");
        cardList.add("**** **** **** 5666");
        productListView = (ListView) findViewById(R.id.productlist);
        CardAdapter adapter = new CardAdapter(CardsActivity.this, R.layout.card_item, cardList);
        productListView.setAdapter(adapter);
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartemptylay = (LinearLayout) findViewById(R.id.cartemptylay);
        if(cardList != null && cardList.size() > 0){
            cartemptylay.setVisibility(View.GONE);
            productListView.setVisibility(View.VISIBLE);
        }else{
            cartemptylay.setVisibility(View.VISIBLE);
            productListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void setEmptyCart(List<String> list) {
        if(list != null && list.size() > 0){
            cartemptylay.setVisibility(View.GONE);
            productListView.setVisibility(View.VISIBLE);
        }else{
            cartemptylay.setVisibility(View.VISIBLE);
            productListView.setVisibility(View.GONE);
        }
    }
}
