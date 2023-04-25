package es.aroldan.rickandmorty.presentation.mapper

import androidx.annotation.StringRes
import es.aroldan.rickandmorty.domain.model.Character
import es.aroldan.rickandmorty.domain.model.CharacterStatus
import es.aroldan.rickandmorty.presentation.R
import es.aroldan.rickandmorty.presentation.model.CharacterView

class CharacterViewMapper : ViewMapper<Character, CharacterView>() {

    override fun map(entity: Character): CharacterView =
        CharacterView(
            id = entity.id,
            name = entity.name,
            avatar = entity.avatar,
            locationId = entity.locationId,
            isFavourite = entity.isFavourite,
            statusIndicatorColor = mapStatusIndicatorColor(entity.status),
            status = mapStatus(entity.status)
        )

    private fun mapStatusIndicatorColor(status: CharacterStatus): Int {
        return when (status) {
            CharacterStatus.ALIVE -> {
                R.color.status_indicator_alive
            }
            CharacterStatus.DEAD -> {
                R.color.status_indicator_dead
            }
            else -> {
                R.color.status_indicator_unknown
            }
        }
    }

    @StringRes
    private fun mapStatus(status: CharacterStatus): Int {
        return when (status) {
            CharacterStatus.ALIVE -> R.string.status_alive
            CharacterStatus.DEAD -> R.string.status_dead
            else -> R.string.status_unknown
        }
    }

    /*
    override fun reverseMap(entity: CharacterView): Character =
        Character(
            id = entity.id,
            name = entity.name,
            avatar = entity.avatar,
            locationId = entity.locationId,
            isFavourite = entity.isFavourite,
            status = reverseMapStatusIndicatorColor()
        )
    */
}
