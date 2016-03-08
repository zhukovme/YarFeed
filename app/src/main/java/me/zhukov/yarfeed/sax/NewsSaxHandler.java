package me.zhukov.yarfeed.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.zhukov.yarfeed.model.NewsItem;
import me.zhukov.yarfeed.util.BitmapHelper;
import me.zhukov.yarfeed.util.DateHelper;

import static me.zhukov.yarfeed.sax.NewsSaxHandler.NewsXmlTags.CATEGORY;
import static me.zhukov.yarfeed.sax.NewsSaxHandler.NewsXmlTags.DESCRIPTION;
import static me.zhukov.yarfeed.sax.NewsSaxHandler.NewsXmlTags.ENCLOSURE;
import static me.zhukov.yarfeed.sax.NewsSaxHandler.NewsXmlTags.ITEM;
import static me.zhukov.yarfeed.sax.NewsSaxHandler.NewsXmlTags.LINK;
import static me.zhukov.yarfeed.sax.NewsSaxHandler.NewsXmlTags.PUB_DATE;
import static me.zhukov.yarfeed.sax.NewsSaxHandler.NewsXmlTags.TITLE;

/**
 * @author Michael Zhukov
 */
public class NewsSaxHandler extends DefaultHandler {

    private static final Logger LOGGER = Logger.getLogger(NewsSaxHandler.class.getName());

    private String mElement = null;
    private String mValue = null;
    private NewsItem mNewsItem = null;
    private List<NewsItem> mNewsItems = null;

    public void startDocument() throws SAXException {
        LOGGER.log(Level.INFO, "--- Start document ---");
        mNewsItem = new NewsItem();
        mNewsItems = new ArrayList<>();
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        LOGGER.log(Level.INFO, "Start element: " + qName);
        mElement = qName;
        mValue = "";

        if (qName.equals(ENCLOSURE)) {
            mNewsItem.setEnclosure(BitmapHelper.INSTANCE.getBitmapFromUrl(attributes.getValue(0)));
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (mElement != null) {
            String value = new String(ch, start, length);
            mValue += value;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (mElement != null) {
            LOGGER.log(Level.INFO, "Value = " + mValue);
            switch (mElement) {
                case TITLE:
                    mNewsItem.setTitle(mValue);
                    break;
                case LINK:
                    mNewsItem.setLink(mValue);
                    break;
                case DESCRIPTION:
                    mNewsItem.setDescription(mValue);
                    break;
                case PUB_DATE:
                    mNewsItem.setPubDate(DateHelper.INSTANCE.dateFromString(mValue));
                    break;
                case CATEGORY:
                    mNewsItem.setCategory(mValue);
            }
        }
        LOGGER.log(Level.INFO, "End element: " + qName);
        if (qName.equals(ITEM)) {
            mNewsItems.add(mNewsItem);
            mNewsItem = new NewsItem();
        }
        mElement = null;
    }

    public void endDocument() throws SAXException {
        LOGGER.log(Level.INFO, "--- End document ---");
        mNewsItem = null;
    }

    public List<NewsItem> getNewsItems() {
        return mNewsItems;
    }

    public interface NewsXmlTags {
        String ITEM = "item";
        String TITLE = "title";
        String LINK = "link";
        String DESCRIPTION = "description";
        String ENCLOSURE = "enclosure";
        String PUB_DATE = "pubDate";
        String CATEGORY = "category";
    }
}
