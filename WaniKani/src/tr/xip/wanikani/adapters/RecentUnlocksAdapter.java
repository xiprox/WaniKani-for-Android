package tr.xip.wanikani.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.RecentUnlocksList;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class RecentUnlocksAdapter extends ArrayAdapter<RecentUnlocksList.UnlockItem> {

    View mUnlockType;
    TextView mUnlockCharacter;
    TextView mUnlockDate;

    private List<RecentUnlocksList.UnlockItem> items;

    public RecentUnlocksAdapter(Context context, int textViewResourceId, List<RecentUnlocksList.UnlockItem> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        RecentUnlocksList.UnlockItem item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_recent_unlock, null);

            mUnlockType = v.findViewById(R.id.item_recent_unlock_type);
            mUnlockCharacter = (TextView) v.findViewById(R.id.item_recent_unlock_character);
            mUnlockDate = (TextView) v.findViewById(R.id.item_recent_unlock_date);

            if (item.type.equals("radical")) {
                mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_radical));
            }

            if (item.type.equals("kanji")) {
                mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_kanji));
            }

            if (item.type.equals("vocabulary")) {
                mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_vocabulary));
            }

            mUnlockCharacter.setText(item.character);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
            mUnlockDate.setText(sdf.format(item.unlocked_date * 1000));
        }

        return v;
    }
}