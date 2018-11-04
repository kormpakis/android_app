package com.example.user.recyclerviewjsonvolley;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;


public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    AwesomeText show_hide_ic;
    AwesomeText show_hide_ic_confirm;
    boolean pwd_status = true;
    boolean pwd_status_confirm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etConfirm = (EditText) findViewById(R.id.etConfirmPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        show_hide_ic = (AwesomeText) findViewById(R.id.pwd_show_hide);
        show_hide_ic_confirm = (AwesomeText) findViewById(R.id.pwd_show_hide_confirm);

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

        show_hide_ic_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status_confirm) {
                    etConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status_confirm = false;
                    show_hide_ic_confirm.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    etConfirm.setSelection(etConfirm.length());
                } else {
                    etConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status_confirm = true;
                    show_hide_ic_confirm.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    etConfirm.setSelection(etConfirm.length());

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

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        final String url = "https://straightonmusic.com/wp-json/wp/v2/users/";
        //final JSONObject body = new JSONObject();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Assert")
            @Override
            public void onClick(View v) {

                final String username = etUsername.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String confirmPass = etConfirm.getText().toString();

                etUsername.onEditorAction(EditorInfo.IME_ACTION_DONE);
                etEmail.onEditorAction(EditorInfo.IME_ACTION_DONE);
                etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
                etConfirm.onEditorAction(EditorInfo.IME_ACTION_DONE);
                bRegister.onEditorAction(EditorInfo.IME_ACTION_DONE);

                final String authorization = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvc3RyYWlnaHRvbm11c2ljLmNvbSIsImlhdCI6MTUzOTA4NjQ0OSwibmJmIjoxNTM5MDg2NDQ5LCJleHAiOjE1Mzk2OTEyNDksImRhdGEiOnsidXNlciI6eyJpZCI6IjIifX19.8iijhP5hdrUxTIqAqTam-We7fGGO9n0lsXy-Vh6hDsU";
                final String content = "application/x-www-form-urlencoded";

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPass.isEmpty()) {

                    if (password.equals(confirmPass)) {

                        progressBar.setVisibility(View.VISIBLE);

                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject registerResponse = new JSONObject(response);
                                    String successString = registerResponse.getString("username");
                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);

                                    progressBar.setVisibility(View.GONE);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.VISIBLE);

                                String json = null;

                                NetworkResponse response = error.networkResponse;

                                progressBar.setVisibility(View.GONE);

                                if (response != null && response.data != null) {
                                    switch (response.statusCode) {
                                        case 500:
                                            json = new String(response.data);
                                            json = trimMessage(json, "message");
                                            if (json != null) displayMessage(json);
                                            etUsername.getText().clear();
                                            etEmail.getText().clear();
                                            etPassword.getText().clear();
                                            break;
                                        case 403:
                                            json = new String(response.data);
                                            json = trimMessage(json, "message");
                                            if (json != null) displayMessage(json);
                                            etUsername.getText().clear();
                                            etEmail.getText().clear();
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
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> header = new HashMap<>();
                                header.put("Authorization", authorization);
                                header.put("Content-Type", content);
                                header.put("cache-control", "no-cache");
                                return header;
                            }

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("email", email);
                                params.put("password", password);
                                return params;
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                // if you want to use the status code for any other purpose like to handle 401, 403, 404
                                String statusCode = String.valueOf(response.statusCode);
                                //Handling logic
                                return super.parseNetworkResponse(response);
                            }
                        };

                        request.setRetryPolicy(new DefaultRetryPolicy(
                                999999999,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                        request.setShouldCache(false);
                        requestQueue.add(request);
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords don't match! Please retry!", Toast.LENGTH_LONG).show();
                        etPassword.getText().clear();
                        etConfirm.getText().clear();
                    }
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