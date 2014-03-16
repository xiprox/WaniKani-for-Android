package tr.xip.wanikani.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.CriticalItemsList;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class CriticalItemsAdapter extends ArrayAdapter<CriticalItemsList.CriticalItem> {

    View mItemType;
    TextView mItemCharacter;
    TextView mItemPercentage;

    private List<CriticalItemsList.CriticalItem> items;

    public CriticalItemsAdapter(Context context, int textViewResourceId, List<CriticalItemsList.CriticalItem> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        Log.d("ADAPTER CALLED", "!!!");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        CriticalItemsList.CriticalItem item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_critical, null);

            mItemType = v.findViewById(R.id.item_critical_type);
            mItemCharacter = (TextView) v.findViewById(R.id.item_critical_character);
            mItemPercentage = (TextView) v.findViewById(R.id.item_critical_percentage);

            if (item.type.equals("radical")) {
                mItemType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_radical));
            }

            if (item.type.equals("kanji")) {
                mItemType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_kanji));
            }

            if (item.type.equals("vocabulary")) {
                mItemType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_vocabulary));
            }

            mItemCharacter.setText(item.character);
            mItemPercentage.setText(item.percentage + "");
        }

        return v;
    }
}