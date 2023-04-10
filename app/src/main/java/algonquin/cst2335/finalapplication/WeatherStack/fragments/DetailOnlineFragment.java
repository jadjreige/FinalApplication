package algonquin.cst2335.finalapplication.WeatherStack.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStack;
import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStackDatabase;

/**
 * @author Imandeep Singh Sidhu
 * version 1
 * This class holds all the data when it is fetched with internet connection
 */
public class DetailOnlineFragment extends Fragment {

    /** the city name received from previious fragment i.e. home fragment **/
    private String cityName;

    /** the root view of fragment **/
    private View rootView;

    /** the weather stack object containing detail received  **/
    private WeatherStack weatherStack;

    private TextView txtCityName;
    private TextView txtdateTime;
    private TextView txtTemperature;
    private TextView txtobservedTime;
    private TextView txtPrecipitation;
    private TextView txthumidity;
    private TextView txtUVIndex;
    private TextView txtPressure;
    private TextView txtVisibility;
    private TextView txtWindSpeed;
    private TextView txtWindDegree;
    private TextView txtWindDirection;
    private TextView txtCloudCover;
    private TextView txtFellLike;
    private TextView txtWeatherDesc;
    private ImageView imgWeatherIcon;
    private Button btnSave;


    public DetailOnlineFragment() {
        // Required empty public constructor
    }

    public DetailOnlineFragment(WeatherStack weatherStack) {
        this.weatherStack = weatherStack;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_detail_online, container, false);
        initializeWidgets();
        fillWidets();
        return rootView;
    }


    /**
     * initialize all widgets
     */
    private void initializeWidgets(){
        txtCityName = rootView.findViewById(R.id.txtCityName);
        txtdateTime = rootView.findViewById(R.id.txtDataTime);
        txtTemperature = rootView.findViewById(R.id.txtTemperature);
        txtobservedTime = rootView.findViewById(R.id.txtObservedTime);
        txtPrecipitation = rootView.findViewById(R.id.txtPrecipitation);
        txthumidity = rootView.findViewById(R.id.txtHumidity);
        txtUVIndex = rootView.findViewById(R.id.txtUVindex);
        txtPressure = rootView.findViewById(R.id.txtPressure);
        txtVisibility = rootView.findViewById(R.id.txtVisibility);
        txtWindSpeed = rootView.findViewById(R.id.txtWindSpeed);
        txtWindDegree = rootView.findViewById(R.id.txtWindDegree);
        txtWindDirection = rootView.findViewById(R.id.txtWindDirection);
        txtCloudCover = rootView.findViewById(R.id.txtCloudCover);
        txtFellLike = rootView.findViewById(R.id.txtFeelLike);
        txtWeatherDesc = rootView.findViewById(R.id.txtweatherDescription);
        imgWeatherIcon = rootView.findViewById(R.id.imgWeatherIcon);
        btnSave = rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        WeatherStackDatabase.getInstance(getContext()).weatherStackDAO().insertWeatherStackRecord(weatherStack);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getResources().getString(R.string.weather_data_saved_message), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }


    private void fillWidets(){
        txtCityName.setText(weatherStack.getCityName() + ", " + weatherStack.getCountryName());
        txtdateTime.setText(weatherStack.getDateTime() + "");
        txtTemperature.setText(weatherStack.getTemperature() + "");
        txtobservedTime.setText(weatherStack.getObservedTime() + "");
        txtPrecipitation.setText(weatherStack.getPrecipitation() + " " + getString(R.string.milimeter));
        txthumidity.setText(weatherStack.getHumidity() + " %");
        txtUVIndex.setText(weatherStack.getUvIndex() + "");
        txtPressure.setText(weatherStack.getPressure() + " " + getString(R.string.milibar));
        txtVisibility.setText(weatherStack.getVisibility() + " " + getString(R.string.kilometer));
        txtWindSpeed.setText(weatherStack.getWindSpeed() + " " + getString(R.string.kmph));
        txtWindDegree.setText(weatherStack.getWindDegree() + " ");
        txtWindDirection.setText(weatherStack.getWindDirection());
        txtCloudCover.setText(weatherStack.getCloudCover() + " %");
        txtFellLike.setText(weatherStack.getFeelLike() + " " + getString(R.string.celsius));
        txtWeatherDesc.setText(weatherStack.getWeatherDesc() + "");
        Picasso.get()
                .load(weatherStack.getWeatherThumnailIcon())
                .placeholder(R.mipmap.ic_launcher)
                .into(imgWeatherIcon);

    }
}