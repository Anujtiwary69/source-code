package co.stutzen.shopzen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.stutzen.shopzen.database.DBController;

public class AddressActivity extends AppCompatActivity {

    private DBController dbCon;
    private TextView placeorder;
    private EditText fname;
    private EditText lname;
    private EditText address;
    private EditText address22;
    private EditText city;
    private EditText state;
    private EditText pincode;
    private EditText country;
    private EditText mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        dbCon = new DBController(AddressActivity.this);
        placeorder = (TextView) findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fname.getText().toString().trim().length() > 0 && address.getText().toString().trim().length() > 0
                        && city.getText().toString().trim().length() > 0 && state.getText().toString().trim().length() > 0
                        && pincode.getText().toString().trim().length() > 0 && country.getText().toString().trim().length() > 0
                        && mobile.getText().toString().trim().length() > 0){
                    Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                    intent.putExtra("amount", getIntent().getDoubleExtra("amount", 0));
                    intent.putExtra("fname", fname.getText().toString());
                    intent.putExtra("lname", lname.getText().toString());
                    intent.putExtra("addr1", address.getText().toString());
                    intent.putExtra("addr2", address22.getText().toString());
                    intent.putExtra("city", city.getText().toString());
                    intent.putExtra("state", state.getText().toString());
                    intent.putExtra("country", country.getText().toString());
                    intent.putExtra("pincode", pincode.getText().toString());
                    intent.putExtra("mobile", mobile.getText().toString());
                    intent.putExtra("address", fname.getText().toString()+(lname.getText().toString().trim().length() > 0 ? " "+lname.getText().toString() : "")
                    +", "+address.getText().toString()+(address22.getText().toString().trim().length() > 0 ? " "+address22.getText().toString() : "")
                    +", "+city.getText().toString()+", "+state.getText().toString()+", "+country.getText().toString()+"-"+pincode.getText().toString());
                    startActivity(intent);
                }else {
                    if(fname.getText().toString().trim().length() == 0)
                        fname.setError("Field must be filled");
                    if(address.getText().toString().trim().length() == 0)
                        address.setError("Field must be filled");
                    if(city.getText().toString().trim().length() == 0)
                        city.setError("Field must be filled");
                    if(state.getText().toString().trim().length() == 0)
                        state.setError("Field must be filled");
                    if(pincode.getText().toString().trim().length() == 0)
                        pincode.setError("Field must be filled");
                    if(country.getText().toString().trim().length() == 0)
                        country.setError("Field must be filled");
                    if(mobile.getText().toString().trim().length() == 0)
                        mobile.setError("Field must be filled");
                }
            }
        });
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        address = (EditText) findViewById(R.id.address);
        address22 = (EditText) findViewById(R.id.address22);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        pincode = (EditText) findViewById(R.id.pincode);
        country = (EditText) findViewById(R.id.country);
        mobile = (EditText) findViewById(R.id.mobile);
        LinearLayout backclick = (LinearLayout) findViewById(R.id.backclick);
        backclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}
