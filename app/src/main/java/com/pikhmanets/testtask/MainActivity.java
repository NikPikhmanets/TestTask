package com.pikhmanets.testtask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_TITLE = "title";
    public static String INTENT_LINK = "post";

    public static String ERR_CONNECT = "connect";
    public static String ERR_PARSE = "parse";

    final String URL = "http://feeds.feedburner.com/blogspot/hsDu";

//    Elements mElementsTitle;
//    Elements mElementsTime;
//    Elements mElementsPost;

    RvAdapter mRvAdapter;
    RecyclerView mRecyclerView;
    List<News> mNewsList = new ArrayList<>();

    ParsingDataTask parsingDataTask;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_list_title);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setScrollbarFadingEnabled(true);

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (parsingDataTask != null) {
                    parsingDataTask.cancel(true);
                }
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        if (parsingDataTask == null) {
            parsingDataTask = new ParsingDataTask(URL);
            parsingDataTask.execute();
        }
    }

    class ParsingDataTask extends AsyncTask<Void, String, Void> {

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
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(ERR_CONNECT);
            } catch (XmlPullParserException x) {
                x.printStackTrace();
                publishProgress(ERR_PARSE);
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
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values[0].equals(ERR_CONNECT)) {
                Toast.makeText(MainActivity.this, R.string.err_connect, Toast.LENGTH_SHORT).show();
            }
            if (values[0].equals(ERR_PARSE)) {
                Toast.makeText(MainActivity.this, R.string.err_parse, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            parsingDataTask = null;
            mSwipeRefreshLayout.setRefreshing(false);
            if (mNewsList == null) {
                Toast.makeText(MainActivity.this, R.string.load_err, Toast.LENGTH_SHORT).show();
                return;
            }
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
//                intent.putExtra(INTENT_POST, mTitle.getTextNews());
                intent.putExtra(INTENT_LINK, mTitle.getRefNews());
                startActivity(intent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mRvAdapter);
    }
}
