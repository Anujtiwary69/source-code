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
import co.stutzen.shopzen.adapters.NotificationAdapter;
import co.stutzen.shopzen.bo.ColorBO;
import co.stutzen.shopzen.bo.NotificationBO;
import co.stutzen.shopzen.bo.ProductBO;
import co.stutzen.shopzen.constants.CommonFunctions;
import co.stutzen.shopzen.database.DBController;

public class NotificationActivity extends AppCompatActivity {

    private ArrayList<NotificationBO> notiList;
    private ListView productListView;
    private DBController dbCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notofication);
        dbCon = new DBController(NotificationActivity.this);
        notiList = new ArrayList<NotificationBO>();
        notiList = CommonFunctions.getNotifications();
        productListView = (ListView) findViewById(R.id.productlist);
        NotificationAdapter adapter = new NotificationAdapter(NotificationActivity.this, R.layout.notification_item, notiList);
        productListView.setAdapter(adapter);
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
