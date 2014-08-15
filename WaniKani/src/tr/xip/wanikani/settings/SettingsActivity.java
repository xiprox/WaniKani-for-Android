package tr.xip.wanikani.settings;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tr.xip.wanikani.R;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.settings.preferences.CriticalItemsPercentagePreference;
import tr.xip.wanikani.settings.preferences.FontsPreference;
import tr.xip.wanikani.settings.preferences.LessonsScreenOrientationPreference;
import tr.xip.wanikani.settings.preferences.RecentUnlocksNumberPreference;
import tr.xip.wanikani.settings.preferences.ReviewsScreenOrientationPreference;

/**
 * Created by xihsa_000 on 4/4/14.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener,
        ReviewsScreenOrientationPreference.ReviewsScreenOrientationPreferenceListener,
        LessonsScreenOrientationPreference.LessonsScreenOrientationPreferenceListener {

    PrefManager prefMan;

    TextView mApiKey;
    TextView mGeneralFonts;
    RelativeLayout mGeneralUseSpecificDates;
    CheckBox mGeneralUseSpecificDatesCheckBox;
    LinearLayout mDashboardRecentUnlocksNumber;
    LinearLayout mDashboardCriticalItemsPercentage;
    LinearLayout mLessonsScreenOrientation;
    TextView mLessonsScreenOrientationSummary;
    LinearLayout mReviewsScreenOrientation;
    TextView mReviewsScreenOrientationSummary;

    ViewGroup mActionBarLayout;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefMan = new PrefManager(this);

        mApiKey = (TextView) findViewById(R.id.settings_api_key);
        mGeneralFonts = (TextView) findViewById(R.id.settings_general_fonts);
        mGeneralUseSpecificDates = (RelativeLayout) findViewById(R.id.settings_general_use_specific_dates);
        mGeneralUseSpecificDatesCheckBox = (CheckBox) findViewById(R.id.settings_general_use_specific_dates_check_box);
        mDashboardRecentUnlocksNumber = (LinearLayout) findViewById(R.id.settings_dashboard_recent_unlocks_number);
        mDashboardCriticalItemsPercentage = (LinearLayout) findViewById(R.id.settings_dashboard_critical_items_percentage);
        mLessonsScreenOrientation = (LinearLayout) findViewById(R.id.settings_lessons_screen_orientation);
        mLessonsScreenOrientationSummary = (TextView) findViewById(R.id.settings_lessons_screen_orientation_summary);
        mReviewsScreenOrientation = (LinearLayout) findViewById(R.id.settings_reviews_screen_orientation);
        mReviewsScreenOrientationSummary = (TextView) findViewById(R.id.settings_reviews_screen_orientation_summary);

        mGeneralUseSpecificDatesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                prefMan.setUseSpecificDates(isChecked);
            }
        });

        setUp();
    }

    public void setUp() {
        setUpActionBar();
        loadPreferences();
        setOnClickListeners();
    }

    public void setUpActionBar() {
        mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_main, null);

        mActionBarIcon = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_icon);
        mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setCustomView(mActionBarLayout);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setIcon(android.R.color.transparent);
        mActionBar.setHomeAsUpIndicator(android.R.color.transparent);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setHomeButtonEnabled(false);

        mActionBarTitle.setText(R.string.title_settings);

        mActionBarIcon.setImageResource(R.drawable.ic_action_back);
        mActionBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadPreferences() {
        loadApiKey();
        loadGeneralUseSpecificDates();
        loadLessonsScreenOrientation();
        loadReviewsScreenOrientation();
    }

    public void setOnClickListeners() {
        mGeneralFonts.setOnClickListener(this);
        mGeneralUseSpecificDates.setOnClickListener(this);
        mDashboardRecentUnlocksNumber.setOnClickListener(this);
        mDashboardCriticalItemsPercentage.setOnClickListener(this);
        mLessonsScreenOrientation.setOnClickListener(this);
        mReviewsScreenOrientation.setOnClickListener(this);
    }

    private void loadApiKey() {
        String apiKey = prefMan.getApiKey();
        String maskedApiKey = "************************";

        for (int i = 25; i < apiKey.length(); i++) {
            maskedApiKey += apiKey.charAt(i);
        }

        mApiKey.setText(maskedApiKey);
    }

    private void loadGeneralUseSpecificDates() {
        mGeneralUseSpecificDatesCheckBox.setChecked(prefMan.isUseSpecificDates());
    }

    private void loadLessonsScreenOrientation() {
        mLessonsScreenOrientationSummary.setText(prefMan.getLessonsScreenOrientation());
    }

    private void loadReviewsScreenOrientation() {
        mReviewsScreenOrientationSummary.setText(prefMan.getReviewsScreenOrientation());
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
            case R.id.settings_general_fonts:
                new FontsPreference().show(getSupportFragmentManager(), "fonts-preference");
                break;
            case R.id.settings_general_use_specific_dates:
                mGeneralUseSpecificDatesCheckBox.toggle();
                break;
            case R.id.settings_dashboard_recent_unlocks_number:
                new RecentUnlocksNumberPreference().show(getSupportFragmentManager(),
                        "recent-unlocks-numbers-preference");
                break;
            case R.id.settings_dashboard_critical_items_percentage:
                new CriticalItemsPercentagePreference().show(getSupportFragmentManager(),
                        "critical-items-percentage-preference");
                break;
            case R.id.settings_lessons_screen_orientation:
                new LessonsScreenOrientationPreference().show(getSupportFragmentManager(),
                        "lessons-screen-orientation-preference");
                break;
            case R.id.settings_reviews_screen_orientation:
                new ReviewsScreenOrientationPreference().show(getSupportFragmentManager(),
                        "reviews-screen-orientation-preference");

        }
    }

    @Override
    public void onReviewsScreenOrientationPreferenceChanged(String preference) {
        mReviewsScreenOrientationSummary.setText(preference);
    }

    @Override
    public void onLessonsScreenOrientationPreferenceChanged(String preference) {
        mLessonsScreenOrientationSummary.setText(preference);
    }
}