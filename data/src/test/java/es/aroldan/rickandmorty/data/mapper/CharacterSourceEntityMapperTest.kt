package es.aroldan.rickandmorty.data.mapper

import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.CharacterEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CharacterSourceEntityMapperTest {

    private lateinit var characterSourceEntityMapperTested: CharacterSourceEntityMapper

    @RelaxedMockK private lateinit var locationSourceEntityMapper: LocationSourceEntityMapper

    private fun provideLocationEntity(id: Int = 0, name: String = "Location", url: String = "Url"): LocationEntity =
        LocationEntity(id = id, name = name, url = url)

    private fun provideLocationLocalEntity(id: Int = 0, name: String = "Location", url: String = "Url", dimension: String = "unknown"): LocationLocalEntity =
        LocationLocalEntity(id = id, name = name, url = url, dimension = dimension)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        characterSourceEntityMapperTested = CharacterSourceEntityMapper(
            locationSourceEntityMapper = locationSourceEntityMapper
        )
    }

    @Test
    fun `transform character entity to character local model`() {
        every { locationSourceEntityMapper.map(any() as LocationEntity) }returns provideLocationLocalEntity()

        val input = listOf(
            CharacterEntity(id = 1, name = "Name #1", url = "https://url/1", location = provideLocationEntity(), image = "image", status = "alive"),
            CharacterEntity(id = 2, name = "Name #2", url = "https://url/2", location = provideLocationEntity(), image = "image", status = "DEAD"),
            CharacterEntity(id = 3, name = "Name #3", url = "https://url/3", location = provideLocationEntity(), image = null, status = "unknown"),
            CharacterEntity(id = 4, name = "Name #4", url = "https://url/4", location = provideLocationEntity(), image = null, status = null)
        )

        val expected = listOf(
            CharacterLocalEntity(id = 1, name = "Name #1", url = "https://url/1", location = provideLocationLocalEntity(), image = "image", status = "alive", page = null, isFavorite = false),
            CharacterLocalEntity(id = 2, name = "Name #2", url = "https://url/2", location = provideLocationLocalEntity(), image = "image", status = "DEAD", page = null, isFavorite = false),
            CharacterLocalEntity(id = 3, name = "Name #3", url = "https://url/3", location = provideLocationLocalEntity(), image = null, status = "unknown", page = null, isFavorite = false),
            CharacterLocalEntity(id = 4, name = "Name #4", url = "https://url/4", location = provideLocationLocalEntity(), image = null, status = "unknown", page = null, isFavorite = false)
        )

        val output = characterSourceEntityMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}