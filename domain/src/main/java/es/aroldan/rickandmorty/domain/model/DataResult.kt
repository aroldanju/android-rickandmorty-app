package es.aroldan.rickandmorty.domain.model

sealed class DataResult<out T: Any> {
    data class Success<out T: Any>(val data: T): DataResult<T>()
    data class Error(val error: DefinedError): DataResult<Nothing>()
}