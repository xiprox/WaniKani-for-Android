package tr.xip.wanikani.models;

import java.util.ArrayList;

import tr.xip.wanikani.database.DatabaseManager;

public class ItemsList extends ArrayList<BaseItem> implements Storable {
    @Override
    public void save() {
        if (size() == 0) return;

        DatabaseManager.saveItems(this, get(0).getType());
    }

}
