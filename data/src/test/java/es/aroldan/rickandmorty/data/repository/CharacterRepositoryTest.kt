package es.aroldan.rickandmorty.data.repository

import es.aroldan.rickandmorty.data.datasource.local.LocalDataSource
import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity
import es.aroldan.rickandmorty.data.datasource.local.model.LocationLocalEntity
import es.aroldan.rickandmorty.data.datasource.remote.RemoteDataSource
import es.aroldan.rickandmorty.data.datasource.remote.model.CharacterEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.CharactersPageEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.LocationEntity
import es.aroldan.rickandmorty.data.datasource.remote.model.PageInfoEntity
import es.aroldan.rickandmorty.data.mapper.CharacterEntityMapper
import es.aroldan.rickandmorty.data.mapper.CharacterLocalEntityMapper
import es.aroldan.rickandmorty.data.mapper.CharacterSourceEntityMapper
import es.aroldan.rickandmorty.domain.ErrorHandlerContract
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.DataResult
import es.aroldan.rickandmorty.domain.model.DefinedError
import es.aroldan.rickandmorty.domain.repository.CharacterRepositoryContract
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CharacterRepositoryTest {

    private lateinit var characterRepositoryTested: CharacterRepositoryContract

    @RelaxedMockK private lateinit var errorHandler: ErrorHandlerContract
    @RelaxedMockK private lateinit var remoteDataSource: RemoteDataSource
    @RelaxedMockK private lateinit var localDataSource: LocalDataSource
    @RelaxedMockK private lateinit var characterEntityMapper: CharacterEntityMapper
    @RelaxedMockK private lateinit var characterLocalEntityMapper: CharacterLocalEntityMapper
    @RelaxedMockK private lateinit var characterSourceEntityMapper: CharacterSourceEntityMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        characterRepositoryTested = CharacterRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            characterEntityMapper = characterEntityMapper,
            errorHandler = errorHandler,
            characterLocalEntityMapper = characterLocalEntityMapper,
            characterSourceEntityMapper = characterSourceEntityMapper
        )
    }


    @Test
    fun `given local characters page when fetch characters page then fetch characters page from local data source`() {
        every { characterLocalEntityMapper.map(any() as List<CharacterLocalEntity>) } returns
                listOf(Character(id = 0, name = "Name", avatar = "avatar", locationId = 0, isFavourite = false))

        // Given
        coEvery { localDataSource.getCharactersPage(any()) } returns
                listOf(
                    CharacterLocalEntity(id = 0, name = "Name", url = "url", location = LocationLocalEntity(0, "Name", "url", dimension = "unknown"), image = "image", page = 1, isFavorite = false)
                )

        runBlocking {
            val flow = characterRepositoryTested.fetchCharacters(1)

            Assert.assertEquals(
                DataResult.Success(listOf(Character(name = "Name", id = 0, avatar = "avatar", locationId = 0, isFavourite = false))),
                flow.first()
            )
        }
    }

    @Test
    fun `when fetch characters page then store characters page to local data source`() {
        coEvery { localDataSource.getCharactersPage(any()) } returns emptyList()

        every { characterEntityMapper.map(any() as List<CharacterEntity>) } returns
                listOf(Character(id = 0, name = "Name", avatar = "image", locationId = 0, isFavourite = false))

        coEvery { remoteDataSource.fetchCharacters(any()) } returns
                CharactersPageEntity(
                    info = PageInfoEntity(1, 2, null, null),
                    results = listOf(
                        CharacterEntity(id = 0, name = "Name", url = "Url",
                            location = LocationEntity(0, "Name", "url"),
                            image = "image")
                    )
                )

        coEvery { localDataSource.getCharacter(any()) } returns
                CharacterLocalEntity(id = 0, page = 1, isFavorite = false, name = "Name", url = "url", location = LocationLocalEntity(id = 0, name = "Name", url = "Url", dimension = "unknown"), image = "image")
        every { characterLocalEntityMapper.map(any() as CharacterLocalEntity) } returns
                Character(id = 0, name = "Name", avatar = "image", locationId = 0, isFavourite = false)
        every { characterSourceEntityMapper.map(any() as CharacterEntity) } returns
                CharacterLocalEntity(id = 0, page = 1, isFavorite = false, name = "Name", url = "url", location = LocationLocalEntity(id = 0, name = "Name", url = "Url", dimension = "unknown"), image = "image")

        runBlocking {
            characterRepositoryTested.fetchCharacters(1).collect()

            coVerify {
                localDataSource.saveCharacter(any())
            }
        }
    }

    @Test
    fun `when fetch characters page then fetch characters page from remote data source`() {
        coEvery { localDataSource.getCharactersPage(any()) } returns emptyList()

        every { characterEntityMapper.map(any() as List<CharacterEntity>) } returns
                listOf(Character(id = 0, name = "Name", avatar = "image", locationId = 0, isFavourite = false))

        coEvery { remoteDataSource.fetchCharacters(any()) } returns
                CharactersPageEntity(
                    info = PageInfoEntity(1, 2, null, null),
                    results = listOf(
                        CharacterEntity(id = 0, name = "Name", url = "Url",
                            location = LocationEntity(0, "Name", "url"),
                            image = "image")
                    )
                )

        runBlocking {
            characterRepositoryTested.fetchCharacters(1).collect()
            coVerify { remoteDataSource.fetchCharacters(any()) }
        }
    }

    @Test
    fun `get error when fetching characters page from api`() {
        val error: DefinedError = DefinedError.Unknown(Throwable())

        coEvery { errorHandler.handleError(any()) } answers {
            error
        }

        coEvery {
            remoteDataSource.fetchCharacters(any())
        } throws Throwable()

        runBlocking {
            val flow = characterRepositoryTested.fetchCharacters(1)

            Assert.assertEquals(
                DataResult.Error(error),
                flow.first()
            )
        }
    }

    @Test
    fun `given a local character entity when fetch character then fetch character from local data source`() {

        coEvery { localDataSource.getCharacter(any()) } returns
                CharacterLocalEntity(id = 0, name = "Name", url = "url",
                    location = LocationLocalEntity(0, "Name", "url", dimension = "unknown"),
                    image = "image", page = 1, isFavorite = false)

        runBlocking {
            characterRepositoryTested.fetchCharacter(1).collect()

            coVerify(exactly = 1) {
                localDataSource.getCharacter(any())
            }
        }
    }

}