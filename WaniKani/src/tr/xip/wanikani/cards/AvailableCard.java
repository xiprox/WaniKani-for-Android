package tr.xip.wanikani.cards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import tr.xip.wanikani.BroadcastIntents;
import tr.xip.wanikani.R;
import tr.xip.wanikani.Webview;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.managers.ApiManager;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class AvailableCard extends Fragment {

    WaniKaniApi api;
    ApiManager apiMan;
    OfflineDataManager dataMan;
    Utils utils;

    View rootView;

    LinearLayout mLessonsParent;
    LinearLayout mReviewsParent;

    TextView mLessonsAvailable;
    TextView mReviewsAvailable;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                new LoadTask().execute();
        }
    };

    @Override
    public void onCreate(Bundle state) {
        api = new WaniKaniApi(getActivity());
        apiMan = new ApiManager(getActivity());
        utils = new Utils(getActivity());
        dataMan = new OfflineDataManager(getActivity());
        super.onCreate(state);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_available, null);

        mLessonsParent = (LinearLayout) rootView.findViewById(R.id.card_available_lessons_parent);
        mReviewsParent = (LinearLayout) rootView.findViewById(R.id.card_available_reviews_parent);

        mLessonsAvailable = (TextView) rootView.findViewById(R.id.card_available_lessons);
        mReviewsAvailable = (TextView) rootView.findViewById(R.id.card_available_reviews);

        setUpParentOnClicks();

        setOldValues();

        return rootView;
    }

    private void setOldValues() {
        mLessonsAvailable.setText(dataMan.getLessonsAvailable() + "");
        mReviewsAvailable.setText(dataMan.getReviewsAvailable() + "");
    }

    private void setUpParentOnClicks() {

        mLessonsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Webview.class);
                intent.putExtra("action", "Lesson");
                getActivity().startActivity(intent);
            }
        });

        mReviewsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Webview.class);
                intent.putExtra("action", "Review");
                getActivity().startActivity(intent);
            }
        });
    }

    private class LoadTask extends AsyncTask<String, Void, String> {
        int lessonsAvailable;
        int reviewsAvailable;

        @Override
        protected String doInBackground(String... strings) {
            try {
                lessonsAvailable = apiMan.getLessonsAvailable();
                reviewsAvailable = apiMan.getReviewsAvailable();
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                mLessonsAvailable.setText(lessonsAvailable + "");
                mReviewsAvailable.setText(reviewsAvailable + "");
                Intent intent = new Intent(BroadcastIntents.FINISHED_SYNC_AVAILABLE_CARD());
                intent.putExtra("action", "show");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            } else {
                Intent intent = new Intent(BroadcastIntents.FINISHED_SYNC_AVAILABLE_CARD());
                intent.putExtra("action", "hide");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }


        }
    }
}
