package es.aroldan.rickandmorty.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import es.aroldan.rickandmorty.presentation.mapper.CharacterViewMapper
import es.aroldan.rickandmorty.presentation.mapper.LocationViewMapper

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideCharacterViewMapper(): CharacterViewMapper =
        CharacterViewMapper()

    @Provides
    fun provideLocationViewMapper(): LocationViewMapper =
        LocationViewMapper()
}