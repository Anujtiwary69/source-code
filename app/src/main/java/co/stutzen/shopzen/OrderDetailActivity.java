package co.stutzen.shopzen;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.stutzen.shopzen.adapters.CartAdapter;
import co.stutzen.shopzen.adapters.MyOrderAdapter;
import co.stutzen.shopzen.adapters.OrderAdapter;
import co.stutzen.shopzen.bo.OrderBO;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.custom.Helper;
import co.stutzen.shopzen.database.DBController;

public class OrderDetailActivity extends AppCompatActivity {

    private ArrayList<ProductBO> cartList;
    private ListView productListView;
    private DBController dbCon;
    private TextView placeorder;
    private TextView orderplaced;
    private TextView orderid;
    private TextView orderqty;
    private TextView estimatedate;
    private View shipline1;
    private View shipline2;
    private ImageView shipgreen;
    private ImageView shipblack;
    private ImageView deliblack;
    private ImageView deligreen;
    private View deliline1;
    private ProgressBar progbar;
    private ScrollView scrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        dbCon = new DBController(OrderDetailActivity.this);
        cartList = new ArrayList<ProductBO>();
        cartList = dbCon.getCartList();
        productListView = (ListView) findViewById(R.id.productlist);
        progbar = (ProgressBar) findViewById(R.id.progbar);
        placeorder = (TextView) findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderDetailActivity.this, PlaceOrderActivity.class));
            }
        });
        scrl = (ScrollView)findViewById(R.id.scrl);
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        orderplaced = (TextView)findViewById(R.id.orderplaced);
        orderid = (TextView)findViewById(R.id.orderid);
        orderqty = (TextView)findViewById(R.id.orderqty);
        estimatedate = (TextView)findViewById(R.id.estimatedate);

        shipline1 = (View)findViewById(R.id.shipline1);
        shipline2 = (View)findViewById(R.id.shipline2);
        shipgreen = (ImageView)findViewById(R.id.shipgreen);
        shipblack = (ImageView)findViewById(R.id.shipblack);

        deliblack = (ImageView)findViewById(R.id.deliblack);
        deligreen = (ImageView)findViewById(R.id.deligreen);
        deliline1 = (View)findViewById(R.id.deliline1);
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("d MMM yyyy hh:mm a");
            Date date = format1.parse(getIntent().getStringExtra("date"));
            orderplaced.setText(format2.format(date));
        }catch(Exception e){
            e.printStackTrace();
        }
        orderid.setText(getIntent().getStringExtra("id"));
        orderqty.setText("Total:  "+String.format("%.2f", getIntent().getDoubleExtra("amount", 0)));
        estimatedate.setText(getIntent().getStringExtra("payment"));

        if(getIntent().getStringExtra("status").equalsIgnoreCase("processing")){
            shipline1.setBackgroundColor(Color.parseColor("#6bc16a"));
            shipline2.setBackgroundColor(Color.parseColor("#6bc16a"));
            shipgreen.setVisibility(View.VISIBLE);
            shipblack.setVisibility(View.GONE);
        }else if(getIntent().getStringExtra("status").equalsIgnoreCase("completed")){
            shipline1.setBackgroundColor(Color.parseColor("#6bc16a"));
            shipline2.setBackgroundColor(Color.parseColor("#6bc16a"));
            shipgreen.setVisibility(View.VISIBLE);
            shipblack.setVisibility(View.GONE);

            deliline1.setBackgroundColor(Color.parseColor("#6bc16a"));
            deligreen.setVisibility(View.VISIBLE);
            deliblack.setVisibility(View.GONE);
        }else{
            shipline1.setBackgroundColor(Color.parseColor("#dedede"));
            shipline2.setBackgroundColor(Color.parseColor("#dedede"));
            shipgreen.setVisibility(View.GONE);
            shipblack.setVisibility(View.VISIBLE);

            deliline1.setBackgroundColor(Color.parseColor("#dedede"));
            deligreen.setVisibility(View.GONE);
            deliblack.setVisibility(View.VISIBLE);
        }
        GetProductTask task = new GetProductTask();
        task.execute(AppConstants.ORDER_RETIREVE_API + getIntent().getIntExtra("mainid", 0)+"?"+AppConstants.cust_keysecret);
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
            Log.i("REspppp", resp+"");
            progbar.setVisibility(View.GONE);
            if(resp != null){
                try{
                    JSONObject json = new JSONObject(resp);
                    cartList = new ArrayList<ProductBO>();
                    JSONArray array =  json.getJSONArray("line_items");
                    for(int i=0; i<array.length(); i++){
                        JSONObject singleObj = array.getJSONObject(i);
                        cartList.add(new ProductBO(singleObj.getInt("id"), singleObj.getString("name"), "", singleObj.getInt("quantity"), 0, singleObj.getDouble("total"), false, singleObj.getDouble("price"),"" ));
                    }
                    OrderAdapter adapter = new OrderAdapter(OrderDetailActivity.this, R.layout.detail_item, cartList);
                    productListView.setAdapter(adapter);
                    Helper.getListViewSize(productListView);
                    scrl.smoothScrollTo(0,0);
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(productListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(productListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }


}
