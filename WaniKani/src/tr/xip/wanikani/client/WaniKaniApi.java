package tr.xip.wanikani.client;

import android.content.Context;

import java.util.List;

import retrofit.RestAdapter;
import tr.xip.wanikani.client.error.RetrofitErrorHandler;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.models.User;
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

    public List<BaseItem> getRadicalsList(String level) {
        return service.getRadicalsList(API_KEY, level).getList();
    }

    public List<BaseItem> getKanjiList(String level) {
        return service.getKanjiList(API_KEY, level).getList();
    }

    public List<BaseItem> getVocabularyList(String level) {
        return service.getVocabularyList(API_KEY, level).getList();
    }
}

