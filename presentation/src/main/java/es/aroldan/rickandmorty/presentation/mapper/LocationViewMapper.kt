package es.aroldan.rickandmorty.presentation.mapper

import es.aroldan.rickandmorty.domain.model.Location
import es.aroldan.rickandmorty.presentation.model.LocationView

class LocationViewMapper : ViewMapper<Location, LocationView>() {

    override fun map(entity: Location): LocationView =
        LocationView(
            name = entity.name,
            dimension = entity.dimension
        )

    /*
    override fun reverseMap(entity: LocationView): Location =
        Location(
            name = entity.name,
            dimension = entity.dimension
        )

     */
}
