package es.aroldan.rickandmorty.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class CharactersPageEntity(
    @SerializedName("info") val info: PageInfoEntity,
    @SerializedName("results") val results: List<CharacterEntity>
)
