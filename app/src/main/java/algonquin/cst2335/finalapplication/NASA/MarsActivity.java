package algonquin.cst2335.finalapplication.NASA;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalapplication.KittenPlaceholder.KittenActivity;
import algonquin.cst2335.finalapplication.MainActivity;
import algonquin.cst2335.finalapplication.NewYorkTimes.NewYorkActivity;
import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.WeatherActivity;
import algonquin.cst2335.finalapplication.databinding.ActivityMarsBinding;
import algonquin.cst2335.finalapplication.databinding.ViewMarsResultsBinding;

/**
 * Class for the first mars activity that lets you type a number from 1 to 1000 to show the pictures taken on that certain date.
 * It includes a recyclerView, fragment, toast, snackBar and alert dialog.
 * @Author Jad Jreige
 */
public class MarsActivity extends AppCompatActivity {

    ArrayList<MarsDTO> marsList;
    MarsDTO marsDTO;
    protected RequestQueue queue = null;

    private RecyclerView.Adapter myAdapter;
    MarsViewModel marsModel;
    MarsDAO phDAO;
    int position;
    ImageView image;
    TextView text;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.mars_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.save:

                MarsDTO ph = marsList.get(position);

                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() ->
                {
                    long id = phDAO.insertPhoto(ph);
                    ph.id = (int) id;

                });

                Picasso.get().load(ph.getImageUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        String fileName = "marsPhoto-" + position + ".png";
                        File file = new File(getFilesDir(), fileName);
                        try {

                            if (!file.exists()) {
                                file.createNewFile();
                            }


                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                            Log.d(TAG, "Image saved to " + file);

                        } catch (Exception e) {
                            Log.e(TAG, "Error saving image: " + e.getMessage());

                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

                Toast.makeText(MarsActivity.this, "Saved photo", Toast.LENGTH_SHORT).show();

                break;

            case R.id.about:

                AlertDialog.Builder builder = new AlertDialog.Builder(MarsActivity.this);
                builder.setMessage(R.string.helpMars)
                        .setTitle("Help")

                        .setNegativeButton("ok", (dialog, cl) -> {

                        })
                        .create().show();

                break;

            case R.id.showSaved:

                Intent dataPage = new Intent(MarsActivity.this, MarsDataActivity.class);

                startActivity(dataPage);

                break;

            case R.id.kitten:

                Intent kitten = new Intent(MarsActivity.this, KittenActivity.class);

                startActivity(kitten);

                break;

            case R.id.weather:

                Intent weather = new Intent(MarsActivity.this, WeatherActivity.class);

                startActivity(weather);

                break;

            case R.id.newYork:

                Intent ny = new Intent(MarsActivity.this, NewYorkActivity.class);

                startActivity(ny);

                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = newRequestQueue(this);

        ActivityMarsBinding binding = ActivityMarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        PhotoDatabase db = Room.databaseBuilder(getApplicationContext(), PhotoDatabase.class, "database-name").build();
        phDAO = db.mDAO();

        SharedPreferences prefs = getSharedPreferences("MyMarsData", Context.MODE_PRIVATE);

        String userInput = prefs.getString("InputNumbers", "");
        binding.textInput.setText(userInput);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        marsModel = new ViewModelProvider(this).get(MarsViewModel.class);
        marsModel.selectedPhoto.observe(this, (newPhotoValue) -> {

            MarsDetailsFragment photoFrag = new MarsDetailsFragment(newPhotoValue);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, photoFrag)
                    .addToBackStack("")
                    .commit();
        });

        marsList = new ArrayList<MarsDTO>();


        binding.search.setOnClickListener( clk -> {

            String input = binding.textInput.getText().toString();

            marsList.clear();

            if (input.matches("[0-9]+")) {


                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("InputNumbers", binding.textInput.getText().toString());
                editor.apply();

                String stringUrl;

                try {
                    stringUrl = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + URLEncoder.encode(input, "UTF-8") + "&api_key=sUaKzNCKJSOH5w0Y15GS0Mr7JG8cwantgIuvD3Ph";
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null,
                        (response) -> {

                            try {
                                JSONArray photos = response.getJSONArray("photos");
                                int jsonLength = photos.length();

                                for (int i = 0; i < jsonLength; i++) {

                                    JSONObject position = photos.getJSONObject(i);
                                    String imgSrc = position.getString("img_src");

                                    JSONObject camera = position.getJSONObject("rover");
                                    String rover = camera.getString("name");

                                    marsDTO = new MarsDTO(imgSrc, rover);
                                    marsList.add(marsDTO);
                                    //myAdapter.notifyItemInserted(marsList.size() - 1);
                                }

                                Snackbar.make(binding.getRoot(), marsList.size() + " Photos loaded", Snackbar.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }, (error) -> {

                });

                queue.add(request);

            }

            else {
                Toast.makeText(MarsActivity.this, "Please enter only numbers between 1 and 1000", Toast.LENGTH_LONG).show();
            }


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
                text.setText(obj.getName());
                Picasso.get().load(obj.getImageUrl()).into(image);
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
     */
    class MyRowHolder extends RecyclerView.ViewHolder {

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {

                position = getAdapterPosition();
                MarsDTO selected = marsList.get(position);
                marsModel.selectedPhoto.postValue(selected);

            });

            image = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.roverName);
        }
    }
}