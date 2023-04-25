package es.aroldan.rickandmorty.data.repository

import es.aroldan.rickandmorty.data.datasource.local.LocalDataSource
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.RemoteDataSource
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.data.mapper.LocationEntityMapper
import es.aroldan.rickandmorty.data.mapper.LocationLocalEntityMapper
import es.aroldan.rickandmorty.data.mapper.LocationSourceEntityMapper
import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.model.DefinedError
import es.aroldan.rickandmorty.domain.model.Location
import es.aroldan.rickandmorty.domain.repository.LocationRepositoryContract
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LocationRepositoryTest {

    private lateinit var locationRepositoryTested: LocationRepositoryContract

    @RelaxedMockK private lateinit var errorHandler: ErrorHandlerContract
    @RelaxedMockK private lateinit var remoteDataSource: RemoteDataSource
    @RelaxedMockK private lateinit var localDataSource: LocalDataSource
    @RelaxedMockK private lateinit var locationEntityMapper: LocationEntityMapper
    @RelaxedMockK private lateinit var locationLocalEntityMapper: LocationLocalEntityMapper
    @RelaxedMockK private lateinit var locationSourceEntityMapper: LocationSourceEntityMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        locationRepositoryTested = LocationRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            locationEntityMapper = locationEntityMapper,
            locationLocalEntityMapper = locationLocalEntityMapper,
            locationSourceEntityMapper = locationSourceEntityMapper,
            errorHandler = errorHandler,
        )
    }

    @Test
    fun `given local location when fetch location then fetch location from local data source`() {
        every { locationLocalEntityMapper.map(any() as LocationLocalEntity) } returns Location(name = "Name", dimension = "unknown")

        // Given
        coEvery { localDataSource.fetchLocation(any()) } returns LocationLocalEntity(id = 0, name = "Name", url = "Url", dimension = "unknown")

        runBlocking {
            val flow = locationRepositoryTested.fetchLocation(1)

            Assert.assertEquals(
                DataResult.Success(Location(name = "Name", dimension = "unknown")),
                flow.first()
            )
        }
    }

    @Test
    fun `when fetch location then store location to local data source`() {
        coEvery { localDataSource.fetchLocation(any()) } returns null

        every { locationEntityMapper.map(any() as LocationEntity) } returns Location(name = "Name", dimension = "unknown")

        coEvery { remoteDataSource.fetchLocation(any()) } returns LocationEntity(id = 0, name = "Name", url = "Url", dimension = "unknown")

        runBlocking {
            locationRepositoryTested.fetchLocation(1).first()

            coVerify(exactly = 1) {
                localDataSource.saveLocation(any())
            }
        }
    }

    @Test
    fun `when fetch location then fetch location from remote data source`() {
        coEvery { localDataSource.fetchLocation(any()) } returns null

        every { locationEntityMapper.map(any() as LocationEntity) } returns Location(name = "Name", dimension = "unknown")

        coEvery { remoteDataSource.fetchLocation(any()) } returns LocationEntity(id = 0, name = "Name", url = "Url", dimension = "unknown")

        runBlocking {
            val flow = locationRepositoryTested.fetchLocation(1)

            Assert.assertEquals(
                DataResult.Success(Location(name = "Name", dimension = "unknown")),
                flow.first()
            )
        }
    }

    @Test
    fun `given a local location when fetch location then fetch location from remote data source`() {
        // Given
        coEvery { localDataSource.fetchLocation(any()) } returns LocationLocalEntity(id = 0, name = "Local", url = "Url", dimension = "unknown")

        every { locationEntityMapper.map(any() as LocationEntity) } returns Location(name = "Name", dimension = "unknown")

        coEvery { remoteDataSource.fetchLocation(any()) } returns LocationEntity(id = 0, name = "Name", url = "Url", dimension = "unknown")

        runBlocking {
            val flow = locationRepositoryTested.fetchLocation(1)

            Assert.assertEquals(
                DataResult.Success(Location(name = "Name", dimension = "unknown")),
                flow.drop(1).first()
            )
        }
    }

    @Test
    fun `get error when fetching location from api`() {
        val error: DefinedError = DefinedError.Unknown(Throwable())

        coEvery { errorHandler.handleError(any()) } answers {
            error
        }

        coEvery {
            remoteDataSource.fetchLocation(any())
        } throws Throwable()

        runBlocking {
            val flow = locationRepositoryTested.fetchLocation(1)

            Assert.assertEquals(
                DataResult.Error(error),
                flow.first()
            )
        }
    }
}