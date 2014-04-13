package tr.xip.wanikani.items;

import java.util.ArrayList;

import tr.xip.wanikani.R;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class NavigationItems {

    public ArrayList<NavItem> ITEMS = new ArrayList<NavItem>();

    public NavigationItems() {
        addItem(new NavItem(R.drawable.ic_dashboard, R.string.title_dashboard));
        addItem(new NavItem(R.drawable.ic_radicals, R.string.title_radicals));
        addItem(new NavItem(R.drawable.ic_kanji, R.string.title_kanji));
        addItem(new NavItem(R.drawable.ic_vocabulary, R.string.title_vocabulary));
    }

    public static class NavItem {
        public int icon;
        public int title;

        public NavItem(int icon, int title) {
            this.icon = icon;
            this.title = title;
        }
    }

    public void addItem(NavItem item) {
        ITEMS.add(item);
    }

}