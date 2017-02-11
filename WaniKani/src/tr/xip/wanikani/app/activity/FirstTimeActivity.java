package tr.xip.wanikani.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.content.notification.NotificationScheduler;
import tr.xip.wanikani.dialogs.HowToGetKeyDialogFragment;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.User;

public class FirstTimeActivity extends ActionBarActivity {
    EditText mApiKey;
    Button mHowTo;
    Button mSignIn;

    ViewSwitcher mViewSwitcher;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        context = this;

        mApiKey = (EditText) findViewById(R.id.first_time_api_key);
        mHowTo = (Button) findViewById(R.id.first_time_how_to_api_key);
        mSignIn = (Button) findViewById(R.id.first_time_sign_in);

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.firt_time_view_switcher);
        mViewSwitcher.setInAnimation(context, R.anim.abc_fade_in);
        mViewSwitcher.setOutAnimation(context, R.anim.abc_fade_out);

        mHowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HowToGetKeyDialogFragment().show(getSupportFragmentManager(), "how-to-get-key");
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

                    WaniKaniApi.getUser(mApiKey.getText().toString()).enqueue(new ThroughDbCallback<Request<User>, User>() {
                        @Override
                        public void onResponse(Call<Request<User>> call, Response<Request<User>> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful() && response.body().user_information != null) {
                                PrefManager.setApiKey(mApiKey.getText().toString());
                                PrefManager.setFirstLaunch(false);
                                startActivity(new Intent(context, MainActivity.class));

                                // Set an alarm for notifications for the first time
                                new NotificationScheduler(context).schedule();

                                finish();
                            } else {
                                onFailure(call, null);
                            }
                        }

                        @Override
                        public void onFailure(Call<Request<User>> call, Throwable t) {
                            if (mViewSwitcher.getDisplayedChild() == 1) {
                                mViewSwitcher.showPrevious();
                            }
                            Toast.makeText(context, R.string.error_invalid_api_key, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}