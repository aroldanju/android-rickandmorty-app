package es.aroldan.rickandmorty.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class CharacterEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("location") val location: LocationEntity,
    @SerializedName("image") val image: String?,
    @SerializedName("status") val status: String? = "unknown"
)
