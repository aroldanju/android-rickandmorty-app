package es.aroldan.rickandmorty.presentation.model

import es.aroldan.rickandmorty.domain.model.DefinedError

sealed interface Event {
    data class ShowError(val definedError: DefinedError): Event
    data class Navigate(val screenNavigation: ScreenNavigation): Event
}
