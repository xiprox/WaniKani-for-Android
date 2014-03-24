package tr.xip.wanikani.adapters;

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
import tr.xip.wanikani.api.response.RecentUnlocksList;
import tr.xip.wanikani.utils.Fonts;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class RecentUnlocksAdapter extends ArrayAdapter<RecentUnlocksList.UnlockItem> {

    Context context;
    Typeface typeface;

    View mUnlockType;
    TextView mUnlockCharacter;
    ImageView mUnlockCharacterImage;
    TextView mUnlockDate;

    private List<RecentUnlocksList.UnlockItem> items;

    public RecentUnlocksAdapter(Context context, int textViewResourceId, List<RecentUnlocksList.UnlockItem> objects, Typeface typeface) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.typeface = typeface;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        RecentUnlocksList.UnlockItem item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_recent_unlock, null);

            mUnlockType = v.findViewById(R.id.item_recent_unlock_type);
            mUnlockCharacter = (TextView) v.findViewById(R.id.item_recent_unlock_character);
            mUnlockCharacterImage = (ImageView) v.findViewById(R.id.item_recent_unlock_character_image);
            mUnlockDate = (TextView) v.findViewById(R.id.item_recent_unlock_date);

            mUnlockCharacter.setTypeface(typeface);

            if (item.type.equals("radical")) {
                mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_radical));
            }

            if (item.type.equals("kanji")) {
                mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_kanji));
            }

            if (item.type.equals("vocabulary")) {
                mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_vocabulary));
            }

            if (item.image == null) {
                mUnlockCharacter.setVisibility(View.VISIBLE);
                mUnlockCharacterImage.setVisibility(View.GONE);
                mUnlockCharacter.setText(item.character);
            } else {
                mUnlockCharacter.setVisibility(View.GONE);
                mUnlockCharacterImage.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(item.image)
                        .into(mUnlockCharacterImage);
                mUnlockCharacterImage.setColorFilter(context.getResources().getColor(R.color.text_gray), Mode.SRC_ATOP);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
            mUnlockDate.setText(sdf.format(item.unlocked_date * 1000));
        }

        return v;
    }
}