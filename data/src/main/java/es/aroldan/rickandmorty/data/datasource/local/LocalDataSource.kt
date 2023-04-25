package es.aroldan.rickandmorty.data.datasource.local

import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.repository.datasource.LocalDataSourceContract
import es.aroldan.rickandmorty.data.service.DatabaseService

class LocalDataSource(private val databaseService: DatabaseService): LocalDataSourceContract {

    override suspend fun getCharactersPage(page: Int): List<CharacterLocalEntity> =
        databaseService.getCharactersDao().getCharactersPage(page)

    override suspend fun getCharacter(characterId: Int): CharacterLocalEntity? =
        databaseService.getCharactersDao().getCharacterById(characterId)

    override suspend fun saveCharacter(character: CharacterLocalEntity) =
        databaseService.getCharactersDao().insertCharacter(character)

    override suspend fun getFavouriteCharacters(): List<CharacterLocalEntity> =
        databaseService.getCharactersDao().getFavouriteCharacters()

    override suspend fun fetchLocation(locationId: Int): LocationLocalEntity? =
        databaseService.getLocationsDao().getLocationById(locationId)

    override suspend fun saveLocation(location: LocationLocalEntity) =
        databaseService.getLocationsDao().insertLocation(location)
}
