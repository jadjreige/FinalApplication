package algonquin.cst2335.finalapplication.WeatherStack.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @author Imandeep Singh SIdhu
 * This class establishes connection with room database
 */
@Database(entities = {WeatherStack.class}, version= 1)
public abstract class WeatherStackDatabase extends RoomDatabase {

    public abstract WeatherStackDAO weatherStackDAO();
    private static WeatherStackDatabase connection;

    public static synchronized WeatherStackDatabase getInstance(Context context) {
        if(connection == null) {
            connection = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherStackDatabase.class, "weather-stack")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return connection;
    }

}
