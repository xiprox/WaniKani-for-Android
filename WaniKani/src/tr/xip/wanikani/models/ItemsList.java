package tr.xip.wanikani.models;

import java.util.ArrayList;
import java.util.Collection;

import tr.xip.wanikani.database.DatabaseManager;

public class ItemsList extends ArrayList<BaseItem> implements Storable {
    @Override
    public void save() {
        if (size() == 0) return;

        DatabaseManager.saveItems(this, getType());
    }

    protected BaseItem.ItemType getType() {
        return BaseItem.ItemType.RADICAL;
    }

    @Override
    public boolean add(BaseItem item) {
        item.setType(getType());
        return super.add(item);
    }

    @Override
    public void add(int index, BaseItem item) {
        item.setType(getType());
        super.add(index, item);
    }

    @Override
    public boolean addAll(Collection<? extends BaseItem> c) {
        if (c == null) return false;

        for (BaseItem item : c) {
            item.setType(getType());
        }

        return super.addAll(c);
    }
}
