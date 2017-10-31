package tr.xip.wanikani.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.EmptyRequestListener;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.Sku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import tr.xip.wanikani.R;
import tr.xip.wanikani.app.App;
import tr.xip.wanikani.utils.SkuComparator;
import tr.xip.wanikani.widget.adapter.DonationsAdapter;

public class DonationsActivity extends AppCompatActivity {
    private static final int FLIPPER_PROGRESS = 0;
    private static final int FLIPPER_CONTENT = 1;
    private static final int FLIPPER_ERROR = 2;

    private static final List<String> SKUS = new ArrayList<String>() {{
        add("tea");
        add("chocobar");
        add("lunch");
        add("dinner");
        add("extra1");
    }};

    private ViewFlipper flipper;
    private ListView list;
    private TextView error;

    private final ActivityCheckout checkout = Checkout.forActivity(this, App.get().getBilling());
    private Inventory inventory;

    private List<Sku> skus = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donatons);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        list = (ListView) findViewById(R.id.list);
        error = (TextView) findViewById(R.id.error);

        final FirebaseAnalytics fa = FirebaseAnalytics.getInstance(this);

        checkout.start();
        checkout.createPurchaseFlow(new EmptyRequestListener<Purchase>() {
            @Override
            public void onSuccess(@Nonnull final Purchase result) {
                toast(getString(R.string.info_thank_you_donation));
                Log.i("WANIKANI", "IAB purchase success: " + result.sku);

                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, result.sku);
                fa.logEvent("item_purchase_success", bundle);

                // Consume the purchase
                checkout.whenReady(new Checkout.EmptyListener() {
                    @Override
                    public void onReady(@Nonnull BillingRequests requests) {
                        requests.consume(result.token, new EmptyRequestListener<Object>() {
                            @Override
                            public void onSuccess(@Nonnull Object obj) {
                                Log.i("WANIKANI", "IAB purchase consume success: " + result.sku);
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, result.sku);
                                fa.logEvent("item_purchase_consume_success", bundle);
                            }

                            @Override
                            public void onError(int response, @Nonnull Exception e) {
                                Log.e("WANIKANI", "IAB purchase consume fail: " + result.sku);
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, result.sku);
                                fa.logEvent("item_purchase_consume_fail", bundle);
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(int response, @Nonnull Exception e) {
                toast("Error: " + e.getMessage());
                Log.e("WANIKANI", "IAB error (" + response + "): " + e.getMessage());
                e.printStackTrace();

                Bundle bundle = new Bundle();
                bundle.putString("error_message", e.getMessage());
                fa.logEvent("item_purchase_fail", bundle);
            }
        });

        inventory = checkout.makeInventory();
        inventory.load(Inventory.Request.create().loadPurchases(ProductTypes.IN_APP)
                .loadSkus(ProductTypes.IN_APP, SKUS), new Inventory.Callback() {
            @Override
            public void onLoaded(@Nonnull Inventory.Products products) {
                Bundle bundle = new Bundle();
                bundle.putString("list_size", String.valueOf(products.size()));
                fa.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);

                if (products.size() != 0) {
                    skus.clear();
                    skus.addAll(products.get(ProductTypes.IN_APP).getSkus());

                    Collections.sort(skus, new SkuComparator());

                    list.setAdapter(new DonationsAdapter(DonationsActivity.this, skus));

                    flipper.setDisplayedChild(FLIPPER_CONTENT);
                } else {
                    error.setText(R.string.error_no_donation_options_at_the_moment);
                    flipper.setDisplayedChild(FLIPPER_ERROR);
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                checkout.whenReady(new Checkout.EmptyListener() {
                    @Override
                    public void onReady(BillingRequests requests) {
                        String productId = skus.get(position).id.code;

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, productId);
                        fa.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                        requests.purchase(ProductTypes.IN_APP, productId, null, checkout.getPurchaseFlow());
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        checkout.stop();
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkout.onActivityResult(requestCode, resultCode, data);
    }

    private void toast(String text) {
        Toast.makeText(DonationsActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
