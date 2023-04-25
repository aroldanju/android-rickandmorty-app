package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.domain.model.Location

class LocationLocalEntityMapper: EntityMapper<LocationLocalEntity, Location>() {

    override fun map(entity: LocationLocalEntity): Location =
        Location(
            name = entity.name,
            dimension = entity.dimension
        )
}
