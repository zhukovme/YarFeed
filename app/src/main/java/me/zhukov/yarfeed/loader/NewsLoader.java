package me.zhukov.yarfeed.loader;

import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import me.zhukov.yarfeed.NewsXmlParser;
import me.zhukov.yarfeed.R;
import me.zhukov.yarfeed.database.NewsTable;
import me.zhukov.yarfeed.model.NewsItem;

/**
 * @author Michael Zhukov
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    public static final String REFRESH_BUNDLE = "refresh_bundle";

    private Context mContext;
    private NewsTable mNewsTable;
    private ProgressDialog mProgressDialog;
    private Bundle mRefreshBundle;

    public NewsLoader(Context context, Bundle refreshBundle) {
        super(context);
        mContext = context;
        mNewsTable = new NewsTable(context);
        mRefreshBundle = refreshBundle;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.progress_dialog_title));
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (!mRefreshBundle.getBoolean(REFRESH_BUNDLE)) {
            mProgressDialog.show();
        }
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        try {
            if (mNewsTable.getNewsList().isEmpty() || mRefreshBundle.getBoolean(REFRESH_BUNDLE)) {
                NewsXmlParser parser = new NewsXmlParser();
                NewsTable newsTable = new NewsTable(mContext);
                newsTable.save(parser.parse());
            }
            return mNewsTable.getNewsList();
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            mProgressDialog.dismiss();
        }
    }
}
