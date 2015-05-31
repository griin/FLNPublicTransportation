package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.fln.flnpublictransportation.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import controlers.RestServices;
import controlers.StopAdapter;
import dto.Departure;
import dto.Stop;

public class DetailsActivity extends Activity {
    private String id;
    private String searchKey;
    private StopAdapter stopAdapter;
    private String strWeekDay = "";
    private String strSaturday = "";
    private String strSunday = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle params = getIntent().getExtras();
        id = params.getString("id");
        searchKey = params.getString("searchKey");
        TextView txtTitle = (TextView) findViewById(R.id.details_title_route);
        txtTitle.setText(params.getString("name"));

        Button btnBack = (Button) findViewById(R.id.button_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                Bundle params = new Bundle();
                params.putString("searchKey", searchKey);
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });

        stopAdapter  = new StopAdapter(new ArrayList<Stop>(), this);
        ListView listViewStops = (ListView) findViewById(R.id.list_stops);
        listViewStops.setAdapter(stopAdapter);

        (new AsyncListStopsLoader()).execute(getString(R.string.findStopsByRouteId));
        (new AsyncListDeparturesLoader()).execute(getString(R.string.findDeparturesByRouteId));
    }

    private class AsyncListStopsLoader extends AsyncTask<String, Void, List<Stop>> {

        @Override
        protected void onPostExecute(List<Stop> listStop) {
            super.onPostExecute(listStop);
            stopAdapter.setItemList(listStop);
            stopAdapter.notifyDataSetChanged();
            if(listStop == null || listStop.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.no_stops, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Stop> doInBackground(String... params) {
            List<Stop> listStop = new ArrayList<Stop>();

            try {
                RestServices restService = new RestServices();
                String param =  "{\"params\":{\"routeId\": \"" + id + "\"}}";

                JSONObject objJSON = restService.connectToRestService(params[0], param, getApplicationContext());
                JSONArray arrJSON = objJSON.getJSONArray("rows");

                for (int i = 0; i < arrJSON.length(); i++) {
                    listStop.add(restService.convertStop(arrJSON.getJSONObject(i)));
                }
            }
            catch(Exception ex) {
                Toast.makeText(getApplicationContext(),ex.getMessage() , Toast.LENGTH_LONG).show();
            }
            return listStop;
        }
    }

    private class AsyncListDeparturesLoader extends AsyncTask<String, Void, List<Departure>> {
        private final ProgressDialog dialogDeparturesLoader = new ProgressDialog(DetailsActivity.this);

        @Override
        protected void onPostExecute(List<Departure> listDeparture) {
            super.onPostExecute(listDeparture);
            dialogDeparturesLoader.dismiss();

            TextView textWeekDay = (TextView) findViewById(R.id.departure_weekday);
            textWeekDay.setText(textWeekDay.getText() + strWeekDay);
            TextView textSaturday = (TextView) findViewById(R.id.departure_saturday);
            textSaturday.setText(textSaturday.getText() + strSaturday);
            TextView textMonday = (TextView) findViewById(R.id.departure_sunday);
            textMonday.setText(textMonday.getText() + strSunday);

            if(listDeparture == null || listDeparture.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.no_departures, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogDeparturesLoader.setMessage(getString(R.string.loading_departures));
            dialogDeparturesLoader.show();
        }

        @Override
        protected List<Departure> doInBackground(String... params) {
            List<Departure> listDeparture = new ArrayList<Departure>();

            try {
                RestServices restService = new RestServices();
                String param =  "{\"params\":{\"routeId\": \"" + id + "\"}}";

                JSONObject objJSON = restService.connectToRestService(params[0], param, getApplicationContext());
                JSONArray arrJSON = objJSON.getJSONArray("rows");


                for (int i = 0; i < arrJSON.length(); i++) {

                    Departure departure = restService.convertDeparture(arrJSON.getJSONObject(i));
                    listDeparture.add(departure);

                    if(departure.getCalendar().toUpperCase().equals("WEEKDAY")) {
                        strWeekDay += " - " + departure.getTime();
                    }
                    else if(departure.getCalendar().toUpperCase().equals("SATURDAY")) {
                        strSaturday += " - " + departure.getTime();
                    }
                    else if (departure.getCalendar().toUpperCase().equals("SUNDAY")) {
                        strSunday += " - " + departure.getTime();
                    }
                }
            }
            catch(Exception ex) {
                Toast.makeText(getApplicationContext(),ex.getMessage() , Toast.LENGTH_LONG).show();
            }
            return listDeparture;
        }
    }
}
