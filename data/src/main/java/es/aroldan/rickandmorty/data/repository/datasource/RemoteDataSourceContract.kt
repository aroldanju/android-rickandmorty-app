package es.aroldan.rickandmorty.data.repository.datasource

import es.aroldan.rickandmorty.data.datasource.remote.model.CharactersPageEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity

interface RemoteDataSourceContract {
    suspend fun fetchCharacters(page: Int): CharactersPageEntity
    suspend fun fetchLocation(locationId: Int): LocationEntity
}