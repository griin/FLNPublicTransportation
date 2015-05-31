package controlers;

import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;

import dto.Route;
import dto.Stop;
import dto.Departure;

public class RestServices {
    public JSONObject connectToRestService(String url_service, String param, Context context)
    {
        try {
            if(url_service == null || url_service.length() == 0)
                throw new Exception("URL invalid");

            URL url = new URL(url_service);
            HttpURLConnection connectionHttp = (HttpURLConnection) url.openConnection();
            connectionHttp.setRequestMethod("POST");
            connectionHttp.setRequestProperty("X-AppGlu-Environment", "staging");
            connectionHttp.setRequestProperty("Content-Type", "application/json");
            String base64EncodedCredentials = Base64.encodeToString(
                    ("WKD4N7YMA1uiM8V:DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68").getBytes(), Base64.DEFAULT);
            connectionHttp.setRequestProperty("Authorization", "Basic " + base64EncodedCredentials);

            connectionHttp.setDoOutput(true);

            if(param != null && param.length() > 0) {
                byte[] outputInBytes = param.getBytes("UTF-8");
                OutputStream os = connectionHttp.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }
            connectionHttp.connect();
            int responseCode = connectionHttp.getResponseCode();

            if(responseCode >= 400)
                throw new Exception("Unable to connect to rest service: " +  connectionHttp.getErrorStream());

            InputStream inputStream = connectionHttp.getInputStream();

            // Read the stream
            byte[] b = new byte[1024];
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();

            while ( inputStream.read(b) != -1)
                bAOS.write(b);

            return new JSONObject(bAOS.toString());
        }
        catch(Throwable t) {
            Toast.makeText(context,t.getMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    public Route convertRoute(JSONObject objJSON) throws JSONException {
        return new Route(objJSON.getString("id"),objJSON.getString("shortName"),objJSON.getString("longName"),
                objJSON.getString("lastModifiedDate"),objJSON.getString("agencyId"));
    }

    public Stop convertStop(JSONObject objJSON) throws JSONException {
        return new Stop(objJSON.getString("id"),objJSON.getString("name"),
                objJSON.getString("sequence"),objJSON.getString("route_id"));
    }

    public Departure convertDeparture(JSONObject objJSON) throws JSONException {
        return new Departure(objJSON.getString("id"),objJSON.getString("calendar"),objJSON.getString("time"));
    }
}
