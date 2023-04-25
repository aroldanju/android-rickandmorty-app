package es.aroldan.rickandmorty.domain

import es.aroldan.rickandmorty.domain.model.DefinedError

interface ErrorHandlerContract {

    fun handleError(error: Throwable): DefinedError
}
