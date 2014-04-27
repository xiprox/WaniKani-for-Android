package tr.xip.wanikani;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.KanjiList;
import tr.xip.wanikani.api.response.RadicalsList;
import tr.xip.wanikani.api.response.VocabularyList;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.utils.Fonts;

/**
 * Created by xihsa_000 on 3/23/14.
 */
public class ItemDetailsActivity extends ActionBarActivity {

    WaniKaniApi api;
    ThemeManager themeMan;

    public static final String ARG_TYPE = "type";
    public static final String ARG_CHARACTER = "character";
    public static final String ARG_IMAGE = "image";
    public static final String ARG_LEVEL = "level";

    public static final String TYPE_RADICAL = "radical";
    public static final String TYPE_KANJI = "kanji";
    public static final String TYPE_VOCABULARY = "vocabulary";

    String gotType;
    String gotCharacter;
    String gotImage;
    int gotLevel;

    TextView mLevel;
    TextView mCharacter;
    LinearLayout mCharacterCard;
    ImageView mImage;
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
    TextView mProgressTitle;
    ImageView mSRSLogo;
    TextView mSRSLevel;
    RelativeLayout mUnlockHolder;
    TextView mUnlocked;
    RelativeLayout mNextAvailableHolder;
    TextView mNextAvailable;
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

//    ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = new WaniKaniApi(this);
        themeMan = new ThemeManager(this);

        setTheme(themeMan.getTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();
        gotType = intent.getStringExtra(ARG_TYPE);
        gotCharacter = intent.getStringExtra(ARG_CHARACTER);
        gotImage = intent.getStringExtra(ARG_IMAGE);
        gotLevel = intent.getIntExtra(ARG_LEVEL, 0);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mActionBarView = inflater.inflate(R.layout.actionbar_custom, null);

        TextView mActionBarTitleText = (TextView) mActionBarView.findViewById(R.id.actionbar_custom_title_text);
        final ImageView mActionBarTitleImage = (ImageView) mActionBarView.findViewById(R.id.actionbar_custom_title_image);

        mActionBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (gotImage != null) {
            mActionBarTitleImage.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getApplicationContext()).load(gotImage).into(mActionBarTitleImage);
                }
            }).start();
            mActionBarTitleImage.setColorFilter(getResources().getColor(R.color.text_gray), PorterDuff.Mode.SRC_ATOP);
            mActionBarTitleText.setVisibility(View.GONE);

        } else {
            mActionBarTitleImage.setVisibility(View.GONE);
            mActionBarTitleText.setText(gotCharacter);
            mActionBarTitleText.setTypeface(new Fonts().getKanjiFont(this));
            mActionBarTitleText.setVisibility(View.VISIBLE);
        }

        mActionBar.setCustomView(mActionBarView);

        mLevel = (TextView) findViewById(R.id.details_level);
        mCharacter = (TextView) findViewById(R.id.details_character_text);
        mCharacterCard = (LinearLayout) findViewById(R.id.details_character_card);
        mImage = (ImageView) findViewById(R.id.details_character_image);
        mMeaning = (TextView) findViewById(R.id.details_meaning);

        mAlternativeMeaningsAndUserSynonymsCard = (LinearLayout) findViewById(R.id.details_aternative_meanings_and_user_synonyms_card);
        mAlternativeMeaningsAndUserSynonymsCard.setBackgroundResource(themeMan.getCard());

        mAlternativeMeanings = (TextView) findViewById(R.id.details_alternative_meanings);
        mAlternativeMeaningsHolder = (LinearLayout) findViewById(R.id.details_alternative_meanings_holder);
        mUserSynonyms = (TextView) findViewById(R.id.details_user_synonyms);
        mUserSynonymsHolder = (LinearLayout) findViewById(R.id.details_user_synonyms_holder);

        mReadingsCard = (LinearLayout) findViewById(R.id.details_readings_card);
        mReadingsCard.setBackgroundResource(themeMan.getCard());

        mReading = (TextView) findViewById(R.id.details_reading);
        mReadingHolder = (LinearLayout) findViewById(R.id.details_readings_reading_holder);
        mOnyomi = (TextView) findViewById(R.id.details_readings_onyomi);
        mOnyomiHolder = (LinearLayout) findViewById(R.id.details_readings_onyomi_holder);
        mKunyomi = (TextView) findViewById(R.id.details_readings_kunyomi);
        mKunyomiHolder = (LinearLayout) findViewById(R.id.details_readings_kunyomi_holder);

        mReadingNote = (TextView) findViewById(R.id.details_reading_note);
        mReadingNoteCard = (LinearLayout) findViewById(R.id.details_reading_note_card);
        mReadingNoteCard.setBackgroundResource(themeMan.getCard());

        mMeaningNote = (TextView) findViewById(R.id.details_meaning_note);
        mMeaningNoteCard = (LinearLayout) findViewById(R.id.details_meaning_note_card);
        mMeaningNoteCard.setBackgroundResource(themeMan.getCard());

        mLocked = (LinearLayout) findViewById(R.id.details_progress_locked);
        mBurned = (LinearLayout) findViewById(R.id.details_progress_burned);
        mBurnedDate = (TextView) findViewById(R.id.details_progress_burned_date);
        mProgressContent = (LinearLayout) findViewById(R.id.details_progress_content);

        mProgressCard = (LinearLayout) findViewById(R.id.details_progress_card);
        mProgressCard.setBackgroundResource(themeMan.getCard());

        mProgressTitle = (TextView) findViewById(R.id.details_progress_title);
        mSRSLogo = (ImageView) findViewById(R.id.details_progress_srs_logo);
        mSRSLevel = (TextView) findViewById(R.id.details_progress_srs_level);
        mUnlockHolder = (RelativeLayout) findViewById(R.id.details_progress_unlocked_holder);
        mUnlocked = (TextView) findViewById(R.id.details_progress_unlocked);
        mNextAvailableHolder = (RelativeLayout) findViewById(R.id.details_progress_next_available_holder);
        mNextAvailable = (TextView) findViewById(R.id.details_progress_next_available);
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

        /*mViewFlipper = (ViewFlipper) findViewById(R.id.details_view_flipper);
        mViewFlipper.setInAnimation(this, R.anim.abc_fade_in);
        mViewFlipper.setOutAnimation(this, R.anim.abc_fade_out);*/

        mCharacter.setTypeface(new Fonts().getKanjiFont(this));
        mReading.setTypeface(new Fonts().getKanjiFont(this));
        mOnyomi.setTypeface(new Fonts().getKanjiFont(this));
        mKunyomi.setTypeface(new Fonts().getKanjiFont(this));

        new LoadTask().execute();
    }

    @Override
    public boolean onNavigateUp() {
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

                if (gotType.equals(TYPE_RADICAL)) {
                    intent.putExtra(Browser.ARG_ITEM_TYPE, TYPE_RADICAL);
                    intent.putExtra(Browser.ARG_ITEM, mMeaning.getText().toString());
                }
                if (gotType.equals(TYPE_KANJI)) {
                    intent.putExtra(Browser.ARG_ITEM_TYPE, TYPE_KANJI);
                    intent.putExtra(Browser.ARG_ITEM, mCharacter.getText().toString());
                }
                if (gotType.equals(TYPE_VOCABULARY)) {
                    intent.putExtra(Browser.ARG_ITEM_TYPE, TYPE_VOCABULARY);
                    intent.putExtra(Browser.ARG_ITEM, mCharacter.getText().toString());
                }

                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchToRadicalMode() {
        mCharacterCard.setBackgroundResource(R.drawable.card_radical);
        mAlternativeMeaningsHolder.setVisibility(View.GONE);
        mReadingsCard.setVisibility(View.GONE);
        mReadingNoteCard.setVisibility(View.GONE);
        mReadingCorrectHolder.setVisibility(View.GONE);
        mReadingIncorrectHolder.setVisibility(View.GONE);
        mReadingMaxStreakHolder.setVisibility(View.GONE);
        mReadingCurrentStreakHolder.setVisibility(View.GONE);
    }

    private void switchToKanjiMode() {
        mCharacterCard.setBackgroundResource(R.drawable.card_kanji);
        mReadingHolder.setVisibility(View.GONE);
    }

    private void switchToVocabularyMode() {
        mCharacterCard.setBackgroundResource(R.drawable.card_vocabulary);
        mOnyomiHolder.setVisibility(View.GONE);
        mKunyomiHolder.setVisibility(View.GONE);
    }

    private class LoadTask extends AsyncTask<Void, Void, String> {

        RadicalsList.RadicalItem radicalItem;
        KanjiList.KanjiItem kanjiItem;
        VocabularyList.VocabularyItem vocabularyItem;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                if (gotType.equals(TYPE_RADICAL)) {
                    List<RadicalsList.RadicalItem> list = api.getRadicalsList(gotLevel + "");

                    if (gotCharacter != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).character != null) {
                                if (list.get(i).character.equals(gotCharacter)) {
                                    radicalItem = list.get(i);
                                    break;
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).image != null) {
                                if (list.get(i).image.equals(gotImage)) {
                                    radicalItem = list.get(i);
                                    break;
                                }
                            }
                        }
                    }
                }

                if (gotType.equals(TYPE_KANJI)) {
                    List<KanjiList.KanjiItem> list = api.getKanjiList(gotLevel + "");

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).character.equals(gotCharacter)) {
                            kanjiItem = list.get(i);
                            break;
                        }
                    }
                }

                if (gotType.equals(TYPE_VOCABULARY)) {
                    List<VocabularyList.VocabularyItem> list = api.getVocabularyList(gotLevel + "");

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).character.equals(gotCharacter)) {
                            vocabularyItem = list.get(i);
                            break;
                        }
                    }
                }
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("success")) {
                if (gotType.equals(TYPE_RADICAL)) {
                    switchToRadicalMode();

                    mLevel.setText(radicalItem.level + "");

                    if (radicalItem.image != null) {
                        mImage.setVisibility(View.VISIBLE);
                        mCharacter.setVisibility(View.GONE);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(getApplicationContext()).load(radicalItem.image).into(mImage);
                            }
                        }).start();
                    } else {
                        mImage.setVisibility(View.GONE);
                        mCharacter.setVisibility(View.VISIBLE);
                        mCharacter.setText(radicalItem.character);
                    }

                    mMeaning.setText(WordUtils.capitalize(radicalItem.meaning));

                    if (radicalItem.user_specific != null) {
                        if (radicalItem.user_specific.user_synonyms != null) {
                            String synonyms = WordUtils.capitalize(Arrays.toString(radicalItem.user_specific.user_synonyms).replace("[", "").replace("]", ""));
                            mUserSynonyms.setText(synonyms);
                        } else {
                            mAlternativeMeaningsAndUserSynonymsCard.setVisibility(View.GONE);
                        }

                        if (radicalItem.user_specific.meaning_note != null) {
                            mMeaningNote.setText(radicalItem.user_specific.meaning_note);
                        } else {
                            mMeaningNoteCard.setVisibility(View.GONE);
                        }

                        if (radicalItem.user_specific.burned) {
                            mProgressContent.setVisibility(View.GONE);
                            mBurned.setVisibility(View.VISIBLE);

                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
                            mBurnedDate.setText(sdf.format(radicalItem.user_specific.burned_date * 1000));
                        } else {

                            if (radicalItem.user_specific.srs.equals("apprentice")) {
                                mSRSLogo.setImageResource(R.drawable.apprentice);
                                mSRSLevel.setText(R.string.srs_title_apprentice);
                            }
                            if (radicalItem.user_specific.srs.equals("guru")) {
                                mSRSLogo.setImageResource(R.drawable.guru);
                                mSRSLevel.setText(R.string.srs_title_guru);
                            }
                            if (radicalItem.user_specific.srs.equals("master")) {
                                mSRSLogo.setImageResource(R.drawable.master);
                                mSRSLevel.setText(R.string.srs_title_master);
                            }
                            if (radicalItem.user_specific.srs.equals("enlighten")) {
                                mSRSLogo.setImageResource(R.drawable.enlighten);
                                mSRSLevel.setText(R.string.srs_title_enlightened);
                            }
                            if (radicalItem.user_specific.srs.equals("burned")) {
                                mSRSLogo.setImageResource(R.drawable.burned);
                                mSRSLevel.setText(R.string.srs_title_burned);
                            }

                            SimpleDateFormat unlockDateFormat = new SimpleDateFormat("MMMM d, yyyy");
                            SimpleDateFormat availableDateFormat = new SimpleDateFormat("dd MMMM HH:mm");

                            mUnlocked.setText(unlockDateFormat.format(radicalItem.user_specific.unlocked_date * 1000) + "");
                            mNextAvailable.setText(availableDateFormat.format(radicalItem.user_specific.available_date * 1000) + "");

                            int totalMeaningAnswers = radicalItem.user_specific.meaning_correct + radicalItem.user_specific.meaning_incorrect;

                            double meaningCorrectPercentage = (double) radicalItem.user_specific.meaning_correct / totalMeaningAnswers * 100;
                            double meaningIncorrectPercentage = (double) radicalItem.user_specific.meaning_incorrect / totalMeaningAnswers * 100;

                            mMeaningCorrectPercentage.setText(meaningCorrectPercentage + "");
                            mMeaningIncorrectPercentage.setText(meaningIncorrectPercentage + "");

                            mMeaningCorrectProgressBar.setProgress((int) meaningCorrectPercentage);
                            mMeaningIncorrectProgressBar.setProgress((int) meaningIncorrectPercentage);

                            mMeaningMaxStreak.setText(radicalItem.user_specific.meaning_max_streak + "");
                            mMeaningCurrentStreak.setText(radicalItem.user_specific.meaning_current_streak + "");
                        }
                    } else {
                        mProgressContent.setVisibility(View.GONE);
                        mLocked.setVisibility(View.VISIBLE);
                    }
                }
                if (gotType.equals(TYPE_KANJI)) {
                    switchToKanjiMode();

                    mLevel.setText(kanjiItem.level + "");
                    mCharacter.setText(kanjiItem.character);

                    String[] meanings = kanjiItem.meaning.split(", ");
                    mMeaning.setText(WordUtils.capitalize(meanings[0]));

                    if (meanings.length > 1) {
                        mAlternativeMeanings.setText("");
                        for (int i = 0; i < meanings.length; i++) {
                            if (i != 0) {
                                if (i == 1) {
                                    mAlternativeMeanings.setText(WordUtils.capitalize(meanings[i]));
                                } else {
                                    mAlternativeMeanings.setText(mAlternativeMeanings.getText().toString() + ", " + WordUtils.capitalize(meanings[i]));
                                }
                            }
                        }
                    } else {
                        if (kanjiItem.user_specific == null || kanjiItem.user_specific.user_synonyms == null) {
                            mAlternativeMeaningsAndUserSynonymsCard.setVisibility(View.GONE);
                        } else {
                            mAlternativeMeaningsHolder.setVisibility(View.GONE);
                        }
                    }

                    if (kanjiItem.onyomi != null) {
                        mOnyomi.setText(kanjiItem.onyomi);
                    } else {
                        mOnyomiHolder.setVisibility(View.GONE);
                    }

                    if (kanjiItem.kunyomi != null) {
                        mKunyomi.setText(kanjiItem.kunyomi);
                    } else {
                        mKunyomiHolder.setVisibility(View.GONE);
                    }

                    if (kanjiItem.important_reading.equals("onyomi")) {
                        mKunyomi.setTextColor(getResources().getColor(R.color.text_gray_light));
                    } else {
                        mOnyomi.setTextColor(getResources().getColor(R.color.text_gray_light));
                    }

                    if (kanjiItem.user_specific != null) {
                        if (kanjiItem.user_specific.user_synonyms != null) {
                            String synonyms = WordUtils.capitalize(Arrays.toString(kanjiItem.user_specific.user_synonyms).replace("[", "").replace("]", ""));
                            mUserSynonyms.setText(synonyms);
                        } else {
                            mUserSynonymsHolder.setVisibility(View.GONE);
                        }

                        if (kanjiItem.user_specific.reading_note != null) {
                            mReadingNote.setText(kanjiItem.user_specific.reading_note);
                        } else {
                            mReadingNoteCard.setVisibility(View.GONE);
                        }

                        if (kanjiItem.user_specific.meaning_note != null) {
                            mMeaningNote.setText(kanjiItem.user_specific.meaning_note);
                        } else {
                            mMeaningNoteCard.setVisibility(View.GONE);
                        }

                        if (kanjiItem.user_specific.burned) {
                            mProgressContent.setVisibility(View.GONE);
                            mBurned.setVisibility(View.VISIBLE);

                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
                            mBurnedDate.setText(sdf.format(kanjiItem.user_specific.burned_date * 1000));
                        } else {

                            if (kanjiItem.user_specific.srs.equals("apprentice")) {
                                mSRSLogo.setImageResource(R.drawable.apprentice);
                                mSRSLevel.setText(R.string.srs_title_apprentice);
                            }
                            if (kanjiItem.user_specific.srs.equals("guru")) {
                                mSRSLogo.setImageResource(R.drawable.guru);
                                mSRSLevel.setText(R.string.srs_title_guru);
                            }
                            if (kanjiItem.user_specific.srs.equals("master")) {
                                mSRSLogo.setImageResource(R.drawable.master);
                                mSRSLevel.setText(R.string.srs_title_master);
                            }
                            if (kanjiItem.user_specific.srs.equals("enlighten")) {
                                mSRSLogo.setImageResource(R.drawable.enlighten);
                                mSRSLevel.setText(R.string.srs_title_enlightened);
                            }
                            if (kanjiItem.user_specific.srs.equals("burned")) {
                                mSRSLogo.setImageResource(R.drawable.burned);
                                mSRSLevel.setText(R.string.srs_title_burned);
                            }

                            SimpleDateFormat unlockDateFormat = new SimpleDateFormat("MMMM d, yyyy");
                            SimpleDateFormat availableDateFormat = new SimpleDateFormat("dd MMMM HH:mm");

                            mUnlocked.setText(unlockDateFormat.format(kanjiItem.user_specific.unlocked_date * 1000) + "");
                            mNextAvailable.setText(availableDateFormat.format(kanjiItem.user_specific.available_date * 1000) + "");

                            int totalMeaningAnswers = kanjiItem.user_specific.meaning_correct + kanjiItem.user_specific.meaning_incorrect;
                            double meaningCorrectPercentage = (double) kanjiItem.user_specific.meaning_correct / totalMeaningAnswers * 100;
                            double meaningIncorrectPercentage = (double) kanjiItem.user_specific.meaning_incorrect / totalMeaningAnswers * 100;

                            int totalReadingAnswers = kanjiItem.user_specific.reading_correct + kanjiItem.user_specific.reading_incorrect;
                            double readingCorrectPercentage = (double) kanjiItem.user_specific.reading_correct / totalReadingAnswers * 100;
                            double readingIncorrectPercentage = (double) kanjiItem.user_specific.reading_incorrect / totalReadingAnswers * 100;

                            mMeaningCorrectPercentage.setText(meaningCorrectPercentage + "");
                            mMeaningIncorrectPercentage.setText(meaningIncorrectPercentage + "");
                            mMeaningMaxStreak.setText(kanjiItem.user_specific.meaning_max_streak + "");
                            mMeaningCurrentStreak.setText(kanjiItem.user_specific.meaning_current_streak + "");

                            mMeaningCorrectProgressBar.setProgress((int) meaningCorrectPercentage);
                            mMeaningIncorrectProgressBar.setProgress((int) meaningIncorrectPercentage);

                            mReadingCorrectPercentage.setText(readingCorrectPercentage + "");
                            mReadingIncorrectPercentage.setText(readingIncorrectPercentage + "");
                            mReadingMaxStreak.setText(kanjiItem.user_specific.reading_max_streak + "");
                            mReadingCurrentStreak.setText(kanjiItem.user_specific.reading_current_streak + "");

                            mReadingCorrectProgressBar.setProgress((int) readingCorrectPercentage);
                            mReadingIncorrectProgressBar.setProgress((int) readingIncorrectPercentage);
                        }
                    } else {
                        mProgressCard.setBackgroundResource(R.drawable.card_light_pressed);
                        if (mAlternativeMeaningsHolder.getVisibility() == View.GONE) {
                            mAlternativeMeaningsAndUserSynonymsCard.setVisibility(View.GONE);
                        } else {
                            mUserSynonymsHolder.setVisibility(View.GONE);
                        }
                        mReadingNoteCard.setVisibility(View.GONE);
                        mMeaningNoteCard.setVisibility(View.GONE);
                        mProgressContent.setVisibility(View.GONE);
                        mLocked.setVisibility(View.VISIBLE);
                        mProgressTitle.setVisibility(View.GONE);
                    }
                }
                if (gotType.equals(TYPE_VOCABULARY)) {
                    switchToVocabularyMode();

                    mLevel.setText(vocabularyItem.level + "");
                    mCharacter.setText(vocabularyItem.character);

                    mReading.setText(vocabularyItem.kana);

                    String[] meanings = vocabularyItem.meaning.split(", ");
                    mMeaning.setText(WordUtils.capitalize(meanings[0]));

                    if (meanings.length > 1) {
                        mAlternativeMeanings.setText("");
                        for (int i = 0; i < meanings.length; i++) {
                            if (i != 0) {
                                if (i == 1) {
                                    mAlternativeMeanings.setText(WordUtils.capitalize(meanings[i]));
                                } else {
                                    mAlternativeMeanings.setText(mAlternativeMeanings.getText().toString() + ", " + WordUtils.capitalize(meanings[i]));
                                }
                            }
                        }
                    } else {
                        if (vocabularyItem.user_specific == null || vocabularyItem.user_specific.user_synonyms == null) {
                            mAlternativeMeaningsAndUserSynonymsCard.setVisibility(View.GONE);
                        } else {
                            mAlternativeMeaningsHolder.setVisibility(View.GONE);
                        }
                    }

                    if (vocabularyItem.user_specific != null) {
                        if (vocabularyItem.user_specific.user_synonyms != null) {
                            String synonyms = WordUtils.capitalize(Arrays.toString(vocabularyItem.user_specific.user_synonyms).replace("[", "").replace("]", ""));
                            mUserSynonyms.setText(synonyms);
                        } else {
                            mUserSynonymsHolder.setVisibility(View.GONE);
                        }

                        if (vocabularyItem.user_specific.reading_note != null) {
                            mReadingNote.setText(vocabularyItem.user_specific.reading_note);
                        } else {
                            mReadingNoteCard.setVisibility(View.GONE);
                        }

                        if (vocabularyItem.user_specific.meaning_note != null) {
                            mMeaningNote.setText(vocabularyItem.user_specific.meaning_note);
                        } else {
                            mMeaningNoteCard.setVisibility(View.GONE);
                        }

                        if (vocabularyItem.user_specific.burned) {
                            mProgressContent.setVisibility(View.GONE);
                            mBurned.setVisibility(View.VISIBLE);

                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
                            mBurnedDate.setText(sdf.format(vocabularyItem.user_specific.burned_date * 1000));
                        } else {

                            if (vocabularyItem.user_specific.srs.equals("apprentice")) {
                                mSRSLogo.setImageResource(R.drawable.apprentice);
                                mSRSLevel.setText(R.string.srs_title_apprentice);
                            }
                            if (vocabularyItem.user_specific.srs.equals("guru")) {
                                mSRSLogo.setImageResource(R.drawable.guru);
                                mSRSLevel.setText(R.string.srs_title_guru);
                            }
                            if (vocabularyItem.user_specific.srs.equals("master")) {
                                mSRSLogo.setImageResource(R.drawable.master);
                                mSRSLevel.setText(R.string.srs_title_master);
                            }
                            if (vocabularyItem.user_specific.srs.equals("enlighten")) {
                                mSRSLogo.setImageResource(R.drawable.enlighten);
                                mSRSLevel.setText(R.string.srs_title_enlightened);
                            }
                            if (vocabularyItem.user_specific.srs.equals("burned")) {
                                mSRSLogo.setImageResource(R.drawable.burned);
                                mSRSLevel.setText(R.string.srs_title_burned);
                            }

                            SimpleDateFormat unlockDateFormat = new SimpleDateFormat("MMMM d, yyyy");
                            SimpleDateFormat availableDateFormat = new SimpleDateFormat("dd MMMM HH:mm");

                            mUnlocked.setText(unlockDateFormat.format(vocabularyItem.user_specific.unlocked_date * 1000) + "");
                            mNextAvailable.setText(availableDateFormat.format(vocabularyItem.user_specific.available_date * 1000) + "");

                            int totalMeaningAnswers = vocabularyItem.user_specific.meaning_correct + vocabularyItem.user_specific.meaning_incorrect;
                            double meaningCorrectPercentage = (double) vocabularyItem.user_specific.meaning_correct / totalMeaningAnswers * 100;
                            double meaningIncorrectPercentage = (double) vocabularyItem.user_specific.meaning_incorrect / totalMeaningAnswers * 100;

                            int totalReadingAnswers = vocabularyItem.user_specific.reading_correct + vocabularyItem.user_specific.reading_incorrect;
                            double readingCorrectPercentage = (double) vocabularyItem.user_specific.reading_correct / totalReadingAnswers * 100;
                            double readingIncorrectPercentage = (double) vocabularyItem.user_specific.reading_incorrect / totalReadingAnswers * 100;

                            mMeaningCorrectPercentage.setText(meaningCorrectPercentage + "");
                            mMeaningIncorrectPercentage.setText(meaningIncorrectPercentage + "");
                            mMeaningMaxStreak.setText(vocabularyItem.user_specific.meaning_max_streak + "");
                            mMeaningCurrentStreak.setText(vocabularyItem.user_specific.meaning_current_streak + "");

                            mMeaningCorrectProgressBar.setProgress((int) meaningCorrectPercentage);
                            mMeaningIncorrectProgressBar.setProgress((int) meaningIncorrectPercentage);

                            mReadingCorrectPercentage.setText(readingCorrectPercentage + "");
                            mReadingIncorrectPercentage.setText(readingIncorrectPercentage + "");
                            mReadingMaxStreak.setText(vocabularyItem.user_specific.reading_max_streak + "");
                            mReadingCurrentStreak.setText(vocabularyItem.user_specific.reading_current_streak + "");

                            mReadingCorrectProgressBar.setProgress((int) readingCorrectPercentage);
                            mReadingIncorrectProgressBar.setProgress((int) readingIncorrectPercentage);
                        }
                    } else {
                        mProgressCard.setBackgroundResource(R.drawable.card_light_pressed);
                        if (mAlternativeMeaningsHolder.getVisibility() == View.GONE) {
                            mAlternativeMeaningsAndUserSynonymsCard.setVisibility(View.GONE);
                        } else {
                            mUserSynonymsHolder.setVisibility(View.GONE);
                        }
                        mReadingNoteCard.setVisibility(View.GONE);
                        mMeaningNoteCard.setVisibility(View.GONE);
                        mProgressContent.setVisibility(View.GONE);
                        mLocked.setVisibility(View.VISIBLE);
                        mProgressTitle.setVisibility(View.GONE);
                    }
                }

                /*if(mViewFlipper.getDisplayedChild() == 0) {
                    mViewFlipper.showNext();
                }*/
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_couldnt_load_data, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}