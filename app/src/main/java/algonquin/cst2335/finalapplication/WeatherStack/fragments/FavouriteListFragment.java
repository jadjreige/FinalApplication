package algonquin.cst2335.finalapplication.WeatherStack.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.adapter.WeatherRecyclerViewAdapter;
import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStack;
import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStackDatabase;
import algonquin.cst2335.finalapplication.WeatherStack.interfaces.RecyclerItemClickCallback;


/**
 * @author Imandeep Singh Sidhu
 *
 * this fragment is for showing list of saved weather detail city wise
 */
public class FavouriteListFragment extends Fragment implements TextWatcher {

    /* the root view of fragment **/
    private View rootView;

    /** the recycler view **/
    private RecyclerView recyclerView;

    /** the edit text for searching city weather offline **/
    private EditText edtSeach;

    /** the list weather detail cityWise to be displayed in recycler  **/
    private List<WeatherStack> listWeathers;

    /** the list of weather data to be used for searching purpose **/
    private List<WeatherStack> listWeathersNoChange;

    /** the adapter **/
    private WeatherRecyclerViewAdapter weatherRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favourite_list, container, false);
        initialize();
        return rootView;
    }


    /**
     * initilization of widgets
     */
    private void initialize(){
        edtSeach = rootView.findViewById(R.id.edtSearchCity);
        edtSeach.addTextChangedListener(this);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        listWeathers = new ArrayList<>();
        listWeathersNoChange = new ArrayList<>();

        weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(getContext(), listWeathers);
        recyclerView.setAdapter(weatherRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchWeatherDataFromDatabase();
        weatherRecyclerViewAdapter.setRecyclerItemClickCallback(new RecyclerItemClickCallback() {
            @Override
            public void onItemClick(int position) {
                // load offline detail fragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frameLayout,
                                new DetailOfflineFragment(listWeathers.get(position)))
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClick(int position) {
                showDeleteConfirmationDialog(position);
            }
        });

    }

    /**
     * fetching data from local database
     */
    private void fetchWeatherDataFromDatabase(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<WeatherStack> listFromDatabase =
                        WeatherStackDatabase.getInstance(getContext()).weatherStackDAO().getAllWeatherStackRecords();
                for(WeatherStack ws : listFromDatabase){
                    listWeathers.add(ws);
                    listWeathersNoChange.add(ws);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weatherRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
     }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String searchTerm = charSequence.toString().toLowerCase();
        listWeathers.clear();
        for (WeatherStack ws: listWeathersNoChange) {
            if(ws.getCountryName().toLowerCase().contains(searchTerm)
                    || ws.getCityName().toLowerCase().contains(searchTerm)){
                listWeathers.add(ws);
            }
            weatherRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    /**
     * show confirmation dialog for deleting the weatherstack record
     * @param position position of recycler item to be deleted
     */
    private void showDeleteConfirmationDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.confirmation));
        builder.setMessage(getResources().getString(R.string.confirmation_delete));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        // delete from database
                        WeatherStackDatabase
                                .getInstance(getContext())
                                .weatherStackDAO()
                                .deleteWeatherStackRecord(listWeathers.get(position));
                        listWeathersNoChange.remove(listWeathers.remove(position));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(rootView, getString(R.string.record_deleted), Snackbar.LENGTH_SHORT).show();
                                weatherRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}