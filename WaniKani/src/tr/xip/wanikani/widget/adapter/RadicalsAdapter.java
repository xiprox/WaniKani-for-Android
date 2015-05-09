package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.utils.Animations;
import tr.xip.wanikani.utils.Fonts;

public class RadicalsAdapter extends StickyGridHeadersSimpleArrayAdapter<BaseItem> {
    Context context;

    int headerResourceId;
    Typeface typeface;
    private List<BaseItem> items;

    public RadicalsAdapter(Context context, List<BaseItem> list, int headerResId, int itemResId) {
        super(context, list, headerResId, itemResId);
        this.items = list;
        this.context = context;
        this.headerResourceId = headerResId;
        this.typeface = new Fonts().getKanjiFont(context);
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        BaseItem radicalItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_radical, null);

            viewHolder = new ViewHolder();

            viewHolder.card = (FrameLayout) convertView.findViewById(R.id.item_radical_card);
            viewHolder.status = convertView.findViewById(R.id.item_radical_status);
            viewHolder.character = (TextView) convertView.findViewById(R.id.item_radical_character);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.item_radical_character_image);
            viewHolder.meaning = (TextView) convertView.findViewById(R.id.item_radical_meaning);
            viewHolder.srs = convertView.findViewById(R.id.item_radical_srs_level);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }

        ((FrameLayout) convertView).setLayoutAnimation(Animations.FadeInController());

        viewHolder.character.setTypeface(this.typeface);

        if (radicalItem.getImage() == null) {
            viewHolder.character.setVisibility(View.VISIBLE);
            viewHolder.image.setVisibility(View.GONE);
            viewHolder.character.setText(radicalItem.getCharacter());
        } else {
            viewHolder.character.setVisibility(View.GONE);
            viewHolder.image.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(radicalItem.getImage())
                    .into(viewHolder.image);

            viewHolder.image.setColorFilter(context.getResources().getColor(R.color.text_gray), PorterDuff.Mode.SRC_ATOP);
        }

        if (!radicalItem.isUnlocked()) {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            viewHolder.status.setBackgroundResource(R.drawable.pattern_diagonal_xml);
        } else if (radicalItem.isBurned()) {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(R.color.wanikani_burned));
            viewHolder.status.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        } else {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            viewHolder.status.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        if (radicalItem.isUnlocked()) {
            if (radicalItem.getSrsLevel().equals("apprentice"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_apprentice);
            if (radicalItem.getSrsLevel().equals("guru"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_guru);
            if (radicalItem.getSrsLevel().equals("master"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_master);
            if (radicalItem.getSrsLevel().equals("enlighten"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_enlightened);
            if (radicalItem.getSrsLevel().equals("burned"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_burned);
        } else
            viewHolder.srs.setBackgroundResource(R.drawable.oval_disabled);

        viewHolder.meaning.setText(WordUtils.capitalize((radicalItem.getMeaning())));

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getLevel();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(headerResourceId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.header_level);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        BaseItem item = getItem(position);

        holder.textView.setText(item.getLevel() + "");

        return convertView;
    }

    protected class ViewHolder {
        public FrameLayout card;
        public TextView character;
        public ImageView image;
        public TextView meaning;
        public View status;
        public View srs;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }
}

