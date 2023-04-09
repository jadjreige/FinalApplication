package algonquin.cst2335.finalapplication.WeatherStack.interfaces;



import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStack;

/**
 * @author Imandeep Singh Sidhu
 * This interface holds the methods representing weather there was an error or
 * response from server
 */
public interface RestApiCallback {

    void onResponseReceived(WeatherStack weatherStack);

    void onError(String errorMessage);

}
