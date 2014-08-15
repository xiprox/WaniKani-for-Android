package tr.xip.wanikani.settings.preferences;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import tr.xip.wanikani.R;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.settings.SettingsActivity;

/**
 * Created by xihsa_000 on 4/4/14.
 */
public class LessonsScreenOrientationPreference extends DialogFragment implements RadioButton.OnClickListener {

    Context context;
    PrefManager prefMan;
    View view;

    public static final String ORIENTATION_LANDSCAPE = "Landscape";

    public static final String ORIENTATION_AUTO = "Auto-rotate";
    public static final String ORIENTATION_PORTRAIT = "Portrait";
    public static final String DEFAULT_ORIENTATION = ORIENTATION_AUTO;

    String mSelection = DEFAULT_ORIENTATION;

    RadioButton mAutoRotateButton;
    RadioButton mPortraitButton;
    RadioButton mLandscapeButton;

    LessonsScreenOrientationPreferenceListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        prefMan = new PrefManager(context);
        mListener = (SettingsActivity) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_pref_screen_orientation, null);

        Button mOk = (Button) view.findViewById(R.id.button1);
        Button mCancel = (Button) view.findViewById(R.id.button2);

        mAutoRotateButton = (RadioButton) view.findViewById(R.id.screen_orientation_auto_rotate);
        mPortraitButton = (RadioButton) view.findViewById(R.id.screen_orientation_portrait);
        mLandscapeButton = (RadioButton) view.findViewById(R.id.screen_orientation_landscape);

        mAutoRotateButton.setOnClickListener(this);
        mPortraitButton.setOnClickListener(this);
        mLandscapeButton.setOnClickListener(this);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefMan.setLessonsScreenOrientation(mSelection);
                mListener.onLessonsScreenOrientationPreferenceChanged(mSelection);
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        loadPreference();

        return view;
    }

    private void loadPreference() {
        String preference = prefMan.getLessonsScreenOrientation();

        if (preference.equals(ORIENTATION_PORTRAIT))
            mPortraitButton.setChecked(true);
        else if (preference.equals(ORIENTATION_LANDSCAPE))
            mLandscapeButton.setChecked(true);
        else
            mAutoRotateButton.setChecked(true);

    }

    @Override
    public void onClick(View view) {
        boolean checked = false;

        if (view instanceof RadioButton) {
            checked = ((RadioButton) view).isChecked();
        }

        switch(view.getId()) {
            case R.id.screen_orientation_auto_rotate:
                if (checked)
                    mSelection = ORIENTATION_AUTO;
                    break;
            case R.id.screen_orientation_portrait:
                if (checked)
                    mSelection = ORIENTATION_PORTRAIT;
                    break;
            case R.id.screen_orientation_landscape:
                if (checked)
                    mSelection = ORIENTATION_LANDSCAPE;
                    break;
        }
    }

    public interface LessonsScreenOrientationPreferenceListener {
        public void onLessonsScreenOrientationPreferenceChanged(String preference);
    }
}