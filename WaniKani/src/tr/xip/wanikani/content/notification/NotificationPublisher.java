package tr.xip.wanikani.content.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import tr.xip.wanikani.app.activity.Browser;
import tr.xip.wanikani.app.activity.MainActivity;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.SWWebReviewActivity;
import tr.xip.wanikani.app.activity.WebReviewActivity;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.client.task.StudyQueueGetTask;
import tr.xip.wanikani.client.task.callback.StudyQueueGetTaskCallbacks;

/**
 * Created by Hikari on 12/16/14.
 */
public class NotificationPublisher extends BroadcastReceiver implements StudyQueueGetTaskCallbacks {
    public static final int REQUEST_CODE = 0;

    private static final int NOTIFICATION_ID = 0;

    private static final int BROWSER_TYPE_LESSONS = 0;
    private static final int BROWSER_TYPE_REVIEWS = 1;

    private Context context;

    private NotificationPreferences notifPrefs;

    private PrefManager prefMan;

    @Override
    public void onReceive(Context context, Intent intent) {
        publish(context);
    }

    public void publish(Context context) {
        this.context = context;
        this.prefMan = new PrefManager(context);
        this.notifPrefs = new NotificationPreferences(context);

        new StudyQueueGetTask(context, this).executeParallel();
    }

    private String getString(int res) {
        return context.getString(res);
    }

    @Override
    public void onStudyQueueGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onStudyQueueGetTaskPostExecute(StudyQueue queue) {
        if (queue != null) {
            int lessonsCount = queue.getAvailableLesonsCount();
            int reviewsCount = queue.getAvailableReviewsCount();

            if (lessonsCount != 0 || reviewsCount != 0) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.ic_school_white_36dp)
                        .setColor(context.getResources().getColor(R.color.apptheme_main))
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(true);

                /** We have both lessons and reviews */
                if (lessonsCount != 0 && reviewsCount != 0) {
                    mBuilder.setContentTitle(getString(R.string.notif_title_new_lessons_and_reviews));

                    NotificationCompat.Action mLessonsAction = new NotificationCompat.Action(
                            R.drawable.ic_arrow_forward_white_36dp,
                            getString(R.string.notif_action_lessons),
                            getBrowserPendingIntent(BROWSER_TYPE_LESSONS)
                    );

                    NotificationCompat.Action mReviewsAction = new NotificationCompat.Action(
                            R.drawable.ic_arrow_forward_white_36dp,
                            getString(R.string.notif_action_reviews),
                            getBrowserPendingIntent(BROWSER_TYPE_REVIEWS)
                    );

                    mBuilder.setContentIntent(getDashboardPendingIntent())
                            .addAction(mLessonsAction)
                            .addAction(mReviewsAction);
                }

                /** We only have lessons */
                if (lessonsCount != 0 && reviewsCount == 0) {
                    mBuilder.setContentTitle(getString(R.string.notif_title_new_lessons));
                    mBuilder.setContentIntent(getBrowserPendingIntent(BROWSER_TYPE_LESSONS));
                }

                /** We only have reviews */
                if (reviewsCount != 0 && lessonsCount == 0) {
                    mBuilder.setContentTitle(getString(R.string.notif_title_new_reviews));
                    mBuilder.setContentIntent(getBrowserPendingIntent(BROWSER_TYPE_REVIEWS));
                }

                mBuilder.setContentText(getContentText(lessonsCount, reviewsCount));

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                notifPrefs.saveLastNotificationShown(System.currentTimeMillis());
            }
        }

        notifPrefs.setAlarmSet(false);
        new NotificationReceiver().handleSituation(context);

        Log.d("NOTIFICATION PUBLISHER", "PUBLISHED A NOTIFICATION");
    }

    private PendingIntent getBrowserPendingIntent(int type) {
        Intent browserIntent = prefMan.getWebViewIntent();
        browserIntent.setAction(WebReviewActivity.OPEN_ACTION);

        switch (type) {
            case BROWSER_TYPE_LESSONS:
                browserIntent.setData(Uri.parse(Browser.LESSON_URL));
                break;
            case BROWSER_TYPE_REVIEWS:
                browserIntent.setData(Uri.parse(Browser.REVIEW_URL));
                break;
        }

        TaskStackBuilder browserStackBuilder = TaskStackBuilder.create(context);
        browserStackBuilder.addParentStack(
                prefMan.getHWAccel() ? WebReviewActivity.class : SWWebReviewActivity.class);
        browserStackBuilder.addNextIntent(browserIntent);

        return browserStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getDashboardPendingIntent() {
        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getContentText(int lessonsCount, int reviewsCount) {
        String lesson = getString(R.string.notif_content_lesson);
        String lessons = getString(R.string.notif_content_lessons);
        String review = getString(R.string.notif_content_review);
        String reviews = getString(R.string.notif_content_reviews);

        /** We have both lessons and reviews */
        if (lessonsCount != 0 && reviewsCount != 0) {
            return getString(R.string.notif_content_x_lessons_and_x_reviews_available)
                    .replace("{lessons_count}", lessonsCount + "")
                    .replace("{reviews_count}", reviewsCount + "")
                    .replace("{lessons}", lessonsCount == 1 ? lesson : lessons)
                    .replace("{reviews}", reviewsCount == 1 ? review : reviews);
        }

        /** We only have lessons */
        if (lessonsCount != 0 && reviewsCount == 0) {
            return getString(R.string.notif_content_x_lessons_available)
                    .replace("{lessons_count}", lessonsCount + "")
                    .replace("{lessons}", lessonsCount == 1 ? lesson : lessons);
        }

        /** We only have reviews */
        if (reviewsCount != 0 && lessonsCount == 0) {
            return getString(R.string.notif_content_x_reviews_available)
                    .replace("{reviews_count}", reviewsCount + "")
                    .replace("{reviews}", reviewsCount == 1 ? review : reviews);
        }

        return null;
    }
}