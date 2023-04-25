package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.CharacterEntity

class CharacterSourceEntityMapper(
    private val locationSourceEntityMapper: LocationSourceEntityMapper
): EntityMapper<CharacterEntity, CharacterLocalEntity>() {

    override fun map(entity: CharacterEntity): CharacterLocalEntity =
        CharacterLocalEntity(
            id = entity.id,
            page = null,
            isFavorite = false,
            name = entity.name,
            url = entity.url,
            location = locationSourceEntityMapper.map(entity.location),
            image = entity.image,
            status = entity.status ?: "unknown"
        )
}