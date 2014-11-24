package tr.xip.wanikani.api.response;

import java.io.Serializable;

public class UnlockItem extends BaseItem implements Serializable {
    private String type;
    private long unlocked_date;

    @Override
    public ItemType getType() {
        return getTypeFromString(type);
    }

    @Override
    public String getTypeString() {
        return type;
    }

    public long getUnlockDate() {
        return unlocked_date * 1000;
    }
}