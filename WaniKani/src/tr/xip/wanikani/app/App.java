package tr.xip.wanikani.app;

import android.app.Application;

import tr.xip.wanikani.database.DatabaseManager;

public class App extends Application {

    @Override
    public void onCreate() {
        DatabaseManager.init(getApplicationContext());
        super.onCreate();
    }
}
