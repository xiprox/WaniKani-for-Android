package tr.xip.wanikani.settings.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import net.simonvt.numberpicker.NumberPicker;

import tr.xip.wanikani.R;

/**
 * Created by xihsa_000 on 4/4/14.
 */
public class RecentUnlocksNumberPreference extends DialogPreference {

    NumberPicker mNumberPicker;

    private int DEFAULT_VALUE = 10;

    private int mCurrentValue = 10;

    public RecentUnlocksNumberPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected View onCreateDialogView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.number_picker, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(3);
        mNumberPicker.setMaxValue(30);
        mNumberPicker.setWrapSelectorWheel(false);

        mNumberPicker.setValue(mCurrentValue);

        return view;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mCurrentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            mCurrentValue = (Integer) defaultValue;
            persistInt((Integer) defaultValue);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mNumberPicker.getValue());
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }
}