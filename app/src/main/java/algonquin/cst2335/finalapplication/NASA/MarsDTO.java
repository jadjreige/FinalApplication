package algonquin.cst2335.finalapplication.NASA;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MarsDTO {

    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "name")
    private String name;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    public MarsDTO() {
    }

    public MarsDTO (String image, String name) {
        this.imageUrl = image;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
