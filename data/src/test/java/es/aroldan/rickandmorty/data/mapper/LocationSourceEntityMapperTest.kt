package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import org.junit.Assert
import org.junit.Test

class LocationSourceEntityMapperTest {

    private val locationSourceEntityMapperTested: LocationSourceEntityMapper = LocationSourceEntityMapper()

    @Test
    fun `transform location entity to location local entity`() {
        val input = listOf(
            LocationEntity(id = 0, name = "Name #0", url = "https://url/0", dimension = "dimension"),
            LocationEntity(id = null, name = "Name #1", url = "https://url/1", dimension = ""),
            LocationEntity(id = 0, name = "Name #2", url = "https://url/2"),
        )

        val expected = listOf(
            LocationLocalEntity(id = 0, name = "Name #0", url = "https://url/0", dimension = "dimension"),
            LocationLocalEntity(id = null, name = "Name #1", url = "https://url/1", dimension = ""),
            LocationLocalEntity(id = 0, name = "Name #2", url = "https://url/2", dimension = "unknown"),
        )

        val output = locationSourceEntityMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}
