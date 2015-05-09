package tr.xip.wanikani.app.fragment.card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 9/18/14.
 */
public class ProgressCardNoTitle extends ProgressCard {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_progress_no_title, null);

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
}
