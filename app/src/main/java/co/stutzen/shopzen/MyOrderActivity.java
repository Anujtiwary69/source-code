package co.stutzen.shopzen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.stutzen.shopzen.adapters.MyOrderAdapter;
import co.stutzen.shopzen.bo.OrderBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.database.DBController;

public class MyOrderActivity extends AppCompatActivity {

    private ArrayList<OrderBO> orderList;
    private ListView orderListView;
    private DBController dbCon;
    private ProgressBar progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        progbar = (ProgressBar) findViewById(R.id.progbar);
        dbCon = new DBController(MyOrderActivity.this);
        orderList = new ArrayList<OrderBO>();
        orderListView = (ListView) findViewById(R.id.myorderlist);
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        progbar.setVisibility(View.VISIBLE);
        GetProductTask task = new GetProductTask();
        task.execute(AppConstants.CREATE_ORDER+"&customer="+dbCon.getCustomerId()+"&per_page=100");
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyOrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("id", orderList.get(i).getOrderId());
                intent.putExtra("amount", orderList.get(i).getAmount());
                intent.putExtra("date", orderList.get(i).getOrderDate());
                intent.putExtra("status", orderList.get(i).getStatus());
                intent.putExtra("payment", orderList.get(i).getPayment());
                intent.putExtra("mainid", orderList.get(i).getMainid());
                startActivity(intent);
            }
        });
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
            Log.i("REspppp", resp + "");
            progbar.setVisibility(View.GONE);
            if(resp != null){
                try{
                    JSONArray array = new JSONArray(resp);
                    orderList = new ArrayList<OrderBO>();
                    for(int i=0; i<array.length(); i++){
                        JSONObject singleObj = array.getJSONObject(i);
                        orderList.add(new OrderBO(singleObj.getString("number"), singleObj.getString("date_created"), singleObj.getDouble("total"), singleObj.getString("status"), singleObj.getString("payment_method_title"), singleObj.getInt("id")));
                    }
                    MyOrderAdapter adapter = new MyOrderAdapter(MyOrderActivity.this, R.layout.order_item, orderList);
                    orderListView.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(orderListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(orderListView, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
