package es.aroldan.rickandmorty.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
import es.aroldan.rickandmorty.domain.repository.LocationRepositoryContract
import es.aroldan.rickandmorty.domain.usecase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher =
        Dispatchers.IO

    @Provides
    fun provideGetCharactersPageUseCase(characterRepository: CharacterRepositoryContract,
                                        dispatcher: CoroutineDispatcher
    ): GetCharactersPageUseCase =
        GetCharactersPageUseCase(characterRepository, dispatcher)

    @Provides
    fun provideGetLocationUseCase(locationRepository: LocationRepositoryContract,
                                  dispatcher: CoroutineDispatcher
    ): GetLocationUseCase =
        GetLocationUseCase(locationRepository, dispatcher)

    @Provides
    fun provideGetCharacterUseCase(characterRepository: CharacterRepositoryContract,
                                   dispatcher: CoroutineDispatcher
    ): GetCharacterUseCase =
        GetCharacterUseCase(characterRepository, dispatcher)

    @Provides
    fun provideToggleFavouriteCharacterUseCase(characterRepository: CharacterRepositoryContract,
                                               dispatcher: CoroutineDispatcher
    ): ToggleFavouriteCharacterUseCase =
        ToggleFavouriteCharacterUseCase(characterRepository, dispatcher)

    @Provides
    fun provideGetFavouriteCharactersUseCase(characterRepository: CharacterRepositoryContract,
                                             dispatcher: CoroutineDispatcher
    ): GetFavouriteCharactersUseCase =
        GetFavouriteCharactersUseCase(characterRepository, dispatcher)

}
