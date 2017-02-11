package tr.xip.wanikani.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.ItemDetailsActivity;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.dialogs.LegendDialogFragment;
import tr.xip.wanikani.dialogs.LevelPickerDialogFragment;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.ItemsList;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.utils.Utils;
import tr.xip.wanikani.widget.adapter.RadicalsAdapter;

public class RadicalsFragment extends Fragment implements LevelPickerDialogFragment.LevelDialogListener,
        SwipeRefreshLayout.OnRefreshListener {

    Context context;

    TextView mMessageTitle;
    TextView mMessageSummary;
    ImageView mMessageIcon;
    ViewFlipper mMessageFlipper;

    StickyGridHeadersGridView mGrid;

    ViewFlipper mListFlipper;

    LevelPickerDialogFragment mLevelPickerDialog;

    RadicalsAdapter mRadicalsAdapter;

    View rootView;

    String level = "";

    MenuItem mLevelItem;

    private SwipeRefreshLayout mMessageSwipeRefreshLayout;

    private void showLegend() {
        new LegendDialogFragment().show(getActivity().getSupportFragmentManager(), "legend-dialog");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        context = getActivity();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mLevelPickerDialog == null)
            mLevelItem.setVisible(false);
        else
            mLevelItem.setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_radicals, menu);
        mLevelItem = menu.findItem(R.id.action_level);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        rootView = layoutInflater.inflate(R.layout.fragment_radicals, viewGroup, false);

        mMessageSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.radicals_message_swipe_refresh);
        mMessageSwipeRefreshLayout.setOnRefreshListener(this);
        mMessageSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh);

        mGrid = (StickyGridHeadersGridView) rootView.findViewById(R.id.radicals_grid);
        mGrid.setOnItemClickListener(new gridItemClickListener());

        mListFlipper = (ViewFlipper) rootView.findViewById(R.id.radicals_list_flipper);
        mMessageFlipper = (ViewFlipper) rootView.findViewById(R.id.radicals_message_flipper);

        mMessageIcon = (ImageView) rootView.findViewById(R.id.radicals_message_icon);
        mMessageTitle = (TextView) rootView.findViewById(R.id.radicals_message_title);
        mMessageSummary = (TextView) rootView.findViewById(R.id.radicals_message_summary);

        if (!PrefManager.isLegendLearned()) {
            showLegend();
        }

        fetchLevelAndData();

        setHasOptionsMenu(true);

        return rootView;
    }

    public void fetchLevelAndData() {
        User user = DatabaseManager.getUser();

        if (user != null) {
            setLevel(user.level);
            fetchData();
        } else {
            WaniKaniApi.getUser().enqueue(new ThroughDbCallback<Request<User>, User>() {
                @Override
                public void onResponse(Call<Request<User>> call, Response<Request<User>> response) {
                    super.onResponse(call, response);

                    if (response.isSuccessful() && response.body().user_information != null) {
                        setLevel(response.body().user_information.level);
                        fetchData();
                    }
                }
            });
        }
    }

    private void setLevel(int level) {
        this.level = level + "";
        mLevelPickerDialog = new LevelPickerDialogFragment();
    }

    public void fetchData() {
        if (mListFlipper.getDisplayedChild() == 1)
            mListFlipper.showPrevious();

        WaniKaniApi.getRadicalsList(level).enqueue(new ThroughDbCallback<Request<ItemsList>, ItemsList>() {
            @Override
            public void onResponse(Call<Request<ItemsList>> call, Response<Request<ItemsList>> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body().requested_information != null) {
                    load(response.body().requested_information);
                } else {
                    onFailure(call, null);
                }

                ((ActionBarActivity) context).invalidateOptionsMenu();

                if (mListFlipper.getDisplayedChild() == 0)
                    mListFlipper.showNext();

                mMessageSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Request<ItemsList>> call, Throwable t) {
                ItemsList list = DatabaseManager.getItems(BaseItem.ItemType.RADICAL, Utils.convertStringArrayToIntArray(level.split(",")));
                if (list != null) {
                    load(list);
                } else {
                    mMessageIcon.setImageResource(R.drawable.ic_error_red_36dp);
                    mMessageTitle.setText(R.string.no_items_title);
                    mMessageSummary.setText(R.string.no_items_summary);

                    mGrid.setAdapter(new ArrayAdapter(context, R.layout.item_radical));

                    if (mMessageFlipper.getDisplayedChild() == 0) {
                        mMessageFlipper.showNext();
                    }
                }
            }

            void load(ItemsList list) {
                Collections.sort(list, new Comparator<BaseItem>() {
                    public int compare(BaseItem item1, BaseItem item2) {
                        return Float.valueOf((item1.getLevel() + "")).compareTo(Float.valueOf(item2.getLevel() + ""));
                    }
                });

                mRadicalsAdapter = new RadicalsAdapter(context, list, R.layout.header_level, R.layout.item_radical);
                mGrid.setAdapter(mRadicalsAdapter);

                if (mMessageFlipper.getDisplayedChild() == 1)
                    mMessageFlipper.showPrevious();
            }
        });
    }

    @Override
    public void onLevelDialogPositiveClick(DialogFragment dialog, String level) {
        this.level = level;
        fetchData();
    }

    @Override
    public void onLevelDialogResetClick(DialogFragment dialogFragment, String level) {
        fetchLevelAndData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_level:
                showLevelDialog();
                break;
            case R.id.action_legend:
                showLegend();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLevelDialog() {
        if (mLevelPickerDialog != null) {
            mLevelPickerDialog.init(this.getId(), level);
            mLevelPickerDialog.show(getActivity().getSupportFragmentManager(), "LevelPickerDialogFragment");
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    private class gridItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            BaseItem radicalItem = mRadicalsAdapter.getItem(position);

            Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
            intent.putExtra(ItemDetailsActivity.ARG_ITEM, radicalItem);
            getActivity().startActivity(intent);
        }
    }
}