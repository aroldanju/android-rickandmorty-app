package es.aroldan.rickandmorty.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.aroldan.rickandmorty.data.datasource.local.model.CharacterLocalEntity

@Dao
interface CharactersDao {

    @Query("SELECT * FROM characters WHERE page=:page")
    fun getCharactersPage(page: Int): List<CharacterLocalEntity>

    @Query("SELECT * FROM characters WHERE id=:id")
    fun getCharacterById(id: Int): CharacterLocalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharacterLocalEntity)

    @Query("SELECT * FROM characters WHERE is_favorite=true")
    fun getFavouriteCharacters(): List<CharacterLocalEntity>
}
