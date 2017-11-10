package com.pikhmanets.testtask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_TITLE = "title";
    public static String INTENT_POST = "post";
    final String URL = "http://feeds.feedburner.com/blogspot/hsDu";

//    Elements mElementsTitle;
//    Elements mElementsTime;
//    Elements mElementsPost;

    RvAdapter mRvAdapter;
    RecyclerView mRecyclerView;
    List<News> mNewsList = new ArrayList<>();

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

        String urlBase;

        ParsingDataTask(String url) {
            urlBase = url;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DataXmlParser dataXmlParser = new DataXmlParser();
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlBase);
                connection = (HttpURLConnection) url.openConnection();
                mNewsList = dataXmlParser.parse(new BufferedInputStream(connection.getInputStream()));
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
//            try {
//                Document doc = Jsoup.connect(Url).get();
//                String page = doc.getElementsByTag("openSearch:itemsPerPage").text();
//                mElementsTitle = doc.getElementsByTag("title");
//                mElementsTime = doc.getElementsByTag("updated");
//                mElementsPost = doc.getElementsByTag("content");
//
//                mNewsList.clear();
//                for (int i = 1; i <= mElementsPost.size(); i++) {
//                    News itemPost = new News();
//                    itemPost.setTitle(mElementsTitle.get(i).text());
//                    itemPost.setPostTime(mElementsTime.get(i - 1).text());
//                    itemPost.setTextNews(mElementsPost.get(i - 1).text());
//                    mNewsList.add(itemPost);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mNewsList == null) return;
            buildList();
        }
    }

    private void buildList() {
        mRvAdapter = new RvAdapter(mNewsList);
        mRvAdapter.setListener(new OnItemTitleClickListener() {
            @Override
            public void onItemTitleClick(News mTitle) {

                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(INTENT_TITLE, mTitle.getTitle());
                intent.putExtra(INTENT_POST, mTitle.getTextNews());
                startActivity(intent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mRvAdapter);
    }
}
