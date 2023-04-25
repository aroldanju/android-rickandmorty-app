package es.aroldan.rickandmorty.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.aroldan.rickandmorty.data.ErrorHandler
import es.aroldan.rickandmorty.data.datasource.local.LocalDataSource
import es.aroldan.rickandmorty.data.datasource.remote.RemoteDataSource
import es.aroldan.rickandmorty.data.mapper.CharacterEntityMapper
import es.aroldan.rickandmorty.data.mapper.CharacterLocalEntityMapper
import es.aroldan.rickandmorty.data.mapper.CharacterSourceEntityMapper
import es.aroldan.rickandmorty.data.mapper.LocationEntityMapper
import es.aroldan.rickandmorty.data.mapper.LocationLocalEntityMapper
import es.aroldan.rickandmorty.data.mapper.LocationSourceEntityMapper
import es.aroldan.rickandmorty.data.repository.CharacterRepository
import es.aroldan.rickandmorty.data.repository.LocationRepository
import es.aroldan.rickandmorty.data.repository.datasource.LocalDataSourceContract
import es.aroldan.rickandmorty.data.repository.datasource.RemoteDataSourceContract
import es.aroldan.rickandmorty.data.service.ApiService
import es.aroldan.rickandmorty.data.service.DatabaseService
import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
import es.aroldan.rickandmorty.domain.repository.LocationRepositoryContract

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideErrorHandler(): ErrorHandlerContract =
        ErrorHandler()

    @Provides
    fun provideLocationEntityMapper(): LocationEntityMapper =
        LocationEntityMapper()

    @Provides
    fun provideLocationLocalEntityMapper(): LocationLocalEntityMapper =
        LocationLocalEntityMapper()

    @Provides
    fun provideCharacterEntityMapper(): CharacterEntityMapper =
        CharacterEntityMapper()

    @Provides
    fun provideCharacterLocalEntityMapper(): CharacterLocalEntityMapper =
        CharacterLocalEntityMapper()

    @Provides
    fun provideLocationSourceEntityMapper(): LocationSourceEntityMapper =
        LocationSourceEntityMapper()

    @Provides
    fun provideCharacterSourceEntityMapper(locationLocalEntityMapper: LocationSourceEntityMapper
    ): CharacterSourceEntityMapper =
        CharacterSourceEntityMapper(locationLocalEntityMapper)

    @Provides
    fun provideLocalDataSource(databaseService: DatabaseService): LocalDataSourceContract =
        LocalDataSource(databaseService)

    @Provides
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSourceContract =
        RemoteDataSource(apiService)

    @Provides
    fun provideLocationRepository(remoteDataSource: RemoteDataSourceContract,
                                  localDataSource: LocalDataSourceContract,
                                  locationEntityMapper: LocationEntityMapper,
                                  locationLocalEntityMapper: LocationLocalEntityMapper,
                                  locationSourceEntityMapper: LocationSourceEntityMapper,
                                  errorHandler: ErrorHandlerContract
    ): LocationRepositoryContract =
        LocationRepository(remoteDataSource, localDataSource, locationEntityMapper,
            locationLocalEntityMapper, locationSourceEntityMapper, errorHandler)

    @Provides
    fun provideCharacterRepository(remoteDataSource: RemoteDataSourceContract,
                                   localDataSource: LocalDataSourceContract,
                                   characterEntityMapper: CharacterEntityMapper,
                                   characterSourceEntityMapper: CharacterSourceEntityMapper,
                                   characterLocalEntityMapper: CharacterLocalEntityMapper,
                                   errorHandler: ErrorHandlerContract
    ): CharacterRepositoryContract =
        CharacterRepository(remoteDataSource, localDataSource,  characterEntityMapper,
            characterLocalEntityMapper, characterSourceEntityMapper, errorHandler)
}