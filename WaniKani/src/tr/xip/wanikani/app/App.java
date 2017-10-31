package tr.xip.wanikani.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.solovyev.android.checkout.Billing;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.managers.PrefManager;

public class App extends Application {

    private static App instance;

    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Override
        public String getPublicKey() {
            return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsKCnkO5/8UsJ0fnXpYi2dxYIkOknG/2KhrGp/k2LGvwx07T4lLIEdSlo6bE785b2vFfG9wMY2tNmwmbBjjVlixVDTMFydbMGRoE2Nd9dQ4Fq+oVlC7SvuAamLJ6atKOAPd2g4Xr74PyURU8lmXnA7MldMYcoBU3wvdHzW5os8r+inkyY14IRyHSoslm/LgWt9YtXtF3XSzgmKRS/uAJXC83SxODgy4KNSizmdqXwZqasRUOQH0nT/yiY5H3n+cMb3aWu68tUuOwQ2GBcGkT2pYzD+qDZ3ADMbz57wR5+9hvI3XG82C2jnsgYXcozcGJA3jbHJCkBL7oFR54vSy3YdQIDAQAB";
        }
    });

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        DatabaseManager.init(getApplicationContext());
        PrefManager.init(getApplicationContext());
        super.onCreate();
    }

    public static App get() {
        return instance;
    }

    public Billing getBilling() {
        return billing;
    }

    public static Context getContext() {
        return context;
    }
}
