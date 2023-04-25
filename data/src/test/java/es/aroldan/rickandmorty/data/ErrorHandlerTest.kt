package es.aroldan.rickandmorty.data

import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.model.DefinedError
import org.junit.Assert
import org.junit.Test
import java.net.UnknownHostException

class ErrorHandlerTest {

    private val errorHandlerTested: ErrorHandlerContract = ErrorHandler()

    @Test
    fun `given unknown host exception when handle error then return no internet connection`() {
        val unknownHostException: Throwable = UnknownHostException()

        val error = errorHandlerTested.handleError(unknownHostException)

        Assert.assertEquals(
            error, DefinedError.NoInternetConnection
        )
    }

    @Test
    fun `given undetermined error when handle error then return unknown error`() {
        val undeterminedError = Throwable()

        val error = errorHandlerTested.handleError(undeterminedError)

        Assert.assertEquals(
            error, DefinedError.Unknown(undeterminedError)
        )
    }
}