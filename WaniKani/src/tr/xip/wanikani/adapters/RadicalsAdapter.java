package tr.xip.wanikani.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.api.response.RadicalsList;
import tr.xip.wanikani.utils.Fonts;

public class RadicalsAdapter
        extends ArrayAdapter<RadicalsList.RadicalItem> {
    Context context;

    private List<RadicalsList.RadicalItem> items;

    Typeface typeface;

    public RadicalsAdapter(Context context, int position, List<RadicalsList.RadicalItem> list) {
        super(context, position, list);
        this.items = list;
        this.context = context;
        this.typeface = new Fonts().getKanjiFont(context);
    }

    public View getView(int position, View converView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        RadicalsList.RadicalItem radicalItem = items.get(position);

        if (converView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            converView = inflater.inflate(R.layout.item_radical, null);

            viewHolder = new ViewHolder();

            viewHolder.status = converView.findViewById(R.id.item_radical_status);
            viewHolder.character = (TextView) (converView.findViewById(R.id.item_radical_character));
            viewHolder.image = (ImageView) (converView.findViewById(R.id.item_radical_character_image));
            viewHolder.meaning = (TextView) (converView.findViewById(R.id.item_radical_meaning));
            viewHolder.level = (TextView) (converView.findViewById(R.id.item_radical_level));

            converView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (converView.getTag());
        }

        viewHolder.character.setTypeface(this.typeface);

        if (radicalItem.image == null) {
            viewHolder.character.setVisibility(View.VISIBLE);
            viewHolder.image.setVisibility(View.GONE);
            viewHolder.character.setText(radicalItem.character);
        } else {
            viewHolder.character.setVisibility(View.GONE);
            viewHolder.image.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(radicalItem.image)
                    .into(viewHolder.image);

            viewHolder.image.setColorFilter(context.getResources().getColor(R.color.text_gray), PorterDuff.Mode.SRC_ATOP);
        }

        if (radicalItem.user_specific == null) {
            viewHolder.status.setBackgroundColor(this.context.getResources().getColor(R.color.background_disabled));
        } else if (radicalItem.user_specific.burned) {
            viewHolder.status.setBackgroundColor(this.context.getResources().getColor(R.color.wanikani_burned));
        }

        viewHolder.meaning.setText(WordUtils.capitalize((radicalItem.meaning)));
        viewHolder.level.setText(radicalItem.level + "");

        return converView;
    }

    static class ViewHolder {
        public TextView character;
        public ImageView image;
        public TextView level;
        public TextView meaning;
        public View status;

        private ViewHolder() {

        }
    }

}

