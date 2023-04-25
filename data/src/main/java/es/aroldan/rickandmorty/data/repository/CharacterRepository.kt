package es.aroldan.rickandmorty.data.repository

import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.CharactersPageEntity
import es.aroldan.rickandmorty.data.mapper.CharacterEntityMapper
import es.aroldan.rickandmorty.data.mapper.CharacterLocalEntityMapper
import es.aroldan.rickandmorty.data.mapper.CharacterSourceEntityMapper
import es.aroldan.rickandmorty.data.repository.datasource.LocalDataSourceContract
import es.aroldan.rickandmorty.data.repository.datasource.RemoteDataSourceContract
import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.helper.RepositoryHelper
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CharacterRepository(private val remoteDataSource: RemoteDataSourceContract,
                          private val localDataSource: LocalDataSourceContract,
                          private val characterEntityMapper: CharacterEntityMapper,
                          private val characterLocalEntityMapper: CharacterLocalEntityMapper,
                          private val characterSourceEntityMapper: CharacterSourceEntityMapper,
                          private val errorHandler: ErrorHandlerContract
): CharacterRepositoryContract {

    override suspend fun fetchCharacters(page: Int): Flow<DataResult<List<Character>>> =
        flow {
            val localResult = fetchCharactersFromLocal(page)
            if (localResult.isNotEmpty()) {
                emit(RepositoryHelper.handleSuccess(characterLocalEntityMapper.map(localResult)))
            }

            fetchCharactersFromRemote(page).also {
                storeCharactersPage(page, it)
                emit(RepositoryHelper.handleSuccess(
                    characterLocalEntityMapper.map(fetchCharactersFromLocal(page)))
                )
            }
        }
        .catch { exception ->
            emit(RepositoryHelper.handleError(errorHandler, exception))
        }

    private suspend fun storeCharactersPage(page: Int, characters: CharactersPageEntity) {
        characters.results.forEach { characterEntity ->
            val localCharacter = fetchCharacterFromLocal(characterEntity.id)
            val isFavourite = localCharacter?.isFavorite ?: false

            localDataSource.saveCharacter(characterSourceEntityMapper.map(characterEntity)
                .copy(page = page, isFavorite = isFavourite)
            )
        }
    }

    override suspend fun fetchCharacter(characterId: Int): Flow<DataResult<Character>> =
        flow {
            val characterEntity = fetchCharacterFromLocal(characterId)
            characterEntity?.let {
                emit(DataResult.Success(characterLocalEntityMapper.map(it)))
            }
        }

    override suspend fun setFavouriteCharacter(characterId: Int,
                                               value: Boolean
    ): Flow<DataResult<Character>> = flow {
        val localCharacter = fetchCharacterFromLocal(characterId)
        val updatedLocalCharacter = localCharacter!!.copy(isFavorite = value)
        localDataSource.saveCharacter(updatedLocalCharacter)
        emit(RepositoryHelper.handleSuccess(characterLocalEntityMapper.map(updatedLocalCharacter)))
    }.catch { emit(RepositoryHelper.handleError(errorHandler, it)) }

    override suspend fun fetchFavouriteCharacters(): Flow<DataResult<List<Character>>> =
        flow {
            emit(RepositoryHelper.handleSuccess(characterLocalEntityMapper.map(localDataSource.getFavouriteCharacters())))
        }.catch { emit(RepositoryHelper.handleError(errorHandler, it)) }

    private suspend fun fetchCharactersFromRemote(page: Int): CharactersPageEntity =
        remoteDataSource.fetchCharacters(page)

    private suspend fun fetchCharactersFromLocal(page: Int): List<CharacterLocalEntity> =
        localDataSource.getCharactersPage(page)

    private suspend fun fetchCharacterFromLocal(characterId: Int): CharacterLocalEntity? =
        localDataSource.getCharacter(characterId)
}