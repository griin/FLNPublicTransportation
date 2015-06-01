package controlers;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fln.flnpublictransportation.R;

import dto.Stop;

public class StopAdapter extends ArrayAdapter<Stop> {

    private List<Stop> itemList;
    private Context context;

    public StopAdapter(List<Stop> itemList, Context context) {
        super(context, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = context;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Stop getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_stop, null);
        }

        Stop stop = itemList.get(position);

        TextView textName = (TextView) view.findViewById(R.id.stop);
        textName.setText(stop.getSequence() + " - " +  stop.getName());

        return view;
    }

    public List<Stop> getItemList() {
        return itemList;
    }

    public void setItemList(List<Stop> itemList) {
        this.itemList = itemList;
    }
}
