package tr.xip.wanikani.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;

import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.StudyQueue;

public class NotificationScheduler {
    private Context context;

    private NotificationPreferences prefs;

    public NotificationScheduler(Context context) {
        this.context = context;
        prefs = new NotificationPreferences(context);
    }

    public void schedule() {
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
                    new NotificationPublisher().publish(context);
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

                    Log.d("NOTIFICATION SCHEDULER", "SCHEDULED NOTIFICATION FOR " + new SimpleDateFormat("HH:mm:ss").format(queue.getNextReviewDate() + NotificationPreferences.NOTIFICATION_CHECK_DELAY));

                    prefs.setAlarmSet(true);
                }
            }
        }
    }
}