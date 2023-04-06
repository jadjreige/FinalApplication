package algonquin.cst2335.finalapplication.NASA;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.finalapplication.MainActivity;
import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.databinding.ActivityMarsBinding;

public class MarsActivity extends AppCompatActivity {

    ArrayList<MarsDTO> marsList;
    MarsDTO marsDTO;
    protected RequestQueue queue = null;

    //https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol= + DATATIME + &api_key=sUaKzNCKJSOH5w0Y15GS0Mr7JG8cwantgIuvD3Ph

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.mars_menu, menu);

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

        binding.search.setOnClickListener( clk -> {

            String input = binding.textInput.getText().toString();


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

    }
}