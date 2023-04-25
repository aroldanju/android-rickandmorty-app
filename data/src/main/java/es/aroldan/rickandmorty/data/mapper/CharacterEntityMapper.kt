package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.remote.model.CharacterEntity
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus

class CharacterEntityMapper: EntityMapper<CharacterEntity, Character>() {

    override fun map(entity: CharacterEntity): Character =
        Character(
            id = entity.id,
            name = entity.name,
            avatar = entity.image,
            locationId = entity.location.url.substring(entity.location.url.lastIndexOf("/") + 1).toIntOrNull(),
            isFavourite = false,
            status = mapStatus(entity)
        )

    private fun mapStatus(entity: CharacterEntity): CharacterStatus {
        return when (entity.status?.lowercase()) {
            "alive" -> {
                CharacterStatus.ALIVE
            }
            "dead" -> {
                CharacterStatus.DEAD
            }
            else -> {
                CharacterStatus.UNKNOWN
            }
        }
    }
}
