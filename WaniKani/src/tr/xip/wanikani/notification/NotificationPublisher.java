package tr.xip.wanikani.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import tr.xip.wanikani.Browser;
import tr.xip.wanikani.MainActivity;
import tr.xip.wanikani.R;
import tr.xip.wanikani.SWWebReviewActivity;
import tr.xip.wanikani.WebReviewActivity;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.StudyQueue;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by Hikari on 12/16/14.
 */
public class NotificationPublisher extends BroadcastReceiver {
    public static final int REQUEST_CODE = 0;

    private static final int NOTIFICATION_ID = 0;

    private static final int BROWSER_TYPE_LESSONS = 0;
    private static final int BROWSER_TYPE_REVIEWS = 1;

    private Context context;

    private NotificationPreferences notifPrefs;

    private PrefManager prefMan;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.prefMan = new PrefManager(context);
        this.notifPrefs = new NotificationPreferences(context);

        new FetchTask().execute();
    }

    private String getString(int res) {
        return context.getString(res);
    }

    private class FetchTask extends AsyncTask<Void, Void, Boolean> {
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
                        String contentText = getString(R.string.notif_content_x_lessons_and_x_reviews_available)
                                .replace("{lessons_count}", lessonsCount + "")
                                .replace("{reviews_count}", reviewsCount + "");
                        mBuilder.setContentText(contentText);

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

                        notifPrefs.saveLastLessonsShown(System.currentTimeMillis());
                    }

                    /** We only have lessons */
                    if (lessonsCount != 0 && reviewsCount == 0) {
                        mBuilder.setContentTitle(getString(R.string.notif_title_new_lessons));
                        String contentText = getString(R.string.notif_content_x_lessons_available)
                                .replace("{lessons_count}", lessonsCount + "");
                        mBuilder.setContentText(contentText);
                        mBuilder.setContentIntent(getBrowserPendingIntent(BROWSER_TYPE_LESSONS));

                        notifPrefs.saveLastLessonsShown(System.currentTimeMillis());
                    }

                    /** We only have reviews */
                    if (reviewsCount != 0 && lessonsCount == 0) {
                        mBuilder.setContentTitle(getString(R.string.notif_title_new_reviews));
                        String contentText = getString(R.string.notif_content_x_reviews_available)
                                .replace("{reviews_count}", reviewsCount + "");
                        mBuilder.setContentText(contentText);
                        mBuilder.setContentIntent(getBrowserPendingIntent(BROWSER_TYPE_REVIEWS));
                    }

                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                }
            }

            notifPrefs.setAlarmSet(false);
            Intent broadcastIntent = new Intent(NotificationPreferences.BROADCAST_SCHEDULE_NOTIF_ALARM);
            context.sendBroadcast(broadcastIntent);
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
    }
}