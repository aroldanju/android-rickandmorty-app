package es.aroldan.rickandmorty.data.datasource.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterLocalEntity(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("page") val page: Int? = null,
    @ColumnInfo("is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("url") val url: String,
    @Embedded val location: LocationLocalEntity,
    @ColumnInfo("image") val image: String?,
    @ColumnInfo("status") val status: String = "unknown"
)
