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

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import kailianc.andrew.cmu.edu.flickrgallery.R;
import kailianc.andrew.cmu.edu.flickrgallery.control.FlickerClient;
import kailianc.andrew.cmu.edu.flickrgallery.dagger.FlickerApplication;
import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotoInfoResponse;
import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotosResponse;
import kailianc.andrew.cmu.edu.flickrgallery.model.Photo;
import kailianc.andrew.cmu.edu.flickrgallery.model.PhotoDescription;

/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * Customized fragment class for single photo screen.<p>
 *
 * In this screen, Glide is used to load single photo to ImageView<br>
 * in a smooth style(from half size thumbnail to full size).<p>
 *
 * Volley is used for downloading single photo description text.<br>
 * DownloadManager is used for downloading orignal single photo data.<p>
 *
 * Volley is a fast and efficient third party library for HTTP transmission,<br>
 * JSON format parsing and stream downloading. However, it is not suitable for<br>
 * large data download. In that occasion, DownloadManager should be used.<p>
 *
 */
public class PhotoFragment extends Fragment implements FlickerClient.Listener {

    @Inject FlickerClient mFlickerClient;

    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.desc_text) TextView mDescText;
    @BindView(R.id.photo) ImageView mImageView;
    @BindView(R.id.rGraphView) GraphView mrGraphView;
    @BindView(R.id.gGraphView) GraphView mgGraphView;
    @BindView(R.id.bGraphView) GraphView mbGraphView;
    @BindView(R.id.download) LinearLayout mDownloadView;
    @BindView(R.id.open) LinearLayout mOpenView;

    private Bitmap mBitmap;
    private Photo mPhoto;
    private DownloadManager mDownloadManager;
    private Parcelable mState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ((FlickerApplication)getActivity().getApplication()).getAppComponent().inject(this);
        mFlickerClient.setListener(this);
    }

    @Override
    public void onDestroy() {
        mFlickerClient.removeListener(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);

        mPhoto = (Photo) getActivity().getIntent().getSerializableExtra("photo");

        mDownloadManager = (DownloadManager) getActivity().getSystemService(
                getActivity().DOWNLOAD_SERVICE);

        mProgressBar.setVisibility(View.VISIBLE);

        mImageView = (ImageView) view.findViewById(R.id.photo);
        Glide.with(this).load(mPhoto.url()).thumbnail(0.5f).into(mImageView);

        // work thread for histogram display
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mBitmap = Glide.
                            with(getActivity()).
                            load(mPhoto.url()).
                            asBitmap().
                            into(128, 128).
                            get();
                } catch (final ExecutionException | InterruptedException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void dummy) {
                if (null != mBitmap) {
                    int height = mBitmap.getHeight();
                    int width = mBitmap.getWidth();

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
                }
            }
        }.execute();

        // download original single photo
        mDownloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPhoto();
                Toast.makeText(getActivity(), "Start downloading", Toast.LENGTH_LONG).show();
            }
        });

        // open url link for Flickr official app
        mOpenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp();
            }
        });

        // load original photo
        startLoading();
        return view;
    }

    @Override
    public void onQuerySuccess(GetPhotosResponse response) { }

    @Override
    public void onQueryFailure(String message) { }

    @Override
    public void onGetInfoSuccess(GetPhotoInfoResponse response) {
        PhotoDescription description = response.photoInfo().description();
        mDescText.setText(description.content());
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onGetInfoFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    // function for downloading original photo when download button is pressed
    private void downloadPhoto() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mPhoto.url()));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("SharkFeed Download");
        request.setDescription(mPhoto.url());
        mDownloadManager.enqueue(request);
    }

    // function for opening Flickr official app when open button is pressed
    private void openApp () {
        String url = FlickerClient.getFlickrUrl(mPhoto.id());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    // function for loading single photo description by Volley
    private void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mFlickerClient.getPhotoInfo(mPhoto.id());
    }

    // restore state when configuration changes
    public Parcelable getState() {
        return mState;
    }

    public void setState(Parcelable parcelable) {
        mState = parcelable;
    }
}
