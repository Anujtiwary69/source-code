package co.stutzen.shopzen;

import android.app.Application;

import com.onesignal.OneSignal;

import co.stutzen.shopzen.database.DBController;
import co.stutzen.shopzen.database.DatabaseManager;

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        OneSignal.startInit(this).init();
        DatabaseManager.initializeInstance(new DBController(getApplicationContext()));
    }

}