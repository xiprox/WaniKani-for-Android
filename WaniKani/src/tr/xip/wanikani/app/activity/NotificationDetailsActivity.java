package tr.xip.wanikani.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tr.xip.wanikani.R;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.Notification;

public class NotificationDetailsActivity extends AppCompatActivity {
    public static final String ARG_NOTIFICATION = "notification";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getSerializable(ARG_NOTIFICATION) != null) {
            load((Notification) bundle.getSerializable(ARG_NOTIFICATION));
        } else {
            finish();
        }
    }

    private void load(final Notification item) {
        ImageView image = (ImageView) findViewById(R.id.notification_image);
        TextView title = (TextView) findViewById(R.id.notification_title);
        TextView shortText = (TextView) findViewById(R.id.notification_short_text);
        TextView text = (TextView) findViewById(R.id.notification_text);
        Button action = (Button) findViewById(R.id.notification_action);

        text.setMovementMethod(new LinkMovementMethod());

        if (item.getImage() != null) {
            Picasso.with(this).load(item.getImage()).into(image);
        } else {
            image.setVisibility(View.GONE);
        }

        if (item.getTitle() != null) {
            title.setText(item.getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        if (item.getShortText() != null) {
            shortText.setText(item.getShortText());
        } else {
            shortText.setVisibility(View.GONE);
        }

        if (item.getText() != null) {
            text.setText(Html.fromHtml(item.getText()));
        } else {
            text.setVisibility(View.GONE);
        }

        if (item.getActionUrl() != null && item.getActionText() != null) {
            action.setText(item.getActionText());
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getActionUrl()));
                    startActivity(intent);
                }
            });
        } else {
            action.setVisibility(View.GONE);
        }

        /* Mark notification as read */
        item.setRead(true);
        new DatabaseManager(this).saveNotification(item);
    }
}
