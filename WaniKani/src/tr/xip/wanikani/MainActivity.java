package tr.xip.wanikani;

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
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import tr.xip.wanikani.cards.AvailableCard;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.utils.Utils;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String STATE_ACTIONBAR_TITLE = "action_bar_title";

    public static boolean isFirstSyncDashboardDone = false;
    public static boolean isFirstSyncProfileDone = false;

    public static CharSequence mTitle;

    ViewGroup mActionBarLayout;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;

    PrefManager prefMan;

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

        prefMan = new PrefManager(this);

        mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_main, null);

        mActionBarIcon = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_icon);
        mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setCustomView(mActionBarLayout);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setIcon(android.R.color.transparent);

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(STATE_ACTIONBAR_TITLE);
            mActionBarTitle.setText(mTitle);
        }

        if (prefMan.isFirstLaunch()) {
            startActivity(new Intent(this, FirstTimeActivity.class));
            finish();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mActionBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigationDrawerFragment.toggleDrawer();
            }
        });
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        if (mActionBarTitle != null) {
            mActionBarTitle.setText(mTitle);
        }
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
}
