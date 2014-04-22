package tr.xip.wanikani.cards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import tr.xip.wanikani.R;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.LevelProgression;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class ProgressCard extends Fragment {

    WaniKaniApi api;
    PrefManager prefMan;
    OfflineDataManager dataMan;
    Utils utils;
    ThemeManager themeMan;

    View rootView;

    Context context;

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

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new LoadTask().execute();
        }
    };

    @Override
    public void onCreate(Bundle state) {
        api = new WaniKaniApi(getActivity());
        prefMan = new PrefManager(getActivity());
        dataMan = new OfflineDataManager(getActivity());
        utils = new Utils(getActivity());
        themeMan = new ThemeManager(getActivity());
        super.onCreate(state);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
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
        mCard.setBackgroundResource(themeMan.getCard());

        setOldValues();

        return rootView;
    }

    private void setOldValues() {
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

    private class LoadTask extends AsyncTask<String, Void, String> {
        User user;
        LevelProgression progression;
        int userLevel;
        int radicalPercentage;
        int radicalProgress;
        int radicalTotal;
        int kanjiPercentage;
        int kanjiProgress;
        int kanjiTotal;

        @Override
        protected String doInBackground(String... strings) {
            try {
                user = api.getUser();
                progression = api.getLevelProgression();

                userLevel = user.getLevel(context);
                radicalProgress = progression.getRadicalsProgress(context);
                radicalTotal = progression.getRadicalsTotal(context);
                kanjiProgress = progression.getKanjiProgress(context);
                kanjiTotal = progression.getKanjiTotal(context);
                radicalPercentage = progression.getRadicalsPercentage(context);
                kanjiPercentage = progression.getKanjiPercentage(context);
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
            }

            Intent intent = new Intent(BroadcastIntents.FINISHED_SYNC_PROGRESS_CARD());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
    }
}
