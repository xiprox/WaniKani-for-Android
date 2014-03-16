package tr.xip.wanikani;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import tr.xip.wanikani.managers.ApiManager;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class ProfileFragment extends Fragment implements OnRefreshListener {

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

    LinearLayout mAboutHolder;
    RelativeLayout mWebsiteHolder;
    RelativeLayout mTwitterHolder;

    ViewFlipper mViewFlipper;

    WaniKaniApi api;
    ApiManager apiMan;
    OfflineDataManager dataMan;
    PrefManager prefMan;

    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container,
                false);

        api = new WaniKaniApi(getActivity());
        apiMan = new ApiManager(getActivity());
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

        mAboutHolder = (LinearLayout) rootView.findViewById(R.id.profile_about_holder);
        mWebsiteHolder = (RelativeLayout) rootView.findViewById(R.id.profile_website_holder);
        mTwitterHolder = (RelativeLayout) rootView.findViewById(R.id.profile_twitter_holder);

        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.profile_view_flipper);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_out);

        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.dashboard_pull_to_refresh);

        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        if(prefMan.isProfileFirstTime()) {
            if(mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.showNext();
            }
        }

        setOldValues();

        mPullToRefreshLayout.setRefreshing(true);
        new LoadTask().execute();


        return rootView;
    }

    private void setOldValues() {
        mUsername.setText(dataMan.getUsername());
        mTitle.setText(dataMan.getTitle());
        mLevel.setText(dataMan.getLevel() + "");
        mTopicsCount.setText(dataMan.getTopicsCount() + "");
        mPostsCount.setText(dataMan.getPostsCount() + "");

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        mCreationDate.setText(sdf.format(dataMan.getCreationDate()));

        if(dataMan.getAbout().length() != 0) {
            mAbout.setText(dataMan.getAbout());
            mAboutHolder.setVisibility(View.VISIBLE);
        } else {
            mAboutHolder.setVisibility(View.GONE);
        }

        if(dataMan.getWebsite().length() != 0) {
            mWebsite.setText(dataMan.getWebsite());
            mWebsiteHolder.setVisibility(View.VISIBLE);
        } else {
            mWebsiteHolder.setVisibility(View.GONE);
        }

        if(dataMan.getTwitter().length() != 0) {
            mTwitter.setText(dataMan.getTwitter());
            mTwitterHolder.setVisibility(View.VISIBLE);
        } else {
            mTwitterHolder.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRefreshStarted(View view) {
        new LoadTask().execute();
    }

    public class LoadTask extends AsyncTask<Void, Void, String> {
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
        protected String doInBackground(Void... voids) {
            try {
                Picasso.with(getActivity())
                        .load("http://www.gravatar.com/avatar/" + apiMan.getGravatar() + "?s=200")
                        .placeholder(R.drawable.profile_loading)
                        .error(R.drawable.profile_error)
                        .fit()
                        .into(mAvatar);

                username = apiMan.getUsername();
                title = apiMan.getTitle();
                level = apiMan.getLevel();
                topicsCount = apiMan.getTopicsCount();
                postsCount = apiMan.getPostsCount();
                website = apiMan.getWebsite();
                twitter = apiMan.getTwitter();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
                creationDate = sdf.format(apiMan.getCreationDate());

                about = apiMan.getAbout();

                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("success")) {
                mUsername.setText(username);
                mTitle.setText(title);
                mLevel.setText(level + "");
                mTopicsCount.setText(topicsCount + "");
                mPostsCount.setText(postsCount + "");
                mCreationDate.setText(creationDate + "");

                if(about.length() != 0) {
                    mAbout.setText(about);
                    mAboutHolder.setVisibility(View.VISIBLE);
                } else {
                    mAboutHolder.setVisibility(View.GONE);
                }

                if(website != null && website.length() != 0) {
                    mWebsite.setText(website);
                    mWebsiteHolder.setVisibility(View.VISIBLE);
                } else {
                    mWebsiteHolder.setVisibility(View.GONE);
                }

                if(twitter != null && twitter.length() != 0) {
                    mTwitter.setText(twitter);
                    mTwitterHolder.setVisibility(View.VISIBLE);
                } else {
                    mTwitterHolder.setVisibility(View.GONE);
                }

                if(mViewFlipper.getDisplayedChild() == 1) {
                    mViewFlipper.showPrevious();
                }

                if(prefMan.isProfileFirstTime()) {
                    prefMan.setProfileFirstTime(false);
                }
            }

            mPullToRefreshLayout.setRefreshComplete();
        }
    }
}
