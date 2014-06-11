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

    MessageCardListener mLisneter;

    public static final String ARG_TITLE = "title";
    public static final String ARG_SUMMARY = "summary";
    public static final String ARG_HIDE_OK_BUTTON = "hide-ok-button";
    public static final String ARG_BUTTON_TEXT = "button-text";

    String title;
    String summary;
    String buttonText;
    boolean hideOkButton;

    public void setListener(MessageCardListener listener) {
        mLisneter = listener;
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
        hideOkButton = args.getBoolean(ARG_HIDE_OK_BUTTON);
        buttonText = args.getString(ARG_BUTTON_TEXT);

        TextView mTitle = (TextView) rootView.findViewById(R.id.message_card_title);
        TextView mSummary = (TextView) rootView.findViewById(R.id.message_card_summary);
        Button mButton = (Button) rootView.findViewById(R.id.message_card_button);

        if (title != null)
            mTitle.setText(title);

        if (summary != null)
            mSummary.setText(summary);

        if (hideOkButton)
            mButton.setVisibility(View.GONE);
        else if (buttonText != null)
            mButton.setText(buttonText);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLisneter.onMessageCardOkButtonClick();
            }
        });

        return rootView;
    }

    public interface MessageCardListener {
        public void onMessageCardOkButtonClick();
    }
}
