package tr.xip.wanikani.adapters;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.RecentUnlocksList;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.utils.Animations;
import tr.xip.wanikani.utils.Fonts;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class RecentUnlocksStickyHeaderGridViewArrayAdapter extends StickyGridHeadersSimpleArrayAdapter<RecentUnlocksList.UnlockItem> {

    Context context;
    Typeface typeface;

    View mUnlockType;
    TextView mUnlockCharacter;
    ImageView mUnlockCharacterImage;
    TextView mUnlockDate;

    RelativeLayout mCard;

    int headerResourceId;

    private List<RecentUnlocksList.UnlockItem> items;

    public RecentUnlocksStickyHeaderGridViewArrayAdapter(Context context, List<RecentUnlocksList.UnlockItem> objects, int headerResId, int itemResId) {
        super(context, objects, headerResId, itemResId);
        this.items = objects;
        this.context = context;
        this.typeface = new Fonts().getKanjiFont(context);
        this.headerResourceId = headerResId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        RecentUnlocksList.UnlockItem item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_recent_unlock_grid, null);
        }

        ((FrameLayout) v).setLayoutAnimation(Animations.FadeInController());

        mCard = (RelativeLayout) v.findViewById(R.id.item_recent_unlock_grid_card);
        mCard.setBackgroundResource(new ThemeManager(context).getCard());

        mUnlockType = v.findViewById(R.id.item_recent_unlock_type);
        mUnlockCharacter = (TextView) v.findViewById(R.id.item_recent_unlock_character);
        mUnlockCharacterImage = (ImageView) v.findViewById(R.id.item_recent_unlock_character_image);
        mUnlockDate = (TextView) v.findViewById(R.id.item_recent_unlock_date);

        mUnlockCharacter.setTypeface(typeface);

        if (item.getType().equals("radical")) {
            mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_radical));
        }

        if (item.getType().equals("kanji")) {
            mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_kanji));
        }

        if (item.getType().equals("vocabulary")) {
            mUnlockType.setBackgroundColor(v.getResources().getColor(R.color.wanikani_vocabulary));
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

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getUnlockDate();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(headerResourceId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.header_text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        RecentUnlocksList.UnlockItem item = getItem(position);

        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM, yyyy – hh:mm:ss");
        holder.textView.setText(sdf.format(item.getUnlockDate()));

        return convertView;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }
}