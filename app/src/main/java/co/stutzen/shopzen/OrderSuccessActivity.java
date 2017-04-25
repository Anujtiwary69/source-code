package co.stutzen.shopzen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.stutzen.shopzen.database.DBController;

public class OrderSuccessActivity extends AppCompatActivity {

    private DBController dbCon;
    private TextView placeorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        dbCon = new DBController(OrderSuccessActivity.this);
        dbCon.dropCart();
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        placeorder = (TextView) findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderSuccessActivity.this, MainActivity.class));
            }
        });
        TextView amounttopay = (TextView) findViewById(R.id.amt);
        amounttopay.setText(String.format("%.0f", getIntent().getDoubleExtra("amount", 0)) + " Rs");
        TextView orderno = (TextView) findViewById(R.id.orderno);
        orderno.setText("REF NO:  "+getIntent().getStringExtra("number"));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrderSuccessActivity.this, MainActivity.class));
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
