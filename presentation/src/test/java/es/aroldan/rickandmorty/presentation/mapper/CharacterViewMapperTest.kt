package es.aroldan.rickandmorty.presentation.mapper

import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.presentation.model.CharacterView
import org.junit.Assert
import org.junit.Test

class CharacterViewMapperTest {

    private val characterViewMapperTested: CharacterViewMapper = CharacterViewMapper()

    @Test
    fun `transform character domain model to character view`() {
        val input = listOf(
            Character(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false),
            Character(id = 1, name = "Name 1", avatar = "Avatar 1", locationId = null, isFavourite = true),
            Character(id = 2, name = "Name 2", avatar = null, locationId = 1, isFavourite = false)
        )

        val expected = listOf(
            CharacterView(id = 0, name = "Name", avatar = "Avatar", locationId = null, isFavourite = false),
            CharacterView(id = 1, name = "Name 1", avatar = "Avatar 1", locationId = null, isFavourite = true),
            CharacterView(id = 2, name = "Name 2", avatar = null, locationId = 1, isFavourite = false)
        )

        val output = characterViewMapperTested.map(input)

        Assert.assertEquals(
            expected, output
        )
    }
}