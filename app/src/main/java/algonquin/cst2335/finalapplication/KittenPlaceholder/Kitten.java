package algonquin.cst2335.finalapplication.KittenPlaceholder;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Kitten {

    @ColumnInfo(name="width")
    private int width;

    @ColumnInfo(name="height")
    private int height;

    @ColumnInfo(name="dateTime")
    private String dateTime;

    @ColumnInfo(name="fileName")
    private String fileName;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    Kitten(int width, int height, String fileName, String dateTime){
        this.fileName = fileName;
        this.width = width;
        this.height = height;
        this.dateTime = dateTime;
    }

    public String getDateTime(){
        return dateTime;
    }
    public String getFileName() {return fileName;}
    public int getWidth(){
        return width;
    }
    public int getHeight(){return height;}
}
