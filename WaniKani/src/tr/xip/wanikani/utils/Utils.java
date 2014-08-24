package tr.xip.wanikani.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Date;

import tr.xip.wanikani.R;

/**
 * Created by xihsa_000 on 3/15/14.
 */
public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public static String getTimeDifference(Context context, Date date1, Date date2) {

        long differenceInMilliseconds = date1.getTime() - date2.getTime();
        long differenceInMinutes = differenceInMilliseconds / 60000;
        long differenceInHours = differenceInMinutes / 60;
        long differenceInDays = differenceInHours / 24;
        double differenceInMonths = differenceInDays / 30.4368;

        if (differenceInMinutes == 0) {
            return context.getString(R.string.less_than_a_minute)
                    + getAgoIfPastTime(context, differenceInMilliseconds);
        } else if (differenceInMinutes >= 60 || differenceInMinutes <= -60) {
            if (differenceInHours >= 24 || differenceInHours <= -24) {
                if (differenceInDays >= 30 || differenceInDays <= -30) {
                    if ((differenceInDays >= 30 && differenceInDays < 46)
                            || (differenceInDays <= -30 && differenceInDays > -46))
                        return "~ " + context.getString(R.string.a_month)
                                + getAgoIfPastTime(context, differenceInMonths);
                    else
                        return Math.round(removeMinusFromLong(differenceInMonths)) + " "
                                + context.getString(R.string.months)
                                + getAgoIfPastTime(context, differenceInMonths);
                } else {
                    if (differenceInDays == 1 || differenceInDays == -1)
                        return context.getString(R.string.a_day)
                                + getAgoIfPastTime(context, differenceInDays);
                    else
                        return (long) removeMinusFromLong(differenceInDays) + " "
                                + context.getString(R.string.days)
                                + getAgoIfPastTime(context, differenceInDays);
                }
            } else {
                if (differenceInHours == 1 || differenceInHours == -1)
                    return context.getString(R.string.an_hour)
                            + getAgoIfPastTime(context, differenceInDays);
                else
                    return (long) removeMinusFromLong(differenceInHours) + " "
                            + context.getString(R.string.hours)
                            + getAgoIfPastTime(context, differenceInDays);
            }
        } else {
            if (differenceInMinutes == 1 || differenceInMinutes == -1)
                return context.getString(R.string.a_minute)
                        + getAgoIfPastTime(context, differenceInMinutes);
            else
                return (long) removeMinusFromLong(differenceInMinutes) + " "
                        + context.getString(R.string.minutes)
                        + getAgoIfPastTime(context, differenceInMinutes);
        }
    }

    private static String getAgoIfPastTime(Context context, double time) {
        if (time < 0)
            return " " + context.getString(R.string.ago);
        else return "";
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
