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

    public void setUsername(String username) {
        editor.putString("username", username).commit();
    }

    public String getUsername() {
        return data.getString("username", "");
    }

    public String getGravatar() {
        return data.getString("gravatar", "");
    }

    public void setGravatar(String gravatar) {
        editor.putString("gravatar", gravatar).commit();
    }

    public int getLevel() {
        return data.getInt("level", 0);
    }

    public void setLevel(int level) {
        editor.putInt("level", level).commit();
    }

    public String getTitle() {
        return data.getString("title", "");
    }

    public void setTitle(String title) {
        editor.putString("title", title).commit();
    }

    public String getAbout() {
        return data.getString("about", "");
    }

    public void setAbout(String about) {
        editor.putString("about", about).commit();
    }

    public String getWebsite() {
        return data.getString("website", "");
    }

    public void setWebsite(String website) {
        editor.putString("website", website).commit();
    }

    public String getTwitter() {
        return data.getString("twitter", "");
    }

    public void setTwitter(String twitter) {
        editor.putString("twitter", twitter).commit();

    }

    public int getTopicsCount() {
        return data.getInt("topics_count", 0);
    }

    public void setTopicsCount(int topics) {
        editor.putInt("topics_count", topics).commit();
    }

    public int getPostsCount() {
        return data.getInt("posts_count", 0);
    }

    public void setPostsCount(int posts) {
        editor.putInt("posts_count", posts).commit();
    }

    public long getCreationDate() {
        return data.getLong("creation_date", 0);
    }

    public void setCreationDate(long creationdate) {
        editor.putLong("creation_date", creationdate).commit();
    }

    public long getVacationDate() {
        return data.getLong("vacation_date", 0);
    }

    public void setVacationDate(long vacationdate) {
        editor.putLong("vacation_date", vacationdate).commit();
    }

    public String getVacationDateString() {
        return data.getString("vacation_date_string", "");
    }

    public void setVacationDateString(String vacationdatestring) {
        editor.putString("vacation_date_string", vacationdatestring).commit();
    }

    public boolean isVacationModeActive() {
        return data.getBoolean("vacation_mode", false);
    }

    public void setVacationModeActive(boolean value) {
        editor.putBoolean("vacation_mode", value).commit();
    }

    public int getLessonsAvailable() {
        return data.getInt("lessons_available", 0);
    }

    public void setLessonsAvailable(int lessons) {
        editor.putInt("lessons_available", lessons).commit();
    }

    public int getReviewsAvailable() {
        return data.getInt("reviews_available", 0);
    }

    public void setReviewsAvailable(int reviews) {
        editor.putInt("reviews_available", reviews).commit();
    }

    public long getNextReviewDate() {
        return data.getLong("next_review_date", 0);
    }

    public void setNextReviewDate(long nextreviewdate) {
        editor.putLong("next_review_date", nextreviewdate).commit();
    }

    public int getReviewsAvailableNextHour() {
        return data.getInt("reviews_available_next_hour", 0);
    }

    public void setReviewsAvailableNextHour(int reviews) {
        editor.putInt("reviews_available_next_hour", reviews).commit();
    }

    public int getReviewsAvailableNextDay() {
        return data.getInt("reviews_available_next_day", 0);
    }

    public void setReviewsAvailableNextDay(int reviews) {
        editor.putInt("reviews_available_next_day", reviews).commit();
    }

    public int getRadicalsProgress() {
        return data.getInt("radicals_progress", 0);
    }

    public void setRadicalsProgress(int progress) {
        editor.putInt("radicals_progress", progress).commit();
    }

    public int getRadicalsTotal() {
        return data.getInt("radicals_total", 0);
    }

    public void setRadicalsTotal(int total) {
        editor.putInt("radicals_total", total).commit();
    }

    public int getRadicalsPercentage() {
        return data.getInt("radicals_percentage", 0);
    }

    public void setRadicalsPercentage(int percentage) {
        editor.putInt("radicals_percentage", percentage).commit();
    }

    public int getKanjiPercentage() {
        return data.getInt("kanji_percentage", 0);
    }

    public void setKanjiPercentage(int percentage) {
        editor.putInt("kanji_percentage", percentage).commit();
    }

    public int getKanjiProgress() {
        return data.getInt("kanji_progress", 0);
    }

    public void setKanjiProgress(int progress) {
        editor.putInt("kanji_progress", progress).commit();
    }

    public int getKanjiTotal() {
        return data.getInt("kanji_total", 0);
    }

    public void setKanjiTotal(int total) {
        editor.putInt("kanji_total", total).commit();
    }

    public int getApprenticeRadicalsCount() {
        return data.getInt("apprentice_radicals_count", 0);
    }

    public void setApprenticeRadicalsCount(int radicals) {
        editor.putInt("apprentice_radicals_count", radicals).commit();
    }

    public int getApprenticeKanjiCount() {
        return data.getInt("apprentice_kanji_count", 0);
    }

    public void setApprenticeKanjiCount(int kanji) {
        editor.putInt("apprentice_kanji_count", kanji).commit();
    }

    public int getApprenticeVocabularyCount() {
        return data.getInt("apprentice_vocabulary_count", 0);
    }

    public void setApprentiveVocabularyCount(int vocabulary) {
        editor.putInt("apprentice_vocabulary_count", vocabulary).commit();
    }

    public int getApprenticeTotalCount() {
        return data.getInt("apprentice_total_count", 0);
    }

    public void setApprenticeTotalCount(int total) {
        editor.putInt("apprentice_total_count", total).commit();
    }

    public int getGuruRadicalsCount() {
        return data.getInt("guru_radicals_count", 0);
    }

    public void setGuruRadicalsCount(int radicals) {
        editor.putInt("guru_radicals_count", radicals).commit();
    }

    public int getGuruKanjiCount() {
        return data.getInt("guru_kanji_count", 0);
    }

    public void setGuruKanjiCount(int kanji) {
        editor.putInt("guru_kanji_count", kanji).commit();
    }

    public int getGuruVocabularyCount() {
        return data.getInt("guru_vocabulary_count", 0);
    }

    public void setGuruVocabularyCount(int vocabulary) {
        editor.putInt("guru_vocabulary_count", vocabulary).commit();
    }

    public int getGuruTotalCount() {
        return data.getInt("guru_total_count", 0);
    }

    public void setGuruTotalCount(int total) {
        editor.putInt("guru_total_count", total).commit();
    }

    public int getMasterRadicalsCount() {
        return data.getInt("master_radicals_count", 0);
    }

    public void setMasterRadicalsCount(int radicals) {
        editor.putInt("master_radicals_count", radicals).commit();
    }

    public int getMasterKanjiCount() {
        return data.getInt("master_kanji_count", 0);
    }

    public void setMasterKanjiCount(int kanji) {
        editor.putInt("master_kanji_count", kanji).commit();
    }

    public int getMasterVocabularyCount() {
        return data.getInt("master_vocabulary_count", 0);
    }

    public void setMasterVocabularyCount(int vocabulary) {
        editor.putInt("master_vocabulary_count", vocabulary).commit();
    }

    public int getMasterTotalCount() {
        return data.getInt("master_total_count", 0);
    }

    public void setMasterTotalCount(int total) {
        editor.putInt("master_total_count", total).commit();
    }

    public int getEnlightenRadicalsCount() {
        return data.getInt("enlighten_radicals_count", 0);
    }

    public void setEnlightenRadicalsCount(int radicals) {
        editor.putInt("enlighten_radicals_count", radicals).commit();
    }

    public int getEnlightenKanjiCount() {
        return data.getInt("enlighten_kanji_count", 0);
    }

    public void setEnlightenKanjiCount(int kanji) {
        editor.putInt("enlighten_kanji_count", kanji).commit();
    }

    public int getEnlightenVocabularyCount() {
        return data.getInt("enlighten_vocabulary_count", 0);
    }

    public void setEnlightenVocabularyCount(int vocabulary) {
        editor.putInt("enlighten_vocabulary_count", vocabulary).commit();
    }

    public int getEnlightenTotalCount() {
        return data.getInt("enlighten_total_count", 0);
    }

    public void setEnlightenTotalCount(int total) {
        editor.putInt("enlighten_total_count", total).commit();
    }

    public int getBurnedRadicalsCount() {
        return data.getInt("burned_radicals_count", 0);
    }

    public void setBurnedRadicalsCount(int radicals) {
        editor.putInt("burned_radicals_count", radicals).commit();
    }

    public int getBurnedKanjiCount() {
        return data.getInt("burned_kanji_count", 0);
    }

    public void setBurnedKanjiCount(int kanji) {
        editor.putInt("burned_kanji_count", kanji).commit();
    }

    public int getBurnedVocabularyCount() {
        return data.getInt("burned_vocabulary_count", 0);
    }

    public void setBurnedVocabularyCount(int vocabulary) {
        editor.putInt("burned_vocabulary_count", vocabulary).commit();
    }

    public int getBurnedTotalCount() {
        return data.getInt("burned_total_count", 0);
    }

    public void setBurnedTotalCount(int total) {
        editor.putInt("burned_total_count", total).commit();
    }
}
