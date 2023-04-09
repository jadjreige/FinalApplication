package algonquin.cst2335.finalapplication.NASA;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {MarsDTO.class}, version = 1)
public abstract class PhotoDatabase extends RoomDatabase {

    public abstract MarsDAO mDAO();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }


    @Override
    public void clearAllTables() {

    }
}
