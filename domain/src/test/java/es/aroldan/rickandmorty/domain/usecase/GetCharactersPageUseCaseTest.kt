package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.model.Character
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

class GetCharactersPageUseCaseTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var getCharactersPageUseCaseTested: GetCharactersPageUseCase

    @RelaxedMockK private lateinit var characterRepository: CharacterRepositoryContract

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getCharactersPageUseCaseTested = GetCharactersPageUseCase(
            characterRepository = characterRepository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `when get characters page then fetch characters page from repository`() {
        val charactersPage: List<Character> = listOf(
            Character(id = 0, name = "Name #0", avatar = null, locationId = null, isFavourite = false),
            Character(id = 1, name = "Name #1", avatar = "avatar-1", locationId = null, isFavourite = false),
            Character(id = 2, name = "Name #2", avatar = "avatar-2", locationId = 0, isFavourite = true),
        )

        coEvery { characterRepository.fetchCharacters(any()) } answers {
            flowOf(DataResult.Success(
                charactersPage
            ))
        }

        runBlocking {
            getCharactersPageUseCaseTested(1)

            coVerify(exactly = 1) {
                characterRepository.fetchCharacters(any())
            }
        }
    }
}