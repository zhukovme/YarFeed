package me.zhukov.yarfeed.model;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.Date;

/**
 * @author Michael Zhukov
 */
public class NewsItem {

    private String title;
    private String link;
    private String description;
    private Bitmap enclosure;
    private Date pubDate;
    private String category;

    public NewsItem() {}

    public NewsItem(String title, String link, String description, Bitmap enclosure,
                    Date pubDate, String category) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.enclosure = enclosure;
        this.pubDate = pubDate;
        this.category = category;
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

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NewsItem)) {
            return false;
        }
        NewsItem that = (NewsItem) o;
        return that.title.equals(title) &&
                that.link.equals(link) &&
                that.description.equals(description) &&
                that.enclosure.equals(enclosure) &&
                that.pubDate.equals(pubDate) &&
                that.category.equals(category);
    }
}
