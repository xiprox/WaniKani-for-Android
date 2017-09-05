package tr.xip.wanikani.models;

import java.io.Serializable;
import java.util.ArrayList;

import tr.xip.wanikani.database.DatabaseManager;

public class RecentUnlocksList extends ArrayList<UnlockItem> implements Storable {
    @Override
    public void save() {
        DatabaseManager.saveRecentUnlocks(this);
    }
}
