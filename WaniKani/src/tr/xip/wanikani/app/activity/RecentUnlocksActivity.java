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

import tr.xip.wanikani.R;
import tr.xip.wanikani.widget.adapter.RecentUnlocksStickyHeaderGridViewArrayAdapter;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.client.task.RecentUnlocksListGetTask;
import tr.xip.wanikani.client.task.callback.RecentUnlocksListGetTaskCallbacks;

/**
 * Created by xihsa_000 on 3/25/14.
 */
public class RecentUnlocksActivity extends ActionBarActivity implements RecentUnlocksListGetTaskCallbacks {

    WaniKaniApi api;

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

        api = new WaniKaniApi(this);
        context = this;

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mActionBar.setTitle(getString(R.string.card_title_recent_unlocks));

        mRecentUnlocksGrid = (StickyGridHeadersGridView) findViewById(R.id.activity_recent_unlocks_grid);

        mViewFlipper = (ViewFlipper) findViewById(R.id.activity_recent_unlocks_view_flipper);
        mViewFlipper.setInAnimation(this, R.anim.abc_fade_in);
        mViewFlipper.setOutAnimation(this, R.anim.abc_fade_out);

        new RecentUnlocksListGetTask(this, 100, this).executeParallel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onRecentUnlocksListGetTaskPreExecute() {
        /* Do nothing */
    }

    @Override
    public void onRecentUnlocksListGetTaskPostExecute(List<UnlockItem> list) {
        if (list != null) {
            mRecentUnlocksAdapter = new RecentUnlocksStickyHeaderGridViewArrayAdapter(context,
                    list, R.layout.header_recent_unlocks, R.layout.item_recent_unlock_grid);
            mRecentUnlocksGrid.setAdapter(mRecentUnlocksAdapter);

            mRecentUnlocksGrid.setOnItemClickListener(new recentUnlocksListItemClickListener());

            if (mViewFlipper.getDisplayedChild() == 0) {
                mViewFlipper.showNext();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_couldnt_load_data, Toast.LENGTH_SHORT).show();
            finish();
        }
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
