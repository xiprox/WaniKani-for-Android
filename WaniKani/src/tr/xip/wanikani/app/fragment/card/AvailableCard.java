package tr.xip.wanikani.app.fragment.card;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.app.activity.Browser;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.WebReviewActivity;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.StudyQueueGetTask;
import tr.xip.wanikani.client.task.callback.StudyQueueGetTaskCallbacks;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class AvailableCard extends Fragment implements StudyQueueGetTaskCallbacks {

    public static final int BROWSER_REQUEST = 1;

    View rootView;
    Context context;

    WaniKaniApi api;
    Utils utils;

    ImageView mLessonsGo;
    ImageView mReviewsGo;
    TextView mLessonsAvailable;
    TextView mReviewsAvailable;
    LinearLayout mCard;

    AvailableCardListener mListener;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new StudyQueueGetTask(context, AvailableCard.this).executeParallel();
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
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_available, parent, false);

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

        return rootView;
    }

    private void setUpParentOnClicks() {

        mLessonsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PrefManager(context).getWebViewIntent();
                intent.setAction(WebReviewActivity.OPEN_ACTION);
                intent.setData(Uri.parse(Browser.LESSON_URL));
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);
            }
        });

        mReviewsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PrefManager(context).getWebViewIntent();
                intent.setAction(WebReviewActivity.OPEN_ACTION);
                intent.setData(Uri.parse(Browser.REVIEW_URL));
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);
            }
        });
    }

    @Override
    public void onStudyQueueGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onStudyQueueGetTaskPostExecute(StudyQueue queue) {
        if (queue != null && queue.getUserInfo() != null) {
            if (!queue.getUserInfo().isVacationModeActive()) {
                mLessonsAvailable.setText(queue.getAvailableLesonsCount() + "");
                mReviewsAvailable.setText(queue.getAvailableReviewsCount() + "");

                mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
            } else
                // Vacation mode is handled in DashboardFragment
                mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
        } else
            mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
    }

    public interface AvailableCardListener {
        public void onAvailableCardSyncFinishedListener(String result);
    }
}
