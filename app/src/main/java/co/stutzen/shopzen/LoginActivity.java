package co.stutzen.shopzen;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.database.DBController;

public class LoginActivity extends AppCompatActivity {

    private DBController dbCon;
    private TextView placeorder;
    private CallbackManager callbackManager;
    private TextView btntext;
    private EditText username;
    private EditText password;
    private Dialog progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbCon = new DBController(LoginActivity.this);
        progBar= new Dialog(this);
        progBar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progBar.setContentView(R.layout.progress_load);
        progBar.setCancelable(false);
        if(dbCon.getCustomerCount() > 0)
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        btntext = (TextView) findViewById(R.id.btntext);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        callbackManager = CallbackManager.Factory.create();
        LinearLayout fbclick = (LinearLayout) findViewById(R.id.fbclick);
        LinearLayout loginclick = (LinearLayout) findViewById(R.id.loginclick);
        TextView newuser = (TextView) findViewById(R.id.newuser);
        loginclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
                    btntext.setText("Processing...");
                    LoginTask task = new LoginTask();
                    task.execute(username.getText().toString(), password.getText().toString());
                }else{
                    if(username.getText().toString().trim().length() == 0)
                        username.setError("Field must be filled.");
                    if(password.getText().toString().trim().length() == 0)
                        password.setError("Field must be filled.");
                }
            }
        });
        fbclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        try {
            PackageInfo info = getPackageManager().getPackageInfo("co.stutzen.shopzen",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken=loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken,new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        Log.i("LoginActivity", response.toString() + "");
                        progBar.show();
                        JSONObject jboj = new JSONObject(response.toString());
                        GetDetailTask task = new GetDetailTask();
                        task.execute(jboj.getString("email"), jboj.getString("name"));
                        }catch(Exception e){
                            e.printStackTrace();
                            Log.i("loginerrorfb",e.getMessage()+"");
                        }
                    }
                });
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("cancel", "dddddddddd");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
                }
        );
        fbclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "user_birthday", "email"));
            }
        });
    }

    @Override
    public void onBackPressed() {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("LoginTask", "started");
        }

        protected String doInBackground(String... param) {
            Log.i("LoginTask", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                JSONObject jobj = new JSONObject();
                jobj.put("username", param[0]);
                jobj.put("password", param[1]);
                response = connection.sendHttpPostjson(AppConstants.LOGIN_USER, jobj, "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("respppppp", resp + "");
            if(resp != null){
                try{
                    JSONObject jobj = new JSONObject(resp);
                    if(jobj.has("token") && jobj.getString("token") != null){
                        GetLoginDetailTask task = new GetLoginDetailTask();
                        task.execute(jobj.getString("user_email"));
                    }else{
                        btntext.setText("Signin");
                        if(jobj.has("message"))
                            Snackbar.make(btntext, jobj.getString("message"), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        else
                            Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                    }
                }catch (Exception e){
                    btntext.setText("Signin");
                    e.printStackTrace();
                    Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{

                btntext.setText("Signin");
                Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    private class GetLoginDetailTask extends AsyncTask<String, Void, String> {

        private String email = "";

        @Override
        protected void onPreExecute() {
            Log.i("GetDetailTask", "started");
        }

        protected String doInBackground(String... param) {
            Log.i("GetDetailTask", param[0]);
            String response = null;
            try {
                email = param[0];
                Connection connection = new Connection();
                response = connection.connStringResponse(AppConstants.CREATE_CUSTOMER + "&email=" + URLEncoder.encode(param[0]), "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("respppppp", resp + "");
            btntext.setText("Signin");
            if(resp != null){
                try{
                    JSONArray data = new JSONArray(resp);
                    JSONObject jobj = data.getJSONObject(0);
                    if(jobj.has("id") && jobj.getInt("id") > 0){
                        dbCon.insertCustomer(jobj.getInt("id"), jobj.getString("first_name")+" "+jobj.getString("last_name"), jobj+"");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        if(jobj.has("message"))
                            Snackbar.make(btntext, jobj.getString("message"), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        else
                            Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    private class GetDetailTask extends AsyncTask<String, Void, String> {

        private String email = "";
        private String name = "";

        @Override
        protected void onPreExecute() {
            Log.i("GetDetailTask", "started");
        }

        protected String doInBackground(String... param) {
            Log.i("GetDetailTask", param[0]);
            String response = null;
            try {
                email = param[0];
                name = param[1];
                Connection connection = new Connection();
                response = connection.connStringResponse(AppConstants.CREATE_CUSTOMER + "&email=" + URLEncoder.encode(param[0]), "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("respppppp", resp + "");
            if(resp != null){
                try{
                    JSONArray data = new JSONArray(resp);
                    JSONObject jobj = data.getJSONObject(0);
                    if(jobj.has("id") && jobj.getInt("id") > 0){
                        progBar.dismiss();
                        dbCon.insertCustomer(jobj.getInt("id"), jobj.getString("first_name")+" "+jobj.getString("last_name"), jobj+"");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        RegisterTask task = new RegisterTask();
                        task.execute(email, name);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    progBar.dismiss();
                    Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                progBar.dismiss();
                Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("RegisterTask", "started");
        }

        protected String doInBackground(String... param) {
            Log.i("RegisterTask", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                JSONObject jobj = new JSONObject();
                jobj.put("username", param[0]);
                jobj.put("first_name", param[1]);
                jobj.put("last_name", "");
                jobj.put("email", param[0]);
                jobj.put("password", "socialLogin");
                response = connection.sendHttpPostjson(AppConstants.CREATE_CUSTOMER, jobj, "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("respppppp", resp + "");
            progBar.dismiss();
            if(resp != null){
                try{
                    JSONObject jobj = new JSONObject(resp);
                    if(jobj.has("id") && jobj.getInt("id") > 0){
                        dbCon.insertCustomer(jobj.getInt("id"), jobj.getString("first_name")+" "+jobj.getString("last_name"), jobj+"");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        if(jobj.has("message"))
                            Snackbar.make(btntext, jobj.getString("message"), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        else
                            Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                Snackbar.make(btntext, "Error in network. Please check your internet connection.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }
}
