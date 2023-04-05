package algonquin.cst2335.finalapplication.NASA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import algonquin.cst2335.finalapplication.MainActivity;
import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.databinding.ActivityMarsBinding;

public class MarsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMarsBinding binding = ActivityMarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol= + DATATIME + &api_key=sUaKzNCKJSOH5w0Y15GS0Mr7JG8cwantgIuvD3Ph

        binding.search.setOnClickListener( clk -> {

            String input = binding.textInput.getText().toString();


            if (input.matches("[0-9]+")) {

                Intent results = new Intent(MarsActivity.this, MarsSecondActivity.class);

                startActivity(results);
            }

            else {
                Toast.makeText(MarsActivity.this, "Please enter numbers between 1 and 1000", Toast.LENGTH_SHORT).show();
            }


        });

    }
}