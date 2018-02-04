package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import java.util.ArrayList;

import javax.inject.Inject;
import kailianc.andrew.cmu.edu.flickrgallery.R;
import kailianc.andrew.cmu.edu.flickrgallery.control.FlickerClient;
import kailianc.andrew.cmu.edu.flickrgallery.dagger.FlickerApplication;
import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotoInfoResponse;
import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotosResponse;
import kailianc.andrew.cmu.edu.flickrgallery.model.Photo;
import kailianc.andrew.cmu.edu.flickrgallery.model.Photos;
import kailianc.andrew.cmu.edu.flickrgallery.model.SuggestionProvider;


/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * Customized fragment class for infinite photo scrolling, automatically requesting<br>
 * and loading photos when scrolling down.<p>
 *
 * In this screen, the following features are supported<br>
 * 1) Swipe-to-refresh design pattern on the top of the screen, with state machine to show<br>
 *    fish rotating animation and text<br>
 * 2) RecyclerView plus GridLayoutManger are used to support efficient scrolling and loading<br>
 * 3) Volley is used to support fast and efficient HTTP downloading and JSON parsing<br>
 * 4) Glide is used to support asynchronous image loading for each grid photo item<br>
 * 5) Configuration changes are handled by setting fragment to retained and providing saving<br>
 *    and restoring interface<br>
 * 6) Menu with Search, Search Suggestion, Move back to Top, Clear Search History options is<br>
 *    provided based on SearchSuggestionProvider and SearchView<br>
 * 7) Lazy loading which loads 100 photos for scrolling and not loading until next page is needed<p>
 *
 * Volley is a fast and efficient third party library for concurrent HTTP request, response,<br>
 * JSON parse and cancel.<p>
 *
 */
public class FeedFragment extends Fragment implements FlickerClient.Listener {

    public static final String TAG = PhotoFragment.class.getSimpleName();

    private static final int COLUMN_NUM = 3;
    private static final int FEED_PER_PAGE = 100;

    @Inject FlickerClient mFlickerClient;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) CustomSwipeRefreshLayout mSwipeRefreshLayout;

    private FeedAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private boolean mLoading = false;
    private boolean mHasMore = true;
    private SearchView mSearchView = null;

    private int mCurrentRequestPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItem = mLayoutManager.getItemCount();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (mHasMore && !mLoading && lastVisibleItem == totalItem - 1) {
                    startLoading();
                }
            }
        });

        mLayoutManager = new GridLayoutManager(getActivity(), COLUMN_NUM);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FeedAdapter(getActivity(), new ArrayList<Photo>());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setCustomHeadview(new SwipeRefresher(getActivity()));
        mSwipeRefreshLayout.setRefreshMode(CustomSwipeRefreshLayout.REFRESH_MODE_PULL);
        mSwipeRefreshLayout.setOnRefreshListener(new CustomSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        startLoading();
        return view;
    }

    public void refresh() {
        mAdapter.clear();
        startLoading();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FlickerApplication)getActivity().getApplication()).getAppComponent().inject(this);
        mFlickerClient.setListener(this);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        mFlickerClient.removeListener(this);
        super.onDestroy();
    }

    @Override
    public void onQuerySuccess(GetPhotosResponse response) {
        Photos photos = response.photos();
        mHasMore = photos.page() == mCurrentRequestPage;

        mAdapter.addAll(photos.photos());
        mAdapter.notifyDataSetChanged();
        mLoading = false;
        mSwipeRefreshLayout.refreshComplete();
    }

    @Override
    public void onQueryFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetInfoSuccess(GetPhotoInfoResponse response) { }

    @Override
    public void onGetInfoFailure(String message) { }

    // http request and response in JSON format
    private void startLoading() {
        mLoading = true;
        int totalItem = mLayoutManager.getItemCount();
        final int page = (totalItem / FEED_PER_PAGE) + 1;

        String query = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(FlickerClient.PREF_SEARCH_QUERY, null);

        mFlickerClient.query(query, page);
        mCurrentRequestPage = page;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView)searchItem.getActionView();
        if(mSearchView != null) {
            mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    String suggestion = getSuggestion(position);
                    if (mSearchView != null && suggestion != null) {
                        mSearchView.setQuery(suggestion, true);
                    }
                    return true;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    String suggestion = getSuggestion(position);
                    if (mSearchView != null && suggestion != null) {
                        mSearchView.setQuery(suggestion, true);
                    }
                    return true;
                }

                // set the SearchView text to the selected history word
                private String getSuggestion(int position) {
                    String suggest = null;
                    if (mSearchView != null) {
                        Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(
                                position);
                        suggest = cursor.getString(cursor
                                .getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                    }
                    return suggest;
                }
            });
        }

        SearchManager searchManager = (SearchManager)getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);

        mSearchView.setSearchableInfo(searchInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selectionHandled;

        switch (item.getItemId()) {
            // search keyword
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                selectionHandled = true;
                break;

            // clear history
            case R.id.menu_item_clear:
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                        SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
                suggestions.clearHistory();
                if(mSearchView != null) {
                    mSearchView.setQuery("", false);
                    mSearchView.setIconified(false);
                }

                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickerClient.PREF_SEARCH_QUERY, null)
                        .apply();
                refresh();
                selectionHandled = true;
                break;

            // move to top
            case R.id.menu_item_reset:
                reset();
                selectionHandled = true;
                break;
            default:
                selectionHandled = super.onOptionsItemSelected(item);
        }

        return selectionHandled;
    }

    // restore state when configuration changes
    public Parcelable getState() {
        if(mRecyclerView != null) {
            Log.d(TAG, "getState");
            return mRecyclerView.getLayoutManager().onSaveInstanceState();
        }
        return null;
    }

    // save state when configuration changes
    public void setState(Parcelable state) {
        if(mRecyclerView != null) {
            Log.d(TAG, "setState");
            mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    // move to top
    private void reset() {
        if(mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }
}
