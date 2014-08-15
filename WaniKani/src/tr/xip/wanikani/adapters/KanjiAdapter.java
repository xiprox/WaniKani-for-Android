package tr.xip.wanikani.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.KanjiList;
import tr.xip.wanikani.utils.Animations;
import tr.xip.wanikani.utils.Fonts;

public class KanjiAdapter extends StickyGridHeadersSimpleArrayAdapter<KanjiList.KanjiItem> {
    Context context;

    int headerResourceId;

    private List<KanjiList.KanjiItem> items;

    Typeface typeface;

    public KanjiAdapter(Context context, List<KanjiList.KanjiItem> list, int headerResId, int itemResId) {
        super(context, list, headerResId, itemResId);
        this.items = list;
        this.context = context;
        this.headerResourceId = headerResId;
        this.typeface = new Fonts().getKanjiFont(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        KanjiList.KanjiItem kanjiItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_kanji, null);

            viewHolder = new ViewHolder();

            viewHolder.card = (FrameLayout) convertView.findViewById(R.id.item_kanji_card);
            viewHolder.status = convertView.findViewById(R.id.item_kanji_status);
            viewHolder.character = (TextView) convertView.findViewById(R.id.item_kanji_character);
            viewHolder.meaning = (TextView) convertView.findViewById(R.id.item_kanji_meaning);
            viewHolder.reading = (TextView) convertView.findViewById(R.id.item_kanji_reading);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }

        ((FrameLayout) convertView).setLayoutAnimation(Animations.FadeInController());

        viewHolder.character.setText(kanjiItem.getCharacter());
        viewHolder.character.setTypeface(typeface);

        if (!kanjiItem.isUnlocked()) {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            viewHolder.status.setBackgroundResource(R.drawable.pattern_diagonal_xml);
        } else if (kanjiItem.isBurned()) {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(R.color.wanikani_burned));
            viewHolder.status.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        } else {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            viewHolder.status.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        if (kanjiItem.getImportantReading().equals("onyomi"))
            viewHolder.reading.setText(kanjiItem.getOnyomi());
        else
            viewHolder.reading.setText(kanjiItem.getKunyomi());

        viewHolder.reading.setTypeface(typeface);

        String[] meanings = kanjiItem.getMeaning().split(",");

        viewHolder.meaning.setText(WordUtils.capitalize(meanings[0]));

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

        KanjiList.KanjiItem item = getItem(position);

        holder.textView.setText(item.getLevel() + "");

        return convertView;
    }

    protected class ViewHolder {
        public FrameLayout card;
        public TextView character;
        public TextView meaning;
        public TextView reading;
        public View status;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

}

