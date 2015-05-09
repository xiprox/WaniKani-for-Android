package tr.xip.wanikani.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import tr.xip.wanikani.R;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.app.activity.MainActivity;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.UserInfoGetTask;
import tr.xip.wanikani.client.task.callback.UserInfoGetTaskCallbacks;
import tr.xip.wanikani.graphics.bitmap.transform.CircleTransformation;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class ProfileFragment extends Fragment implements UserInfoGetTaskCallbacks {

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
    PrefManager prefMan;

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

        if (prefMan.isProfileFirstTime()) {
            if (mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.showNext();
            }
        }

        if (!MainActivity.isFirstSyncProfileDone) {
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

    @Override
    public void onUserInfoGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onUserInfoGetTaskPostExecute(User user) {
        if (user != null) {
            Picasso.with(getActivity())
                    .load("http://www.gravatar.com/avatar/" + user.getGravatar() + "?s=200")
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
        }
    }
}
