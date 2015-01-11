package tr.xip.wanikani.api.response;

import java.io.Serializable;

/**
 * Created by xihsa_000 on 3/12/14.
 */
public class LevelProgression implements Serializable {

    private int id;

    private UserInfo user_information;
    private RequestedInformation requested_information;

    public LevelProgression(int id,
                            UserInfo userInfo,
                            int radicalsProgress,
                            int radicalsTotal,
                            int kanjiProgress,
                            int kanjiTotal) {
        this.id = id;
        this.user_information = userInfo;
        this.requested_information = new RequestedInformation(
                radicalsProgress,
                radicalsTotal,
                kanjiProgress,
                kanjiTotal
        );
    }

    public UserInfo getUserInfo() {
        return user_information;
    }

    public int getRadicalsProgress() {
        return requested_information.radicals_progress;
    }

    public int getRadicalsTotal() {
        return requested_information.radicals_total;
    }

    public int getRadicalsPercentage() {
        return (int) ((double) requested_information.radicals_progress / requested_information.radicals_total * 100);
    }

    public int getKanjiProgress() {
        return requested_information.kanji_progress;
    }

    public int getKanjiTotal() {
        return requested_information.kanji_total;
    }

    public int getKanjiPercentage() {
        return (int) ((double) requested_information.kanji_progress / requested_information.kanji_total * 100);
    }

    private class RequestedInformation implements Serializable {
        private int radicals_progress;
        private int radicals_total;
        private int kanji_progress;
        private int kanji_total;

        public RequestedInformation(int radicalsProgress, int radicalsTotal, int kanjiProgress, int kanjiTotal) {
            this.radicals_progress = radicalsProgress;
            this.radicals_total = radicalsTotal;
            this.kanji_progress = kanjiProgress;
            this.kanji_total = kanjiTotal;
        }
    }
}
