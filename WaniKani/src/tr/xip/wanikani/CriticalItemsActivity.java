package tr.xip.wanikani;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import tr.xip.wanikani.adapters.CriticalItemsGridAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.CriticalItemsList;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by Hikari on 10/2/14.
 */
public class CriticalItemsActivity extends Activity {

    WaniKaniApi api;
    PrefManager prefMan;

    ViewGroup mActionBarLayout;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;

    GridView mGrid;
    ViewFlipper mFlipper;

    List<CriticalItemsList.CriticalItem> mList = new ArrayList<CriticalItemsList.CriticalItem>();

    CriticalItemsGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critical_items);

        api = new WaniKaniApi(this);
        prefMan = new PrefManager(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                    R.layout.actionbar_main, null);

            mActionBarIcon = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_icon);
            mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);

            ActionBar mActionBar = getActionBar();
            mActionBar.setCustomView(mActionBarLayout);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setHomeButtonEnabled(false);
            mActionBar.setIcon(android.R.color.transparent);
            if (Build.VERSION.SDK_INT >= 18)
                mActionBar.setHomeAsUpIndicator(android.R.color.transparent);

            mActionBarIcon.setImageResource(R.drawable.ic_action_back);
            mActionBarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        setActionBarTitle(getString(R.string.card_title_critical_items));

        mGrid = (GridView) findViewById(R.id.activity_critical_items_grid);
        mFlipper = (ViewFlipper) findViewById(R.id.activity_critical_items_view_flipper);

        new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private void setActionBarTitle(String title) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            if (mActionBarTitle != null)
                mActionBarTitle.setText(title);
        } else
            getActionBar().setTitle(title);
    }

    private class LoadTask extends AsyncTask<Void, Void, List<CriticalItemsList.CriticalItem>> {

        @Override
        protected List<CriticalItemsList.CriticalItem> doInBackground(Void... voids) {
            try {
                mList = api.getCriticalItemsList(prefMan.getDashboardCriticalItemsPercentage());
                return mList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CriticalItemsList.CriticalItem> result) {
            if (result != null) {
                mAdapter = new CriticalItemsGridAdapter(
                        CriticalItemsActivity.this,
                        R.layout.item_critical_grid,
                        mList
                );

                mGrid.setAdapter(mAdapter);

                mGrid.setOnItemClickListener(new GridItemClickListener());

                if (mFlipper.getDisplayedChild() == 0) {
                    mFlipper.showNext();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_couldnt_load_data, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    private class GridItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            CriticalItemsList.CriticalItem item = mList.get(position);

            Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_TYPE, item.getType());
            intent.putExtra(ItemDetailsActivity.ARG_CHARACTER, item.getCharacter());
            intent.putExtra(ItemDetailsActivity.ARG_IMAGE, item.getImage());
            intent.putExtra(ItemDetailsActivity.ARG_LEVEL, item.getLevel());
            startActivity(intent);
        }
    }
}
