package tr.xip.wanikani.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.adapter.VocabularyAdapter;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.app.activity.ItemDetailsActivity;
import tr.xip.wanikani.dialogs.LegendDialogFragment;
import tr.xip.wanikani.dialogs.LevelPickerDialogFragment;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.UserInfoGetTask;
import tr.xip.wanikani.client.task.VocabularyListGetTask;
import tr.xip.wanikani.client.task.callback.UserInfoGetTaskCallbacks;
import tr.xip.wanikani.client.task.callback.VocabularyListGetTaskCallbacks;

public class VocabularyFragment extends Fragment implements LevelPickerDialogFragment.LevelDialogListener,
        SwipeRefreshLayout.OnRefreshListener, VocabularyListGetTaskCallbacks {

    Context context;

    WaniKaniApi apiMan;
    PrefManager prefMan;

    TextView mMessageTitle;
    TextView mMessageSummary;
    ImageView mMessageIcon;
    ViewFlipper mMessageFlipper;

    StickyGridHeadersGridView mGrid;
    ViewFlipper mListFlipper;

    LevelPickerDialogFragment mLevelPickerDialog;

    VocabularyAdapter mVocabularyAdapter;
    List<BaseItem> vocabularyList = null;

    View rootView;

    String LEVEL = "";

    MenuItem mLevelItem;

    private SwipeRefreshLayout mMessageSwipeRefreshLayout;

    private void showLegend() {
        new LegendDialogFragment().show(getActivity().getSupportFragmentManager(), "legend-dialog");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        context = getActivity();
        apiMan = new WaniKaniApi(getActivity());
        prefMan = new PrefManager(getActivity());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mLevelPickerDialog == null)
            mLevelItem.setVisible(false);
        else
            mLevelItem.setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_radicals, menu);
        mLevelItem = menu.findItem(R.id.action_level);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        rootView = layoutInflater.inflate(R.layout.fragment_vocabulary, viewGroup, false);

        mMessageSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.vocabulary_message_swipe_refresh);
        mMessageSwipeRefreshLayout.setOnRefreshListener(this);
        mMessageSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh);

        mGrid = (StickyGridHeadersGridView) rootView.findViewById(R.id.vocabulary_grid);
        mGrid.setOnItemClickListener(new gridItemClickListener());

        mListFlipper = (ViewFlipper) rootView.findViewById(R.id.vocabulary_list_flipper);
        mMessageFlipper = (ViewFlipper) rootView.findViewById(R.id.vocabulary_message_flipper);

        mMessageIcon = (ImageView) rootView.findViewById(R.id.vocabulary_message_icon);
        mMessageTitle = (TextView) rootView.findViewById(R.id.vocabulary_message_title);
        mMessageSummary = (TextView) rootView.findViewById(R.id.vocabulary_message_summary);

        if (!prefMan.isLegendLearned()) {
            showLegend();
        }

        fetchLevelAndData();

        setHasOptionsMenu(true);

        return rootView;
    }

    public void fetchLevelAndData() {
        new UserInfoGetTask(context, new UserInfoGetTaskCallbacks() {
            @Override
            public void onUserInfoGetTaskPreExecute() {
                /* Do nothing */
            }

            @Override
            public void onUserInfoGetTaskPostExecute(User user) {
                if (user != null) {
                    LEVEL = user.getLevel() + "";

                    fetchData();

                    mLevelPickerDialog = new LevelPickerDialogFragment();
                } else {
                    mMessageIcon.setImageResource(R.drawable.ic_error_red_36dp);
                    mMessageTitle.setText(R.string.no_items_title);
                    mMessageSummary.setText(R.string.no_items_summary);

                    if (mMessageFlipper.getDisplayedChild() == 0)
                        mMessageFlipper.showNext();

                    mMessageSwipeRefreshLayout.setRefreshing(false);
                }

                if (mListFlipper.getDisplayedChild() == 0)
                    mListFlipper.showNext();
            }
        }).executeParallel();
    }

    public void fetchData() {
        new VocabularyListGetTask(context, LEVEL, this).executeParallel();
    }

    @Override
    public void onLevelDialogPositiveClick(DialogFragment dialog, String level) {
        LEVEL = level;
        fetchData();
    }

    @Override
    public void onLevelDialogResetClick(DialogFragment dialogFragment, String level) {
        fetchLevelAndData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_level:
                showLevelDialog();
                break;
            case R.id.action_legend:
                showLegend();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLevelDialog() {
        if (mLevelPickerDialog != null) {
            mLevelPickerDialog.init(this.getId(), LEVEL);
            mLevelPickerDialog.show(getActivity().getSupportFragmentManager(), "LevelPickerDialogFragment");
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onVocabularyListGetTaskPreExecute() {
        if (mListFlipper.getDisplayedChild() == 1)
            mListFlipper.showPrevious();
    }

    @Override
    public void onVocabularyListGetTaskPostExecute(List<BaseItem> list) {
        if (list != null) {
            Collections.sort(list, new Comparator<BaseItem>() {
                public int compare(BaseItem item1, BaseItem item2) {
                    return Float.valueOf((item1.getLevel() + "")).compareTo(Float.valueOf(item2.getLevel() + ""));
                }
            });

            mVocabularyAdapter = new VocabularyAdapter(context, list, R.layout.header_level, R.layout.item_kanji);
            mGrid.setAdapter(mVocabularyAdapter);

            if (mMessageFlipper.getDisplayedChild() == 1)
                mMessageFlipper.showPrevious();
        } else {
            mMessageIcon.setImageResource(R.drawable.ic_error_red_36dp);
            mMessageTitle.setText(R.string.no_items_title);
            mMessageSummary.setText(R.string.no_items_summary);

            mGrid.setAdapter(new ArrayAdapter(context, R.layout.item_radical));

            if (mMessageFlipper.getDisplayedChild() == 0) {
                mMessageFlipper.showNext();
            }
        }

        ((ActionBarActivity) context).invalidateOptionsMenu();

        if (mListFlipper.getDisplayedChild() == 0)
            mListFlipper.showNext();

        mMessageSwipeRefreshLayout.setRefreshing(false);
    }

    private class gridItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            BaseItem vocabularyItem = mVocabularyAdapter.getItem(position);

            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, vocabularyItem);
            getActivity().startActivity(intent);
        }
    }

}