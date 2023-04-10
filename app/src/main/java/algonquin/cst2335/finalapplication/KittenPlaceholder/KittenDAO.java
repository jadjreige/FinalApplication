package algonquin.cst2335.finalapplication.KittenPlaceholder;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface KittenDAO {
    @Insert
    public long insertKitten(Kitten k);

    @Query("Select * from Kitten")
    public List<Kitten> getAllKittens();


    @Delete
    void deleteKitten(Kitten k);
}
