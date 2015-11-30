package me.zhukov.yarfeed.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private Context mContext;

    public NewsCardsAdapter(Context context, List<NewsItem> newsItems) {
        this.mContext = context;
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
        if (newsItem.getEnclosure() == null) {
            holder.enclosure.setImageResource(R.drawable.ic_image_area_grey600_48dp);
        } else {
            holder.enclosure.setImageBitmap(newsItem.getEnclosure());
        }
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri link = Uri.parse(mNewsItems.get(getAdapterPosition()).getLink());
                    Intent linkIntent = new Intent(Intent.ACTION_VIEW, link);
                    mContext.startActivity(linkIntent);
                }
            });
        }
    }
}
