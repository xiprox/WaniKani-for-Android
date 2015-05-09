package tr.xip.wanikani.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.adapter.LevelPickerCheckBoxAdapter;

public class LevelPickerDialogFragment extends DialogFragment implements LevelPickerCheckBoxAdapter.LevelPickerCheckBoxAdapterListener {

    private static final String ARG_USER_LEVEL = "user_level";
    private static final String ARG_FRAGMENT_ID = "fragment_id";
    private static final String ARG_SELECTED_ITEMS_STORAGE = "selected_items_storage";
    private static final String ARG_SELECTION_STORAGE = "selection_storage";

    Context context;
    LevelDialogListener mListener;
    String userLevel;

    int fragmentId;

    String selectedLevel = "0";
    int wanikaniLevelsNumber;

    private HashSet<Integer> mSelectedItems;
    private ArrayList<Integer> mSelectedItemsStorage;

    private boolean[] mSelection;
    private boolean[] mSelectionStorage;

    public void init(int fragmentId, String userLevel) {
        this.userLevel = userLevel;
        this.fragmentId = fragmentId;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        context = getActivity();
        wanikaniLevelsNumber = context.getResources().getInteger(R.integer.wanikani_levels_number);

        Dialog dialog = super.onCreateDialog(bundle);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if (bundle != null) {
            userLevel = bundle.getString(ARG_USER_LEVEL);
            fragmentId = bundle.getInt(ARG_FRAGMENT_ID);
            mSelectedItemsStorage = bundle.getIntegerArrayList(ARG_SELECTED_ITEMS_STORAGE);
            mSelectionStorage = bundle.getBooleanArray(ARG_SELECTION_STORAGE);
        }

        mListener = (LevelDialogListener) getFragmentManager().findFragmentById(fragmentId);

        mSelectedItems = new HashSet<Integer>();
        mSelection = new boolean[wanikaniLevelsNumber];

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

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_level_picker, null);

        Button mOk = (Button) view.findViewById(R.id.button1);
        Button mReset = (Button) view.findViewById(R.id.button2);
        Button mCancel = (Button) view.findViewById(R.id.button3);

        final ListView mCheckList = (ListView) view.findViewById(R.id.listView);

        ArrayList<LevelPickerCheckBoxAdapter.LevelCheckBox> mListItems = new ArrayList<LevelPickerCheckBoxAdapter.LevelCheckBox>();

        for (int i = 0; i < wanikaniLevelsNumber; i++) {
            LevelPickerCheckBoxAdapter.LevelCheckBox checkBox =
                    new LevelPickerCheckBoxAdapter.LevelCheckBox(i + 1 + "", mSelection[i]);
            mListItems.add(checkBox);
        }

        mCheckList.setAdapter(new LevelPickerCheckBoxAdapter(context,
                R.layout.item_level_picker_checkbox, mListItems, this));

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLevel = "0";

                for (Integer item : mSelectedItems) {
                    if (selectedLevel.equals("0")) {
                        selectedLevel = (item + 1) + "";
                    } else {
                        selectedLevel += "," + (item + 1);
                    }
                }

                if (!selectedLevel.equals("0")) {
                    mListener.onLevelDialogPositiveClick(LevelPickerDialogFragment.this, selectedLevel);

                    if (mSelectedItemsStorage == null) {
                        mSelectedItemsStorage = new ArrayList<Integer>();
                    }

                    if (mSelectionStorage == null) {
                        mSelectionStorage = new boolean[wanikaniLevelsNumber];
                    }

                    mSelectedItemsStorage.clear();

                    for (int item : mSelectedItems) {
                        mSelectedItemsStorage.add(item);
                    }

                    for (int i = 0; i < mSelection.length; i++) {
                        mSelectionStorage[i] = mSelection[i];
                    }

                    dismiss();
                } else
                    Toast.makeText(context, R.string.error_no_levels_selected, Toast.LENGTH_LONG).show();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedItems != null)
                    mSelectedItems.clear();
                if (mSelection != null)
                    mSelection = new boolean[wanikaniLevelsNumber];
                if (mSelectedItemsStorage != null)
                    mSelectedItemsStorage.clear();
                if (mSelectionStorage != null)
                    mSelectionStorage = new boolean[wanikaniLevelsNumber];

                mListener.onLevelDialogResetClick(LevelPickerDialogFragment.this, userLevel);

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

    @Override
    public void onLevelPickerCheckBoxAdapterItemClickListener(int which, boolean isChecked) {
        if (isChecked) {
            mSelectedItems.add(which);
        } else if (mSelectedItems.contains(which)) {
            mSelectedItems.remove(Integer.valueOf(which));
        }

        mSelection[which] = isChecked;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_USER_LEVEL, userLevel);
        outState.putInt(ARG_FRAGMENT_ID, fragmentId);
        outState.putIntegerArrayList(ARG_SELECTED_ITEMS_STORAGE, mSelectedItemsStorage);
        outState.putBooleanArray(ARG_SELECTION_STORAGE, mSelectionStorage);
        super.onSaveInstanceState(outState);
    }


    public interface LevelDialogListener {
        public void onLevelDialogPositiveClick(DialogFragment dialog, String level);

        public void onLevelDialogResetClick(DialogFragment dialog, String level);
    }

}

