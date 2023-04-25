package es.aroldan.rickandmorty.data.datasource.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity

class DataConverter {

    private inline fun <reified T> fromList(list: List<T>?): String {
        var finalList: List<T> = listOf()
        list?.let {
            finalList = list
        }

        return Gson().toJson(finalList, object : TypeToken<List<T>?>() {}.type)
    }

    private inline fun <reified T> toList(list: String): List<T> {
        return Gson().fromJson(list, object : TypeToken<List<T>>() {}.type)
    }

    @TypeConverter
    fun fromLocation(location: LocationEntity?): String =
        Gson().toJson(location)

    @TypeConverter
    fun toLocation(location: String): LocationEntity =
        Gson().fromJson(location, LocationEntity::class.java)

    @TypeConverter
    fun fromIntList(list: List<Int>): String =
        fromList<Int>(list)


    @TypeConverter
    fun toIntList(list: String): List<Int> =
        toList<Int>(list)


    @TypeConverter
    fun fromStringList(list: List<String>): String =
        fromList<String>(list)


    @TypeConverter
    fun toStringList(list: String): List<String> =
        toList<String>(list)

}