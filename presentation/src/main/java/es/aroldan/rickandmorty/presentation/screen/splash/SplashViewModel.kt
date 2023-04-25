package es.aroldan.rickandmorty.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.model.ScreenNavigation
import es.aroldan.rickandmorty.presentation.util.Constant
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun animationFinished() = viewModelScope.launch {
        eventChannel.send(Event.Navigate(ScreenNavigation.Navigate(Constant.Deeplink.Path.CHARACTERS, true)))
    }
}
