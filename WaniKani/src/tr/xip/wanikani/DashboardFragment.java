package tr.xip.wanikani;

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

import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class DashboardFragment extends Fragment
        implements OnRefreshListener {

    View rootView;

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
            if(intent.getStringExtra("action").equals("hide")) {
                mAvailableHolder.setVisibility(View.GONE);
            }
            if(intent.getStringExtra("action").equals("show")) {
                mAvailableHolder.setVisibility(View.VISIBLE);
            }

            isAvailableCardSynced = true;
            updateSyncStatus();
        }
    };

    private BroadcastReceiver mReviewsCardSyncFinishedReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("action").equals("hide")) {
                mReviewsHolder.setVisibility(View.GONE);
            }
            if(intent.getStringExtra("action").equals("show")) {
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

        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.dashboard_pull_to_refresh);

        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        mAvailableHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_available_holder);
        mReviewsHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_reviews_holder);
        mRecentUnlocksFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_recent_unlocks_holder);
        mCriticalItemsFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_critical_items_holder);

        mPullToRefreshLayout.setRefreshing(true);
        Intent intent = new Intent(BroadcastIntents.SYNC());
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

        return rootView;
    }

    private void updateSyncStatus() {
        if (isAvailableCardSynced && isReviewsCardSynced && isStatusCardSynced && isProgressCardSynced && isRecentUnlocksCardSynced && isCriticalItemsCardSynced)
            mPullToRefreshLayout.setRefreshComplete();
    }

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSyncCalled,
                new IntentFilter(BroadcastIntents.SYNC()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mAvailableCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_AVAILABLE_CARD()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReviewsCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_REVIEWS_CARD()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mStatusCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_STATUS_CARD()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mProgressCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_PROGRESS_CARD()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRecentUnlocksCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_RECENT_UNLOCKS_CARD()));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mCriticalItemsCardSyncFinishedReceiver,
                new IntentFilter(BroadcastIntents.FINISHED_SYNC_CRITICAL_ITEMS_CARD()));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mSyncCalled);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mAvailableCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReviewsCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mStatusCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mProgressCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRecentUnlocksCardSyncFinishedReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mCriticalItemsCardSyncFinishedReceiver);
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
}