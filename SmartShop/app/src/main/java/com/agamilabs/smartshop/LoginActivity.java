package com.agamilabs.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.database.MySharedPreferenceManager;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    private EditText editText_host_url, editText_domain_name, editText_user_name, editText_password;
    private Button btn_submit;
    private String url = "/connector/index.php";

    //private DatabaseHandler mDbHandler;
    private MySharedPreferenceManager mySharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //mDbHandler = new DatabaseHandler(this);
        mySharedPreferenceManager = new MySharedPreferenceManager(this);

        if (mySharedPreferenceManager.hasSingleAccount()) {
            onAccountSelect("");
        } else if (mySharedPreferenceManager.hasMultipleAccounts()) {
            onAccountSelect("");
        } else {

        }

        editText_host_url = findViewById(R.id.login_hosturl);
        editText_domain_name = findViewById(R.id.login_domain_name);
        editText_user_name = findViewById(R.id.login_username);
        editText_password = findViewById(R.id.login_password);

        btn_submit = findViewById(R.id.login_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final HashMap<String, String> params = new HashMap<>();

                String host_url = editText_host_url.getText().toString();
                String domain_name = editText_domain_name.getText().toString();
                String user_name = editText_user_name.getText().toString();
                params.put("domain_name", domain_name);
                params.put("userid", user_name);
                params.put("password", editText_password.getText().toString());

                mySharedPreferenceManager.saveLoginInfo(host_url, domain_name, user_name);
//                Log.e("TAG", "prams map:  " + params);

                String host = "" + host_url + url;

//                StringRequest stringRequest = new StringRequest(Request.Method.POST, host,
//                        LoginActivity.this, LoginActivity.this) {
//                    @Override
//                    protected Map<String, String> getParams() {
//                        return params;
//                    }
//                };
//                requestQueue.add(stringRequest);

                AppController.getAppController().getAppNetworkController().makeRequest(host, LoginActivity.this, LoginActivity.this, new HashMap<>());
            }
        });


    }

    public void onAccountSelect(String authkey) {
        editText_host_url.setText(mySharedPreferenceManager.getSavedHost(authkey));
        editText_domain_name.setText(mySharedPreferenceManager.getSavedDomain(authkey));
        editText_user_name.setText(mySharedPreferenceManager.getSavedUsername(authkey));
//        editText_password.setText("" + mySharedPreferenceManager.getLoggedInHost());
    }


    @Override
    public void onResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            if (object.has("error") && !object.getBoolean("error")) {

                JSONArray topicsArray = object.getJSONArray("topics");
                ArrayList<String> topics = new ArrayList<>();
                for (int i = 0; i < topicsArray.length(); i++) {
                    JSONObject aTopic = topicsArray.getJSONObject(i);
                    String shop_name = aTopic.getString("shop_name");
                    String topic_name = aTopic.getString("topic_name");
                    String auth_domain = aTopic.getString("auth_domain");
                    String auth_key = aTopic.getString("auth_key");

                    FirebaseMessaging.getInstance().subscribeToTopic(topic_name);

                    // BEGIN[save topics in database]
                    mySharedPreferenceManager.saveLoginSuccessInfo(shop_name, topic_name, auth_domain, auth_key);

                    // END[save topics in database]
                }

                mySharedPreferenceManager.saveLoggedIn(true);
                startActivity(new Intent(getApplicationContext(), ShopAdminHome.class));

            } else {
                Toast.makeText(this, object.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }
}