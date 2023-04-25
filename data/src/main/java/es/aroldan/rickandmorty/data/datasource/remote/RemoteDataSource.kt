package es.aroldan.rickandmorty.data.datasource.remote

import es.aroldan.rickandmorty.data.datasource.remote.model.CharactersPageEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.data.repository.datasource.RemoteDataSourceContract
import es.aroldan.rickandmorty.data.service.ApiService

class RemoteDataSource(private val apiService: ApiService): RemoteDataSourceContract {

    override suspend fun fetchCharacters(page: Int): CharactersPageEntity =
        apiService.fetchCharacters(page)

    override suspend fun fetchLocation(locationId: Int): LocationEntity =
        apiService.fetchLocation(locationId)
}
