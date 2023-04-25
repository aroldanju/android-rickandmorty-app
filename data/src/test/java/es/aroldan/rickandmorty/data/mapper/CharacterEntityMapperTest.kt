package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.remote.model.CharacterEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus
import org.junit.Assert
import org.junit.Test

class CharacterEntityMapperTest {

    private val characterEntityMapperTested: CharacterEntityMapper = CharacterEntityMapper()

    private fun provideLocationEntity(id: Int = 0, name: String = "Location", url: String = "url/0"): LocationEntity =
        LocationEntity(id = id, name = name, url = url)

    @Test
    fun `transform character entity to character domain`() {
        val input = listOf(
            CharacterEntity(id = 0, name = "Name #1", url = "http://url", location = provideLocationEntity(), image = "Image", status = "ALIVE"),
            CharacterEntity(id = 1, name = "Name #2", url = "http://url", location = provideLocationEntity(url = "url/1"), image = null, status = "Dead"),
            CharacterEntity(id = 2, name = "Name #3", url = "http://url/image", location = provideLocationEntity(url = "url/url"), image = "Image", status = "unknown"),
            CharacterEntity(id = 3, name = "Name #4", url = "http://url", location = provideLocationEntity(url = ""), image = null, status = null),
        )

        val expected = listOf(
            Character(id = 0, name = "Name #1", avatar = "Image", 0, isFavourite = false, status = CharacterStatus.ALIVE),
            Character(id = 1, name = "Name #2", avatar = null, 1, isFavourite = false, status = CharacterStatus.DEAD),
            Character(id = 2, name = "Name #3", avatar = "Image", null, isFavourite = false, status = CharacterStatus.UNKNOWN),
            Character(id = 3, name = "Name #4", avatar = null, locationId = null, isFavourite = false, status = CharacterStatus.UNKNOWN)
        )

        val output = characterEntityMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}
