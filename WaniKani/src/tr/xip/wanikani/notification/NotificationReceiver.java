package tr.xip.wanikani.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.StudyQueue;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by Hikari on 12/16/14.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private Context context;

    private NotificationPreferences prefs;
    private PrefManager prefManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        prefs = new NotificationPreferences(context);
        prefManager = new PrefManager(context);

        if (prefManager.notificationsEnabled())
            new LoadTask().execute();
    }

    private class LoadTask extends AsyncTask<Void, Void, Boolean> {
        StudyQueue queue;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                queue = new WaniKaniApi(context).getStudyQueue();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success) {
                if (queue.getNextReviewDate() <= System.currentTimeMillis()) {
                    context.sendBroadcast(new Intent(NotificationPreferences.BROADCAST_SHOWN_NOTIFICATIONS));
                    return;
                }

                if (!prefs.isAlarmSet()) {
                    Intent notificationIntent = new Intent(context, NotificationPublisher.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context,
                            NotificationPublisher.REQUEST_CODE,
                            notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            queue.getNextReviewDate() + NotificationPreferences.NOTIFICATION_CHECK_DELAY,
                            pendingIntent
                    );

                    prefs.setAlarmSet(true);
                }
            }
        }
    }
}
