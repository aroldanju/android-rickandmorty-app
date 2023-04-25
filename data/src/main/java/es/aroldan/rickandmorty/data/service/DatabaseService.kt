package es.aroldan.rickandmorty.data.service

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.aroldan.rickandmorty.data.datasource.local.converter.DataConverter
import es.aroldan.rickandmorty.data.datasource.local.dao.CharactersDao
import es.aroldan.rickandmorty.data.datasource.local.dao.LocationsDao
import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity

@Database(
    entities = [CharacterLocalEntity::class, LocationLocalEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class DatabaseService : RoomDatabase() {

    abstract fun getCharactersDao(): CharactersDao

    abstract fun getLocationsDao(): LocationsDao
}