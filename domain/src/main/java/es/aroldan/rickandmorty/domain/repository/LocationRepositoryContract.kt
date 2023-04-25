package es.aroldan.rickandmorty.domain.repository

import es.aroldan.rickandmorty.domain.model.DataResult
import kotlinx.coroutines.flow.Flow
import es.aroldan.rickandmorty.domain.model.Location

interface LocationRepositoryContract {

    suspend fun fetchLocation(locationId: Int): Flow<DataResult<Location>>
}
