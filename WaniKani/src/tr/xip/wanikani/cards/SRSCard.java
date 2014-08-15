package tr.xip.wanikani.cards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import tr.xip.wanikani.BroadcastIntents;
import tr.xip.wanikani.DashboardFragment;
import tr.xip.wanikani.R;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.SRSDistribution;
import tr.xip.wanikani.managers.OfflineDataManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class SRSCard extends Fragment {

    WaniKaniApi api;
    PrefManager prefMan;
    OfflineDataManager dataMan;
    Utils utils;

    View rootView;

    Context context;

    StatusCardListener mListener;

    LinearLayout mApprenticeParent;
    LinearLayout mGuruParent;
    LinearLayout mMasterParent;
    LinearLayout mEnlightenedParent;
    LinearLayout mBurnedParent;

    TextView mApprentice;
    TextView mGuru;
    TextView mMaster;
    TextView mEnlightened;
    TextView mBurned;
    TextView mTotalItems;

    ImageView mApprenticeIcon;
    ImageView mGuruIcon;
    ImageView mMasterIcon;
    ImageView mEnlightenedIcon;
    ImageView mBurnedIcon;

//    ViewFlipper mDetailsFlipper;
    ViewFlipper mDetailsSuperFlipper;

    TextView mDetailsRadicals;
    TextView mDetailsKanji;
    TextView mDetailsVocabulary;
    TextView mDetailsTotal;

    ImageView mDetailsSRSLogo;
    TextView mDetailsSRSLevel;

    RelativeLayout mDetailsUp;

    LinearLayout mMainContent;
    RelativeLayout mDetailsContent;

    LinearLayout mCard;

    SRSDistribution srs;

    int apprentice;
    int guru;
    int master;
    int enlightened;
    int burned;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= 11)
                new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new LoadTask().execute();
        }
    };

    public void setListener(StatusCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

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
        rootView = inflater.inflate(R.layout.card_srs, null);

        context = getActivity();

        mApprenticeParent = (LinearLayout) rootView.findViewById(R.id.card_status_apprentice_parent);
        mGuruParent = (LinearLayout) rootView.findViewById(R.id.card_status_guru_parent);
        mMasterParent = (LinearLayout) rootView.findViewById(R.id.card_status_master_parent);
        mEnlightenedParent = (LinearLayout) rootView.findViewById(R.id.card_status_enlightened_parent);
        mBurnedParent = (LinearLayout) rootView.findViewById(R.id.card_status_burned_parent);

        mApprentice = (TextView) rootView.findViewById(R.id.card_status_apprentice);
        mGuru = (TextView) rootView.findViewById(R.id.card_status_guru);
        mMaster = (TextView) rootView.findViewById(R.id.card_status_master);
        mEnlightened = (TextView) rootView.findViewById(R.id.card_status_enlightened);
        mBurned = (TextView) rootView.findViewById(R.id.card_status_burned);
        mTotalItems = (TextView) rootView.findViewById(R.id.card_status_total_items);

        mApprenticeIcon = (ImageView) rootView.findViewById(R.id.card_status_apprentice_icon);
        mGuruIcon = (ImageView) rootView.findViewById(R.id.card_status_guru_icon);
        mMasterIcon = (ImageView) rootView.findViewById(R.id.card_status_master_icon);
        mEnlightenedIcon = (ImageView) rootView.findViewById(R.id.card_status_enlightened_icon);
        mBurnedIcon = (ImageView) rootView.findViewById(R.id.card_status_burned_icon);

        mApprenticeIcon.setColorFilter(getResources().getColor(R.color.apptheme_main),
                PorterDuff.Mode.SRC_ATOP);
        mGuruIcon.setColorFilter(getResources().getColor(R.color.apptheme_main),
                PorterDuff.Mode.SRC_ATOP);
        mMasterIcon.setColorFilter(getResources().getColor(R.color.apptheme_main),
                PorterDuff.Mode.SRC_ATOP);
        mEnlightenedIcon.setColorFilter(getResources().getColor(R.color.apptheme_main),
                PorterDuff.Mode.SRC_ATOP);
        mBurnedIcon.setColorFilter(getResources().getColor(R.color.apptheme_main),
                PorterDuff.Mode.SRC_ATOP);
/*

        mDetailsFlipper = (ViewFlipper) rootView.findViewById(R.id.card_status_details_flipper);
        mDetailsFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mDetailsFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);
*/

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

        mMainContent = (LinearLayout) rootView.findViewById(R.id.card_status_main_content);
        mDetailsContent = (RelativeLayout) rootView.findViewById(R.id.card_status_details_content);

        mCard = (LinearLayout) rootView.findViewById(R.id.card_status_card);

        setOnClickListeners();

        loadOfflineValues();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void loadOfflineValues() {
        mApprentice.setText(dataMan.getApprenticeTotalCount() + "");
        mGuru.setText(dataMan.getGuruTotalCount() + "");
        mMaster.setText(dataMan.getMasterTotalCount() + "");
        mEnlightened.setText(dataMan.getEnlightenTotalCount() + "");
        mBurned.setText(dataMan.getBurnedTotalCount() + "");

        mTotalItems.setText(dataMan.getApprenticeTotalCount()
                + dataMan.getGuruTotalCount()
                + dataMan.getMasterTotalCount()
                + dataMan.getEnlightenTotalCount()
                + dataMan.getBurnedTotalCount()
                + "");
    }

    private void saveOfflineValues() {
        dataMan.setApprenticeTotalCount(apprentice);
        dataMan.setGuruTotalCount(guru);
        dataMan.setMasterTotalCount(master);
        dataMan.setEnlightenTotalCount(enlightened);
        dataMan.setBurnedTotalCount(burned);
    }

    private void setOnClickListeners() {
        mApprenticeParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 11)
                    new DetailsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "apprentice");
                else
                    new DetailsLoadTask().execute("apprentice");

                switchToDetails();
            }
        });

        mGuruParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 11)
                    new DetailsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "guru");
                else
                    new DetailsLoadTask().execute("guru");

                switchToDetails();
            }
        });

        mMasterParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 11)
                    new DetailsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "master");
                else
                    new DetailsLoadTask().execute("master");

                switchToDetails();
            }
        });

        mEnlightenedParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 11)
                    new DetailsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "enlighten");
                else
                    new DetailsLoadTask().execute("enlighten");

                switchToDetails();
            }
        });

        mBurnedParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 11)
                    new DetailsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "burned");
                else
                    new DetailsLoadTask().execute("burned");

                switchToDetails();
            }
        });

        mDetailsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMain();
            }
        });
    }

    private void switchToDetails() {
//        if (mDetailsFlipper.getDisplayedChild() == 0)
//            mDetailsFlipper.showNext();
        mMainContent.setVisibility(View.GONE);
        mDetailsContent.setVisibility(View.VISIBLE);
    }

    private void switchToMain() {
//        if (mDetailsFlipper.getDisplayedChild() == 1)
//            mDetailsFlipper.showPrevious();
        if (mDetailsSuperFlipper.getDisplayedChild() == 1)
            mDetailsSuperFlipper.showPrevious();
        mMainContent.setVisibility(View.VISIBLE);
        mDetailsContent.setVisibility(View.GONE);
    }

    public interface StatusCardListener {
        public void onStatusCardSyncFinishedListener(String result);
    }

    private class LoadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                srs = api.getSRSDistribution();
                apprentice = srs.getAprentice().getTotalCount();
                guru = srs.getGuru().getTotalCount();
                master = srs.getMaster().getTotalCount();
                enlightened = srs.getEnlighten().getTotalCount();
                burned = srs.getBurned().getTotalCount();
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

                mTotalItems.setText(apprentice + guru + master + enlightened + burned + "");

                saveOfflineValues();

                mListener.onStatusCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
            } else
                mListener.onStatusCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
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
                    radicals = srs.getAprentice().getRadicalsCount();
                    kanji = srs.getAprentice().getKanjiCount();
                    vocabulary = srs.getAprentice().getVocabularyCount();
                    total = srs.getAprentice().getTotalCount();

                    dataMan.setApprenticeRadicalsCount(radicals);
                    dataMan.setApprenticeKanjiCount(kanji);
                    dataMan.setApprentiveVocabularyCount(vocabulary);
                }
                if (strings[0].equals("guru")) {
                    radicals = srs.getGuru().getRadicalsCount();
                    kanji = srs.getGuru().getKanjiCount();
                    vocabulary = srs.getGuru().getVocabularyCount();
                    total = srs.getGuru().getTotalCount();

                    dataMan.setGuruRadicalsCount(radicals);
                    dataMan.setGuruKanjiCount(kanji);
                    dataMan.setGuruVocabularyCount(vocabulary);
                }
                if (strings[0].equals("master")) {
                    radicals = srs.getMaster().getRadicalsCount();
                    kanji = srs.getMaster().getKanjiCount();
                    vocabulary = srs.getMaster().getVocabularyCount();
                    total = srs.getMaster().getTotalCount();

                    dataMan.setMasterRadicalsCount(radicals);
                    dataMan.setMasterKanjiCount(kanji);
                    dataMan.setMasterVocabularyCount(vocabulary);
                }
                if (strings[0].equals("enlighten")) {
                    radicals = srs.getEnlighten().getRadicalsCount();
                    kanji = srs.getEnlighten().getKanjiCount();
                    vocabulary = srs.getEnlighten().getVocabularyCount();
                    total = srs.getEnlighten().getTotalCount();

                    dataMan.setEnlightenRadicalsCount(radicals);
                    dataMan.setEnlightenKanjiCount(kanji);
                    dataMan.setEnlightenVocabularyCount(vocabulary);
                }
                if (strings[0].equals("burned")) {
                    radicals = srs.getBurned().getRadicalsCount();
                    kanji = srs.getBurned().getKanjiCount();
                    vocabulary = srs.getBurned().getVocabularyCount();
                    total = srs.getBurned().getTotalCount();

                    dataMan.setBurnedRadicalsCount(radicals);
                    dataMan.setBurnedKanjiCount(kanji);
                    dataMan.setBurnedVocabularyCount(vocabulary);
                }
            } catch (Exception e) {
                e.printStackTrace();

                if (strings[0].equals("apprentice")) {
                    radicals = dataMan.getApprenticeRadicalsCount();
                    kanji = dataMan.getApprenticeKanjiCount();
                    vocabulary = dataMan.getApprenticeVocabularyCount();
                    total = dataMan.getApprenticeTotalCount();
                }
                if (strings[0].equals("guru")) {
                    radicals = dataMan.getGuruRadicalsCount();
                    kanji = dataMan.getGuruKanjiCount();
                    vocabulary = dataMan.getGuruVocabularyCount();
                    total = dataMan.getGuruTotalCount();
                }
                if (strings[0].equals("master")) {
                    radicals = dataMan.getMasterRadicalsCount();
                    kanji = dataMan.getMasterKanjiCount();
                    vocabulary = dataMan.getMasterVocabularyCount();
                    total = dataMan.getMasterTotalCount();
                }
                if (strings[0].equals("enlighten")) {
                    radicals = dataMan.getEnlightenRadicalsCount();
                    kanji = dataMan.getEnlightenKanjiCount();
                    vocabulary = dataMan.getEnlightenVocabularyCount();
                    total = dataMan.getEnlightenTotalCount();
                }
                if (strings[0].equals("burned")) {
                    radicals = dataMan.getBurnedRadicalsCount();
                    kanji = dataMan.getBurnedKanjiCount();
                    vocabulary = dataMan.getBurnedVocabularyCount();
                    total = dataMan.getBurnedTotalCount();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
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

            mDetailsSRSLogo.setColorFilter(getResources().getColor(R.color.apptheme_main),
                    PorterDuff.Mode.SRC_ATOP);

            if (mDetailsSuperFlipper.getDisplayedChild() == 0) {
                mDetailsSuperFlipper.showNext();
            }
        }
    }
}
