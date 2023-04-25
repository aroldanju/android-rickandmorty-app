package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetFavouriteCharactersUseCase(private val characterRepository: CharacterRepositoryContract,
                                    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): Flow<DataResult<List<Character>>> =
        characterRepository.fetchFavouriteCharacters()
            .flowOn(dispatcher)
}
