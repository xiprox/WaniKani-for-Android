package tr.xip.wanikani.api;

import retrofit.http.GET;
import retrofit.http.Path;
import tr.xip.wanikani.api.response.CriticalItemsList;
import tr.xip.wanikani.api.response.KanjiList;
import tr.xip.wanikani.api.response.LevelProgression;
import tr.xip.wanikani.api.response.RadicalsList;
import tr.xip.wanikani.api.response.RecentUnlocksList;
import tr.xip.wanikani.api.response.SRSDistribution;
import tr.xip.wanikani.api.response.StudyQueue;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.api.response.VocabularyList;

/**
 * Created by xihsa_000 on 3/11/14.
 */
public interface WaniKaniService {

    @GET("/{api_key}/user-information")
    User getUser(@Path("api_key") String api_key);

    @GET("/{api_key}/study-queue")
    StudyQueue getStudyQueue(@Path("api_key") String api_key);

    @GET("/{api_key}/level-progression")
    LevelProgression getLevelProgression(@Path("api_key") String api_key);

    @GET("/{api_key}/srs-distribution")
    SRSDistribution getSRSDistribution(@Path("api_key") String api_key);

    @GET("/{api_key}/recent-unlocks/{limit}")
    RecentUnlocksList getRecentUnlocksList(@Path("api_key") String api_key, @Path("limit") int limit);

    @GET("/{api_key}/critical-items/{percentage}")
    CriticalItemsList getCriticalItemsList(@Path("api_key") String api_key, @Path("percentage") int percentage);

    @GET("/{api_key}/radicals/{level}")
    RadicalsList getRadicalsList(@Path("api_key") String api_key, @Path("level") String level); // We use a string to handle the level argument as the API accepts comma-delimited level argument

    @GET("/{api_key}/kanji/{level}")
    KanjiList getKanjiList(@Path("api_key") String api_key, @Path("level") String level); // We use a string to handle the level argument as the API accepts comma-delimited level argument

    @GET("/{api_key}/vocabulary/{level}")
    VocabularyList getVocabularyList(@Path("api_key") String api_key, @Path("level") String level); // We use a string to handle the level argument as the API accepts comma-delimited level argument

}
