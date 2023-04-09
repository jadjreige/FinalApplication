package algonquin.cst2335.finalapplication.WeatherStack.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStack;
import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStackPrefrences;
import algonquin.cst2335.finalapplication.WeatherStack.interfaces.RestApiCallback;
import algonquin.cst2335.finalapplication.WeatherStack.network.WeatherStackAPI;

/**
 * @author ImanDeep Singh
 * version 1
 *
 * this is home fragment where the user can search weather for city
 */
public class HomeFragment extends Fragment {

    /** the root view of fragment **/
    private View rootView;

    /** the seach edit text **/
    private EditText edtSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return rootView;
    }


    /**
     * initilization of widgets
     */
    private void init(){
        edtSearch = rootView.findViewById(R.id.edtSearchCity);
        edtSearch.setText(WeatherStackPrefrences.getInstance(getContext()).getFromPrefrences("city_searched", ""));
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    // hide keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

                    if(edtSearch.length() == 0){
                        Snackbar.make(rootView, getResources().getString(R.string.please_enter_city), Snackbar.LENGTH_LONG).show();
                        return true;
                    }

                    getWeatherDetailFromServer(edtSearch.getText().toString().trim());

                    return true;
                }
                return false;
            }
        });
    }


    /**
     * call rest API to get weather detail from server
     * @param cityName
     */
    private void getWeatherDetailFromServer(String cityName){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching weather detail, please wait...");
        progressDialog.show();
        WeatherStackAPI.getInstance(getContext()).getWeatherDetail(cityName, new RestApiCallback() {
            @Override
            public void onResponseReceived(WeatherStack weatherStack) {
                progressDialog.dismiss();
                // saving search term into prefrences
                WeatherStackPrefrences.getInstance(getContext()).saveToPrefrences("city_searched", cityName);
                loadingDetailScreen(weatherStack);

            }

            @Override
            public void onError(String errorMessage) {
                progressDialog.dismiss();
                showMessage(errorMessage);

            }
        });
    }



    /**
     * load Detail weather fragment to show the detail of city weather
     */
    private void loadingDetailScreen(WeatherStack weatherStack){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, new DetailOnlineFragment(weatherStack))
                .addToBackStack(null)
                .commit();
    }


    /**
     * show alert dialog with message
     * @param message
     */
    private void showMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error");
        builder.setMessage(message + "");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

}