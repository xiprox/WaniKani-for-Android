package tr.xip.wanikani.api.response;

public class RadicalItem extends BaseItem {
    @Override
    public ItemType getType() {
        return ItemType.RADICAL;
    }
}