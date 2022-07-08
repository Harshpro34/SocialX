package com.qunatum.socialx;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView view;

    private ArrayList<String> date;
    private ArrayList<String> title;
    private ArrayList<String> content;
    private ArrayList<String> image_url;
    private ArrayList<String> source;
    private RequestQueue mQueue;

    ProgrammingAdapter_news programmingAdapter_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#2196F3"));

        ImageView sign_out = findViewById(R.id.imageView);
        sign_out.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Home.this,MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(),"Sign out Successfully",Toast.LENGTH_SHORT).show();
        });

        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);

        view = findViewById(R.id.recycle);
        view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        date = new ArrayList<>();
        title = new ArrayList<>();
        content = new ArrayList<>();
        image_url = new ArrayList<>();
        source = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);
        getDataFromUrl();



    }



    private void getDataFromUrl() {

        String url = "https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=2b90de88bc054dda93b2760c0c84b98b";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

            try {
                JSONArray jsonArray = response.getJSONArray("articles");
                for(int i =0;i<jsonArray.length();i++)
                {
                    JSONObject data = jsonArray.getJSONObject(i);
                    JSONObject source = data.getJSONObject("source");

                    this.source.add(source.getString("name"));
                    title.add(data.getString("title"));
                    date.add(data.getString("publishedAt"));
                    content.add(data.getString("content"));
                    image_url.add(data.getString("urlToImage"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            programmingAdapter_news = new ProgrammingAdapter_news(date,title,content,image_url,source,this);
            view.setAdapter(programmingAdapter_news);

        }, error -> {

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };
        mQueue.add(request);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterQuery(newText.trim());
        return false;
    }



    public void filterQuery(String text) {

        ArrayList<String> filtered_date = new ArrayList<>();
        ArrayList<String> filtered_title = new ArrayList<>();
        ArrayList<String> filtered_content = new ArrayList<>();
        ArrayList<String> filtered_image_url = new ArrayList<>();
        ArrayList<String> filtered_source = new ArrayList<>();

        for (int a=0;a<title.size();a++) {
            String s=title.get(a);
            if (s.toLowerCase().contains(text) || s.toLowerCase().contains(text)) {
                filtered_date.add(date.get(a));
                filtered_title.add(title.get(a));
                filtered_content.add(content.get(a));
                filtered_image_url.add(image_url.get(a));
                filtered_source.add(source.get(a));

            }
        }
        this.programmingAdapter_news.setFilter(filtered_date,filtered_title,filtered_content,filtered_image_url,filtered_source);
    }


}