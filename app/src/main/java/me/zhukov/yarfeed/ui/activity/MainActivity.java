package me.zhukov.yarfeed.ui.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import me.zhukov.yarfeed.R;
import me.zhukov.yarfeed.Utils;
import me.zhukov.yarfeed.database.NewsTable;
import me.zhukov.yarfeed.loader.NewsLoader;
import me.zhukov.yarfeed.model.NewsItem;
import me.zhukov.yarfeed.ui.adapter.NewsCardsAdapter;

/**
 * @author Michael Zhukov
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRvNews = (RecyclerView) findViewById(R.id.rv_news);

        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRvNews.setLayoutManager(layoutManager);
        mRvNews.setItemAnimator(itemAnimator);

        final Bundle refreshBundle = new Bundle();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBundle.putBoolean(NewsLoader.REFRESH_BUNDLE, true);
                MainActivity.this.getLoaderManager()
                        .restartLoader(R.integer.news_loader_id, refreshBundle, MainActivity.this);
            }
        });
        refreshBundle.putBoolean(NewsLoader.REFRESH_BUNDLE, false);
        getLoaderManager().initLoader(R.integer.news_loader_id, refreshBundle, this);
    }

    private NewsCardsAdapter initAdapter(List<NewsItem> newsItems) {
        return new NewsCardsAdapter(this, newsItems);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        if (data != null) {
            mRvNews.setAdapter(initAdapter(data));
        }
        if (!Utils.isNetworkAvailable(this)) {
            Snackbar.make(mSwipeRefreshLayout, R.string.no_internet_msg, Snackbar.LENGTH_SHORT)
                    .show();
        }
        mSwipeRefreshLayout.setRefreshing(false);
        getLoaderManager().destroyLoader(R.integer.news_loader_id);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_cache:
                new NewsTable(this).clear();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
