package me.zhukov.yarfeed.loader;

import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.zhukov.yarfeed.NewsXmlParser;
import me.zhukov.yarfeed.R;

/**
 * @author Michael Zhukov
 */
public class NewsLoader extends AsyncTaskLoader<NewsResponse> {

    private static final Logger LOGGER = Logger.getLogger(NewsLoader.class.getName());

    private ProgressDialog mProgressDialog;

    public NewsLoader(Context context) {
        super(context);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.progress_dialog_title));
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mProgressDialog.show();
        forceLoad();
    }

    @Override
    public NewsResponse loadInBackground() {
        try {
            NewsXmlParser parser = new NewsXmlParser();
            NewsResponse newsResponse = parser.parse();
            if (newsResponse.getRequestResult() == RequestResult.SUCCESS) {
                newsResponse.save(getContext());
                onSuccess();
            } else {
                onError();
            }
            mProgressDialog.dismiss();
            return newsResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return new NewsResponse();
        }
    }

    private void onSuccess() {
        LOGGER.log(Level.SEVERE, "SUCCESS");
    }

    private void onError() {
        LOGGER.log(Level.SEVERE, "ERROR");
    }
}
