package es.aroldan.rickandmorty.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class PageInfoEntity(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String? = null,
    @SerializedName("prev") val prev: String? = null
)
