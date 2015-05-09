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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.app.activity.ItemDetailsActivity;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.RecentUnlocksActivity;
import tr.xip.wanikani.widget.adapter.RecentUnlocksArrayAdapter;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.RecentUnlocksListGetTask;
import tr.xip.wanikani.client.task.callback.RecentUnlocksListGetTaskCallbacks;
import tr.xip.wanikani.utils.Fonts;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class RecentUnlocksCard extends Fragment implements RecentUnlocksListGetTaskCallbacks {

    View rootView;

    WaniKaniApi api;
    Utils utils;
    PrefManager prefMan;

    Context mContext;

    TextView mCardTitle;
    ListView mRecentUnlocksList;
    RelativeLayout mMoreItemsButton;
    ViewFlipper mViewFlipper;
    ViewFlipper mMessageViewFlipper;
    LinearLayout mCard;
    ImageView mMessageIcon;
    TextView mMessageTitle;
    TextView mMessageSummary;

    RecentUnlocksCardListener mListener;

    List<UnlockItem> recentUnlocksList = null;

    RecentUnlocksArrayAdapter mRecentUnlocksAdapter;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            new RecentUnlocksListGetTask(context, prefMan.getDashboardRecentUnlocksNumber(),
                    RecentUnlocksCard.this).executeParallel();
        }
    };

    public void setListener(RecentUnlocksCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

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

    @Override
    public void onRecentUnlocksListGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onRecentUnlocksListGetTaskPostExecute(List<UnlockItem> list) {
        int height;

        if (list != null) {
            recentUnlocksList = list;

            mRecentUnlocksAdapter = new RecentUnlocksArrayAdapter(mContext,
                    R.layout.item_recent_unlock, list, new Fonts().getKanjiFont(mContext));

            if (mRecentUnlocksAdapter.getCount() != 0) {
                mRecentUnlocksList.setAdapter(mRecentUnlocksAdapter);

                if (mMessageViewFlipper.getDisplayedChild() == 1) {
                    mMessageViewFlipper.showPrevious();
                }

                height = setRecentUnlocksHeightBasedOnListView(mRecentUnlocksList);
            } else {
                mMessageIcon.setImageResource(R.drawable.ic_folder_open_black_36dp);
                mMessageTitle.setText(R.string.card_content_unlocks_no_items_title);
                mMessageSummary.setText(R.string.card_content_unlocks_no_items_summary);

                if (mMessageViewFlipper.getDisplayedChild() == 0) {
                    mMessageViewFlipper.showNext();
                }

                height = (int) pxFromDp(158);
            }

            mListener.onRecentUnlocksCardSyncFinishedListener(height, DashboardFragment.SYNC_RESULT_SUCCESS);
        } else {
            mMessageIcon.setImageResource(R.drawable.ic_error_red_36dp);
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

    public interface RecentUnlocksCardListener {
        public void onRecentUnlocksCardSyncFinishedListener(int height, String result);
    }

    private class recentUnlocksListItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            UnlockItem item = recentUnlocksList.get(position);

            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, item);
            getActivity().startActivity(intent);
        }
    }
}
