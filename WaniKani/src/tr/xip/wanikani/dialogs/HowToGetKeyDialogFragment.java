package tr.xip.wanikani.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 8/18/14.
 */
public class HowToGetKeyDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_how_to_get_key, null, false);
        ((TextView) view.findViewById(R.id.wanikani_link)).setMovementMethod(new LinkMovementMethod());
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.action_how_to_api_key)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
        return dialog;
    }
}
