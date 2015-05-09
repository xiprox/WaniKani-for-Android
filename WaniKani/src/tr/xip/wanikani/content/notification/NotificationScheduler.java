package tr.xip.wanikani.content.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;

import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.client.task.StudyQueueGetTask;
import tr.xip.wanikani.client.task.callback.StudyQueueGetTaskCallbacks;

public class NotificationScheduler implements StudyQueueGetTaskCallbacks {
    private Context context;

    private NotificationPreferences prefs;

    public NotificationScheduler(Context context) {
        this.context = context;
        prefs = new NotificationPreferences(context);
    }

    public void schedule() {
        new StudyQueueGetTask(context, this).executeParallel();
    }

    @Override
    public void onStudyQueueGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onStudyQueueGetTaskPostExecute(StudyQueue queue) {
        if (queue != null) {
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