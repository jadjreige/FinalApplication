package algonquin.cst2335.finalapplication.NASA;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.databinding.ActivityMarsBinding;
import algonquin.cst2335.finalapplication.databinding.ViewMarsResultsBinding;

public class MarsActivity extends AppCompatActivity {

    ArrayList<MarsDTO> marsList;
    MarsDTO marsDTO;
    protected RequestQueue queue = null;

    private RecyclerView.Adapter myAdapter;
    MarsViewModel marsModel;

    //https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol= + DATATIME + &api_key=sUaKzNCKJSOH5w0Y15GS0Mr7JG8cwantgIuvD3Ph

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //getMenuInflater().inflate(R.menu.mars_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = newRequestQueue(this);

        ActivityMarsBinding binding = ActivityMarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


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

    class MyRowHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {

                int position = getAdapterPosition();
                MarsDTO selected = marsList.get(position);
                marsModel.selectedPhoto.postValue(selected);

            });

            image = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.roverName);
        }
    }
}