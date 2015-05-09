package tr.xip.wanikani.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.adapter.NavigationItemsAdapter;
import tr.xip.wanikani.widget.adapter.NavigationSecondaryItemsAdapter;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.app.activity.MainActivity;
import tr.xip.wanikani.dialogs.LogoutDialogFragment;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.preference.SettingsActivity;
import tr.xip.wanikani.client.task.UserInfoGetTask;
import tr.xip.wanikani.client.task.callback.UserInfoGetTaskCallbacks;
import tr.xip.wanikani.graphics.bitmap.transform.BlurTransformation;
import tr.xip.wanikani.graphics.bitmap.transform.CircleTransformation;

public class NavigationDrawerFragment extends Fragment implements UserInfoGetTaskCallbacks {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    View rootView;
    Context context;

    WaniKaniApi api;
    PrefManager prefMan;

    ImageView mAvatar;
    ImageView mAvatarBg;
    TextView mUsername;

    FrameLayout mProfile;

    NavigationItemsAdapter mNavigationItemsAdapter;

    private UserInfoGetTask mUserInfoFetchTask;

    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mMainListView;
    private ListView mSecondaryListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = new WaniKaniApi(getActivity());
        prefMan = new PrefManager(getActivity());

        context = getActivity();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_navigation_drawer, null);

        mAvatar = (ImageView) rootView.findViewById(R.id.navigation_drawer_avatar);
        mAvatarBg = (ImageView) rootView.findViewById(R.id.navigation_drawer_avatar_bg);
        mUsername = (TextView) rootView.findViewById(R.id.navigation_drawer_username);

        mMainListView = (ListView) rootView.findViewById(R.id.navigation_drawer_list_main);
        mSecondaryListView = (ListView) rootView.findViewById(R.id.navigation_drawer_list_secondary);

        mNavigationItemsAdapter = new NavigationItemsAdapter(getActivity());
        mMainListView.setAdapter(mNavigationItemsAdapter);
        mMainListView.getLayoutParams().height = mNavigationItemsAdapter.getListViewHeight();
        mMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mMainListView.setItemChecked(mCurrentSelectedPosition, true);

        NavigationSecondaryItemsAdapter mNavSecondaryItemsAdapter =
                new NavigationSecondaryItemsAdapter(getActivity());
        mSecondaryListView.setAdapter(mNavSecondaryItemsAdapter);
        mSecondaryListView.getLayoutParams().height = mNavSecondaryItemsAdapter.getListViewHeight();
        mSecondaryListView.setOnItemClickListener(new SecondaryNavigationItemClickListener());

        mProfile = (FrameLayout) rootView.findViewById(R.id.navigation_drawer_profile);

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ProfileFragment())
                        .commit();
                // We dedicate 100 for profile
                selectItem(100);
            }
        });

        fetchUserInfo();

        selectItem(mCurrentSelectedPosition);

        return rootView;
    }

    public void fetchUserInfo() {
        if (mUserInfoFetchTask != null)
            mUserInfoFetchTask.cancel(true);

        mUserInfoFetchTask = new UserInfoGetTask(context, this);
        mUserInfoFetchTask.executeParallel();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void toggleDrawer() {
        if (mDrawerLayout != null) {
            if (isDrawerOpen()) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            } else {
                mDrawerLayout.openDrawer(mFragmentContainerView);
            }
        }
    }

    public void setUp(int drawerHolderId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(drawerHolderId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerLayout.setStatusBarBackground(R.color.apptheme_main_dark);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                ((MainActivity) getActivity()).getToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;

        if (mMainListView != null)
            mMainListView.setItemChecked(position, true);
        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        if (mCallbacks != null)
            mCallbacks.onNavigationDrawerItemSelected(position);

        if (position == 100) {
            // Profile was selected
            MainActivity.mTitle = getString(R.string.title_profile);

            if (mMainListView != null && mMainListView.getAdapter() != null)
                ((NavigationItemsAdapter) mMainListView.getAdapter()).selectItem(100);
        } else if (mMainListView != null && mMainListView.getAdapter() != null)
            ((NavigationItemsAdapter) mMainListView.getAdapter()).selectItem(position);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        if (activity != null)
            activity.getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    private void showlogoutDialog() {
        new LogoutDialogFragment().show(getActivity().getSupportFragmentManager(), "logout-dialog");
    }

    @Override
    public void onUserInfoGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onUserInfoGetTaskPostExecute(User user) {
        if (user != null && user.getUserInformation() != null) {
            Picasso.with(context)
                    .load("http://www.gravatar.com/avatar/" + user.getGravatar() + "?s=100")
                    .fit()
                    .transform(new CircleTransformation())
                    .into(mAvatar);

            Picasso.with(context)
                    .load("http://www.gravatar.com/avatar/" + user.getGravatar())
                    .transform(new BlurTransformation(context))
                    .into(mAvatarBg);

            mUsername.setText(user.getUsername());
        }
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }

    private class SecondaryNavigationItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            switch (position) {
                case 0:
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    break;
                case 1:
                    showlogoutDialog();
                    break;
            }

            if (mDrawerLayout != null) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            }
        }
    }
}
