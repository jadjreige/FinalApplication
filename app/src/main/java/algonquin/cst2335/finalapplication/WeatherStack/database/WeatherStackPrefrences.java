package algonquin.cst2335.finalapplication.WeatherStack.database;

import android.content.Context;
import android.content.SharedPreferences;

public class WeatherStackPrefrences {

    /** the object of this class **/
    private static WeatherStackPrefrences weatherStackPrefrences;

    /** the preferences object **/
    private SharedPreferences sharedPreferences;

    /** the private constructor **/
     private WeatherStackPrefrences(Context context){
        sharedPreferences = context.getSharedPreferences("Cocktail_pref", Context.MODE_PRIVATE);
     }

    /** the method to get instance of this class **/
    public static WeatherStackPrefrences getInstance(Context context){
        if(weatherStackPrefrences == null)
            weatherStackPrefrences = new WeatherStackPrefrences(context);

        return weatherStackPrefrences;
    }

    /**
     * to save string to prefrences
     * @param key
     * @param value
     */
    public void saveToPrefrences(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
    }

    public String getFromPrefrences(String key, String defValue){
        return sharedPreferences.getString(key, defValue);
    }


}
