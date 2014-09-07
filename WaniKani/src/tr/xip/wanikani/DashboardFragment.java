package tr.xip.wanikani;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.cards.AvailableCard;
import tr.xip.wanikani.cards.CriticalItemsCard;
import tr.xip.wanikani.cards.MessageCard;
import tr.xip.wanikani.cards.ProgressCard;
import tr.xip.wanikani.cards.RecentUnlocksCard;
import tr.xip.wanikani.cards.ReviewsCard;
import tr.xip.wanikani.cards.SRSCard;
import tr.xip.wanikani.cards.VacationModeCard;
import tr.xip.wanikani.managers.PrefManager;

public class DashboardFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, AvailableCard.AvailableCardListener, ReviewsCard.ReviewsCardListener,
        SRSCard.StatusCardListener, ProgressCard.ProgressCardListener, RecentUnlocksCard.RecentUnlocksCardListener,
        CriticalItemsCard.CriticalItemsCardListener, MessageCard.MessageCardListener, View.OnClickListener {

    public static final String SYNC_RESULT_SUCCESS = "success";
    public static final String SYNC_RESULT_FAILED = "failed";

    View rootView;
    Activity activity;
    PrefManager prefMan;
    WaniKaniApi api;

    boolean isAvailableCardSynced = false;
    boolean isReviewsCardSynced = false;
    boolean isStatusCardSynced = false;
    boolean isProgressCardSynced = false;
    boolean isRecentUnlocksCardSynced = false;
    boolean isCriticalItemsCardSynced = false;

    boolean isAvailableCardSyncedSuccess = false;
    boolean isReviewsCardSyncedSuccess = false;
    boolean isStatusCardSyncedSuccess = false;
    boolean isProgressCardSyncedSuccess = false;
    boolean isRecentUnlocksCardSyncedSuccess = false;
    boolean isCriticalItemsCardSyncedSuccess = false;

    LinearLayout mAvailableHolder;
    LinearLayout mReviewsHolder;
    LinearLayout mCriticalItemsFragmentHolder;
    LinearLayout mRecentUnlocksFragmentHolder;

    FrameLayout mVacationModeCard;
    FrameLayout mReviewsCard;
    FrameLayout mProgressCard;
    private SwipeRefreshLayout mSwipeToRefreshLayout;
    private BroadcastReceiver mSyncCalled = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSwipeToRefreshLayout.setRefreshing(true);
        }
    };
    private BroadcastReceiver mRetrofitConnectionTimeoutErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(MESSAGE_TYPE.ERROR_CONNECTION_TIMEOUT);
        }
    };
    private BroadcastReceiver mRetrofitConnectionErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(MESSAGE_TYPE.ERROR_NO_CONNECTION);
        }
    };
    private BroadcastReceiver mRetrofitUnknownErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(MESSAGE_TYPE.ERROR_UNKNOWN);
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
    public void onCreate(Bundle paramBundle) {
        api = new WaniKaniApi(getActivity());
        prefMan = new PrefManager(getActivity());
        super.onCreate(paramBundle);
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {

        rootView = paramLayoutInflater.inflate(R.layout.fragment_dashboard, paramViewGroup, false);

        activity = getActivity();

        mSwipeToRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.dashboard_swipe_refresh);
        mSwipeToRefreshLayout.setOnRefreshListener(this);
        mSwipeToRefreshLayout.setColorScheme(R.color.swipe_refresh_1, R.color.swipe_refresh_2,
                R.color.swipe_refresh_3, R.color.swipe_refresh_4);

        mAvailableHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_available_holder);
        mReviewsHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_reviews_holder);
        mRecentUnlocksFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_recent_unlocks_holder);
        mCriticalItemsFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_critical_items_holder);

        mVacationModeCard = (FrameLayout) rootView.findViewById(R.id.fragment_dashboard_vacation_mode_card);
        mReviewsCard = (FrameLayout) rootView.findViewById(R.id.fragment_dashboard_reviews_card);
        mProgressCard = (FrameLayout) rootView.findViewById(R.id.fragment_dashboard_progress_card);

        mReviewsCard.setOnClickListener(this);
        mProgressCard.setOnClickListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        VacationModeCard vacationModeCard = new VacationModeCard();
        AvailableCard availableCard = new AvailableCard();
        ReviewsCard reviewsCard = new ReviewsCard();
        SRSCard statusCard = new SRSCard();
        ProgressCard progressCard = new ProgressCard();
        RecentUnlocksCard recentUnlocksCard = new RecentUnlocksCard();
        CriticalItemsCard criticalItemsCard = new CriticalItemsCard();

        availableCard.setListener(this, getActivity());
        reviewsCard.setListener(this, getActivity());
        statusCard.setListener(this, getActivity());
        progressCard.setListener(this, getActivity());
        recentUnlocksCard.setListener(this, getActivity());
        criticalItemsCard.setListener(this, getActivity());

        transaction.replace(R.id.fragment_dashboard_vacation_mode_card, vacationModeCard);
        transaction.replace(R.id.fragment_dashboard_available_card, availableCard);
        transaction.replace(R.id.fragment_dashboard_reviews_card, reviewsCard);
        transaction.replace(R.id.fragment_dashboard_status_card, statusCard);
        transaction.replace(R.id.fragment_dashboard_progress_card, progressCard);
        transaction.replace(R.id.fragment_dashboard_recent_unlocks_card, recentUnlocksCard);
        transaction.replace(R.id.fragment_dashboard_critical_items_card, criticalItemsCard);
        transaction.commit();

        if (!MainActivity.isFirstSyncDashboardDone) {
            mSwipeToRefreshLayout.setRefreshing(true);
            Intent intent = new Intent(BroadcastIntents.SYNC());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            new VacationModeCheckTask().execute();
            MainActivity.isFirstSyncDashboardDone = true;
        } else {
            Intent intent = new Intent(BroadcastIntents.SYNC());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            new VacationModeCheckTask().execute();
        }

        return rootView;

    }

    private void updateSyncStatus() {
        if (isAvailableCardSynced && isReviewsCardSynced && isStatusCardSynced && isProgressCardSynced && isRecentUnlocksCardSynced && isCriticalItemsCardSynced) {
            mSwipeToRefreshLayout.setRefreshing(false);

            if (isAvailableCardSyncedSuccess && isReviewsCardSyncedSuccess && isStatusCardSyncedSuccess && isRecentUnlocksCardSyncedSuccess
                    && isCriticalItemsCardSyncedSuccess) {
                prefMan.setDashboardLastUpdateDate(System.currentTimeMillis());
                onMessageCardOkButtonClick();
            }
        }
    }

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(activity).registerReceiver(mSyncCalled,
                new IntentFilter(BroadcastIntents.SYNC()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitConnectionTimeoutErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_TIMEOUT()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitConnectionErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_CONNECTION()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitUnknownErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_UNKNOWN()));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mSyncCalled);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitConnectionTimeoutErrorReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitConnectionErrorReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitUnknownErrorReceiver);
    }

    private void showMessage(MESSAGE_TYPE msgType) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        MessageCard fragment = new MessageCard();
        fragment.setListener(this);

        String title = "";
        String prefix = "";

        if (msgType == MESSAGE_TYPE.ERROR_CONNECTION_TIMEOUT) {
            title = getString(R.string.error_connection_timeout);
            prefix = getString(R.string.content_last_updated) + " ";
        }

        if (msgType == MESSAGE_TYPE.ERROR_NO_CONNECTION) {
            title = getString(R.string.error_no_connection);
            prefix = getString(R.string.content_last_updated) + " ";
        }

        if (msgType == MESSAGE_TYPE.ERROR_UNKNOWN) {
            title = getString(R.string.error_unknown_error);
            prefix = getString(R.string.content_last_updated) + " ";
        }

        Bundle args = new Bundle();
        args.putString(MessageCard.ARG_TITLE, title);
        args.putString(MessageCard.ARG_PREFIX, prefix);
        args.putLong(MessageCard.ARG_TIME, prefMan.getDashboardLastUpdateTime());
        fragment.setArguments(args);

        transaction.replace(R.id.fragment_dashboard_message_card, fragment).commit();

        rootView.findViewById(R.id.fragment_dashboard_message_card).setVisibility(View.VISIBLE);
    }

    private void setCriticalItemsFragmentHeight(int height) {
        ViewGroup.LayoutParams params = mCriticalItemsFragmentHolder.getLayoutParams();
        params.height = height;
        mCriticalItemsFragmentHolder.setLayoutParams(params);
    }

    private void setRecentUnlocksFragmentHeight(int height) {
        ViewGroup.LayoutParams params = mRecentUnlocksFragmentHolder.getLayoutParams();
        params.height = height;
        mRecentUnlocksFragmentHolder.setLayoutParams(params);
    }

    @Override
    public void onAvailableCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isAvailableCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isAvailableCardSyncedSuccess = false;

        isAvailableCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onReviewsCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isReviewsCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isReviewsCardSyncedSuccess = false;

        isReviewsCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onStatusCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isStatusCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isStatusCardSyncedSuccess = false;

        isStatusCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onProgressCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isProgressCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isProgressCardSyncedSuccess = false;

        isProgressCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onRecentUnlocksCardSyncFinishedListener(int height, String result) {
        if (result.equals(SYNC_RESULT_SUCCESS)) {
            isRecentUnlocksCardSyncedSuccess = true;
        }

        if (result.equals(SYNC_RESULT_FAILED)) {
            isRecentUnlocksCardSyncedSuccess = false;
        }

        setRecentUnlocksFragmentHeight(height);
        isRecentUnlocksCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onCriticalItemsCardSyncFinishedListener(int height, String result) {
        if (result.equals(SYNC_RESULT_SUCCESS)) {
            isCriticalItemsCardSyncedSuccess = true;
        }

        if (result.equals(SYNC_RESULT_FAILED)) {
            isCriticalItemsCardSyncedSuccess = false;
        }

        setCriticalItemsFragmentHeight(height);
        isCriticalItemsCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onMessageCardOkButtonClick() {
        rootView.findViewById(R.id.fragment_dashboard_message_card).setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        isAvailableCardSynced = false;
        isReviewsCardSynced = false;
        isStatusCardSynced = false;
        isProgressCardSynced = false;
        isRecentUnlocksCardSynced = false;
        isCriticalItemsCardSynced = false;

        isAvailableCardSyncedSuccess = false;
        isReviewsCardSyncedSuccess = false;
        isStatusCardSyncedSuccess = false;
        isProgressCardSyncedSuccess = false;
        isRecentUnlocksCardSyncedSuccess = false;
        isCriticalItemsCardSyncedSuccess = false;

        Intent intent = new Intent(BroadcastIntents.SYNC());
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        new VacationModeCheckTask().execute();
    }

    @Override
    public void onClick(View view) {
        if (view == mReviewsCard) {
            // TODO - Handle reviews card stuff
        }
        if (view == mProgressCard) {
            // TODO - Handle progress card stuff
        }
    }

    enum MESSAGE_TYPE {
        ERROR_CONNECTION_TIMEOUT,
        ERROR_NO_CONNECTION,
        ERROR_UNKNOWN
    }

    private class VacationModeCheckTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                return api.getUser().getVacationDate() != 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isVacationModeActive) {
            super.onPostExecute(isVacationModeActive);

            if (isVacationModeActive) {
                mAvailableHolder.setVisibility(View.GONE);
                mReviewsHolder.setVisibility(View.GONE);
                mVacationModeCard.setVisibility(View.VISIBLE);
            } else {
                mAvailableHolder.setVisibility(View.VISIBLE);
                mReviewsHolder.setVisibility(View.VISIBLE);
                mVacationModeCard.setVisibility(View.GONE);
            }
        }
    }
}