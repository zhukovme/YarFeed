package me.zhukov.yarfeed.loader;

import android.content.Context;

import java.util.List;

import me.zhukov.yarfeed.database.NewsTable;
import me.zhukov.yarfeed.model.NewsItem;

/**
 * @author Michael Zhukov
 */
public class NewsResponse {

    private List<NewsItem> mAnswer;
    private RequestResult mRequestResult;

    public NewsResponse() {
        mRequestResult = RequestResult.ERROR;
    }

    public RequestResult getRequestResult() {
        return mRequestResult;
    }

    public NewsResponse setRequestResult(RequestResult requestResult) {
        mRequestResult = requestResult;
        return this;
    }

    public List<NewsItem> getAnswer() {
        if (mAnswer == null) {
            return null;
        }
        return mAnswer;
    }

    public NewsResponse setAnswer(List<NewsItem> answer) {
        mAnswer = answer;
        return this;
    }

    public void save(Context context) {
        List<NewsItem> newsItems = getAnswer();
        if (newsItems != null) {
            NewsTable newsTable = new NewsTable(context);
            newsTable.save(newsItems);
        }
    }
}
