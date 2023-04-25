package es.aroldan.rickandmorty.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.model.DefinedError
import es.aroldan.rickandmorty.domain.model.Location
import es.aroldan.rickandmorty.domain.usecase.GetCharacterUseCase
import es.aroldan.rickandmorty.domain.usecase.GetLocationUseCase
import es.aroldan.rickandmorty.domain.usecase.ToggleFavouriteCharacterUseCase
import es.aroldan.rickandmorty.presentation.mapper.CharacterViewMapper
import es.aroldan.rickandmorty.presentation.mapper.LocationViewMapper
import es.aroldan.rickandmorty.presentation.model.CharacterView
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.model.LocationView
import es.aroldan.rickandmorty.presentation.model.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {

    private var dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private lateinit var characterDetailViewModelTested: CharacterDetailViewModel

    @RelaxedMockK private lateinit var getCharacterUseCase: GetCharacterUseCase
    @RelaxedMockK private lateinit var getLocationUseCase: GetLocationUseCase
    @RelaxedMockK private lateinit var toggleFavouriteCharacterUseCase: ToggleFavouriteCharacterUseCase
    @RelaxedMockK private lateinit var characterViewMapper: CharacterViewMapper
    @RelaxedMockK private lateinit var locationViewMapper: LocationViewMapper
    @RelaxedMockK private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(dispatcher)

        // Mock character ID
        every { savedStateHandle.get<String>(any()) } returns "0"

        characterDetailViewModelTested = CharacterDetailViewModel(
            getCharacterUseCase = getCharacterUseCase,
            getLocationUseCase = getLocationUseCase,
            toggleFavouriteCharacterUseCase = toggleFavouriteCharacterUseCase,
            characterViewMapper = characterViewMapper,
            locationViewMapper = locationViewMapper,
            savedStateHandle = savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when start view model then get character`() = runTest {
        characterDetailViewModelTested.started()

        coVerify(exactly = 1) {
            getCharacterUseCase(any())
        }
    }

    @Test
    fun `when get character getting success response then update character`() = runTest(dispatcher) {
        coEvery { getCharacterUseCase(any()) } answers {
            flowOf(DataResult.Success(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)))
        }
        every { characterViewMapper.map(any() as Character) } answers {
            CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)
        }

        val job = characterDetailViewModelTested.uiState.launchIn(this)

        characterDetailViewModelTested.started()

        Assert.assertEquals(
            UiState.Content<Pair<CharacterView, LocationView?>>(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false) to null),
            characterDetailViewModelTested.uiState.value
        )

        job.cancel()
    }

    @Test
    fun `when get location getting success response then update location`() = runTest(dispatcher) {
        coEvery { getCharacterUseCase(any()) } answers {
            flowOf(DataResult.Success(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)))
        }
        every { characterViewMapper.map(any() as Character) } answers {
            CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = 0, isFavourite = false)
        }

        coEvery { getLocationUseCase(any()) } answers {
            flowOf(DataResult.Success(Location(name = "Location", dimension = "unknown")))
        }
        every { locationViewMapper.map(any() as Location) } answers {
            LocationView(name = "Location", dimension = "unknown")
        }

        val job = characterDetailViewModelTested.uiState.launchIn(this)

        characterDetailViewModelTested.started()

        Assert.assertEquals(
            UiState.Content(
                Pair<CharacterView, LocationView?>(
                    CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = 0, isFavourite = false),
                    LocationView(name = "Location", dimension = "unknown"))),
            characterDetailViewModelTested.uiState.value
        )

        job.cancel()
    }

    @Test
    fun `when get character with null location id then ignore location`() = runTest(dispatcher) {
        coEvery { getCharacterUseCase(any()) } answers {
            flowOf(DataResult.Success(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)))
        }
        every { characterViewMapper.map(any() as Character) } answers {
            CharacterView(id = 0, name = "Name", avatar = "Avatar",
                locationId = null, // null location id
                isFavourite = false
            )
        }

        coEvery { getLocationUseCase(any()) } answers {
            flowOf(DataResult.Success(Location(name = "Location", dimension = "unknown")))
        }
        every { locationViewMapper.map(any() as Location) } answers {
            LocationView(name = "Location", dimension = "unknown")
        }

        val job = characterDetailViewModelTested.uiState.launchIn(this)

        characterDetailViewModelTested.started()

        Assert.assertEquals(
            UiState.Content(
                Pair<CharacterView, LocationView?>(
                    CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false),
                    null)),
            characterDetailViewModelTested.uiState.value
        )

        job.cancel()
    }

    @Test
    fun `when get character getting failure response then update error`() = runTest {
        val error = DefinedError.Unknown(Throwable())

        coEvery { getCharacterUseCase(any()) } answers {
            flowOf(DataResult.Error(error))
        }

        characterDetailViewModelTested.started()

        Assert.assertEquals(
            Event.ShowError(error),
            characterDetailViewModelTested.events.first()
        )
    }

    @Test
    fun `when get location getting failure response then update error`() = runTest {
        val error = DefinedError.Unknown(Throwable())

        coEvery { getCharacterUseCase(any()) } answers {
            flowOf(DataResult.Success(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)))
        }
        every { characterViewMapper.map(any() as Character) } answers {
            CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = 0, isFavourite = false)
        }

        coEvery { getLocationUseCase(any()) } answers {
            flowOf(DataResult.Error(error))
        }

        characterDetailViewModelTested.started()

        Assert.assertEquals(
            Event.ShowError(error),
            characterDetailViewModelTested.events.first()
        )
    }

    @Test
    fun `when toggle favourite status getting failure response then update error`() = runTest {
        val error = DefinedError.Unknown(Throwable())

        coEvery { toggleFavouriteCharacterUseCase(any(), any()) } answers {
            flowOf(DataResult.Error(error))
        }

        // Fill character
        coEvery { getCharacterUseCase(any()) } answers {
            flowOf(DataResult.Success(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)))
        }
        every { characterViewMapper.map(any() as Character) } answers {
            CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = true)
        }
        characterDetailViewModelTested.started()

        characterDetailViewModelTested.toggleFavourite()

        Assert.assertEquals(
            Event.ShowError(error),
            characterDetailViewModelTested.events.first()
        )
    }

    @Test
    fun `when toggle favourite status getting success response then update character`() = runTest(dispatcher) {
        coEvery { toggleFavouriteCharacterUseCase(any(), any()) } answers {
            flowOf(DataResult.Success(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)))
        }
        every { characterViewMapper.map(any() as Character) } answers {
            CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = true)
        }

        val job = characterDetailViewModelTested.uiState.launchIn(this)

        // Fill character
        coEvery { getCharacterUseCase(any()) } answers {
            flowOf(DataResult.Success(Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false)))
        }
        characterDetailViewModelTested.started()

        characterDetailViewModelTested.toggleFavourite()

        Assert.assertEquals(
            UiState.Content<Pair<CharacterView, LocationView?>>(CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = true) to null),
            characterDetailViewModelTested.uiState.value
        )

        job.cancel()
    }

}