package tr.xip.wanikani.models;

import java.util.ArrayList;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.managers.PrefManager;

public class CriticalItemsList extends ArrayList<CriticalItem> implements Storable {
    @Override
    public void save() {
        DatabaseManager.saveCriticalItems(this);
    }
}