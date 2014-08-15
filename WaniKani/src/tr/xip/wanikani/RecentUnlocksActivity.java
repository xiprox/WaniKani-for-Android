package tr.xip.wanikani;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.List;

import tr.xip.wanikani.adapters.RecentUnlocksStickyHeaderGridViewArrayAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.RecentUnlocksList;

/**
 * Created by xihsa_000 on 3/25/14.
 */
public class RecentUnlocksActivity extends ActionBarActivity {

    WaniKaniApi api;

    Context context;

    ViewGroup mActionBarLayout;
    ImageView mActionBarIcon;
    TextView mActionBarTitle;

    StickyGridHeadersGridView mRecentUnlocksGrid;

    ViewFlipper mViewFlipper;

    RecentUnlocksStickyHeaderGridViewArrayAdapter mRecentUnlocksAdapter;

    List<RecentUnlocksList.UnlockItem> recentUnlocksList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_unlocks);

        api = new WaniKaniApi(this);
        context = this;

        mActionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar_main, null);

        mActionBarIcon = (ImageView) mActionBarLayout.findViewById(R.id.actionbar_icon);
        mActionBarTitle = (TextView) mActionBarLayout.findViewById(R.id.actionbar_title);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setCustomView(mActionBarLayout);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setIcon(android.R.color.transparent);
        mActionBar.setHomeAsUpIndicator(android.R.color.transparent);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setHomeButtonEnabled(false);

        mActionBarTitle.setText(R.string.card_title_recent_unlocks);

        mActionBarIcon.setImageResource(R.drawable.ic_action_back);
        mActionBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecentUnlocksGrid = (StickyGridHeadersGridView) findViewById(R.id.activity_recent_unlocks_grid);

        mViewFlipper = (ViewFlipper) findViewById(R.id.activity_recent_unlocks_view_flipper);
        mViewFlipper.setInAnimation(this, R.anim.abc_fade_in);
        mViewFlipper.setOutAnimation(this, R.anim.abc_fade_out);

        new LoadTask().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private class LoadTask extends AsyncTask<Void, Void, List<RecentUnlocksList.UnlockItem>> {

        @Override
        protected List<RecentUnlocksList.UnlockItem> doInBackground(Void... voids) {
            try {
                recentUnlocksList = api.getRecentUnlocksList(100); // Get the maximum amount of unlocks which is 100
                return recentUnlocksList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RecentUnlocksList.UnlockItem> result) {
            if (result != null) {
                mRecentUnlocksAdapter = new RecentUnlocksStickyHeaderGridViewArrayAdapter(context,
                        result, R.layout.header_recent_unlocks, R.layout.item_recent_unlock_grid);
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
    }

    private class recentUnlocksListItemClickListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            RecentUnlocksList.UnlockItem item = recentUnlocksList.get(position);

            Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_TYPE, item.getType());
            intent.putExtra(ItemDetailsActivity.ARG_CHARACTER, item.getCharacter());
            intent.putExtra(ItemDetailsActivity.ARG_IMAGE, item.getImage());
            intent.putExtra(ItemDetailsActivity.ARG_LEVEL, item.getLevel());
            startActivity(intent);
        }
    }
}
