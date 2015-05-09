package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 8/19/14.
 */
public class LevelPickerCheckBoxAdapter extends ArrayAdapter<LevelPickerCheckBoxAdapter.LevelCheckBox> {

    Context context;

    ArrayList<LevelCheckBox> mItems;

    LevelPickerCheckBoxAdapterListener mListener;


    ViewHolder viewHolder;

    public LevelPickerCheckBoxAdapter(Context context, int resource, ArrayList<LevelCheckBox> levels,
                                      LevelPickerCheckBoxAdapterListener listener) {
        super(context, resource);
        mItems = levels;
        this.context = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final LevelCheckBox item = mItems.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_level_picker_checkbox, null);

            viewHolder = new ViewHolder();

            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.level_checkbox_checkbox);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mListener.onLevelPickerCheckBoxAdapterItemClickListener(position, isChecked);
                mItems.get(position).setChecked(isChecked);
            }
        });

        viewHolder.checkBox.setText(item.getTitle());
        viewHolder.checkBox.setChecked(item.isChecked());

        return convertView;
    }

    public interface LevelPickerCheckBoxAdapterListener {
        public void onLevelPickerCheckBoxAdapterItemClickListener(int which, boolean isChecked);
    }

    public static class LevelCheckBox {
        String title;
        boolean checked;

        public LevelCheckBox(String title, boolean checked) {
            this.title = title;
            this.checked = checked;
        }

        public String getTitle() {
            return title;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }

    protected class ViewHolder {
        public CheckBox checkBox;
    }
}