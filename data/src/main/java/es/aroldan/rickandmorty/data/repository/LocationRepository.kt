package es.aroldan.rickandmorty.data.repository

import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.data.mapper.LocationEntityMapper
import es.aroldan.rickandmorty.data.mapper.LocationLocalEntityMapper
import es.aroldan.rickandmorty.data.mapper.LocationSourceEntityMapper
import es.aroldan.rickandmorty.data.repository.datasource.LocalDataSourceContract
import es.aroldan.rickandmorty.data.repository.datasource.RemoteDataSourceContract
import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.helper.RepositoryHelper
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.model.Location
import es.aroldan.rickandmorty.domain.repository.LocationRepositoryContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LocationRepository(private val remoteDataSource: RemoteDataSourceContract,
                         private val localDataSource: LocalDataSourceContract,
                         private val locationEntityMapper: LocationEntityMapper,
                         private val locationLocalEntityMapper: LocationLocalEntityMapper,
                         private val locationSourceEntityMapper: LocationSourceEntityMapper,
                         private val errorHandler: ErrorHandlerContract
): LocationRepositoryContract {

    override suspend fun fetchLocation(locationId: Int): Flow<DataResult<Location>> =
        flow {
            val localResult = fetchLocationFromLocal(locationId)
            localResult?.let {
                emit(RepositoryHelper.handleSuccess(locationLocalEntityMapper.map(it)))
            }

            val remoteResult = fetchLocationFromRemote(locationId).also { locationEntity ->
                localDataSource.saveLocation(locationSourceEntityMapper.map(locationEntity))
            }

            emit(RepositoryHelper.handleSuccess(locationEntityMapper.map(remoteResult)))
        }
            .catch { exception ->
                emit(RepositoryHelper.handleError(errorHandler, exception))
            }

    private suspend fun fetchLocationFromRemote(locationId: Int): LocationEntity =
        remoteDataSource.fetchLocation(locationId)

    private suspend fun fetchLocationFromLocal(locationId: Int): LocationLocalEntity? =
        localDataSource.fetchLocation(locationId)
}