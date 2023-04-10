package algonquin.cst2335.finalapplication.WeatherStack.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStack;
import algonquin.cst2335.finalapplication.WeatherStack.interfaces.RestApiCallback;

/**
 * Imandeep Singh Sidhu
 * this class handles network calls that retreive the data from server
 * and loads into the application
 */

public class WeatherStackAPI {

    /** the api key  **/
    private final String KEY = "607bc9568a1c3cfa5c07861a0d134577";

    /** the context **/
    private static Context mContext;

    /** the instance of this class **/
    private static WeatherStackAPI webstackAPICall;

    /** the the URL **/
    private final String URL = "http://api.weatherstack.com/current?access_key=";

    /** make it singleton class **/
    private WeatherStackAPI(){}


    /**
     * get instance of this class to access other member functions
     * @param _context
     * @return
     */
    public static WeatherStackAPI getInstance(Context _context){
        mContext = _context;
        if(webstackAPICall == null)
            webstackAPICall = new WeatherStackAPI();

        return webstackAPICall;
    }


    /**
     * using volley library to fetch the weather detail from server
     * the result and  load into the model class
     *
     * @param cityName of which weather is to be checked
     */
    public void getWeatherDetail(String cityName, RestApiCallback restApiCallback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL + KEY + "&query=" + cityName,
                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // on response received from server
                        WeatherStack weatherStack = new WeatherStack();
                        try {

                            if(response.has("error")){
                                // error occured
                                String errorMessage = response.getJSONObject("error").getString("info");
                                restApiCallback.onError(errorMessage);
                                return;
                            }

                            // parsing json data into model class
                            JSONObject localtionObj = response.getJSONObject("location");
                            weatherStack.setCityName(localtionObj.getString("name"));
                            weatherStack.setCountryName(localtionObj.getString("country"));
                            weatherStack.setDateTime(localtionObj.getString("localtime"));

                            JSONObject currentObj = response.getJSONObject("current");
                            weatherStack.setObservedTime(currentObj.getString("observation_time"));
                            weatherStack.setTemperature(currentObj.getInt("temperature"));

                            JSONArray iconsArray = currentObj.getJSONArray("weather_icons");
                            if(iconsArray.length() > 0){
                                weatherStack.setWeatherThumnailIcon(iconsArray.getString(0));
                            }

                            JSONArray descArray = currentObj.getJSONArray("weather_descriptions");
                            String desc = "";
                            for(int i = 0; i < descArray.length(); i++){
                                desc = desc + descArray.getString(i) + "\n";
                            }

                            weatherStack.setWeatherDesc(desc);
                            weatherStack.setWindSpeed(currentObj.getInt("wind_speed"));
                            weatherStack.setWindDegree(currentObj.getInt("wind_degree"));
                            weatherStack.setWindDirection(currentObj.getString("wind_dir"));
                            weatherStack.setPressure(currentObj.getInt("pressure"));
                            weatherStack.setPrecipitation(currentObj.getInt("precip"));
                            weatherStack.setHumidity(currentObj.getInt("humidity"));
                            weatherStack.setCloudCover(currentObj.getInt("cloudcover"));
                            weatherStack.setFeelLike(currentObj.getInt("feelslike"));
                            weatherStack.setUvIndex(currentObj.getInt("uv_index"));
                            weatherStack.setVisibility(currentObj.getInt("visibility"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        restApiCallback.onResponseReceived(weatherStack);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // on error occurred
                        restApiCallback.onError(error.getMessage() + "");
                    }
                }
        );

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
    }




}
