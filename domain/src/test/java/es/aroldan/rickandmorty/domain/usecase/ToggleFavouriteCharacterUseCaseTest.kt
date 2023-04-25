package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
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

class ToggleFavouriteCharacterUseCaseTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var toggleFavouriteCharacterUseCaseTested: ToggleFavouriteCharacterUseCase

    @RelaxedMockK private lateinit var characterRepository: CharacterRepositoryContract

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        toggleFavouriteCharacterUseCaseTested = ToggleFavouriteCharacterUseCase(
            characterRepository = characterRepository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `given a fav status when set favourite then call set favourite from repository with opposite status`() {
        val character = Character(id = 0, name = "Name", avatar = null, locationId = null, isFavourite = false)

        coEvery { characterRepository.setFavouriteCharacter(any(), any()) } answers {
            flowOf(DataResult.Success(
                character
            ))
        }

        runBlocking {
            toggleFavouriteCharacterUseCaseTested(1, true)

            coVerify(exactly = 1) {
                characterRepository.setFavouriteCharacter(any(), false)
            }
        }
    }
}