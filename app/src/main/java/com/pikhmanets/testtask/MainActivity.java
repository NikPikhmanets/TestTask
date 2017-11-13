package com.pikhmanets.testtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadParsingDataTask.TaskCallback {

    public static String INTENT_TITLE = "title";
    public static String INTENT_LINK = "post";

    public static String ERR_CONNECT = "connect";
    public static String ERR_PARSE = "parse";

    final String URL = "http://feeds.feedburner.com/blogspot/hsDu";

    RvAdapter mRvAdapter;
    RecyclerView mRecyclerView;

    LoadParsingDataTask mLoadParsingDataTask;
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
                if (mLoadParsingDataTask != null) {
                    mLoadParsingDataTask.cancel(true);
                }
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        if (mLoadParsingDataTask == null) {
            mLoadParsingDataTask = new LoadParsingDataTask(URL);
            mLoadParsingDataTask.registerCallBack(this);
            mLoadParsingDataTask.execute();
        }
    }

    @Override
    public void callBackProgress(String res) {
        if (res.equals(ERR_CONNECT)) {
            Toast.makeText(MainActivity.this, R.string.err_connect, Toast.LENGTH_SHORT).show();
        }
        if (res.equals(ERR_PARSE)) {
            Toast.makeText(MainActivity.this, R.string.err_parse, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void callBackPostExecute(List<News> news) {
        mLoadParsingDataTask = null;
        mSwipeRefreshLayout.setRefreshing(false);
        if (news == null) {
            Toast.makeText(MainActivity.this, R.string.load_err, Toast.LENGTH_SHORT).show();
            return;
        }
        buildList(news);
    }

    private void buildList(List<News> news) {
        mRvAdapter = new RvAdapter(news);
        mRvAdapter.setListener(new OnItemClickListener() {
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
