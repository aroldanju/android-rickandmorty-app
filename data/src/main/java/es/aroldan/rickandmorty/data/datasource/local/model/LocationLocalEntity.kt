package es.aroldan.rickandmorty.data.datasource.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationLocalEntity(
    @PrimaryKey @ColumnInfo(name = "location_id") val id: Int? = null,
    @ColumnInfo(name = "location_name") val name: String,
    @ColumnInfo(name = "location_url") val url: String,
    @ColumnInfo(name = "location_dimension") val dimension: String
)
