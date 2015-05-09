package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.utils.Animations;
import tr.xip.wanikani.utils.Fonts;

public class VocabularyAdapter extends StickyGridHeadersSimpleArrayAdapter<BaseItem> {
    Context context;

    int headerResourceId;
    Typeface typeface;
    private List<BaseItem> items;

    public VocabularyAdapter(Context context, List<BaseItem> list, int headerResId, int itemResId) {
        super(context, list, headerResId, itemResId);
        this.items = list;
        this.context = context;
        this.headerResourceId = headerResId;
        this.typeface = new Fonts().getKanjiFont(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        BaseItem vocabularyItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_vocabulary, null);

            viewHolder = new ViewHolder();

            viewHolder.card = (FrameLayout) convertView.findViewById(R.id.item_vocabulary_card);
            viewHolder.status = convertView.findViewById(R.id.item_vocabulary_status);
            viewHolder.character = (TextView) convertView.findViewById(R.id.item_vocabulary_character);
            viewHolder.meaning = (TextView) convertView.findViewById(R.id.item_vocabulary_meaning);
            viewHolder.kana = (TextView) convertView.findViewById(R.id.item_vocabulary_kana);
            viewHolder.srs = convertView.findViewById(R.id.item_vocabulary_srs_level);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }

        ((FrameLayout) convertView).setLayoutAnimation(Animations.FadeInController());

        viewHolder.character.setText(vocabularyItem.getCharacter());
        viewHolder.character.setTypeface(typeface);

        if (!vocabularyItem.isUnlocked()) {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            viewHolder.status.setBackgroundResource(R.drawable.pattern_diagonal_xml);
        } else if (vocabularyItem.isBurned()) {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(R.color.wanikani_burned));
            viewHolder.status.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        } else {
            viewHolder.card.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            viewHolder.status.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        if (vocabularyItem.isUnlocked()) {
            if (vocabularyItem.getSrsLevel().equals("apprentice"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_apprentice);
            if (vocabularyItem.getSrsLevel().equals("guru"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_guru);
            if (vocabularyItem.getSrsLevel().equals("master"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_master);
            if (vocabularyItem.getSrsLevel().equals("enlighten"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_enlightened);
            if (vocabularyItem.getSrsLevel().equals("burned"))
                viewHolder.srs.setBackgroundResource(R.drawable.oval_burned);
        } else
            viewHolder.srs.setBackgroundResource(R.drawable.oval_disabled);

        String[] readings = vocabularyItem.getKana().split(",");

        viewHolder.kana.setText(readings[0]);
        viewHolder.kana.setTypeface(typeface);

        String[] meanings = vocabularyItem.getMeaning().split(",");

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

        BaseItem item = getItem(position);

        holder.textView.setText(item.getLevel() + "");

        return convertView;
    }

    protected class ViewHolder {
        public FrameLayout card;
        public TextView character;
        public TextView meaning;
        public TextView kana;
        public View status;
        public View srs;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

}

