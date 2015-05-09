package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.UnlockItem;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class RecentUnlocksArrayAdapter extends ArrayAdapter<UnlockItem> {

    Context context;
    Typeface typeface;

    View mUnlockType;
    TextView mUnlockCharacter;
    ImageView mUnlockCharacterImage;
    TextView mUnlockDate;

    private List<UnlockItem> items;

    public RecentUnlocksArrayAdapter(Context context, int textViewResourceId, List<UnlockItem> objects, Typeface typeface) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.typeface = typeface;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        UnlockItem item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_recent_unlock, null);
        }

        mUnlockType = v.findViewById(R.id.item_recent_unlock_type);
        mUnlockCharacter = (TextView) v.findViewById(R.id.item_recent_unlock_character);
        mUnlockCharacterImage = (ImageView) v.findViewById(R.id.item_recent_unlock_character_image);
        mUnlockDate = (TextView) v.findViewById(R.id.item_recent_unlock_date);

        mUnlockCharacter.setTypeface(typeface);

        if (item.getType() == BaseItem.ItemType.RADICAL) {
            mUnlockType.setBackgroundResource(R.drawable.oval_radical);
        }

        if (item.getType() == BaseItem.ItemType.KANJI) {
            mUnlockType.setBackgroundResource(R.drawable.oval_kanji);
        }

        if (item.getType() == BaseItem.ItemType.VOCABULARY) {
            mUnlockType.setBackgroundResource(R.drawable.oval_vocabulary);
        }

        if (item.getImage() == null) {
            mUnlockCharacter.setVisibility(View.VISIBLE);
            mUnlockCharacterImage.setVisibility(View.GONE);
            mUnlockCharacter.setText(item.getCharacter());
        } else {
            mUnlockCharacter.setVisibility(View.GONE);
            mUnlockCharacterImage.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(item.getImage())
                    .into(mUnlockCharacterImage);
            mUnlockCharacterImage.setColorFilter(context.getResources().getColor(R.color.text_gray), Mode.SRC_ATOP);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
        mUnlockDate.setText(sdf.format(item.getUnlockDate()));

        return v;
    }
}