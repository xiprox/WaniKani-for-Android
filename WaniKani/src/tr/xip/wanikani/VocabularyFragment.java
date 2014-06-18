package tr.xip.wanikani;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.List;

import tr.xip.wanikani.adapters.VocabularyAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.KanjiList;
import tr.xip.wanikani.api.response.VocabularyList;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.managers.ThemeManager;

public class VocabularyFragment extends Fragment implements LevelPickerDialogFragment.LevelDialogListener, SwipeRefreshLayout.OnRefreshListener {

    Context context;

    WaniKaniApi apiMan;
    ThemeManager themeMan;
    PrefManager prefMan;

    TextView mMessageTitle;
    TextView mMessageSummary;
    ImageView mMessageIcon;
    ViewFlipper mMessageFlipper;

    StickyGridHeadersGridView mGrid;
    ViewFlipper mListFlipper;

    LinearLayout mLegend;
    LinearLayout mLegendOk;

    LevelPickerDialogFragment mLevelPickerDialog;

    VocabularyAdapter mVocabularyAdapter;
    List<VocabularyList.VocabularyItem> vocabularyList = null;

    View rootView;

    String LEVEL = "";

    MenuItem mLevelItem;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout mMessageSwipeRefreshLayout;

    private void hideLegend() {
        mLegend.setVisibility(View.GONE);
    }

    private void showLegend() {
        mLegend.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        context = getActivity();
        apiMan = new WaniKaniApi(getActivity());
        prefMan = new PrefManager(getActivity());
        themeMan = new ThemeManager(getActivity());
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.vocabulary_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(R.color.swipe_refresh_1, R.color.swipe_refresh_2,
                R.color.swipe_refresh_3, R.color.swipe_refresh_4);

        mMessageSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.vocabulary_message_swipe_refresh);
        mMessageSwipeRefreshLayout.setOnRefreshListener(this);
        mMessageSwipeRefreshLayout.setColorScheme(R.color.swipe_refresh_1, R.color.swipe_refresh_2,
                R.color.swipe_refresh_3, R.color.swipe_refresh_4);

        mLegend = (LinearLayout) rootView.findViewById(R.id.vocabulary_legend);
        mLegend.setBackgroundColor(getResources().getColor(themeMan.getWindowBackgroundColor()));
        mLegendOk = (LinearLayout) rootView.findViewById(R.id.vocabulary_legend_ok);

        mGrid = (StickyGridHeadersGridView) rootView.findViewById(R.id.vocabulary_grid);
        mGrid.setOnItemClickListener(new gridItemClickListener());

        mListFlipper = (ViewFlipper) rootView.findViewById(R.id.vocabulary_list_flipper);
        mMessageFlipper = (ViewFlipper) rootView.findViewById(R.id.vocabulary_message_flipper);

        mMessageIcon = (ImageView) rootView.findViewById(R.id.vocabulary_message_icon);
        mMessageTitle = (TextView) rootView.findViewById(R.id.vocabulary_message_title);
        mMessageSummary = (TextView) rootView.findViewById(R.id.vocabulary_message_summary);

        if (!prefMan.isVocabularyLegendLearned()) {
            showLegend();
        }

        mLegendOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideLegend();
                prefMan.setVocabularyLegendLearned(true);
            }
        });

        if (Build.VERSION.SDK_INT >= 11)
            new UserLevelTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new UserLevelTask().execute();

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onLevelDialogPositiveClick(DialogFragment dialog, String level) {
        LEVEL = level;
        if (Build.VERSION.SDK_INT >= 11)
            new FetchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new FetchTask().execute();
    }

    @Override
    public void onLevelDialogResetClick(DialogFragment dialogFragment, String level) {
        if (Build.VERSION.SDK_INT >= 11)
            new UserLevelTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new UserLevelTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_level:
                showLevelDialog();
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
        if (Build.VERSION.SDK_INT >= 11)
            new FetchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            new FetchTask().execute();
    }

    private class FetchTask extends AsyncTask<Void, Void, List<VocabularyList.VocabularyItem>> {

        protected List<VocabularyList.VocabularyItem> doInBackground(Void... voids) {
            try {
                vocabularyList = apiMan.getVocabularyList(LEVEL);
                return vocabularyList;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return vocabularyList;
        }

        protected void onPostExecute(List<VocabularyList.VocabularyItem> list) {
            super.onPostExecute(list);

            if (list != null) {
                mVocabularyAdapter = new VocabularyAdapter(context, list, R.layout.header_level, R.layout.item_kanji);
                mGrid.setAdapter(mVocabularyAdapter);

                if (!prefMan.isKanjiLegendLearned()) {
                    showLegend();
                }

                if (mMessageFlipper.getDisplayedChild() == 1)
                    mMessageFlipper.showPrevious();
            } else {
                mMessageIcon.setImageResource(R.drawable.ic_action_warning);
                mMessageTitle.setText(R.string.no_items_title);
                mMessageSummary.setText(R.string.no_items_summary);

                mGrid.setAdapter(null);

                if (mMessageFlipper.getDisplayedChild() == 0) {
                    mMessageFlipper.showNext();
                }
            }

            ((ActionBarActivity) context).supportInvalidateOptionsMenu();

            if (mListFlipper.getDisplayedChild() == 0)
                mListFlipper.showNext();

            mSwipeRefreshLayout.setRefreshing(false);
            mMessageSwipeRefreshLayout.setRefreshing(false);
        }

        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

    private class UserLevelTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void[] voids) {
            try {
                LEVEL = apiMan.getUser().getLevel() + "";
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success) {
                if (Build.VERSION.SDK_INT >= 11)
                    new FetchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    new FetchTask().execute();

                mLevelPickerDialog = new LevelPickerDialogFragment();
            } else {
                mMessageIcon.setImageResource(R.drawable.ic_action_warning);
                mMessageTitle.setText(R.string.no_items_title);
                mMessageSummary.setText(R.string.no_items_summary);

                if (mMessageFlipper.getDisplayedChild() == 0)
                    mMessageFlipper.showNext();

                mMessageSwipeRefreshLayout.setRefreshing(false);
            }

            if (mListFlipper.getDisplayedChild() == 0)
                mListFlipper.showNext();
        }
    }

    private class gridItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            VocabularyList.VocabularyItem vocabularyItem = mVocabularyAdapter.getItem(position);

            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_TYPE, ItemDetailsActivity.TYPE_VOCABULARY);
            intent.putExtra(ItemDetailsActivity.ARG_CHARACTER, vocabularyItem.getCharacter());
            intent.putExtra(ItemDetailsActivity.ARG_LEVEL, vocabularyItem.getLevel());
            getActivity().startActivity(intent);
        }
    }

}