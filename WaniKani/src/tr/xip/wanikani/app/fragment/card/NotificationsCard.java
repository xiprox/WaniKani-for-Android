package tr.xip.wanikani.app.fragment.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.NotificationDetailsActivity;
import tr.xip.wanikani.models.Notification;

public class NotificationsCard extends Fragment {

    public static final String ARG_NOTIFICATIONS = "notifications";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_notifications, container, false);

        Bundle bundle = getArguments();
        //noinspection unchecked
        List<Notification> notifications = (List<Notification>) bundle.getSerializable(ARG_NOTIFICATIONS);

        if (notifications != null && notifications.size() != 0) {
            LinearLayout holder = (LinearLayout) rootView.findViewById(R.id.notifications_holder);

            holder.removeAllViews();
            for (final Notification n : notifications) {
                ViewGroup item = (ViewGroup) inflater.inflate(R.layout.item_notification, holder, false);

                TextView title = (TextView) item.findViewById(R.id.notification_title);
                TextView shortText = (TextView) item.findViewById(R.id.notification_short_text);

                title.setText(n.getTitle());
                shortText.setText(n.getShortText());

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), NotificationDetailsActivity.class);
                        intent.putExtra(NotificationDetailsActivity.ARG_NOTIFICATION, n);
                        startActivity(intent);
                    }
                });

                holder.addView(item);
            }
        }

        return rootView;
    }
}
