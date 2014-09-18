package tr.xip.wanikani;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import tr.xip.wanikani.adapters.RemainingKanjiAdapter;
import tr.xip.wanikani.adapters.RemainingRadicalsAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.KanjiList;
import tr.xip.wanikani.api.response.RadicalsList;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.cards.ProgressCard;
import tr.xip.wanikani.cards.ProgressCardNoTitle;

/**
 * Created by Hikari on 9/18/14.
 */
public class ProgressDetailsActivity extends Activity implements ProgressCard.ProgressCardListener {

    WaniKaniApi api;

    List<RadicalsList.RadicalItem> mRemainingRadicals = new ArrayList<RadicalsList.RadicalItem>();
    List<KanjiList.KanjiItem> mRemainingKanji = new ArrayList<KanjiList.KanjiItem>();

    GridView mRadicalsGrid;
    GridView mKanjiGrid;

    ViewGroup mActionBarLayout;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;

    ViewFlipper mRadicalsFlipper;
    ViewFlipper mKanjiFlipper;

    ViewFlipper mRadicalsMessageFlipper;
    ViewFlipper mKanjiMessageFlipper;

    TextView mRadicalsMessageText;
    TextView mKanjiMessageText;

    FrameLayout mRadicalsCard;
    FrameLayout mKanjiCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_details);
        setUpActionBar();
        api = new WaniKaniApi(this);


        mRadicalsGrid = (GridView) findViewById(R.id.progress_details_radicals_grid);
        mKanjiGrid = (GridView) findViewById(R.id.progress_details_kanji_grid);

        mRadicalsFlipper = (ViewFlipper) findViewById(R.id.progress_details_radicals_flipper);
        mKanjiFlipper = (ViewFlipper) findViewById(R.id.progress_details_kanji_flipper);

        mRadicalsMessageFlipper = (ViewFlipper) findViewById(R.id.progress_details_radicals_message_flipper);
        mKanjiMessageFlipper = (ViewFlipper) findViewById(R.id.progress_details_kanji_message_flipper);

        mRadicalsMessageText = (TextView) findViewById(R.id.progress_details_radicals_message_text);
        mKanjiMessageText = (TextView) findViewById(R.id.progress_details_kanji_message_text);

        mRadicalsGrid.setOnItemClickListener(new RadicalsItemClickListener());
        mKanjiGrid.setOnItemClickListener(new KanjiItemClickListener());

        mRadicalsCard = (FrameLayout) findViewById(R.id.progress_details_radicals_card);
        mKanjiCard = (FrameLayout) findViewById(R.id.progress_details_kanji_card);

        Fragment mProgressCard = getFragmentManager().
                findFragmentById(R.id.progress_details_progress_card);

        ((ProgressCardNoTitle) mProgressCard).load();
        ((ProgressCardNoTitle) mProgressCard).setListener(this, this);

        new RemainingItemsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void setUpActionBar() {
        // TODO - Fix extra empty space
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                    R.layout.actionbar_main, null);

            mActionBarIcon = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_icon);
            mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);

            ActionBar mActionBar = getActionBar();
            mActionBar.setCustomView(mActionBarLayout);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setHomeButtonEnabled(false);
            mActionBar.setIcon(android.R.color.transparent);
            if (Build.VERSION.SDK_INT >= 18)
                mActionBar.setHomeAsUpIndicator(android.R.color.transparent);

            mActionBarTitle.setText(R.string.card_title_progress);
            mActionBarIcon.setImageResource(R.drawable.ic_action_back);
            mActionBarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onProgressCardSyncFinishedListener(String result) {
        /* empty */
    }

    private class RemainingItemsLoadTask extends AsyncTask<Void, Void, Boolean> {
        List<RadicalsList.RadicalItem> mRadicals;
        List<KanjiList.KanjiItem> mKanji;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                User user = api.getUser();
                mRadicals = api.getRadicalsList(user.getLevel() + "");
                mKanji = api.getKanjiList(user.getLevel() + "");

                filterRemaining();

                return true; // success
            } catch (Exception e) {
                e.printStackTrace();
                return false; // failure
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success) {
                if (mRemainingRadicals.size() > 0) {
                    mRadicalsGrid.setAdapter(
                            new RemainingRadicalsAdapter(
                                    ProgressDetailsActivity.this,
                                    R.layout.item_radical_remaining,
                                    mRemainingRadicals
                            )
                    );
                    mRadicalsCard.setVisibility(View.VISIBLE);
                } else {
                    mRadicalsCard.setVisibility(View.GONE);
                }

                if (mRemainingKanji.size() > 0) {
                    mKanjiGrid.setAdapter(
                            new RemainingKanjiAdapter(
                                    ProgressDetailsActivity.this,
                                    R.layout.item_kanji_remaining,
                                    mRemainingKanji
                            )
                    );
                    mKanjiCard.setVisibility(View.VISIBLE);
                } else {
                    mKanjiCard.setVisibility(View.GONE);
                }
            } else {
                mRadicalsMessageText.setText(R.string.error_loading_items);
                mKanjiMessageText.setText(R.string.error_loading_items);

                if (mRadicalsMessageFlipper.getDisplayedChild() == 0)
                    mRadicalsMessageFlipper.showNext();

                if (mKanjiMessageFlipper.getDisplayedChild() == 0)
                    mKanjiMessageFlipper.showNext();
            }

            if (mRadicalsFlipper.getDisplayedChild() == 0)
                mRadicalsFlipper.showNext();

            if (mKanjiFlipper.getDisplayedChild() == 0)
                mKanjiFlipper.showNext();
        }

        private void filterRemaining() {
            for (RadicalsList.RadicalItem item : mRadicals)
                if (item.isUnlocked() && item.getSrsLevel().equals("apprentice"))
                    mRemainingRadicals.add(item);

            for (KanjiList.KanjiItem item : mKanji)
                if (item.isUnlocked() && item.getSrsLevel().equals("apprentice"))
                    mRemainingKanji.add(item);
        }
    }

    private class RadicalsItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            RadicalsList.RadicalItem item = mRemainingRadicals.get(position);

            Intent intent = new Intent(ProgressDetailsActivity.this, ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_TYPE, ItemDetailsActivity.TYPE_RADICAL);
            intent.putExtra(ItemDetailsActivity.ARG_CHARACTER, item.getCharacter());
            intent.putExtra(ItemDetailsActivity.ARG_IMAGE, item.getImage());
            intent.putExtra(ItemDetailsActivity.ARG_LEVEL, item.getLevel());
            startActivity(intent);
        }
    }

    private class KanjiItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            KanjiList.KanjiItem item = mRemainingKanji.get(position);

            Intent intent = new Intent(ProgressDetailsActivity.this, ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_TYPE, ItemDetailsActivity.TYPE_KANJI);
            intent.putExtra(ItemDetailsActivity.ARG_CHARACTER, item.getCharacter());
            intent.putExtra(ItemDetailsActivity.ARG_LEVEL, item.getLevel());
            startActivity(intent);
        }
    }
}
