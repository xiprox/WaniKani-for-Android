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

import com.cocosw.undobar.UndoBarController;

import tr.xip.wanikani.cards.AvailableCard;
import tr.xip.wanikani.cards.CriticalItemsCard;
import tr.xip.wanikani.cards.ProgressCard;
import tr.xip.wanikani.cards.RecentUnlocksCard;
import tr.xip.wanikani.cards.ReviewsCard;
import tr.xip.wanikani.cards.StatusCard;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class DashboardFragment extends Fragment
        implements OnRefreshListener, UndoBarController.UndoListener, AvailableCard.AvailableCardListener,
        ReviewsCard.ReviewsCardListener, StatusCard.StatusCardListener, ProgressCard.ProgressCardListener,
        RecentUnlocksCard.RecentUnlocksCardListener, CriticalItemsCard.CriticalItemsCardListener {

    View rootView;

    Activity activity;

    public static final String SYNC_RESULT_SUCCESS = "success";
    public static final String SYNC_RESULT_FAILED = "failed";

    boolean isAvailableCardSynced = false;
    boolean isReviewsCardSynced = false;
    boolean isStatusCardSynced = false;
    boolean isProgressCardSynced = false;
    boolean isRecentUnlocksCardSynced = false;
    boolean isCriticalItemsCardSynced = false;

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
            showConnectionError("timeout");
        }
    };

    private BroadcastReceiver mRetrofitConnectionErorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showConnectionError("connection");
        }
    };

    private BroadcastReceiver mRetrofitUnknownErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showConnectionError("unknown");
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

        AvailableCard availableCard = new AvailableCard(this, getActivity());
        ReviewsCard reviewsCard = new ReviewsCard(this, getActivity());
        StatusCard statusCard = new StatusCard(this, getActivity());
        ProgressCard progressCard = new ProgressCard(this, getActivity());
        RecentUnlocksCard recentUnlocksCard = new RecentUnlocksCard(this, getActivity());
        CriticalItemsCard criticalItemsCard = new CriticalItemsCard(this, getActivity());

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
        if (isAvailableCardSynced && isReviewsCardSynced && isStatusCardSynced && isProgressCardSynced && isRecentUnlocksCardSynced && isCriticalItemsCardSynced)
            mPullToRefreshLayout.setRefreshComplete();
    }

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(activity).registerReceiver(mSyncCalled,
                new IntentFilter(BroadcastIntents.SYNC()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitConnectionTimeoutErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_TIMEOUT()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitConnectionErorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_CONNECTION()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitUnknownErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_UNKNOWN()));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mSyncCalled);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitConnectionTimeoutErrorReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitConnectionErorReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitUnknownErrorReceiver);
    }

    private void showConnectionError(String error) {
        if (error.equals("timeout")) {
            UndoBarController.show(activity, getString(R.string.error_connection_timeout),
                    this, UndoBarController.RETRYSTYLE);
        }

        if (error.equals("connection")) {
            UndoBarController.show(activity, getString(R.string.error_connection_error),
                    this, UndoBarController.RETRYSTYLE);
        }

        if (error.equals("unknown")) {
            UndoBarController.show(activity, getString(R.string.error_connection_error),
                    this, UndoBarController.RETRYSTYLE);
        }
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

        Intent intent = new Intent(BroadcastIntents.SYNC());
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    @Override
    public void onUndo(Parcelable parcelable) {
        Intent intent = new Intent(BroadcastIntents.SYNC());
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }

    @Override
    public void onAvailableCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            mAvailableHolder.setVisibility(View.VISIBLE);
        if (result.equals(SYNC_RESULT_FAILED))
            mAvailableHolder.setVisibility(View.GONE);

        isAvailableCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onReviewsCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            mReviewsHolder.setVisibility(View.VISIBLE);
        if (result.equals(SYNC_RESULT_FAILED))
            mReviewsHolder.setVisibility(View.GONE);

        isReviewsCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onStatusCardSyncFinishedListener() {
        isStatusCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onProgressCardSyncFinishedListener() {
        isProgressCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onRecentUnlocksCardSyncFinishedListener(int height) {
        setRecentUnlocksFragmentHeight(height);
        isRecentUnlocksCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onCriticalItemsCardSyncFinishedListener(int height) {
        setCriticalItemsFragmentHeight(height);
        isCriticalItemsCardSynced = true;
        updateSyncStatus();
    }
}