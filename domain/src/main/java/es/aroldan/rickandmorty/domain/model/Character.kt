package es.aroldan.rickandmorty.domain.model

data class Character(
    val id: Int,
    val name: String,
    val avatar: String?,
    val locationId: Int?,
    val isFavourite: Boolean,
    val status: CharacterStatus
)
