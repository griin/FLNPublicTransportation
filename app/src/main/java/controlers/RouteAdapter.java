package controlers;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fln.flnpublictransportation.R;

import dto.Route;

public class RouteAdapter extends ArrayAdapter<Route> {

    private List<Route> itemList;
    private Context context;

    public RouteAdapter(List<Route> itemList, Context context) {
        super(context, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.context = context;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Route getItem(int position) {
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
            view = inflater.inflate(R.layout.list_item_route, null);
        }

        Route route = itemList.get(position);

        TextView textName = (TextView) view.findViewById(R.id.route);
        textName.setText(route.getLongName() + " - " + route.getShortName());

        return view;
    }

    public List<Route> getItemList() {
        return itemList;
    }

    public void setItemList(List<Route> itemList) {
        this.itemList = itemList;
    }
}
