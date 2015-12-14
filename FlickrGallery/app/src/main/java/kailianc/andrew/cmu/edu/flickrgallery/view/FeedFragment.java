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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.reginald.swiperefresh.CustomSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kailianc.andrew.cmu.edu.flickrgallery.R;
import kailianc.andrew.cmu.edu.flickrgallery.control.UrlManager;
import kailianc.andrew.cmu.edu.flickrgallery.model.Feed;
import kailianc.andrew.cmu.edu.flickrgallery.model.SuggestionProvider;


/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 */
public class FeedFragment extends Fragment {
    public static final String TAG = PhotoFragment.class.getSimpleName();

    private static final int SPAN_COUNT = 3;
    private static final int FEED_PER_PAGE = 100;

    private RecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;
    private RequestQueue mRq;
    private boolean mLoading = false;
    private boolean mHasMore = true;
    private SearchView mSearchView = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed,
                container,
                false);

        mRq = Volley.newRequestQueue(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
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

        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FeedAdapter(getActivity(), new ArrayList<Feed>());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void startLoading() {
        mLoading = true;
        int totalItem = mLayoutManager.getItemCount();
        final int page = (totalItem / FEED_PER_PAGE) + 1;

        String query = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(UrlManager.PREF_SEARCH_QUERY, null);

        String url = UrlManager.getInstance().getFeedsUrl(query, page);
        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Feed> result = new ArrayList<Feed>();
                        try {
                            JSONObject photos = response.getJSONObject("photos");
                            if (photos.getInt("pages") == page) {
                                mHasMore = false;
                            }
                            JSONArray photoArr = photos.getJSONArray("photo");
                            for (int i = 0 ; i < photoArr.length() ; i++) {
                                JSONObject feedObj = photoArr.getJSONObject(i);
                                Feed feed = new Feed(
                                        feedObj.getString("id"),
                                        feedObj.getString("secret"),
                                        feedObj.getString("server"),
                                        feedObj.getString("farm"));
                                result.add(feed);
                            }
                        } catch (JSONException e) {
                        }
                        mAdapter.addAll(result);
                        mAdapter.notifyDataSetChanged();
                        mLoading = false;
                        mSwipeRefreshLayout.refreshComplete();
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
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                selectionHandled = true;
                break;
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
                        .putString(UrlManager.PREF_SEARCH_QUERY, null)
                        .commit();
                refresh();
                selectionHandled = true;
                break;
            case R.id.menu_item_reset:
                reset();
                selectionHandled = true;
                break;
            default:
                selectionHandled = super.onOptionsItemSelected(item);
        }

        return selectionHandled;
    }

    public Parcelable getState() {
        if(mRecyclerView != null) {
            Log.d(TAG, "getState");
            return mRecyclerView.getLayoutManager().onSaveInstanceState();
        }
        return null;
    }

    public void setState(Parcelable state) {
        if(mRecyclerView != null) {
            Log.d(TAG, "setState");
            mRecyclerView.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    private void reset() {
        if(mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    public void refresh() {
        mAdapter.clear();
        startLoading();
    }
}
