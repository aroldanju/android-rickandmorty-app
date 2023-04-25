package es.aroldan.rickandmorty.data.repository.datasource

import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity

interface LocalDataSourceContract {
    suspend fun getCharactersPage(page: Int): List<CharacterLocalEntity>
    suspend fun getCharacter(characterId: Int): CharacterLocalEntity?
    suspend fun saveCharacter(character: CharacterLocalEntity)
    suspend fun getFavouriteCharacters(): List<CharacterLocalEntity>

    suspend fun fetchLocation(locationId: Int): LocationLocalEntity?
    suspend fun saveLocation(location: LocationLocalEntity)
}
