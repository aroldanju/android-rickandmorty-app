package es.aroldan.rickandmorty.presentation.screen.list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.usecase.GetCharactersPageUseCase
import es.aroldan.rickandmorty.domain.usecase.GetFavouriteCharactersUseCase
import es.aroldan.rickandmorty.presentation.mapper.CharacterViewMapper
import es.aroldan.rickandmorty.presentation.model.CharacterView
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.model.ScreenNavigation
import es.aroldan.rickandmorty.presentation.util.Constant
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersPageUseCase: GetCharactersPageUseCase,
    private val getFavouriteCharactersUseCase: GetFavouriteCharactersUseCase,
    private val characterViewMapper: CharacterViewMapper
): ViewModel() {

    private val pages: HashMap<Int, List<CharacterView>> = hashMapOf()
    private var showFilter: Boolean = false

    private val _currentPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private val _characters: MutableStateFlow<List<CharacterView>> = MutableStateFlow(listOf())
    val characters: StateFlow<List<CharacterView>> = _characters

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var page: Int = 1

    fun onStarted() {
        getCharactersPage(page)
    }

    private fun getCharacters(): List<CharacterView> {
        val characters = mutableListOf<CharacterView>()
        pages.map { characters.addAll(it.value) }
        return characters.toList()
    }

    private fun getCharactersPage(page: Int) = viewModelScope.launch {

        _isLoading.value = true

        getCharactersPageUseCase(page).onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    if (!showFilter) {
                        val charactersView = characterViewMapper.map(result.data)
                        pages[page] = charactersView
                        _characters.value = getCharacters()//charactersView
                    }

                    _isLoading.value = false
                }
                is DataResult.Error -> {
                    eventChannel.send(Event.ShowError(result.error))

                    _isLoading.value = false
                }
            }
        }.collect()
    }

    fun characterClicked(characterView: CharacterView) = viewModelScope.launch {
        eventChannel.send(Event.Navigate(ScreenNavigation.Navigate("${Constant.Deeplink.Path.CHARACTERS}/${characterView.id}")))
    }

    fun loadNextPage() {
        if (!showFilter && !_isLoading.value) {
            page += 1
            getCharactersPage(page)
        }
    }

    fun toggleFavouriteFilter() {
        showFilter = !showFilter

        if (showFilter) {
            getFavouriteCharacters()
        }
        else {
            _characters.value = getCharacters()
        }
    }

    private fun getFavouriteCharacters() = viewModelScope.launch {
        getFavouriteCharactersUseCase().onEach { result ->
            when (result) {
                is DataResult.Success -> {
                    _characters.value = characterViewMapper.map(result.data)

                    _isLoading.value = false
                }
                is DataResult.Error -> {
                    eventChannel.send(Event.ShowError(result.error))

                    _isLoading.value = false
                }
            }
        }.collect()
    }

    fun updateCurrentPosition(position: Int) {
        _currentPosition.value = position
    }
}
