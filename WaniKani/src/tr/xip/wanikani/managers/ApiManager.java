package tr.xip.wanikani.managers;

import android.content.Context;
import android.content.SharedPreferences;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.utils.Utils;


/**
 * Created by xihsa_000 on 3/15/14.
 */
public class ApiManager {
    private static SharedPreferences data;
    private static SharedPreferences.Editor editor;
    private static Context context;

    WaniKaniApi api;
    Utils utils;

    public ApiManager(Context mContext) {
        context = mContext;
        data = context.getSharedPreferences("offline_data", 0);
        editor = data.edit();
        api = new WaniKaniApi(context);
        utils = new Utils(context);
    }

    public String getUsername() {
        String username = "";
        username = api.getUsername();
        editor.putString("username", username);
        editor.commit();
        return username;
    }

    public String getGravatar() {
        String gravatar = "";
        gravatar = api.getGravatar();
        editor.putString("gravatar", gravatar);
        editor.commit();
        return gravatar;
    }

    public int getLevel() {
        int level = 0;
        level = api.getLevel();
        editor.putInt("level", level);
        editor.commit();
        return level;
    }

    public String getTitle() {
        String title;
        title = api.getTitle();
        editor.putString("title", title);
        editor.commit();
        return title;
    }

    public String getAbout() {
        String about;
        about = api.getAbout();
        editor.putString("about", about);
        editor.commit();
        return about;
    }

    public String getWebsite() {
        String website = "";
        website = api.getWebsite();
        editor.putString("website", website);
        editor.commit();
        return website;
    }

    public String getTwitter() {
        String twitter = "";
        twitter = api.getTwitter();
        editor.putString("twitter", twitter);
        editor.commit();
        return twitter;
    }

    public int getTopicsCount() {
        int topics = 0;
        topics = api.getTopicsCount();
        editor.putInt("topics_count", topics);
        editor.commit();
        return topics;
    }

    public int getPostsCount() {
        int posts = 0;
        posts = api.getPostsCount();
        editor.putInt("posts_count", posts);
        editor.commit();
        return posts;
    }

    public long getCreationDate() {
        long date = 0;
        date = api.getCreationDate();
        editor.putLong("creation_date", date);
        editor.commit();
        return date;
    }

    public long getVacationDate() {
        long date = 0;
        date = api.getVacationDate();
        editor.putLong("vacation_date", date);
        editor.commit();
        return date;
    }

    public String getVacationDateString() {
        String date = "";
        date = api.getVacationDateString();
        editor.putString("vacation_date_string", date);
        editor.commit();
        return date;
    }

    public boolean isVacationModeActive() {
        boolean mode = false;
        mode = api.isVacationModeActive();
        editor.putBoolean("vacation_mode", mode);
        editor.commit();
        return mode;
    }

    public int getLessonsAvailable() {
        int lessons = 0;
        lessons = api.getLessonsAvailable();
        editor.putInt("lessons_available", lessons);
        editor.commit();
        return lessons;
    }

    public int getReviewsAvailable() {
        int reviews = 0;
        reviews = api.getReviewsAvailable();
        editor.putInt("reviews_available", reviews);
        editor.commit();
        return reviews;
    }

    public long getNextReviewDate() {
        long date = 0;
        date = api.getNextReviewDate();
        editor.putLong("next_review_date", date);
        editor.commit();
        return date;
    }

    public int getReviewsAvailableNextHour() {
        int reviews = 0;
        reviews = api.getReviewsAvailableNextHour();
        editor.putInt("reviews_available_next_hour", reviews);
        editor.commit();
        return reviews;
    }

    public int getReviewsAvailableNextDay() {
        int reviews = 0;
        reviews = api.getReviewsAvailableNextDay();
        editor.putInt("reviews_available_next_day", reviews);
        editor.commit();
        return reviews;
    }

    public int getRadicalsProgress() {
        int progress = 0;
        progress = api.getRadicalsProgress();
        editor.putInt("radicals_progress", progress);
        editor.commit();
        return progress;
    }

    public int getRadicalsTotal() {
        int total = 0;
        total = api.getRadicalsTotal();
        editor.putInt("radicals_total", total);
        editor.commit();
        return total;
    }

    public int getRadicalsPercentage() {
        int percentage = 0;
        percentage = api.getRadicalsPercentage();
        editor.putInt("radicals_percentage", percentage);
        editor.commit();
        return percentage;
    }

    public int getKanjiPercentage() {
        int percentage = 0;
        percentage = api.getKanjiPercentage();
        editor.putInt("kanji_percentage", percentage);
        editor.commit();
        return percentage;
    }

    public int getKanjiProgress() {
        int progress = 0;
        progress = api.getKanjiProgress();
        editor.putInt("kanji_progress", progress);
        editor.commit();
        return progress;
    }

    public int getKanjiTotal() {
        int total = 0;
        total = api.getKanjiTotal();
        editor.putInt("kanji_total", total);
        editor.commit();
        return total;
    }

    public int getApprenticeRadicalsCount() {
        int count = 0;
        count = api.getApprenticeRadicalsCount();
        editor.putInt("apprentice_radicals_count", count);
        editor.commit();
        return count;
    }

    public int getApprenticeKanjiCount() {
        int count = 0;
        count = api.getApprenticeKanjiCount();
        editor.putInt("apprentice_kanji_count", count);
        editor.commit();
        return count;
    }

    public int getApprenticeVocabularyCount() {
        int count = 0;
        count = api.getApprenticeVocabularyCount();
        editor.putInt("apprentice_vocabulary_count", count);
        editor.commit();
        return count;
    }

    public int getApprenticeTotalCount() {
        int count = 0;
        count = api.getApprenticeTotalCount();
        editor.putInt("apprentice_total_count", count);
        editor.commit();
        return count;
    }

    public int getGuruRadicalsCount() {
        int count = 0;
        count = api.getGuruRadicalsCount();
        editor.putInt("guru_radicals_count", count);
        editor.commit();
        return count;
    }

    public int getGuruKanjiCount() {
        int count = 0;
        count = api.getGuruKanjiCount();
        editor.putInt("guru_kanji_count", count);
        editor.commit();
        return count;
    }

    public int getGuruVocabularyCount() {
        int count = 0;
        count = api.getGuruVocabularyCount();
        editor.putInt("guru_vocabulary_count", count);
        editor.commit();
        return count;
    }

    public int getGuruTotalCount() {
        int count = 0;
        count = api.getGuruTotalCount();
        editor.putInt("guru_total_count", count);
        editor.commit();
        return count;
    }

    public int getMasterRadicalsCount() {
        int count = 0;
        count = api.getMasterRadicalsCount();
        editor.putInt("master_radicals_count", count);
        editor.commit();
        return count;
    }

    public int getMasterKanjiCount() {
        int count = 0;
        count = api.getMasterKanjiCount();
        editor.putInt("master_kanji_count", count);
        editor.commit();
        return count;
    }

    public int getMasterVocabularyCount() {
        int count = 0;
        count = api.getMasterVocabularyCount();
        editor.putInt("master_vocabulary_count", count);
        editor.commit();
        return count;
    }

    public int getMasterTotalCount() {
        int count = 0;
        count = api.getMasterTotalCount();
        editor.putInt("master_total_count", count);
        editor.commit();
        return count;
    }

    public int getEnlightenRadicalsCount() {
        int count = 0;
        count = api.getEnlightenRadicalsCount();
        editor.putInt("enlighten_radicals_count", count);
        editor.commit();

        return count;
    }

    public int getEnlightenKanjiCount() {
        int count = 0;
        count = api.getEnlightenKanjiCount();
        editor.putInt("enlighten_kanji_count", count);
        editor.commit();
        return count;
    }

    public int getEnlightenVocabularyCount() {
        int count = 0;
        count = api.getEnlightenVocabularyCount();
        editor.putInt("enlighten_vocabulary_count", count);
        editor.commit();
        return count;
    }

    public int getEnlightenTotalCount() {
        int count = 0;
        count = api.getEnlightenTotalCount();
        editor.putInt("enlighten_total_count", count);
        editor.commit();
        return count;
    }

    public int getBurnedRadicalsCount() {
        int count = 0;
        count = api.getBurnedRadicalsCount();
        editor.putInt("burned_radicals_count", count);
        editor.commit();
        return count;
    }

    public int getBurnedKanjiCount() {
        int count = 0;
        count = api.getBurnedKanjiCount();
        editor.putInt("burned_kanji_count", count);
        editor.commit();
        return count;
    }

    public int getBurnedVocabularyCount() {
        int count = 0;
        count = api.getBurnedVocabularyCount();
        editor.putInt("burned_vocabulary_count", count);
        editor.commit();
        return count;
    }

    public int getBurnedTotalCount() {
        int count = 0;
        count = api.getBurnedTotalCount();
        editor.putInt("burned_total_count", count);
        editor.commit();
        return count;
    }

}
