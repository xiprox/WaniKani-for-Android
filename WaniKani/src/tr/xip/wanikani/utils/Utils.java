package tr.xip.wanikani.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Date;

/**
 * Created by xihsa_000 on 3/15/14.
 */
public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    private static double removeMinusFromLong(double time) {
        if (time < 0) {
            String timeString = time + "";
            timeString = timeString.substring(1, timeString.length());
            return Double.parseDouble(timeString);
        } else return time;
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public float pxFromDp(float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
