package tr.xip.wanikani.app.fragment.card;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.RelativeTimeTextView;

/**
 * Created by Hikari on 6/11/14.
 */
public class MessageCard extends Fragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_PREFIX = "prefix";
    public static final String ARG_TIME = "time";
    View rootView;
    MessageCardListener mListener;
    String title;
    String prefix;
    long time;

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
        prefix = args.getString(ARG_PREFIX);
        time = args.getLong(ARG_TIME);

        TextView mTitle = (TextView) rootView.findViewById(R.id.message_card_title);
        RelativeTimeTextView mSummary = (RelativeTimeTextView) rootView.findViewById(R.id.message_card_summary);

        if (title != null)
            mTitle.setText(title);

        if (prefix != null) {
            mSummary.setPrefix(prefix);
            mSummary.setReferenceTime(time);
        }


        return rootView;
    }

    public interface MessageCardListener {
        public void onMessageCardOkButtonClick();
    }
}
