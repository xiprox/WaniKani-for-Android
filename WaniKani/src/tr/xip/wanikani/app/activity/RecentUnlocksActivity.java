package tr.xip.wanikani.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.RecentUnlocksList;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.widget.adapter.RecentUnlocksStickyHeaderGridViewArrayAdapter;

/**
 * Created by xihsa_000 on 3/25/14.
 */
public class RecentUnlocksActivity extends ActionBarActivity {
    Context context;

    ActionBar mActionBar;

    StickyGridHeadersGridView mRecentUnlocksGrid;

    ViewFlipper mViewFlipper;

    RecentUnlocksStickyHeaderGridViewArrayAdapter mRecentUnlocksAdapter;

    List<UnlockItem> recentUnlocksList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_unlocks);

        context = this;

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mActionBar.setTitle(getString(R.string.card_title_recent_unlocks));

        mRecentUnlocksGrid = (StickyGridHeadersGridView) findViewById(R.id.activity_recent_unlocks_grid);

        mViewFlipper = (ViewFlipper) findViewById(R.id.activity_recent_unlocks_view_flipper);
        mViewFlipper.setInAnimation(this, R.anim.abc_fade_in);
        mViewFlipper.setOutAnimation(this, R.anim.abc_fade_out);

        WaniKaniApi.getRecentUnlocksList(100).enqueue(new ThroughDbCallback<Request<RecentUnlocksList>, RecentUnlocksList>() {
            @Override
            public void onResponse(Call<Request<RecentUnlocksList>> call, Response<Request<RecentUnlocksList>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body().requested_information != null) {
                    load(response.body().requested_information);
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<Request<RecentUnlocksList>> call, Throwable t) {
                super.onFailure(call, t);

                RecentUnlocksList list = DatabaseManager.getRecentUnlocks(100);
                if (list != null) {
                    load(list);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_couldnt_load_data, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            void load(RecentUnlocksList list) {
                recentUnlocksList = list;

                mRecentUnlocksAdapter = new RecentUnlocksStickyHeaderGridViewArrayAdapter(context,
                        recentUnlocksList, R.layout.header_recent_unlocks, R.layout.item_recent_unlock_grid);
                mRecentUnlocksGrid.setAdapter(mRecentUnlocksAdapter);

                mRecentUnlocksGrid.setOnItemClickListener(new recentUnlocksListItemClickListener());

                if (mViewFlipper.getDisplayedChild() == 0) {
                    mViewFlipper.showNext();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private class recentUnlocksListItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            UnlockItem item = recentUnlocksList.get(position);

            Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, item);
            startActivity(intent);
        }
    }
}
