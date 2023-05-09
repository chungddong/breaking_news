package com.sophra.test_parsing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.usage.NetworkStats;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {  //implements SwipeRefreshLayout.OnRefreshListener

    private AdView adView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<card_item> dataList = new ArrayList<>();
    private String news_path;
    private TextView test_textView;
    ProgressDialog progressDialog;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("뉴스 속보");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //test_textView = findViewById(R.id.test_textView);
        recyclerView = findViewById(R.id.recycler_view);
        //swipeRefreshLayout = findViewById(R.id.swipe_layout);
        //swipeRefreshLayout.setOnRefreshListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("잠시만 기다려주세요....");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if(!(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()))
        {
            Toast.makeText(this,"인터넷에 연결되어 있지 않습니다. \n    연결 후 다시 시도해주세요.",Toast.LENGTH_LONG).show();
        }
        else {
            new Description().execute();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh :
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.information :
                Intent info = new Intent(this, infos.class);
                info.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(info);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onRefresh() {
        dataList.clear();
        new Description().execute();

        swipeRefreshLayout.setRefreshing(false);
    }*/

    private class Description extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            parsing();
            if(dataList.size() < 20)
            {
                i += 10;
                doInBackground();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerAdapter adapter = new RecyclerAdapter(dataList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }

    public void parsing() {
        try {
            Document doc = Jsoup.connect("https://search.naver.com/search.naver?&where=news&query=%5B%EC%86%8D%EB%B3%B4%5D&sort=1&start=" + i).get();
            Elements ElementDataSize = doc.select("ul[class=list_news]").select("li");

            for (Element elem : ElementDataSize) {
                String title_text = elem.select("a[class=news_tit]").text(); //제목
                String contents_text = elem.select("a[class=api_txt_lines dsc_txt_wrap]").text(); //기사내용
                Elements element = elem.select("a[class=dsc_thumb] img");  //이미지 src
                String img_url = "";

                img_url = element.attr("data-lazysrc");

                //Log.v("img_url", "" + element.toString());
                Log.v("img_url", "" + img_url);
                String web_url = elem.select("a[class=dsc_thumb]").attr("href");
                String title_link = web_url;

                if(title_text.substring(1,3).equals("속보"))
                {
                    dataList.add(new card_item(title_text,title_link,contents_text,img_url,web_url));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}