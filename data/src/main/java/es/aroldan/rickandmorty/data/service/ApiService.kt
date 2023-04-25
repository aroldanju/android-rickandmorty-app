package es.aroldan.rickandmorty.data.service

import es.aroldan.rickandmorty.data.datasource.remote.model.CharactersPageEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("character/")
    suspend fun fetchCharacters(@Query("page") page: Int): CharactersPageEntity

    @GET("location/{locationId}")
    suspend fun fetchLocation(@Path("locationId") locationId: Int): LocationEntity
}
