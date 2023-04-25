package es.aroldan.rickandmorty.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity

@Dao
interface LocationsDao {

    @Query("SELECT * FROM locations WHERE location_id=:id")
    fun getLocationById(id: Int): LocationLocalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationLocalEntity)
}
