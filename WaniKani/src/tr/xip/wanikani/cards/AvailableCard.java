package tr.xip.wanikani.cards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tr.xip.wanikani.BroadcastIntents;
import tr.xip.wanikani.Browser;
import tr.xip.wanikani.DashboardFragment;
import tr.xip.wanikani.R;
import tr.xip.wanikani.WebReviewActivity;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.StudyQueue;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class AvailableCard extends Fragment {

    public static final int BROWSER_REQUEST = 1;

    View rootView;
    Context context;

    WaniKaniApi api;
    OfflineDataManager dataMan;
    Utils utils;

    ImageView mLessonsGo;
    ImageView mReviewsGo;
    TextView mLessonsAvailable;
    TextView mReviewsAvailable;
    LinearLayout mCard;

    int lessonsAvailable;
    int reviewsAvailable;
    boolean isVacationModeActive;

    AvailableCardListener mListener;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= 11)
                new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new LoadTask().execute();
        }
    };

    public void setListener(AvailableCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    @Override
    public void onCreate(Bundle state) {
        api = new WaniKaniApi(getActivity());
        utils = new Utils(getActivity());
        dataMan = new OfflineDataManager(getActivity());
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_available, null);

        context = getActivity();

        mLessonsGo = (ImageView) rootView.findViewById(R.id.card_available_lessons_go);
        mReviewsGo = (ImageView) rootView.findViewById(R.id.card_available_reviews_go);

        mLessonsGo.setColorFilter(getResources().getColor(R.color.text_gray),
                PorterDuff.Mode.SRC_ATOP);
        mReviewsGo.setColorFilter(getResources().getColor(R.color.text_gray),
                PorterDuff.Mode.SRC_ATOP);

        mLessonsAvailable = (TextView) rootView.findViewById(R.id.card_available_lessons);
        mReviewsAvailable = (TextView) rootView.findViewById(R.id.card_available_reviews);

        mCard = (LinearLayout) rootView.findViewById(R.id.card_available_card);

        setUpParentOnClicks();

        loadOfflineValues();

        return rootView;
    }

    private void loadOfflineValues() {
        mLessonsAvailable.setText(dataMan.getLessonsAvailable() + "");
        mReviewsAvailable.setText(dataMan.getReviewsAvailable() + "");
    }

    private void saveOfflineValues() {
        dataMan.setLessonsAvailable(lessonsAvailable);
        dataMan.setReviewsAvailable(reviewsAvailable);
    }

    private void setUpParentOnClicks() {

        mLessonsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getActivity(), Browser.class);
                intent.putExtra(Browser.ARG_ACTION, Browser.ACTION_LESSON);
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);*/
                Intent intent = new PrefManager(context).getWebViewIntent();
                intent.setAction (WebReviewActivity.OPEN_ACTION);
                intent.setData(Uri.parse(Browser.LESSON_URL));
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);
            }
        });

        mReviewsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(getActivity(), Browser.class);
                intent.putExtra(Browser.ARG_ACTION, Browser.ACTION_REVIEW);
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);
                */
                Intent intent = new PrefManager(context).getWebViewIntent();
                intent.setAction (WebReviewActivity.OPEN_ACTION);
                intent.setData(Uri.parse(Browser.REVIEW_URL));
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);
            }
        });
    }

    public interface AvailableCardListener {
        public void onAvailableCardSyncFinishedListener(String result);
    }

    private class LoadTask extends AsyncTask<String, Void, String> {
        StudyQueue studyQueue;

        @Override
        protected String doInBackground(String... strings) {
            try {
                studyQueue = api.getStudyQueue();

                lessonsAvailable = studyQueue.getAvailableLesonsCount();
                reviewsAvailable = studyQueue.getAvailableReviewsCount();
                isVacationModeActive = api.isVacationModeActive(studyQueue.getUserInfo());
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                if (!isVacationModeActive) {
                    mLessonsAvailable.setText(lessonsAvailable + "");
                    mReviewsAvailable.setText(reviewsAvailable + "");

                    mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);

                    saveOfflineValues();
                } else {
                    mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
                }
            } else {
                // Vacation mode is handled in DashboardFragment
                mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
            }
        }
    }
}
