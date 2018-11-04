package com.example.user.recyclerviewjsonvolley;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class YourBands extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView1;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue1;
    private Button mButton;
    public String theurl;

    private RecyclerView suggestionRecyclerView;

    public SwipeRefreshLayout swipeRefreshLayout;

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_bands);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        mRecyclerView1 = findViewById(R.id.recycler_view1);
        //mButton = findViewById(R.id.load_more_button);

        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();
        mRequestQueue1 = Volley.newRequestQueue(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        String url = "https://straightonmusic.com/wp-json/wp/v2/posts?tags=540";

        parseJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu_main_your_bands, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                sessionManager.logout();
                Intent i = new Intent(YourBands.this, LoginActivity.class);
                i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                YourBands.this.startActivity(i);
                this.finish();
                break;
            case R.id.main_activity:
                //sessionManager.logout();
                Intent x = new Intent(YourBands.this, MainActivity.class);
                //x.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                YourBands.this.startActivity(x);
                //this.finish();
                break;
        }

        ;
        return true;
    }

    //String url = "https://straightonmusic.com/wp-json/wp/v2/posts";

    private void parseJSON() {
        swipeRefreshLayout.setRefreshing(true);
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String url = "https://straightonmusic.com/wp-json/wp/v2/posts?tags=540";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Log.d("Response:", response.toString());

                mExampleList.clear();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject articleObject = response.getJSONObject(i);

                        //Log.d("Items: ", articleObject.getString("slug"));

                        JSONObject title_object = articleObject.getJSONObject("title");
                        final String title1 = title_object.getString("rendered");
                        final String title = title1.replaceAll("<p>", "")
                                .replaceAll("</p>", "")
                                .replaceAll("</p>", "")
                                .replaceAll("&#8220;", "'")
                                .replaceAll("&#8221;", "'")
                                .replaceAll("&#8230;", "...")
                                .replaceAll("&#8211;", "-")
                                .replaceAll("&#8216;", "'")
                                .replaceAll("&#8217;", "'");

                        JSONObject excerpt_object = articleObject.getJSONObject("excerpt");
                        final String excerpt1 = excerpt_object.getString("rendered");
                        final String excerpt = excerpt1.replaceAll("<p>", "")
                                .replaceAll("</p>", "")
                                .replaceAll("</p>", "")
                                .replaceAll("&#8220;", "'")
                                .replaceAll("&#8221;", "'")
                                .replaceAll("&#8230;", "...")
                                .replaceAll("&#8211;", "-")
                                .replaceAll("&#8216;", "'")
                                .replaceAll("&#8217;", "'");

                        final String url = articleObject.getString("link");


                        //The ID of the featured image
                        int image_id = articleObject.getInt("featured_media");

                        //General media url - here are all the media of the initial JSON
                        final String media_url = "https://straightonmusic.com/wp-json/wp/v2/media/";

                        final int id = articleObject.getInt("id");

                        //The URL of this article's featured image
                        String article_media_url = media_url + image_id;

                        JsonObjectRequest article_media_request = new JsonObjectRequest(Request.Method.GET, article_media_url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject mediaREQ = response.getJSONObject("guid");
                                    String featured_image_url = mediaREQ.getString("rendered");
                                    Log.d("Featured images' URLs: ", featured_image_url);

                                    mExampleList.add(new ExampleItem(featured_image_url, title, excerpt, url, id));
                                    mExampleAdapter = new ExampleAdapter(YourBands.this, mExampleList, mExampleList);
                                    mRecyclerView1.setAdapter(mExampleAdapter);
                                    swipeRefreshLayout.setRefreshing(false);
                                } catch (JSONException e) {
                                    swipeRefreshLayout.setRefreshing(false);
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("Error.", error.getMessage());
                            }
                        });

                        mRequestQueue1.add(article_media_request);

                        Log.d("Titles:", title);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error.", error.getMessage());
                    }
                });

        mRequestQueue1.getCache().remove(url);
        mRequestQueue1.add(arrayRequest);
    }

    @Override
    public void onRefresh() {
        parseJSON();
    }
}