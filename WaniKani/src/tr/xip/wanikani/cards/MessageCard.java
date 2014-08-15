package tr.xip.wanikani.cards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 6/11/14.
 */
public class MessageCard extends Fragment {

    View rootView;

    MessageCardListener mListener;

    public static final String ARG_TITLE = "title";
    public static final String ARG_SUMMARY = "summary";

    String title;
    String summary;

    public void setListener(MessageCardListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message_card, container, false);

        Bundle args = getArguments();
        title = args.getString(ARG_TITLE);
        summary = args.getString(ARG_SUMMARY);

        TextView mTitle = (TextView) rootView.findViewById(R.id.message_card_title);
        TextView mSummary = (TextView) rootView.findViewById(R.id.message_card_summary);

        if (title != null)
            mTitle.setText(title);

        if (summary != null)
            mSummary.setText(summary);

        return rootView;
    }

    public interface MessageCardListener {
        public void onMessageCardOkButtonClick();
    }
}
