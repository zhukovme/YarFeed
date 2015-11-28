package me.zhukov.yarfeed.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.zhukov.yarfeed.R;
import me.zhukov.yarfeed.model.NewsItem;

/**
 * @author Michael Zhukov
 */
public class NewsCardsAdapter extends RecyclerView.Adapter<NewsCardsAdapter.NewsViewHolder> {

    private List<NewsItem> mNewsItems;

    public NewsCardsAdapter(List<NewsItem> newsItems) {
        this.mNewsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem newsItem = mNewsItems.get(position);
        holder.title.setText(newsItem.getTitle());
        holder.description.setText(newsItem.getDescription());
        holder.pubDate.setText(newsItem.getPubDate());
        holder.enclosure.setImageBitmap(newsItem.getEnclosure());
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView pubDate;
        private ImageView enclosure;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            pubDate = (TextView) itemView.findViewById(R.id.tv_pub_date);
            enclosure = (ImageView) itemView.findViewById(R.id.iv_enclosure);
        }
    }
}
