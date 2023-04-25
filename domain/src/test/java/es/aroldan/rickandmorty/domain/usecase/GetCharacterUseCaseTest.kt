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

class GetCharacterUseCaseTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private lateinit var getCharacterUseCaseTested: GetCharacterUseCase

    @RelaxedMockK private lateinit var characterRepository: CharacterRepositoryContract

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        getCharacterUseCaseTested = GetCharacterUseCase(
            characterRepository = characterRepository,
            dispatcher = dispatcher
        )
    }

    @Test
    fun `when get characters page then fetch characters page from repository`() {
        val character = Character(id = 0, name = "Name #0", avatar = null, locationId = 1, isFavourite = false, status = CharacterStatus.ALIVE)

        coEvery { characterRepository.fetchCharacter(any()) } answers {
            flowOf(DataResult.Success(
                character
            ))
        }

        runBlocking {
            getCharacterUseCaseTested(1)

            coVerify(exactly = 1) {
                characterRepository.fetchCharacter(any())
            }
        }
    }
}