package algonquin.cst2335.finalapplication.NASA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalapplication.KittenPlaceholder.KittenActivity;
import algonquin.cst2335.finalapplication.NewYorkTimes.NewYorkActivity;
import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.WeatherActivity;
import algonquin.cst2335.finalapplication.databinding.ActivityMarsDataBinding;
import algonquin.cst2335.finalapplication.databinding.ViewMarsResultsBinding;

/**
 * This class hold all the saves photos and shows then in a recyclerView, and when the user presses on the item an alert dialog pops up to ask if they want to delete the photo.
 */
public class MarsDataActivity extends AppCompatActivity {

    ActivityMarsDataBinding binding;

    MarsDAO phDAO;
    private RecyclerView.Adapter myAdapter;
    ArrayList<MarsDTO> marsList;
    MarsDTO marsDTO;
    MarsViewModel marsModel;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.about:

                AlertDialog.Builder builder = new AlertDialog.Builder(MarsDataActivity.this);
                builder.setMessage("To use this application you need to enter a number between 1 and 1000 to load pictures from the rover that were taken on a certain day.\n" +
                                "\n" +
                                "By pressing on a picture, you will get an expanded version of that picture with the image URL that you can press to take you to the browser.\n" +
                                "\n" +
                                "Once you expand the picture, you can also save it to the database of favorites and for offline viewing.\n" +
                                "\n" +
                                "Finally, you can press the toolbar option to show your selected favorites and remove them.")
                        .setTitle("Help")

                        .setNegativeButton("ok", (dialog, cl) -> {

                        })
                        .create().show();

                break;

            case R.id.kitten:

                Intent kitten = new Intent(MarsDataActivity.this, KittenActivity.class);

                startActivity(kitten);

                break;

            case R.id.weather:

                Intent weather = new Intent(MarsDataActivity.this, WeatherActivity.class);

                startActivity(weather);

                break;

            case R.id.newYork:

                Intent ny = new Intent(MarsDataActivity.this, NewYorkActivity.class);

                startActivity(ny);

                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.mars_data_menu, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        marsModel = new ViewModelProvider(this).get(MarsViewModel.class);

        binding = ActivityMarsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        PhotoDatabase db = Room.databaseBuilder(getApplicationContext(), PhotoDatabase.class, "database-name").build();
        phDAO = db.mDAO();

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() ->
        {
            marsList = new ArrayList<MarsDTO>();
            marsList.addAll(phDAO.getAllPhotos());

            runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ViewMarsResultsBinding marsBinding = ViewMarsResultsBinding.inflate(getLayoutInflater(), parent, false);

                return new MyRowHolder(marsBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                MarsDTO obj = marsList.get(position);
                holder.text.setText(obj.getName());
                Picasso.get().load(obj.getImageUrl()).into(holder.image);

            }

            @Override
            public int getItemCount() {
                return marsList.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

        });
    }
    /**
     * This inner class sets the items in the recyclerView and gets the position for the item when it is pressed.
     * In addition, when pressed it pops up an alert dialog to delete the message.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {

                int position = getAdapterPosition();

                MarsDTO selected = marsList.get(position);
                marsModel.selectedPhoto.postValue(selected);

                AlertDialog.Builder builder = new AlertDialog.Builder(MarsDataActivity.this);
                builder.setMessage("Do you want to delete the photo from the rover " + text.getText() + " ?")
                        .setTitle("Delete")
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            MarsDTO ph = marsList.get(position);

                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() ->
                            {
                                phDAO.deletePhoto(ph);

                                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter ));
                            });

                            marsList.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(text, "You deleted message #" + position, Snackbar.LENGTH_LONG).show();
                        })
                        .setNegativeButton("No", (dialog, cl) -> {

                        })
                        .create().show();
            });

            image = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.roverName);
        }
    }
}