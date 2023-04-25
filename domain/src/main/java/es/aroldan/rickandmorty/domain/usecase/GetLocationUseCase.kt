package es.aroldan.rickandmorty.domain.usecase

import es.aroldan.rickandmorty.domain.repository.LocationRepositoryContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn

class GetLocationUseCase(private val locationRepository: LocationRepositoryContract,
                         private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(locationId: Int) =
        locationRepository.fetchLocation(locationId)
            .flowOn(dispatcher)
}
