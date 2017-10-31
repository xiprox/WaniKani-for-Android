package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 5/17/14.
 */
public class NavigationSecondaryItemsAdapter extends ArrayAdapter<NavigationSecondaryItemsAdapter.NavSecondaryItem> {

    private ArrayList<NavSecondaryItem> mDataset = new ArrayList<NavSecondaryItem>();

    public NavigationSecondaryItemsAdapter(Context context) {
        super(context, R.layout.item_navigation_secondary);

        mDataset.add(new NavSecondaryItem(R.drawable.ic_heart, R.string.title_support_the_developer));
        mDataset.add(new NavSecondaryItem(R.drawable.ic_settings_black_24dp, R.string.title_settings));
        mDataset.add(new NavSecondaryItem(R.drawable.ic_logout_black_24dp, R.string.title_logout));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_navigation_secondary, null);
        }

        NavSecondaryItem item = mDataset.get(position);

        if (item != null) {
            ImageView mIcon = (ImageView) v.findViewById(R.id.navigation_secondary_item_icon);
            TextView mTitle = (TextView) v.findViewById(R.id.navigation_secondary_item_title);

            mIcon.setImageResource(item.getIcon());
            mTitle.setText(item.getTitle());

            if (position != 0) {
                int color = mIcon.getContext().getResources().getColor(R.color.text_gray);
                mIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }

        return v;
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    public int getListViewHeight() {
        return getCount() * getContext().getResources().getDimensionPixelSize(R.dimen.nav_drawer_item_height);
    }

    public class NavSecondaryItem {
        private int icon;
        private int title;

        public NavSecondaryItem(int icon, int title) {
            this.icon = icon;
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public int getTitle() {
            return title;
        }
    }
}
