package tr.xip.wanikani.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.adapter.RemainingKanjiAdapter;
import tr.xip.wanikani.widget.adapter.RemainingRadicalsAdapter;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.app.fragment.card.ProgressCard;
import tr.xip.wanikani.app.fragment.card.ProgressCardNoTitle;

/**
 * Created by Hikari on 9/18/14.
 */
public class ProgressDetailsActivity extends ActionBarActivity implements ProgressCard.ProgressCardListener {

    WaniKaniApi api;

    Toolbar mToolbar;

    List<BaseItem> mRemainingRadicals = new ArrayList<>();
    List<BaseItem> mRemainingKanji = new ArrayList<>();

    GridView mRadicalsGrid;
    GridView mKanjiGrid;

    ViewFlipper mRadicalsFlipper;
    ViewFlipper mKanjiFlipper;

    ViewFlipper mRadicalsMessageFlipper;
    ViewFlipper mKanjiMessageFlipper;

    TextView mRadicalsMessageText;
    TextView mKanjiMessageText;

    CardView mRadicalsCard;
    CardView mKanjiCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_details);
        api = new WaniKaniApi(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        mRadicalsCard = (CardView) findViewById(R.id.progress_details_radicals_card);
        mKanjiCard = (CardView) findViewById(R.id.progress_details_kanji_card);

        Fragment mProgressCard = getSupportFragmentManager().
                findFragmentById(R.id.progress_details_progress_card);

        ((ProgressCardNoTitle) mProgressCard).load();
        ((ProgressCardNoTitle) mProgressCard).setListener(this, this);

        new RemainingItemsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onProgressCardSyncFinishedListener(String result) {
        /* empty */
    }

    private class RemainingItemsLoadTask extends AsyncTask<Void, Void, Boolean> {
        List<BaseItem> mRadicals;
        List<BaseItem> mKanji;

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
            for (BaseItem item : mRadicals)
                if (item.isUnlocked()) {
                    if (item.getSrsLevel().equals("apprentice"))
                        mRemainingRadicals.add(item);
                } else
                    mRemainingRadicals.add(item);

            for (BaseItem item : mKanji)
                if (item.isUnlocked()) {
                    if (item.getSrsLevel().equals("apprentice"))
                        mRemainingKanji.add(item);
                } else
                    mRemainingKanji.add(item);
        }
    }

    private class RadicalsItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            BaseItem item = mRemainingRadicals.get(position);

            Intent intent = new Intent(ProgressDetailsActivity.this, ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, item);
            startActivity(intent);
        }
    }

    private class KanjiItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            BaseItem item = mRemainingKanji.get(position);

            Intent intent = new Intent(ProgressDetailsActivity.this, ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, item);
            startActivity(intent);
        }
    }
}
