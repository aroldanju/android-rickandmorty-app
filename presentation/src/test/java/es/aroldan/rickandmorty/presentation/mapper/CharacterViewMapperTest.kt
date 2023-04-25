package es.aroldan.rickandmorty.presentation.mapper

import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus
import es.aroldan.rickandmorty.presentation.model.CharacterView
import org.junit.Assert
import org.junit.Test
import es.aroldan.rickandmorty.presentation.R

class CharacterViewMapperTest {

    private val characterViewMapperTested: CharacterViewMapper = CharacterViewMapper()

    @Test
    fun `transform character domain model to character view`() {
        val input = listOf(
            Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = CharacterStatus.ALIVE),
            Character(id = 1, name = "Name 1", avatar = "Avatar 1", locationId = null, isFavourite = true, status = CharacterStatus.DEAD),
            Character(id = 2, name = "Name 2", avatar = null, locationId = 1, isFavourite = false, status = CharacterStatus.UNKNOWN)
        )

        val expected = listOf(
            CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false, status = R.string.status_alive, statusIndicatorColor = R.color.status_indicator_alive),
            CharacterView(id = 1, name = "Name 1", avatar = "Avatar 1", locationId = null, isFavourite = true, status = R.string.status_dead, statusIndicatorColor = R.color.status_indicator_dead),
            CharacterView(id = 2, name = "Name 2", avatar = null, locationId = 1, isFavourite = false, status = R.string.status_unknown, statusIndicatorColor = R.color.status_indicator_unknown)
        )

        val output = characterViewMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}