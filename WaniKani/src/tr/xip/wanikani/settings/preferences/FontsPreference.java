package tr.xip.wanikani.settings.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import tr.xip.wanikani.R;
import tr.xip.wanikani.managers.ThemeManager;

/**
 * Created by Hikari on 6/18/14.
 */
public class FontsPreference extends DialogPreference {

    Context context;

    public FontsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setPositiveButtonText(android.R.string.ok);
        setDialogIcon(null);
    }

    @Override
    protected View onCreateDialogView() {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_pref_fonts, null);
        view.setBackgroundColor(context.getResources().getColor(new ThemeManager(context).getWindowBackgroundColor()));
        return view;
    }
}
