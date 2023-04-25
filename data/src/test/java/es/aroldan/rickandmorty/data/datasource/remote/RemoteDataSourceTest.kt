package es.aroldan.rickandmorty.data.datasource.remote

import es.aroldan.rickandmorty.data.repository.datasource.RemoteDataSourceContract
import es.aroldan.rickandmorty.data.service.ApiService
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RemoteDataSourceTest {

    private lateinit var remoteDataSourceTested: RemoteDataSourceContract

    @RelaxedMockK private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        remoteDataSourceTested = RemoteDataSource(
            apiService = apiService
        )
    }

    @Test
    fun `when fetch characters page then fetch characters page from api service`() {
        runBlocking {
            remoteDataSourceTested.fetchCharacters(1)

            coVerify {
                apiService.fetchCharacters(any())
            }
        }
    }

    @Test
    fun `when fetch location then fetch location from api service`() {
        runBlocking {
            remoteDataSourceTested.fetchLocation(1)

            coVerify {
                apiService.fetchLocation(any())
            }
        }
    }
}