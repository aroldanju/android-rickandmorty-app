package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.remote.model.CharacterEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.domain.model.Character
import org.junit.Assert
import org.junit.Test

class CharacterEntityMapperTest {

    private val characterEntityMapperTested: CharacterEntityMapper = CharacterEntityMapper()

    private fun provideLocationEntity(id: Int = 0, name: String = "Location", url: String = "url/0"): LocationEntity =
        LocationEntity(id = id, name = name, url = url)

    @Test
    fun `transform character entity to character domain`() {
        val input = listOf(
            CharacterEntity(id = 0, name = "Name #1", url = "http://url", location = provideLocationEntity(), image = "Image"),
            CharacterEntity(id = 1, name = "Name #2", url = "http://url", location = provideLocationEntity(url = "url/1"), image = null),
            CharacterEntity(id = 2, name = "Name #3", url = "http://url/image", location = provideLocationEntity(url = "url/url"), image = "Image"),
            CharacterEntity(id = 3, name = "Name #4", url = "http://url", location = provideLocationEntity(url = ""), image = null),
        )

        val expected = listOf(
            Character(id = 0, name = "Name #1", avatar = "Image", 0, isFavourite = false),
            Character(id = 1, name = "Name #2", avatar = null, 1, isFavourite = false),
            Character(id = 2, name = "Name #3", avatar = "Image", null, isFavourite = false),
            Character(id = 3, name = "Name #4", avatar = null, locationId = null, isFavourite = false)
        )

        val output = characterEntityMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}
