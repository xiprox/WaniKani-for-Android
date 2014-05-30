package tr.xip.wanikani;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.managers.ThemeManager;

public class FirstTimeActivity extends ActionBarActivity {

    WaniKaniApi api;
    ThemeManager themeMan;
    PrefManager prefMan;

    EditText mApiKey;
    Button mHowTo;
    Button mSignIn;

    ViewSwitcher mViewSwitcher;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        api = new WaniKaniApi(getApplicationContext());
        prefMan = new PrefManager(getApplicationContext());
        themeMan = new ThemeManager(this);

        setTheme(themeMan.getTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        mApiKey = (EditText) findViewById(R.id.first_time_api_key);
        mHowTo = (Button) findViewById(R.id.first_time_how_to_api_key);
        mSignIn = (Button) findViewById(R.id.first_time_sign_in);

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.firt_time_view_switcher);
        mViewSwitcher.setInAnimation(context, R.anim.abc_fade_in);
        mViewSwitcher.setOutAnimation(context, R.anim.abc_fade_out);

        mHowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(FirstTimeActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_how_to_get_key, null);

                TextView linkTextView = (TextView) dialogView.findViewById(R.id.wanikani_go_link_text);
                linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

                AlertDialog.Builder builder = new AlertDialog.Builder(FirstTimeActivity.this);
                builder.setTitle(R.string.action_how_to_api_key)
                        .setView(dialogView)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                builder.create();
                builder.show();
            }
        });

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
                if (api.isApiKeyValid(key)) {
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