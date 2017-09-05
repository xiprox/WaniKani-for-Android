package tr.xip.wanikani.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.managers.PrefManager;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        DatabaseManager.init(getApplicationContext());
        PrefManager.init(getApplicationContext());
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
