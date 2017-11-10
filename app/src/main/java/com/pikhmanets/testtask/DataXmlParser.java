package com.pikhmanets.testtask;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class DataXmlParser {

    List<News> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            xmlPullParser.nextTag();
            return readFeed(xmlPullParser);
        } finally {
            inputStream.close();
        }
    }

    private List<News> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<News> newsList = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("entry")) {
                newsList.add(readNews(parser));
            } else {
                skip(parser);
            }
        }
        return newsList;
    }

    private News readNews(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "entry");
        String title = null;
        String time = null;
        String text = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    title = readTitle(parser);
                    break;
                case "published":
                    time = readTime(parser);
                    break;
                case "content":
                    text = readNewsText(parser);
                    break;
                case "link":
                    link = readLink(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new News(title, time, text, link);
    }

    private String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "published");
        String t = readText(parser);
        String time = t.substring(0, 10) + "/" + t.substring(11, 19);
        parser.require(XmlPullParser.END_TAG, null, "published");
        return time;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readNewsText(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "content");
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "content");
        return text;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, null, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
            }
        }
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }
}
