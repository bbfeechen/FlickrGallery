package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;

import java.util.List;

import kailianc.andrew.cmu.edu.flickrgallery.R;
import kailianc.andrew.cmu.edu.flickrgallery.model.Photo;

/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * Customized Adapter class for RecyclerView to maintain photo items.<p>
 *
 * RecyclerView is used because it is a more advanced and flexible version of ListView,<br>
 * for displaying large data sets that can be scrolled very efficiently by maintaining<br>
 * a limited number of views<p<br>>
 *
 * Glide is a third party Asynchronous Image Loading library which is used for making<br>
 * scrolling any kind of a list of images as smooth and  * fast as possible, but Glide<br>
 * is also effective for almost any case where you need to fetch, resize, and display<br>
 * a remote image.<p>
 *
 * Here, Glide is used to load each photo item to ImageView<br>
 * in a smooth style(from half size thumbnail to full size).<p>
 *
 */
class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context mContext;
    private List<Photo> mList;

    FeedAdapter(Context context, List<Photo> list) {
        mContext = context;
        mList = list;
    }

    /**
     * internal item holder class for each photo (ImageView wrapper)<p>
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image) ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = mList.get(position);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra("photo", photo);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext)
                .load(photo.url())
                .thumbnail(0.5f)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * function to add in all new content<br>
     * @param newList : new feed list<p>
     */
    void addAll(List<Photo> newList) {
        mList.addAll(newList);
    }

    /**
     * function to clear contents<p>
     */
    void clear() {
        mList.clear();
    }
}
