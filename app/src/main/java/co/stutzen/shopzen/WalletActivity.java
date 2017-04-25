package co.stutzen.shopzen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import co.stutzen.shopzen.adapters.CardAdapter;
import co.stutzen.shopzen.database.DBController;

public class WalletActivity extends AppCompatActivity {

    private DBController dbCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        dbCon = new DBController(WalletActivity.this);
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
