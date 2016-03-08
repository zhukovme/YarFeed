package me.zhukov.yarfeed.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import me.zhukov.yarfeed.R;
import me.zhukov.yarfeed.database.NewsTable;
import me.zhukov.yarfeed.loader.NewsLoader;
import me.zhukov.yarfeed.model.NewsItem;
import me.zhukov.yarfeed.ui.adapter.NewsCardsAdapter;
import me.zhukov.yarfeed.util.NetworkHelper;

/**
 * @author Michael Zhukov
 */
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItem>>,
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRvNews;
    private List<NewsItem> mNewsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRvNews = (RecyclerView) findViewById(R.id.rv_news);

        setSupportActionBar(toolbar);

        NewsTable.INSTANCE.open(this);
        mNewsItems = NewsTable.INSTANCE.getNewsList();

        mRvNews.setLayoutManager(new LinearLayoutManager(this));
        mRvNews.setAdapter(new NewsCardsAdapter(this, mNewsItems));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (mNewsItems.isEmpty()) {
            getLoaderManager().initLoader(R.integer.news_loader_id, Bundle.EMPTY, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewsTable.INSTANCE.close();
    }

    @Override
    public void onRefresh() {
        getLoaderManager().initLoader(R.integer.news_loader_id, Bundle.EMPTY, MainActivity.this);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.integer.news_loader_id:
                return new NewsLoader(this, mSwipeRefreshLayout);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        int id = loader.getId();
        switch (id) {
            case R.integer.news_loader_id:
                if (data != null) {
                    mNewsItems.clear();
                    mNewsItems.addAll(data);
                    mRvNews.getAdapter().notifyDataSetChanged();
                }

                if (!NetworkHelper.INSTANCE.isNetworkAvailable(getBaseContext())) {
                    Snackbar.make(
                            mSwipeRefreshLayout,
                            R.string.no_internet_msg,
                            Snackbar.LENGTH_SHORT).show();
                }

                mSwipeRefreshLayout.setRefreshing(false);
        }
        getLoaderManager().destroyLoader(id);
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
                NewsTable.INSTANCE.clear();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
