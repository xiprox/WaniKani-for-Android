package tr.xip.wanikani;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.managers.PrefManager;

public class FirstTimeActivity extends ActionBarActivity {

    WaniKaniApi api;
    PrefManager prefMan;

    EditText mApiKey;
    Button mHowTo;
    Button mSignIn;

    ViewSwitcher mViewSwitcher;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        context = getApplicationContext();

        api = new WaniKaniApi(getApplicationContext());
        prefMan = new PrefManager(getApplicationContext());

        mApiKey = (EditText) findViewById(R.id.first_time_api_key);
        mHowTo = (Button) findViewById(R.id.first_time_how_to_api_key);
        mSignIn = (Button) findViewById(R.id.first_time_sign_in);

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.firt_time_view_switcher);
        mViewSwitcher.setInAnimation(context, R.anim.abc_fade_in);
        mViewSwitcher.setOutAnimation(context, R.anim.abc_fade_out);

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mApiKey.getText().toString())) {
                    Toast.makeText(context, R.string.error_enter_api_key, Toast.LENGTH_SHORT).show();
                } else {
                    if (mViewSwitcher.getDisplayedChild() == 0) {
                        mViewSwitcher.showNext();
                    }
                    new SignInTask().execute(mApiKey.getText().toString());
                }
            }
        });
    }

    private class SignInTask extends AsyncTask<String, Void, String> {
        String key;

        @Override
        protected String doInBackground(String... params) {
            key = params[0];

            try {
                if (api.checkAPIKey(key).user_information != null) {
                    return "success";
                } else {
                    return "no_user";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("success")) {
                prefMan.setApiKey(key);
                prefMan.setFirstLaunch(false);
                startActivity(new Intent(context, MainActivity.class));
                finish();
            } else if (result.equals("no_user")) {
                if (mViewSwitcher.getDisplayedChild() == 1) {
                    mViewSwitcher.showPrevious();
                }
                Toast.makeText(context, R.string.error_invalid_api_key, Toast.LENGTH_SHORT).show();
            } else {
                if (mViewSwitcher.getDisplayedChild() == 1) {
                    mViewSwitcher.showPrevious();
                }
                Toast.makeText(context, R.string.error_unknown_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}