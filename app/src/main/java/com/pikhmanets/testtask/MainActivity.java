package com.pikhmanets.testtask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_TITLE = "title";
    public static String INTENT_POST = "post";
    final String URL = "http://feeds.feedburner.com/blogspot/hsDu";

    Elements mElementsTitle;
    Elements mElementsTime;
    Elements mElementsPost;

    RvAdapter mRvAdapter;
    RecyclerView mRecyclerView;
    List<ItemPost> mTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_list_title);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setScrollbarFadingEnabled(true);

        ParsingDataTask parsingDataTask = new ParsingDataTask(URL);
        parsingDataTask.execute();
    }

    class ParsingDataTask extends AsyncTask<Void, Void, Void> {

        String Url;

        ParsingDataTask(String url) {
            Url = url;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect(Url).get();
                String page = doc.getElementsByTag("openSearch:itemsPerPage").text();
                mElementsTitle = doc.getElementsByTag("title");
                mElementsTime = doc.getElementsByTag("updated");
                mElementsPost = doc.getElementsByTag("content");

                mTitleList.clear();
                for (int i = 1; i <= mElementsPost.size(); i++) {
                    ItemPost itemPost = new ItemPost();
                    itemPost.setItemTitle(mElementsTitle.get(i).text());
                    itemPost.setItemPostTime(mElementsTime.get(i - 1).text());
                    itemPost.setItemPost(mElementsPost.get(i - 1).text());
                    mTitleList.add(itemPost);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            super.onPreExecute();
            mRvAdapter = new RvAdapter(mTitleList);
            mRvAdapter.setListener(new OnItemTitleClickListener() {
                @Override
                public void onItemTitleClick(ItemPost mTitle) {

                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    intent.putExtra(INTENT_TITLE, mTitle.getItemTitle());
                    intent.putExtra(INTENT_POST, mTitle.getItemPost());
                    startActivity(intent);
                }
            });
            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.setAdapter(mRvAdapter);
        }
    }
}
