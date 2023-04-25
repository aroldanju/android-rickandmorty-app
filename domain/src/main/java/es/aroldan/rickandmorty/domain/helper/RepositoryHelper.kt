package es.aroldan.rickandmorty.domain.helper

import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.model.DataResult

object RepositoryHelper {

    fun <T: Any> handleSuccess(data: T): DataResult<T> =
        DataResult.Success(data)

    fun handleError(errorHandler: ErrorHandlerContract, throwable: Throwable) =
        DataResult.Error(errorHandler.handleError(throwable))
}