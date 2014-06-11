package tr.xip.wanikani;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import tr.xip.wanikani.api.error.RetrofitErrorHandler;
import tr.xip.wanikani.cards.AvailableCard;
import tr.xip.wanikani.cards.CriticalItemsCard;
import tr.xip.wanikani.cards.MessageCard;
import tr.xip.wanikani.cards.ProgressCard;
import tr.xip.wanikani.cards.RecentUnlocksCard;
import tr.xip.wanikani.cards.ReviewsCard;
import tr.xip.wanikani.cards.StatusCard;
import tr.xip.wanikani.managers.PrefManager;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class DashboardFragment extends Fragment
        implements OnRefreshListener, AvailableCard.AvailableCardListener, ReviewsCard.ReviewsCardListener,
        StatusCard.StatusCardListener, ProgressCard.ProgressCardListener, RecentUnlocksCard.RecentUnlocksCardListener,
        CriticalItemsCard.CriticalItemsCardListener, MessageCard.MessageCardListener {

    View rootView;

    Activity activity;

    PrefManager prefMan;

    public static final String SYNC_RESULT_SUCCESS = "success";
    public static final String SYNC_RESULT_FAILED = "failed";

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

    private PullToRefreshLayout mPullToRefreshLayout;

    private BroadcastReceiver mSyncCalled = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPullToRefreshLayout.setRefreshing(true);
        }
    };

    private BroadcastReceiver mRetrofitConnectionTimeoutErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(getString(R.string.error_connection_timeout), getString(R.string.content_last_updated)
                    + " " + prefMan.getDashboardLastUpdateTime(), false, getString(R.string.ok));
        }
    };

    private BroadcastReceiver mRetrofitConnectionErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(getString(R.string.error_connection_error), getString(R.string.content_last_updated)
                    + " " + prefMan.getDashboardLastUpdateTime(), false, getString(R.string.ok));
        }
    };

    private BroadcastReceiver mRetrofitUnknownErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(getString(R.string.error_unknown_error), getString(R.string.content_last_updated)
                    + " " + prefMan.getDashboardLastUpdateTime(), false, getString(R.string.ok));
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
        prefMan = new PrefManager(getActivity());
        super.onCreate(paramBundle);
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {

        rootView = paramLayoutInflater.inflate(R.layout.fragment_dashboard, paramViewGroup, false);

        activity = getActivity();

        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.dashboard_pull_to_refresh);

        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        mAvailableHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_available_holder);
        mReviewsHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_reviews_holder);
        mRecentUnlocksFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_recent_unlocks_holder);
        mCriticalItemsFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_critical_items_holder);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        AvailableCard availableCard = new AvailableCard();
        ReviewsCard reviewsCard = new ReviewsCard();
        StatusCard statusCard = new StatusCard();
        ProgressCard progressCard = new ProgressCard();
        RecentUnlocksCard recentUnlocksCard = new RecentUnlocksCard();
        CriticalItemsCard criticalItemsCard = new CriticalItemsCard();

        availableCard.setListener(this, getActivity());
        reviewsCard.setListener(this, getActivity());
        statusCard.setListener(this, getActivity());
        progressCard.setListener(this, getActivity());
        recentUnlocksCard.setListener(this, getActivity());
        criticalItemsCard.setListener(this, getActivity());

        transaction.replace(R.id.fragment_dashboard_available_card, availableCard);
        transaction.replace(R.id.fragment_dashboard_reviews_card, reviewsCard);
        transaction.replace(R.id.fragment_dashboard_status_card, statusCard);
        transaction.replace(R.id.fragment_dashboard_progress_card, progressCard);
        transaction.replace(R.id.fragment_dashboard_recent_unlocks_card, recentUnlocksCard);
        transaction.replace(R.id.fragment_dashboard_critical_items_card, criticalItemsCard);
        transaction.commit();

        if (!MainActivity.isFirstSyncDashboardDone) {
            mPullToRefreshLayout.setRefreshing(true);
            Intent intent = new Intent(BroadcastIntents.SYNC());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            MainActivity.isFirstSyncDashboardDone = true;
        } else {
            Intent intent = new Intent(BroadcastIntents.SYNC());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }

        return rootView;

    }

    private void updateSyncStatus() {
        if (isAvailableCardSynced && isReviewsCardSynced && isStatusCardSynced && isProgressCardSynced && isRecentUnlocksCardSynced && isCriticalItemsCardSynced) {
            mPullToRefreshLayout.setRefreshComplete();

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

    private void showMessage(String title, String summary, boolean hideOkButton, String buttonText) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        MessageCard fragment = new MessageCard();
        fragment.setListener(this);

        Bundle args = new Bundle();
        args.putString(MessageCard.ARG_TITLE, title);
        args.putString(MessageCard.ARG_SUMMARY, summary);
        args.putBoolean(MessageCard.ARG_HIDE_OK_BUTTON, hideOkButton);
        args.putString(MessageCard.ARG_BUTTON_TEXT, buttonText);
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
    public void onRefreshStarted(View paramView) {
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
    }

    @Override
    public void onAvailableCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS)) {
            mAvailableHolder.setVisibility(View.VISIBLE);
            isAvailableCardSyncedSuccess = true;
        }

        if (result.equals(SYNC_RESULT_FAILED)) {
            mAvailableHolder.setVisibility(View.GONE);
            isAvailableCardSyncedSuccess = false;
        }

        isAvailableCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onReviewsCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS)) {
            mReviewsHolder.setVisibility(View.VISIBLE);
            isReviewsCardSyncedSuccess = true;
        }

        if (result.equals(SYNC_RESULT_FAILED)) {
            mReviewsHolder.setVisibility(View.GONE);
            isReviewsCardSyncedSuccess = false;
        }

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
            mRecentUnlocksFragmentHolder.setVisibility(View.VISIBLE);
        }

            if (result.equals(SYNC_RESULT_FAILED)) {
            isRecentUnlocksCardSyncedSuccess = false;
            mRecentUnlocksFragmentHolder.setVisibility(View.GONE);
        }

        setRecentUnlocksFragmentHeight(height);
        isRecentUnlocksCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onCriticalItemsCardSyncFinishedListener(int height, String result) {
        if (result.equals(SYNC_RESULT_SUCCESS)) {
            isCriticalItemsCardSyncedSuccess = true;
            mCriticalItemsFragmentHolder.setVisibility(View.VISIBLE);
        }

        if (result.equals(SYNC_RESULT_FAILED)) {
            isCriticalItemsCardSyncedSuccess = false;
            mCriticalItemsFragmentHolder.setVisibility(View.GONE);
        }

        setCriticalItemsFragmentHeight(height);
        isCriticalItemsCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onMessageCardOkButtonClick() {
        rootView.findViewById(R.id.fragment_dashboard_message_card).setVisibility(View.GONE);
    }
}