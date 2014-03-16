package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xihsa_000 on 3/16/14.
 */
public class OfflineDataManager {
    private static SharedPreferences data;
    private static SharedPreferences.Editor editor;
    private static Context context;

    public OfflineDataManager(Context mContext) {
        context = mContext;
        data = context.getSharedPreferences("offline_data", 0);
        editor = data.edit();
    }

    public String getUsername() {
        return data.getString("username", "");
    }

    public String getGravatar() {
        return data.getString("gravatar", "");
    }

    public int getLevel() {
        return data.getInt("level", 0);
    }

    public String getTitle() {
        return data.getString("title", "");
    }

    public String getAbout() {
        return data.getString("about", "");
    }

    public String getWebsite() {
        return data.getString("website", "");
    }

    public String getTwitter() {
        return data.getString("twitter", "");
    }

    public int getTopicsCount() {
        return data.getInt("topics_count", 0);
    }

    public int getPostsCount() {
        return data.getInt("posts_count", 0);
    }

    public long getCreationDate() {
        return data.getLong("creation_date", 0);
    }

    public long getVacationDate() {
        return data.getLong("vacation_date", 0);
    }

    public String getVacationDateString() {
        return data.getString("vacation_date_string", "");
    }

    public boolean isVacationModeActive() {
        return data.getBoolean("vacation_mode", false);
    }

    public int getLessonsAvailable() {
        return data.getInt("lessons_available", 0);
    }

    public int getReviewsAvailable() {
        return data.getInt("reviews_available", 0);
    }

    public long getNextReviewDate() {
        return data.getLong("next_review_date", 0);
    }

    public int getReviewsAvailableNextHour() {
        return data.getInt("reviews_available_next_hour", 0);
    }

    public int getReviewsAvailableNextDay() {
        return data.getInt("reviews_available_next_day", 0);
    }

    public int getRadicalsProgress() {
        return data.getInt("radicals_progress", 0);
    }

    public int getRadicalsTotal() {
        return data.getInt("radicals_total", 0);
    }

    public int getRadicalsPercentage() {
        return data.getInt("radicals_percentage", 0);
    }

    public int getKanjiPercentage() {
        return data.getInt("kanji_percentage", 0);
    }

    public int getKanjiProgress() {
        return data.getInt("kanji_progress", 0);
    }

    public int getKanjiTotal() {
        return data.getInt("kanji_total", 0);
    }

    public int getApprenticeRadicalsCount() {
        return data.getInt("apprentice_radicals_count", 0);
    }

    public int getApprenticeKanjiCount() {
        return data.getInt("apprentice_kanji_count", 0);
    }

    public int getApprenticeVocabularyCount() {
        return data.getInt("apprentice_vocabulary_count", 0);
    }

    public int getApprenticeTotalCount() {
        return data.getInt("apprentice_total_count", 0);
    }

    public int getGuruRadicalsCount() {
        return data.getInt("guru_radicals_count", 0);
    }

    public int getGuruKanjiCount() {
        return data.getInt("guru_kanji_count", 0);
    }

    public int getGuruVocabularyCount() {
        return data.getInt("guru_vocabulary_count", 0);
    }

    public int getGuruTotalCount() {
        return data.getInt("guru_total_count", 0);
    }

    public int getMasterRadicalsCount() {
        return data.getInt("master_radicals_count", 0);
    }

    public int getMasterKanjiCount() {
        return data.getInt("master_kanji_count", 0);
    }

    public int getMasterVocabularyCount() {
        return data.getInt("master_vocabulary_count", 0);
    }

    public int getMasterTotalCount() {
        return data.getInt("master_total_count", 0);
    }

    public int getEnlightenRadicalsCount() {
        return data.getInt("enlighten_radicals_count", 0);
    }

    public int getEnlightenKanjiCount() {
        return data.getInt("enlighten_kanji_count", 0);
    }

    public int getEnlightenVocabularyCount() {
        return data.getInt("enlighten_vocabulary_count", 0);
    }

    public int getEnlightenTotalCount() {
        return data.getInt("enlighten_total_count", 0);
    }

    public int getBurnedRadicalsCount() {
        return data.getInt("burned_radicals_count", 0);
    }

    public int getBurnedKanjiCount() {
        return data.getInt("burned_kanji_count", 0);
    }

    public int getBurnedVocabularyCount() {
        return data.getInt("burned_vocabulary_count", 0);
    }

    public int getBurnedTotalCount() {
        return data.getInt("burned_total_count", 0);
    }
}
