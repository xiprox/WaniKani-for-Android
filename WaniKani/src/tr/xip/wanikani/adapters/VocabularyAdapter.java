package tr.xip.wanikani.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.VocabularyList;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.utils.Animations;
import tr.xip.wanikani.utils.Fonts;

public class VocabularyAdapter extends StickyGridHeadersSimpleArrayAdapter<VocabularyList.VocabularyItem> {
    Context context;

    int headerResourceId;

    ThemeManager themeMan;

    private List<VocabularyList.VocabularyItem> items;

    Typeface typeface;

    public VocabularyAdapter(Context context, List<VocabularyList.VocabularyItem> list, int headerResId, int itemResId) {
        super(context, list, headerResId, itemResId);
        this.items = list;
        this.context = context;
        this.headerResourceId = headerResId;
        this.typeface = new Fonts().getKanjiFont(context);
        this.themeMan = new ThemeManager(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        VocabularyList.VocabularyItem vocabularyItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_vocabulary, null);

            viewHolder = new ViewHolder();

            viewHolder.card = (LinearLayout) convertView.findViewById(R.id.item_vocabulary_card);
            viewHolder.status = convertView.findViewById(R.id.item_vocabulary_status);
            viewHolder.character = (TextView) convertView.findViewById(R.id.item_vocabulary_character);
            viewHolder.meaning = (TextView) convertView.findViewById(R.id.item_vocabulary_meaning);
            viewHolder.kana = (TextView) convertView.findViewById(R.id.item_vocabulary_kana);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }

        ((FrameLayout) convertView).setLayoutAnimation(Animations.FadeInController());

        viewHolder.card.setBackgroundResource(themeMan.getCard());

        viewHolder.character.setText(vocabularyItem.getCharacter());
        viewHolder.character.setTypeface(typeface);

        if (!vocabularyItem.isUnlocked()) {
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.background_disabled));
        } else if (vocabularyItem.isBurned()) {
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.wanikani_burned));
        }

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

        VocabularyList.VocabularyItem item = getItem(position);

        holder.textView.setText(item.getLevel() + "");

        return convertView;
    }

    protected class ViewHolder {
        public LinearLayout card;
        public TextView character;
        public TextView meaning;
        public TextView kana;
        public View status;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

}

