package co.stutzen.shopzen;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.database.DBController;

public class PlaceOrderActivity extends AppCompatActivity {

    private DBController dbCon;
    private TextView placeorder;
    private TextView couponcode;
    private LinearLayout couponclick;
    private ImageView couponcheck;
    private ImageView giftwrapcheck;
    private TextView carttotal;
    private TextView subtotal;
    private TextView coupondiscount;
    private TextView amttopay;
    private double totalamount;
    private double subtotalamount;
    private double payableamount;
    private double giftamount;
    private double discountamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        dbCon = new DBController(PlaceOrderActivity.this);
        placeorder = (TextView) findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceOrderActivity.this, AddressActivity.class);
                intent.putExtra("amount", payableamount);
                startActivity(intent);
            }
        });
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        couponclick = (LinearLayout) findViewById(R.id.couponclick);
        LinearLayout giftclick = (LinearLayout) findViewById(R.id.giftclick);
        couponcheck = (ImageView) findViewById(R.id.couponcheck);
        giftwrapcheck = (ImageView) findViewById(R.id.giftwrapcheck);
        couponcode = (TextView) findViewById(R.id.couponcode);
        TextView giftwrapamounttext = (TextView) findViewById(R.id.giftwrapamounttext);
        TextView discountpertext = (TextView) findViewById(R.id.discountpertext);
        giftwrapamounttext.setText(AppConstants.giftWrapAmount+" Rs");
        discountpertext.setText(AppConstants.discountInPercentage+"%");
        couponclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(couponcheck.getVisibility() == View.VISIBLE){
                    couponcheck.setVisibility(View.GONE);
                    couponcode.setText("");
                    discountamount = 0;
                    coupondiscount.setText("-0 Rs");
                    payableamount = subtotalamount + giftamount - discountamount;
                    amttopay.setText(String.format("%.0f", payableamount)+" Rs");
                }else{
                    showCouponPopup();
                }
            }
        });
        giftclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(giftwrapcheck.getVisibility() == View.VISIBLE){
                    giftwrapcheck.setVisibility(View.GONE);
                    giftamount = 0;
                    payableamount = subtotalamount + giftamount - discountamount;
                    amttopay.setText(String.format("%.0f", payableamount) + " Rs");
                }else{
                    giftamount = AppConstants.giftWrapAmount;
                    payableamount = subtotalamount + giftamount - discountamount;
                    amttopay.setText(String.format("%.0f", payableamount) + " Rs");
                    giftwrapcheck.setVisibility(View.VISIBLE);
                }
            }
        });

        carttotal = (TextView) findViewById(R.id.carttotal);
        subtotal = (TextView) findViewById(R.id.subtotal);
        coupondiscount = (TextView) findViewById(R.id.coupondiscount);
        amttopay = (TextView) findViewById(R.id.amttopay);
        totalamount = dbCon.getCartTotal();
        subtotalamount = totalamount - (( totalamount * AppConstants.discountInPercentage)/100); //discount constant 10% in this template
        payableamount = subtotalamount;
        carttotal.setText(String.format("%.0f", totalamount)+" Rs");
        subtotal.setText(String.format("%.0f", subtotalamount)+" Rs");
        coupondiscount.setText("-0 Rs");
        discountamount = 0;
        amttopay.setText(String.format("%.0f", payableamount)+" Rs");
    }

    public void showCouponPopup() {

        final Dialog open = new Dialog(PlaceOrderActivity.this);
        open.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View popup = LayoutInflater.from(PlaceOrderActivity.this).inflate(R.layout.add_coupon, null);
        final EditText produtname=(EditText)popup.findViewById(R.id.txt);
        open.setContentView(popup);
        open.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        open.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width= (int) (displaymetrics.widthPixels*1);
        open.getWindow().setLayout(width - 50, ActionBar.LayoutParams.WRAP_CONTENT);
        open.show();
        TextView yes=(TextView)popup.findViewById(R.id.yes);
        TextView no=(TextView)popup.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(produtname.getText().toString().trim().length() > 0) {
                    couponcode.setText(produtname.getText().toString());
                    couponcheck.setVisibility(View.VISIBLE);
                    coupondiscount.setText("-500 Rs");
                    discountamount = 500;
                    payableamount = subtotalamount + giftamount - discountamount;
                    amttopay.setText(String.format("%.0f", payableamount)+" Rs");
                    open.dismiss();
                }else{
                    produtname.setError("Coupon code must not be empty.");
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                open.dismiss();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
