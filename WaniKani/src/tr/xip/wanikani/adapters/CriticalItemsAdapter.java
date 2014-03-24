package tr.xip.wanikani.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.CriticalItemsList;
import tr.xip.wanikani.utils.Fonts;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class CriticalItemsAdapter extends ArrayAdapter<CriticalItemsList.CriticalItem> {

    Context context;
    Typeface typeface;

    View mItemType;
    TextView mItemCharacter;
    ImageView mItemCharacterImage;
    TextView mItemPercentage;

    private List<CriticalItemsList.CriticalItem> items;

    public CriticalItemsAdapter(Context context, int textViewResourceId, List<CriticalItemsList.CriticalItem> objects, Typeface typeface) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.typeface = typeface;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        CriticalItemsList.CriticalItem item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_critical, null);

            mItemType = v.findViewById(R.id.item_critical_type);
            mItemCharacter = (TextView) v.findViewById(R.id.item_critical_character);
            mItemCharacterImage = (ImageView) v.findViewById(R.id.item_critical_character_image);
            mItemPercentage = (TextView) v.findViewById(R.id.item_critical_percentage);

            mItemCharacter.setTypeface(typeface);

            if (item.type.equals("radical")) {
                mItemType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_radical));
            }

            if (item.type.equals("kanji")) {
                mItemType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_kanji));
            }

            if (item.type.equals("vocabulary")) {
                mItemType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_vocabulary));
            }

            if (item.image == null) {
                mItemCharacter.setVisibility(View.VISIBLE);
                mItemCharacterImage.setVisibility(View.GONE);
                mItemCharacter.setText(item.character);
            } else {
                mItemCharacter.setVisibility(View.GONE);
                mItemCharacterImage.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(item.image)
                        .into(mItemCharacterImage);
                mItemCharacterImage.setColorFilter(context.getResources().getColor(R.color.text_gray), PorterDuff.Mode.SRC_ATOP);
            }

            mItemCharacter.setText(item.character);
            mItemPercentage.setText(item.percentage + "");
        }

        return v;
    }
}