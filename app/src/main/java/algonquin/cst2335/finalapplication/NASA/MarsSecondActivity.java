package algonquin.cst2335.finalapplication.NASA;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.databinding.ActivityMarsSecondBinding;

public class MarsSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMarsSecondBinding binding = ActivityMarsSecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol= + DATATIME + &api_key=sUaKzNCKJSOH5w0Y15GS0Mr7JG8cwantgIuvD3Ph


    }
}