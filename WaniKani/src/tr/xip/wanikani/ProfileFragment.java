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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.CircleTransformation;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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

    LinearLayout mAvatarCard;
    LinearLayout mAboutCard;
    LinearLayout mDetailsCard;

    RelativeLayout mWebsiteHolder;
    RelativeLayout mTwitterHolder;

    ViewFlipper mViewFlipper;

    WaniKaniApi api;
    OfflineDataManager dataMan;
    PrefManager prefMan;

    User user;

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

        mAvatarCard = (LinearLayout) rootView.findViewById(R.id.profile_avatar_card);
        mAboutCard = (LinearLayout) rootView.findViewById(R.id.profile_about_card);
        mDetailsCard = (LinearLayout) rootView.findViewById(R.id.profile_details_card);

        mWebsiteHolder = (RelativeLayout) rootView.findViewById(R.id.profile_website_holder);
        mTwitterHolder = (RelativeLayout) rootView.findViewById(R.id.profile_twitter_holder);

        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.profile_view_flipper);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_out);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.dashboard_pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(R.color.swipe_refresh_1, R.color.swipe_refresh_2,
                R.color.swipe_refresh_3, R.color.swipe_refresh_4);

        if (prefMan.isProfileFirstTime()) {
            if (mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.showNext();
            }
        }

        loadOfflineValues();

        if (!MainActivity.isFirstSyncProfileDone) {
            mSwipeRefreshLayout.setRefreshing(true);

            if (Build.VERSION.SDK_INT >= 11)
                new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new LoadTask().execute();

            MainActivity.isFirstSyncProfileDone = true;
        } else {
            if (Build.VERSION.SDK_INT >= 11)
                new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new LoadTask().execute();
        }

        return rootView;
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

    private void saveOfflineValues() {
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
        if (Build.VERSION.SDK_INT >= 11)
            new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new LoadTask().execute();

    }

    public class LoadTask extends AsyncTask<Void, Void, String> {
        String gravatar = dataMan.getGravatar();
        String username;
        String title;
        int level;
        int topicsCount;
        int postsCount;
        String creationDate;
        String about;
        String website;
        String twitter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Picasso.with(context)
                    .load(R.drawable.profile_loading)
                    .fit()
                    .transform(new CircleTransformation())
                    .into(mAvatar);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                user = api.getUser();
                gravatar = user.getGravatar();
                username = user.getUsername();
                title = user.getTitle();
                level = user.getLevel();
                topicsCount = user.getTopicsCount();
                postsCount = user.getPostsCount();
                website = user.getWebsite();
                twitter = user.getTwitter();

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
                creationDate = sdf.format(user.getCreationDate());

                about = user.getAbout();

                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Picasso.with(getActivity())
                    .load("http://www.gravatar.com/avatar/" + gravatar + "?s=200")
                    .error(R.drawable.profile_error)
                    .fit()
                    .transform(new CircleTransformation())
                    .into(mAvatar);

            if (result.equals("success")) {
                mUsername.setText(username);
                mTitle.setText(title);
                mLevel.setText(level + "");
                mTopicsCount.setText(topicsCount + "");
                mPostsCount.setText(postsCount + "");
                mCreationDate.setText(creationDate + "");

                if (about.length() != 0) {
                    mAbout.setText(about);
                    mAboutCard.setVisibility(View.VISIBLE);
                } else {
                    mAboutCard.setVisibility(View.GONE);
                }

                if (website != null && website.length() != 0) {
                    mWebsite.setText(website);
                    mWebsiteHolder.setVisibility(View.VISIBLE);
                } else {
                    mWebsiteHolder.setVisibility(View.GONE);
                }

                if (twitter != null && twitter.length() != 0) {
                    mTwitter.setText(twitter);
                    mTwitterHolder.setVisibility(View.VISIBLE);
                } else {
                    mTwitterHolder.setVisibility(View.GONE);
                }

                if (mViewFlipper.getDisplayedChild() == 1) {
                    mViewFlipper.showPrevious();
                }

                if (prefMan.isProfileFirstTime()) {
                    prefMan.setProfileFirstTime(false);
                }

                saveOfflineValues();
            }

            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
