package algonquin.cst2335.finalapplication.WeatherStack;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.fragments.FavouriteListFragment;

/**
 * @author Imandeep Singh Sidhu
 * This class basically shows favourite activity along with
 * menu items
 */

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // Load favourite fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, new FavouriteListFragment())
                .commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
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