package tr.xip.wanikani.app.fragment;

import android.annotation.SuppressLint;
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

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.MainActivity;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.graphics.bitmap.transform.CircleTransformation;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.User;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class ProfileFragment extends Fragment {

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

        if (PrefManager.isProfileFirstTime()) {
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
        WaniKaniApi.getUser().enqueue(new ThroughDbCallback<Request<User>, User>() {
            @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
            @Override
            public void onResponse(Call<Request<User>> call, Response<Request<User>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body().user_information != null) {
                    load(response.body().user_information);
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<Request<User>> call, Throwable t) {
                super.onFailure(call, t);

                User user = DatabaseManager.getUser();
                if (user != null) {
                    load(user);
                }
            }

            void load(User user) {
                Picasso.with(getActivity())
                        .load("http://www.gravatar.com/avatar/" + user.gravatar + "?s=200")
                        .fit()
                        .transform(new CircleTransformation())
                        .into(mAvatar);

                mUsername.setText(user.username);
                mTitle.setText(user.title);
                mLevel.setText(user.level + "");
                mTopicsCount.setText(user.topics_count + "");
                mPostsCount.setText(user.posts_count + "");
                mCreationDate.setText(new SimpleDateFormat("MMMM d, yyyy").format(user.getCreationDateInMillis()));

                if (user.about.length() != 0) {
                    mAbout.setText(user.about);
                    mAboutCard.setVisibility(View.VISIBLE);
                } else
                    mAboutCard.setVisibility(View.GONE);

                if (user.website != null && user.website.trim().length() != 0) {
                    mWebsite.setText(user.website);
                    mWebsiteHolder.setVisibility(View.VISIBLE);
                } else
                    mWebsiteHolder.setVisibility(View.GONE);

                if (user.twitter != null && user.twitter.trim().length() != 0) {
                    mTwitter.setText(user.twitter);
                    mTwitterHolder.setVisibility(View.VISIBLE);
                } else
                    mTwitterHolder.setVisibility(View.GONE);

                if (mViewFlipper.getDisplayedChild() == 1) {
                    mViewFlipper.showPrevious();
                }

                if (PrefManager.isProfileFirstTime()) {
                    PrefManager.setProfileFirstTime(false);
                }
            }
        });
    }
}
