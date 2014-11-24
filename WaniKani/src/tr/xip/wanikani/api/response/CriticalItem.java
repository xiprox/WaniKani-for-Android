package tr.xip.wanikani.api.response;

import java.io.Serializable;

public class CriticalItem extends BaseItem implements Serializable {
    private String type;
    private int percentage;

    @Override
    public ItemType getType() {
        return getTypeFromString(type);
    }

    @Override
    public String getTypeString() {
        return type;
    }

    public int getPercentage() {
        return percentage;
    }
}