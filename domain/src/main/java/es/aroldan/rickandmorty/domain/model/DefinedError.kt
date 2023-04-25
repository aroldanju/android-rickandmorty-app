package es.aroldan.rickandmorty.domain.model

sealed interface DefinedError {
    object NoInternetConnection: DefinedError
    data class Unknown(val exception: Throwable): DefinedError
}