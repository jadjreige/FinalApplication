package algonquin.cst2335.finalapplication.NewYorkTimes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addArticle(Article article);

    @Delete
    void deleteArticle(Article article);

    @Query("SELECT * FROM favorite_articles WHERE id = :id")
    Article getArticleById(String id);

    @Query("SELECT * FROM favorite_articles")
    List<Article> getAllArticles();
}
