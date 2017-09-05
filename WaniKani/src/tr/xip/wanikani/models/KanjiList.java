package tr.xip.wanikani.models;

public class KanjiList extends ItemsList {

    @Override
    protected BaseItem.ItemType getType() {
        return BaseItem.ItemType.KANJI;
    }
}
