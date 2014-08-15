package tr.xip.wanikani.items;

import java.util.ArrayList;

import tr.xip.wanikani.R;

/**
 * Created by Hikari on 5/17/14.
 */
public class NavigationSecondaryItems {

    public ArrayList<ListItem> ITEMS = new ArrayList<ListItem>();

    public NavigationSecondaryItems() {
        addItem(new ListItem(R.drawable.ic_action_settings, R.string.title_settings));
        addItem(new ListItem(R.drawable.ic_action_logout, R.string.title_logout));
    }

    public static class ListItem {
        public int icon;
        public int title;

        public ListItem(int icon, int title) {
            this.icon = icon;
            this.title = title;
        }
    }

    public void addItem(ListItem item) {
        ITEMS.add(item);
    }

}