package algonquin.cst2335.finalapplication.WeatherStack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.fragments.HomeFragment;


/***
 * @Author Imandeep
 * version 1
 *  this is main activity which load home fragment and others
 */
public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Load home fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, new HomeFragment())
                .commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainactivity_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            // favourite icon selected
            case R.id.action_favourite:
                // start Favourite activity
                Intent intent = new Intent(
                        WeatherActivity.this,
                        FavouriteActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_help:
                showHelpDialog();
                return true;
        }
        return false;
    }


    /**
     * show help dialog alert
     */
    private void showHelpDialog(){
        String message = getString(R.string.help_message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(getString(R.string.help));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

}