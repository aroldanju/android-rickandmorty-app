package es.aroldan.rickandmorty.domain.repository

import kotlinx.coroutines.flow.Flow
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.DataResult

interface CharacterRepositoryContract {

    suspend fun fetchCharacters(page: Int): Flow<DataResult<List<Character>>>

    suspend fun fetchCharacter(characterId: Int): Flow<DataResult<Character>>

    suspend fun setFavouriteCharacter(characterId: Int, value: Boolean): Flow<DataResult<Character>>

    suspend fun fetchFavouriteCharacters(): Flow<DataResult<List<Character>>>
}
