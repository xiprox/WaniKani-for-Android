package tr.xip.wanikani.items;

import java.util.ArrayList;

import tr.xip.wanikani.R;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class NavigationItems {

    public ArrayList<NavItem> ITEMS = new ArrayList<NavItem>();

    public NavigationItems() {
        addItem(new NavItem(R.drawable.ic_nav_dashboard, R.drawable.ic_nav_dashboard_selected, R.string.title_dashboard));
        addItem(new NavItem(R.drawable.ic_nav_radicals, R.drawable.ic_nav_radicals_selected, R.string.title_radicals));
        addItem(new NavItem(R.drawable.ic_nav_kanji, R.drawable.ic_nav_kanji_selected, R.string.title_kanji));
        addItem(new NavItem(R.drawable.ic_nav_vocabulary, R.drawable.ic_nav_vocabulary_selected, R.string.title_vocabulary));
    }

    public static class NavItem {
        public int icon;
        public int iconSelected;
        public int title;

        public NavItem(int icon, int iconSelected, int title) {
            this.icon = icon;
            this.iconSelected = iconSelected;
            this.title = title;
        }
    }

    public void addItem(NavItem item) {
        ITEMS.add(item);
    }

}