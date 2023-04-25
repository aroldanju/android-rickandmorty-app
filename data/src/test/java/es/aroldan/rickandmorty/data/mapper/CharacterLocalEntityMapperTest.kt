package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus
import org.junit.Assert
import org.junit.Test

class CharacterLocalEntityMapperTest {

    private val characterLocalEntityMapperTested: CharacterLocalEntityMapper = CharacterLocalEntityMapper()

    private fun provideLocationLocalEntity(id: Int = 0, name: String = "Location", url: String = "Url", dimension: String = "unknown"): LocationLocalEntity =
        LocationLocalEntity(id = id, name = name, url = url, dimension = dimension)

    @Test
    fun `transform character entity to local character model`() {
        val input = listOf(
            CharacterLocalEntity(id = 0, name = "Name #1", url = "http://url", image = "Image", page = 1, isFavorite = false, location = provideLocationLocalEntity(), status = "alive"),
            CharacterLocalEntity(id = 1, name = "Name #2", url = "http://url/1", image = "Image", page = null, isFavorite = true, location = provideLocationLocalEntity(url = "url/1"), status = "DEAD"),
            CharacterLocalEntity(id = 2, name = "Name #3", url = "http://url", image = null, page = null, isFavorite = false, location = provideLocationLocalEntity(), status = "??"),
            CharacterLocalEntity(id = 3, name = "Name #4", url = "http://url", image = null, page = null, isFavorite = true, location = provideLocationLocalEntity(url = "url/location"), status = ""),
        )

        val expected = listOf(
            Character(id = 0, name = "Name #1", avatar = "Image", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE),
            Character(id = 1, name = "Name #2", avatar = "Image", locationId = 1, isFavourite = true, status = CharacterStatus.DEAD),
            Character(id = 2, name = "Name #3", avatar = null, locationId = null, isFavourite = false, status = CharacterStatus.UNKNOWN),
            Character(id = 3, name = "Name #4", avatar = null, locationId = null, isFavourite = true, status = CharacterStatus.UNKNOWN),
        )

        val output = characterLocalEntityMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}