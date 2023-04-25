package es.aroldan.rickandmorty.presentation.mapper

import es.aroldan.rickandmorty.domain.model.Location
import es.aroldan.rickandmorty.presentation.model.LocationView
import org.junit.Assert
import org.junit.Test

class LocationViewMapperTest {

    private val locationViewMapperTested: LocationViewMapper = LocationViewMapper()

    @Test
    fun `transform location domain model to location view`() {
        val input = listOf(
            Location(name = "Location", dimension = "unknown"),
            Location(name = "Location 2", dimension = "Dimension"),
            Location(name = "Location 3", dimension = "")
        )

        val expected = listOf(
            LocationView(name = "Location", dimension = "unknown"),
            LocationView(name = "Location 2", dimension = "Dimension"),
            LocationView(name = "Location 3", dimension = "")
        )

        val output = locationViewMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}