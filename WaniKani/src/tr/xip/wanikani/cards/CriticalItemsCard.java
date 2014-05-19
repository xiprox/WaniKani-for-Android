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
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import tr.xip.wanikani.BroadcastIntents;
import tr.xip.wanikani.ItemDetailsActivity;
import tr.xip.wanikani.R;
import tr.xip.wanikani.adapters.CriticalItemsAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.CriticalItemsList;
import tr.xip.wanikani.api.response.RecentUnlocksList;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.utils.Fonts;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class CriticalItemsCard extends Fragment {

    View rootView;

    WaniKaniApi api;
    Utils utils;
    PrefManager prefMan;
    ThemeManager themeMan;

    Context mContext;

    TextView mCardTitle;
    ListView mCriticalItemsList;

    CriticalItemsAdapter mCriticalItemsAdapter;

    ViewFlipper mViewFlipper;
    ViewFlipper mMessageViewFlipper;

    LinearLayout mCard;

    ImageView mMessageIcon;
    TextView mMessageTitle;
    TextView mMessageSummary;

    List<CriticalItemsList.CriticalItem> criticalItemsList = null;

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
        themeMan = new ThemeManager(getActivity());
        super.onCreate(state);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
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
        mCard.setBackgroundResource(themeMan.getCard());

        mMessageIcon = (ImageView) rootView.findViewById(R.id.card_critical_items_message_icon);
        mMessageTitle = (TextView) rootView.findViewById(R.id.card_critical_items_message_title);
        mMessageSummary = (TextView) rootView.findViewById(R.id.card_critical_items_message_summary);

        mCriticalItemsList.setOnItemClickListener(new criticalItemListItemClickListener());

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
            totalHeight += pxFromDp(16); // Add the paddings as well

            return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
    }

    private float pxFromDp(float dp) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    private class LoadTask extends AsyncTask<String, Void, List<CriticalItemsList.CriticalItem>> {

        @Override
        protected List<CriticalItemsList.CriticalItem> doInBackground(String... strings) {
            try {
                criticalItemsList = api.getCriticalItemsList(prefMan.getDashboardCriticalItemsPercentage());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return criticalItemsList;
        }

        @Override
        protected void onPostExecute(List<CriticalItemsList.CriticalItem> result) {
            int height;

            if (result != null) {
                mCriticalItemsAdapter = new CriticalItemsAdapter(mContext,
                        R.layout.item_critical, result, new Fonts().getKanjiFont(mContext));

                if (mCriticalItemsAdapter.getCount() != 0) {
                    mCriticalItemsList.setAdapter(mCriticalItemsAdapter);

                    if (mMessageViewFlipper.getDisplayedChild() == 1) {
                        mMessageViewFlipper.showPrevious();
                    }

                    height = setCriticalItemsHeightBasedOnListView(mCriticalItemsList);
                } else {
                    mMessageIcon.setImageResource(R.drawable.ic_action_good);
                    mMessageTitle.setText(R.string.card_content_critical_no_items_title);
                    mMessageSummary.setText(R.string.card_content_critical_no_items_summary);

                    if (mMessageViewFlipper.getDisplayedChild() == 0) {
                        mMessageViewFlipper.showNext();
                    }

                    height = (int) pxFromDp(158);
                }
            } else {
                mMessageIcon.setImageResource(R.drawable.ic_action_warning);
                mMessageTitle.setText(R.string.error_oops);
                mMessageSummary.setText(R.string.error_display_items);

                if (mMessageViewFlipper.getDisplayedChild() == 0) {
                    mMessageViewFlipper.showNext();
                }

                height = (int) pxFromDp(158);
            }

            if (mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.showNext();
            }

            Intent intent = new Intent(BroadcastIntents.FINISHED_SYNC_CRITICAL_ITEMS_CARD());
            intent.putExtra("height", height);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }

    private class criticalItemListItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            CriticalItemsList.CriticalItem item = criticalItemsList.get(position);

            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_TYPE, item.type);
            intent.putExtra(ItemDetailsActivity.ARG_CHARACTER, item.character);
            intent.putExtra(ItemDetailsActivity.ARG_IMAGE, item.image);
            intent.putExtra(ItemDetailsActivity.ARG_LEVEL, item.level);
            getActivity().startActivity(intent);
        }
    }
}
