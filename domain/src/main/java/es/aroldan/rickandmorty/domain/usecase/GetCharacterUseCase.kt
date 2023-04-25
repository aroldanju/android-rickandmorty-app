package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetCharacterUseCase(private val characterRepository: CharacterRepositoryContract,
                          private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(characterId: Int): Flow<DataResult<Character>> =
        characterRepository.fetchCharacter(characterId)
            .flowOn(dispatcher)
}
