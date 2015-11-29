package me.zhukov.yarfeed.ui.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import me.zhukov.yarfeed.R;
import me.zhukov.yarfeed.loader.NewsLoader;
import me.zhukov.yarfeed.loader.NewsResponse;
import me.zhukov.yarfeed.model.NewsItem;
import me.zhukov.yarfeed.ui.adapter.NewsCardsAdapter;

/**
 * @author Michael Zhukov
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<NewsResponse> {

    private RecyclerView mRvNews;
    private List<NewsItem> mNewsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getLoaderManager().initLoader(R.integer.news_loader_id, Bundle.EMPTY, this);
    }

    @Override
    public Loader<NewsResponse> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<NewsResponse> loader, NewsResponse data) {
        mNewsItems = data.getAnswer();
        if (mNewsItems != null) {
            mRvNews = (RecyclerView) findViewById(R.id.rv_news);
            NewsCardsAdapter newsAdapter = new NewsCardsAdapter(this, mNewsItems);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

            mRvNews.setAdapter(newsAdapter);
            mRvNews.setLayoutManager(layoutManager);
            mRvNews.setItemAnimator(itemAnimator);
        }
    }

    @Override
    public void onLoaderReset(Loader<NewsResponse> loader) {

    }
}
