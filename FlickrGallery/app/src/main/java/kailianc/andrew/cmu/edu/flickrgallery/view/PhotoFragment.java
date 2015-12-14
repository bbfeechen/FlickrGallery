package kailianc.andrew.cmu.edu.flickrgallery.view;


import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import kailianc.andrew.cmu.edu.flickrgallery.R;
import kailianc.andrew.cmu.edu.flickrgallery.control.UrlManager;
import kailianc.andrew.cmu.edu.flickrgallery.model.Feed;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 */
public class PhotoFragment extends Fragment {
    public static final String TAG = PhotoFragment.class.getSimpleName();

    public static final String ARG_FEED = "ARGS_FEED";

    private Feed mFeed;
    private ProgressBar mProgressBar;
    private TextView mDescText;
    private RequestQueue mRq;
    private boolean mLoading = false;
    private DownloadManager mDownloadManager;
    private ImageView mPhoto;
    private GraphView mrGraphView;
    private GraphView mgGraphView;
    private GraphView mbGraphView;
    private Bitmap mBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo,
                container,
                false);

        mFeed = (Feed) getActivity().getIntent().getSerializableExtra(ARG_FEED);

        mDownloadManager = (DownloadManager) getActivity().getSystemService(
                getActivity().DOWNLOAD_SERVICE);
        mRq = Volley.newRequestQueue(getActivity());

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mDescText = (TextView) view.findViewById(R.id.desc_text);

        mPhoto = (ImageView) view.findViewById(R.id.photo);
        Glide.with(this).load(mFeed.getUrl()).thumbnail(0.5f).into(mPhoto);

        mrGraphView = (GraphView)view.findViewById(R.id.rGraphView);
        mgGraphView = (GraphView)view.findViewById(R.id.gGraphView);
        mbGraphView = (GraphView)view.findViewById(R.id.bGraphView);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mBitmap = Glide.
                            with(getActivity()).
                            load(mFeed.getUrl()).
                            asBitmap().
                            into(128, 128).
                            get();
                } catch (final ExecutionException e) {
                } catch (final InterruptedException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void dummy) {
                if (null != mBitmap) {
                    int height = mBitmap.getHeight();
                    int width = mBitmap.getWidth();

                    int[] histogramY = new int[height * width];
                    int[] histogramR = new int[height * width];
                    int[] histogramG = new int[height * width];
                    int[] histogramB = new int[height * width];

                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            int color = mBitmap.getPixel(i, j);
                            int r = Color.red(color);
                            int g = Color.green(color);
                            int b = Color.blue(color);
                            histogramR[r]++;
                            histogramG[g]++;
                            histogramB[b]++;
                        }
                    }

                    if (mrGraphView != null &&
                            mgGraphView != null && mbGraphView != null) {
                        mrGraphView.setHistogram(histogramR);
                        mgGraphView.setHistogram(histogramG);
                        mbGraphView.setHistogram(histogramB);
                    }
                };
            }
        }.execute();

        LinearLayout downloadView = (LinearLayout) view.findViewById(R.id.download);
        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPhoto();
                Toast.makeText(getActivity(), "Start downloading", Toast.LENGTH_LONG).show();
            }
        });

        LinearLayout openView = (LinearLayout) view.findViewById(R.id.open);
        openView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp();
            }
        });

        startLoading();
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void downloadPhoto() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mFeed.getUrl()));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("SharkFeed Download");
        request.setDescription(mFeed.getUrl());
        mDownloadManager.enqueue(request);
    }

    private void openApp () {
        String url = UrlManager.getInstance().getFlickrUrl(mFeed.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void startLoading() {
        mLoading = true;
        mProgressBar.setVisibility(View.VISIBLE);
        String url =  UrlManager.getInstance().getPhotoInfoUrl(mFeed.getId());
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject photo = response.getJSONObject("photo");
                            JSONObject descObj = photo.getJSONObject("description");
                            String desc = descObj.getString("_content");
                            mDescText.setText(desc);
                        } catch (JSONException e) {

                        }
                        mProgressBar.setVisibility(View.GONE);
                        mLoading = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        request.setTag(TAG);
        mRq.add(request);
    }

    private void stopLoading() {
        if (mRq != null) {
            mRq.cancelAll(TAG);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLoading();
    }

    public Parcelable getState() {
        return null;
    }

    public void setState(Parcelable state) {
    }
}
