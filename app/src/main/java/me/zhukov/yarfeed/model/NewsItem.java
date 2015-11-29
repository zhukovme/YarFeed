package me.zhukov.yarfeed.model;

import android.graphics.Bitmap;

import me.zhukov.yarfeed.Utils;

/**
 * @author Michael Zhukov
 */
public class NewsItem {

    private String title;
    private String link;
    private String description;
    private Bitmap enclosure;
    private String pubDate;

    public NewsItem() {}

    public NewsItem(String title, String link, String description, Bitmap enclosure,
                    String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.enclosure = enclosure;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Bitmap enclosure) {
        this.enclosure = enclosure;
    }

    public String getPubDate() {
        return Utils.dateFormatter(pubDate);
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
