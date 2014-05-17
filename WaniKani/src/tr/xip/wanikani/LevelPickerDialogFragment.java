package tr.xip.wanikani;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.ArrayList;

public class LevelPickerDialogFragment extends DialogFragment {

    LevelDialogListener mListener;

    private ArrayList<Integer> mSelectedItems;
    private ArrayList<Integer> mSelectedItemsStorage;

    private boolean[] mSelection;
    private boolean[] mSelectionStorage;

    String userLevel;

    public LevelPickerDialogFragment(LevelDialogListener levelDialogListener, String userLevel) {
        mListener = levelDialogListener;
        this.userLevel = userLevel;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        mSelectedItems = new ArrayList();
        mSelection = new boolean[getResources().getStringArray(R.array.wanikani_levels).length];

        if (mSelectedItemsStorage == null && mSelectionStorage == null) {
            mSelectedItems.add(Integer.parseInt(userLevel));
            mSelection[Integer.parseInt(userLevel) - 1] = true;
        }

        if (mSelectedItemsStorage != null) {
            mSelectedItems.clear();
            for (int i = 0; i < mSelectedItemsStorage.size(); ++i) {
                mSelectedItems.add(mSelectedItemsStorage.get(i));
            }
        }

        if (mSelectionStorage != null) {
            for (int i = 0; i < mSelectionStorage.length; ++i) {
                mSelection[i] = mSelectionStorage[i];
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_level_level)
                .setMultiChoiceItems(R.array.wanikani_levels, mSelection, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(which + 1);
                        } else if (mSelectedItems.contains(which + 1)) {
                            mSelectedItems.remove(Integer.valueOf(which + 1));
                        }
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String level = "0";

                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            if (mSelectedItems.get(i) == null)
                                if (level.equals("0")) {
                                    level = mSelectedItems.get(i) + ",";
                                }
                            level += mSelectedItems.get(i) + ",";
                        }

                        mListener.onLevelDialogPositiveClick(LevelPickerDialogFragment.this, level);

                        if (mSelectedItemsStorage == null)
                            mSelectedItemsStorage = new ArrayList();

                        if (mSelectionStorage == null)
                            mSelectionStorage = new boolean[getResources().getStringArray(R.array.wanikani_levels).length];

                        mSelectedItemsStorage.clear();

                        for (int j = 0; j < mSelectedItems.size(); j++) {
                            LevelPickerDialogFragment.this.mSelectedItemsStorage.add(LevelPickerDialogFragment.this.mSelectedItems.get(j));
                        }

                        for (int k = 0; k < mSelection.length; k++) {
                            mSelectionStorage[k] = mSelection[k];
                        }
                    }
                }).
                setNeutralButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mSelectedItems != null)
                            mSelectedItems.clear();
                        if (mSelection != null)
                            mSelection = new boolean[getResources().getStringArray(R.array.wanikani_levels).length];
                        if (mSelectedItemsStorage != null)
                            mSelectedItemsStorage.clear();
                        if (mSelectionStorage != null)
                            mSelectionStorage = new boolean[getResources().getStringArray(R.array.wanikani_levels).length];

                        mListener.onLevelDialogResetClick(LevelPickerDialogFragment.this, userLevel);
                    }
                }).
                setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public interface LevelDialogListener {
        public void onLevelDialogPositiveClick(DialogFragment dialog, String level);

        public void onLevelDialogResetClick(DialogFragment dialog, String level);
    }

}

