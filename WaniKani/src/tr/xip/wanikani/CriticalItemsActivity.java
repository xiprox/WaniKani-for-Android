package tr.xip.wanikani;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import tr.xip.wanikani.adapters.CriticalItemsGridAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.BaseItem;
import tr.xip.wanikani.api.response.CriticalItem;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by Hikari on 10/2/14.
 */
public class CriticalItemsActivity extends ActionBarActivity {

    WaniKaniApi api;
    PrefManager prefMan;

    ActionBar mActionBar;

    GridView mGrid;
    ViewFlipper mFlipper;

    List<CriticalItem> mList = new ArrayList<CriticalItem>();

    CriticalItemsGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critical_items);

        api = new WaniKaniApi(this);
        prefMan = new PrefManager(this);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mActionBar.setTitle(R.string.card_title_critical_items);

        mGrid = (GridView) findViewById(R.id.activity_critical_items_grid);
        mFlipper = (ViewFlipper) findViewById(R.id.activity_critical_items_view_flipper);

        new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private class LoadTask extends AsyncTask<Void, Void, List<CriticalItem>> {

        @Override
        protected List<CriticalItem> doInBackground(Void... voids) {
            try {
                mList = api.getCriticalItemsList(prefMan.getDashboardCriticalItemsPercentage());
                return mList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CriticalItem> result) {
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
            CriticalItem item = mList.get(position);

            Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, item);
            startActivity(intent);
        }
    }
}
