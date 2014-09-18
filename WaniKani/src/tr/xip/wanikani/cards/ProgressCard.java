package tr.xip.wanikani.cards;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import tr.xip.wanikani.BroadcastIntents;
import tr.xip.wanikani.DashboardFragment;
import tr.xip.wanikani.R;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.LevelProgression;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class ProgressCard extends Fragment {

    WaniKaniApi api;
    PrefManager prefMan;
    OfflineDataManager dataMan;
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

    int userLevel;
    int radicalPercentage;
    int radicalProgress;
    int radicalTotal;
    int kanjiPercentage;
    int kanjiProgress;
    int kanjiTotal;

    public void setListener(ProgressCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= 11)
                new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new LoadTask().execute();
        }
    };

    @Override
    public void onCreate(Bundle state) {
        api = new WaniKaniApi(getActivity());
        prefMan = new PrefManager(getActivity());
        dataMan = new OfflineDataManager(getActivity());
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

        loadOfflineValues();

        return rootView;
    }

    public void loadOfflineValues() {
        mUserLevel.setText(dataMan.getLevel() + "");
        mRadicalsProgress.setText(dataMan.getRadicalsProgress() + "");
        mRadicalsTotal.setText(dataMan.getRadicalsProgress() + "");
        mKanjiProgress.setText(dataMan.getKanjiProgress() + "");
        mKanjiTotal.setText(dataMan.getKanjiTotal() + "");

        mRadicalPercentage.setText(dataMan.getRadicalsPercentage() + "");
        mKanjiPercentage.setText(dataMan.getKanjiPercentage() + "");

        mRadicalProgressBar.setProgress(dataMan.getRadicalsPercentage());
        mKanjiProgressBar.setProgress(dataMan.getKanjiPercentage());
    }

    private void saveOfflineValues() {
        dataMan.setLevel(userLevel);
        dataMan.setRadicalsPercentage(radicalPercentage);
        dataMan.setRadicalsProgress(radicalProgress);
        dataMan.setRadicalsTotal(radicalTotal);
        dataMan.setKanjiPercentage(kanjiPercentage);
        dataMan.setKanjiProgress(kanjiProgress);
        dataMan.setKanjiTotal(kanjiTotal);
    }

    public void load() {
        new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LoadTask extends AsyncTask<String, Void, String> {
        User user;
        LevelProgression progression;

        @Override
        protected String doInBackground(String... strings) {
            try {
                user = api.getUser();
                progression = api.getLevelProgression();

                userLevel = user.getLevel();
                radicalProgress = progression.getRadicalsProgress();
                radicalTotal = progression.getRadicalsTotal();
                kanjiProgress = progression.getKanjiProgress();
                kanjiTotal = progression.getKanjiTotal();
                radicalPercentage = progression.getRadicalsPercentage();
                kanjiPercentage = progression.getKanjiPercentage();
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                mUserLevel.setText(userLevel + "");
                mRadicalPercentage.setText(radicalPercentage + "");
                mRadicalsProgress.setText(radicalProgress + "");
                mRadicalsTotal.setText(radicalTotal + "");
                mKanjiPercentage.setText(kanjiPercentage + "");
                mKanjiProgress.setText(kanjiProgress + "");
                mKanjiTotal.setText(kanjiTotal + "");

                mRadicalProgressBar.setProgress(radicalPercentage);
                mKanjiProgressBar.setProgress(kanjiPercentage);

                mListener.onProgressCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);

                saveOfflineValues();
            } else
                mListener.onProgressCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
        }
    }

    public interface ProgressCardListener {
        public void onProgressCardSyncFinishedListener(String result);
    }
}
