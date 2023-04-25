package es.aroldan.rickandmorty.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.usecase.GetCharacterUseCase
import es.aroldan.rickandmorty.domain.usecase.GetLocationUseCase
import es.aroldan.rickandmorty.domain.usecase.ToggleFavouriteCharacterUseCase
import es.aroldan.rickandmorty.presentation.mapper.CharacterViewMapper
import es.aroldan.rickandmorty.presentation.mapper.LocationViewMapper
import es.aroldan.rickandmorty.presentation.model.CharacterView
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.model.LocationView
import es.aroldan.rickandmorty.presentation.model.UiState
import es.aroldan.rickandmorty.presentation.util.Constant
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCharacterUseCase: GetCharacterUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val toggleFavouriteCharacterUseCase: ToggleFavouriteCharacterUseCase,
    private val characterViewMapper: CharacterViewMapper,
    private val locationViewMapper: LocationViewMapper
): ViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private val characterId: String = savedStateHandle[Constant.Deeplink.Argument.CHARACTER_ID] ?: "0"

    private val _character: MutableStateFlow<CharacterView?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _location: MutableStateFlow<LocationView?> = MutableStateFlow(null)

    val uiState: StateFlow<UiState<Pair<CharacterView, LocationView?>>> = combine(
        _character, _location, _isLoading
    ) { character, location, isLoading ->
        if (isLoading) {
            UiState.Loading
        }
        else {
            if (character != null) {
                UiState.Content(character to location)
            }
            else {
                UiState.Loading
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun started() {
        getCharacter(characterId.toInt()) { characterView ->
            characterView.locationId?.let {
                getLocation(it)
            }
        }
    }

    private fun getCharacter(characterId: Int, handler: (characterView: CharacterView) -> Unit) = viewModelScope.launch {
        getCharacterUseCase(characterId).onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    val characterView = characterViewMapper.map(result.data)

                    _character.value = characterView
                    handler(characterView)

                    _isLoading.value = false
                }
                is DataResult.Error -> {
                    eventChannel.send(Event.ShowError(result.error))

                    _isLoading.value = false
                }
            }
        }.collect()
    }

    private fun getLocation(locationId: Int) = viewModelScope.launch {
        getLocationUseCase(locationId).onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    _isLoading.value = false
                    _location.value = locationViewMapper.map(result.data)
                }
                is DataResult.Error -> {
                    _isLoading.value = false
                    eventChannel.send(Event.ShowError(result.error))
                }
            }
        }.collect()
    }

    fun toggleFavourite() {
        _character.value?.let {
            toggleFavouriteCharacter(it.id, it.isFavourite)
        }
    }

    private fun toggleFavouriteCharacter(characterId: Int, isFavourite: Boolean) = viewModelScope.launch {
        toggleFavouriteCharacterUseCase(characterId, isFavourite).onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    _character.value = characterViewMapper.map(result.data)
                }
                is DataResult.Error -> {
                    eventChannel.send(Event.ShowError(result.error))
                }
            }
        }.collect()
    }


}