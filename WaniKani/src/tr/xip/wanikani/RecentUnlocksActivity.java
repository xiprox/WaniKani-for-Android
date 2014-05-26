package tr.xip.wanikani;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.List;

import tr.xip.wanikani.adapters.RecentUnlocksAdapter;
import tr.xip.wanikani.api.WaniKaniApi;
import tr.xip.wanikani.api.response.RecentUnlocksList;
import tr.xip.wanikani.managers.ThemeManager;
import tr.xip.wanikani.utils.Fonts;

/**
 * Created by xihsa_000 on 3/25/14.
 */
public class RecentUnlocksActivity extends ActionBarActivity {

    WaniKaniApi api;
    ThemeManager themeMan;

    Context context;

    ListView mRecentUnlocksList;

    ViewFlipper mViewFlipper;

    LinearLayout mCard;

    RecentUnlocksAdapter mRecentUnlocksAdapter;

    List<RecentUnlocksList.UnlockItem> recentUnlocksList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = new WaniKaniApi(this);
        themeMan = new ThemeManager(this);
        context = this;

        setTheme(themeMan.getTheme());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_unlocks);

        mCard = (LinearLayout) findViewById(R.id.activity_recent_unlocks_card);
        mCard.setBackgroundResource(themeMan.getCard());

        mRecentUnlocksList = (ListView) findViewById(R.id.activity_recent_unlocks_list);

        mViewFlipper = (ViewFlipper) findViewById(R.id.activity_recent_unlocks_view_flipper);
        mViewFlipper.setInAnimation(this, R.anim.abc_fade_in);
        mViewFlipper.setOutAnimation(this, R.anim.abc_fade_out);

        new LoadTask().execute();
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
                mRecentUnlocksAdapter = new RecentUnlocksAdapter(context,
                        R.layout.item_recent_unlock, result, new Fonts().getKanjiFont(context));
                mRecentUnlocksList.setAdapter(mRecentUnlocksAdapter);

                mRecentUnlocksList.setOnItemClickListener(new recentUnlocksListItemClickListener());

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
