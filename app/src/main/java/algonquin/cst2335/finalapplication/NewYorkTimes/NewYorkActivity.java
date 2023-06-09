
package algonquin.cst2335.finalapplication.NewYorkTimes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2335.finalapplication.R;


/**
 * MainActivity class that allows the user to search for articles using a search term.
 * The user's search term is saved in SharedPreferences, allowing it to be loaded when the
 * app is opened again. This activity also provides a help menu option to assist the user.
 */

public class NewYorkActivity extends AppCompatActivity {
    /**
     *  Constants for SharedPreferences key and preference name
     */
    public static final String SEARCH_TERM_KEY ="search_term_key";
    private static final String PREFS_NAME = "NYTArticleSearch";
    public static final String API_KEY = "J1vZy9jEI9445lwJjOHFr8quBuE8JrCB";

    /**
     *  UI elements
     */
    private EditText searchTermEditText;
    private Button searchButton;

    /**
     * onCreate method is called when the activity is created.
     * Sets the view, initializes UI elements, loads saved search term from SharedPreferences,
     * and sets onClickListener for the search button.
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_york);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchTermEditText = findViewById(R.id.search_term_edit_text);
        searchButton = findViewById(R.id.search_button);

        /**
         * Load the saved search term from SharedPreferences
         */
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedSearchTerm = sharedPreferences.getString(SEARCH_TERM_KEY, "");
        searchTermEditText.setText(savedSearchTerm);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = searchTermEditText.getText().toString();

                if (!searchTerm.isEmpty()) {
                    /**
                     * Save searchTerm to SharedPreferences
                     */
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SEARCH_TERM_KEY, searchTerm);
                    editor.apply();

                    /**
                     * Start the SearchResultActivity
                     */
                    Intent intent = new Intent(NewYorkActivity.this, ArticleSearchActivity.class);
                    intent.putExtra(SEARCH_TERM_KEY, searchTerm);
                    startActivity(intent);
                } else {
                    /**
                     * Show a Toast message for an empty search term
                     */
                    Toast.makeText(NewYorkActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    /**
     * Inflate the menu for the toolbar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handle toolbar menu item selection.
     * Show help dialog when the help menu item is selected.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Display a help dialog containing information about the app and how to use it.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_title)
                .setMessage(R.string.help_message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}