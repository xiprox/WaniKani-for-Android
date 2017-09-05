package tr.xip.wanikani.models;

public class VocabularyList extends ItemsList {

    @Override
    protected BaseItem.ItemType getType() {
        return BaseItem.ItemType.VOCABULARY;
    }
}
