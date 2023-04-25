package es.aroldan.rickandmorty.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class LocationEntity(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("dimension") val dimension: String? = "unknown"
)
