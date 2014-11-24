package tr.xip.wanikani.api;

import android.content.Context;

import java.util.List;

import retrofit.RestAdapter;
import tr.xip.wanikani.api.error.RetrofitErrorHandler;
import tr.xip.wanikani.api.response.CriticalItem;
import tr.xip.wanikani.api.response.KanjiItem;
import tr.xip.wanikani.api.response.LevelProgression;
import tr.xip.wanikani.api.response.RadicalItem;
import tr.xip.wanikani.api.response.SRSDistribution;
import tr.xip.wanikani.api.response.StudyQueue;
import tr.xip.wanikani.api.response.UnlockItem;
import tr.xip.wanikani.api.response.User;
import tr.xip.wanikani.api.response.UserInfo;
import tr.xip.wanikani.api.response.VocabularyItem;
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

    public boolean isApiKeyValid(String key) {
        return service.getUser(key).user_information != null;
    }

    public User getUser() {
        return service.getUser(API_KEY);
    }

    public boolean isVacationModeActive(UserInfo userInfo) {
        return userInfo.getVacationDate() != 0;
    }

    public StudyQueue getStudyQueue() {
        return service.getStudyQueue(API_KEY);
    }

    public LevelProgression getLevelProgression() {
        return service.getLevelProgression(API_KEY);
    }

    public SRSDistribution getSRSDistribution() {
        return service.getSRSDistribution(API_KEY);
    }

    public List<UnlockItem> getRecentUnlocksList(int limit) {
        return service.getRecentUnlocksList(API_KEY, limit).getList();
    }

    public List<CriticalItem> getCriticalItemsList(int percentage) {
        return service.getCriticalItemsList(API_KEY, percentage).getList();
    }

    public List<RadicalItem> getRadicalsList(String level) {
        return service.getRadicalsList(API_KEY, level).getList();
    }

    public List<KanjiItem> getKanjiList(String level) {
        return service.getKanjiList(API_KEY, level).getList();
    }

    public List<VocabularyItem> getVocabularyList(String level) {
        return service.getVocabularyList(API_KEY, level).getList();
    }
}

