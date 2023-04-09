package algonquin.cst2335.finalapplication.WeatherStack.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @author Imandeep Singh Sidhu
 * version 1
 * This DAO interface represents
 */
@Dao
public interface WeatherStackDAO {

    @Insert
    public long insertWeatherStackRecord(WeatherStack cocktail);

    @Query("Select * from WeatherStack")
    public List<WeatherStack> getAllWeatherStackRecords();

    @Delete
    void deleteWeatherStackRecord(WeatherStack cocktail);

}
