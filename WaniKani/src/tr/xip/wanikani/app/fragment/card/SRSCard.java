package tr.xip.wanikani.app.fragment.card;

import android.annotation.SuppressLint;
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
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class SRSCard extends Fragment {
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
            WaniKaniApi.getSRSDistribution().enqueue(new ThroughDbCallback<Request<SRSDistribution>, SRSDistribution>() {
                @Override
                public void onResponse(Call<Request<SRSDistribution>> call, Response<Request<SRSDistribution>> response) {
                    super.onResponse(call, response);

                    if (response.isSuccessful() && response.body().requested_information != null) {
                        displayData(response.body().requested_information);
                    } else {
                        onFailure(call, null);
                    }
                }

                @Override
                public void onFailure(Call<Request<SRSDistribution>> call, Throwable t) {
                    super.onFailure(call, t);

                    SRSDistribution distribution = DatabaseManager.getSrsDistribution();
                    if (distribution != null) {
                        displayData(distribution);
                    }
                }
            });
        }
    };

    public void setListener(StatusCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    @Override
    public void onCreate(Bundle state) {
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
                if (srs == null) return;
                loadDetails("apprentice");
                switchToDetails();
            }
        });

        mGuruParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (srs == null) return;
                loadDetails("guru");
                switchToDetails();
            }
        });

        mMasterParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (srs == null) return;
                loadDetails("master");
                switchToDetails();
            }
        });

        mEnlightenedParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (srs == null) return;
                loadDetails("enlighten");
                switchToDetails();
            }
        });

        mBurnedParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (srs == null) return;
                loadDetails("burned");
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

    @SuppressLint("SetTextI18n")
    private void displayData(SRSDistribution distribution) {
        if (distribution != null) {
            srs = distribution;

            mApprentice.setText(distribution.apprentice.total + "");
            mGuru.setText(distribution.guru.total + "");
            mMaster.setText(distribution.master.total + "");
            mEnlightened.setText(distribution.enlighten.total + "");
            mBurned.setText(distribution.burned.total + "");

            mTotalItems.setText(distribution.total() + "");

            mListener.onStatusCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
        } else {
            mListener.onStatusCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
        }
    }

    public interface StatusCardListener {
        public void onStatusCardSyncFinishedListener(String result);
    }

    @SuppressLint("SetTextI18n")
    private void loadDetails(String level) {
        int radicals = 0;
        int kanji = 0;
        int vocabulary = 0;
        int total = 0;

        switch (level) {
            case "apprentice":
                radicals = srs.apprentice.radicals;
                kanji = srs.apprentice.kanji;
                vocabulary = srs.apprentice.vocabulary;
                total = srs.apprentice.total;

                mDetailsSRSLogo.setImageResource(R.drawable.apprentice);
                mDetailsSRSLevel.setText(R.string.srs_title_apprentice);
                break;
            case "guru":
                radicals = srs.guru.radicals;
                kanji = srs.guru.kanji;
                vocabulary = srs.guru.vocabulary;
                total = srs.guru.total;

                mDetailsSRSLogo.setImageResource(R.drawable.guru);
                mDetailsSRSLevel.setText(R.string.srs_title_guru);
                break;
            case "master":
                radicals = srs.master.radicals;
                kanji = srs.master.kanji;
                vocabulary = srs.master.vocabulary;
                total = srs.master.total;

                mDetailsSRSLogo.setImageResource(R.drawable.master);
                mDetailsSRSLevel.setText(R.string.srs_title_master);
                break;
            case "enlighten":
                radicals = srs.enlighten.radicals;
                kanji = srs.enlighten.kanji;
                vocabulary = srs.enlighten.vocabulary;
                total = srs.enlighten.total;

                mDetailsSRSLogo.setImageResource(R.drawable.enlighten);
                mDetailsSRSLevel.setText(R.string.srs_title_enlightened);
                break;
            case "burned":
                radicals = srs.burned.radicals;
                kanji = srs.burned.kanji;
                vocabulary = srs.burned.vocabulary;
                total = srs.burned.total;

                mDetailsSRSLogo.setImageResource(R.drawable.burned);
                mDetailsSRSLevel.setText(R.string.srs_title_burned);
                break;
        }

        mDetailsRadicals.setText(radicals + "");
        mDetailsKanji.setText(kanji + "");
        mDetailsVocabulary.setText(vocabulary + "");
        mDetailsTotal.setText(total + "");

        mDetailsSRSLogo.setColorFilter(getResources().getColor(R.color.apptheme_main),
                PorterDuff.Mode.SRC_ATOP);

        if (mDetailsSuperFlipper.getDisplayedChild() == 0) {
            mDetailsSuperFlipper.showNext();
        }
    }
}
