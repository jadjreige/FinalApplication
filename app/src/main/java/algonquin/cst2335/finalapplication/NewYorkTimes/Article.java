package algonquin.cst2335.finalapplication.NewYorkTimes;

import java.io.Serializable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_articles")
public class Article implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String headline;
    private String pubDate;
    private String webUrl;

    public Article(String headline, String pubDate, String webUrl, String id) {
        this.id = id;
        this.headline = headline;
        this.pubDate = pubDate;
        this.webUrl = webUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}

