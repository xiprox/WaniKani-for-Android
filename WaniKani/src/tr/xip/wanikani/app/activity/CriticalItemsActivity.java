package tr.xip.wanikani.app.activity;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.CriticalItemsList;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.widget.adapter.CriticalItemsGridAdapter;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.managers.PrefManager;

/**
 * Created by Hikari on 10/2/14.
 */
public class CriticalItemsActivity extends ActionBarActivity {

    ActionBar mActionBar;

    GridView mGrid;
    ViewFlipper mFlipper;

    List<CriticalItem> mList = new ArrayList<CriticalItem>();

    CriticalItemsGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critical_items);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mActionBar.setTitle(R.string.card_title_critical_items);

        mGrid = (GridView) findViewById(R.id.activity_critical_items_grid);
        mFlipper = (ViewFlipper) findViewById(R.id.activity_critical_items_view_flipper);

        WaniKaniApi.getCriticalItemsList(PrefManager.getDashboardCriticalItemsPercentage()).enqueue(new ThroughDbCallback<Request<CriticalItemsList>, CriticalItemsList>() {
            @Override
            public void onResponse(Call<Request<CriticalItemsList>> call, Response<Request<CriticalItemsList>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().requested_information != null) {
                    loadData(response.body().requested_information);
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<Request<CriticalItemsList>> call, Throwable t) {
                super.onFailure(call, t);

                CriticalItemsList list = DatabaseManager.getCriticalItems(PrefManager.getDashboardCriticalItemsPercentage());

                if (list != null) {
                    loadData(list);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_couldnt_load_data, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            void loadData(CriticalItemsList list) {
                mList = list;

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
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
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
