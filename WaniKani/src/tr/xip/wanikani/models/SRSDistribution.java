package tr.xip.wanikani.models;

import java.io.Serializable;

import tr.xip.wanikani.database.DatabaseManager;

public class SRSDistribution implements Serializable, Storable {
    public int id;
    public Level apprentice;
    public Level guru;
    public Level master;
    public Level enlighten;
    public Level burned;

    public SRSDistribution(int id,
                           int apprenticeRadicals,
                           int apprenticeKanji,
                           int apprenticeVocabulary,
                           int guruRadicals,
                           int guruKanji,
                           int guruVocabulary,
                           int masterRadicals,
                           int masterKanji,
                           int masterVocabulary,
                           int enlightenedRadicals,
                           int enlightenedKanji,
                           int enlightenedVocabulary,
                           int burnedRadicals,
                           int burnedKanji,
                           int burnedVocabulary) {
        this.id = id;
        this.apprentice = new Level(apprenticeRadicals, apprenticeKanji, apprenticeVocabulary);
        this.guru = new Level(guruRadicals, guruKanji, guruVocabulary);
        this.master = new Level(masterRadicals, masterKanji, masterVocabulary);
        this.enlighten = new Level(enlightenedRadicals, enlightenedKanji, enlightenedVocabulary);
        this.burned = new Level(burnedRadicals, burnedKanji, burnedVocabulary);
    }

    public int total() {
        return apprentice.total + guru.total + master.total + enlighten.total + burned.total;
    }

    @Override
    public void save() {
        DatabaseManager.saveSrsDistribution(this);
    }

    public class Level implements Serializable {
        public int radicals;
        public int kanji;
        public int vocabulary;
        public int total;

        public Level(int radicals, int kanji, int vocabulary) {
            this.radicals = radicals;
            this.kanji = kanji;
            this.vocabulary = vocabulary;
            this.total = radicals + kanji + vocabulary;
        }
    }
}
