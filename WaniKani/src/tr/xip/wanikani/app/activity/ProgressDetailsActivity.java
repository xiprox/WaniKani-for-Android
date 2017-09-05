package tr.xip.wanikani.app.activity;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.fragment.card.ProgressCard;
import tr.xip.wanikani.app.fragment.card.ProgressCardNoTitle;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.ItemsList;
import tr.xip.wanikani.models.KanjiList;
import tr.xip.wanikani.models.RadicalsList;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.widget.adapter.RemainingKanjiAdapter;
import tr.xip.wanikani.widget.adapter.RemainingRadicalsAdapter;

/**
 * Created by Hikari on 9/18/14.
 */
public class ProgressDetailsActivity extends ActionBarActivity implements ProgressCard.ProgressCardListener {
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

    private LoadState radicalsLoaded = LoadState.NOT_LOADED;
    private LoadState kanjiLoaded = LoadState.NOT_LOADED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_details);

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

        loadData();
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

    private void loadData() {
        final User user = DatabaseManager.getUser();
        if (user == null) return;

        WaniKaniApi.getRadicalsList(user.level + "").enqueue(new ThroughDbCallback<Request<RadicalsList>, RadicalsList>() {
            @Override
            public void onResponse(Call<Request<RadicalsList>> call, Response<Request<RadicalsList>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().requested_information != null) {
                    load(response.body().requested_information);
                } else {
                    onFailure(call, null);
                }
                displayData();
            }

            @Override
            public void onFailure(Call<Request<RadicalsList>> call, Throwable t) {
                super.onFailure(call, t);

                RadicalsList items = new RadicalsList();
                items.addAll(DatabaseManager.getItems(BaseItem.ItemType.RADICAL, new int[] {user.level}));

                if (items.size() != 0) {
                    load(items);
                } else {
                    radicalsLoaded = LoadState.FAILED;
                }
            }

            void load(ItemsList list) {
                for (BaseItem item : list) {
                    if (item.isUnlocked()) {
                        if (item.getSrsLevel().equals("apprentice")) {
                            mRemainingRadicals.add(item);
                        }
                    } else {
                        mRemainingRadicals.add(item);
                    }
                }
                radicalsLoaded = LoadState.SUCCESS;
            }
        });

        WaniKaniApi.getKanjiList(user.level + "").enqueue(new ThroughDbCallback<Request<KanjiList>, KanjiList>() {
            @Override
            public void onResponse(Call<Request<KanjiList>> call, Response<Request<KanjiList>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().requested_information != null) {
                    load(response.body().requested_information);
                } else {
                    onFailure(call, null);
                }
                displayData();
            }

            @Override
            public void onFailure(Call<Request<KanjiList>> call, Throwable t) {
                super.onFailure(call, t);

                KanjiList items = new KanjiList();
                items.addAll(DatabaseManager.getItems(BaseItem.ItemType.KANJI, new int[] {user.level}));

                if (items.size() != 0) {
                    load(items);
                } else {
                    kanjiLoaded = LoadState.FAILED;
                }
            }

            void load(ItemsList list) {
                for (BaseItem item : list) {
                    if (item.isUnlocked()) {
                        if (item.getSrsLevel().equals("apprentice")) {
                            mRemainingKanji.add(item);
                        }
                    } else {
                        mRemainingKanji.add(item);
                    }
                }
                kanjiLoaded = LoadState.SUCCESS;
            }
        });
    }

    private void displayData() {
        if (radicalsLoaded != LoadState.NOT_LOADED && kanjiLoaded != LoadState.NOT_LOADED) {
            if (radicalsLoaded == LoadState.SUCCESS) {
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
            } else {
                mRadicalsMessageText.setText(R.string.error_loading_items);
                if (mRadicalsMessageFlipper.getDisplayedChild() == 0)
                    mRadicalsMessageFlipper.showNext();
            }

            if (kanjiLoaded == LoadState.SUCCESS) {
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
                mKanjiMessageText.setText(R.string.error_loading_items);
                if (mKanjiMessageFlipper.getDisplayedChild() == 0)
                    mKanjiMessageFlipper.showNext();
            }

            if (mRadicalsFlipper.getDisplayedChild() == 0)
                mRadicalsFlipper.showNext();

            if (mKanjiFlipper.getDisplayedChild() == 0)
                mKanjiFlipper.showNext();
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

    private enum LoadState {
        NOT_LOADED, SUCCESS, FAILED
    }
}
