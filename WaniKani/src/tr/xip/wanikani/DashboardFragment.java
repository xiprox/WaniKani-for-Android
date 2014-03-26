package tr.xip.wanikani;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController;

import tr.xip.wanikani.cards.AvailableCard;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class DashboardFragment extends Fragment
        implements OnRefreshListener, UndoBarController.UndoListener {

    View rootView;

    Activity activity;

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

    private BroadcastReceiver mAvailableCardSyncFinishedReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("action").equals("hide")) {
                mAvailableHolder.setVisibility(View.GONE);
            }
            if (intent.getStringExtra("action").equals("show")) {
                mAvailableHolder.setVisibility(View.VISIBLE);
            }

            isAvailableCardSynced = true;
            updateSyncStatus();
        }
    };

    private BroadcastReceiver mReviewsCardSyncFinishedReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("action").equals("hide")) {
                mReviewsHolder.setVisibility(View.GONE);
            }
            if (intent.getStringExtra("action").equals("show")) {
                mReviewsHolder.setVisibility(View.VISIBLE);
            }

            isReviewsCardSynced = true;
            updateSyncStatus();
        }
    };

    private BroadcastReceiver mStatusCardSyncFinishedReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            isStatusCardSynced = true;
            updateSyncStatus();
        }
    };

    private BroadcastReceiver mProgressCardSyncFinishedReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            isProgressCardSynced = true;
            updateSyncStatus();
        }
    };

    private BroadcastReceiver mRecentUnlocksCardSyncFinishedReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            setRecentUnlocksFragmentHeight(intent.getIntExtra("height", 1010));
            isRecentUnlocksCardSynced = true;
            updateSyncStatus();
        }
    };

    private BroadcastReceiver mCriticalItemsCardSyncFinishedReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            setCriticalItemsFragmentHeight(intent.getIntExtra("height", 1010));
            isCriticalItemsCardSynced = true;
            updateSyncStatus();
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
        LocalBroadcastManager.getInstance(activity).registerReceiver(mAvailableCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_AVAILABLE_CARD()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mReviewsCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_REVIEWS_CARD()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mStatusCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_STATUS_CARD()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mProgressCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_PROGRESS_CARD()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRecentUnlocksCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_RECENT_UNLOCKS_CARD()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mCriticalItemsCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_CRITICAL_ITEMS_CARD()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitConnectionTimeoutErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_TIMEOUT()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitConnectionErorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_CONNECTION()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitUnknownErrorReceiver,
                new IntentFilter(BroadcastIntents.RETROFIT_ERROR_UNKNOWN()));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mSyncCalled);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mAvailableCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReviewsCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mStatusCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mProgressCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRecentUnlocksCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mCriticalItemsCardSyncFinishedReceiver);
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
}