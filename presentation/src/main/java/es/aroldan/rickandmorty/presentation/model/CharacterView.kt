package es.aroldan.rickandmorty.presentation.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class CharacterView(
    val id: Int,
    val name: String,
    val avatar: String?,
    val locationId: Int?,
    val isFavourite: Boolean,
    @StringRes val status: Int,
    @ColorRes val statusIndicatorColor: Int
)
