package algonquin.cst2335.finalapplication.WeatherStack.interfaces;



import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStack;

public interface RestApiCallback {

    void onResponseReceived(WeatherStack weatherStack);

    void onError(String errorMessage);

}
