package tr.xip.wanikani.api;

import android.content.Context;

import java.util.List;

import retrofit.RestAdapter;
import tr.xip.wanikani.api.error.RetrofitErrorHandler;
import tr.xip.wanikani.api.response.CriticalItemsList;
import tr.xip.wanikani.api.response.KanjiList;
import tr.xip.wanikani.api.response.LevelProgression;
import tr.xip.wanikani.api.response.RadicalsList;
import tr.xip.wanikani.api.response.RecentUnlocksList;
import tr.xip.wanikani.api.response.SRSDistribution;
import tr.xip.wanikani.api.response.StudyQueue;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.api.response.VocabularyList;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public class WaniKaniApi {
    private static final String API_HOST = "https://www.wanikani.com/api/user";

    Context context;
    WaniKaniService service;
    String API_KEY;

    public WaniKaniApi(Context context) {
        PrefManager prefManager = new PrefManager(context);
        API_KEY = prefManager.getApiKey();
        this.context = context;
        setupService();
    }

    private void setupService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_HOST)
                .setErrorHandler(new RetrofitErrorHandler(context))
                .build();

        service = restAdapter.create(WaniKaniService.class);
    }

    public User checkAPIKey(String key) {
        return service.getUser(key);
    }

    public User getUser() {
        return service.getUser(API_KEY);
    }

    public String getUsername() {
        return getUser().user_information.username;
    }

    public String getGravatar() {
        return getUser().user_information.gravatar;
    }

    public int getLevel() {
        return getUser().user_information.level;
    }

    public String getTitle() {
        return getUser().user_information.title;
    }

    public String getAbout() {
        return getUser().user_information.about;
    }

    public String getWebsite() {
        return getUser().user_information.website;
    }

    public String getTwitter() {
        return getUser().user_information.twitter;
    }

    public int getTopicsCount() {
        return getUser().user_information.topics_count;
    }

    public int getPostsCount() {
        return getUser().user_information.posts_count;
    }

    public long getCreationDate() {
        return getUser().user_information.creation_date * 1000;
    }

    public long getVacationDate() {
        return getUser().user_information.vacation_date * 1000;
    }

    public String getVacationDateString() {
        return getUser().user_information.vacation_date + "";
    }

    public boolean isVacationModeActive() {
        if (getVacationDateString().equals(null)) {
            return true;
        } else {
            return false;
        }
    }

    public StudyQueue getStudyQueue() {
        return service.getStudyQueue(API_KEY);
    }

    public int getLessonsAvailable() {
        return getStudyQueue().requested_information.lessons_available;
    }

    public int getReviewsAvailable() {
        return getStudyQueue().requested_information.reviews_available;
    }

    public long getNextReviewDate() {
        return getStudyQueue().requested_information.next_review_date * 1000;
    }

    public int getReviewsAvailableNextHour() {
        return getStudyQueue().requested_information.reviews_available_next_hour;
    }

    public int getReviewsAvailableNextDay() {
        return getStudyQueue().requested_information.reviews_available_next_day;
    }

    public LevelProgression getLevelProgression() {
        return service.getLevelProgression(API_KEY);
    }

    public int getRadicalsProgress() {
        return getLevelProgression().requested_information.radicals_progress;
    }

    public int getRadicalsTotal() {
        return getLevelProgression().requested_information.radicals_total;
    }

    public int getRadicalsPercentage() {
        double percentage;
        percentage = ((double) getRadicalsProgress() / getRadicalsTotal()) * 100;
        return (int) percentage;
    }

    public int getKanjiPercentage() {
        double percentage;
        percentage = ((double) getKanjiProgress() / getKanjiTotal()) * 100;
        return (int) percentage;
    }

    public int getKanjiProgress() {
        return getLevelProgression().requested_information.kanji_progress;
    }

    public int getKanjiTotal() {
        return getLevelProgression().requested_information.kanji_total;
    }

    public SRSDistribution getSRSDistribution() {
        return service.getSRSDistribution(API_KEY);
    }

    public int getApprenticeRadicalsCount() {
        return getSRSDistribution().requested_information.apprentice.radicals;
    }

    public int getApprenticeKanjiCount() {
        return getSRSDistribution().requested_information.apprentice.kanji;
    }

    public int getApprenticeVocabularyCount() {
        return getSRSDistribution().requested_information.apprentice.vocabulary;
    }

    public int getApprenticeTotalCount() {
        return getSRSDistribution().requested_information.apprentice.total;
    }

    public int getGuruRadicalsCount() {
        return getSRSDistribution().requested_information.guru.radicals;
    }

    public int getGuruKanjiCount() {
        return getSRSDistribution().requested_information.guru.kanji;
    }

    public int getGuruVocabularyCount() {
        return getSRSDistribution().requested_information.guru.vocabulary;
    }

    public int getGuruTotalCount() {
        return getSRSDistribution().requested_information.guru.total;
    }

    public int getMasterRadicalsCount() {
        return getSRSDistribution().requested_information.master.radicals;
    }

    public int getMasterKanjiCount() {
        return getSRSDistribution().requested_information.master.kanji;
    }

    public int getMasterVocabularyCount() {
        return getSRSDistribution().requested_information.master.vocabulary;
    }

    public int getMasterTotalCount() {
        return getSRSDistribution().requested_information.master.total;
    }

    public int getEnlightenRadicalsCount() {
        return getSRSDistribution().requested_information.enlighten.radicals;
    }

    public int getEnlightenKanjiCount() {
        return getSRSDistribution().requested_information.enlighten.kanji;
    }

    public int getEnlightenVocabularyCount() {
        return getSRSDistribution().requested_information.enlighten.vocabulary;
    }

    public int getEnlightenTotalCount() {
        return getSRSDistribution().requested_information.enlighten.total;
    }

    public int getBurnedRadicalsCount() {
        return getSRSDistribution().requested_information.burned.radicals;
    }

    public int getBurnedKanjiCount() {
        return getSRSDistribution().requested_information.burned.kanji;
    }

    public int getBurnedVocabularyCount() {
        return getSRSDistribution().requested_information.burned.vocabulary;
    }

    public int getBurnedTotalCount() {
        return getSRSDistribution().requested_information.burned.total;
    }

    public List<RecentUnlocksList.UnlockItem> getRecentUnlocksList(int limit) {
        return service.getRecentUnlocksList(API_KEY, limit).requested_information;
    }

    public List<CriticalItemsList.CriticalItem> getCriticalItemsList(int percentage) {
        return service.getCriticalItemsList(API_KEY, percentage).requested_information;
    }

    public List<RadicalsList.RadicalItem> getRadicalsList(String level) {
        return service.getRadicalsList(API_KEY, level).requested_information;
    }

    public List<KanjiList.KanjiItem> getKanjiList(String level) {
        return service.getKanjiList(API_KEY, level).requested_information;
    }

    public List<VocabularyList.VocabularyItem> getVocabularyList(String level) {
        return service.getVocabularyList(API_KEY, level).requested_information;
    }
}

