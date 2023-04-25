package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus

class CharacterLocalEntityMapper: EntityMapper<CharacterLocalEntity, Character>() {

    override fun map(entity: CharacterLocalEntity): Character =
        Character(
            id = entity.id,
            name = entity.name,
            avatar = entity.image,
            locationId = entity.location.url.substring(entity.location.url.lastIndexOf("/") + 1).toIntOrNull(),
            isFavourite = entity.isFavorite,
            status = mapStatus(entity)
        )

    private fun mapStatus(entity: CharacterLocalEntity): CharacterStatus {
        return when (entity.status.lowercase()) {
            "alive" -> CharacterStatus.ALIVE
            "dead" -> CharacterStatus.DEAD
            else -> CharacterStatus.UNKNOWN
        }
    }
}
