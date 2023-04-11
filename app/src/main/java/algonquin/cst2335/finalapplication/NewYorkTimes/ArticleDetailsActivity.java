package algonquin.cst2335.finalapplication.NewYorkTimes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import algonquin.cst2335.finalapplication.R;


/**
 * ArticleDetailsActivity class displays the details of a selected article,
 * including its headline, URL, and publication date.
 */
public class ArticleDetailsActivity extends AppCompatActivity {
    /**
     * Constant for the intent extra key
     */
    public static final String ARTICLE_EXTRA = "article";
    /**
     * UI elements
     */
    private AppDatabase db;
    private ArticleDao articleDao;
    private TextView headlineTextView;
    private TextView urlTextView;
    private TextView publicationDateTextView;
    private ImageView favoriteIcon;
    private Article article;

    /**
     * onCreate method is called when the activity is created.
     * Sets the view, initializes UI elements, and populates the views with
     * the details of the selected article.
     *
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        headlineTextView = findViewById(R.id.headline);
        urlTextView = findViewById(R.id.url);
        publicationDateTextView = findViewById(R.id.publication_date);
        favoriteIcon = findViewById(R.id.favorite_icon);

        Intent intent = getIntent();
        WebView webView = findViewById(R.id.web_view);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "article_database").build();
        articleDao = db.articleDao();

        // Retrieve the data from the intent
        String headline = intent.getStringExtra("headline");
        String webUrl = intent.getStringExtra("webUrl");
        String pubDate = intent.getStringExtra("pubDate");
        article = (Article) intent.getSerializableExtra(ARTICLE_EXTRA);

        headlineTextView.setText(headline);
        urlTextView.setText(webUrl);
        publicationDateTextView.setText(pubDate);
        if (webUrl != null) {
            webView.loadUrl(webUrl);
        }

        updateFavoriteIcon();

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite(article)) {
                    removeFromFavorites(article);
                } else {
                    addToFavorites(article);
                }
                updateFavoriteIcon();
            }
        });
    }

    private void updateFavoriteIcon() {
        if (isFavorite(article)) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void addToFavorites(Article article) {
        if (article != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    articleDao.addArticle(article);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateFavoriteIcon();
                        }
                    });
                }
            }).start();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ArticleDetailsActivity.this, "Error: Unable to add article to favorites", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void removeFromFavorites(Article article) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                articleDao.deleteArticle(article);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateFavoriteIcon();
                    }
                });
            }
        }).start();
    }

    private boolean isFavorite(Article article) {
        try {
            Article existingArticle = articleDao.getArticleById(article.getId());
            return existingArticle != null;
        } catch (Exception e) {
            return false;
        }
    }
}

