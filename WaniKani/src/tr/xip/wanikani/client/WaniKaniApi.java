package tr.xip.wanikani.client;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tr.xip.wanikani.BuildConfig;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.models.User;

public class WaniKaniApi {
    private static final String API_HOST = "https://www.wanikani.com/api/user";

    private static WaniKaniService service;
    private static String API_KEY;

    static {
        if (API_KEY == null) {
            API_KEY = PrefManager.getApiKey();
        }
        if (service == null) {
            setupService();
        }
    }

    private static void setupService() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_HOST)
                .build();

        service = retrofit.create(WaniKaniService.class);
    }

    public boolean isApiKeyValid(String key) {
        boolean result = false;

        try {
            User res = service.getUser(key).execute().body();
            if (res != null && res.user_information != null) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
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

