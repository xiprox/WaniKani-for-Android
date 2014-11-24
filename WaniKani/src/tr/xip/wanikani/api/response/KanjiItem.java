package tr.xip.wanikani.api.response;

public class KanjiItem extends BaseItem {
    @Override
    public ItemType getType() {
        return ItemType.KANJI;
    }
}