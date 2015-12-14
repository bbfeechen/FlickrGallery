package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.reginald.swiperefresh.CustomSwipeRefreshLayout;
import com.reginald.swiperefresh.CustomSwipeRefreshLayout.State;

import kailianc.andrew.cmu.edu.flickrgallery.R;

/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * Customized layout class for supporting swipe-to-refresh function on the top of RecyclerView.<p>
 *
 * Swiperefresh is a third party library, which detects the vertical swipe<br>
 * and triggers callback methods in your app.<p>
 *
 * By adding the widget to your layout file as the parent of a ListView or GridView,<br>
 * and implementing the refresh behavior that gets invoked when the user swipes,<br>
 * the more efficient and nicer user experience will be provided.<p>
 *
 *
 */
public class SwipeRefresher extends LinearLayout implements
        CustomSwipeRefreshLayout.CustomSwipeRefreshHeadLayout {

    // tag for logct
    public static final String TAG = SwipeRefresher.class.getSimpleName();

    // state machine for animation and text display
    private static final SparseArray<String> STATE_MAP = new SparseArray<>();

    private ViewGroup mContainer;
    private TextView mMainTextView;
    private ImageView mImageView;
    private Space mSpace;
    private int mState = -1;

    {
        STATE_MAP.put(0, "STATE_NORMAL");
        STATE_MAP.put(1, "STATE_READY");
        STATE_MAP.put(2, "STATE_REFRESHING");
        STATE_MAP.put(3, "STATE_COMPLETE");
    }

    public SwipeRefresher(Context context) {
        super(context);
        setupLayout();
    }

    // initialize layout
    private void setupLayout() {
        ViewGroup.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.widget_swiperefresher, null);
        addView(mContainer, lp);
        mSpace = (Space) findViewById(R.id.space);
        mImageView = (ImageView) findViewById(R.id.fish_img);
        mMainTextView = (TextView) findViewById(R.id.header_text);
    }

    @Override
    public void onStateChange(State state, State lastState) {
        int stateCode = state.getRefreshState();
        int lastStateCode = lastState.getRefreshState();
        float percent = state.getPercent();

        switch (stateCode) {
            case State.STATE_NORMAL:
                if (percent > 0.5f) {
                    float scale = (percent - 0.5f) / 0.5f;
                    mImageView.setTranslationY( - (mSpace.getHeight() + 20) * scale);
                }
                if (stateCode != lastStateCode) {
                    mImageView.clearAnimation();
                    mMainTextView.setText(getContext().getString(R.string.pull_to_refresh));
                }
                break;
            case State.STATE_READY:
                if (stateCode != lastStateCode) {
                    mMainTextView.setText(getContext().getString(R.string.release_to_refresh));
                }
                break;
            case State.STATE_REFRESHING:
                if (stateCode != lastStateCode) {
                    startRotate();
                    mMainTextView.setText(getContext().getString(R.string.refreshing));
                }
                break;

            default:
        }
        mState = stateCode;
    }

    // display rotate animation from angle -45 to 45 degree in ImageView
    private void startRotate() {
        mImageView.clearAnimation();
        RotateAnimation animation = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, -0.55f);
        animation.setDuration(100);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RotateAnimation repeat = new RotateAnimation(45, -45, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, -0.55f);
                repeat.setDuration(200);
                repeat.setInterpolator(new LinearInterpolator());
                repeat.setRepeatMode(Animation.REVERSE);
                repeat.setRepeatCount(Animation.INFINITE);
                mImageView.startAnimation(repeat);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mImageView.startAnimation(animation);
    }
}
