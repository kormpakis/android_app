package com.example.user.recyclerviewjsonvolley;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;


public class LoginActivity extends AppCompatActivity {

    SessionManager sessionManager;

    AwesomeText show_hide_ic;
    boolean pwd_status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        final EditText etUsername = (EditText) findViewById(R.id.etUsernameLogin);
        final EditText etPassword = (EditText) findViewById(R.id.etPasswordLogin);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.etGoRegister);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.straightonmusic);
        show_hide_ic = (AwesomeText) findViewById(R.id.pwd_show_hide);

        show_hide_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    show_hide_ic.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    etPassword.setSelection(etPassword.length());
                } else {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    show_hide_ic.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    etPassword.setSelection(etPassword.length());
                }
            }
        });

        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        final String url = "https://straightonmusic.com/wp-json/jwt-auth/v1/token";

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                etUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
                etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
                bLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);

                if (!username.isEmpty() && !password.isEmpty()) {

                    progressBar.setVisibility(View.VISIBLE);

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject loginResponse = new JSONObject(response);
                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                LoginActivity.this.startActivity(i);


                                sessionManager.createSession(username, password);

                                progressBar.setVisibility(View.GONE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressBar.setVisibility(View.VISIBLE);

                            NetworkResponse response = error.networkResponse;

                            progressBar.setVisibility(View.GONE);

                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 503:
                                        //json = new String(response.data);
                                        Toast.makeText(getApplicationContext(), "Something went wrong! Please retry.", Toast.LENGTH_LONG).show();

                                        //json = trimMessage(json, "message");
                                        //if (json != null) displayMessage(json);
                                        etUsername.getText().clear();
                                        etPassword.getText().clear();
                                        break;
                                    case 403:
                                        //json = new String(response.data);
                                        Toast.makeText(getApplicationContext(), "Something went wrong! Please retry.", Toast.LENGTH_LONG).show();

                                        //json = trimMessage(json, "message");
                                        //if (json != null) displayMessage(json);
                                        etUsername.getText().clear();
                                        etPassword.getText().clear();
                                        break;
                                }
                                //Additional cases
                            }
                        }

                        public String trimMessage(String json, String key) {
                            String trimmedString = null;
                            try {
                                JSONObject obj = new JSONObject(json);
                                trimmedString = obj.getString(key);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                            return trimmedString;
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", username);
                            params.put("password", password);
                            return params;
                        }
                    };

                    request.setRetryPolicy(new DefaultRetryPolicy(
                            999999999,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    request.setShouldCache(false);
                    requestQueue.add(request);

                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all the fields and try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void displayMessage(String toastString) {
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}