package tr.xip.wanikani.api.response;

public class VocabularyItem extends BaseItem {
    @Override
    public ItemType getType() {
        return ItemType.VOCABULARY;
    }
}