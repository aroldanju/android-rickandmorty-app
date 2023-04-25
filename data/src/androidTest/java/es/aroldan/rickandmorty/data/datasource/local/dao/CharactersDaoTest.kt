package es.aroldan.rickandmorty.data.datasource.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.service.DatabaseService
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharactersDaoTest {

    private lateinit var database: DatabaseService
    private lateinit var charactersDao: CharactersDao

    private fun provideLocation(id: Int = 0, name: String = "Location", url: String = "location/0", dimension: String = "unknown"): LocationLocalEntity =
        LocationLocalEntity(id = id, name = name, url = url, dimension = dimension)

    private fun provideCharacter(id: Int = 0, name: String = "Name", url: String = "url/0", location: LocationLocalEntity = provideLocation(), image: String = "image/0", page: Int = 1, isFavorite: Boolean = false): CharacterLocalEntity =
        CharacterLocalEntity(
            id = id, name = name, url = url, location = location,
            image = image, page = page, isFavorite = isFavorite
        )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, DatabaseService::class.java).build()
        charactersDao = database.getCharactersDao()
    }

    @After
    fun dispose() {
        database.close()
    }

    @Test
    fun givenCharacter_whenGetCharacter_thenFetchCharacter() {
        // Given
        val character = provideCharacter(id = 100, name = "Name #100", url = "url/100", image = "image/1", location = provideLocation(id = 1, url = "location/1"), page = 2)
        charactersDao.insertCharacter(character)

        val characterById = charactersDao.getCharacterById(100)

        Assert.assertEquals(
            100,
            characterById?.id
        )
    }

    @Test
    fun givenCharactersPage_whenGetCharactersPage_thenFetchCharactersPage() {
        // Given
        val characters = listOf(
            provideCharacter(id = 1, name = "Name #1", url = "url/1", image = "image/1", location = provideLocation(id = 1, url = "location/1"), page = 1),
            provideCharacter(id = 2, name = "Name #2", url = "url/2", image = "image/2", location = provideLocation(id = 1, url = "location/1"), page = 1),
            provideCharacter(id = 3, name = "Name #3", url = "url/3", image = "image/3", location = provideLocation(id = 1, url = "location/1"), page = 1),
            provideCharacter(id = 4, name = "Name #4", url = "url/4", image = "image/4", location = provideLocation(id = 2, url = "location/2"), page = 1)
        )

        for (character in characters) {
            charactersDao.insertCharacter(character)
        }

        val charactersPage = charactersDao.getCharactersPage(1)

        Assert.assertEquals(
            listOf("Name #1", "Name #2", "Name #3", "Name #4"),
            charactersPage.map { it.name }.toList()
        )
    }
}