package tr.xip.wanikani.settings.preferences;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import net.simonvt.numberpicker.NumberPicker;

import tr.xip.wanikani.R;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by xihsa_000 on 4/4/14.
 */
public class CriticalItemsPercentagePreference extends DialogFragment {

    Context context;
    PrefManager prefMan;

    NumberPicker mNumberPicker;

    private int DEFAULT_VALUE = 75;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        prefMan = new PrefManager(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pref_critical_items_percentage, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(50);
        mNumberPicker.setMaxValue(90);
        mNumberPicker.setWrapSelectorWheel(false);

        mNumberPicker.setValue(prefMan.getDashboardCriticalItemsPercentage());

        Button mOk = (Button) view.findViewById(R.id.button1);
        Button mCancel = (Button) view.findViewById(R.id.button2);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefMan.setDashboardCriticalItemsPercentage(mNumberPicker.getValue());
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}