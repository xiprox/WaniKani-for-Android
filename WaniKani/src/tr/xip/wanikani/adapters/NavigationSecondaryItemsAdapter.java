package tr.xip.wanikani.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tr.xip.wanikani.R;
import tr.xip.wanikani.items.NavigationItems;
import tr.xip.wanikani.items.NavigationSecondaryItems;

/**
 * Created by Hikari on 5/17/14.
 */
public class NavigationSecondaryItemsAdapter extends ArrayAdapter<NavigationSecondaryItems.ListItem> {

    private ArrayList<NavigationSecondaryItems.ListItem> items;

    public NavigationSecondaryItemsAdapter(Context context, int resourceId,
                                           ArrayList<NavigationSecondaryItems.ListItem> objects) {
        super(context, resourceId, objects);
        this.items = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_navigation_secondary, null);
        }

        NavigationSecondaryItems.ListItem item = items.get(position);

        if (item != null) {
            ImageView mIcon = (ImageView) v.findViewById(R.id.navigation_secondary_item_icon);
            TextView mTitle = (TextView) v.findViewById(R.id.navigation_secondary_item_title);

            mIcon.setImageResource(item.icon);
            mTitle.setText(item.title);
        }

        return v;
    }
}
