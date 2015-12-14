package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import kailianc.andrew.cmu.edu.flickrgallery.model.Feed;
import kailianc.andrew.cmu.edu.flickrgallery.R;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    public static final String TAG = FeedAdapter.class.getSimpleName();

    private Context mContext;
    private List<Feed> mList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.feed_image);
        }
    }

    public FeedAdapter(Context context, List<Feed> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Feed feed = mList.get(position);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra(PhotoFragment.ARG_FEED, feed);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext)
                .load(feed.getUrl())
                .thumbnail(0.5f)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<Feed> newList) {
        mList.addAll(newList);
    }

    public void clear() {
        mList.clear();
    }
}
