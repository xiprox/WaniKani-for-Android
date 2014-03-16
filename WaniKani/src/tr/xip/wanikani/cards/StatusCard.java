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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import tr.xip.wanikani.BroadcastIntents;
import tr.xip.wanikani.R;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.managers.ApiManager;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class StatusCard extends Fragment {

    WaniKaniApi api;
    ApiManager apiMan;
    PrefManager prefMan;
    OfflineDataManager dataMan;
    Utils utils;

    View rootView;

    RelativeLayout mApprenticeParent;
    RelativeLayout mGuruParent;
    RelativeLayout mMasterParent;
    RelativeLayout mEnlightenedParent;
    RelativeLayout mBurnedParent;

    TextView mApprentice;
    TextView mGuru;
    TextView mMaster;
    TextView mEnlightened;
    TextView mBurned;

    ViewFlipper mDetailsFlipper;
    ViewFlipper mDetailsSuperFlipper;

    TextView mDetailsRadicals;
    TextView mDetailsKanji;
    TextView mDetailsVocabulary;
    TextView mDetailsTotal;

    ImageView mDetailsSRSLogo;
    TextView mDetailsSRSLevel;

    RelativeLayout mDetailsUp;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (utils.isNetworkAvailable()) {
                new LoadTask().execute();
            } else {
                Intent broadcastIntent = new Intent(BroadcastIntents.FINISHED_SYNC_STATUS_CARD());
                broadcastIntent.putExtra("action", "hide");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(broadcastIntent);
            }
        }
    };

    @Override
    public void onCreate(Bundle state) {
        api = new WaniKaniApi(getActivity());
        apiMan = new ApiManager(getActivity());
        prefMan = new PrefManager(getActivity());
        dataMan = new OfflineDataManager(getActivity());
        utils = new Utils(getActivity());
        super.onCreate(state);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_status, null);

        mApprenticeParent = (RelativeLayout) rootView.findViewById(R.id.card_status_apprentice_parent);
        mGuruParent = (RelativeLayout) rootView.findViewById(R.id.card_status_guru_parent);
        mMasterParent = (RelativeLayout) rootView.findViewById(R.id.card_status_master_parent);
        mEnlightenedParent = (RelativeLayout) rootView.findViewById(R.id.card_status_enlightened_parent);
        mBurnedParent = (RelativeLayout) rootView.findViewById(R.id.card_status_burned_parent);

        mApprentice = (TextView) rootView.findViewById(R.id.card_status_apprentice);
        mGuru = (TextView) rootView.findViewById(R.id.card_status_guru);
        mMaster = (TextView) rootView.findViewById(R.id.card_status_master);
        mEnlightened = (TextView) rootView.findViewById(R.id.card_status_enlightened);
        mBurned = (TextView) rootView.findViewById(R.id.card_status_burned);

        mDetailsFlipper = (ViewFlipper) rootView.findViewById(R.id.card_status_details_flipper);
        mDetailsFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mDetailsFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);

        mDetailsSuperFlipper = (ViewFlipper) rootView.findViewById(R.id.card_status_details_super_flipper);
        mDetailsSuperFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mDetailsSuperFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);

        mDetailsRadicals = (TextView) rootView.findViewById(R.id.card_status_details_radicals_count);
        mDetailsKanji = (TextView) rootView.findViewById(R.id.card_status_details_kanji_count);
        mDetailsVocabulary = (TextView) rootView.findViewById(R.id.card_status_details_vocabulary_count);
        mDetailsTotal = (TextView) rootView.findViewById(R.id.card_status_details_total_count);
        mDetailsSRSLogo = (ImageView) rootView.findViewById(R.id.card_status_details_srs_logo);
        mDetailsSRSLevel = (TextView) rootView.findViewById(R.id.card_status_details_srs_level);
        mDetailsUp = (RelativeLayout) rootView.findViewById(R.id.card_status_details_up_button);

        setOnClickListeners();

        setOldValues();

        return rootView;
    }

    private void setOldValues() {
        mApprentice.setText(dataMan.getApprenticeTotalCount() + "");
        mGuru.setText(dataMan.getGuruTotalCount() + "");
        mMaster.setText(dataMan.getMasterTotalCount() + "");
        mEnlightened.setText(dataMan.getEnlightenTotalCount() + "");
        mBurned.setText(dataMan.getBurnedTotalCount() + "");
    }

    private void setOnClickListeners() {
        mApprenticeParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DetailsLoadTask().execute("apprentice");
                if (mDetailsFlipper.getDisplayedChild() == 0) {
                    mDetailsFlipper.showNext();
                }
            }
        });

        mGuruParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DetailsLoadTask().execute("guru");
                if (mDetailsFlipper.getDisplayedChild() == 0) {
                    mDetailsFlipper.showNext();
                }
            }
        });

        mMasterParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DetailsLoadTask().execute("master");
                if (mDetailsFlipper.getDisplayedChild() == 0) {
                    mDetailsFlipper.showNext();
                }
            }
        });

        mEnlightenedParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DetailsLoadTask().execute("enlighten");
                if (mDetailsFlipper.getDisplayedChild() == 0) {
                    mDetailsFlipper.showNext();
                }
            }
        });

        mBurnedParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DetailsLoadTask().execute("burned");
                if (mDetailsFlipper.getDisplayedChild() == 0) {
                    mDetailsFlipper.showNext();
                }
            }
        });

        mDetailsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDetailsFlipper.getDisplayedChild() == 1) {
                    mDetailsFlipper.showPrevious();
                }
                if (mDetailsSuperFlipper.getDisplayedChild() == 1) {
                    mDetailsSuperFlipper.showPrevious();
                }
            }
        });
    }

    private class LoadTask extends AsyncTask<String, Void, String> {
        int apprentice;
        int guru;
        int master;
        int enlightened;
        int burned;

        @Override
        protected String doInBackground(String... strings) {
            try {
                apprentice = apiMan.getApprenticeTotalCount();
                guru = apiMan.getGuruTotalCount();
                master = apiMan.getMasterTotalCount();
                enlightened = apiMan.getEnlightenTotalCount();
                burned = apiMan.getBurnedTotalCount();
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                mApprentice.setText(apprentice + "");
                mGuru.setText(guru + "");
                mMaster.setText(master + "");
                mEnlightened.setText(enlightened + "");
                mBurned.setText(burned + "");
            }

            Intent intent = new Intent(BroadcastIntents.FINISHED_SYNC_STATUS_CARD());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
    }

    private class DetailsLoadTask extends AsyncTask<String, Void, String> {
        int radicals;
        int kanji;
        int vocabulary;
        int total;

        String srsLevel;

        @Override
        protected String doInBackground(String... strings) {
            srsLevel = strings[0];

            try {
                if (strings[0].equals("apprentice")) {
                    radicals = apiMan.getApprenticeRadicalsCount();
                    kanji = apiMan.getApprenticeKanjiCount();
                    vocabulary = apiMan.getApprenticeVocabularyCount();
                    total = apiMan.getApprenticeTotalCount();
                }
                if (strings[0].equals("guru")) {
                    radicals = apiMan.getGuruRadicalsCount();
                    kanji = apiMan.getGuruKanjiCount();
                    vocabulary = apiMan.getGuruVocabularyCount();
                    total = apiMan.getGuruTotalCount();
                }
                if (strings[0].equals("master")) {
                    radicals = apiMan.getMasterRadicalsCount();
                    kanji = apiMan.getMasterKanjiCount();
                    vocabulary = apiMan.getMasterVocabularyCount();
                    total = apiMan.getMasterTotalCount();
                }
                if (strings[0].equals("enlighten")) {
                    radicals = apiMan.getEnlightenRadicalsCount();
                    kanji = apiMan.getEnlightenKanjiCount();
                    vocabulary = apiMan.getEnlightenVocabularyCount();
                    total = apiMan.getEnlightenTotalCount();
                }
                if (strings[0].equals("burned")) {
                    radicals = apiMan.getBurnedRadicalsCount();
                    kanji = apiMan.getBurnedKanjiCount();
                    vocabulary = apiMan.getBurnedVocabularyCount();
                    total = apiMan.getBurnedTotalCount();
                }
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                mDetailsRadicals.setText(radicals + "");
                mDetailsKanji.setText(kanji + "");
                mDetailsVocabulary.setText(vocabulary + "");
                mDetailsTotal.setText(total + "");

                if (srsLevel.equals("apprentice")) {
                    mDetailsSRSLogo.setImageResource(R.drawable.apprentice);
                    mDetailsSRSLevel.setText(R.string.srs_title_apprentice);
                }
                if (srsLevel.equals("guru")) {
                    mDetailsSRSLogo.setImageResource(R.drawable.guru);
                    mDetailsSRSLevel.setText(R.string.srs_title_guru);
                }
                if (srsLevel.equals("master")) {
                    mDetailsSRSLogo.setImageResource(R.drawable.master);
                    mDetailsSRSLevel.setText(R.string.srs_title_master);
                }
                if (srsLevel.equals("enlighten")) {
                    mDetailsSRSLogo.setImageResource(R.drawable.enlighten);
                    mDetailsSRSLevel.setText(R.string.srs_title_enlightened);
                }
                if (srsLevel.equals("burned")) {
                    mDetailsSRSLogo.setImageResource(R.drawable.burned);
                    mDetailsSRSLevel.setText(R.string.srs_title_burned);
                }

                if (mDetailsSuperFlipper.getDisplayedChild() == 0) {
                    mDetailsSuperFlipper.showNext();
                }
            } else {
                if (mDetailsFlipper.getDisplayedChild() == 1) {
                    mDetailsFlipper.showPrevious();
                }
            }
        }
    }
}
