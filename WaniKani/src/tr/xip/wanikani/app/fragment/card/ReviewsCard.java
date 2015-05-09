package tr.xip.wanikani.app.fragment.card;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.R;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.StudyQueueGetTask;
import tr.xip.wanikani.client.task.callback.StudyQueueGetTaskCallbacks;
import tr.xip.wanikani.utils.Utils;
import tr.xip.wanikani.widget.RelativeTimeTextView;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class ReviewsCard extends Fragment implements StudyQueueGetTaskCallbacks {

    Context context;

    WaniKaniApi api;
    PrefManager prefMan;
    Utils utils;

    View rootView;

    ReviewsCardListener mListener;

    RelativeTimeTextView mNextReview;
    TextView mNextHour;
    TextView mNextDay;

    LinearLayout mMoreReviewsHolder;
    TextView mMoreReviews;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new StudyQueueGetTask(context, ReviewsCard.this).executeParallel();
        }
    };

    public void setListener(ReviewsCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    @Override
    public void onCreate(Bundle state) {
        api = new WaniKaniApi(getActivity());
        prefMan = new PrefManager(getActivity());
        utils = new Utils(getActivity());
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_reviews, null);

        context = getActivity();

        mNextReview = (RelativeTimeTextView) rootView.findViewById(R.id.card_reviews_next_review);
        mNextHour = (TextView) rootView.findViewById(R.id.card_reviews_next_hour);
        mNextDay = (TextView) rootView.findViewById(R.id.card_reviews_next_day);

        mMoreReviewsHolder = (LinearLayout) rootView.findViewById(R.id.card_reviews_more_reviews_holder);
        mMoreReviews = (TextView) rootView.findViewById(R.id.card_reviews_more_reviews);

        return rootView;
    }

    @Override
    public void onStudyQueueGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onStudyQueueGetTaskPostExecute(StudyQueue queue) {
        if (queue != null && queue.getUserInfo() != null) {
            if (!queue.getUserInfo().isVacationModeActive()) {
                mNextHour.setText(queue.getAvailableReviewsNextHourCount() + "");
                mNextDay.setText(queue.getAvailableReviewsNextDayCount() + "");

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM HH:mm");

                if (queue.getAvailableReviewsCount() != 0) {
                    mNextReview.setText(R.string.card_content_reviews_available_now);

                        /*

                        I had thought that the API was returning the date when more items would
                        be added as next_review_date when you already had items available. Turns
                        out I was wrong. It gives you current date if you have items to review...
                        This feature will be implemented later in a slightly different way...

                        mMoreReviewsHolder.setVisibility(View.VISIBLE);

                        if (prefMan.isUseSpecificDates()) {
                            mMoreReviews.setText(sdf.format(nextReview));
                        } else {
                            mMoreReviews.setText(Utils.getTimeDifference(context, new Date(nextReview),
                                    Utils.getCurrentDate()));
                        }

                        */
                } else {
                    if (prefMan.isUseSpecificDates()) {
                        mNextReview.setText(sdf.format(queue.getNextReviewDate()));
                    } else {
                        mNextReview.setReferenceTime(queue.getNextReviewDate());
                    }
                }

                mListener.onReviewsCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
            } else {
                // Vacation mode is handled in DashboardFragment
                mListener.onReviewsCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
            }
        } else {
            mListener.onReviewsCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
        }
    }

    public interface ReviewsCardListener {
        public void onReviewsCardSyncFinishedListener(String result);
    }
}
