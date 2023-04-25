package es.aroldan.rickandmorty.data.datasource.local.converter

import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import org.junit.Assert
import org.junit.Test

class DataConverterTest {

    private val dataConverterTested: DataConverter = DataConverter()

    @Test
    fun `convert location list to string`() {
        val input = LocationEntity(id = 0, name = "Location 0", url = "location/0", dimension = "unknown")

        val expected = "{\"id\":0,\"name\":\"Location 0\",\"url\":\"location/0\",\"dimension\":\"unknown\"}"

        val output = dataConverterTested.fromLocation(input)

        Assert.assertEquals(expected, output)
    }

    @Test
    fun `convert string to location`() {
        val input = "{\"id\":0,\"name\":\"Location 0\",\"url\":\"location/0\",\"dimension\":\"unknown\"}"

        val expected = LocationEntity(id = 0, name = "Location 0", url = "location/0", dimension = "unknown")

        val output = dataConverterTested.toLocation(input)

        Assert.assertEquals(expected, output)
    }

    @Test
    fun `convert int list to string`() {
        val input = listOf(0, 1, 2, 3, 4)

        val expected = "[0,1,2,3,4]"

        val output = dataConverterTested.fromIntList(input)

        Assert.assertEquals(expected, output)
    }

    @Test
    fun `convert string to int list`() {
        val input = "[0,1,2,3,4]"

        val expected = listOf(0, 1, 2, 3, 4)

        val output = dataConverterTested.toIntList(input)

        Assert.assertEquals(expected, output)
    }


    @Test
    fun `convert string list to string`() {
        val input = listOf("string", "another string", "one more")

        val expected = "[\"string\",\"another string\",\"one more\"]"

        val output = dataConverterTested.fromStringList(input)

        Assert.assertEquals(expected, output)
    }

    @Test
    fun `convert string to string list`() {
        val input = "[\"string\",\"another string\",\"one more\"]"

        val expected = listOf("string", "another string", "one more")

        val output = dataConverterTested.toStringList(input)

        Assert.assertEquals(expected, output)
    }
}