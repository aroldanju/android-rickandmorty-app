package es.aroldan.rickandmorty.presentation.model

import es.aroldan.rickandmorty.domain.model.DefinedError

sealed interface UiState<out T> {
    object Loading: UiState<Nothing>
    object Empty: UiState<Nothing>
    data class Error(val definedError: DefinedError): UiState<Nothing>
    data class Content<out T>(val data: T): UiState<T>
}
