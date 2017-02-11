package tr.xip.wanikani.app.fragment.card;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class ProgressCard extends Fragment {
    Utils utils;

    View rootView;

    Context context;

    ProgressCardListener mListener;

    TextView mUserLevel;
    TextView mRadicalPercentage;
    TextView mRadicalsProgress;
    TextView mRadicalsTotal;
    TextView mKanjiPercentage;
    TextView mKanjiProgress;
    TextView mKanjiTotal;

    ProgressBar mRadicalProgressBar;
    ProgressBar mKanjiProgressBar;

    LinearLayout mCard;

    public void setListener(ProgressCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            load();
        }
    };

    @Override
    public void onCreate(Bundle state) {
        utils = new Utils(getActivity());
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_progress, null);

        context = getActivity();

        mUserLevel = (TextView) rootView.findViewById(R.id.card_progress_level);
        mRadicalPercentage = (TextView) rootView.findViewById(R.id.card_progress_radicals_percentage);
        mRadicalsProgress = (TextView) rootView.findViewById(R.id.card_progress_radicals_progress);
        mRadicalsTotal = (TextView) rootView.findViewById(R.id.card_progress_radicals_total);
        mKanjiPercentage = (TextView) rootView.findViewById(R.id.card_progress_kanji_percentage);
        mKanjiProgress = (TextView) rootView.findViewById(R.id.card_progress_kanji_progress);
        mKanjiTotal = (TextView) rootView.findViewById(R.id.card_progress_kanji_total);

        mRadicalProgressBar = (ProgressBar) rootView.findViewById(R.id.card_progress_radicals_progress_bar);
        mKanjiProgressBar = (ProgressBar) rootView.findViewById(R.id.card_progress_kanji_progress_bar);

        mCard = (LinearLayout) rootView.findViewById(R.id.card_progress_card);

        return rootView;
    }

    public void load() {
        WaniKaniApi.getLevelProgression().enqueue(new ThroughDbCallback<Request<LevelProgression>, LevelProgression>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Request<LevelProgression>> call, Response<Request<LevelProgression>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body().requested_information != null && response.body().user_information != null) {
                    displayData(response.body().user_information, response.body().requested_information);
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<Request<LevelProgression>> call, Throwable t) {
                User user = DatabaseManager.getUser();
                LevelProgression progression = DatabaseManager.getLevelProgression();

                if (user != null && progression != null) {
                    displayData(user, progression);
                } else {
                    mListener.onProgressCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayData(User user, LevelProgression progression) {
        mUserLevel.setText(user.level + "");
        mRadicalPercentage.setText(progression.getRadicalsPercentage() + "");
        mRadicalsProgress.setText(progression.radicals_progress + "");
        mRadicalsTotal.setText(progression.radicals_total + "");
        mKanjiPercentage.setText(progression.getKanjiPercentage() + "");
        mKanjiProgress.setText(progression.kanji_progress + "");
        mKanjiTotal.setText(progression.kanji_total + "");

        mRadicalProgressBar.setProgress(progression.getRadicalsPercentage());
        mKanjiProgressBar.setProgress(progression.getKanjiPercentage());

        mListener.onProgressCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
    }

    public interface ProgressCardListener {
        public void onProgressCardSyncFinishedListener(String result);
    }
}
