package tr.xip.wanikani.cards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import tr.xip.wanikani.tasks.LevelProgressionGetTask;
import tr.xip.wanikani.tasks.callbacks.LevelProgressionGetTaskCallbacks;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class ProgressCard extends Fragment implements LevelProgressionGetTaskCallbacks {

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

    private void saveOfflineValues(LevelProgression progression) {
        dataMan.setLevel(progression.getUserInfo().getLevel());
        dataMan.setRadicalsPercentage(progression.getRadicalsPercentage());
        dataMan.setRadicalsProgress(progression.getRadicalsProgress());
        dataMan.setRadicalsTotal(progression.getRadicalsTotal());
        dataMan.setKanjiPercentage(progression.getKanjiPercentage());
        dataMan.setKanjiProgress(progression.getKanjiProgress());
        dataMan.setKanjiTotal(progression.getKanjiTotal());
    }

    public void load() {
        new LevelProgressionGetTask(context, ProgressCard.this).executeParallel();
    }

    @Override
    public void onLevelProgressionGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onLevelProgressionGetTaskPostExecute(LevelProgression progression) {
        if (progression != null) {
            mUserLevel.setText(progression.getUserInfo().getLevel() + "");
            mRadicalPercentage.setText(progression.getRadicalsPercentage() + "");
            mRadicalsProgress.setText(progression.getRadicalsProgress() + "");
            mRadicalsTotal.setText(progression.getRadicalsTotal() + "");
            mKanjiPercentage.setText(progression.getKanjiPercentage() + "");
            mKanjiProgress.setText(progression.getKanjiProgress() + "");
            mKanjiTotal.setText(progression.getKanjiTotal() + "");

            mRadicalProgressBar.setProgress(progression.getRadicalsPercentage());
            mKanjiProgressBar.setProgress(progression.getKanjiPercentage());

            mListener.onProgressCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);

            saveOfflineValues(progression);
        } else
            mListener.onProgressCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
    }

    public interface ProgressCardListener {
        public void onProgressCardSyncFinishedListener(String result);
    }
}
