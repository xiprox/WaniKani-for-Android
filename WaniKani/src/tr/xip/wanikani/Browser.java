package tr.xip.wanikani;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.Fonts;

/**
 * Created by xihsa_000 on 3/15/14.
 */
public class Browser extends Activity {

    public static final String ARG_ACTION = "action";
    public static final String ARG_ITEM = "item";
    public static final String ARG_ITEM_TYPE = "itemtype";

    public static final String ACTION_ITEM_DETAILS = "itemdetails";
    public static final String ACTION_LESSON = "lesson";
    public static final String ACTION_REVIEW = "reviews";
    public static final String ACTION_ACCOUNT_SETTINGS = "account_settings";

    public static final String WANIKANI_BASE_URL = "https://www.wanikani.com";
    public static final String LESSON_URL = WANIKANI_BASE_URL + "/lesson/session";
    public static final String REVIEW_URL = WANIKANI_BASE_URL + "/review/session";
    public static final String RADICAL_URL = WANIKANI_BASE_URL + "/radicals/";
    public static final String KANJI_URL = WANIKANI_BASE_URL + "/kanji/";
    public static final String VOCABULARY_URL = WANIKANI_BASE_URL + "/vocabulary/";
    public static final String ACCOUNT_SETTINGS_URL = WANIKANI_BASE_URL + "/account";

    ViewGroup mActionBarLayout;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;

    WebView mWebview;
    PrefManager prefMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        prefMan = new PrefManager(this);

        mWebview = (WebView) findViewById(R.id.browser_webview);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                    R.layout.actionbar_main, null);

            mActionBarIcon = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_icon);
            mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);

            ActionBar mActionBar = getActionBar();
            mActionBar.setCustomView(mActionBarLayout);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setIcon(android.R.color.transparent);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setHomeButtonEnabled(false);
            if (Build.VERSION.SDK_INT >= 18)
                mActionBar.setHomeAsUpIndicator(android.R.color.transparent);

            mActionBarIcon.setImageResource(R.drawable.ic_action_back);
            mActionBarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        mWebview.getSettings().setJavaScriptEnabled(true);

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("WANIKANI BROWSER", "Error code: " + errorCode + "; Description: " + description + "; Url: " + failingUrl);
            }
        });

        Intent intent = getIntent();
        String action = intent.getStringExtra(ARG_ACTION);
        String itemType = intent.getStringExtra(ARG_ITEM_TYPE);
        String item = intent.getStringExtra(ARG_ITEM);

        if (action.equals(ACTION_LESSON)) {
            /* Lessons have moved to WebReviewActivity */
        }
        if (action.equals(ACTION_REVIEW)) {
            /* Reviews have moved to WebReviewActivity */
        }

        if (action.equals(ACTION_ITEM_DETAILS)) {
            if (itemType.equals(ItemDetailsActivity.TYPE_RADICAL)) {
                mWebview.loadUrl(RADICAL_URL + WordUtils.uncapitalize(item));
                setActionBarTitle(item, false);
            }
            if (itemType.equals(ItemDetailsActivity.TYPE_KANJI)) {
                mWebview.loadUrl(KANJI_URL + item);
                setActionBarTitle(item, true);
            }
            if (itemType.equals(ItemDetailsActivity.TYPE_VOCABULARY)) {
                mWebview.loadUrl(VOCABULARY_URL + item);
                setActionBarTitle(item, true);
            }
        }

        if (action.equals(ACTION_ACCOUNT_SETTINGS)) {
            mWebview.loadUrl(ACCOUNT_SETTINGS_URL);
            setActionBarTitle(getString(R.string.title_account_settings), false);
        }
    }

    private void setActionBarTitle(String title, boolean setTypeface) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            if (mActionBarTitle != null) {
                mActionBarTitle.setText(title);

                if (setTypeface)
                    mActionBarTitle.setTypeface(new Fonts().getKanjiFont(this));
            }
        } else
            getActionBar().setTitle(title);
    }

    private void setActionBarIcon(int res) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            if (mActionBarIcon != null)
                mActionBarIcon.setImageResource(res);
        } else
            getActionBar().setIcon(res);
    }

    private void setOrientation(String orientation) {
        if (orientation.equals("Portrait"))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (orientation.equals("Landscape"))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public boolean onNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
