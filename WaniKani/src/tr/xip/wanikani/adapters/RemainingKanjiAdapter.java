package tr.xip.wanikani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.KanjiItem;
import tr.xip.wanikani.utils.Fonts;

/**
 * Created by Hikari on 9/18/14.
 */
public class RemainingKanjiAdapter extends ArrayAdapter<KanjiItem> {

    View rootView;
    Context context;

    List<KanjiItem> mList;

    public RemainingKanjiAdapter(Context context, int resource, List<KanjiItem> objects) {
        super(context, resource, objects);
        this.context = context;
        mList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KanjiItem item = mList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.item_kanji_remaining, null);
        }

        TextView mChar = (TextView) rootView.findViewById(R.id.item_kanji_character);
        mChar.setText(item.getCharacter());
        mChar.setTypeface(new Fonts().getKanjiFont(context));

        return rootView;
    }
}
