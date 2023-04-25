package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetCharactersPageUseCase(private val characterRepository: CharacterRepositoryContract,
                               private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(page: Int): Flow<DataResult<List<Character>>> =
        characterRepository.fetchCharacters(page)
            .flowOn(dispatcher)
}
