package es.aroldan.rickandmorty.presentation.screen.list

import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus
import es.aroldan.rickandmorty.domain.model.DefinedError
import es.aroldan.rickandmorty.domain.usecase.GetCharactersPageUseCase
import es.aroldan.rickandmorty.domain.usecase.GetFavouriteCharactersUseCase
import es.aroldan.rickandmorty.presentation.mapper.CharacterViewMapper
import es.aroldan.rickandmorty.presentation.model.CharacterView
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.model.ScreenNavigation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    private var dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private lateinit var characterListViewModelTested: CharacterListViewModel

    @RelaxedMockK private lateinit var getCharactersPageUseCase: GetCharactersPageUseCase
    @RelaxedMockK private lateinit var getFavouriteCharactersUseCase: GetFavouriteCharactersUseCase
    @RelaxedMockK private lateinit var characterViewMapper: CharacterViewMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(dispatcher)

        characterListViewModelTested = CharacterListViewModel(
            getCharactersPageUseCase = getCharactersPageUseCase,
            getFavouriteCharactersUseCase = getFavouriteCharactersUseCase,
            characterViewMapper = characterViewMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when start view model then get characters page`() = runTest {
        characterListViewModelTested.onStarted()

        coVerify(exactly = 1) {
            getCharactersPageUseCase(any())
        }
    }

    @Test
    fun `when get characters page then set loading state to true`() = runTest {
        characterListViewModelTested.onStarted()

        Assert.assertEquals(
            true,
            characterListViewModelTested.isLoading.value
        )
    }

    @Test
    fun `when get characters page getting success response then set loading state to false`() = runTest {
        coEvery { getCharactersPageUseCase(any()) } answers {
            flowOf(DataResult.Success(listOf(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE))))
        }
        every { characterViewMapper.map(any() as List<Character>) } answers {
            listOf(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = 0, statusIndicatorColor = 0))
        }

        characterListViewModelTested.onStarted()

        Assert.assertEquals(
            false,
            characterListViewModelTested.isLoading.value
        )
    }

    @Test
    fun `when get characters page getting failure response then set loading state to false`() = runTest {
        coEvery { getCharactersPageUseCase(any()) } answers {
            flowOf(DataResult.Error(DefinedError.Unknown(Throwable())))
        }

        characterListViewModelTested.onStarted()

        Assert.assertEquals(
            false,
            characterListViewModelTested.isLoading.value
        )
    }

    @Test
    fun `when get characters page getting success response then update characters`() = runTest {
        coEvery { getCharactersPageUseCase(any()) } answers {
            flowOf(DataResult.Success(listOf(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE))))
        }
        every { characterViewMapper.map(any() as List<Character>) } answers {
            listOf(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = 0, statusIndicatorColor = 0))
        }

        characterListViewModelTested.onStarted()

        Assert.assertEquals(
            listOf(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = 0, statusIndicatorColor = 0)),
            characterListViewModelTested.characters.value
        )
    }

    @Test
    fun `when get characters page getting failure response then update error`() = runTest {
        val error = DefinedError.Unknown(Throwable())

        coEvery { getCharactersPageUseCase(any()) } answers {
            flowOf(DataResult.Error(error))
        }

        characterListViewModelTested.onStarted()

        Assert.assertEquals(
            Event.ShowError(error),
            characterListViewModelTested.events.first()
        )
    }

    @Test
    fun `when character clicked then send navigate event`() = runTest {
        characterListViewModelTested.characterClicked(
            CharacterView(id = 6540, name = "Name", avatar = "avatar", locationId = null, isFavourite = false, status = 0, statusIndicatorColor = 0)
        )

        Assert.assertEquals(
            Event.Navigate(ScreenNavigation.Navigate("characters/6540")),
            characterListViewModelTested.events.first()
        )
    }

    @Test
    fun `when call to update scroll position then update scroll position`() = runTest {
        val random = Random(System.currentTimeMillis()).nextInt(100, 500)
        characterListViewModelTested.updateCurrentPosition(random)

        Assert.assertEquals(
            random,
            characterListViewModelTested.currentPosition.value
        )
    }

    @Test
    fun `given favourite filter enabled when get characters page getting success response then ignore`() = runTest {
        coEvery { getCharactersPageUseCase(any()) } answers {
            flowOf(DataResult.Success(listOf(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE))))
        }
        every { characterViewMapper.map(any() as List<Character>) } answers {
            listOf(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = 0, statusIndicatorColor = 0))
        }

        // Given
        characterListViewModelTested.toggleFavouriteFilter()

        // When
        characterListViewModelTested.onStarted()

        Assert.assertEquals(
            emptyList<CharacterView>(),
            characterListViewModelTested.characters.value
        )
    }

    @Test
    fun `when enable favourite filter then call get favourite characters`() = runTest {
        coEvery { getFavouriteCharactersUseCase() } answers {
            flowOf(DataResult.Success(listOf(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE))))
        }
        every { characterViewMapper.map(any() as List<Character>) } answers {
            listOf(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = true, statusIndicatorColor = 0, status = 0))
        }

        characterListViewModelTested.toggleFavouriteFilter()

        Assert.assertEquals(
            listOf(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = true, status = 0, statusIndicatorColor = 0)),
            characterListViewModelTested.characters.value
        )
    }

    @Test
    fun `when disable favourite filter then call get all characters`() = runTest {
        coEvery { getCharactersPageUseCase(any()) } answers {
            flowOf(DataResult.Success(listOf(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE))))
        }

        coEvery { getFavouriteCharactersUseCase() } answers {
            flowOf(DataResult.Success(listOf(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE))))
        }
        every { characterViewMapper.map(any() as List<Character>) } answers {
            listOf(CharacterView(id = 0, name = "Fetched from page", avatar = "Avatar", locationId = null, isFavourite = true, status = 0, statusIndicatorColor = 0))
        }

        characterListViewModelTested.onStarted()

        characterListViewModelTested.toggleFavouriteFilter()    // Enable filter
        characterListViewModelTested.toggleFavouriteFilter()    // Disable filter

        Assert.assertEquals(
            listOf(CharacterView(id = 0, name = "Fetched from page", avatar = "Avatar", locationId = null, isFavourite = true, status = 0, statusIndicatorColor = 0)),
            characterListViewModelTested.characters.value
        )
    }
}
