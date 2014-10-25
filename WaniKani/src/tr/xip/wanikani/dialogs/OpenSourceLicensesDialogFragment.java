package tr.xip.wanikani.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 8/24/14.
 */
public class OpenSourceLicensesDialogFragment extends DialogFragment {

    final String licensesUrl = "file:///android_asset/licenses.html";
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
        View view = inflater.inflate(R.layout.dialog_open_source_licenses, null);

        Button mOk = (Button) view.findViewById(R.id.button1);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        WebView mWebview = (WebView) view.findViewById(R.id.webview);
        mWebview.loadUrl(licensesUrl);
        return view;
    }
}
