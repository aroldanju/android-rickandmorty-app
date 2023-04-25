package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity

class LocationSourceEntityMapper: EntityMapper<LocationEntity, LocationLocalEntity>() {

    override fun map(entity: LocationEntity): LocationLocalEntity =
        LocationLocalEntity(
            name = entity.name,
            id = entity.id,
            url = entity.url,
            dimension = entity.dimension ?: ""
        )
}
