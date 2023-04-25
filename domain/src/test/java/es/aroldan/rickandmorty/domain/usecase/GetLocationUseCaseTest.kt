package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.model.Location
import es.aroldan.rickandmorty.domain.repository.LocationRepositoryContract
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Test

class GetLocationUseCaseTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var getLocationUseCaseTested: GetLocationUseCase

    @RelaxedMockK private lateinit var locationRepository: LocationRepositoryContract

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getLocationUseCaseTested = GetLocationUseCase(
            locationRepository = locationRepository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `when get characters page then fetch characters page from repository`() {
        val location = Location(name = "Location", dimension = "unknown")

        coEvery { locationRepository.fetchLocation(any()) } answers {
            flowOf(DataResult.Success(
                location
            ))
        }

        runBlocking {
            getLocationUseCaseTested(1)

            coVerify(exactly = 1) {
                locationRepository.fetchLocation(any())
            }
        }
    }
}