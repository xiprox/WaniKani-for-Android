package tr.xip.wanikani.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.FirstTimeActivity;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by Hikari on 8/18/14.
 */
public class LogoutDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefManager.logout(getContext());
                        startActivity(new Intent(getActivity(), FirstTimeActivity.class));
                        getActivity().finish();
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
}
