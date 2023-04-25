package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.domain.model.Location
import org.junit.Assert
import org.junit.Test

class LocationLocalEntityMapperTest {

    private val locationLocalEntityMapperTested: LocationLocalEntityMapper = LocationLocalEntityMapper()

    private fun provideLocationLocalEntity(id: Int = 0, name: String = "Location", url: String = "Url", dimension: String = "unknown"): LocationLocalEntity =
        LocationLocalEntity(id = id, name = name, url = url, dimension = dimension)

    @Test
    fun `transform location entity to local location model`() {
        val input = listOf(
            provideLocationLocalEntity(0, "loc 0", "loc/0"),
            provideLocationLocalEntity(1, "loc 1", "loc/1", "dimension"),
            provideLocationLocalEntity(2, "loc 2", "loc/2", ""),
        )

        val expected = listOf(
            Location("loc 0", "unknown"),
            Location("loc 1", "dimension"),
            Location("loc 2", "")
        )

        val output = locationLocalEntityMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}