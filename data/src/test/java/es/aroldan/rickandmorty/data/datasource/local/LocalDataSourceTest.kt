package es.aroldan.rickandmorty.data.datasource.local

import es.aroldan.rickandmorty.data.datasource.local.dao.CharactersDao
import es.aroldan.rickandmorty.data.datasource.local.dao.LocationsDao
import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.repository.datasource.LocalDataSourceContract
import es.aroldan.rickandmorty.data.service.DatabaseService
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LocalDataSourceTest {

    private lateinit var localDataSourceTested: LocalDataSourceContract

    @RelaxedMockK private lateinit var databaseService: DatabaseService
    @RelaxedMockK private lateinit var charactersDao: CharactersDao
    @RelaxedMockK private lateinit var locationsDao: LocationsDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        localDataSourceTested = LocalDataSource(
            databaseService = databaseService
        )
    }

    @Test
    fun `when fetch characters page then fetch characters page from database service`() {
        every { databaseService.getCharactersDao() } returns charactersDao

        runBlocking {
            localDataSourceTested.getCharactersPage(1)

            coVerify(exactly = 1) {
                charactersDao.getCharactersPage(any())
            }
        }
    }

    @Test
    fun `when fetch character then fetch character from database service`() {
        every { databaseService.getCharactersDao() } returns charactersDao

        runBlocking {
            localDataSourceTested.getCharacter(1)

            coVerify(exactly = 1) {
                charactersDao.getCharacterById(any())
            }
        }
    }

    @Test
    fun `when save character then save character to database service`() {
        every { databaseService.getCharactersDao() } returns charactersDao

        runBlocking {
            localDataSourceTested.saveCharacter(
                CharacterLocalEntity(id = 1, page = 1, isFavorite = false, name = "Name", url = "url",
                    location = LocationLocalEntity(id = 0, name = "Location", url = "Url", dimension = "unknown"),
                    image = "Image"
                )
            )

            coVerify(exactly = 1) {
                charactersDao.insertCharacter(any())
            }
        }
    }


    @Test
    fun `when fetch location then fetch location from database service`() {
        every { databaseService.getLocationsDao() } returns locationsDao

        runBlocking {
            localDataSourceTested.fetchLocation(1)

            coVerify(exactly = 1) {
                locationsDao.getLocationById(any())
            }
        }
    }

    @Test
    fun `when save location then save location to database service`() {
        every { databaseService.getLocationsDao() } returns locationsDao

        runBlocking {
            localDataSourceTested.saveLocation(
                LocationLocalEntity(id = 0, name = "Location", url = "Url", dimension = "unknown")
            )

            coVerify(exactly = 1) {
                locationsDao.insertLocation(any())
            }
        }
    }
}