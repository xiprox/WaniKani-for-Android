package tr.xip.wanikani.api.response;

import android.content.Context;

import tr.xip.wanikani.managers.OfflineDataManager;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class LevelProgression {
    public UserInfo user_information;
    public RequestedInformation requested_information;

    public class RequestedInformation {
        public int radicals_progress;
        public int radicals_total;
        public int kanji_progress;
        public int kanji_total;
    }

    public int getRadicalsProgress(Context context) {
        new OfflineDataManager(context).setRadicalsProgress(requested_information.radicals_progress);
        return requested_information.radicals_progress;
    }

    public int getRadicalsTotal(Context context) {
        new OfflineDataManager(context).setRadicalsTotal(requested_information.radicals_total);
        return requested_information.radicals_total;
    }

    public int getRadicalsPercentage(Context context) {
        double percentage;
        percentage = ((double) getRadicalsProgress(context) / getRadicalsTotal(context)) * 100;
        new OfflineDataManager(context).setRadicalsPercentage((int) percentage);
        return (int) percentage;
    }

    public int getKanjiProgress(Context context) {
        new OfflineDataManager(context).setKanjiProgress(requested_information.kanji_progress);
        return requested_information.kanji_progress;
    }

    public int getKanjiTotal(Context context) {
        new OfflineDataManager(context).setKanjiTotal(requested_information.kanji_total);
        return requested_information.kanji_total;
    }

    public int getKanjiPercentage(Context context) {
        double percentage;
        percentage = ((double) getKanjiProgress(context) / getKanjiTotal(context)) * 100;
        new OfflineDataManager(context).setKanjiPercentage((int) percentage);
        return (int) percentage;
    }
}
