package tr.xip.wanikani;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.tasks.UserInfoGetTask;
import tr.xip.wanikani.tasks.callbacks.UserInfoGetTaskCallbacks;
import tr.xip.wanikani.utils.CircleTransformation;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        UserInfoGetTaskCallbacks {

    Context context;

    ImageView mAvatar;
    TextView mUsername;
    TextView mTitle;
    TextView mLevel;
    TextView mTopicsCount;
    TextView mPostsCount;
    TextView mCreationDate;
    TextView mAbout;
    TextView mWebsite;
    TextView mTwitter;

    CardView mAboutCard;

    RelativeLayout mWebsiteHolder;
    RelativeLayout mTwitterHolder;

    ViewFlipper mViewFlipper;

    WaniKaniApi api;
    OfflineDataManager dataMan;
    PrefManager prefMan;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private BroadcastReceiver mRetrofitConnectionTimeoutErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        }
    };

    private BroadcastReceiver mRetrofitConnectionErorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        }
    };

    private BroadcastReceiver mRetrofitUnknownErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    public void onPause() {
        unregisterReceivers();
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container,
                false);

        context = getActivity();

        api = new WaniKaniApi(getActivity());
        dataMan = new OfflineDataManager(getActivity());
        prefMan = new PrefManager(getActivity());

        mAvatar = (ImageView) rootView.findViewById(R.id.profile_avatar);
        mUsername = (TextView) rootView.findViewById(R.id.profile_username);
        mTitle = (TextView) rootView.findViewById(R.id.profile_title);
        mLevel = (TextView) rootView.findViewById(R.id.profile_level);
        mTopicsCount = (TextView) rootView.findViewById(R.id.profile_topics_count);
        mPostsCount = (TextView) rootView.findViewById(R.id.profile_posts_count);
        mCreationDate = (TextView) rootView.findViewById(R.id.profile_creation_date);
        mAbout = (TextView) rootView.findViewById(R.id.profile_about);
        mWebsite = (TextView) rootView.findViewById(R.id.profile_website);
        mTwitter = (TextView) rootView.findViewById(R.id.profile_twitter);

        mAboutCard = (CardView) rootView.findViewById(R.id.profile_about_card);

        mWebsiteHolder = (RelativeLayout) rootView.findViewById(R.id.profile_website_holder);
        mTwitterHolder = (RelativeLayout) rootView.findViewById(R.id.profile_twitter_holder);

        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.profile_view_flipper);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_out);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.dashboard_pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh);

        if (prefMan.isProfileFirstTime()) {
            if (mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.showNext();
            }
        }

        loadOfflineValues();

        if (!MainActivity.isFirstSyncProfileDone) {
            mSwipeRefreshLayout.setRefreshing(true);

            fetchData();

            MainActivity.isFirstSyncProfileDone = true;
        } else {
            fetchData();
        }

        return rootView;
    }

    public void fetchData() {
        new UserInfoGetTask(context, this).executeParallel();
    }

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(context).registerReceiver(mRetrofitConnectionTimeoutErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_TIMEOUT()));
        LocalBroadcastManager.getInstance(context).registerReceiver(mRetrofitConnectionErorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_CONNECTION()));
        LocalBroadcastManager.getInstance(context).registerReceiver(mRetrofitUnknownErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_UNKNOWN()));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mRetrofitConnectionTimeoutErrorReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mRetrofitConnectionErorReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mRetrofitUnknownErrorReceiver);
    }

    private void loadOfflineValues() {
        mUsername.setText(dataMan.getUsername());
        mTitle.setText(dataMan.getTitle());
        mLevel.setText(dataMan.getLevel() + "");
        mTopicsCount.setText(dataMan.getTopicsCount() + "");
        mPostsCount.setText(dataMan.getPostsCount() + "");

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        mCreationDate.setText(sdf.format(dataMan.getCreationDate()));

        if (dataMan.getAbout().length() != 0) {
            mAbout.setText(dataMan.getAbout());
            mAboutCard.setVisibility(View.VISIBLE);
        } else {
            mAboutCard.setVisibility(View.GONE);
        }

        if (dataMan.getWebsite().length() != 0) {
            mWebsite.setText(dataMan.getWebsite());
            mWebsiteHolder.setVisibility(View.VISIBLE);
        } else {
            mWebsiteHolder.setVisibility(View.GONE);
        }

        if (dataMan.getTwitter().length() != 0) {
            mTwitter.setText(dataMan.getTwitter());
            mTwitterHolder.setVisibility(View.VISIBLE);
        } else {
            mTwitterHolder.setVisibility(View.GONE);
        }
    }

    private void saveOfflineValues(User user) {
        dataMan.setUsername(user.getUsername());
        dataMan.setTitle(user.getTitle());
        dataMan.setLevel(user.getLevel());
        dataMan.setTopicsCount(user.getTopicsCount());
        dataMan.setPostsCount(user.getPostsCount());
        dataMan.setCreationDate(user.getCreationDate());
        dataMan.setAbout(user.getAbout());
        dataMan.setWebsite(user.getWebsite());
        dataMan.setTwitter(user.getTwitter());
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onUserInfoGetTaskPreExecute() {
        Picasso.with(context)
                .load(R.drawable.profile_loading)
                .fit()
                .transform(new CircleTransformation())
                .into(mAvatar);
    }

    @Override
    public void onUserInfoGetTaskPostExecute(User user) {
        if (user != null) {
            Picasso.with(getActivity())
                    .load("http://www.gravatar.com/avatar/" + user.getGravatar() + "?s=200")
                    .error(R.drawable.profile_error)
                    .fit()
                    .transform(new CircleTransformation())
                    .into(mAvatar);

            mUsername.setText(user.getUsername());
            mTitle.setText(user.getTitle());
            mLevel.setText(user.getLevel() + "");
            mTopicsCount.setText(user.getTopicsCount() + "");
            mPostsCount.setText(user.getPostsCount() + "");
            mCreationDate.setText(new SimpleDateFormat("MMMM d, yyyy").format(user.getCreationDate()));

            if (user.getAbout().length() != 0) {
                mAbout.setText(user.getAbout());
                mAboutCard.setVisibility(View.VISIBLE);
            } else
                mAboutCard.setVisibility(View.GONE);

            if (user.getWebsite() != null && user.getWebsite().trim().length() != 0) {
                mWebsite.setText(user.getWebsite());
                mWebsiteHolder.setVisibility(View.VISIBLE);
            } else
                mWebsiteHolder.setVisibility(View.GONE);

            if (user.getTwitter() != null && user.getTwitter().trim().length() != 0) {
                mTwitter.setText(user.getTwitter());
                mTwitterHolder.setVisibility(View.VISIBLE);
            } else
                mTwitterHolder.setVisibility(View.GONE);

            if (mViewFlipper.getDisplayedChild() == 1) {
                mViewFlipper.showPrevious();
            }

            if (prefMan.isProfileFirstTime()) {
                prefMan.setProfileFirstTime(false);
            }

            saveOfflineValues(user);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
