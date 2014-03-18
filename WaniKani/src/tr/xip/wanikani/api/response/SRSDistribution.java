package tr.xip.wanikani.api.response;

import android.content.Context;

import tr.xip.wanikani.managers.OfflineDataManager;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class SRSDistribution {
    public UserInfo user_information;
    public RequestedInformation requested_information;

    public class RequestedInformation {
        public Level apprentice;
        public Level guru;
        public Level master;
        public Level enlighten;
        public Level burned;

        public class Level {
            public int radicals;
            public int kanji;
            public int vocabulary;
            public int total;
        }
    }

    public int getApprenticeRadicalsCount(Context context) {
        new OfflineDataManager(context).setApprenticeRadicalsCount(requested_information.apprentice.radicals);
        return requested_information.apprentice.radicals;
    }

    public int getApprenticeKanjiCount(Context context) {
        new OfflineDataManager(context).setApprenticeKanjiCount(requested_information.apprentice.kanji);
        return requested_information.apprentice.kanji;
    }

    public int getApprenticeVocabularyCount(Context context) {
        new OfflineDataManager(context).setApprentiveVocabularyCount(requested_information.apprentice.vocabulary);
        return requested_information.apprentice.vocabulary;
    }

    public int getApprenticeTotalCount(Context context) {
        new OfflineDataManager(context).setApprenticeTotalCount(requested_information.apprentice.total);
        return requested_information.apprentice.total;
    }

    public int getGuruRadicalsCount(Context context) {
        new OfflineDataManager(context).setGuruRadicalsCount(requested_information.guru.radicals);
        return requested_information.guru.radicals;
    }

    public int getGuruKanjiCount(Context context) {
        new OfflineDataManager(context).setGuruKanjiCount(requested_information.guru.kanji);
        return requested_information.guru.kanji;
    }

    public int getGuruVocabularyCount(Context context) {
        new OfflineDataManager(context).setGuruVocabularyCount(requested_information.guru.vocabulary);
        return requested_information.guru.vocabulary;
    }

    public int getGuruTotalCount(Context context) {
        new OfflineDataManager(context).setGuruTotalCount(requested_information.guru.total);
        return requested_information.guru.total;
    }

    public int getMasterRadicalsCount(Context context) {
        new OfflineDataManager(context).setMasterRadicalsCount(requested_information.master.radicals);
        return requested_information.master.radicals;
    }

    public int getMasterKanjiCount(Context context) {
        new OfflineDataManager(context).setMasterKanjiCount(requested_information.master.kanji);
        return requested_information.master.kanji;
    }

    public int getMasterVocabularyCount(Context context) {
        new OfflineDataManager(context).setMasterVocabularyCount(requested_information.master.vocabulary);
        return requested_information.master.vocabulary;
    }

    public int getMasterTotalCount(Context context) {
        new OfflineDataManager(context).setMasterTotalCount(requested_information.master.total);
        return requested_information.master.total;
    }

    public int getEnlightenRadicalsCount(Context context) {
        new OfflineDataManager(context).setEnlightenRadicalsCount(requested_information.enlighten.radicals);
        return requested_information.enlighten.radicals;
    }

    public int getEnlightenKanjiCount(Context context) {
        new OfflineDataManager(context).setEnlightenKanjiCount(requested_information.enlighten.kanji);
        return requested_information.enlighten.kanji;
    }

    public int getEnlightenVocabularyCount(Context context) {
        new OfflineDataManager(context).setEnlightenVocabularyCount(requested_information.enlighten.vocabulary);
        return requested_information.enlighten.vocabulary;
    }

    public int getEnlightenTotalCount(Context context) {
        new OfflineDataManager(context).setEnlightenTotalCount(requested_information.enlighten.total);
        return requested_information.enlighten.total;
    }

    public int getBurnedRadicalsCount(Context context) {
        new OfflineDataManager(context).setBurnedRadicalsCount(requested_information.burned.radicals);
        return requested_information.burned.radicals;
    }

    public int getBurnedKanjiCount(Context context) {
        new OfflineDataManager(context).setBurnedKanjiCount(requested_information.burned.kanji);
        return requested_information.burned.kanji;
    }

    public int getBurnedVocabularyCount(Context context) {
        new OfflineDataManager(context).setBurnedVocabularyCount(requested_information.burned.vocabulary);
        return requested_information.burned.vocabulary;
    }

    public int getBurnedTotalCount(Context context) {
        new OfflineDataManager(context).setBurnedTotalCount(requested_information.burned.total);
        return requested_information.burned.total;
    }
}
