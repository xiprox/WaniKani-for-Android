package tr.xip.wanikani.app.fragment.card;

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

import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.R;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.SRSDistributionGetTask;
import tr.xip.wanikani.client.task.callback.SRSDistributionGetTaskCallbacks;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class SRSCard extends Fragment implements SRSDistributionGetTaskCallbacks {

    WaniKaniApi api;
    PrefManager prefMan;
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

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new SRSDistributionGetTask(context, SRSCard.this).executeParallel();
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

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
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
                switchToMain(mDetailsUp);
            }
        });
    }

    private void switchToDetails() {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            int cx = (mDetailsContent.getLeft() + mDetailsContent.getRight()) / 2;
            int cy = (mDetailsContent.getTop() + mDetailsContent.getBottom()) / 2;

            int finalRadius = mDetailsContent.getWidth();

            Log.e("LALALALALAa", cx + "_------_" + cy + " ------ " + finalRadius);

            ValueAnimator anim =
                    ViewAnimationUtils.createCircularReveal(mDetailsContent, cx, cy, 0, finalRadius);
            anim.start();
        } else {*/
        mMainContent.setVisibility(View.GONE);
        mDetailsContent.setVisibility(View.VISIBLE);
//        }
    }

    private void switchToMain(final View view) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            int initialRadius = view.getWidth();

            ValueAnimator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            anim.start();
        } else {*/
        mMainContent.setVisibility(View.VISIBLE);
        mDetailsContent.setVisibility(View.GONE);
//        }

        if (mDetailsSuperFlipper.getDisplayedChild() == 1)
            mDetailsSuperFlipper.showPrevious();
    }

    @Override
    public void onSRSDistributionGetTaskPreExecute() {

    }

    @Override
    public void onSRSDistributionGetTaskPostExecute(SRSDistribution distribution) {
        if (distribution != null) {
            mApprentice.setText(distribution.getAprentice().getTotalCount() + "");
            mGuru.setText(distribution.getGuru().getTotalCount() + "");
            mMaster.setText(distribution.getMaster().getTotalCount() + "");
            mEnlightened.setText(distribution.getMaster().getTotalCount() + "");
            mBurned.setText(distribution.getBurned().getTotalCount() + "");

            mTotalItems.setText(distribution.getTotal() + "");

            mListener.onStatusCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
        } else
            mListener.onStatusCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
    }

    public interface StatusCardListener {
        public void onStatusCardSyncFinishedListener(String result);
    }

    private class DetailsLoadTask extends AsyncTask<String, Void, String> {
        int radicals;
        int kanji;
        int vocabulary;
        int total;

        String srsLevel;

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (strings[0].equals("apprentice")) {
                    radicals = srs.getAprentice().getRadicalsCount();
                    kanji = srs.getAprentice().getKanjiCount();
                    vocabulary = srs.getAprentice().getVocabularyCount();
                    total = srs.getAprentice().getTotalCount();
                }
                if (strings[0].equals("guru")) {
                    radicals = srs.getGuru().getRadicalsCount();
                    kanji = srs.getGuru().getKanjiCount();
                    vocabulary = srs.getGuru().getVocabularyCount();
                    total = srs.getGuru().getTotalCount();
                }
                if (strings[0].equals("master")) {
                    radicals = srs.getMaster().getRadicalsCount();
                    kanji = srs.getMaster().getKanjiCount();
                    vocabulary = srs.getMaster().getVocabularyCount();
                    total = srs.getMaster().getTotalCount();
                }
                if (strings[0].equals("enlighten")) {
                    radicals = srs.getEnlighten().getRadicalsCount();
                    kanji = srs.getEnlighten().getKanjiCount();
                    vocabulary = srs.getEnlighten().getVocabularyCount();
                    total = srs.getEnlighten().getTotalCount();
                }
                if (strings[0].equals("burned")) {
                    radicals = srs.getBurned().getRadicalsCount();
                    kanji = srs.getBurned().getKanjiCount();
                    vocabulary = srs.getBurned().getVocabularyCount();
                    total = srs.getBurned().getTotalCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
