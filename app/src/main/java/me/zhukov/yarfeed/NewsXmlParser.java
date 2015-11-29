package me.zhukov.yarfeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.zhukov.yarfeed.model.NewsItem;

/**
 * @author Michael Zhukov
 */
public class NewsXmlParser {

    private static final String URL = "http://76.ru/text/rss.xml";

    private XmlPullParser mParser;

    public NewsXmlParser() {
        initParser();
    }

    private void initParser() {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            mParser = factory.newPullParser();
            java.net.URL input = new URL(URL);
            mParser.setInput(input.openStream(), null);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<NewsItem> parse() throws Exception {
        List<NewsItem> newsItems = new ArrayList<>();
        NewsItem newsItem = new NewsItem();
        int eventType = mParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = mParser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagName.equals("item")) {
                        newsItem = new NewsItem();
                    }
                    switch (tagName) {
                        case "title":
                            newsItem.setTitle(mParser.nextText());
                            break;
                        case "link":
                            newsItem.setLink(mParser.nextText());
                            break;
                        case "description":
                            newsItem.setDescription(mParser.nextText());
                            break;
                        case "enclosure":
                            newsItem.setEnclosure(Utils.fetchImage(mParser.getAttributeValue(0)));
                            break;
                        case "pubDate":
                            newsItem.setPubDate(mParser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (tagName.equals("item")) {
                        newsItems.add(newsItem);
                    }
                    break;
            }
            eventType = mParser.next();
        }
        return newsItems;
    }
}
