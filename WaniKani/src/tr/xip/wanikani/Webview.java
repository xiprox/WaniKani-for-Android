package tr.xip.wanikani;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by xihsa_000 on 3/15/14.
 */
public class Webview extends ActionBarActivity {

    WebView mWebview;

    String action;

    String lessonUrl = "https://www.wanikani.com/lesson";
    String reviewUrl = "https://www.wanikani.com/review";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebview = new WebView(this);
        setContentView(mWebview);

        mWebview.getSettings().setJavaScriptEnabled(true);

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_LONG).show();
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        if (action.equals("Lesson")) {
            mWebview.loadUrl(lessonUrl);
        }
        if (action.equals("Review")) {
            mWebview.loadUrl(reviewUrl);
        }

        getSupportActionBar().setTitle(action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
