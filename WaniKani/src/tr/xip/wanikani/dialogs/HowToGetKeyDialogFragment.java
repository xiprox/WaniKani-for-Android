package tr.xip.wanikani.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 8/18/14.
 */
public class HowToGetKeyDialogFragment extends DialogFragment {

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_how_to_get_key, null);

        TextView mWaniKaniLink = (TextView) view.findViewById(R.id.wanikani_go_link_text);
        mWaniKaniLink.setMovementMethod(LinkMovementMethod.getInstance());
        Button mOk = (Button) view.findViewById(R.id.button1);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}
