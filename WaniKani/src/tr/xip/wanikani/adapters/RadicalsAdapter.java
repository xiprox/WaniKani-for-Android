package tr.xip.wanikani.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.RadicalsList;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.utils.Fonts;

public class RadicalsAdapter extends StickyGridHeadersSimpleArrayAdapter<RadicalsList.RadicalItem> {
    Context context;

    int headerResourceId;

    ThemeManager themeMan;

    private List<RadicalsList.RadicalItem> items;

    Typeface typeface;

    public RadicalsAdapter(Context context, List<RadicalsList.RadicalItem> list, int headerResId, int itemResId) {
        super(context, list, headerResId, itemResId);
        this.items = list;
        this.context = context;
        this.headerResourceId = headerResId;
        this.typeface = new Fonts().getKanjiFont(context);
        this.themeMan = new ThemeManager(context);
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        RadicalsList.RadicalItem radicalItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_radical, null);

            viewHolder = new ViewHolder();

            viewHolder.status = convertView.findViewById(R.id.item_radical_status);
            viewHolder.character = (TextView) (convertView.findViewById(R.id.item_radical_character));
            viewHolder.image = (ImageView) (convertView.findViewById(R.id.item_radical_character_image));
            viewHolder.meaning = (TextView) (convertView.findViewById(R.id.item_radical_meaning));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }

        convertView.setBackgroundResource(themeMan.getCard());

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
            viewHolder.status.setBackgroundColor(this.context.getResources().getColor(R.color.background_disabled));
        } else if (radicalItem.isBurned()) {
            viewHolder.status.setBackgroundColor(this.context.getResources().getColor(R.color.wanikani_burned));
        }

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

        RadicalsList.RadicalItem item = getItem(position);

        holder.textView.setText(item.getLevel() + "");

        return convertView;
    }

    protected class ViewHolder {
        public TextView character;
        public ImageView image;
        public TextView meaning;
        public View status;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }
}

