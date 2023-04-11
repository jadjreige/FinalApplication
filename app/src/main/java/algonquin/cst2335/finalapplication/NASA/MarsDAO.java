package algonquin.cst2335.finalapplication.NASA;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO interface.
 */
@Dao
public interface MarsDAO {

    @Insert
    long insertPhoto(MarsDTO p);

    @Query("SELECT * FROM MarsDTO")
    List<MarsDTO> getAllPhotos();

    @Delete
    void deletePhoto(MarsDTO p);
}
