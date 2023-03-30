package algonquin.cst2335.finalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import algonquin.cst2335.finalapplication.KittenPlaceholder.KittenActivity;
import algonquin.cst2335.finalapplication.NASA.MarsActivity;
import algonquin.cst2335.finalapplication.NewYorkTimes.NewYorkActivity;
import algonquin.cst2335.finalapplication.WeatherStack.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nasa = findViewById(R.id.NASA);
        Button kitten = findViewById(R.id.kitten);
        Button newYork = findViewById(R.id.newYork);
        Button weather = findViewById(R.id.weather);

        nasa.setOnClickListener( clk -> {

            Intent nasaPage = new Intent(MainActivity.this, MarsActivity.class);

            startActivity(nasaPage);
        });

        kitten.setOnClickListener( clk -> {

            Intent kittenPage = new Intent(MainActivity.this, KittenActivity.class);

            startActivity(kittenPage);
        });

        newYork.setOnClickListener( clk -> {

            Intent newYorkPage = new Intent(MainActivity.this, NewYorkActivity.class);

            startActivity(newYorkPage);
        });

        weather.setOnClickListener( clk -> {

            Intent weatherPage = new Intent(MainActivity.this, WeatherActivity.class);

            startActivity(weatherPage);
        });
    }
}