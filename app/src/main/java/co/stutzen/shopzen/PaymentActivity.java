package co.stutzen.shopzen;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.database.DBController;

public class PaymentActivity extends AppCompatActivity {

    private DBController dbCon;
    private TextView placeorder;
    private String pay_type;
    private String pay_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payorder);
        dbCon = new DBController(PaymentActivity.this);
        placeorder = (TextView) findViewById(R.id.placeorder);
        TextView shiptoaddress = (TextView) findViewById(R.id.shiptoaddress);
        LinearLayout backtoaddress = (LinearLayout) findViewById(R.id.backtoaddress);
        LinearLayout codclick = (LinearLayout) findViewById(R.id.codclick);
        final ImageView codcircle = (ImageView) findViewById(R.id.codcircle);

        LinearLayout payuclick = (LinearLayout) findViewById(R.id.payuclick);
        final ImageView payucircle = (ImageView) findViewById(R.id.payucircle);
        LinearLayout paytmclick = (LinearLayout) findViewById(R.id.paytmclick);
        final ImageView paytmcircle = (ImageView) findViewById(R.id.paytmcircle);
        LinearLayout walletclick = (LinearLayout) findViewById(R.id.walletclick);
        final ImageView walletcircle = (ImageView) findViewById(R.id.walletcircle);
        LinearLayout paypalclick = (LinearLayout) findViewById(R.id.paypalclick);
        final ImageView paypalcircle = (ImageView) findViewById(R.id.paypalcircle);
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        backtoaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        pay_type = "paypal";
        pay_title = "Paypal";
        codclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalcircle.setVisibility(View.GONE);
                walletcircle.setVisibility(View.GONE);
                paytmcircle.setVisibility(View.GONE);
                payucircle.setVisibility(View.GONE);
                codcircle.setVisibility(View.VISIBLE);
                pay_type = "cod";
                pay_title = "Cash On Delivery";
            }
        });

        payuclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalcircle.setVisibility(View.GONE);
                walletcircle.setVisibility(View.GONE);
                paytmcircle.setVisibility(View.GONE);
                payucircle.setVisibility(View.VISIBLE);
                codcircle.setVisibility(View.GONE);
                pay_type = "payu";
                pay_title = "Payu";
            }
        });
        walletclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalcircle.setVisibility(View.GONE);
                walletcircle.setVisibility(View.VISIBLE);
                paytmcircle.setVisibility(View.GONE);
                payucircle.setVisibility(View.GONE);
                codcircle.setVisibility(View.GONE);
                pay_type = "COD";
                pay_title = "Cash On Delivery";
            }
        });
        paytmclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalcircle.setVisibility(View.GONE);
                walletcircle.setVisibility(View.GONE);
                paytmcircle.setVisibility(View.VISIBLE);
                payucircle.setVisibility(View.GONE);
                codcircle.setVisibility(View.GONE);
                pay_type = "paytm";
                pay_title = "Paytm";
            }
        });
        paypalclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalcircle.setVisibility(View.VISIBLE);
                walletcircle.setVisibility(View.GONE);
                paytmcircle.setVisibility(View.GONE);
                payucircle.setVisibility(View.GONE);
                codcircle.setVisibility(View.GONE);
                pay_type = "Paypal";
                pay_title = "Paypal";
            }
        });
        TextView amounttopay = (TextView) findViewById(R.id.amounttopay);
        amounttopay.setText(String.format("%.0f", getIntent().getDoubleExtra("amount", 0)) + " Rs to pay");
        shiptoaddress.setText(getIntent().getStringExtra("address"));

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**if (paypalcircle.getVisibility() == View.VISIBLE) {
                    String client_tkn = AppConstants.gateway.clientToken().generate();
                    Intent intent = new Intent(PaymentActivity.this, BraintreePaymentActivity.class);
                    intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, client_tkn);
                    startActivityForResult(intent, 1);
                } else {*/
                if(paypalcircle.getVisibility() == View.VISIBLE || walletcircle.getVisibility() == View.VISIBLE || paytmcircle.getVisibility() == View.VISIBLE
                        || codcircle.getVisibility() == View.VISIBLE || payucircle.getVisibility() == View.VISIBLE){
                    placeorder.setText("Processing...");
                    CreateOrderTask task = new CreateOrderTask();
                    task.execute();
                }else{
                    Snackbar.make(paypalcircle, "Select atleast one payment method.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                    /***/
                //}
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                    String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                    Intent intent = new Intent(PaymentActivity.this, OrderSuccessActivity.class);
                    intent.putExtra("amount", getIntent().getDoubleExtra("amount", 0));
                    startActivity(intent);
                }
                break;
        }
    }

    private class CreateOrderTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("CreateOrderTask", "started");
        }

        protected String doInBackground(String... param) {
            String response = null;
            try {
                Connection connection = new Connection();
                JSONObject jobj = new JSONObject();
                jobj.put("payment_method", pay_type);
                jobj.put("customer_id", dbCon.getCustomerId());
                jobj.put("payment_method_title", pay_title);
                jobj.put("set_paid", true);
                JSONObject billshipaddress = new JSONObject();
                billshipaddress.put("first_name", getIntent().getStringExtra("fname"));
                billshipaddress.put("last_name", getIntent().getStringExtra("lname"));
                billshipaddress.put("address_1", getIntent().getStringExtra("addr1"));
                billshipaddress.put("address_2", getIntent().getStringExtra("addr2"));
                billshipaddress.put("city", getIntent().getStringExtra("city"));
                billshipaddress.put("state", getIntent().getStringExtra("state"));
                billshipaddress.put("country", getIntent().getStringExtra("country"));
                billshipaddress.put("postcode", getIntent().getStringExtra("pincode"));
                billshipaddress.put("phone", getIntent().getStringExtra("mobile"));
                jobj.put("billing", billshipaddress);
                jobj.put("shipping", billshipaddress);
                ArrayList<ProductBO> cartdata = dbCon.getCartList();
                JSONArray jProduct = new JSONArray();
                for(int i=0; i<cartdata.size(); i++){
                    JSONObject product = new JSONObject();
                    product.put("product_id", cartdata.get(i).getId());
                    product.put("quantity", cartdata.get(i).getQuantity());
                    if(!cartdata.get(i).getType().equalsIgnoreCase("simple"))
                        product.put("variation_id", cartdata.get(i).getVariationId());
                    jProduct.put(product);
                }
                jobj.put("line_items", jProduct);
                JSONArray shiparray = new JSONArray();
                JSONObject shipjson = new JSONObject();
                shipjson.put("method_id", "flat_rate");
                shipjson.put("method_title", "Flat Rate");
                shiparray.put(shipjson);
                jobj.put("shipping_lines", shiparray);
                Log.i("order request", AppConstants.CREATE_ORDER+"===="+jobj);
                response = connection.sendHttpPostjson(AppConstants.CREATE_ORDER, jobj, "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            placeorder.setText("Pay Now");
            Log.i("respppppp", resp + "");
            if(resp != null){
                try{
                    JSONObject jobj = new JSONObject(resp);
                    if(jobj.has("id") && jobj.getInt("id") > 0){
                        Intent intent = new Intent(PaymentActivity.this, OrderSuccessActivity.class);
                        intent.putExtra("amount", jobj.getDouble("total"));
                        intent.putExtra("number", jobj.getString("number"));
                        startActivity(intent);
                    }else{
                        if(jobj.has("message"))
                            Snackbar.make(placeorder, jobj.getString("message"), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        else
                            Snackbar.make(placeorder, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(placeorder, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(placeorder, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
