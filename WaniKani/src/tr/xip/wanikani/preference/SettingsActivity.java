package tr.xip.wanikani.preference;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tr.xip.wanikani.R;
import tr.xip.wanikani.dialogs.OpenSourceLicensesDialogFragment;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.preference.custom.CriticalItemsNumberPreference;
import tr.xip.wanikani.preference.custom.CriticalItemsPercentagePreference;
import tr.xip.wanikani.preference.custom.RecentUnlocksNumberPreference;

/**
 * Created by xihsa_000 on 4/4/14.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    PrefManager prefMan;

    TextView mApiKey;
    RelativeLayout mCustomFonts;
    CheckBox mCustomFontsCheckBox;
    RelativeLayout mGeneralUseSpecificDates;
    CheckBox mGeneralUseSpecificDatesCheckBox;
    LinearLayout mDashboardRecentUnlocksNumber;
    LinearLayout mDashboardCriticalItemsPercentage;
    LinearLayout mCriticalItemsNumber;
    RelativeLayout mReviewsLessonsFullscreen;
    CheckBox mReviewsLessonsFullscreenCheckBox;
    RelativeLayout mReviewImprovements;
    CheckBox mReviewImprovementsCheckBox;
    RelativeLayout mIgnoreButton;
    CheckBox mIgnoreButtonCheckBox;
    RelativeLayout mSingleButton;
    CheckBox mSingleButtonCheckBox;
    RelativeLayout mPortraitMode;
    CheckBox mPortraitModeCheckBox;
    RelativeLayout mWaniKaniImprove;
    CheckBox mWaniKaniImproveCheckBox;
    RelativeLayout mReviewOrder;
    CheckBox mReviewOrderCheckBox;
    RelativeLayout mLessonOrder;
    CheckBox mLessonOrderCheckBox;
    RelativeLayout mExternalFramePlacer;
    CheckBox mExternalFramePlacerCheckBox;
    TextView mExternalFramePlacerDictionary;
    RelativeLayout mPartOfSpeech;
    CheckBox mPartOfSpeechCheckBox;
    RelativeLayout mAutoPopup;
    RelativeLayout mMistakeDelay;
    CheckBox mMistakeDelayCheckBox;
    CheckBox mAutoPopupCheckBox;
    RelativeLayout mRomaji;
    CheckBox mRomajiCheckBox;
    RelativeLayout mNoSuggestion;
    CheckBox mNoSuggestionCheckBox;
    RelativeLayout mMuteButton;
    CheckBox mMuteButtonCheckBox;
    RelativeLayout mSRSIndication;
    CheckBox mSRSIndicationCheckBox;
    RelativeLayout mHWAccel;
    CheckBox mHWAccelCheckBox;
    TextView mDeveloperOpenSourceLicenses;
    TextView mDeveloperAppVersionSummary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefMan = new PrefManager(this);

        mApiKey = (TextView) findViewById(R.id.settings_api_key);
        mCustomFonts = (RelativeLayout) findViewById(R.id.settings_general_use_custom_fonts);
        mCustomFontsCheckBox = (CheckBox) findViewById(R.id.settings_general_use_custom_fonts_check_box);
        mGeneralUseSpecificDates = (RelativeLayout) findViewById(R.id.settings_general_use_specific_dates);
        mGeneralUseSpecificDatesCheckBox = (CheckBox) findViewById(R.id.settings_general_use_specific_dates_check_box);
        mDashboardRecentUnlocksNumber = (LinearLayout) findViewById(R.id.settings_dashboard_recent_unlocks_number);
        mDashboardCriticalItemsPercentage = (LinearLayout) findViewById(R.id.settings_dashboard_critical_items_percentage);
        mCriticalItemsNumber = (LinearLayout) findViewById(R.id.settings_dashboard_critical_items_number);
        mReviewsLessonsFullscreen = (RelativeLayout) findViewById(R.id.settings_rev_les_fullscreen);
        mReviewsLessonsFullscreenCheckBox = (CheckBox) findViewById(R.id.settings_rev_les_fullscreen_check_box);
        mReviewImprovements = (RelativeLayout) findViewById(R.id.settings_userscripts_review_improvements);
        mReviewImprovementsCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_review_improvements_check_box);
        mIgnoreButton = (RelativeLayout) findViewById(R.id.settings_userscripts_ignore_button);
        mIgnoreButtonCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_ignore_button_check_box);
        mSingleButton = (RelativeLayout) findViewById(R.id.settings_userscripts_single_button);
        mSingleButtonCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_single_button_check_box);
        mPortraitMode = (RelativeLayout) findViewById(R.id.settings_userscripts_portrait_mode);
        mPortraitModeCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_portrait_mode_check_box);
        mWaniKaniImprove = (RelativeLayout) findViewById(R.id.settings_userscripts_wanikani_improve);
        mWaniKaniImproveCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_wanikani_improve_check_box);
        mReviewOrder = (RelativeLayout) findViewById(R.id.settings_userscripts_review_order);
        mReviewOrderCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_review_order_check_box);
        mLessonOrder = (RelativeLayout) findViewById(R.id.settings_userscripts_lesson_order);
        mLessonOrderCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_lesson_order_check_box);
        mExternalFramePlacer = (RelativeLayout) findViewById(R.id.settings_userscripts_external_frame_placer);
        mExternalFramePlacerCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_external_frame_placer_check_box);
        mExternalFramePlacerDictionary = (TextView) findViewById(R.id.settings_userscripts_external_frame_placer_dictionary);
        mPartOfSpeech = (RelativeLayout) findViewById(R.id.settings_userscripts_part_of_speech);
        mPartOfSpeechCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_part_of_speech_check_box);
        mAutoPopup = (RelativeLayout) findViewById(R.id.settings_userscripts_auto_popup);
        mAutoPopupCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_auto_popup_check_box);
        mMistakeDelay = (RelativeLayout) findViewById(R.id.settings_userscripts_mistake_delay);
        mMistakeDelayCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_mistake_delay_check_box);
        mRomaji = (RelativeLayout) findViewById(R.id.settings_userscripts_romaji);
        mRomajiCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_romaji_check_box);
        mNoSuggestion = (RelativeLayout) findViewById(R.id.settings_userscripts_no_suggestions);
        mNoSuggestionCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_no_suggestions_check_box);
        mMuteButton = (RelativeLayout) findViewById(R.id.settings_userscripts_mute_button);
        mMuteButtonCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_mute_button_check_box);
        mSRSIndication = (RelativeLayout) findViewById(R.id.settings_userscripts_srs_indication);
        mSRSIndicationCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_srs_indication_check_box);
        mHWAccel = (RelativeLayout) findViewById(R.id.settings_userscripts_hw_accel);
        mHWAccelCheckBox = (CheckBox) findViewById(R.id.settings_userscripts_hw_accel_check_box);
        mDeveloperOpenSourceLicenses = (TextView) findViewById(R.id.settings_developer_open_source_licenses);
        mDeveloperAppVersionSummary = (TextView) findViewById(R.id.settings_developer_app_version_summary);

        mCustomFontsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setUseCUstomFonts(isChecked);
            }
        });

        mGeneralUseSpecificDatesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setUseSpecificDates(isChecked);
            }
        });

        mReviewsLessonsFullscreenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setReviewsLessonsFullscreen(isChecked);
            }
        });

        mReviewImprovementsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setReviewsImprovements(isChecked);
                mIgnoreButton.setEnabled(isChecked);
                mIgnoreButtonCheckBox.setEnabled(isChecked);
                mWaniKaniImprove.setEnabled(isChecked);
                mWaniKaniImproveCheckBox.setEnabled(isChecked);
                mReviewOrder.setEnabled(isChecked);
                mReviewOrderCheckBox.setEnabled(isChecked);
                mLessonOrder.setEnabled(isChecked);
                mLessonOrderCheckBox.setEnabled(isChecked);
                mAutoPopup.setEnabled(isChecked);
                mAutoPopupCheckBox.setEnabled(isChecked);
                mMistakeDelay.setEnabled(isChecked);
                mMistakeDelayCheckBox.setEnabled(isChecked);
                mNoSuggestion.setEnabled(isChecked);
                mNoSuggestionCheckBox.setEnabled(isChecked);
            }
        });

        mIgnoreButtonCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setIgnoreButton(isChecked);
            }
        });

        mSingleButtonCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setSingleButton(isChecked);
            }
        });

        mPortraitModeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setPortraitMode(isChecked);
            }
        });

        mWaniKaniImproveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setWaniKaniImprove(isChecked);
            }
        });

        mReviewOrderCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setReviewOrder(isChecked);
            }
        });

        mLessonOrderCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setLessonOrder(isChecked);
            }
        });

        mExternalFramePlacerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setExternalFramePlacer(isChecked);
                mExternalFramePlacerDictionary.setEnabled(isChecked);
            }
        });

        mPartOfSpeechCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setPartOfSpeech(isChecked);
            }
        });

        mAutoPopupCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setAutoPopup(isChecked);
            }
        });

        mMistakeDelayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setMistakeDelay(isChecked);
            }
        });

        mRomajiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setRomaji(isChecked);
            }
        });

        mNoSuggestionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setNoSuggestion(isChecked);
            }
        });

        mMuteButtonCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setMuteButton(isChecked);
            }
        });

        mSRSIndicationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setSRSIndication(isChecked);
            }
        });

        mHWAccelCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setHWAccel(isChecked);
            }
        });
        setUp();
    }

    public void setUp() {
        loadPreferences();
        setOnClickListeners();
    }

    private void loadPreferences() {
        loadApiKey();
        loadUseCustomFonts();
        loadGeneralUseSpecificDates();
        loadReviewsImprovements();
        loadReviewsLessonsFullscreen();
        loadIgnoreButton();
        loadSingleButton();
        loadPortraitMode();
        loadWaniKaniImprove();
        loadReviewOrder();
        loadLessonOrder();
        loadExternalFramePlacer();
        loadPartOfSpeech();
        loadAutoPopup();
        loadMistakeDelay();
        loadRomaji();
        loadNoSuggestion();
        loadMuteButton();
        loadSRSIndication();
        loadHWAccel();
        loadAppVersionSummary();
    }

    public void setOnClickListeners() {
        mCustomFonts.setOnClickListener(this);
        mGeneralUseSpecificDates.setOnClickListener(this);
        mDashboardRecentUnlocksNumber.setOnClickListener(this);
        mDashboardCriticalItemsPercentage.setOnClickListener(this);
        mCriticalItemsNumber.setOnClickListener(this);
        mReviewImprovements.setOnClickListener(this);
        mReviewsLessonsFullscreen.setOnClickListener(this);
        mIgnoreButton.setOnClickListener(this);
        mSingleButton.setOnClickListener(this);
        mPortraitMode.setOnClickListener(this);
        mWaniKaniImprove.setOnClickListener(this);
        mReviewOrder.setOnClickListener(this);
        mLessonOrder.setOnClickListener(this);
        mExternalFramePlacer.setOnClickListener(this);
        mExternalFramePlacerDictionary.setOnClickListener(this);
        mPartOfSpeech.setOnClickListener(this);
        mAutoPopup.setOnClickListener(this);
        mMistakeDelay.setOnClickListener(this);
        mRomaji.setOnClickListener(this);
        mNoSuggestion.setOnClickListener(this);
        mMuteButton.setOnClickListener(this);
        mSRSIndication.setOnClickListener(this);
        mHWAccel.setOnClickListener(this);
        mDeveloperOpenSourceLicenses.setOnClickListener(this);
    }

    private void loadApiKey() {
        String apiKey = prefMan.getApiKey();
        String maskedApiKey = "************************";

        for (int i = 25; i < apiKey.length(); i++) {
            maskedApiKey += apiKey.charAt(i);
        }

        mApiKey.setText(maskedApiKey);
    }

    private void loadUseCustomFonts() {
        mCustomFontsCheckBox.setChecked(prefMan.isUseCustomFonts());
    }

    private void loadGeneralUseSpecificDates() {
        mGeneralUseSpecificDatesCheckBox.setChecked(prefMan.isUseSpecificDates());
    }

    private void loadReviewsImprovements() {
        mReviewImprovementsCheckBox.setChecked(prefMan.getReviewsImprovements());
    }

    private void loadReviewsLessonsFullscreen() {
        mReviewsLessonsFullscreenCheckBox.setChecked(prefMan.getReviewsLessonsFullscreen());
    }

    private void loadIgnoreButton() {
        mIgnoreButtonCheckBox.setChecked(prefMan.getIgnoreButton());
    }

    private void loadSingleButton() {
        mSingleButtonCheckBox.setChecked(prefMan.getSingleButton());
    }

    private void loadPortraitMode() {
        mPortraitModeCheckBox.setChecked(prefMan.getPortraitMode());
    }

    private void loadWaniKaniImprove() {
        mWaniKaniImproveCheckBox.setChecked(prefMan.getWaniKaniImprove());
    }

    private void loadReviewOrder() {
        mReviewOrderCheckBox.setChecked(prefMan.getReviewOrder());
    }

    private void loadLessonOrder() {
        mLessonOrderCheckBox.setChecked(prefMan.getLessonOrder());
    }

    private void loadExternalFramePlacer() {
        mExternalFramePlacerCheckBox.setChecked(prefMan.getExternalFramePlacer());
    }

    private void loadPartOfSpeech() {
        mPartOfSpeechCheckBox.setChecked(prefMan.getPartOfSpeech());
    }

    private void loadAutoPopup() {
        mAutoPopupCheckBox.setChecked(prefMan.getAutoPopup());
    }

    private void loadMistakeDelay() {
        mMistakeDelayCheckBox.setChecked(prefMan.getMistakeDelay());
    }

    private void loadRomaji() {
        mRomajiCheckBox.setChecked(prefMan.getRomaji());
    }

    private void loadNoSuggestion() {
        mNoSuggestionCheckBox.setChecked(prefMan.getNoSuggestion());
    }

    private void loadMuteButton() {
        mMuteButtonCheckBox.setChecked(prefMan.getMuteButton());
    }

    private void loadSRSIndication() {
        mSRSIndicationCheckBox.setChecked(prefMan.getSRSIndication());
    }

    private void loadHWAccel() {
        mHWAccelCheckBox.setChecked(prefMan.getHWAccel());
    }

    private void loadAppVersionSummary() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mDeveloperAppVersionSummary.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= 16) {
            super.onNavigateUp();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_general_use_custom_fonts:
                mCustomFontsCheckBox.toggle();
                break;
            case R.id.settings_general_use_specific_dates:
                mGeneralUseSpecificDatesCheckBox.toggle();
                break;
            case R.id.settings_dashboard_recent_unlocks_number:
                new RecentUnlocksNumberPreference().show(getFragmentManager(),
                        "recent-unlocks-numbers-preference");
                break;
            case R.id.settings_dashboard_critical_items_percentage:
                new CriticalItemsPercentagePreference().show(getFragmentManager(),
                        "critical-items-percentage-preference");
                break;
            case R.id.settings_dashboard_critical_items_number:
                new CriticalItemsNumberPreference().show(getFragmentManager(),
                        "critical-items-number-preference");
                break;
            case R.id.settings_userscripts_review_improvements:
                mReviewImprovementsCheckBox.toggle();
                break;
            case R.id.settings_rev_les_fullscreen:
                mReviewsLessonsFullscreenCheckBox.toggle();
                break;
            case R.id.settings_userscripts_ignore_button:
                mIgnoreButtonCheckBox.toggle();
                break;
            case R.id.settings_userscripts_single_button:
                mSingleButtonCheckBox.toggle();
                break;
            case R.id.settings_userscripts_portrait_mode:
                mPortraitModeCheckBox.toggle();
                break;
            case R.id.settings_userscripts_wanikani_improve:
                mWaniKaniImproveCheckBox.toggle();
                break;
            case R.id.settings_userscripts_review_order:
                mReviewOrderCheckBox.toggle();
                break;
            case R.id.settings_userscripts_lesson_order:
                mLessonOrderCheckBox.toggle();
                break;
            case R.id.settings_userscripts_external_frame_placer:
                mExternalFramePlacerCheckBox.toggle();
                break;
            case R.id.settings_userscripts_external_frame_placer_dictionary:
                // TODO - Dictionary chooser
                break;
            case R.id.settings_userscripts_part_of_speech:
                mPartOfSpeechCheckBox.toggle();
                break;
            case R.id.settings_userscripts_auto_popup:
                mAutoPopupCheckBox.toggle();
                break;
            case R.id.settings_userscripts_mistake_delay:
                mMistakeDelayCheckBox.toggle();
                break;
            case R.id.settings_userscripts_romaji:
                mRomajiCheckBox.toggle();
                break;
            case R.id.settings_userscripts_no_suggestions:
                mNoSuggestionCheckBox.toggle();
                break;
            case R.id.settings_userscripts_mute_button:
                mMuteButtonCheckBox.toggle();
                break;
            case R.id.settings_userscripts_srs_indication:
                mSRSIndicationCheckBox.toggle();
                break;
            case R.id.settings_userscripts_hw_accel:
                mHWAccelCheckBox.toggle();
                break;
            case R.id.settings_developer_open_source_licenses:
                new OpenSourceLicensesDialogFragment().show(getSupportFragmentManager(),
                        "open-source-licenses-preference-dialog");
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}