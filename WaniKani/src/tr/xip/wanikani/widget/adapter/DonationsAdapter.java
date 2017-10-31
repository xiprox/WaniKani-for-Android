package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.solovyev.android.checkout.Sku;

import java.util.List;

import tr.xip.wanikani.R;

public class DonationsAdapter extends ArrayAdapter<Sku> {

    private List<Sku> items;

    public DonationsAdapter(Context context, List<Sku> items) {
        super(context, R.layout.item_donation, items);
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Sku item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_donation, parent, false);
        }

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView desc = (TextView) v.findViewById(R.id.description);
        TextView price = (TextView) v.findViewById(R.id.price);

        title.setText(item.getDisplayTitle());
        desc.setText(item.description);
        price.setText(item.price);

        return v;
    }
}