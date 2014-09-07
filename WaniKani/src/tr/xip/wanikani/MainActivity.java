package tr.xip.wanikani;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tr.xip.wanikani.cards.AvailableCard;
import tr.xip.wanikani.managers.PrefManager;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String STATE_ACTIONBAR_TITLE = "action_bar_title";

    public static boolean isFirstSyncDashboardDone = false;
    public static boolean isFirstSyncProfileDone = false;

    public static CharSequence mTitle;

    ViewGroup mActionBarLayout;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;

    PrefManager prefMan;

    ActionBar mActionBar;

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

        mActionBar = getActionBar();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                    R.layout.actionbar_main, null);

            mActionBarIcon = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_icon);
            mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);

            mActionBar.setCustomView(mActionBarLayout);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setIcon(android.R.color.transparent);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setHomeButtonEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(false);

            if (Build.VERSION.SDK_INT >= 18)
                mActionBar.setHomeAsUpIndicator(android.R.color.transparent);

            mActionBarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNavigationDrawerFragment.toggleDrawer();
                }
            });
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(STATE_ACTIONBAR_TITLE);
            setActionBarTitle(mTitle.toString());
        }

        if (prefMan.isFirstLaunch()) {
            startActivity(new Intent(this, FirstTimeActivity.class));
            finish();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
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
        setActionBarTitle(mTitle.toString());
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

    public void setActionBarTitle(String title) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            if (mActionBarTitle != null)
                mActionBarTitle.setText(title);
        } else
            mActionBar.setTitle(title);
    }

    private void setActionBarIcon(int res) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            if (mActionBarIcon != null)
                mActionBarIcon.setImageResource(res);
        } else
            mActionBar.setIcon(res);
    }
}
