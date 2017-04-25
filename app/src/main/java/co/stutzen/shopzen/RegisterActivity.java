package co.stutzen.shopzen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Arrays;

import co.stutzen.shopzen.adapters.CategoryAdapter;
import co.stutzen.shopzen.bo.CategoryBO;
import co.stutzen.shopzen.constants.AppConstants;
import co.stutzen.shopzen.constants.Connection;
import co.stutzen.shopzen.database.DBController;

public class RegisterActivity extends AppCompatActivity {

    private DBController dbCon;
    private TextView placeorder;
    private CallbackManager callbackManager;
    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private TextView btntext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        callbackManager = CallbackManager.Factory.create();
        dbCon = new DBController(RegisterActivity.this);
        LinearLayout fbclick = (LinearLayout) findViewById(R.id.fbclick);
        LinearLayout accoutclick = (LinearLayout) findViewById(R.id.accoutclick);
        username = (EditText) findViewById(R.id.username);
        btntext = (TextView) findViewById(R.id.btntext);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        accoutclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().trim().length() > 0 && name.getText().toString().trim().length() > 0
                        && email.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
                    btntext.setText("Processing...");
                    RegisterTask task = new RegisterTask();
                    task.execute(username.getText().toString(), name.getText().toString(), email.getText().toString(), password.getText().toString());
                }else{
                    if(username.getText().toString().trim().length() == 0)
                        username.setError("Field must be filled.");
                    if(email.getText().toString().trim().length() == 0)
                        email.setError("Field must be filled.");
                    if(name.getText().toString().trim().length() == 0)
                        name.setError("Field must be filled.");
                    if(password.getText().toString().trim().length() == 0)
                        password.setError("Field must be filled.");
                }
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.i("LoginActivity", response.toString() + "");
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i("loginerrorfb", e.getMessage() + "");
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
                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "user_friends", "user_birthday", "email"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                jobj.put("email", param[2]);
                jobj.put("password", param[3]);
                response = connection.sendHttpPostjson(AppConstants.CREATE_CUSTOMER, jobj, "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            btntext.setText("Create Account");
            Log.i("respppppp", resp + "");
            if(resp != null){
                try{
                    JSONObject jobj = new JSONObject(resp);
                    if(jobj.has("id") && jobj.getInt("id") > 0){
                        dbCon.insertCustomer(jobj.getInt("id"), jobj.getString("first_name")+" "+jobj.getString("last_name"), jobj+"");
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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
