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

import java.util.ArrayList;
import java.util.List;

import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.app.activity.CriticalItemsActivity;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.app.activity.ItemDetailsActivity;
import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.adapter.CriticalItemsAdapter;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.CriticalItemsListGetTask;
import tr.xip.wanikani.client.task.callback.CriticalItemsListGetTaskCallbacks;
import tr.xip.wanikani.utils.Fonts;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class CriticalItemsCard extends Fragment implements CriticalItemsListGetTaskCallbacks {

    View rootView;

    WaniKaniApi api;
    Utils utils;
    PrefManager prefMan;

    Context mContext;

    LinearLayout mCard;

    TextView mCardTitle;
    ListView mCriticalItemsList;

    ViewFlipper mViewFlipper;

    ViewFlipper mMessageViewFlipper;
    ImageView mMessageIcon;
    TextView mMessageTitle;
    TextView mMessageSummary;

    RelativeLayout mMoreButton;

    CriticalItemsAdapter mCriticalItemsAdapter;

    CriticalItemsCardListener mListener;

    List<CriticalItem> criticalItemsList = null;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            new CriticalItemsListGetTask(context, prefMan.getDashboardCriticalItemsPercentage(), CriticalItemsCard.this).executeParallel();
        }
    };

    public void setListener(CriticalItemsCardListener listener, Context context) {
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
        rootView = inflater.inflate(R.layout.card_critical_items, null);

        mCardTitle = (TextView) rootView.findViewById(R.id.card_critical_items_title);
        mCriticalItemsList = (ListView) rootView.findViewById(R.id.card_critical_items_list);

        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.card_critical_items_view_flipper);
        mViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mViewFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);

        mMessageViewFlipper = (ViewFlipper) rootView.findViewById(R.id.card_critical_items_connection_view_flipper);
        mMessageViewFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mMessageViewFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);

        mCard = (LinearLayout) rootView.findViewById(R.id.card_critical_items_card);

        mMessageIcon = (ImageView) rootView.findViewById(R.id.card_critical_items_message_icon);
        mMessageTitle = (TextView) rootView.findViewById(R.id.card_critical_items_message_title);
        mMessageSummary = (TextView) rootView.findViewById(R.id.card_critical_items_message_summary);

        mCriticalItemsList.setOnItemClickListener(new criticalItemListItemClickListener());

        mMoreButton = (RelativeLayout) rootView.findViewById(R.id.card_critical_items_more_button);
        mMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CriticalItemsActivity.class));
            }
        });

        return rootView;
    }

    public int setCriticalItemsHeightBasedOnListView(ListView listView) {
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
            totalHeight += pxFromDp(32); // Add the paddings as well

            return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
    }

    private float pxFromDp(float dp) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    @Override
    public void onCriticalItemsListGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onCriticalItemsListGetTaskPostExecute(List<CriticalItem> list) {
        int height;

        if (list != null) {
            List<CriticalItem> mNewList = new ArrayList<CriticalItem>();

            for (int i = 0; i < list.size(); i++)
                if (i < prefMan.getCriticalItemsNumber())
                    mNewList.add(list.get(i));

            criticalItemsList = mNewList;

            mCriticalItemsAdapter = new CriticalItemsAdapter(mContext,
                    R.layout.item_critical, mNewList, new Fonts().getKanjiFont(mContext));

            int sixteenDpPaddingInPx = (int) pxFromDp(16);
            int sixtyFourDpInPx = (int) pxFromDp(64);

            if (mNewList.size() < list.size()) {
                mCriticalItemsList.setPadding(
                        sixteenDpPaddingInPx,
                        sixteenDpPaddingInPx,
                        sixteenDpPaddingInPx,
                        sixtyFourDpInPx
                );
                mMoreButton.setVisibility(View.VISIBLE);
            } else {
                mCriticalItemsList.setPadding(sixteenDpPaddingInPx,
                        sixteenDpPaddingInPx,
                        sixteenDpPaddingInPx,
                        sixteenDpPaddingInPx
                );
                mMoreButton.setVisibility(View.GONE);
            }

            if (mCriticalItemsAdapter.getCount() != 0) {
                mCriticalItemsList.setAdapter(mCriticalItemsAdapter);

                if (mMessageViewFlipper.getDisplayedChild() == 1) {
                    mMessageViewFlipper.showPrevious();
                }

                height = setCriticalItemsHeightBasedOnListView(mCriticalItemsList);
            } else {
                mMessageIcon.setImageResource(R.drawable.ic_thumb_up_black_36dp);
                mMessageTitle.setText(R.string.card_content_critical_no_items_title);
                mMessageSummary.setText(R.string.card_content_critical_no_items_summary);

                if (mMessageViewFlipper.getDisplayedChild() == 0) {
                    mMessageViewFlipper.showNext();
                }

                height = (int) pxFromDp(168);
            }

            mListener.onCriticalItemsCardSyncFinishedListener(height, DashboardFragment.SYNC_RESULT_SUCCESS);
        } else {
            mMessageIcon.setImageResource(R.drawable.ic_error_red_36dp);
            mMessageTitle.setText(R.string.error_oops);
            mMessageSummary.setText(R.string.error_display_items);

            if (mMessageViewFlipper.getDisplayedChild() == 0) {
                mMessageViewFlipper.showNext();
            }

            height = (int) pxFromDp(158);

            mListener.onCriticalItemsCardSyncFinishedListener(height, DashboardFragment.SYNC_RESULT_FAILED);
        }

        if (mViewFlipper.getDisplayedChild() == 0) {
            mViewFlipper.showNext();
        }
    }

    public interface CriticalItemsCardListener {
        public void onCriticalItemsCardSyncFinishedListener(int height, String result);
    }

    private class criticalItemListItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            CriticalItem item = criticalItemsList.get(position);

            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, item);
            getActivity().startActivity(intent);
        }
    }
}
