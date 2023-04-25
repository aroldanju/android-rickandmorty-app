package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
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

class GetFavouriteCharactersUseCaseTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var getFavouriteCharactersUseCaseTested: GetFavouriteCharactersUseCase

    @RelaxedMockK private lateinit var characterRepository: CharacterRepositoryContract

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getFavouriteCharactersUseCaseTested = GetFavouriteCharactersUseCase(
            characterRepository = characterRepository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `when get favourite characters then fetch favourite characters from repository`() {
        val characters: List<Character> = listOf(
            Character(id = 0, name = "Name #0", avatar = null, locationId = null, isFavourite = true, status = CharacterStatus.ALIVE),
            Character(id = 1, name = "Name #1", avatar = "avatar-1", locationId = null, isFavourite = true, status = CharacterStatus.ALIVE),
            Character(id = 2, name = "Name #2", avatar = "avatar-2", locationId = 0, isFavourite = true, status = CharacterStatus.ALIVE),
        )

        coEvery { characterRepository.fetchFavouriteCharacters() } answers {
            flowOf(DataResult.Success(
                characters
            ))
        }

        runBlocking {
            getFavouriteCharactersUseCaseTested()

            coVerify(exactly = 1) {
                characterRepository.fetchFavouriteCharacters()
            }
        }
    }
}