package tr.xip.wanikani.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import tr.xip.wanikani.R;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.app.fragment.KanjiFragment;
import tr.xip.wanikani.app.fragment.NavigationDrawerFragment;
import tr.xip.wanikani.app.fragment.RadicalsFragment;
import tr.xip.wanikani.app.fragment.VocabularyFragment;
import tr.xip.wanikani.app.fragment.card.AvailableCard;
import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.Notification;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String STATE_ACTIONBAR_TITLE = "action_bar_title";

    public static boolean isFirstSyncDashboardDone = false;
    public static boolean isFirstSyncProfileDone = false;

    public static CharSequence mTitle;

    ActionBar mActionBar;
    Toolbar mToolbar;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleNotification(getIntent());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(STATE_ACTIONBAR_TITLE);
            mActionBar.setTitle(mTitle.toString());
        }

        if (PrefManager.isFirstLaunch()) {
            startActivity(new Intent(this, FirstTimeActivity.class));
            finish();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer_holder,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                mTitle = getString(R.string.title_dashboard);
                break;
            case 1:
                fragment = new RadicalsFragment();
                mTitle = getString(R.string.title_radicals);
                break;
            case 2:
                fragment = new KanjiFragment();
                mTitle = getString(R.string.title_kanji);
                break;
            case 3:
                fragment = new VocabularyFragment();
                mTitle = getString(R.string.title_vocabulary);
                break;
        }

        if (fragment != null)
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
    }

    public void restoreActionBar() {
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        mActionBar.setTitle(mTitle.toString());
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AvailableCard.BROWSER_REQUEST) {
            Intent intent = new Intent(BroadcastIntents.SYNC());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_ACTIONBAR_TITLE, mTitle.toString());
    }

    private void handleNotification(Intent intent) {
        if (intent == null || intent.getExtras() == null) return;

        Bundle bundle = getIntent().getExtras();

        String idString = bundle.getString(Notification.DATA_NOTIFICATION_ID);

        if (idString == null) return; // Return if no id - basically not a notification Intent

        int id = Integer.valueOf(idString);
        String title = bundle.getString(Notification.DATA_NOTIFICATION_TITLE);
        String shortText = bundle.getString(Notification.DATA_NOTIFICATION_SHORT_TEXT);
        String text = bundle.getString(Notification.DATA_NOTIFICATION_TEXT);
        String image = bundle.getString(Notification.DATA_NOTIFICATION_IMAGE);
        String actionUrl = bundle.getString(Notification.DATA_NOTIFICATION_ACTION_URL);
        String actionText = bundle.getString(Notification.DATA_NOTIFICATION_ACTION_TEXT);

        new DatabaseManager(this).saveNotification(new Notification(
                id,
                title,
                shortText,
                text,
                image,
                actionUrl,
                actionText,
                false
        ));

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BroadcastIntents.NOTIFICATION()));
    }
}
