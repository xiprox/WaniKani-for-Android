package tr.xip.wanikani.client;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tr.xip.wanikani.BuildConfig;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.CriticalItemsList;
import tr.xip.wanikani.models.KanjiList;
import tr.xip.wanikani.models.LevelProgression;
import tr.xip.wanikani.models.RadicalsList;
import tr.xip.wanikani.models.RecentUnlocksList;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.models.VocabularyList;

public abstract class WaniKaniApi {
    private static final String API_HOST = "https://www.wanikani.com/api/user/";

    private static WaniKaniService service;
    private static String API_KEY;

    static {
        init();
    }

    public static void init() {
        API_KEY = PrefManager.getApiKey();
        setupService();
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

    public static Call<Request<User>> getUser() {
        return service.getUser(API_KEY);
    }

    public static Call<Request<User>> getUser(String apiKey) {
        return service.getUser(apiKey);
    }

    public static Call<Request<StudyQueue>> getStudyQueue() {
        return service.getStudyQueue(API_KEY);
    }

    public static Call<Request<LevelProgression>> getLevelProgression() {
        return service.getLevelProgression(API_KEY);
    }

    public static Call<Request<SRSDistribution>> getSRSDistribution() {
        return service.getSRSDistribution(API_KEY);
    }

    public static Call<Request<RecentUnlocksList>> getRecentUnlocksList(int limit) {
        return service.getRecentUnlocksList(API_KEY, limit);
    }

    public static Call<Request<CriticalItemsList>> getCriticalItemsList(int percentage) {
        return service.getCriticalItemsList(API_KEY, percentage);
    }

    public static Call<Request<RadicalsList>> getRadicalsList(String level) {
        return service.getRadicalsList(API_KEY, level);
    }

    public static Call<Request<KanjiList>> getKanjiList(String level) {
        return service.getKanjiList(API_KEY, level);
    }

    public static Call<Request<VocabularyList>> getVocabularyList(String level) {
        return service.getVocabularyList(API_KEY, level);
    }
}

