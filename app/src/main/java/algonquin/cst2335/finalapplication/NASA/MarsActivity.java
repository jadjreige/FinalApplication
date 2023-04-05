package algonquin.cst2335.finalapplication.NASA;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.databinding.ActivityMarsBinding;

public class MarsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMarsBinding binding = ActivityMarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}