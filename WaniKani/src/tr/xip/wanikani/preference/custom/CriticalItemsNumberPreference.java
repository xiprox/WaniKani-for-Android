package tr.xip.wanikani.preference.custom;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import tr.xip.wanikani.R;
import tr.xip.wanikani.managers.PrefManager;

public class CriticalItemsNumberPreference extends DialogFragment {

    Context context;

    NumberPicker mNumberPicker;

    private int DEFAULT_VALUE = 5;

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
        View view = inflater.inflate(R.layout.dialog_pref_critical_items_number, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(3);
        mNumberPicker.setMaxValue(15);
        mNumberPicker.setWrapSelectorWheel(false);

        mNumberPicker.setValue(PrefManager.getCriticalItemsNumber());

        Button mOk = (Button) view.findViewById(R.id.button1);
        Button mCancel = (Button) view.findViewById(R.id.button2);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefManager.setCriticalItemsNumber(mNumberPicker.getValue());
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