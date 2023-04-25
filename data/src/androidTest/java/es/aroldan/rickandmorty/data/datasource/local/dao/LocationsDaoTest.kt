package es.aroldan.rickandmorty.data.datasource.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.service.DatabaseService
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationsDaoTest {

    private lateinit var database: DatabaseService
    private lateinit var locationsDao: LocationsDao

    private fun provideLocation(id: Int = 0, name: String = "Location", url: String = "location/0", dimension: String = "unknown"): LocationLocalEntity =
        LocationLocalEntity(id = id, name = name, url = url, dimension = dimension)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, DatabaseService::class.java).build()
        locationsDao = database.getLocationsDao()
    }

    @After
    fun dispose() {
        database.close()
    }

    @Test
    fun givenLocation_whenGetLocation_thenFetchLocation() {
        // Given
        val location = provideLocation(id = 100, name = "Name #100", url = "url/100")
        locationsDao.insertLocation(location)

        val locationById = locationsDao.getLocationById(100)

        Assert.assertEquals(
            provideLocation(id = 100, name = "Name #100", url = "url/100"),
            locationById
        )
    }
}