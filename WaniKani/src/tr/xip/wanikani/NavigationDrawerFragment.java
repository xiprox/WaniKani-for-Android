package tr.xip.wanikani;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import tr.xip.wanikani.adapters.NavigationItemsAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.items.NavigationItems;

public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    View rootView;

    WaniKaniApi api;

   /* ImageView mAvatar;
    ImageView mFieldBackground;
    TextView mUsername;
    TextView mLessonCount;
    TextView mReviewCount;
    ViewFlipper mLessonsFlipper;
    ViewFlipper mReviewsFlipper;*/

    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;

    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    NavigationItemsAdapter mNavigationItemsAdapter;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = new WaniKaniApi(getActivity());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        selectItem(mCurrentSelectedPosition);
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

        mDrawerListView = (ListView) rootView.findViewById(R.id.navigation_drawer_list_view);
        /*mAvatar = (ImageView) rootView.findViewById(R.id.navigation_drawer_avatar);
        mFieldBackground = (ImageView) rootView.findViewById(R.id.navigation_drawer_field_bg);
        mUsername = (TextView) rootView.findViewById(R.id.navigation_drawer_username);
        mLessonCount = (TextView) rootView.findViewById(R.id.navigation_drawer_lesson_count);
        mReviewCount = (TextView) rootView.findViewById(R.id.navigation_drawer_review_count);

        mLessonsFlipper = (ViewFlipper) rootView.findViewById(R.id.navigation_drawer_lessons_flipper);
        mReviewsFlipper = (ViewFlipper) rootView.findViewById(R.id.navigation_drawer_reviews_flipper);

        mLessonsFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mLessonsFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);

        mReviewsFlipper.setInAnimation(getActivity(), R.anim.abc_fade_in);
        mReviewsFlipper.setOutAnimation(getActivity(), R.anim.abc_fade_out);
*/
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        NavigationItems mNavigationContent = new NavigationItems();
        mNavigationItemsAdapter = new NavigationItemsAdapter(getActivity(),
                R.layout.item_recent_unlock, mNavigationContent.ITEMS);

        mDrawerListView.setAdapter(mNavigationItemsAdapter);

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

//        new LoadTask().execute();

        /*Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.art_00_500);
        Bitmap blurredBmp = Blur.fastblur(getActivity(), image, 20);
        mFieldBackground.setImageBitmap(blurredBmp);*/

        return rootView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu();
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

                getActivity().supportInvalidateOptionsMenu();
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
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
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

        if (item.getItemId() == R.id.action_profile) {
            Toast.makeText(getActivity(), "Should have gone to profile", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }

   /* public class LoadTask extends AsyncTask<Void, Void, Void> {
        String gravatar;
        String username;
        int lessons;
        int reviews;

        @Override
        protected Void doInBackground(Void... voids) {
            gravatar = api.getGravatar();
            username = api.getUsername();
            lessons = api.getLessonsAvailable();
            reviews = api.getReviewsAvailable();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Picasso.with(getActivity()).load("http://www.gravatar.com/avatar/" + gravatar + "?s=100").fit().into(mAvatar);
            mUsername.setText(username);

            if(lessons != 0) {
                mLessonCount.setText(lessons + "");
                mLessonsFlipper.showNext();
            }

            if(reviews != 0) {
                mReviewCount.setText(reviews + "");
                mReviewsFlipper.showNext();
            }
        }
    }*/
}
