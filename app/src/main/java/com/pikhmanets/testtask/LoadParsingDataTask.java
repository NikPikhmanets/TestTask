package com.pikhmanets.testtask;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.pikhmanets.testtask.MainActivity.ERR_CONNECT;
import static com.pikhmanets.testtask.MainActivity.ERR_PARSE;


public class LoadParsingDataTask extends AsyncTask<Void, String, List<News>> {

    private String urlBase;

    LoadParsingDataTask(String urlBase) {
        this.urlBase = urlBase;
    }

    interface TaskCallback {
        void callBackProgress(String res);

        void callBackPostExecute(List<News> news);
    }

    private TaskCallback mTaskCallback;

    void registerCallBack(TaskCallback callback) {
        this.mTaskCallback = callback;
    }

    @Override
    protected List<News> doInBackground(Void... voids) {

        List<News> newsList = new ArrayList<>();
        HttpURLConnection connection = null;
        DataXmlParser dataXmlParser = new DataXmlParser();

        try {
            URL url = new URL(urlBase);
            connection = (HttpURLConnection) url.openConnection();
            try (BufferedInputStream br = new BufferedInputStream(connection.getInputStream())) {
                newsList = dataXmlParser.parse(br);
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (connection != null) {
                connection.disconnect();
            }
            publishProgress(ERR_CONNECT);
        } catch (XmlPullParserException x) {
            x.printStackTrace();
            publishProgress(ERR_PARSE);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return newsList;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        mTaskCallback.callBackProgress(values[0]);
    }

    @Override
    protected void onPostExecute(List<News> news) {
        super.onPostExecute(news);

        mTaskCallback.callBackPostExecute(news);
    }
}
