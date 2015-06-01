package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import com.fln.flnpublictransportation.R;
import android.view.inputmethod.InputMethodManager;
import controlers.RestServices;
import controlers.RouteAdapter;
import dto.Route;
import android.content.Context;

public class MainActivity extends Activity {

    private RouteAdapter routeAdapter;
    private EditText inputSearch;
    private String searchKey;
    private ListView listRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputSearch = (EditText) findViewById(R.id.item_search);

        routeAdapter  = new RouteAdapter(new ArrayList<Route>(), this);
        listRoutes = (ListView) findViewById(R.id.list_routes);
        listRoutes.setAdapter(routeAdapter);

        Bundle params = getIntent().getExtras();
        if(params != null) {
            searchKey = params.getString("searchKey");
            inputSearch.setText(searchKey);
            (new AsyncListRoutesLoader()).execute(getString(R.string.findRoutesByStopName));
        }

        Button btnSearch = (Button) findViewById(R.id.button_search);
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKey = inputSearch.getText().toString();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                (new AsyncListRoutesLoader()).execute(getString(R.string.findRoutesByStopName));
            }
        });

        listRoutes.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Route selectedRoute = routeAdapter.getItem(position);

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                Bundle params = new Bundle();
                params.putString("id", selectedRoute.getId());
                params.putString("name", selectedRoute.getLongName() + " - " + selectedRoute.getShortName());
                params.putString("searchKey", searchKey);
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });
    }

    private class AsyncListRoutesLoader extends AsyncTask<String, Void, List<Route>> {
        private final ProgressDialog dialogRoutesLoader = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPostExecute(List<Route> listRoutes) {
            super.onPostExecute(listRoutes);
            dialogRoutesLoader.dismiss();
            routeAdapter.setItemList(listRoutes);
            routeAdapter.notifyDataSetChanged();
            if(listRoutes == null || listRoutes.isEmpty()) {
                Toast.makeText(getApplicationContext(),R.string.no_routes, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogRoutesLoader.setMessage(getString(R.string.loading_routes));
            dialogRoutesLoader.show();
        }

        @Override
        protected List<Route> doInBackground(String... params) {
            List<Route> listRoutes = new ArrayList<Route>();

            try {
                RestServices restService = new RestServices();
                String param =  "{\"params\":{\"stopName\": \"%" + searchKey.replace(" ","%") + "%\"}}";

                JSONObject objJSON = restService.connectToRestService(params[0], param, getApplicationContext());
                JSONArray arrJSON = objJSON.getJSONArray("rows");

                for (int i = 0; i < arrJSON.length(); i++) {
                    listRoutes.add(restService.convertRoute(arrJSON.getJSONObject(i)));
                }

                return listRoutes;
            }
            catch(Exception ex) {
                Toast.makeText(getApplicationContext(),ex.getMessage() , Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
