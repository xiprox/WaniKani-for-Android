package tr.xip.wanikani.cards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import tr.xip.wanikani.BroadcastIntents;
import tr.xip.wanikani.DashboardFragment;
import tr.xip.wanikani.ItemDetailsActivity;
import tr.xip.wanikani.R;
import tr.xip.wanikani.RecentUnlocksActivity;
import tr.xip.wanikani.adapters.RecentUnlocksArrayAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.RecentUnlocksList;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.Fonts;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class RecentUnlocksCard extends Fragment {

    View rootView;

    WaniKaniApi api;
    Utils utils;
    PrefManager prefMan;

    Context mContext;

    RecentUnlocksCardListener mListener;

    TextView mCardTitle;
    ListView mRecentUnlocksList;
    RelativeLayout mMoreItemsButton;

    RecentUnlocksArrayAdapter mRecentUnlocksAdapter;

    ViewFlipper mViewFlipper;
    ViewFlipper mMessageViewFlipper;

    LinearLayout mCard;

    ImageView mMessageIcon;
    TextView mMessageTitle;
    TextView mMessageSummary;

    List<RecentUnlocksList.UnlockItem> recentUnlocksList = null;

    public void setListener(RecentUnlocksCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            if (Build.VERSION.SDK_INT >= 11)
                new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new LoadTask().execute();
        }
    };

    @Override
    public void onCreate(Bundle state) {
        api = new WaniKaniApi(getActivity());
        utils = new Utils(getActivity());
        prefMan = new PrefManager(getActivity());
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_recent_unlocks, null);

        mCardTitle = (TextView) rootView.findViewById(R.id.card_recent_unlocks_title);
        mRecentUnlocksList = (ListView) rootView.findViewById(R.id.card_recent_unlocks_list);

        mMoreItemsButton = (RelativeLayout) rootView.findViewById(R.id.card_recent_unlocks_more_button);

        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.card_recent_unlocks_view_flipper);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mViewFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);

        mMessageViewFlipper = (ViewFlipper) rootView.findViewById(R.id.card_recent_unlocks_connection_view_flipper);
        mMessageViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mMessageViewFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);

        mRecentUnlocksList.setOnItemClickListener(new recentUnlocksListItemClickListener());

        mCard = (LinearLayout) rootView.findViewById(R.id.card_recent_unlocks_card);

        mMessageIcon = (ImageView) rootView.findViewById(R.id.card_recent_unlocks_message_icon);
        mMessageTitle = (TextView) rootView.findViewById(R.id.card_recent_unlocks_message_title);
        mMessageSummary = (TextView) rootView.findViewById(R.id.card_recent_unlocks_message_summary);

        mMoreItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, RecentUnlocksActivity.class));
            }
        });

        return rootView;
    }

    public int setRecentUnlocksHeightBasedOnListView(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return (int) pxFromDp(550);
        } else {

            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            totalHeight += mCardTitle.getMeasuredHeight();
            totalHeight += pxFromDp(16); // Add the paddings as well
            totalHeight += pxFromDp(48); // Add the more items button

            return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
    }

    private float pxFromDp(float dp) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    private class LoadTask extends AsyncTask<String, Void, List<RecentUnlocksList.UnlockItem>> {

        @Override
        protected List<RecentUnlocksList.UnlockItem> doInBackground(String... strings) {
            try {
                recentUnlocksList = api.getRecentUnlocksList(prefMan.getDashboardRecentUnlocksNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return recentUnlocksList;
        }

        @Override
        protected void onPostExecute(List<RecentUnlocksList.UnlockItem> result) {
            int height;

            if (result != null) {
                mRecentUnlocksAdapter = new RecentUnlocksArrayAdapter(mContext,
                        R.layout.item_recent_unlock, result, new Fonts().getKanjiFont(mContext));

                if (mRecentUnlocksAdapter.getCount() != 0) {
                    mRecentUnlocksList.setAdapter(mRecentUnlocksAdapter);

                    if (mMessageViewFlipper.getDisplayedChild() == 1) {
                        mMessageViewFlipper.showPrevious();
                    }

                    height = setRecentUnlocksHeightBasedOnListView(mRecentUnlocksList);
                } else {
                    mMessageIcon.setImageResource(R.drawable.ic_review_box);
                    mMessageTitle.setText(R.string.card_content_unlocks_no_items_title);
                    mMessageSummary.setText(R.string.card_content_unlocks_no_items_summary);

                    if (mMessageViewFlipper.getDisplayedChild() == 0) {
                        mMessageViewFlipper.showNext();
                    }

                    height = (int) pxFromDp(158);
                }

                mListener.onRecentUnlocksCardSyncFinishedListener(height, DashboardFragment.SYNC_RESULT_SUCCESS);
            } else {
                mMessageIcon.setImageResource(R.drawable.ic_action_warning);
                mMessageTitle.setText(R.string.error_oops);
                mMessageSummary.setText(R.string.error_display_items);

                if (mMessageViewFlipper.getDisplayedChild() == 0) {
                    mMessageViewFlipper.showNext();
                }

                height = (int) pxFromDp(158);

                mListener.onRecentUnlocksCardSyncFinishedListener(height, DashboardFragment.SYNC_RESULT_FAILED);
            }

            if (mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.showNext();
            }
        }
    }

    private class recentUnlocksListItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            RecentUnlocksList.UnlockItem item = recentUnlocksList.get(position);

            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_TYPE, item.getType());
            intent.putExtra(ItemDetailsActivity.ARG_CHARACTER, item.getCharacter());
            intent.putExtra(ItemDetailsActivity.ARG_IMAGE, item.getImage());
            intent.putExtra(ItemDetailsActivity.ARG_LEVEL, item.getLevel());
            getActivity().startActivity(intent);
        }
    }

    public interface RecentUnlocksCardListener {
        public void onRecentUnlocksCardSyncFinishedListener(int height, String result);
    }
}
