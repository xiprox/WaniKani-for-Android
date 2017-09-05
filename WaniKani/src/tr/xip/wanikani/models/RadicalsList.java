package tr.xip.wanikani.models;

public class RadicalsList extends ItemsList {

    @Override
    protected BaseItem.ItemType getType() {
        return BaseItem.ItemType.RADICAL;
    }
}
