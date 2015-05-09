package tr.xip.wanikani.app.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.SynchronizedScrollView;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.KanjiListGetTask;
import tr.xip.wanikani.client.task.RadicalsListGetTask;
import tr.xip.wanikani.client.task.VocabularyListGetTask;
import tr.xip.wanikani.client.task.callback.KanjiListGetTaskCallbacks;
import tr.xip.wanikani.client.task.callback.RadicalsListGetTaskCallbacks;
import tr.xip.wanikani.client.task.callback.VocabularyListGetTaskCallbacks;
import tr.xip.wanikani.utils.Fonts;
import tr.xip.wanikani.widget.RelativeTimeTextView;

/**
 * Created by xihsa_000 on 3/23/14.
 */
public class ItemDetailsActivity extends ActionBarActivity {

    public static final String ARG_ITEM = "arg_item";
    private static final String TAG = "ItemDetailsActivity";
    private static final int FLIPPER_CONTENT = 0;
    private static final int FLIPPER_PROGRESS_BAR = 1;

    WaniKaniApi api;
    PrefManager prefMan;

    Toolbar mToolbar;

    ViewGroup mActionBarLayout;
    TextView mActionBarTitle;
    ImageView mActionBarTitleImage;
    LinearLayout mActionBarExtension;

    TextView mLevel;
    TextView mMeaning;

    LinearLayout mAlternativeMeaningsAndUserSynonymsCard;
    TextView mAlternativeMeanings;
    LinearLayout mAlternativeMeaningsHolder;
    TextView mUserSynonyms;
    LinearLayout mUserSynonymsHolder;

    LinearLayout mReadingsCard;
    TextView mReading;
    LinearLayout mReadingHolder;
    TextView mOnyomi;
    LinearLayout mOnyomiHolder;
    TextView mKunyomi;
    LinearLayout mKunyomiHolder;

    TextView mReadingNote;
    LinearLayout mReadingNoteCard;

    TextView mMeaningNote;
    LinearLayout mMeaningNoteCard;

    LinearLayout mLocked;
    LinearLayout mBurned;
    TextView mBurnedDate;
    LinearLayout mProgressContent;

    LinearLayout mProgressCard;

    TextView mAlternativeMeaningsTitle;
    TextView mUserSynonymsTitle;
    TextView mReadingTitle;
    TextView mMeaningNoteTitle;
    TextView mReadingNoteTitle;
    TextView mProgressTitle;

    ImageView mSRSLogo;
    TextView mSRSLevel;
    RelativeLayout mUnlockHolder;
    TextView mUnlocked;
    RelativeLayout mNextAvailableHolder;
    RelativeTimeTextView mNextAvailable;
    RelativeLayout mMeaningCorrectHolder;
    TextView mMeaningCorrectPercentage;
    RelativeLayout mMeaningIncorrectHolder;
    TextView mMeaningIncorrectPercentage;
    RelativeLayout mMeaningMaxStreakHolder;
    TextView mMeaningMaxStreak;
    RelativeLayout mMeaningCurrentStreakHolder;
    TextView mMeaningCurrentStreak;
    RelativeLayout mReadingCorrectHolder;
    TextView mReadingCorrectPercentage;
    RelativeLayout mReadingIncorrectHolder;
    TextView mReadingIncorrectPercentage;
    RelativeLayout mReadingMaxStreakHolder;
    TextView mReadingMaxStreak;
    RelativeLayout mReadingCurrentStreakHolder;
    TextView mReadingCurrentStreak;

    ProgressBar mMeaningCorrectProgressBar;
    ProgressBar mMeaningIncorrectProgressBar;
    ProgressBar mReadingCorrectProgressBar;
    ProgressBar mReadingIncorrectProgressBar;

    SynchronizedScrollView mScrollView;

    ViewFlipper mFlipper;

    BaseItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        api = new WaniKaniApi(this);
        prefMan = new PrefManager(this);

        Intent intent = getIntent();
        mItem = (BaseItem) intent.getSerializableExtra(ARG_ITEM);

        setUpActionBar();

        mFlipper = (ViewFlipper) findViewById(R.id.details_view_flipper);

        mAlternativeMeaningsTitle = (TextView) findViewById(R.id.details_title_alternative_meanings);
        mUserSynonymsTitle = (TextView) findViewById(R.id.details_title_user_synonyms);
        mReadingTitle = (TextView) findViewById(R.id.details_title_reading);
        mMeaningNoteTitle = (TextView) findViewById(R.id.details_title_meaning_note);
        mReadingNoteTitle = (TextView) findViewById(R.id.details_title_reading_note);
        mProgressTitle = (TextView) findViewById(R.id.details_progress_title);

        mScrollView = (SynchronizedScrollView) findViewById(R.id.details_scrollview);
        mScrollView.setAnchorView(findViewById(R.id.details_actionbar_shadow_placeholder));
        mScrollView.setSynchronizedView(findViewById(R.id.details_actionbar_shadow));

        mLevel = (TextView) findViewById(R.id.details_level);
        mMeaning = (TextView) findViewById(R.id.details_meaning);

        mAlternativeMeaningsAndUserSynonymsCard = (LinearLayout)
                findViewById(R.id.details_alternative_meanings_and_user_synonyms_card);

        mAlternativeMeanings = (TextView) findViewById(R.id.details_alternative_meanings);
        mAlternativeMeaningsHolder = (LinearLayout) findViewById(R.id.details_alternative_meanings_holder);
        mUserSynonyms = (TextView) findViewById(R.id.details_user_synonyms);
        mUserSynonymsHolder = (LinearLayout) findViewById(R.id.details_user_synonyms_holder);

        mReadingsCard = (LinearLayout) findViewById(R.id.details_readings_card);

        mReading = (TextView) findViewById(R.id.details_reading);
        mReadingHolder = (LinearLayout) findViewById(R.id.details_readings_reading_holder);
        mOnyomi = (TextView) findViewById(R.id.details_readings_onyomi);
        mOnyomiHolder = (LinearLayout) findViewById(R.id.details_readings_onyomi_holder);
        mKunyomi = (TextView) findViewById(R.id.details_readings_kunyomi);
        mKunyomiHolder = (LinearLayout) findViewById(R.id.details_readings_kunyomi_holder);

        mReadingNote = (TextView) findViewById(R.id.details_reading_note);
        mReadingNoteCard = (LinearLayout) findViewById(R.id.details_reading_note_card);

        mMeaningNote = (TextView) findViewById(R.id.details_meaning_note);
        mMeaningNoteCard = (LinearLayout) findViewById(R.id.details_meaning_note_card);

        mLocked = (LinearLayout) findViewById(R.id.details_progress_locked);

        mBurned = (LinearLayout) findViewById(R.id.details_progress_burned);
        mBurnedDate = (TextView) findViewById(R.id.details_progress_burned_date);
        mProgressContent = (LinearLayout) findViewById(R.id.details_progress_content);

        mProgressCard = (LinearLayout) findViewById(R.id.details_progress_card);

        mSRSLogo = (ImageView) findViewById(R.id.details_progress_srs_logo);
        mSRSLevel = (TextView) findViewById(R.id.details_progress_srs_level);
        mUnlockHolder = (RelativeLayout) findViewById(R.id.details_progress_unlocked_holder);
        mUnlocked = (TextView) findViewById(R.id.details_progress_unlocked);
        mNextAvailableHolder = (RelativeLayout) findViewById(R.id.details_progress_next_available_holder);
        mNextAvailable = (RelativeTimeTextView) findViewById(R.id.details_progress_next_available);
        mMeaningCorrectHolder = (RelativeLayout) findViewById(R.id.details_progress_meaning_correct_holder);
        mMeaningCorrectPercentage = (TextView) findViewById(R.id.details_progress_meaning_correct_percentage);
        mMeaningIncorrectHolder = (RelativeLayout) findViewById(R.id.details_progress_meaning_incorrect_holder);
        mMeaningIncorrectPercentage = (TextView) findViewById(R.id.details_progress_meaning_incorrect_percentage);
        mMeaningMaxStreakHolder = (RelativeLayout) findViewById(R.id.details_progress_meaning_max_streak_holder);
        mMeaningMaxStreak = (TextView) findViewById(R.id.details_progress_meaning_max_streak);
        mMeaningCurrentStreakHolder = (RelativeLayout) findViewById(R.id.details_progress_meaning_current_streak_holder);
        mMeaningCurrentStreak = (TextView) findViewById(R.id.details_progress_meaning_current_streak);
        mReadingCorrectHolder = (RelativeLayout) findViewById(R.id.details_progress_reading_correct_holder);
        mReadingCorrectPercentage = (TextView) findViewById(R.id.details_progress_reading_correct_percentage);
        mReadingIncorrectHolder = (RelativeLayout) findViewById(R.id.details_progress_reading_incorrect_holder);
        mReadingIncorrectPercentage = (TextView) findViewById(R.id.details_progress_reading_incorrect_percentage);
        mReadingMaxStreakHolder = (RelativeLayout) findViewById(R.id.details_progress_reading_max_streak_holder);
        mReadingMaxStreak = (TextView) findViewById(R.id.details_progress_reading_max_streak);
        mReadingCurrentStreakHolder = (RelativeLayout) findViewById(R.id.details_progress_reading_current_streak_holder);
        mReadingCurrentStreak = (TextView) findViewById(R.id.details_progress_reading_current_streak);

        mMeaningCorrectProgressBar = (ProgressBar) findViewById(R.id.details_progress_meaning_correct_progress);
        mMeaningIncorrectProgressBar = (ProgressBar) findViewById(R.id.details_progress_meaning_incorrect_progress);
        mReadingCorrectProgressBar = (ProgressBar) findViewById(R.id.details_progress_reading_correct_progress);
        mReadingIncorrectProgressBar = (ProgressBar) findViewById(R.id.details_progress_reading_incorrect_progress);

        mReading.setTypeface(new Fonts().getKanjiFont(this));
        mOnyomi.setTypeface(new Fonts().getKanjiFont(this));
        mKunyomi.setTypeface(new Fonts().getKanjiFont(this));

        if (mItem.getType().equals(BaseItem.ItemType.RADICAL))
            switchToRadicalMode();
        if (mItem.getType().equals(BaseItem.ItemType.KANJI))
            switchToKanjiMode();
        if (mItem.getType().equals(BaseItem.ItemType.VOCABULARY))
            switchToVocabularyMode();

        if (mItem instanceof UnlockItem || mItem instanceof CriticalItem) {
            new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    mItem.getType().toString(),
                    mItem.getLevel() + "",
                    mItem.getCharacter(),
                    mItem.getImage()
            );
        } else {
            if (mItem != null)
                loadData();
            else {
                Log.e(TAG, "No item object passed; exiting...");
                Toast.makeText(getApplicationContext(), R.string.error_couldnt_load_data, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_details_view_on_web:
                Intent intent = new Intent(this, Browser.class);
                intent.putExtra(Browser.ARG_ACTION, Browser.ACTION_ITEM_DETAILS);

                if (mItem.getType().equals(BaseItem.ItemType.RADICAL)) {
                    intent.putExtra(Browser.ARG_ITEM_TYPE, BaseItem.ItemType.RADICAL);
                    intent.putExtra(Browser.ARG_ITEM, mMeaning.getText().toString());
                }
                if (mItem.getType().equals(BaseItem.ItemType.KANJI)) {
                    intent.putExtra(Browser.ARG_ITEM_TYPE, BaseItem.ItemType.KANJI);
                    intent.putExtra(Browser.ARG_ITEM, mActionBarTitle.getText().toString());
                }
                if (mItem.getType().equals(BaseItem.ItemType.VOCABULARY)) {
                    intent.putExtra(Browser.ARG_ITEM_TYPE, BaseItem.ItemType.VOCABULARY);
                    intent.putExtra(Browser.ARG_ITEM, mActionBarTitle.getText().toString());
                }

                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar mActionBar = getSupportActionBar();

        mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_dual_no_icon, null);

        mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);
        mActionBarTitleImage = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_title_image);

        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mActionBarExtension = (LinearLayout) findViewById(R.id.details_actionbar_extension);

        mActionBar.setCustomView(mActionBarLayout);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    private void switchToRadicalMode() {
        Resources res = getResources();

        mAlternativeMeaningsHolder.setVisibility(View.GONE);
        mReadingsCard.setVisibility(View.GONE);
        mReadingNoteCard.setVisibility(View.GONE);
        mReadingCorrectHolder.setVisibility(View.GONE);
        mReadingIncorrectHolder.setVisibility(View.GONE);
        mReadingMaxStreakHolder.setVisibility(View.GONE);
        mReadingCurrentStreakHolder.setVisibility(View.GONE);

        mActionBarExtension.setBackgroundColor(getResources().getColor(R.color.wanikani_radical));
        mToolbar.setBackgroundColor(res.getColor(R.color.wanikani_radical));
        mAlternativeMeaningsTitle.setTextColor(getResources().getColor(R.color.wanikani_radical));
        mUserSynonymsTitle.setTextColor(getResources().getColor(R.color.wanikani_radical));
        mReadingTitle.setTextColor(getResources().getColor(R.color.wanikani_radical));
        mMeaningNoteTitle.setTextColor(getResources().getColor(R.color.wanikani_radical));
        mReadingNoteTitle.setTextColor(getResources().getColor(R.color.wanikani_radical));
        mProgressTitle.setTextColor(getResources().getColor(R.color.wanikani_radical));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(res.getColor(R.color.wanikani_radical_dark));
    }

    private void switchToKanjiMode() {
        Resources res = getResources();

        mActionBarExtension.setBackgroundColor(getResources().getColor(R.color.wanikani_kanji));
        mToolbar.setBackgroundColor(res.getColor(R.color.wanikani_kanji));
        mAlternativeMeaningsTitle.setTextColor(getResources().getColor(R.color.wanikani_kanji));
        mUserSynonymsTitle.setTextColor(getResources().getColor(R.color.wanikani_kanji));
        mReadingTitle.setTextColor(getResources().getColor(R.color.wanikani_kanji));
        mMeaningNoteTitle.setTextColor(getResources().getColor(R.color.wanikani_kanji));
        mReadingNoteTitle.setTextColor(getResources().getColor(R.color.wanikani_kanji));
        mProgressTitle.setTextColor(getResources().getColor(R.color.wanikani_kanji));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(res.getColor(R.color.wanikani_kanji_dark));

        mReadingHolder.setVisibility(View.GONE);
    }

    private void switchToVocabularyMode() {
        Resources res = getResources();

        mActionBarExtension.setBackgroundColor(getResources().getColor(R.color.wanikani_vocabulary));
        mToolbar.setBackgroundColor(res.getColor(R.color.wanikani_vocabulary));
        mAlternativeMeaningsTitle.setTextColor(getResources().getColor(R.color.wanikani_vocabulary));
        mUserSynonymsTitle.setTextColor(getResources().getColor(R.color.wanikani_vocabulary));
        mReadingTitle.setTextColor(getResources().getColor(R.color.wanikani_vocabulary));
        mMeaningNoteTitle.setTextColor(getResources().getColor(R.color.wanikani_vocabulary));
        mReadingNoteTitle.setTextColor(getResources().getColor(R.color.wanikani_vocabulary));
        mProgressTitle.setTextColor(getResources().getColor(R.color.wanikani_vocabulary));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(res.getColor(R.color.wanikani_vocabulary_dark));

        mOnyomiHolder.setVisibility(View.GONE);
        mKunyomiHolder.setVisibility(View.GONE);
    }

    private void loadData() {
        /** Title */
        if (mItem.getImage() != null) {
            mActionBarTitleImage.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext()).load(mItem.getImage()).into(mActionBarTitleImage);
            mActionBarTitleImage.setColorFilter(getResources().getColor(android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP);
            mActionBarTitle.setVisibility(View.GONE);
        } else {
            mActionBarTitleImage.setVisibility(View.GONE);
            mActionBarTitle.setText(mItem.getCharacter());
            mActionBarTitle.setTypeface(new Fonts().getKanjiFont(this));
            mActionBarTitle.setVisibility(View.VISIBLE);
        }

        /** Reading (Kana) */
        if (mItem.getType().equals(BaseItem.ItemType.VOCABULARY))
            mReading.setText(mItem.getKana());

        /** Meaning */
        String[] meanings = mItem.getMeaning().split(", ");
        mMeaning.setText(WordUtils.capitalize(meanings[0]));

        if (meanings.length > 1) {
            mAlternativeMeanings.setText("");
            for (int i = 0; i < meanings.length; i++) {
                if (i != 0) {
                    if (i == 1)
                        mAlternativeMeanings.setText(WordUtils.capitalize(meanings[i]));
                    else
                        mAlternativeMeanings.setText(mAlternativeMeanings.getText().toString()
                                + ", " + WordUtils.capitalize(meanings[i]));
                }
            }
        } else
            mAlternativeMeaningsHolder.setVisibility(View.GONE);

        /** Kanji readings */
        if (mItem.getType().equals(BaseItem.ItemType.KANJI)) {
            if (mItem.getOnyomi() != null)
                mOnyomi.setText(mItem.getOnyomi());
            else
                mOnyomiHolder.setVisibility(View.GONE);

            if (mItem.getKunyomi() != null)
                mKunyomi.setText(mItem.getKunyomi());
            else
                mKunyomiHolder.setVisibility(View.GONE);

            if (mItem.getImportantReading().equals("onyomi"))
                mKunyomi.setTextColor(getResources().getColor(R.color.text_gray_light));
            else
                mOnyomi.setTextColor(getResources().getColor(R.color.text_gray_light));
        }

        /** Level */
        mLevel.setText(mItem.getLevel() + "");

        /** User specific info */
        if (mItem.isUnlocked())
            showUserSpecific();
        else
            hideUserSpecific();

        mFlipper.setDisplayedChild(FLIPPER_CONTENT);
    }

    private void showUserSpecific() {
        /** Check whether burned and display content accordingly */
        if (mItem.isBurned()) {
            mProgressContent.setVisibility(View.GONE);
            mBurned.setVisibility(View.VISIBLE);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
            mBurnedDate.setText(sdf.format(mItem.getBurnedDate()));
        } else {

            /** SRS Level */
            if (mItem.getSrsLevel().equals("apprentice")) {
                mSRSLogo.setImageResource(R.drawable.apprentice);
                mSRSLevel.setText(R.string.srs_title_apprentice);
            }
            if (mItem.getSrsLevel().equals("guru")) {
                mSRSLogo.setImageResource(R.drawable.guru);
                mSRSLevel.setText(R.string.srs_title_guru);
            }
            if (mItem.getSrsLevel().equals("master")) {
                mSRSLogo.setImageResource(R.drawable.master);
                mSRSLevel.setText(R.string.srs_title_master);
            }
            if (mItem.getSrsLevel().equals("enlighten")) {
                mSRSLogo.setImageResource(R.drawable.enlighten);
                mSRSLevel.setText(R.string.srs_title_enlightened);
            }
            if (mItem.getSrsLevel().equals("burned")) {
                mSRSLogo.setImageResource(R.drawable.burned);
                mSRSLevel.setText(R.string.srs_title_burned);
            }

            /** Unlock date */
            SimpleDateFormat unlockDateFormat = new SimpleDateFormat("MMMM d, yyyy");
            mUnlocked.setText(unlockDateFormat.format(mItem.getUnlockDate()) + "");

            /** Next available */
            SimpleDateFormat availableDateFormat = new SimpleDateFormat("dd MMMM HH:mm");

            if (prefMan.isUseSpecificDates()) {
                mNextAvailable.setText(availableDateFormat.format(mItem.getAvailableDate()) + "");
            } else {
                mNextAvailable.setReferenceTime(mItem.getAvailableDate());
            }

            /** Meaning */
            mMeaningCorrectPercentage.setText(mItem.getMeaningCorrectPercentage() + "");
            mMeaningIncorrectPercentage.setText(mItem.getMeaningIncorrectPercentage() + "");

            mMeaningCorrectProgressBar.setProgress(mItem.getMeaningCorrectPercentage());
            mMeaningIncorrectProgressBar.setProgress(mItem.getMeaningIncorrectPercentage());

            mMeaningMaxStreak.setText(mItem.getMeaningMaxStreak() + "");
            mMeaningCurrentStreak.setText(mItem.getMeaningCurrentStreak() + "");

            /** Reading (For Kanji and Vocabulary) */
            if (mItem.getType().equals(BaseItem.ItemType.KANJI) || mItem.getType().equals(BaseItem.ItemType.VOCABULARY)) {
                mReadingCorrectPercentage.setText(mItem.getReadingCorrectPercentage() + "");
                mReadingIncorrectPercentage.setText(mItem.getReadingIncorrectPercentage() + "");

                mReadingMaxStreak.setText(mItem.getReadingMaxStreak() + "");
                mReadingCurrentStreak.setText(mItem.getReadingCurrentStreak() + "");

                mReadingCorrectProgressBar.setProgress(mItem.getReadingCorrectPercentage());
                mReadingIncorrectProgressBar.setProgress(mItem.getReadingIncorrectPercentage());
            }

            /** Meaning note */
            if (mItem.getMeaningNote() != null)
                mMeaningNote.setText(mItem.getMeaningNote());
            else
                mMeaningNoteCard.setVisibility(View.GONE);

            /** User synonyms */
            if (mItem.getUserSynonyms() != null) {
                String synonyms = WordUtils.capitalize(
                        Arrays.toString(mItem.getUserSynonyms()).replace("[", "").replace("]", ""));
                mUserSynonyms.setText(synonyms);
            } else {
                if (mAlternativeMeaningsHolder.getVisibility() == View.GONE)
                    mAlternativeMeaningsAndUserSynonymsCard.setVisibility(View.GONE);
                else
                    mUserSynonymsHolder.setVisibility(View.GONE);
            }

            /** Reading note */
            if (mItem.getReadingNote() != null)
                mReadingNote.setText(mItem.getReadingNote());
            else
                mReadingNoteCard.setVisibility(View.GONE);
        }
    }

    private void hideUserSpecific() {
        if (mAlternativeMeaningsHolder.getVisibility() == View.GONE)
            mAlternativeMeaningsAndUserSynonymsCard.setVisibility(View.GONE);
        else
            mUserSynonymsHolder.setVisibility(View.GONE);

        mMeaningNoteCard.setVisibility(View.GONE);
        mReadingNoteCard.setVisibility(View.GONE);
        mProgressContent.setVisibility(View.GONE);
        mLocked.setVisibility(View.VISIBLE);
    }

    private class LoadTask extends AsyncTask<String, Void, Boolean> {

        String type;
        String level;
        String character;
        String image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mFlipper != null)
                mFlipper.setDisplayedChild(FLIPPER_PROGRESS_BAR);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            type = strings[0];
            level = strings[1];
            character = strings[2];
            image = strings[3];

            try {

                /** In case of a radical item */
                if (type.equals(BaseItem.TYPE_RADICAL)) {
                    new RadicalsListGetTask(ItemDetailsActivity.this, level, new RadicalsListGetTaskCallbacks() {
                        @Override
                        public void onRadicalsListGetTaskPreExecute() {
                            /* Do nothing */
                        }

                        @Override
                        public void onRadicalsListGetTaskPostExecute(List<BaseItem> list) {
                            if (list != null) {
                                if (character != null) {
                                    for (BaseItem item : list)
                                        if (item.getCharacter() != null)
                                            if (item.getCharacter().equals(character)) {
                                                mItem = item;
                                                break;
                                            }
                                } else {
                                    for (BaseItem item : list)
                                        if (item.getImage() != null)
                                            if (item.getImage().equals(image)) {
                                                mItem = item;
                                                break;
                                            }
                                }
                            }
                        }
                    }).executeParallel();
                }

                /** In case of a Kanji item */
                if (type.equals(BaseItem.TYPE_KANJI)) {
                    new KanjiListGetTask(ItemDetailsActivity.this, level, new KanjiListGetTaskCallbacks() {
                        @Override
                        public void onKanjiListGetTaskPreExecute() {
                            /* Do nothing */
                        }

                        @Override
                        public void onKanjiListGetTaskPostExecute(List<BaseItem> list) {
                            if (list != null)
                                for (BaseItem item : list)
                                    if (item.getCharacter().equals(character)) {
                                        mItem = item;
                                        break;
                                    }
                        }
                    }).executeParallel();
                }

                /** In case of a vocabulary item */
                if (type.equals(BaseItem.TYPE_VOCABULARY)) {
                    new VocabularyListGetTask(ItemDetailsActivity.this, level, new VocabularyListGetTaskCallbacks() {
                        @Override
                        public void onVocabularyListGetTaskPreExecute() {
                            /* Do nothing */
                        }

                        @Override
                        public void onVocabularyListGetTaskPostExecute(List<BaseItem> list) {
                            if (list != null)
                                for (BaseItem item : list)
                                    if (item.getCharacter().equals(character)) {
                                        mItem = item;
                                        break;
                                    }
                        }
                    }).executeParallel();
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success)
                loadData();
            else {
                Log.e(TAG, "Failed to fetch item info; exiting...");
                Toast.makeText(getApplicationContext(), R.string.error_couldnt_load_data, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}