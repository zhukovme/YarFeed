package me.zhukov.yarfeed.sax;

import android.support.annotation.Nullable;

import org.xml.sax.InputSource;

import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.zhukov.yarfeed.model.NewsItem;

/**
 * @author Michael Zhukov
 */
public enum NewsXmlReader {

    INSTANCE;

    @Nullable
    public List<NewsItem> readXML(URL xmlUrl) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            NewsSaxHandler handler = new NewsSaxHandler();
            saxParser.parse(new InputSource(xmlUrl.openStream()), handler);

            return handler.getNewsItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
