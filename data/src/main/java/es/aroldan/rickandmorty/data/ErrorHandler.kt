package es.aroldan.rickandmorty.data

import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.model.DefinedError
import java.net.UnknownHostException

class ErrorHandler : ErrorHandlerContract {

    override fun handleError(error: Throwable): DefinedError =
        when (error) {
            is UnknownHostException -> {
                DefinedError.NoInternetConnection
            }
            else -> {
                DefinedError.Unknown(error)
            }
        }
}