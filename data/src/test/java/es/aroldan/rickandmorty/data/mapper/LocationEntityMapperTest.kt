package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.domain.model.Location
import org.junit.Assert
import org.junit.Test

class LocationEntityMapperTest {

    private val locationEntityMapperTested: LocationEntityMapper = LocationEntityMapper()

    @Test
    fun `transform location entity to location domain model`() {
        val input = listOf(
            LocationEntity(id = 0, name = "Name #0", url = "https://url/0", dimension = "unknown"),
            LocationEntity(id = null, name = "Name #1", url = "https://url/1", dimension = "unknown"),
            LocationEntity(id = 2, name = "Name #2", url = "https://url/2", dimension = ""),
        )

        val expected = listOf(
            Location(name = "Name #0", dimension = "unknown"),
            Location(name = "Name #1", dimension = "unknown"),
            Location(name = "Name #2", dimension = "")
        )

        val output = locationEntityMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}