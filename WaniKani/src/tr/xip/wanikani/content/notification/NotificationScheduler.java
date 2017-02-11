package tr.xip.wanikani.content.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.StudyQueue;

public class NotificationScheduler {
    private Context context;

    private NotificationPreferences prefs;

    public NotificationScheduler(Context context) {
        this.context = context;
        prefs = new NotificationPreferences(context);
    }

    public void schedule() {
        WaniKaniApi.getStudyQueue().enqueue(new ThroughDbCallback<Request<StudyQueue>, StudyQueue>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onResponse(Call<Request<StudyQueue>> call, Response<Request<StudyQueue>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body().requested_information != null) {
                    load(response.body().requested_information);
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<Request<StudyQueue>> call, Throwable t) {
                super.onFailure(call, t);

                StudyQueue queue = DatabaseManager.getStudyQueue();
                if (queue != null) {
                    load(queue);
                }
            }

            void load(StudyQueue queue) {
                if (queue.getNextReviewDateInMillis() <= System.currentTimeMillis()) {
                    new NotificationPublisher().publish(context);
                    return;
                }

                if (!prefs.isAlarmSet()) {
                    PendingIntent pendingIntent = getPendingIntent();
                    AlarmManager alarmManager = getAlarmManager();
                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            queue.getNextReviewDateInMillis() + NotificationPreferences.NOTIFICATION_CHECK_DELAY,
                            pendingIntent
                    );

                    Log.d("NOTIFICATION SCHEDULER", "SCHEDULED NOTIFICATION FOR " + new SimpleDateFormat("HH:mm:ss").format(queue.getNextReviewDateInMillis() + NotificationPreferences.NOTIFICATION_CHECK_DELAY));

                    prefs.setAlarmSet(true);
                }
            }
        });
    }

    public void cancelNotifications() {
        PendingIntent pendingIntent = getPendingIntent();
        AlarmManager alarmManager = getAlarmManager();
        alarmManager.cancel(pendingIntent);
        prefs.setAlarmSet(false);
    }

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        return PendingIntent.getBroadcast(
                context,
                NotificationPublisher.REQUEST_CODE,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}