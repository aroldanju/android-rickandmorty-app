package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.domain.model.Location

class LocationEntityMapper: EntityMapper<LocationEntity, Location>() {

    override fun map(entity: LocationEntity): Location =
        Location(
            name = entity.name,
            dimension = entity.dimension ?: ""
        )
}
