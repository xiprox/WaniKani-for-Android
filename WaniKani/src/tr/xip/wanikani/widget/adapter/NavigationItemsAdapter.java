package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.content.res.Resources;
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
 * Created by xihsa_000 on 3/14/14.
 */
public class NavigationItemsAdapter extends ArrayAdapter<NavigationItemsAdapter.NavItem> {

    private ArrayList<NavItem> mDataset = new ArrayList<NavItem>();

    private int selectedItem;

    public NavigationItemsAdapter(Context context) {
        super(context, R.layout.item_navigation);

        mDataset.add(new NavItem(R.drawable.ic_dashboard_black_24dp, R.string.title_dashboard));
        mDataset.add(new NavItem(R.drawable.ic_radical_black_24dp, R.string.title_radicals));
        mDataset.add(new NavItem(R.drawable.ic_kanji_black_24dp, R.string.title_kanji));
        mDataset.add(new NavItem(R.drawable.ic_vocabulary_black_24dp, R.string.title_vocabulary));
    }

    public void selectItem(int n) {
        this.selectedItem = n;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_navigation, null);
        }

        NavItem item = mDataset.get(position);

        if (item != null) {
            ImageView mIcon = (ImageView) v.findViewById(R.id.navigation_item_icon);
            TextView mTitle = (TextView) v.findViewById(R.id.navigation_item_title);

            mIcon.setImageResource(item.getIcon());
            mTitle.setText(item.getTitle());

            Resources res = getContext().getResources();
            int transparentColor = res.getColor(android.R.color.transparent);
            int selectedBackgroundColor = res.getColor(R.color.separator_light);
            int selectedItemColor = res.getColor(R.color.apptheme_main);
            int unselectedItemColor = res.getColor(R.color.text_gray);

            v.setBackgroundColor(position == selectedItem ? selectedBackgroundColor : transparentColor);
            mTitle.setTextColor(position == selectedItem ? selectedItemColor : unselectedItemColor);
            mIcon.setColorFilter(position == selectedItem ? selectedItemColor : unselectedItemColor,
                    PorterDuff.Mode.SRC_ATOP);
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

    public class NavItem {
        private int icon;
        private int title;

        public NavItem(int icon, int title) {
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