package tr.xip.wanikani;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import tr.xip.wanikani.cards.AvailableCard;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.settings.SettingsActivity;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static boolean isFirstSyncDashboardDone = false;
    public static boolean isFirstSyncProfileDone = false;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    public String TAG = "WANIKANI";

    PrefManager prefMan;
    ThemeManager themeMan;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefMan = new PrefManager(this);
        themeMan = new ThemeManager(this);

        setTheme(themeMan.getTheme());

        super.onCreate(savedInstanceState);

        if(prefMan.isFirstLaunch()) {
            startActivity(new Intent(this, FirstTimeActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        getSupportActionBar().setIcon(R.drawable.ic_wanikani_stamp);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        switch (position) {
            case 0: {
                fragment = new DashboardFragment();
                getSupportActionBar().setTitle(R.string.title_dashboard);
                break;
            }
            case 1: {
                fragment = new ProfileFragment();
                getSupportActionBar().setTitle(R.string.title_profile);
                break;
            }
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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
            case R.id.action_logout:
                prefMan.logout();
                startActivity(new Intent(this, FirstTimeActivity.class));
                finish();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
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
}
